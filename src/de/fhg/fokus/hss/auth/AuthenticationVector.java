/*
 * $Id
 *
 * Copyright (C) 2004-2006 FhG Fokus
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

package de.fhg.fokus.hss.auth;

import java.net.Inet4Address;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;

/**
 * This class represents authententication vector that is produced in an HSS
 * during the authentication procedure of the user.It contains the necessary 
 * parameters for that.
 * 
 * @author Peter Weik (pwe at fokus dot fraunhofer dot de)
 *
 */
public class AuthenticationVector {
	
	/**
	 * the authentication scheme used in the authentication of SIP messages.	
	 */
	
	private int auth_scheme;

	/** 
	 * ip address related to early ims 
	 */
	private String ip;
	
	/**
     *  the sip authenticate represents the data portion of the WWW-Authenticate 
     *  or Proxy-Authenticate SIP headers that are to be present in a SIP response.
	 * 
	 */
	private byte [] sipAuthenticate; // 32 bytes
	// RAND(16) || AUTN(16)= (sqn xor ak)(6)||amf(2)||mac(8)
	
   /**
    * represents the data portion of the Authorization or Proxy-Authorization 
    * SIP headers suitable for inclusion in a SIP request
    */ 
	
	private byte [] sipAuthorization; // at least 8 bytes
	
	/**
	 * the confidentiality key
	 */
	private byte [] confidentialityityKey; // 16 bytes
	
	/**
	 * the integrity key
	 */
	private byte [] integrityKey; // 16 bytes
	
	
	/**
	 * the realm
	 */
	private String realm;

	/**
	 * the ha1 
	 */
	private byte[] ha1;
	
	/**
	 * the digest result 
	 */
	private byte[] result;
	
	public AuthenticationVector(){}

	public AuthenticationVector(byte [] sipAuthorization) {
		this.sipAuthorization = sipAuthorization;
	}

	public AuthenticationVector(int auth_scheme, byte [] sipAuthenticate, byte [] sipAuthorization) {
		this.auth_scheme = auth_scheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
	}
	
	public AuthenticationVector(int auth_scheme, String realm, byte [] sipAuthenticate, byte [] ha1, byte [] result) {
		this.auth_scheme = auth_scheme;
		this.realm = realm;
		this.sipAuthenticate = sipAuthenticate;
		this.ha1 = ha1;
		this.result = result;
	}
	
	public AuthenticationVector(int auth_scheme, String ip) {
		this.auth_scheme = auth_scheme;
		this.ip = ip;
	}	
	/**
	 * constructor 
	 * @param authenticationScheme 
	 *                the authentication scheme used in the authentication of 
	 *                SIP messages
	 * @param sipAuthenticate
	 *                data portion of the WWW-Authenticate  or Proxy-Authenticate 
	 *                SIP headers that are to be present in a SIP response.
	 * @param sipAuthorization
	 *                the data portion of the Authorization or Proxy-Authorization 
	 *                SIP headers that are to be present in a SIP response.  
	 * @param integrityKey
	 *                the integrity key
	 */
	public AuthenticationVector(int auth_scheme, byte [] sipAuthenticate, byte [] sipAuthorization, byte [] integrityKey) {
		this.auth_scheme = auth_scheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
		this.integrityKey = integrityKey;
	}
	
	/**
	 * constructor 
	 * @param authenticationScheme 
	 *                the authentication scheme used in the authentication of 
	 *                SIP messages
	 * @param ip IP address for Early IMS               
	 * @param sipAuthenticate
	 *                data portion of the WWW-Authenticate  or Proxy-Authenticate 
	 *                SIP headers that are to be present in a SIP response.
	 * @param sipAuthorization
	 *                the data portion of the Authorization or Proxy-Authorization 
	 *                SIP headers that are to be present in a SIP response. 
	 * @param confidentialityKey
	 *                the confidentiality key                
	 * @param integrityKey
	 *                the integrity key
	 */
	public AuthenticationVector(int auth_scheme, byte [] sipAuthenticate, byte [] sipAuthorization, 
			byte [] confidentialityKey, byte [] integrityKey) {
		
		this.auth_scheme = auth_scheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
		this.confidentialityityKey = confidentialityKey;
		this.integrityKey = integrityKey;
	}

	public int getAuth_scheme() {
		return auth_scheme;
	}

	public void setAuth_scheme(int auth_scheme) {
		this.auth_scheme = auth_scheme;
	}

	public byte[] getConfidentialityityKey() {
		return confidentialityityKey;
	}

	public void setConfidentialityityKey(byte[] confidentialityityKey) {
		this.confidentialityityKey = confidentialityityKey;
	}

	public byte[] getIntegrityKey() {
		return integrityKey;
	}

	public void setIntegrityKey(byte[] integrityKey) {
		this.integrityKey = integrityKey;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getSipAuthenticate() {
		return sipAuthenticate;
	}

	public void setSipAuthenticate(byte[] sipAuthenticate) {
		this.sipAuthenticate = sipAuthenticate;
	}

	public byte[] getSipAuthorization() {
		return sipAuthorization;
	}

	public void setSipAuthorization(byte[] sipAuthorization) {
		this.sipAuthorization = sipAuthorization;
	}
	
	public byte[] getHA1() {
		return ha1;
	}
	
	public void setHA1(byte [] ha1) {
		this.ha1 = ha1;
	}
	
	public byte[] getResult() {
		return result;
	}
	
	public void setResult(byte [] result) {
		this.result = result;
	}
	
	public String getRealm() {
		return realm;
	}
	
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
