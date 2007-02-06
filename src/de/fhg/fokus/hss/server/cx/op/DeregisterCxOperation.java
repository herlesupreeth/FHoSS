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
package de.fhg.fokus.hss.server.cx.op;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CSCFOperations;
import de.fhg.fokus.cx.DeregistrationReason;
import de.fhg.fokus.cx.DeregistrationSet;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.model.CxUserProfil;
import de.fhg.fokus.hss.model.Impu;
import de.fhg.fokus.hss.server.cx.CSCFOperationsImpl;

/**
 * This class represents cx specific deregisteration operation RTR.
 * @author Andre Charton  (dev -at- open-ims dot org)
 */
public class DeregisterCxOperation extends CxOperation {

	/** logger */
	private static final Logger LOGGER = Logger.getLogger(DeregisterCxOperation.class);
	/** serving call session control function name */
	private String scscfName;
	/** deregistration reason object */
	private DeregistrationReason reason;
	/** deregistration set object */
	private DeregistrationSet deregistrationSet;

    /**
     * constructor
     * @param _userProfil user profile
     * @param reason deregistartion reason object
     */ 	
	public DeregisterCxOperation(CxUserProfil _userProfil, DeregistrationReason reason){
		
		LOGGER.debug("entering");
		this.userProfil = _userProfil;
		this.scscfName = userProfil.getImpi().getScscfName();
		this.reason = reason;
		
		try {
			this.privateUserIdentity = new URI(_userProfil.getImpi().getImpiString());
		}
		catch (URISyntaxException e) {
			LOGGER.error(this, e);
		}
		deregistrationSet = new DeregistrationSet(privateUserIdentity);
		
		Iterator it = _userProfil.getImpuList().iterator();
		while(it.hasNext()){
			Impu impu = (Impu) it.next();
			deregistrationSet.addPublicId(impu.getSipUrl());
		}
		
		LOGGER.debug("exiting");
	}

    /**
     * it performs the deregistration operation
     * @return null
     * @throws DiameterException
     */	
	public Object execute() throws DiameterException {
		LOGGER.debug("entering");
		CSCFOperations operations = new CSCFOperationsImpl();
		PublicIdentity publicIdentity = new PublicIdentity();
		publicIdentity.setIdentity(userProfil.getImpu().getSipUrl());
		operations.cxDeregister(deregistrationSet, reason, scscfName);
		LOGGER.debug("exiting");
		return null;
	}

}
