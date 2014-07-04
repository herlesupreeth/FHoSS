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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.AVPDecodeException;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.cx.CxExperimentalResultException;
import de.fhg.fokus.hss.cx.CxFinalResultException;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.DB_Op;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.main.HSSProperties;

import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.auth.*;


/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class MAR {
	
	private static Logger logger = Logger.getLogger(MAR.class);
	
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
			if (publicIdentity == null){
                            logger.warn("Missing Public-Identity AVP");
                            throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);	
			}
			if (privateIdentity == null){
                            logger.warn("Missing User-Name AVP");
                            throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);	
			}

			// 1. check if the identities exist in HSS
			IMPU impu = IMPU_DAO.get_by_Identity(session, publicIdentity);
			IMPI impi = IMPI_DAO.get_by_Identity(session, privateIdentity);
			if (impu == null || impi == null){
                                logger.warn("User not found in HSS DB");
				throw new CxExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN);
			}
			
			// 2. check association
			IMPI_IMPU impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, impi.getId(), impu.getId());
			if (impi_impu == null){
                                logger.warn("IMPI and IMPU provided are not associated");
				throw new CxExperimentalResultException(
						DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_IDENTITIES_DONT_MATCH);
			}
			
			int auth_scheme = -1;
			byte[] authorization = null;
			AVP authDataItem = UtilAVP.getSipAuthDataItem(request);
			if (authDataItem == null){
                                logger.warn("SIP-Auth-Data-Item AVP not found");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			try {
				authDataItem.ungroup();
			} catch (AVPDecodeException e) {
				e.printStackTrace();
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			Vector childs = authDataItem.childs;
			if (childs == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			Iterator it = childs.iterator();			
			while (it.hasNext()){
				AVP child = (AVP)it.next();
				if (child.code == DiameterConstants.AVPCode.IMS_SIP_AUTHENTICATION_SCHEME){
					String data = new String(child.getData());
					if (data.equals(CxConstants.Auth_Scheme_AKAv1_Name)){
						auth_scheme = CxConstants.Auth_Scheme_AKAv1; 
					}
					else if (data.equals(CxConstants.Auth_Scheme_AKAv2_Name)){
						auth_scheme = CxConstants.Auth_Scheme_AKAv2;
					}
					else if (data.equals(CxConstants.Auth_Scheme_MD5_Name)){
						auth_scheme = CxConstants.Auth_Scheme_MD5;
					}
					else if (data.equals(CxConstants.Auth_Scheme_Digest_Name)){
						auth_scheme = CxConstants.Auth_Scheme_Digest;
					}
					else if (data.equals(CxConstants.Auth_Scheme_SIP_Digest_Name)){
						auth_scheme = CxConstants.Auth_Scheme_SIP_Digest;
					}
					else if (data.equals(CxConstants.Auth_Scheme_HTTP_Digest_MD5_Name)){
						auth_scheme = CxConstants.Auth_Scheme_HTTP_Digest_MD5;
					}
					else if (data.equals(CxConstants.Auth_Scheme_NASS_Bundled_Name)){
						auth_scheme = CxConstants.Auth_Scheme_NASS_Bundled;
					}
					else if (data.equals(CxConstants.Auth_Scheme_Early_Name)){
						auth_scheme = CxConstants.Auth_Scheme_Early;
					}
					else if (data.equals(CxConstants.Auth_Scheme_Unknown_Name) || data.equals(CxConstants.Auth_Scheme_Unknown_Name_2)){
						// if S- does not specify a scheme, the default one will be used!
						auth_scheme = impi.getDefault_auth_scheme();
					}
				}
				else if (child.code == DiameterConstants.AVPCode.IMS_SIP_AUTHORIZATION){
					authorization = child.data;
				}
			}
			
			if (auth_scheme == -1){
                                logger.warn("SIP-Authentication-Scheme not found or has invalid value");
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			// 3. check if the Authentication-Scheme is supported
			if ((auth_scheme & impi.getAuth_scheme()) == 0){
				throw new CxExperimentalResultException(
						DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_AUTH_SCHEME_NOT_SUPPORTED);
			}
			
			// 4. Synchronisation
			String scscf_name = IMSU_DAO.get_SCSCF_Name_by_IMSU_ID(session, impi.getId_imsu());
			String server_name = UtilAVP.getServerName(request);
			if (server_name == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			if (authorization != null){
				if (scscf_name == null || scscf_name.equals("")) {
					logger.error("Synchronization for: " + impi.getIdentity() + " but no scscf_name in db!");
					throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}
				 
				if (server_name.equals(scscf_name)){
					AuthenticationVector av = null;
					if ((av = synchronize(session, authorization, auth_scheme, impi)) == null){
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					UtilAVP.addPublicIdentity(response, publicIdentity);
					UtilAVP.addUserName(response, privateIdentity);
					
					List avList = new LinkedList();
					avList.add(av);
					
					// add the number of auth items (is 1 for synch)
					UtilAVP.addSIPNumberAuthItems(response, 1);
					UtilAVP.addAuthVectors(response, avList);
					UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
					return response;
				}
			}
			
			
			// 5. check the registration status
			
			int user_state = impu.getUser_state();
			int av_count = UtilAVP.getSipNumberAuthItems(request);
			if (av_count == -1){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			String orig_host = UtilAVP.getOriginatingHost(request);
			if (orig_host == null){
				throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			
			String realm = UtilAVP.getDestinationRealm(request);
			String uri = UtilAVP.getRequestUri(request);
			if (uri==null ||uri.length()==0) uri = realm;
			String method = UtilAVP.getRequestMethod(request);
			if (method==null ||method.length()==0) method="REGISTER";
			
			switch (user_state){
			
				case CxConstants.IMPU_user_state_Registered:
					if (scscf_name == null || scscf_name.equals("")) {
                                                logger.error("User is registered but has no S-CSCF name stored - HSS database consistency error");
						throw new CxFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
					}
					if (!scscf_name.equals(server_name)){
						
						IMSU_DAO.update(session, impi.getId_imsu(), server_name, orig_host);
						// send Sh Notifications for SCSCF_Name for all of the subscribers
						ShNotification_DAO.insert_notif_for_SCSCFName(session, impi.getId_imsu(), server_name);									
						
						impu.setUser_state(CxConstants.IMPU_user_state_Auth_Pending);
						IMPU_DAO.update(session, impu);
						
						UtilAVP.addPublicIdentity(response, publicIdentity);
						UtilAVP.addUserName(response, privateIdentity);
						List avList = MAR.generateAuthVectors(session, auth_scheme, av_count, impi, realm, uri, method);
						if (avList != null){
							UtilAVP.addSIPNumberAuthItems(response, avList.size());
							UtilAVP.addAuthVectors(response, avList);
							UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						}
						else{
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
						}
					}
					else{
						UtilAVP.addPublicIdentity(response, publicIdentity);
						UtilAVP.addUserName(response, privateIdentity);
						
						List avList = MAR.generateAuthVectors(session, auth_scheme, av_count, impi, realm, uri, method);
						if (avList != null){
							UtilAVP.addSIPNumberAuthItems(response, av_count);
							UtilAVP.addAuthVectors(response, avList);
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						}
						else{
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
						}
					}
					break;
					
				case CxConstants.IMPU_user_state_Unregistered:
				case CxConstants.IMPU_user_state_Not_Registered:
				case CxConstants.IMPU_user_state_Auth_Pending:
					
					if (user_state == CxConstants.IMPU_user_state_Unregistered){
						// reset the unregister state from tables
						// (If we were in the unregistered state before, an association IMPU-IMPI was set to Unregister)
						// As the IMPI for the Unregister state was chosen random (in the case of multiple associations), 
						//the reset is required!!!!!
						DB_Op.resetUserState(session, impu.getId_implicit_set());
					}
					
					if (scscf_name == null || scscf_name.equals("") || !scscf_name.equals(server_name)){
						IMSU_DAO.update(session, impi.getId_imsu(), server_name, orig_host);
						// send Sh Notifications for SCSCF_Name for all of the subscribers
						ShNotification_DAO.insert_notif_for_SCSCFName(session, impi.getId_imsu(), server_name);									
						
						// set the Auth-Pending State
						DB_Op.setUserState(session, impi.getId(), impu.getId_implicit_set(), CxConstants.IMPU_user_state_Auth_Pending,
								true);
						UtilAVP.addPublicIdentity(response, publicIdentity);
						UtilAVP.addUserName(response, privateIdentity);
						List avList = MAR.generateAuthVectors(session, auth_scheme, av_count, impi, realm, uri, method);
						if (avList != null){
							UtilAVP.addSIPNumberAuthItems(response, av_count);
							UtilAVP.addAuthVectors(response, avList);
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						}
						else{
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
						}
					}
					else{
						UtilAVP.addPublicIdentity(response, publicIdentity);
						UtilAVP.addUserName(response, privateIdentity);
						List avList = MAR.generateAuthVectors(session, auth_scheme, av_count, impi, realm, uri, method);
						if (avList != null){
							UtilAVP.addSIPNumberAuthItems(response, av_count);
							UtilAVP.addAuthVectors(response, avList);
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						}
						else{
							UtilAVP.addResultCode(response, 
									DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
						}
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
	
	private static AuthenticationVector synchronize(Session session, byte [] sipAuthorization, int auth_scheme, IMPI impi){
    	logger.info("Handling Synchronization between Mobile Station and Home Environment!");

        byte [] secretKey = HexCodec.getBytes(impi.getK(), CxConstants.Auth_Parm_Secret_Key_Size);

        try {
	            // get op and generate opC	
	            byte [] op = impi.getOp();
				byte [] opC = Milenage.generateOpC(secretKey, op);
				byte [] amf = impi.getAmf();

		        // sqnHE - represent the SQN from the HSS
		        // sqnMS - represent the SQN from the client side
				byte[] sqnHe = null;
				try{
					sqnHe = HexCodec.decode(impi.getSqn());	
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
		        
		        sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);

		        //byte [] nonce = new byte[32];
		        byte [] auts = new byte[14];
		        int k = 0;
		        for (int i = sipAuthorization.length - auts.length; i < sipAuthorization.length; i++, k++){
	        		auts[k] = sipAuthorization[i]; 
		        }
		        
		        byte [] rand = new byte[16];
		        k = 0;
		        for (int i = 0; i < 16; i++, k++){
	        		rand[k] = sipAuthorization[i]; 
		        }

		        byte[] ak = null;
		        if (HSSProperties.USE_AK){
	                ak = Milenage.f5star(secretKey, rand, opC);
	            }
		        byte [] sqnMs = new byte[6];
		        k = 0;
		        if (HSSProperties.USE_AK){
			        for (int i = 0; i < 6; i++, k++){
		        		sqnMs[k] = (byte) (auts[i] ^ ak[i]); 
			        }
			        logger.warn("USE_AK is enabled and will be used in Milenage algorithm!");
		        }
		        else{
			        for (int i = 0; i < 6; i++, k++){
		        		sqnMs[k] = auts[i]; 
			        }
			        logger.warn("USE_AK is NOT enabled and will NOT be used in Milenage algorithm!");
		        }
		        //System.out.println("SQN_MS is: " + codec.encode(sqnMs));
		        //System.out.println("SQN_HE is: " + codec.encode(sqnHe));
		        
		        if (DigestAKA.SQNinRange(sqnMs, sqnHe, HSSProperties.IND_LEN, HSSProperties.delta, HSSProperties.L)){
		        	logger.info("The new generated SQN value shall be accepted on the client, abort synchronization!");
		        	k = 0;
		        	byte[] copySqnHe = new byte[6];
			        for (int i = 0; i < 6; i++, k++){
		        		copySqnHe[k] = sqnHe[i]; 
			        }
		        	
		            AuthenticationVector aVector = 
		            	DigestAKA.getAuthenticationVector(auth_scheme, secretKey, opC, amf, copySqnHe);
		            IMPI_DAO.update(session, impi.getId(), HexCodec.encode(sqnHe));
		            
		            return aVector;
		        }
		        
		        // perform sync
		        
	        	byte xmac_s[] = Milenage.f1star(secretKey, rand, opC, sqnMs, amf);
	        	byte mac_s[] = new byte[8];
	        	k = 0;
	        	for (int i = 6; i < 14; i++, k++){
	        		mac_s[k] = auts[i];
	        	}
		        	
	        	for (int i = 0; i < 8; i ++)
	        		if (xmac_s[i] != mac_s[i]){
	        			logger.error("XMAC and MAC are different! User not authorized in performing synchronization!");
	        			return null;
	               }

	            sqnHe = sqnMs;
	            sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);
	     		logger.info("Synchronization of SQN_HE with SQN_MS was completed successfully!");
		        
		        byte[] copySqnHe = new byte[6];
		        k = 0;
		        for (int i = 0; i < 6; i++, k++){
	        		copySqnHe[k] = sqnHe[i]; 
		        }
	            AuthenticationVector aVector = 
	            	DigestAKA.getAuthenticationVector(auth_scheme, secretKey, opC, amf, copySqnHe);
	            
	            // update Cxdata
	            IMPI_DAO.update(session, impi.getId(), HexCodec.encode(sqnHe));
	            return aVector;
	            
			} 
	        catch (InvalidKeyException e) {
				e.printStackTrace();
				return null;
			}
	}

	public static AuthenticationVector generateAuthVector(Session session, int auth_scheme, IMPI impi, String realm, String uri, String method){
		
        byte [] secretKey;
        
        switch (auth_scheme){
        
    		case  CxConstants.Auth_Scheme_MD5:
    			// Authentication Scheme is Digest-MD5
    			logger.debug("Auth-Scheme is Digest-MD5");
    			SecureRandom randomAccess;
    			try {
    				randomAccess = SecureRandom.getInstance("SHA1PRNG");
    			} 
    			catch (NoSuchAlgorithmException e) {
    				e.printStackTrace();
    				return null;
    			}

    			byte[] randBytes = new byte[16];
    			randomAccess.setSeed(System.currentTimeMillis());
    			randomAccess.nextBytes(randBytes);
                
    			secretKey = impi.getK();
    			AuthenticationVector av = new AuthenticationVector(auth_scheme, randBytes, secretKey);
    			return av;
    		
    		case CxConstants.Auth_Scheme_AKAv1:
    		case CxConstants.Auth_Scheme_AKAv2:
        
    			// Authentication Scheme is AKAv1 or AKAv2
        		logger.debug("Auth-Scheme is Digest-AKA");
        		
        		secretKey = HexCodec.getBytes(impi.getK(), CxConstants.Auth_Parm_Secret_Key_Size);
        		byte [] amf = impi.getAmf();	
                byte [] op = impi.getOp();

                // generate opC        
                byte[] opC;
        		try {
        			opC = Milenage.generateOpC(secretKey, op);
        		} catch (InvalidKeyException e1) {
        			e1.printStackTrace();
        			return null;
        		}
        		
                byte [] sqn;
                try{
                	sqn = HexCodec.decode(impi.getSqn());
                }
                catch(Exception e){
                	e.printStackTrace();
                	return null;
                }
        		
        		sqn = DigestAKA.getNextSQN(sqn, HSSProperties.IND_LEN);
    	        byte[] copySqnHe = new byte[6];
    	        int k = 0;
    	        for (int i = 0; i < 6; i++, k++){
            		copySqnHe[k] = sqn[i]; 
    	        }
    	        
            	//System.out.println("auth:" + auth_scheme);
            	//System.out.println("secret:" + secretKey.length);
            	//System.out.println("opC:" + opC.length);
            	//System.out.println("amf:" + amf.length);
            	//System.out.println("SQN LEN:" + copySqnHe.length);
            	
				av = DigestAKA.getAuthenticationVector(auth_scheme, secretKey, opC, amf, copySqnHe);
            	//System.out.println("authenticate:" + av.getSipAuthenticate().length);
            	//System.out.println("auhtorization:" + av.getSipAuthorization().length);
            	//System.out.println("ck:" + av.getConfidentialityityKey().length);
            	//System.out.println("ik:" + av.getIntegrityKey().length);
				
                if (av != null){
                	IMPI_DAO.update(session, impi.getId(), HexCodec.encode(sqn));
                	//System.out.println("The last SQN is:" + HexCodec.encode(sqn));
                }        		
            	return av;
            	
    		case CxConstants.Auth_Scheme_Digest:
    			// Authentication Scheme is Digest
        		logger.debug("Auth-Scheme is Digest");
        
        		byte [] ha1= null;
        		secretKey = impi.getK();
        		
        		if (realm ==null)
        			realm = impi.getIdentity().substring(impi.getIdentity().indexOf("@")+1);
        		ha1 = MD5Util.av_HA1(impi.getIdentity().getBytes(),realm.getBytes(), secretKey);
        		byte [] ha1_hexa = HexCodec.encode(ha1).getBytes();
    			av = new AuthenticationVector(auth_scheme, realm.getBytes(), ha1_hexa);
    			return av;

    		case CxConstants.Auth_Scheme_SIP_Digest:
    			// Authentication Scheme is SIP Digest
        		logger.debug("Auth-Scheme is SIP Digest");
        
        		secretKey = impi.getK();
        		
        		if (realm ==null)
        			realm = impi.getIdentity().substring(impi.getIdentity().indexOf("@")+1);
        		ha1 = MD5Util.av_HA1(impi.getIdentity().getBytes(),realm.getBytes(), secretKey);
        		ha1_hexa = HexCodec.encode(ha1).getBytes();
    			av = new AuthenticationVector(auth_scheme, realm.getBytes(), ha1_hexa);
    			return av;

    		case CxConstants.Auth_Scheme_HTTP_Digest_MD5:
    			// Authentication Scheme is HTTP_Digest_MD5
        		logger.debug("Auth-Scheme is HTTP_Digest_MD5");
        
        		ha1= null;
        		secretKey = impi.getK();
        		
        		if (realm == null)
        			realm = impi.getIdentity().substring(impi.getIdentity().indexOf("@")+1);
        	
        		ha1 = MD5Util.av_HA1(impi.getIdentity().getBytes(),realm.getBytes(), secretKey);
        		
    			try {
    				randomAccess = SecureRandom.getInstance("SHA1PRNG");
    			} 
    			catch (NoSuchAlgorithmException e) {
    				e.printStackTrace();
    				return null;
    			}

    			byte[] authenticate = new byte[16];
    			randomAccess.setSeed(System.currentTimeMillis());
    			randomAccess.nextBytes(authenticate);
    			byte [] ha1_hex = HexCodec.encode(ha1).getBytes();
    			byte [] authenticate_hex = HexCodec.encode(authenticate).getBytes();
    			byte [] result = MD5Util.calcResponse(ha1_hex, authenticate, method.getBytes() /*from request */,uri.getBytes() /*from request */);
    			av = new AuthenticationVector(auth_scheme,uri, authenticate_hex, ha1_hex, result);
    			return av;
    		case CxConstants.Auth_Scheme_NASS_Bundled:
    			// Authentication Scheme is NASS_Bundled
        		logger.debug("Auth-Scheme is NASS_BUNDLED");
    			av = new AuthenticationVector(auth_scheme, impi.getLine_identifier());
    			return av;
    			
    		case CxConstants.Auth_Scheme_Early:
    			logger.debug("Auth-Scheme is Early IMS");
    			av = new AuthenticationVector(auth_scheme, impi.getIp());
    			return av;
        }
        
        return null;
		
	}
	
	public static List generateAuthVectors(Session session, int auth_scheme, int av_cnt, IMPI impi, String realm, String uri, String method){
        List avList = null;

        AuthenticationVector av = null;
        for (long ix = 0; ix < av_cnt; ix++){
        	av = generateAuthVector(session, auth_scheme, impi, realm, uri, method);
        	if (av == null)
        		break;
        	if (avList == null)
                avList = new LinkedList();
        	avList.add(av);
        }
        
        return avList;
	}
}
