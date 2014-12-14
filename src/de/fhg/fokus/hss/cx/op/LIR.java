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
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.cx.CxExperimentalResultException;
import de.fhg.fokus.hss.cx.CxFinalResultException;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.db.hibernate.*;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class LIR {
	private static Logger logger = Logger.getLogger(LIR.class);
	
	public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request){
		DiameterMessage response = diameterPeer.newResponse(request);
		response.flagProxiable = true;
		
		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);
		
		boolean dbException = false;
		try{
			// obtain the hibernate session & transaction
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			// get the needed AVPs
			String publicIdentity = UtilAVP.getPublicIdentity(request);
			if (publicIdentity == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			int originatingRequest = UtilAVP.getOriginatingRequest(request);
			
			// 1. check that the public identity is known
			IMPU impu = IMPU_DAO.get_by_Identity(session, publicIdentity);
			if (impu == null){
				// then lets try a Wildcarded PSI !!
				impu = IMPU_DAO.get_by_Wildcarded_Identity(session, publicIdentity, 0, 1);
				
				if (impu == null) {				
					throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN);
				}
			}
			
			// 2. check if public identity is PSI; if PSI then test activation
			int type = impu.getType();
			if (type == CxConstants.Identity_Type_Distinct_PSI || type == CxConstants.Identity_Type_Wildcarded_PSI){
				if (impu.getPsi_activation() == 0){
					throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN); 
				}
			}
			
			// 3. check the state of public identity
			int user_state = impu.getUser_state();

			List impi_impu_list = IMPI_IMPU_DAO.get_join_by_IMPU_ID(session, impu.getId());
			if (impi_impu_list == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
			}
				
			Iterator it = impi_impu_list.iterator();
			String scscf_name = null;
			while (it.hasNext()){
				Object [] resultRow = (Object []) it.next();
				IMPI impi = (IMPI) resultRow[1];
				//one impi is enough to find out the associated imsu
				if (type == CxConstants.Identity_Type_Public_User_Identity)
				{
					// if its a user it should be registered and have a SCSCF asigned
					scscf_name = IMSU_DAO.get_SCSCF_Name_by_IMSU_ID(session, impi.getId_imsu());
				} else {
					// if its a PSI we are getting the preferred is ok
					logger.info("PSI : getting SCSCF name!\n");
					scscf_name = IMSU_DAO.get_SCSCF_Name_by_PSI_IMSU_ID(session, impi.getId_imsu());
				}
				break;
			}
			
			switch (user_state){
			
				case CxConstants.IMPU_user_state_Registered:
				case CxConstants.IMPU_user_state_Auth_Pending:
					if (scscf_name == null || scscf_name.equals("")){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					UtilAVP.addServerName(response, scscf_name);
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					break;
			
				case CxConstants.IMPU_user_state_Unregistered:
				    
					boolean unregistered_services = false;
					if (SP_IFC_DAO.get_Unreg_Serv_Count(session, impu.getId_sp()) > 0){
						unregistered_services = true;
					}
					
					if (originatingRequest == 1 || unregistered_services == true){
						if (scscf_name == null || scscf_name.equals("")){
							throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
						}
						UtilAVP.addServerName(response, scscf_name);
						UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					}
					else{
						// cannot fulfil the request
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					break;

				case CxConstants.IMPU_user_state_Not_Registered:
					unregistered_services = false;
					
					int cnt =SP_IFC_DAO.get_Unreg_Serv_Count(session, impu.getId_sp());
					if (cnt > 0){
						unregistered_services = true;
					}
					
					if (originatingRequest == 1 || unregistered_services == true){
						if (scscf_name != null && !scscf_name.equals("")){
							UtilAVP.addServerName(response, scscf_name);
							UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
							break;
						}
						else{
							UtilAVP.addExperimentalResultCode(response,
									DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_UNREGISTERED_SERVICE.getCode(), DiameterConstants.Vendor.V3GPP);
						}
					}
					else{
						UtilAVP.addExperimentalResultCode(response, 
								DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITY_NOT_REGISTERED.getCode(), DiameterConstants.Vendor.V3GPP);
					}
					break;
			}
			
		}
		catch(CxExperimentalResultException e){
			UtilAVP.addExperimentalResultCode(response, e.getErrorCode(), e.getVendor());
			e.printStackTrace();
		}
		catch(CxFinalResultException e){
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
