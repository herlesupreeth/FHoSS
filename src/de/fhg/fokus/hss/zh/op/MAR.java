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


package de.fhg.fokus.hss.zh.op;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.*;
import de.fhg.fokus.hss.auth.*;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.*;
import de.fhg.fokus.hss.db.op.*;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.main.HSSProperties;
import de.fhg.fokus.hss.web.util.Tuple;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.zh.*;
import java.security.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class MAR{
    private static Logger logger = Logger.getLogger(MAR.class);


    public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request){
        DiameterMessage response;
        
        response = diameterPeer.newResponse(request);
        response.flagProxiable = true;
        
		UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);
        
		boolean dbException = false;		
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			String privateIdentity = UtilAVP.getUserName(request);
			if (privateIdentity == null){
				throw new ZhFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);	
			}
	
			// 1. check if the identity exist in HSS
			IMPI impi = IMPI_DAO.get_by_Identity(session, privateIdentity);
			if (impi == null){
				throw new ZhExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_UNKNOWN);
			}
			
			int auth_scheme = -1;
			byte[] authorization = null;
			AVP authDataItem = UtilAVP.getSipAuthDataItem(request);
			
			if (authDataItem != null){
				try {
					authDataItem.ungroup();
				} catch (AVPDecodeException e) {
					e.printStackTrace();
					throw new ZhFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
				}
				Vector childs = authDataItem.childs;
				if (childs == null){
					throw new ZhFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
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
					}
					else if (child.code == DiameterConstants.AVPCode.IMS_SIP_AUTHORIZATION){
						authorization = child.data;
					}
				}
			}
			
			if (auth_scheme == -1){
				auth_scheme = impi.getZh_default_auth_scheme();
			}
			
			// 3. check if the Authentication-Scheme is supported
			if ((auth_scheme & impi.getAuth_scheme()) == 0){
				throw new ZhExperimentalResultException(
						DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_AUTH_SCHEME_NOT_SUPPORTED);
			}
			
			// 4. Synchronisation
			
			if (authorization != null){
				AuthenticationVector av = null;
				if ((av = synchronize(session, authorization, auth_scheme, impi)) == null){
					throw new ZhFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}
				UtilAVP.addUserName(response, privateIdentity);
					
				List avList = new LinkedList();
				avList.add(av);
					
				// add the number of auth items (is 1 for synch)
				UtilAVP.addSIPNumberAuthItems(response, 1);
				UtilAVP.addAuthVectors(response, avList);
					
				// add guss
				String gussXML = getGUSS(session, impi); 
				UtilAVP.addGUSS(response, gussXML);
				
				// add Result Code
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
				return response;
			}

			// generation of the Auth Vectors
			int av_count = UtilAVP.getSipNumberAuthItems(request);
			if (av_count == -1){
				av_count = 1;
			}
			List avList = MAR.generateAuthVectors(session, auth_scheme, av_count, impi);
			if (avList != null){
				UtilAVP.addUserName(response, privateIdentity);
				UtilAVP.addSIPNumberAuthItems(response, avList.size());
				UtilAVP.addAuthVectors(response, avList);
				String gussXML = getGUSS(session, impi); 
				UtilAVP.addGUSS(response, gussXML);
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
			}
			else{
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
			}
		}
		catch(ZhExperimentalResultException e){
			UtilAVP.addExperimentalResultCode(response, e.getErrorCode(), e.getVendor());
			e.printStackTrace();
		}
		catch(ZhFinalResultException e){
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

    private static List generateAuthVectors(Session session, int auth_scheme, int av_cnt, IMPI impi)
    {
        List avList = null;
        AuthenticationVector av = null;
        for(long ix = 0L; ix < (long)av_cnt; ix++)
        {
            av = generateAuthVector(session, auth_scheme, impi);
            if(av == null)
                break;
            if(avList == null)
                avList = new LinkedList();
            avList.add(av);
        }

        return avList;
    }
    
	public static AuthenticationVector generateAuthVector(Session session, int auth_scheme, IMPI impi){
		
        byte [] secretKey;
                
        switch (auth_scheme){
        
    		case  CxConstants.Auth_Scheme_MD5:
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

                // op and generate opC	
                byte [] op = impi.getOp();
                
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
				av = DigestAKA.getAuthenticationVector(auth_scheme, secretKey, opC, amf, copySqnHe);
                if (av != null){
                	IMPI_DAO.update(session, impi.getId(), HexCodec.encode(sqn));
                	//System.out.println("The last SQN is:" + HexCodec.encode(sqn));
                }        		
            	return av;
        }
        return null;
	}

    private static final String xml_s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String guss_s = "<guss ";
    private static final String guss_e = "</guss>";
    private static final String bsfInfo_s = "<bsfInfo>";
    private static final String bsfInfo_e = "</bsfInfo>";
    private static final String uiccType_s = "<uiccType>";
    private static final String uiccType_e = "</uiccType>";
    private static final String lifeTime_s = "<lifeTime>";
    private static final String lifeTime_e = "</lifeTime>";
    private static final String ussList_s = "<ussList>";
    private static final String ussList_e = "</ussList>";
    private static final String uss_s = "<uss ";
    private static final String uss_e = "</uss>";
    private static final String uids_s = "<uids>";
    private static final String uids_e = "</uids>";
    private static final String uid_s = "<uid>";
    private static final String uid_e = "</uid>";
    private static final String flags_s = "<flags>";
    private static final String flags_e = "</flags>";
    private static final String flag_s = "<flag>";
    private static final String flag_e = "</flag>";
    
    public static String getGUSS(Session session, IMPI impi){
        StringBuffer sBuff = new StringBuffer();
        sBuff.append(xml_s);
        sBuff.append(guss_s);
        sBuff.append((new StringBuilder()).append("id=\"").append(impi.getIdentity()).append("\">").toString());
        
        // add bsfInfo
        sBuff.append(bsfInfo_s);
        sBuff.append(uiccType_s);
        sBuff.append(((Tuple)WebConstants.select_uicc_type.get(impi.getZh_uicc_type())).getName());
        sBuff.append(uiccType_e);
        if(impi.getZh_key_life_time() >= 0){
            sBuff.append(lifeTime_s);
            sBuff.append(impi.getZh_key_life_time());
            sBuff.append(lifeTime_e);
        }
        sBuff.append(bsfInfo_e);
        
        // add ussList
        sBuff.append(ussList_s);
        List zh_USS_List = Zh_USS_DAO.get_all_for_IMPI_ID(session, impi.getId());
        if(zh_USS_List != null && zh_USS_List.size() > 0){
            List impuList = IMPU_DAO.get_all_for_IMPI_ID(session, impi.getId());
            if(impuList != null && impuList.size() > 0){
                
                for(int i = 0; i < zh_USS_List.size(); i++){
                    Zh_USS zh_uss = (Zh_USS)zh_USS_List.get(i);
                    // add uss
                    sBuff.append(uss_s);
                    sBuff.append((new StringBuilder()).append("id=\"").append(zh_uss.getType()).append("\" type=\"").append(zh_uss.getType()).append("\"").toString());
                    if(zh_uss.getNaf_group()!= null && !zh_uss.getNaf_group().equals(""))
                        sBuff.append((new StringBuilder()).append(" nafGroup=\"").append(zh_uss.getNaf_group()).append("\"").toString());
                    sBuff.append(">");
                    
                    // add uids
                    sBuff.append(uids_s);
                    for(int j = 0; j < impuList.size(); j++){
                        IMPU crtIMPU = (IMPU)impuList.get(j);
                        sBuff.append(uid_s);
                        sBuff.append(crtIMPU.getIdentity());
                        sBuff.append(uid_e);
                    }
                    sBuff.append(uids_e);
                    
                    // add flags
                    sBuff.append(flags_s);
                    if(zh_uss.getFlags() > 0){
                        
                        int flags = zh_uss.getFlags();
                        int crt_flag = 0;
                        while(flags > 0) {
                            if((flags & ZhConstants.GAA_Authorization_Authentication_Allowed) != 0){
                                crt_flag = ZhConstants.GAA_Authorization_Authentication_Allowed;
                                flags -= ZhConstants.GAA_Authorization_Authentication_Allowed;
                            } 
                            else if((flags & ZhConstants.GAA_Authorization_Non_Repudiation_Allowed) != 0){
                                crt_flag = ZhConstants.GAA_Authorization_Non_Repudiation_Allowed;
                                flags -= ZhConstants.GAA_Authorization_Non_Repudiation_Allowed;
                            }
                            sBuff.append(flag_s);
                            sBuff.append(crt_flag);
                            sBuff.append(flag_e);
                        }
                    }
                    sBuff.append(flags_e);
                    sBuff.append(uss_e);
                }
                
            }
        }
        sBuff.append(ussList_e);
        sBuff.append(guss_e);
        return sBuff.toString();
    }
}
