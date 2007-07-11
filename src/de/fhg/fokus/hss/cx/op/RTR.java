/*
  *  Copyright (C) 2004-2007 FhG Fokus
  *
  * This file is part of Open IMS Core - an open source IMS CSCFs & HSS
  * implementation
  *
  * Open IMS Core is free software; you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation; either version 2 of the License, or
  * (at your option) any later version.
  *
  * For a license to use the Open IMS Core software under conditions
  * other than those described here, or to purchase support for this
  * software, please contact Fraunhofer FOKUS by e-mail at the following
  * addresses:
  *     info@open-ims.org
  *
  * Open IMS Core is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * It has to be noted that this Open Source IMS Core System is not
  * intended to become or act as a product in a commercial context! Its
  * sole purpose is to provide an IMS core reference implementation for
  * IMS technology testing and IMS application prototyping for research
  * purposes, typically performed in IMS test-beds.
  *
  * Users of the Open Source IMS Core System have to be aware that IMS
  * technology may be subject of patents and licence terms, as being
  * specified within the various IMS-related IETF, ITU-T, ETSI, and 3GPP
  * standards. Thus all Open IMS Core users have to take notice of this
  * fact and have to agree to check out carefully before installing,
  * using and extending the Open Source IMS Core System, if related
  * patents and licenses may become applicable to the intended usage
  * context. 
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA  
  * 
  */
