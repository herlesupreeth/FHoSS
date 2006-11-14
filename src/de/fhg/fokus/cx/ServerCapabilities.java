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
 * This class describes the server capabilities and provides appropriate
 * getter and setter methods.
 * 
 * @author Peter Weik (pwe at fokus dot fraunhofer dot de)
 *
 */
public class ServerCapabilities {
	
	/** 
	 * the list madatory capabilities
	 */
	private ArrayList mandatoryCapabilities;
	/**
	 * the list of optional capabilities
	 */
	private ArrayList optionalCapabilities;
	
	/**
	 * the list of names of server
	 */
	private ArrayList serverName;

	/**
	 *  default constructor
	 */
	public ServerCapabilities()
	{
		mandatoryCapabilities= new ArrayList();
		optionalCapabilities= new ArrayList();
		serverName= new ArrayList();
	}
    
	/**
	 * Getter for mandatory capabilities
	 * @return Returns the mandatoryCapabilites.
	 */
	public ArrayList getMandatoryCapabilities() {
		return mandatoryCapabilities;
	}
	
	/**
	 * Adds mandatory capability
	 * @param mandatoryCapability The mandatoryCapability to add.
	 */
	public void addMandatoryCapability(Object mandatoryCapability) {
		this.mandatoryCapabilities.add(mandatoryCapability);
	}
	
	/**
	 * Getter for optional capabilities
	 * @return Returns the optionalCapabilities
	 */
	public ArrayList getOptionalCapabilities() {
		return optionalCapabilities;
	}
	
	/**
	 * adds optional capability
	 * @param optionalCapability The optionalCapability to add.
	 */
	public void addOptionalCapability(Object optionalCapability) {
		this.optionalCapabilities.add(optionalCapability);
	}
	
	/**
	 * Getter for names of server
	 * @return Returns the serverNames.
	 */
	public ArrayList getServerNames() {
		return serverName;
	}
	
	/**
	 * adds a server name to the list of server names
	 * @param serverName The serverName to add.
	 */
	public void addServerName(String serverName) {
		this.serverName.add(serverName);
	}
	
	/**
	 * checks whether the capabilities of the server are empty or not. If empty then
	 * it returns true else false.
	 * @return	If there are any values set for the Server Capabilities.
	 */
	public boolean isEmpty(){
		if (this.mandatoryCapabilities == null && this.optionalCapabilities == null && this.serverName == null) {
			return true;
		} else {
			return false;
		}
	}
}
