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


package de.fhg.fokus.cx.exceptions.ims;


/**
 * It is used by HSS to inform the I-CSCF that: <br>
 *   - The user is authorized to register this public identity <br>
 *   - A S-CSCF shall be assigned to that user.
 * 
 * @author Peter Weik (pwe at fokus dot fraunhofer dot de)
 *
 */
public class FirstRegistration extends IMSSuccess {

	/**
	 *  code of diameter result
	 */	
	public final static int ERRORCODE= 2001;
	
	/**
     * Create a new instance of FirstRegistration
     */	
	public FirstRegistration() {
		super(ERRORCODE);
	}
	
    /**
     * Create a new instance with an explanatory message.
     * 
     * @param message
     *            human readable explanation for this result
     */	
	public FirstRegistration(String message) {
		super(message, ERRORCODE);
	}

    /**
     * Create a new instance with a specific cause.
     * 
     * @param cause
     *            the cause for this result
     */	
	public FirstRegistration(Throwable cause){
		super(cause, ERRORCODE);
	}

    /**
     * Create a new instance with an explanatory message and a specific cause.
     * 
     * @param message
     *            human readable explanation for this result
     * @param cause
     *            the cause for this result
     */		
	public FirstRegistration(String message, Throwable cause) {
		super(message, cause, ERRORCODE);
	}
	

}