package de.fhg.fokus.hss.cx.op;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.CxEvents;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.db.op.DB_Op;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.DiameterStack;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.db.hibernate.*;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class RTR {
	private static Logger logger = Logger.getLogger(RTR.class);
	
	public static void sendRequest(DiameterStack diameterStack, String diameter_name,
		List<IMPU> impuList, List<IMPI> impiList, int reasonCode, String reasonInfo, int grp){
		DiameterPeer diameterPeer = diameterStack.diameterPeer;
		DiameterMessage request = diameterPeer.newRequest(DiameterConstants.Command.RTR, DiameterConstants.Application.Cx);
		request.flagProxiable = true;
		
		// add SessionId
		UtilAVP.addSessionID(request, diameterPeer.FQDN, diameterStack.getNextSessionID());

		// add vendor-specific-application-id & auth-session state
		UtilAVP.addAuthSessionState(request, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(request, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);
		
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (impiList == null || impiList.size() == 0){
				logger.error("IMPI List is NULL or empty! RTR Aborted!");
				return;
			}
			
			IMPI defaultIMPI = impiList.get(0);
			IMSU imsu = IMSU_DAO.get_by_ID(session, defaultIMPI.getId_imsu());
			
			// Preparing the response
			
			// add destination host and realm
			//String destHost = IMSU_DAO.get_Diameter_Name_by_IMSU_ID(session, defaultIMPI.getId_imsu());
			UtilAVP.addDestinationHost(request, diameter_name);
			UtilAVP.addDestinationRealm(request, diameter_name.substring(diameter_name.indexOf('.') + 1));
			// add user name
			UtilAVP.addUserName(request, defaultIMPI.getIdentity());

			switch (reasonCode){
				case CxConstants.RTR_Permanent_Termination:
					if (impuList == null || impuList.size() == 0){
						// 1 - first case: we are deregistering IMPIs with all coresponding IMPUs!
						
						Iterator impiIt = impiList.iterator();
						IMPI impi = null;
						while (impiIt.hasNext()){
							impi = (IMPI) impiIt.next();
							impuList = IMPI_IMPU_DAO.get_all_registered_IMPU_by_IMPI_ID(session, impi.getId());
							IMPU impu = null;
							Iterator impuIt = impuList.iterator();
							while (impuIt.hasNext()){
								impu = (IMPU) impuIt.next();
								int user_state = impu.getUser_state();

								switch (user_state){
									case CxConstants.IMPU_user_state_Registered:
										int reg_cnt = IMPI_IMPU_DAO.get_Registered_IMPU_Count(session, impu.getId());
										if (reg_cnt == 1){
											// set the user_state to Not-Registered
											DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), 
													CxConstants.IMPU_user_state_Not_Registered, true);
											HibernateUtil.commitTransaction();
											// commit the transaction, as the previous updates should
											// be seen by the next DB operations
											
											HibernateUtil.beginTransaction();
											// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs
											int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, impi.getId_imsu());
											if (reg_cnt_for_imsu == 0){
												IMSU_DAO.update(session, impi.getId_imsu(), "", "");
											}
										}
										else{
											// Set the user_state to Not-Registered only on IMPI_IMPU association, 
											//IMPU registration state remains registered
											DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), 
													CxConstants.IMPU_user_state_Not_Registered, false);
										}
										break;
										
									case CxConstants.IMPU_user_state_Unregistered:
										// set the user_state to Not-Registered
										DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), 
												CxConstants.IMPU_user_state_Not_Registered, true);
										HibernateUtil.commitTransaction();
										// commit the transaction, as the previous updates should
										// be seen by the next DB operations
										
										HibernateUtil.beginTransaction();
										// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
										int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, impi.getId_imsu());
										if (reg_cnt_for_imsu == 0){
											IMSU_DAO.update(session, impi.getId_imsu(), "", "");
										}
										break;
								}
							}
						}// while impiList
						
						if (impiList.size() > 1){
							// add Associated Identities (contain the rest of IMPIs that should be de-registered together with the defaultIMPI)
							// remove defaultIMPI
							impiList.remove(0);
							// add only the rest of IMPIs
							UtilAVP.addAsssociatedIdentities(request, impiList);
						}
					}
					else{
						// 2 - second case - we have RTR for one or more IMPUs, impuList != null
						IMPU impu = null;
						Iterator impuIt = impuList.iterator();
						while (impuIt.hasNext()){
							impu = (IMPU) impuIt.next();
							int user_state = impu.getUser_state();

							switch (user_state){
								case CxConstants.IMPU_user_state_Registered:
									int reg_cnt = IMPI_IMPU_DAO.get_Registered_IMPU_Count(session, impu.getId());
									if (reg_cnt == 1){
										// set the user_state to Not-Registered
										DB_Op.setUserState(session, defaultIMPI.getId(), impu.getId_implicit_set(), 
												CxConstants.IMPU_user_state_Not_Registered, true);
										HibernateUtil.commitTransaction();
										// commit the transaction, as the previous updates should
										// be seen by the next DB operations
										
										HibernateUtil.beginTransaction();										
										// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
										int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, defaultIMPI.getId_imsu());
										if (reg_cnt_for_imsu == 0){
											IMSU_DAO.update(session, defaultIMPI.getId_imsu(), "", "");
										}
									}
									else{
										// Set the user_state to Not-Registered only on IMPI_IMPU association, 
										//IMPU registration state remain registered
										DB_Op.setUserState(session, defaultIMPI.getId(), impu.getId_implicit_set(), 
												CxConstants.IMPU_user_state_Not_Registered, false);
									}
									break;
									
								case CxConstants.IMPU_user_state_Unregistered:
									// set the user_state to Not-Registered
									DB_Op.setUserState(session, defaultIMPI.getId(), impu.getId_implicit_set(), 
											CxConstants.IMPU_user_state_Not_Registered, true);
									HibernateUtil.commitTransaction();
									// commit the transaction, as the previous updates should
									// be seen by the next DB operations
									
									HibernateUtil.beginTransaction();
									// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
									int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, defaultIMPI.getId_imsu());
									if (reg_cnt_for_imsu == 0){
										IMSU_DAO.update(session, defaultIMPI.getId_imsu(), "", "");
									}
									break;
							}
							// add public identities to the response
							UtilAVP.addPublicIdentity(request, impu.getIdentity());
						}
					}
					
					break;
					
				case CxConstants.RTR_New_Server_Assigned:
					if (impuList == null || impuList.size() == 0){
						logger.error("IMPU List is NULL or empty! This is not permited to the reason code: " + reasonCode);
						logger.error("RTR Aborted!");
						return;
					}
					
					// the HSS indicates  to the S-CSCF that a new S-CSCF was has been allocated to the IMSU

					// add all the IMSUs to the request
					for (int i = 0; i < impuList.size(); i++){
						IMPU impu = impuList.get(i);
						UtilAVP.addPublicIdentity(request, impu.getIdentity());
					}
					break;
					
				case CxConstants.RTR_Server_Change:
					// the HSS indicates that the deregistration is requested to force the selection of new S-CSCF to assign to the IMPU
					if (impuList == null || impuList.size() == 0){
						// deregister all the registered IMPUs corresponding to the provided IMPIs
						for (int i = 0; i < impiList.size(); i++){
							IMPI impi = impiList.get(i);
							// deregister all the IMPUs corresponding to the current IMPI
							DB_Op.deregister_all_IMPUs_for_an_IMPI_ID(session, impi.getId());
						}

						if (impiList.size() > 1){
							// remove defaultIMPI
							impiList.remove(0);
							// add only the rest of IMPIs
							UtilAVP.addAsssociatedIdentities(request, impiList);
						}
					}
					else{
						for (int i = 0; i < impuList.size(); i++){
							IMPU impu = impuList.get(i);
							// deregister impu for the associated impi
							DB_Op.deregister_IMPU_for_an_IMPI_ID(session, impu.getId(), defaultIMPI.getId());
							UtilAVP.addPublicIdentity(request, impu.getIdentity());
						}
					}
					// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
					HibernateUtil.commitTransaction();
					// commit the transaction, as the previous updates should
					// be seen by the next DB operations
					
					HibernateUtil.beginTransaction();
					// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
					int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, defaultIMPI.getId_imsu());
					if (reg_cnt_for_imsu == 0){
						IMSU_DAO.update(session, defaultIMPI.getId_imsu(), "", "");
					}
					break;
					
				case CxConstants.RTR_Remove_S_CSCF:
					// the HSS indicates to the S-CSCF that the S-CSCF will no longer be assigned to 
					// unregitered Public Identities

					if (impuList != null && impuList.size() > 0){
						for (int i = 0; i < impuList.size(); i++){
							IMPU impu = impuList.get(i);
							// deregister impu for the associated impi
							DB_Op.deregister_IMPU_for_an_IMPI_ID(session, impu.getId(), defaultIMPI.getId());
							UtilAVP.addPublicIdentity(request, impu.getIdentity());
						}
						HibernateUtil.commitTransaction();
						// commit the transaction, as the previous updates should
						// be seen by the next DB operations
						
						HibernateUtil.beginTransaction();
						// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
						reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, defaultIMPI.getId_imsu());
						if (reg_cnt_for_imsu == 0){
							IMSU_DAO.update(session, defaultIMPI.getId_imsu(), "", "");
						}
					}
					break;
			}
			
			
			// add deregistration reason
			UtilAVP.addDeregistrationReason(request, reasonCode, reasonInfo);

			// update RTR_PPR
    		// add hopbyhop and endtoend ID into the rtr_ppr table
    		CxEvents_DAO.update_by_grp(session, grp, request.hopByHopID, request.endToEndID);

    		// send the request
			if (!diameterPeer.sendRequestTransactional(diameter_name, request, diameterStack)){
    			// if the host is not connected or the request cannot be sent from other reasons, delete the Cx Event from database!				
				CxEvents_DAO.delete(session, request.hopByHopID, request.endToEndID);				
			}
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException)
				HibernateUtil.commitTransaction();
			HibernateUtil.closeSession();
		}
	}
	
	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage response){
		List<String> associatedIdentities = UtilAVP.getAssociatedIdentities(response);
		
		// delete the coresponding row(s) from rtr_ppr table
		boolean dbException = false;
		try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	List impi_ids_list = CxEvents_DAO.get_all_IMPI_IDs_by_HopByHop_and_EndToEnd_ID(session, response.hopByHopID, response.endToEndID);
        	
        	// test if all the associated identities corresponding to the default IMPI were included in the response
        	// for the moment - a simple test -> refering to the lists size

        	if ((impi_ids_list != null && impi_ids_list.size() > 1) && 
        			(associatedIdentities == null || associatedIdentities.size() + 1 < impi_ids_list.size())){

        		// we are sending new RTRs to all these IMPIs separatelly (S-CSCF is not supporting AssociatedIdentities in the request)
        		CxEvents rtr_ppr = CxEvents_DAO.get_one_from_grp(session, response.hopByHopID, response.endToEndID);
            	int grp = CxEvents_DAO.get_max_grp(session);
            	if (rtr_ppr == null)
            		return;

            	// send new RTR messages to all IMPIs (individually for each IMPI)
            	for (int i = 0; i < impi_ids_list.size(); i++){
            		grp++;
            		int id_impi = (Integer) impi_ids_list.get(i);
            		CxEvents new_rtr_ppr = new CxEvents();
            		new_rtr_ppr.setGrp(grp);
            		new_rtr_ppr.setId_impi(id_impi);
            		new_rtr_ppr.setTrials_cnt(rtr_ppr.getTrials_cnt() + 1);
            		new_rtr_ppr.setId_impu(-1);
            		new_rtr_ppr.setType(rtr_ppr.getType());
            		new_rtr_ppr.setReason_info(rtr_ppr.getReason_info());
            		new_rtr_ppr.setDiameter_name(rtr_ppr.getDiameter_name());
            		CxEvents_DAO.insert(session, new_rtr_ppr);
            	}
        	}
        	
        	// delete the old rows
        	CxEvents_DAO.delete(session, response.hopByHopID, response.endToEndID);
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}
	}
	
	public static void processTimeout(DiameterMessage request){
		boolean dbException = false;
		try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	CxEvents_DAO.delete(session, request.hopByHopID, request.endToEndID);
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}		        	
	}
}
