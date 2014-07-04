/*
  *  Copyright (C) 2004-2009 FhG Fokus
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
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.cx.CxExperimentalResultException;
import de.fhg.fokus.hss.cx.CxFinalResultException;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMPU_VisitedNetwork;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.VisitedNetwork;
import de.fhg.fokus.hss.db.op.CapabilitiesSet_DAO;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_VisitedNetwork_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.Preferred_SCSCF_Set_DAO;
import de.fhg.fokus.hss.db.op.VisitedNetwork_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.db.hibernate.*;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author andreea dot ancuta dot onofrei at fokus dot fraunhofer dot de 
 * Andreea Ancuta Onofrei / FOKUS Fraunhofer Institute
 */
public class UAR {
	private static Logger logger = Logger.getLogger(UAR.class);
	
	public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request){
		
		DiameterMessage response = diameterPeer.newResponse(request);
		response.flagProxiable = true;
		
		UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
		
			String publicIdentity = UtilAVP.getPublicIdentity(request);
			String privateIdentity = UtilAVP.getUserName(request);
			if (publicIdentity == null || privateIdentity == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);	
			}
			
			String visited_network_name = UtilAVP.getVisitedNetwork(request);
			if (visited_network_name == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			VisitedNetwork visited_network = VisitedNetwork_DAO.get_by_Identity(session, visited_network_name);
			if (visited_network == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_ROAMING_NOT_ALLOWED);
			}
			
			// 1. check if the identities exist in hss
			IMPU impu = IMPU_DAO.get_by_Identity(session, publicIdentity);
			IMPI impi = IMPI_DAO.get_by_Identity(session, privateIdentity);
			if (impu == null || impi == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN);
			}
			
			IMSU imsu = IMSU_DAO.get_by_ID(session, impi.getId_imsu());
			
			// 2. check association
			IMPI_IMPU impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, impi.getId(), impu.getId());
			if (impi_impu == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITIES_DONT_MATCH);
			}
			
			// 3. check for IMPU if is barred or it is an Emergency Registration
			boolean Em_Reg = (UtilAVP.getUARFlags(request) & DiameterConstants.AVPValue.UAR_Flag_Emergency)!=0;
			if(Em_Reg)
				logger.debug(": Emergency Registration from "+publicIdentity+"/"+privateIdentity);
						
			if ((!Em_Reg) && (impu.getBarring() == 1)){
				List impuList = IMPU_DAO.get_others_from_set(session, impu.getId(), impu.getId_implicit_set());
				if (impuList == null || impuList.size() == 0){
					throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_AUTHORIZATION_REJECTED);
				}
				Iterator it = impuList.iterator();
				boolean notBarred = false;
				while (it.hasNext()){
					IMPU nextImpu = (IMPU) it.next();
					if (nextImpu.getBarring() == 0){
						notBarred = true;
						break;
					}
				}
				if (!notBarred){
					throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_AUTHORIZATION_REJECTED);
				}
			}
			
			// 4. check the User Authorization Type
			int authorizationType = UtilAVP.getUserAuthorizationType(request); 
			IMPU_VisitedNetwork impu_visited_network;
			switch (authorizationType){
				/*request for registration or without User-Authorization-Type AVP*/
				case DiameterConstants.AVPValue.UAT_Registration:
					/* if Emergency Registration skip roaming agreements */
					if(!Em_Reg){
						impu_visited_network = IMPU_VisitedNetwork_DAO.get_by_IMPU_and_VisitedNetwork_ID(session, impu.getId(), visited_network.getId());
						if (impu_visited_network == null){
							throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_ROAMING_NOT_ALLOWED);
						}
					}
					if (impu.getCan_register() == 0){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_AUTHORIZATION_REJECTED);
					}
					break;
					
				case DiameterConstants.AVPValue.UAT_De_Registration:
					break;
					
				case DiameterConstants.AVPValue.UAT_Registration_and_Capabilities:
					/* if Emergency Registration skip roaming agreements */
					if(!Em_Reg){
						impu_visited_network = 	IMPU_VisitedNetwork_DAO.get_by_IMPU_and_VisitedNetwork_ID(session, impu.getId(), visited_network.getId());
						if (impu_visited_network == null){
							throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_ROAMING_NOT_ALLOWED);
						}
					}
							
					if (impu.getCan_register() == 0){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_AUTHORIZATION_REJECTED);
					}

					// else add capabilities
					List cap_set_mand_list = CapabilitiesSet_DAO.get_all_from_set(session, imsu.getId_capabilities_set(), 1);
					List cap_set_opt_list = CapabilitiesSet_DAO.get_all_from_set(session, imsu.getId_capabilities_set(), 0);
					List pre_SCSCF_name_list = Preferred_SCSCF_Set_DAO.get_all_from_set(session, imsu.getId_preferred_scscf_set());
					
					UtilAVP.addServerCapabilities(response, cap_set_mand_list, cap_set_opt_list, pre_SCSCF_name_list);
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					/* if Emergency Registration, skip further checks */
					if(Em_Reg){
						HibernateUtil.commitTransaction();
						HibernateUtil.closeSession();
						return response;	
					}
					break;
			}

			
			String serverName = IMSU_DAO.get_SCSCF_Name_by_IMSU_ID(session, impi.getId_imsu());
			// 5. check the state of the public identity
			switch (impu.getUser_state()){
			
				case CxConstants.IMPU_user_state_Registered:
					
					if (serverName != null && !serverName.equals(""))
						UtilAVP.addServerName(response, serverName);
					if (authorizationType == DiameterConstants.AVPValue.UAT_Registration){
						UtilAVP.addExperimentalResultCode(response, 
								DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION.getCode(), 
								DiameterConstants.Vendor.V3GPP);
					}
					else if (authorizationType == DiameterConstants.AVPValue.UAT_De_Registration){
						UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					}
					break;
					
				case CxConstants.IMPU_user_state_Unregistered:
					
					if (authorizationType == DiameterConstants.AVPValue.UAT_De_Registration){
						UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					}
					else if (authorizationType == DiameterConstants.AVPValue.UAT_Registration){
						if (serverName != null && !serverName.equals(""))						
							UtilAVP.addServerName(response, serverName);	
						UtilAVP.addExperimentalResultCode(response, 
								DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION.getCode(),
								DiameterConstants.Vendor.V3GPP);
					}
					break;
					
				case CxConstants.IMPU_user_state_Not_Registered:
				case CxConstants.IMPU_user_state_Auth_Pending:
					
					if (authorizationType == DiameterConstants.AVPValue.UAT_De_Registration){
						UtilAVP.addExperimentalResultCode(response, 
								DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITY_NOT_REGISTERED.getCode(),
								DiameterConstants.Vendor.V3GPP);
					}
					else if (authorizationType == DiameterConstants.AVPValue.UAT_Registration){
						
						List list = IMPI_IMPU_DAO.get_all_IMPU_of_IMSU_with_User_State(session, impi.getId_imsu(), 
								CxConstants.IMPU_user_state_Registered);
						if (list.size() > 0){
							if (serverName != null && !serverName.equals(""))						
								UtilAVP.addServerName(response, serverName);
							UtilAVP.addExperimentalResultCode(response, 
									DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION.getCode(),
									DiameterConstants.Vendor.V3GPP);
							break;
						}
						
						list = IMPI_IMPU_DAO.get_all_IMPU_of_IMSU_with_User_State(session, impi.getId_imsu(), 
								CxConstants.IMPU_user_state_Unregistered);
						if (list.size() > 0){
							if (serverName != null && !serverName.equals(""))						
								UtilAVP.addServerName(response, serverName);
							UtilAVP.addExperimentalResultCode(response, 
									DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION.getCode(),
									DiameterConstants.Vendor.V3GPP);
							break;
						}
						list = IMPI_IMPU_DAO.get_all_IMPU_of_IMSU_with_User_State(session, 
								impi.getId_imsu(), CxConstants.IMPU_user_state_Auth_Pending);
						
						if (list.size() > 0 && (serverName != null && !serverName.equals(""))){
							
							UtilAVP.addServerName(response, serverName);
							UtilAVP.addExperimentalResultCode(response, 
									DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION.getCode(),
									DiameterConstants.Vendor.V3GPP);
							break;
						}
						
						// else add capabilities 					
						List cap_set_mand_list = CapabilitiesSet_DAO.get_all_from_set(session, imsu.getId_capabilities_set(), 1);
						List cap_set_opt_list = CapabilitiesSet_DAO.get_all_from_set(session, imsu.getId_capabilities_set(), 0);
						List pre_SCSCF_name_list = Preferred_SCSCF_Set_DAO.get_all_from_set(session, imsu.getId_preferred_scscf_set());
						
						UtilAVP.addServerCapabilities(response, cap_set_mand_list, cap_set_opt_list, pre_SCSCF_name_list);
						UtilAVP.addExperimentalResultCode(response, 
								DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_FIRST_REGISTRATION.getCode(),
								DiameterConstants.Vendor.V3GPP);
						
					}
					break;
			}
		}
		catch(CxExperimentalResultException e){
			UtilAVP.addExperimentalResultCode(response, e.getErrorCode(), e.getVendor());
			e.printStackTrace();
		} 
		catch (CxFinalResultException e) {
			UtilAVP.addResultCode(response, e.getErrorCode());
			e.printStackTrace();
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
		return response;
	}
}
