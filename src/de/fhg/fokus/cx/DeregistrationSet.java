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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * It contains a set of data which are required at the time of deregistration.
 * @author Peter Weik (pwe at fokus dot fraunhofer dot de)
 */
public class DeregistrationSet {
    
    /**
     *  the user name
     */
	private URI userName;
    /**
     * a list of public identities of user
     */
    private List publicIds; 
    
    /**
     * Getter for the public identities
     * @return the public ids
     */
    public List getPublicIds() {
		return publicIds;
	}

    /**
     * Setter for public identities
     * @param publicIds  the public identities
     */
	public void setPublicIds(List publicIds) {
		this.publicIds = publicIds;
	}

    /**	
     * Getter for user name
     * @return user name
     */
	public URI getUserName() {
		return userName;
	}
   
	/**
	 * Setter for user name
	 * @param userName user name
	 */
	public void setUserName(URI userName) {
		this.userName = userName;
	}
    /**
     * constructor 
     * @param userName user name
     */
	public DeregistrationSet(URI userName) {
        this.userName = userName;
        this.publicIds = new ArrayList();
    }
    
	/**
	 * it adds public id
	 * @param publicId public id to be added
	 */
    public void addPublicId(String publicId) {
        this.publicIds.add(publicId);
    }

}
