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

package de.fhg.fokus.hss.diam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.AVPDecodeException;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.auth.AuthenticationVector;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.CapabilitiesSet;
import de.fhg.fokus.hss.db.model.ChargingInfo;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.Preferred_SCSCF_Set;
import de.fhg.fokus.hss.db.op.IMPU_DAO;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author andreea dot ancuta dot onofrei at fokus dot fraunhofer dot de 
 * Andreea Ancuta Onofrei / FOKUS Fraunhofer Institute
 */
public class UtilAVP {
	
	public static String getPublicIdentity(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static AVP getNextPublicIdentityAVP(DiameterMessage message, AVP last_avp){
		Iterator it;
		if (message.avps != null){
			it = message.avps.iterator();
			while (it.hasNext()){
				AVP crt_avp = (AVP) it.next();
				if (crt_avp != null && crt_avp.code == DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY){
					if (crt_avp.equals(last_avp))
						continue;
					else{
						return crt_avp;
					}
				}
			}
		}
		
		return null;
	}
	
	public static String getUserName(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.USER_NAME, true, DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static int getUserAuthorizationType(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_AUTHORIZATION_TYPE, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return avp.getIntData();
		}
		return 0;
	}

	public static int getUARFlags(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_UAR_FLAGS, false, 
				DiameterConstants.Vendor.V3GPP);		
		if (avp != null){
			return avp.getIntData();
		}
		return 0;
	}
	
	public static String getVisitedNetwork(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_VISITED_NETWORK_IDENTIFIER, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static AVP getSipAuthDataItem(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_AUTH_DATA_ITEM, true, 
				DiameterConstants.Vendor.V3GPP);
		return avp;
	}
	
