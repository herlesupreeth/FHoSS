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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.cx.CxExperimentalResultException;
import de.fhg.fokus.hss.cx.CxFinalResultException;
import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.ChargingInfo;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.SPT;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.TP;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.ChargingInfo_DAO;
import de.fhg.fokus.hss.db.op.DB_Op;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.SPT_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_Shared_IFC_Set_DAO;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.db.op.TP_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.db.hibernate.*;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI concept according to Release 7
 *
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */

public class SAR {
	private static Logger logger = Logger.getLogger(SAR.class);
	
	public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request){
		DiameterMessage response = diameterPeer.newResponse(request);
		response.flagProxiable = true;
		
		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);
		
		
		boolean dbException = false;
		try{
			String vendorSpecificAppID = UtilAVP.getVendorSpecificApplicationID(request);
			String authSessionState = UtilAVP.getAuthSessionState(request);
			String originHost = UtilAVP.getOriginatingHost(request);
			String originRealm = UtilAVP.getOriginatingRealm(request);
			String destinationRealm = UtilAVP.getDestinationRealm(request);
			String serverName = UtilAVP.getServerName(request);
			int serverAssignmentType = UtilAVP.getServerAssignmentType(request);
			int userDataAlreadyAvailable = UtilAVP.getUserDataAlreadyAvailable(request);
		
			// 0. check for missing of mandatory AVPs
			if (vendorSpecificAppID == null) {
                                logger.warn("Missing Vendor-Specific-Application-ID");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (authSessionState == null) {
                                logger.warn("Missing Auth-Session-State");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (originHost == null) {
                                logger.warn("Missing Origin-Host");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (originRealm == null) {
                                logger.warn("Missing Origin-Realm");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (destinationRealm == null) {
                                logger.warn("Missing Destination-Realm");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (serverName == null) {
                                logger.warn("Missing Server-Name");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (serverAssignmentType == -1) {
                                logger.warn("Missing Server-Assignment-Type");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			if (userDataAlreadyAvailable == -1) {
                                logger.warn("Missing User-Data-Already-Available");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			String publicIdentity = UtilAVP.getPublicIdentity(request);
			String privateIdentity = UtilAVP.getUserName(request);
			if (publicIdentity == null && privateIdentity == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
			}
			
			// open db transaction
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			// 1. check that the public identity & privateIdentity are known in HSS
			IMPU impu = IMPU_DAO.get_by_Identity(session, publicIdentity);
			if (impu == null) {
				impu = IMPU_DAO.get_by_Wildcarded_Identity(session, publicIdentity, 1, 1);
				
			}
			IMPI impi = IMPI_DAO.get_by_Identity(session, privateIdentity);
			if (publicIdentity != null && impu == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN); 
			}
			if (privateIdentity != null && impi == null){
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN); 
			}
			
			// store the IMSU ID in id_imsu
			int id_imsu=-1;
			if (impi == null){
				IMPI associatedIMPI = IMPI_DAO.get_an_IMPI_for_IMPU(session, impu.getId());
				id_imsu = associatedIMPI.getId_imsu();
			}
			else{
				id_imsu = impi.getId_imsu();
			}
			
			IMPI_IMPU impi_impu;
			// 2. check association
			if (impi != null && impu != null){
				impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, impi.getId(), impu.getId());
				if (impi_impu == null){
					throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITIES_DONT_MATCH);
				}
			}

			// 3. 
			switch (serverAssignmentType){
				case CxConstants.Server_Assignment_Type_Timeout_Deregistration:
				case CxConstants.Server_Assignment_Type_User_Deregistration:
				case CxConstants.Server_Assignment_Type_Deregistration_Too_Much_Data:
				case CxConstants.Server_Assignment_Type_Timeout_Deregistration_Store_Server_Name:					
				case CxConstants.Server_Assignment_Type_User_Deregistration_Store_Server_Name:
				case CxConstants.Server_Assignment_Type_Administrative_Deregistration:
					break;
					
				default:
					AVP avp = request.findAVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY, true, DiameterConstants.Vendor.V3GPP);
					if (avp != null){
						AVP next_public_identity = UtilAVP.getNextPublicIdentityAVP(request, avp);
						if (next_public_identity != null){
							throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_AVP_OCCURS_TOO_MANY_TIMES);
						}
					}
			}
			
			
			// 4. check if the public identity is a Pubic Service Identifier; if yes, check the activation
			int impu_type  = impu.getType();
			if (impu_type == CxConstants.Identity_Type_Distinct_PSI || impu_type == CxConstants.Identity_Type_Wildcarded_PSI){
				if (impu.getPsi_activation() == 0){
					throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN);
				}
			}
			// 5. check the server assignment type received in the request
			switch (serverAssignmentType){
				
				case CxConstants.Server_Assignment_Type_Registration:
				case CxConstants.Server_Assignment_Type_Re_Registration:
					if (impi == null || impu == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}
					
					// clear the auth pending if neccessary
					impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, impi.getId(), impu.getId());
					
					if (impi_impu.getUser_state() == CxConstants.IMPU_user_state_Auth_Pending){
						impi_impu.setUser_state(CxConstants.IMPU_user_state_Registered);
					}
					
					// set registration state to registered, if neccessary
					// set the registration state on all impu from the same implicitset
					DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), 
							CxConstants.IMPU_user_state_Registered, true);
					
                                        // Assign this S-CSCF to this subscriber
                                        IMSU_DAO.update(session, impi.getId_imsu(), serverName, originHost);
					UtilAVP.addUserName(response, privateIdentity);
					
					//download the profile data
					String user_data = SAR.downloadUserData(privateIdentity, impu.getId_implicit_set());
					if (user_data == null){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					UtilAVP.addUserData(response, user_data);
					
					// add charging information
					ChargingInfo chargingInfo = ChargingInfo_DAO.get_by_ID(session, impu.getId_charging_info());
					if (chargingInfo != null){
						UtilAVP.addChargingInformation(response, chargingInfo);	
					}
					
					// if more private are associated to the IMSU,  AssociatedIdentities AVP are added
					List privateIdentitiesList = IMPI_DAO.get_all_by_IMSU_ID(session, impi.getId_imsu());
					if (privateIdentitiesList != null && privateIdentitiesList.size() > 1){
						UtilAVP.addAsssociatedIdentities(response, privateIdentitiesList);
					}
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					logger.info("\nUser with Public Identity: " + impu.getIdentity() + " and all its coresponding implicit-set identities are Registered!");
					if (serverAssignmentType == CxConstants.Server_Assignment_Type_Registration){
						// send Sh Notifications for IMS-User-State for all of the subscribers
						ShNotification_DAO.insert_notif_for_IMS_User_State(session, impu.getId_implicit_set(), 
								CxConstants.IMPU_user_state_Registered);
					}
					break;
					
				case CxConstants.Server_Assignment_Type_Unregistered_User:
					if (impu == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}
					impu.convert_wildcard_from_sql_to_ims();
					// store the scscf_name & orgiin_host
					privateIdentitiesList = IMPI_IMPU_DAO.get_all_IMPI_by_IMPU_ID(session, impu.getId());
					if (privateIdentitiesList == null || privateIdentitiesList.size() == 0){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					
					IMPI first_IMPI = (IMPI) privateIdentitiesList.get(0);
					IMSU_DAO.update(session, first_IMPI.getId_imsu(), serverName, originHost);
					
					// set the user_state to Unregistered
					DB_Op.setUserState(session, first_IMPI.getId(), impu.getId_implicit_set(), 
							CxConstants.IMPU_user_state_Unregistered, true);
					
					
					//download the profile data
					user_data = SAR.downloadUserData(privateIdentity, impu.getId_implicit_set());
					if (user_data == null){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					UtilAVP.addUserData(response, user_data);
					
					// add charging Info
					chargingInfo = ChargingInfo_DAO.get_by_ID(session, impu.getId_charging_info());
					if (chargingInfo != null){
						UtilAVP.addChargingInformation(response, chargingInfo);
					}
					
//					add a private to the response (the first private found, if more than one are available)
					UtilAVP.addUserName(response, first_IMPI.getIdentity());
					
					
					
					//AssociatedIdentities if neccessary
					if (privateIdentitiesList.size() > 1){
						UtilAVP.addAsssociatedIdentities(response, privateIdentitiesList);
					}
					
					// result code = diameter success
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					
					logger.info("\nUser with Public Identity: " + impu.getIdentity() + " and all its coresponding implicit-set identities are Un-Registered!");
					// send Sh Notifications for IMS-User-State for all of the subscribers
					ShNotification_DAO.insert_notif_for_IMS_User_State(session, impu.getId_implicit_set(), 
							CxConstants.IMPU_user_state_Unregistered);
					/*} else {
						// So its a PSI , which is only done in unregistered so a bit of processing needed
//						download the profile data
						logger.info("\n its here where i am\n");
						user_data = SAR.downloadUserData(null, impu.getId_implicit_set());
						if (user_data == null){
							throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
						}
						UtilAVP.addUserData(response, user_data);
						
						// add charging Info
						chargingInfo = ChargingInfo_DAO.get_by_ID(session, impu.getId_charging_info());
						if (chargingInfo != null){
							UtilAVP.addChargingInformation(response, chargingInfo);
						}	
//						result code = diameter success
						UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						
						// its a PSI
						logger.info("\n o god! its a f* PSI.... \n");
						
					}	*/
						
						
					
					impu.convert_wildcard_from_ims_to_sql();
					break;
					
				case CxConstants.Server_Assignment_Type_Timeout_Deregistration:
				case CxConstants.Server_Assignment_Type_User_Deregistration:
				case CxConstants.Server_Assignment_Type_Deregistration_Too_Much_Data:
				case CxConstants.Server_Assignment_Type_Administrative_Deregistration:
					if (impi == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}

					List impuList = UtilAVP.getAllIMPU(session, request);
					if (impuList == null){
						impuList = IMPI_IMPU_DAO.get_all_Default_IMPU_of_Set_by_IMPI(session, impi.getId());
					}
					if (impuList == null){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					Iterator iterator = impuList.iterator();
					IMPU crt_impu;
					while (iterator.hasNext()){
						crt_impu = (IMPU) iterator.next();
						
						switch (crt_impu.getUser_state()){
							case CxConstants.IMPU_user_state_Registered:
								int reg_cnt = IMPI_IMPU_DAO.get_Registered_IMPU_Count(session, crt_impu.getId());
								if (reg_cnt == 1){
									// set the user_state to Not-Registered
									DB_Op.setUserState(session, impi.getId(), crt_impu.getId_implicit_set(), 
											CxConstants.IMPU_user_state_Not_Registered, true);
									HibernateUtil.commitTransaction();
									// commit the transaction, as the previous updates should
									// be seen by the next DB operations
									
									HibernateUtil.beginTransaction();										
									// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
									int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, impi.getId_imsu());
									if (reg_cnt_for_imsu == 0){
										IMSU_DAO.update(session, impi.getId_imsu(), "", "");
										// send Sh Notifications for SCSCF_Name for all of the subscribers
										ShNotification_DAO.insert_notif_for_SCSCFName(session, id_imsu, "");
									}
									logger.info("User with Public Identity: " + crt_impu.getIdentity() + " and all its coresponding implicit-set identities are De-Registered!");									
									// send Sh Notifications for IMS-User-State for all of the subscribers
									ShNotification_DAO.insert_notif_for_IMS_User_State(session, crt_impu.getId_implicit_set(), 
											CxConstants.IMPU_user_state_Not_Registered);
								}
								else{
									DB_Op.setUserState(session, impi.getId(), crt_impu.getId_implicit_set(), 
											CxConstants.IMPU_user_state_Not_Registered, false);

									logger.info("Only the the association of: Public Identity: " + crt_impu.getIdentity() + " with Private Identity: " + impi.getIdentity() + " is De-Registered!");									
									// send Sh Notifications for IMS-User-State for all of the subscribers
									ShNotification_DAO.insert_notif_for_IMS_User_State(session, crt_impu.getId_implicit_set(), 
											CxConstants.IMPU_user_state_Not_Registered);

								}
								break;
								
							case CxConstants.IMPU_user_state_Unregistered:
								if (impi == null){
									throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
								}
								// set the user_state to Not-Registered
								DB_Op.setUserState(session, impi.getId(), crt_impu.getId_implicit_set(), 
										CxConstants.IMPU_user_state_Not_Registered, true);

								HibernateUtil.commitTransaction();
								// commit the transaction, as the previous updates should
								// be seen by the next DB operations
								
								HibernateUtil.beginTransaction();										
								// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
								int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, impi.getId_imsu());
								if (reg_cnt_for_imsu == 0){
									IMSU_DAO.update(session, impi.getId_imsu(), "", "");
									// send Sh Notifications for SCSCF_Name for all of the subscribers
									ShNotification_DAO.insert_notif_for_SCSCFName(session, id_imsu, "");																		
								}
								// send Sh Notifications for IMS-User-State for all of the subscribers
								ShNotification_DAO.insert_notif_for_IMS_User_State(session, crt_impu.getId_implicit_set(), 
										CxConstants.IMPU_user_state_Not_Registered);
								logger.info("User with Public Identity: " + crt_impu.getIdentity() + " and all its coresponding implicit-set identities are De-Registered!");								
								break;
						}
					}
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					break;
					
				case CxConstants.Server_Assignment_Type_Timeout_Deregistration_Store_Server_Name:
				case CxConstants.Server_Assignment_Type_User_Deregistration_Store_Server_Name:		
					// HSS decides not to keep the S-CSCF name
					if (impi == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}	
					impuList = UtilAVP.getAllIMPU(session, request);
					if (impuList == null){
						impuList = IMPI_IMPU_DAO.get_all_Default_IMPU_of_Set_by_IMPI(session, impi.getId());
					}
					if (impuList == null){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}

					iterator = impuList.iterator();
					while (iterator.hasNext()){
						crt_impu = (IMPU) iterator.next();
						
						int reg_cnt = IMPI_IMPU_DAO.get_Registered_IMPU_Count(session, crt_impu.getId());
						if (reg_cnt == 1){
							// set the user_state to Not-Registered
							DB_Op.setUserState(session, impi.getId(), crt_impu.getId_implicit_set(), 
									CxConstants.IMPU_user_state_Not_Registered, true);
							HibernateUtil.commitTransaction();
							// commit the transaction, as the previous updates should
							// be seen by the next DB operations
							
							HibernateUtil.beginTransaction();										
							// 	clear the scscf_name & origin_host ONLY and ONLY if the IMSU has no other registered IMPUs										
							int reg_cnt_for_imsu = IMPI_IMPU_DAO.get_Registered_IMPUs_count_for_IMSU_ID(session, impi.getId_imsu());
							if (reg_cnt_for_imsu == 0){
								IMSU_DAO.update(session, impi.getId_imsu(), "", "");
								// send Sh Notifications for SCSCF_Name for all of the subscribers
								ShNotification_DAO.insert_notif_for_SCSCFName(session, id_imsu, "");									
								
							}
							logger.info("User with Public Identity: " + crt_impu.getIdentity() + " and all its coresponding implicit-set identities are De-Registered!");							
						}
						else{
							// Set the user_state to Not-Registered only on IMPI_IMPU association, IMPU registration state
							//remain registered
							DB_Op.setUserState(session, impi.getId(), crt_impu.getId_implicit_set(), 
									CxConstants.IMPU_user_state_Not_Registered, false);
							logger.info("Only the the association of: Public Identity: " + crt_impu.getIdentity() + " with Private Identity: " + impi.getIdentity() + " is De-Registered!");							
						}
						
						// send Sh Notifications for IMS-User-State for all of the subscribers
						ShNotification_DAO.insert_notif_for_IMS_User_State(session, crt_impu.getId_implicit_set(), 
								CxConstants.IMPU_user_state_Not_Registered);

					}
					
					UtilAVP.addExperimentalResultCode(response, 
							DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_SUCCESS_SERVER_NAME_NOT_STORED.getCode(), 
							DiameterConstants.Vendor.V3GPP);
					break;
					
				case CxConstants.Server_Assignment_Type_No_Assignment:
					if (impi == null || impu == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}
					IMSU imsu = IMSU_DAO.get_by_ID(session, impi.getId_imsu());
					String scscf_name = imsu.getScscf_name();
					
					if (!scscf_name.equals(serverName)){
						if (!scscf_name.equals("")){
							UtilAVP.addExperimentalResultCode(response, 
									DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITY_ALREADY_REGISTERED.getCode(), 
									DiameterConstants.Vendor.V3GPP);	
						}
						else{
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
						}
					}
					else{
						
						user_data = SAR.downloadUserData(privateIdentity, impu.getId_implicit_set());
						if (user_data == null){
							throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
						}
						UtilAVP.addUserData(response, user_data);
						// add charging information
						chargingInfo = ChargingInfo_DAO.get_by_ID(session, impu.getId_charging_info());
						if (chargingInfo != null){
							UtilAVP.addChargingInformation(response, chargingInfo);
						}												
						UtilAVP.addUserName(response, impi.getIdentity());
						
						privateIdentitiesList = IMPI_DAO.get_all_by_IMSU_ID(session, impi.getId_imsu());
						if (privateIdentitiesList != null && privateIdentitiesList.size() > 1){
							UtilAVP.addAsssociatedIdentities(response, privateIdentitiesList);
						}

						UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					}
					break;
					
				case CxConstants.Server_Assignment_Type_Authentication_Failure:
				case CxConstants.Server_Assignment_Type_Authentication_Timeout:	
					if (impi == null || impu == null){
						throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_MISSING_USER_ID);
					}

					impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, impi.getId(), impu.getId());
					int user_state = impi_impu.getUser_state();
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
									// send Sh Notifications for SCSCF_Name for all of the subscribers
									ShNotification_DAO.insert_notif_for_SCSCFName(session, id_imsu, "");									
									
								}
							}
							else{
								// Set the user_state to Not-Registered only on IMPI_IMPU association, 
								//IMPU registration state remain registered
								DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), 
										CxConstants.IMPU_user_state_Not_Registered, false);
							}

							// send Sh Notifications for IMS-User-State for all of the subscribers
							ShNotification_DAO.insert_notif_for_IMS_User_State(session, impu.getId(), 
									CxConstants.IMPU_user_state_Not_Registered);							
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
								// send Sh Notifications for SCSCF_Name for all of the subscribers
								ShNotification_DAO.insert_notif_for_SCSCFName(session, id_imsu, "");																	
							}
							// send Sh Notifications for IMS-User-State for all of the subscribers
							ShNotification_DAO.insert_notif_for_IMS_User_State(session, impu.getId_implicit_set(), 
									CxConstants.IMPU_user_state_Not_Registered);
							
							break;
							
						case CxConstants.IMPU_user_state_Auth_Pending:
							// reset Auth-Pending
							DB_Op.resetAuthPending(session, impi.getId(), impu.getId_implicit_set());
							break;
					}
					
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
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
	
	public static final String xml_version="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String ims_subscription_s="<IMSSubscription>";
	public static final String ims_subscription_e="</IMSSubscription>";
	public static final String private_id_s="<PrivateID>";
	public static final String private_id_e="</PrivateID>";
	public static final String service_profile_s="<ServiceProfile>";
	public static final String service_profile_e="</ServiceProfile>";
	public static final String public_id_s="<PublicIdentity>";
	public static final String public_id_e="</PublicIdentity>";
	public static final String barring_indication_s="<BarringIndication>";
	public static final String barring_indication_e="</BarringIndication>";
	public static final String identity_s="<Identity>";
	public static final String identity_e="</Identity>";
	public static final String identity_type_s="<IdentityType>";
	public static final String identity_type_e="</IdentityType>";
	public static final String wildcarded_psi_s="<WildcardedPSI>";
	public static final String wildcarded_psi_e="</WildcardedPSI>";
	public static final String display_name_s="<DisplayName>";
	public static final String display_name_e="</DisplayName>";
	
	public static final String ifc_s="<InitialFilterCriteria>";
	public static final String ifc_e="</InitialFilterCriteria>";
	public static final String priority_s="<Priority>";
	public static final String priority_e="</Priority>";
	public static final String tp_s="<TriggerPoint>";
	public static final String tp_e="</TriggerPoint>";
	public static final String cnf_s="<ConditionTypeCNF>";
	public static final String cnf_e="</ConditionTypeCNF>";
	public static final String spt_s="<SPT>";
	public static final String spt_e="</SPT>";
	public static final String condition_negated_s="<ConditionNegated>";
	public static final String condition_negated_e="</ConditionNegated>";
	public static final String group_s="<Group>";
	public static final String group_e="</Group>";
	public static final String req_uri_s="<RequestURI>";
	public static final String req_uri_e="</RequestURI>";
	public static final String method_s="<Method>";	
	public static final String method_e="</Method>";	
	public static final String sip_hdr_s="<SIPHeader>";
	public static final String sip_hdr_e="</SIPHeader>";
	public static final String session_case_s="<SessionCase>";
	public static final String session_case_e="</SessionCase>";
	public static final String session_desc_s="<SessionDescription>";
	public static final String session_desc_e="</SessionDescription>";
	public static final String registration_type_s="<RegistrationType>";
	public static final String registration_type_e="</RegistrationType>";
	public static final String header_s="<Header>";
	public static final String header_e="</Header>";
	public static final String content_s="<Content>";
	public static final String content_e="</Content>";
	public static final String line_s="<Line>";
	public static final String line_e="</Line>";

	public static final String app_server_s="<ApplicationServer>";
	public static final String app_server_e="</ApplicationServer>";
	public static final String server_name_s="<ServerName>";
	public static final String server_name_e="</ServerName>";
	public static final String default_handling_s="<DefaultHandling>";
	public static final String default_handling_e="</DefaultHandling>";
	public static final String service_info_s="<ServiceInfo>";
	public static final String service_info_e="</ServiceInfo>";
	public static final String include_register_request="<IncludeRegisterRequest/>";
	public static final String include_register_response="<IncludeRegisterResponse/>";

	public static final String profile_part_ind_s="<ProfilePartIndicator>";
	public static final String profile_part_ind_e="</ProfilePartIndicator>";

	public static final String cn_services_auth_s="<CoreNetworkServicesAuthorization>";
	public static final String cn_services_auth_e="</CoreNetworkServicesAuthorization>";
	public static final String subs_media_profile_id_s="<SubscribedMediaProfileId>";
	public static final String subs_media_profile_id_e="</SubscribedMediaProfileId>";
	public static final String shared_ifc_set_id_s="<Extension><SharedIFCSetID>";
	public static final String shared_ifc_set_id_e="</SharedIFCSetID></Extension>";

	public static final String extension_s = "<Extension>";
	public static final String extension_e = "</Extension>";
	
	public static final String zero="0";
	public static final String one="1";
	public static final String two="2";

	public static String downloadUserData(String privateIdentity, int id_implicit_set){
		Session session = HibernateUtil.getCurrentSession();
		List initial_impus_list = IMPU_DAO.get_all_from_set(session, id_implicit_set); //List of IMPUs that belong to the same implicit set
		Iterator iter = initial_impus_list.iterator();
		List<List> ifcs_list = new ArrayList<List>();    // List of list of iFCs associated to each IMPU
		List<Integer> Sps = new ArrayList<Integer>(); // List of SP id
		List<List<IMPU>> impus_list = new ArrayList<List<IMPU>>();   // "initial_impus_list" re-formatted into a matrix-like list
		while (iter.hasNext()){
			IMPU impu = (IMPU) iter.next();
			List<IMPU> aux = new ArrayList<IMPU>(); // Insert every IMPU in a List format
			aux.add(impu);				//
			impus_list.add(aux);	    // Insert the list "aux" into the "impus_list" matrix
			List aux2= IFC_DAO.get_all_IFCs_by_IMPU_ID_and_DSAI_Value_Active(session, impu.getId());
			ifcs_list.add(aux2);
			Sps.add(impu.getId_sp());  // We only store the SP id, instead of the SP object
		}
		
/*
We will handle 3 Lists:
impus_list,  Where we have the impus in a matrix (each position of the list is another list of IMPUs; Initially there is only one element in each position)
ifcs_list, in each position of this list we have the active ifcs associated to the IMPU/s which are exactly in the same position of the impus_list
Sps, in each position of this list we have the SP_id of the IMPU/s which are exactly in the same position of the impus_list

The main idea is looking inside the different positions of the ifcs_list and check if there are two positions of the list which have exactly the same iFCs;
If this is the case (and if they have also the same SP_id) we can group the IMPUs which are in these two positions.

If we have grouped two IMPUs in the position of the first IMPU which is being compared, we will delete from the three lists the position of the second IMPU
(since we needn't comparing that position anylonger).
Otherwise, we will jump to the next position on the lists to go on comparing.
*/
			
		List<IMPU> export;
		int j;
		for (int i=0; i < (ifcs_list.size()); i++){
			List Ifc_copy = (List) ifcs_list.get(i);
			j=i+1;
			while (j < ifcs_list.size())
			{
				boolean exit= false;
				List Ifc_copy2 = (List) ifcs_list.get(j);
				if ((Ifc_copy2.size()!= Ifc_copy.size() || (Sps.get(i)!=Sps.get(j))) ){
					//Check if the ifc Lists have not the same number of elements or if the SPs they belong to are different
					//In that case they cannot be associated
					exit=true;
			}
				if (exit==false) {
					for (int k=0; k<Ifc_copy2.size(); k++){
						if (!(Ifc_copy.contains(Ifc_copy2.get(k)))){
							// Check if all ifcs are the same in 2 different positions of the main list. If not, they cannot be associated
								exit= true;
								break;
		}
					}
				}
				if (exit==false){
					export =  impus_list.get(i);
					export.add((impus_list.get(j)).get(0));
					impus_list.remove(j);
					ifcs_list.remove(j);
					Sps.remove(j);
					impus_list.remove(i);	// Remove the position of the list where we are going to insert the new list of IMPUs
					impus_list.add(i, export);	// Add the new list of IMPUs
				}
				else{
					j++; //if we don't change anything we jump to the next position.
				}
			}
		}

		//

		// begin to write the data in the buffer
		StringBuffer sb = new StringBuffer();
		sb.append(xml_version);
		sb.append(ims_subscription_s);
		
		// PrivateID
			sb.append(private_id_s);
			sb.append(privateIdentity);
			sb.append(private_id_e);

		//SP

		for (int i = 0; i < impus_list.size(); i++){
			sb.append(service_profile_s);
			// PublicIdentity 					=> 1 to n
			List impu_array = (List) impus_list.get(i);
			Iterator it = impu_array.iterator();
			while (it.hasNext()){
				IMPU impu = (IMPU)it.next();
				sb.append(public_id_s);
				
				if (impu.getBarring() == 1){
					// add Barring Indication
					sb.append(barring_indication_s);
					sb.append(impu.getBarring());
					sb.append(barring_indication_e);
				}
				
				// add Identity
				sb.append(identity_s);
				sb.append(impu.getIdentity());
				sb.append(identity_e);
				
				// add Extension
				sb.append(extension_s);
				// add Identity Type
				sb.append(identity_type_s);
				sb.append(impu.getType());
				sb.append(identity_type_e);
				
				if (impu.getType() == CxConstants.Identity_Type_Wildcarded_PSI){
					if (impu.getWildcard_psi() == null || impu.getWildcard_psi().equals("")){
						logger.error("Wildcarded PSI is NULL or is empty! Please provide a valid Wildcarded PSI! \n Aborting...");
						return null;
					}
					
					sb.append(wildcarded_psi_s);
					impu.convert_wildcard_from_sql_to_ims();
					sb.append(impu.getWildcard_psi());
					impu.convert_wildcard_from_ims_to_sql();
					sb.append(wildcarded_psi_e);
					
				}
				
				if (impu.getDisplay_name() != null && !impu.getDisplay_name().equals("")){
					// add Extension 2 (Display Name)
					sb.append(extension_s);
					sb.append(display_name_s);
					sb.append(impu.getDisplay_name());
					sb.append(display_name_e);
					sb.append(extension_e);
				}
				sb.append(extension_e);

				sb.append(public_id_e);
			}
			
			// InitialFilterCriteria 			=> 0 to n
			
			//	List list_ifc = SP_IFC_DAO.get_all_SP_IFC_by_SP_ID(session, sp_array[i].getId());
			List list_ifc = (List) ifcs_list.get(i);
			if (list_ifc != null && list_ifc.size() > 0){
				Iterator it_ifc;
				it_ifc = list_ifc.iterator();
				//Object[] crt_row;
				
				while (it_ifc.hasNext()){
					sb.append(ifc_s);
				/*	crt_row = (Object[]) it_ifc.next();
					SP_IFC crt_sp_ifc= (SP_IFC) crt_row[0];
					IFC crt_ifc = (IFC) crt_row[1]; */
					
					IFC crt_ifc = (IFC) it_ifc.next();
					SP_IFC crt_sp_ifc= SP_IFC_DAO.get_by_SP_and_IFC_ID(session, Sps.get(i), crt_ifc.getId());

					if (crt_ifc.getId_application_server() == -1 || crt_sp_ifc.getPriority() == -1){
						// error, application server and priority are mandatory!
						logger.error("Application Server ID or SP_IFC priority value is not defined!\n Aborting...");
						return null;
					}
					
					// adding priority
					sb.append(priority_s);
					sb.append(crt_sp_ifc.getPriority());
					sb.append(priority_e);
					
					// add trigger
					if (crt_ifc.getId_tp() != -1){
						// we have a trigger to add
						sb.append(tp_s);
						
						TP tp = TP_DAO.get_by_ID(session, crt_ifc.getId_tp());
						sb.append(cnf_s);
						sb.append(tp.getCondition_type_cnf());
						sb.append(cnf_e);
						
						List list_spt = SPT_DAO.get_all_by_TP_ID(session, tp.getId());
						if (list_spt == null){
							logger.error("SPT list is NULL! Should be at least one SPT asssociated to the TP!\nAborting...");
							return null;
						}
						
						// add SPT
						Iterator it_spt = list_spt.iterator();
						SPT crt_spt;
						while (it_spt.hasNext()){
							crt_spt = (SPT) it_spt.next();
							sb.append(spt_s);
							
							// condition negated
							sb.append(condition_negated_s);
							sb.append(crt_spt.getCondition_negated());
							sb.append(condition_negated_e);
							
							// group
							sb.append(group_s);
							sb.append(crt_spt.getGrp());
							sb.append(group_e);
							
							switch (crt_spt.getType()){
								
								case CxConstants.SPT_Type_RequestURI:
									sb.append(req_uri_s);
									sb.append(crt_spt.getRequesturi());
									sb.append(req_uri_e);
									break;

								case CxConstants.SPT_Type_Method:
									sb.append(method_s);
									sb.append(crt_spt.getMethod());
									sb.append(method_e);
									break;
								
								case CxConstants.SPT_Type_SIPHeader:
									sb.append(sip_hdr_s);
									
									sb.append(header_s);
									sb.append(crt_spt.getHeader());
									sb.append(header_e);
									sb.append(content_s);
									sb.append(crt_spt.getHeader_content());
									sb.append(content_e);

									sb.append(sip_hdr_e);
									break;
								
								case CxConstants.SPT_Type_SessionCase:
									sb.append(session_case_s);
									sb.append(crt_spt.getSession_case());
									sb.append(session_case_e);
									break;
								
								case CxConstants.SPT_Type_SessionDescription:
									sb.append(session_desc_s);
									
									sb.append(line_s);
									sb.append(crt_spt.getSdp_line());
									sb.append(line_e);
									sb.append(content_s);
									sb.append(crt_spt.getSdp_line_content());
									sb.append(content_e);
									
									sb.append(session_desc_e);
									break;
							}
							
							// add Extension if available
							
							if (crt_spt.getRegistration_type() != -1){
								sb.append(extension_s);
								
								int reg_type = crt_spt.getRegistration_type();
								if ((reg_type & CxConstants.RType_Reg_Mask) != 0){
									sb.append(registration_type_s);
									sb.append(zero);
									sb.append(registration_type_e);
									
								}
								if ((reg_type & CxConstants.RType_Re_Reg_Mask) != 0){
									sb.append(registration_type_s);
									sb.append(one);
									sb.append(registration_type_e);
									
								}
								if ((reg_type & CxConstants.RType_De_Reg_Mask) != 0){
									sb.append(registration_type_s);
									sb.append(two);
									sb.append(registration_type_e);
									
								}
								sb.append(extension_e);
							}
							sb.append(spt_e);
						}
						sb.append(tp_e);
					}

					// add the Application Server
					ApplicationServer crt_as = ApplicationServer_DAO.get_by_ID(session, crt_ifc.getId_application_server());
					if (crt_as == null){
						logger.error("Application Server is NULL, Initial Filter Criteria should contain a valid Application Server! \nAborting...");
						return null;
					}
					
					sb.append(app_server_s);
					sb.append(server_name_s);
					sb.append(crt_as.getServer_name());
					sb.append(server_name_e);
					if (crt_as.getDefault_handling() != -1){
						sb.append(default_handling_s);
						sb.append(crt_as.getDefault_handling());
						sb.append(default_handling_e);
					}
					
					if (crt_as.getService_info() != null && !crt_as.getService_info().equals("")){
						sb.append(service_info_s);
						sb.append(crt_as.getService_info());
						sb.append(service_info_e);
					}
					
					if ((crt_as.getInclude_register_request() == 1) || (crt_as.getInclude_register_response() == 1)) {
						sb.append(extension_s);
						
						if (crt_as.getInclude_register_request() == 1) {
							sb.append(include_register_request);
						}
					    
						if (crt_as.getInclude_register_response() == 1) {
							sb.append(include_register_response);
						}
						
						sb.append(extension_e);
					}
					
					sb.append(app_server_e);
					
					if (crt_ifc.getProfile_part_ind() != -1){
						// add the profile part indicator
						sb.append(profile_part_ind_s);
						sb.append(crt_ifc.getProfile_part_ind());
						sb.append(profile_part_ind_e);

					}
					
					sb.append(ifc_e);
				}
				
			}
			/*
			// CoreNetworkServiceAuthorization	=> 0 to n
			if (sp_array[i].getCn_service_auth() != -1){
				sb.append(cn_services_auth_s);
				sb.append(subs_media_profile_id_s);
				sb.append(sp_array[i].getCn_service_auth());
				sb.append(subs_media_profile_id_e);
				sb.append(cn_services_auth_e);
			}
			*/
			
			
			// Extension						=> 0 to 1
			//List all_IDs = SP_Shared_IFC_Set_DAO.get_all_shared_IFC_set_IDs_by_SP_ID(session, sp_array[i].getId());
			int sp_id= Sps.get(i);
			List all_IDs = SP_Shared_IFC_Set_DAO.get_all_shared_IFC_set_IDs_by_SP_ID(session, sp_id);
			if (all_IDs != null && all_IDs.size() > 0){
				sb.append(extension_s);
				Iterator all_IDs_it = all_IDs.iterator();
				while (all_IDs_it.hasNext()){
					int crt_ID = ((Integer) all_IDs_it.next()).intValue();
					sb.append(shared_ifc_set_id_s);
					sb.append(crt_ID);
					sb.append(shared_ifc_set_id_e);
				}
				sb.append(extension_e);
			}
			sb.append(service_profile_e);
		}
		sb.append(ims_subscription_e);
		
		logger.info("\n\nThe UserData XML document which is sent to the S-CSCF:\n" + sb.toString());
		return sb.toString();
	}
}
