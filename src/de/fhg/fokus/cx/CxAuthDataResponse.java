/*
 * $Id$
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

import java.util.ArrayList;

/**
 * This class represents the response message conataining the cx specific 
 * authentication informations
 * @author Peter Weik (pwe at fokus dot fraunhofer dot de)
 *
 */
public class CxAuthDataResponse {

	/**
	 * The result code
	 */
	private int resultCode;
	
	/**
	 * indicates if the result code is base protocol result code or not
	 */
	private boolean baseResultCode;
	
	/**
	 * the authentication vectors calculated at the HSS 
	 */
	private ArrayList authenticationVectors;

	/**
	 * minimal constructor
	 * @param experimentalResultCode	The Experimental Result Code for the CxAuthDataResponse
	 */
	public CxAuthDataResponse(int experimentalResultCode) {
		this.resultCode = experimentalResultCode;
		this.baseResultCode = false;
		authenticationVectors= new ArrayList();
	}
	
	/**
	  * constructor
	  * @param resultCode the result code
	  * @param baseResultCode 	"True" indicates a Base Protocol Result Code
	  */
	public CxAuthDataResponse(int resultCode, boolean baseResultCode){
		this.resultCode = resultCode;
		this.baseResultCode = baseResultCode;
		authenticationVectors= new ArrayList();
	}

	/**
	 * Getter method for result code
	 * @return the resultCode.
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * Getter method for the indicator of base protocol result code
	 * @return	Whether the Result Code of the CxAuthDataResponse is of the
	 * 			Diameter Base Protocol type. 
	 */
	public boolean resultCodeIsBase () {
		return baseResultCode;
	}

	/**
	 * Getter method for authentication vectors
	 * @return Returns the authenticationVectors.
	 */
	public ArrayList getAuthenticationVectors() {
		return authenticationVectors;
	}
	/**
	 * Setter method for authentication vectors
	 * @param authenticationVectors The authenticationVectors to set.
	 */
	public void setAuthenticationVectors(ArrayList authenticationVectors) {
		this.authenticationVectors = authenticationVectors;
	}
	
	/**
	 * It calculates and returns the number of authentication vectors
	 * @return	The number of returned Authentication Vectors.
	 */
	public int getSIPNumberAuthItems() {
		return this.authenticationVectors.size();
	}
}