	public static String getServerName(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SERVER_NAME, true, 
				DiameterConstants.Vendor.V3GPP);		
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}

	public static int getSipNumberAuthItems(DiameterMessage message){

		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_NUMBER_AUTH_ITEMS, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return avp.int_data;
		}
		return -1;
	}
	
	public static String getOriginatingHost(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.ORIGIN_HOST, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String (avp.data);
		}
		return null;
	}
	public static String getOriginatingRealm(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.ORIGIN_REALM, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static int getOriginatingRequest(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_ORIGINATING_REQUEST, false, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return avp.getIntData();
		}
		return -1;
		
	}
	
	public static String getDestinationHost(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.DESTINATION_HOST, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static String getDestinationRealm(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.DESTINATION_REALM, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static String getVendorSpecificApplicationID(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static String getAuthSessionState(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.AUTH_SESSION_STATE, true, 
				DiameterConstants.Vendor.DIAM);
		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static int getServerAssignmentType(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SERVER_ASSIGNMENT_TYPE, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return avp.getIntData();
		}
		return -1;
	}
	
	public static String getRequestUri(DiameterMessage message){
		AVP cAvp= null;
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_AUTH_DATA_ITEM, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp == null)
			return null;
		
		try {
			avp.ungroup();
			cAvp = avp.findChildAVP(DiameterConstants.AVPCode.AVP_ETSI_SIP_Authorization, true, 
					DiameterConstants.Vendor.VETSI);
			if (cAvp == null)
				return null;
		} 
		catch (AVPDecodeException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			cAvp.ungroup();
			AVP uriAvp = cAvp.findChildAVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_URI, true, 
					DiameterConstants.Vendor.VETSI);
			if (uriAvp == null)
				return null;
			return new String(uriAvp.data);
		}
		catch (AVPDecodeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getRequestMethod(DiameterMessage message){
		AVP cAvp= null;
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_AUTH_DATA_ITEM, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp == null)
			return null;
		
		try {
			avp.ungroup();
			cAvp = avp.findChildAVP(DiameterConstants.AVPCode.AVP_ETSI_SIP_Authorization, true, 
					DiameterConstants.Vendor.VETSI);
			if (cAvp == null)
				return null;
		} 
		catch (AVPDecodeException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			cAvp.ungroup();
			AVP uriAvp = cAvp.findChildAVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Method, true, 
					DiameterConstants.Vendor.VETSI);
			if (uriAvp == null)
				return null;
			return new String(uriAvp.data);
		}
		catch (AVPDecodeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getUserDataAlreadyAvailable(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_DATA_ALREADY_AVAILABLE, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp != null){
			return avp.getIntData();
		}
		return -1;
	}

	public static List<IMPU> getAllIMPU(Session session, DiameterMessage message){
		List<IMPU> impuIdentitiesList = null;
		Vector avpVector = message.avps;
		if (avpVector != null){
			Iterator it = avpVector.iterator();
			while (it.hasNext()){
				AVP currentAVP = (AVP) it.next();
				if (currentAVP.code == DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY){
					if (impuIdentitiesList == null){
						impuIdentitiesList = new ArrayList<IMPU>();
					}
					String identity = new String(currentAVP.data);
					IMPU impu = IMPU_DAO.get_by_Identity(session, identity);
					impuIdentitiesList.add(impu);
				}
			}
			return impuIdentitiesList;
		}
		return null;
	}

	public static List<String> getAssociatedIdentities(DiameterMessage message){
		List<String> result = null;
		
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_ASSOCIATED_IDENTITIES, false, 
				DiameterConstants.Vendor.V3GPP);
		
		if (avp == null)
			return null;
		
		Vector childs = avp.childs;
		String username = null;
		if (childs == null)
			return null;
		for (int i=0; i < childs.size(); i++){
			AVP child = (AVP)childs.get(i);
			username = new String(child.data);
			if (result == null){
				result = new ArrayList();
				result.add(username);
			}
		}
		return result;
	}
	
	
	public static void addServerCapabilities(DiameterMessage message, 
			List mandatory_cap_list, List optional_cap_list,
			List preferred_sever_name_list){
		
		AVP server_cap = new AVP(DiameterConstants.AVPCode.IMS_SERVER_CAPABILITIES, true, DiameterConstants.Vendor.V3GPP);
		
		Iterator it;
		CapabilitiesSet row;
		Preferred_SCSCF_Set prow;
		
		if (mandatory_cap_list != null){
			it = mandatory_cap_list.iterator();
			AVP mand_cap_avp;
			while (it.hasNext()){
				row = (CapabilitiesSet) it.next();
				mand_cap_avp = new AVP (DiameterConstants.AVPCode.IMS_MANDATORY_CAPABILITY, true, DiameterConstants.Vendor.V3GPP);
				mand_cap_avp.setData(row.getId_capability());
				server_cap.addChildAVP(mand_cap_avp);
			}
		}
		
		if (optional_cap_list != null ){
			it = optional_cap_list.iterator();
			AVP opt_cap_avp;
			while (it.hasNext()){
				row = (CapabilitiesSet) it.next();
				opt_cap_avp = new AVP (DiameterConstants.AVPCode.IMS_OPTIONAL_CAPABILITY, true, DiameterConstants.Vendor.V3GPP);
				opt_cap_avp.setData(row.getId_capability());
				server_cap.addChildAVP(opt_cap_avp);
			}
		}

		if (preferred_sever_name_list != null ){
			it = preferred_sever_name_list.iterator();
			AVP server_name_avp;
			while (it.hasNext()){
				prow = (Preferred_SCSCF_Set) it.next();
				server_name_avp = new AVP (DiameterConstants.AVPCode.IMS_SERVER_NAME, true, DiameterConstants.Vendor.V3GPP);
				server_name_avp.setData(prow.getScscf_name());
				server_cap.addChildAVP(server_name_avp);
			}
		}

		if ((mandatory_cap_list != null && mandatory_cap_list.size() > 0) || (optional_cap_list != null && optional_cap_list.size() > 0) || (preferred_sever_name_list != null && preferred_sever_name_list.size() > 0)){
			message.addAVP(server_cap);	
		}
	}
	
	public static void addResultCode(DiameterMessage message, int resultCode){
		AVP avp = new AVP(DiameterConstants.AVPCode.RESULT_CODE, true, DiameterConstants.Vendor.DIAM);
		avp.setData(resultCode);
		message.addAVP(avp);
	}

	public static void addExperimentalResultCode(DiameterMessage message, int resultCode, int vendor){
		AVP group = new AVP(DiameterConstants.AVPCode.EXPERIMENTAL_RESULT, true, DiameterConstants.Vendor.DIAM);
		AVP vendorID = new AVP(DiameterConstants.AVPCode.VENDOR_ID, true, DiameterConstants.Vendor.DIAM);
		vendorID.setData(vendor);
		group.addChildAVP(vendorID);
		
		AVP expResult = new AVP(DiameterConstants.AVPCode.EXPERIMENTAL_RESULT_CODE, true, DiameterConstants.Vendor.DIAM); 
		expResult.setData(resultCode);
		group.addChildAVP(expResult);
		message.addAVP(group);
	}
	
	public static void addServerName(DiameterMessage message, String serverName){
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SERVER_NAME, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(serverName);
		message.addAVP(avp);
	}
	
	
	public static void addVendorSpecificApplicationID(DiameterMessage message, int vendorID, int appID){
        AVP vendorAppID_AVP = new AVP(DiameterConstants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, DiameterConstants.Vendor.DIAM);
        
        AVP vendorID_AVP = new AVP(DiameterConstants.AVPCode.VENDOR_ID, true, DiameterConstants.Vendor.DIAM);
        vendorID_AVP.setData(vendorID);
        vendorAppID_AVP.addChildAVP(vendorID_AVP);
        
        AVP appID_AVP = new AVP(DiameterConstants.AVPCode.AUTH_APPLICATION_ID, true,  DiameterConstants.Vendor.DIAM);
        appID_AVP.setData(appID);
        vendorAppID_AVP.addChildAVP(appID_AVP);
        
        message.addAVP(vendorAppID_AVP);
	}

	public static void addAuthSessionState(DiameterMessage message, int state){
	    AVP authSession = new AVP(DiameterConstants.AVPCode.AUTH_SESSION_STATE, true, DiameterConstants.Vendor.DIAM);
	    authSession.setData(state);
	    message.addAVP(authSession);
	}

	public static void addPublicIdentity(DiameterMessage message, String publicIdentity){
	    AVP publicIdentityAVP = new AVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY, 
	    		true, DiameterConstants.Vendor.V3GPP);
	    publicIdentityAVP.setData(publicIdentity);
	    message.addAVP(publicIdentityAVP);
	}
	
	public static void addUserName(DiameterMessage message, String userName){
	    AVP userNameAVP = new AVP(DiameterConstants.AVPCode.USER_NAME, 
	    		true, DiameterConstants.Vendor.DIAM);
	    userNameAVP.setData(userName);
	    message.addAVP(userNameAVP);
	}
	
	public static AVP addEtsiSipAuthenticate(String realm, byte[] nonce, String domain, String algorithm, byte[] auth_ha1)
	{
		AVP etsiSipAuth = new AVP(DiameterConstants.AVPCode.AVP_ETSI_SIP_Authenticate, true, DiameterConstants.Vendor.VETSI);	
		if (realm != null)
		{
			AVP realmAVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Realm, 
		    		true, DiameterConstants.Vendor.VETSI);
		    realmAVP.setData(realm);
		    etsiSipAuth.addChildAVP(realmAVP);
		}	
		
		if (nonce != null)
		{
			AVP nonceAVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Nonce, 
		    		true, DiameterConstants.Vendor.VETSI);
		    nonceAVP.setData(nonce);
		    etsiSipAuth.addChildAVP(nonceAVP);
		}

		if (domain != null)
		{
			AVP domainAVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Domain, 
		    		true, DiameterConstants.Vendor.VETSI);
		    domainAVP.setData(domain);
		    etsiSipAuth.addChildAVP(domainAVP);
		}

		if (algorithm != null)
		{
			AVP algAVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Algorithm, 
		    		true, DiameterConstants.Vendor.VETSI);
		    algAVP.setData(algorithm);
		    etsiSipAuth.addChildAVP(algAVP);
		}

		if (auth_ha1 != null)
		{
			AVP ha1AVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_HA1, 
		    		true, DiameterConstants.Vendor.VETSI);
		    ha1AVP.setData(auth_ha1);
		    etsiSipAuth.addChildAVP(ha1AVP);
		}		
		
		return etsiSipAuth;
	}
	
	public static AVP addEtsiSipAuthenticationInfo(byte [] r_auth)
	{	
		AVP etsiSipAuth = new AVP(DiameterConstants.AVPCode.AVP_ETSI_SIP_Authentication_Info, true, DiameterConstants.Vendor.VETSI);
		if (r_auth != null)
		{
			AVP authAVP = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Response_Auth, 
		    		true, DiameterConstants.Vendor.VETSI);
			authAVP.setData(r_auth);
		    etsiSipAuth.addChildAVP(authAVP);
		}	
		
		return etsiSipAuth;
	}
	
	public static void addAuthVectors(DiameterMessage message, List avList){
		if (avList == null){
			return;
		}
		
		Iterator it = avList.iterator();
		int itemNo = 1;
		while (it.hasNext()){
			AuthenticationVector av = (AuthenticationVector) it.next();
			
            AVP authDataItem = new AVP(DiameterConstants.AVPCode.IMS_SIP_AUTH_DATA_ITEM, true, DiameterConstants.Vendor.V3GPP);
            
            AVP itemNumber = new AVP(DiameterConstants.AVPCode.IMS_SIP_ITEM_NUMBER, true, DiameterConstants.Vendor.V3GPP);
            itemNumber.setData(itemNo++);
            authDataItem.addChildAVP(itemNumber);

            AVP authScheme = new AVP(DiameterConstants.AVPCode.IMS_SIP_AUTHENTICATION_SCHEME, 
            		true, DiameterConstants.Vendor.V3GPP);
            
            switch (av.getAuth_scheme()){
            	case CxConstants.Auth_Scheme_AKAv1:
            		authScheme.setData(CxConstants.Auth_Scheme_AKAv1_Name);
            		break;
            	case CxConstants.Auth_Scheme_AKAv2:
            		authScheme.setData(CxConstants.Auth_Scheme_AKAv2_Name);
            		break;
            	case CxConstants.Auth_Scheme_MD5:
            		authScheme.setData(CxConstants.Auth_Scheme_MD5_Name);
            		break;
            	case CxConstants.Auth_Scheme_Digest:
            		authScheme.setData(CxConstants.Auth_Scheme_Digest_Name);
            		break;
            	case CxConstants.Auth_Scheme_SIP_Digest:
            		authScheme.setData(CxConstants.Auth_Scheme_SIP_Digest_Name);
            		break;
            	case CxConstants.Auth_Scheme_HTTP_Digest_MD5:
            		authScheme.setData(CxConstants.Auth_Scheme_HTTP_Digest_MD5_Name);
            		break;
            	case CxConstants.Auth_Scheme_NASS_Bundled:
            		authScheme.setData(CxConstants.Auth_Scheme_NASS_Bundled_Name);
            		break;
            	case CxConstants.Auth_Scheme_Early:
            		authScheme.setData(CxConstants.Auth_Scheme_Early_Name);
            		break;
            }
            
            authDataItem.addChildAVP(authScheme);
            
            if(((av.getAuth_scheme() & CxConstants.Auth_Scheme_Early) != 0)){
                AVP ip = new AVP(DiameterConstants.AVPCode.FRAMED_IP_ADDRESS, true, 0);
                String sip = av.getIp();
                if (sip != null)
                {
                	byte fIpLen = 4;
                	int poz = 0,k = 0;
                	byte [] result = new byte[fIpLen] ; 
                	for (int i = 0; i< sip.length() ; i++)
                		if (sip.charAt(i) == '.' && k < fIpLen)
                		{
                			try {
                				result[k++] = (byte)Integer.parseInt(sip.substring(poz,i));
                			}
                			catch (NumberFormatException nfe)
                			{
                				result[k++] = -1;
                			}
                			poz = i + 1;
                		}
                	if (k < fIpLen && poz < sip.length())
                	{
                		try {
                			result[k]= (byte) Integer.parseInt(sip.substring(poz));
                		}
                		catch (NumberFormatException nfe)
            			{
            				result[k] = -1;
            			}
                	}
                	ip.setData(result);
                    authDataItem.addChildAVP(ip);
                }
            }
            else if(((av.getAuth_scheme() & CxConstants.Auth_Scheme_NASS_Bundled) != 0)){
            	AVP ip = new AVP(DiameterConstants.AVPCode.AVP_Line_Identifier, true, DiameterConstants.Vendor.VETSI);
                ip.setData((av.getIp()));
                authDataItem.addChildAVP(ip);
            }
            else if(((av.getAuth_scheme() & CxConstants.Auth_Scheme_Digest) != 0)){
            	 AVP authenticate = new AVP(DiameterConstants.AVPCode.AVP_CableLabs_Digest_Realm, true, 
                 		DiameterConstants.Vendor.VCableLabs);
                 authenticate.setData(av.getSipAuthenticate());
                 authDataItem.addChildAVP(authenticate);

                 AVP ha1 = new AVP(DiameterConstants.AVPCode.AVP_CableLabs_Digest_HA1, true, 
                 		DiameterConstants.Vendor.VCableLabs);
                 ha1.setData(av.getSipAuthorization());
                 AVP authorization = new AVP(DiameterConstants.AVPCode.AVP_CableLabs_SIP_Digest_Authenticate, true, 
                  		DiameterConstants.Vendor.VCableLabs);
                 authorization.addChildAVP(ha1);
                 authDataItem.addChildAVP(authorization);
            } 
            else if(((av.getAuth_scheme() & CxConstants.Auth_Scheme_SIP_Digest) != 0)){
                 AVP ha1 = new AVP(DiameterConstants.AVPCode.DIGEST_HA1, true, 
                 		DiameterConstants.Vendor.DIAM);
                 ha1.setData(av.getSipAuthorization());

                 AVP authorization = new AVP(DiameterConstants.AVPCode.IMS_SIP_DIGEST_AUTHENTICATE, true, 
                  		DiameterConstants.Vendor.V3GPP);
                 authorization.addChildAVP(ha1);

                 AVP authenticate = new AVP(DiameterConstants.AVPCode.DIGEST_REALM, true, 
                 		DiameterConstants.Vendor.DIAM);
                 authenticate.setData(av.getSipAuthenticate());
                 authorization.addChildAVP(authenticate);

                 authDataItem.addChildAVP(authorization);
            } 
            else if(((av.getAuth_scheme() & CxConstants.Auth_Scheme_HTTP_Digest_MD5) != 0)){
            	AVP authenticate = new AVP(DiameterConstants.AVPCode.AVP_ETSI_Digest_Realm, true, 
                 		DiameterConstants.Vendor.VCableLabs);
                 authenticate.setData(av.getSipAuthenticate());
                 authDataItem.addChildAVP(authenticate);
                 AVP etsi_auth = UtilAVP.addEtsiSipAuthenticate(av.getRealm(), av.getSipAuthenticate(), null, CxConstants.Auth_Scheme_HTTP_Digest_MD5_Name, av.getHA1());
                 AVP etsi_auth_info = UtilAVP.addEtsiSipAuthenticationInfo(av.getResult());
                 if (etsi_auth != null)
                	 authDataItem.addChildAVP(etsi_auth);
                 if (etsi_auth_info != null)
                	 authDataItem.addChildAVP(etsi_auth_info);
            }
            else{
                AVP authenticate = new AVP(DiameterConstants.AVPCode.IMS_SIP_AUTHENTICATE, true, 
                		DiameterConstants.Vendor.V3GPP);
                authenticate.setData(av.getSipAuthenticate());
                authDataItem.addChildAVP(authenticate);

                AVP authorization = new AVP(DiameterConstants.AVPCode.IMS_SIP_AUTHORIZATION, true, 
                		DiameterConstants.Vendor.V3GPP);
                authorization.setData(av.getSipAuthorization());
                authDataItem.addChildAVP(authorization);
                
                if (av.getConfidentialityityKey() != null){
                	AVP confidentialityKey = new AVP(DiameterConstants.AVPCode.IMS_CONFIDENTIALITY_KEY, true, 
                			DiameterConstants.Vendor.V3GPP);
                	confidentialityKey.setData(av.getConfidentialityityKey());
                	authDataItem.addChildAVP(confidentialityKey);
                
                }
                
                if (av.getIntegrityKey() != null){
                	AVP integrityKey = new AVP(DiameterConstants.AVPCode.IMS_INTEGRITY_KEY, true, 
                			DiameterConstants.Vendor.V3GPP);
                	integrityKey.setData(av.getIntegrityKey());
                	authDataItem.addChildAVP(integrityKey);
                }
                
            }
            message.addAVP(authDataItem);
		}
	}
	
	public static void addUserData(DiameterMessage message, String data){
		AVP userData = new AVP(DiameterConstants.AVPCode.IMS_USER_DATA, true, DiameterConstants.Vendor.V3GPP);
		userData.setData(data);
		message.addAVP(userData);
	}

	public static void addShData(DiameterMessage message, String data){
		AVP userData = new AVP(DiameterConstants.AVPCode.IMS_USER_DATA_SH, true, DiameterConstants.Vendor.V3GPP);
		userData.setData(data);
		message.addAVP(userData);
	}
	
	public static void addChargingInformation(DiameterMessage message, ChargingInfo chargingInfo){
		AVP chargingInfoAVP = new AVP(DiameterConstants.AVPCode.IMS_CHARGING_INFORMATION, true, 
				DiameterConstants.Vendor.V3GPP);
		
		if (chargingInfo.getPri_ccf() != null && !chargingInfo.getPri_ccf().equals("")){
			AVP pri_ccf_AVP = new AVP(DiameterConstants.AVPCode.IMS_PRI_CHRG_COLL_FN_NAME, true, 
					DiameterConstants.Vendor.V3GPP);
			pri_ccf_AVP.setData(chargingInfo.getPri_ccf());
			chargingInfoAVP.addChildAVP(pri_ccf_AVP);
		}

		if (chargingInfo.getPri_ecf() != null && !chargingInfo.getPri_ecf().equals("")){
			AVP pri_ecf_AVP = new AVP(DiameterConstants.AVPCode.IMS_PRI_EVENT_CHARGING_FN_NAME, true, 
					DiameterConstants.Vendor.V3GPP);
			pri_ecf_AVP.setData(chargingInfo.getPri_ecf());
			chargingInfoAVP.addChildAVP(pri_ecf_AVP);
		}
		
		if (chargingInfo.getSec_ccf() != null && !chargingInfo.getSec_ccf().equals("")){
			AVP sec_ccf_AVP = new AVP(DiameterConstants.AVPCode.IMS_SEC_CHRG_COLL_FN_NAME, true, 
					DiameterConstants.Vendor.V3GPP);
			sec_ccf_AVP.setData(chargingInfo.getSec_ccf());
			chargingInfoAVP.addChildAVP(sec_ccf_AVP);
		}

		if (chargingInfo.getSec_ecf() != null && !chargingInfo.getSec_ecf().equals("")){
			AVP sec_ecf_AVP = new AVP(DiameterConstants.AVPCode.IMS_SEC_EVENT_CHARGING_FN_NAME, true, 
					DiameterConstants.Vendor.V3GPP);
			sec_ecf_AVP.setData(chargingInfo.getSec_ecf());
			chargingInfoAVP.addChildAVP(sec_ecf_AVP);
		}
		
		message.addAVP(chargingInfoAVP);
	}
	
	public static void addAsssociatedIdentities(DiameterMessage message, List identitiesList){
		AVP associatedIdentities = new AVP(DiameterConstants.AVPCode.IMS_ASSOCIATED_IDENTITIES, false, 
			DiameterConstants.Vendor.V3GPP);
		
		AVP userName;
		Iterator it = identitiesList.iterator();
		while (it.hasNext()){
			IMPI impi =  (IMPI) it.next();
			userName = new AVP(DiameterConstants.AVPCode.USER_NAME, true, DiameterConstants.Vendor.DIAM);
			userName.setData(impi.getIdentity());
			associatedIdentities.addChildAVP(userName);
		}
		message.addAVP(associatedIdentities);
	}

	public static void addDeregistrationReason(DiameterMessage message, int reasonCode, String reasonInfo){
		AVP deregistrationReasonAVP = new AVP(DiameterConstants.AVPCode.IMS_DEREGISTRATION_REASON, true, 
				DiameterConstants.Vendor.V3GPP);

		AVP reasonCodeAVP = new AVP(DiameterConstants.AVPCode.IMS_REASON_CODE, true, 
				DiameterConstants.Vendor.V3GPP);
		reasonCodeAVP.setData(reasonCode);
		deregistrationReasonAVP.addChildAVP(reasonCodeAVP);
		
		if (reasonInfo != null && !reasonInfo.equals("")){
			AVP reasonInfoAVP = new AVP(DiameterConstants.AVPCode.IMS_REASON_INFO, true, 
					DiameterConstants.Vendor.V3GPP);
			reasonInfoAVP.setData(reasonInfo);
			deregistrationReasonAVP.addChildAVP(reasonInfoAVP);
		}
		message.addAVP(deregistrationReasonAVP);
	}
	
	public static void addDestinationHost(DiameterMessage message, String host){
		AVP destHostAVP = new AVP(DiameterConstants.AVPCode.DESTINATION_HOST, true, 
				DiameterConstants.Vendor.DIAM);
		destHostAVP.setData(host);
		message.addAVP(destHostAVP);
	}
	
	public static void addDestinationRealm(DiameterMessage message, String realm){
		AVP destRealm = new AVP(DiameterConstants.AVPCode.DESTINATION_REALM, true,
				DiameterConstants.Vendor.DIAM);
		destRealm.setData(realm);
		message.addAVP(destRealm);
	}
	
	public static void addSIPNumberAuthItems(DiameterMessage message, int cnt){
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SIP_NUMBER_AUTH_ITEMS, true,
				DiameterConstants.Vendor.V3GPP);
		avp.setData(cnt);
		message.addAVP(avp);
	}

	
	public static void addSessionID(DiameterMessage message, String FQDN, int sessionID){
		AVP avp = new AVP(DiameterConstants.AVPCode.SESSION_ID, true, DiameterConstants.Vendor.DIAM);
		StringBuffer sBuff = new StringBuffer();
		sBuff.append(FQDN);
		sBuff.append(";");
		sBuff.append(System.currentTimeMillis());
		sBuff.append(";");
		sBuff.append(sessionID);
		avp.setData(sBuff.toString());
		message.addAVP(avp);
	}
	
	// methods for fields used by Sh Interface
	
	public static Vector getAllDataReference(DiameterMessage message){
		
		Vector result = null;
		if (message.avps != null && message.avps.size() > 0){
			Iterator it = message.avps.iterator();
			AVP avp;
			while (it.hasNext()){
				avp = (AVP) it.next();
				if (avp.code == DiameterConstants.AVPCode.IMS_DATA_REFERENCE && 
						avp.flag_mandatory == true && 
						avp.vendor_id == DiameterConstants.Vendor.V3GPP){
					if (result == null){
						result = new Vector();
					}
					result.add(new Integer(avp.int_data));
				}
			}
		}
		return result;
	}

	public static int getDataReference(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_DATA_REFERENCE, true, 
				DiameterConstants.Vendor.V3GPP);
		
		if (avp != null){
			return avp.int_data;
		}
		
		return -1;
	}
	
	public static String getShUserIdentity(DiameterMessage message){
		// return the Public Identity or the MSISDN AVP
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_IDENTITY, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp != null){
			try {
				avp.ungroup();
				if (avp.childs != null && avp.childs.size() > 0){
					AVP child_avp = (AVP) avp.childs.get(0);
					return new String(child_avp.data);
				}
			} 
			catch (AVPDecodeException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String getServiceIndication(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SERVICE_INDICATION, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static Vector getAllServiceIndication(DiameterMessage message){
		Vector result = null;
		
		if (message.avps != null && message.avps.size() > 0){
			Iterator it = message.avps.iterator();
			AVP avp;
			while (it.hasNext()){
				avp = (AVP) it.next();
				if (avp.code == DiameterConstants.AVPCode.IMS_SERVICE_INDICATION && 
						avp.flag_mandatory == true && 
						avp.vendor_id == DiameterConstants.Vendor.V3GPP){
					if (result == null){
						result = new Vector();
					}
					result.add(new String(avp.data));
				}
			}
		}
		
		return result;
	}

	public static String getShUserData(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_DATA_SH, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp != null){
			return new String(avp.data);
		}
		return null;
	}
	
	public static int getSubsReqType(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SUBSCRIBTION_REQ_TYPE, true, 
				DiameterConstants.Vendor.V3GPP);

		if (avp == null){
			return -1;
		}
		
		return avp.int_data;
	}

	public static int getExpiryTime(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_EXPIRY_TIME, false, 
				DiameterConstants.Vendor.V3GPP);
		if (avp == null){
			return -1;
		}
		return avp.int_data;
	}

	public static int getSendDataIndication(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SEND_DATA_INDICATION, false, 
				DiameterConstants.Vendor.V3GPP);
		if (avp == null){
			return -1;
		}
		return avp.int_data;
	}
	
	public static String getDSAITag(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_DSAI_TAG, true, 
				DiameterConstants.Vendor.V3GPP);
		if (avp == null)
			return null;
		
		return new String(avp.data);
	}
	
	public static int getIdentitySet(DiameterMessage message){
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_IDENTITY_SET, false, 
				DiameterConstants.Vendor.V3GPP);
		if (avp == null)
			return -1;
		
		return avp.int_data;
	}	
	
	// add methods for Sh
	public static void addExpiryTime(DiameterMessage message, int expiry_time){
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_EXPIRY_TIME, false, 
				DiameterConstants.Vendor.V3GPP);
		avp.setData(expiry_time);
		message.addAVP(avp);
	}
	
	
	public static void addShUserIdentity(DiameterMessage message, String userIdentity){
		AVP shUserIdentity = new AVP(DiameterConstants.AVPCode.IMS_USER_IDENTITY, true, 
				DiameterConstants.Vendor.V3GPP);
		AVP publicIdentity = new AVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY_SH, true, 
				DiameterConstants.Vendor.V3GPP);
		publicIdentity.setData(userIdentity);
		shUserIdentity.addChildAVP(publicIdentity);
		message.addAVP(shUserIdentity);
	}
	

	// methods for Zh
	
	public static void addGUSS(DiameterMessage message, String gussXML){
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_GBA_USER_SEC_SETTINGS, true, 
				DiameterConstants.Vendor.V3GPP);
		avp.setData(gussXML);
		message.addAVP(avp);
	}
}
