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

package de.fhg.fokus.cx;

import java.net.Inet4Address;

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
	
	public String authenticationScheme;
	/** 
	 * ip address related to early ims 
	 */
	public Inet4Address ip;
	
	/**
     *  the sip authenticate represents the data portion of the WWW-Authenticate 
     *  or Proxy-Authenticate SIP headers that are to be present in a SIP response.
	 * 
	 */
	public byte [] sipAuthenticate; // 32 bytes
	// RAND(16) || AUTN(16)= (sqn xor ak)(6)||amf(2)||mac(8)
	
   /**
    * represents the data portion of the Authorization or Proxy-Authorization 
    * SIP headers suitable for inclusion in a SIP request
    */ 
	
	public byte [] sipAuthorization; // at least 8 bytes
	
	/**
	 * the confidentiality key
	 */
	public byte [] confidentialityityKey = new byte[16]; // 16 bytes
	
	/**
	 * the integrity key
	 */
	public byte [] integrityKey = new byte[16]; // 16 bytes
	
	/**
	 * minimal constructor
	 * @param sipAuthorization 
	 *                the data portion of the Authorization or Proxy-Authorization 
	 *                SIP headers that are to be present in a SIP response.
	 */
	public AuthenticationVector(byte [] sipAuthorization) {
		this.sipAuthorization = sipAuthorization;
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
	public AuthenticationVector(String authenticationScheme, byte [] sipAuthenticate, byte [] sipAuthorization, byte [] integrityKey) {
		this.authenticationScheme = authenticationScheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
		this.integrityKey = integrityKey;
	}

	public AuthenticationVector(String authenticationScheme, byte [] sipAuthenticate, byte [] sipAuthorization) {
		this.authenticationScheme = authenticationScheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
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
	public AuthenticationVector(String authenticationScheme, Inet4Address ip, byte [] sipAuthenticate, byte [] sipAuthorization, byte [] confidentialityKey, byte [] integrityKey) {
		this.authenticationScheme = authenticationScheme;
		this.sipAuthenticate = sipAuthenticate;
		this.sipAuthorization = sipAuthorization;
		this.confidentialityityKey = confidentialityKey;
		this.integrityKey = integrityKey;
	}

}
