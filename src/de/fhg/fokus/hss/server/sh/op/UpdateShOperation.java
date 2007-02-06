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
package de.fhg.fokus.hss.server.sh.op;

import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.model.RepDataBO;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.InvalidAvpValue;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.UserDataCannotBeModified;
import de.fhg.fokus.sh.data.ShData;


/**
 * This class represents the sh-operation which is triggered by PUR command 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class UpdateShOperation extends ShOperation
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(UpdateShOperation.class);
    /** requested data */    
    private RequestedData requestedData;
    /** an object representing sh specific data */
    private ShData shData;
    /** service indication */
    byte[] serviceIndication;


	/**
	 * constructor
	 * @param userIdentity public user identity
	 * @param shData sh specific data
	 * @param applicationServerIdentity identity of application server
	 * @param requestedData requested data 
	 */ 
    public UpdateShOperation(URI userIdentity, ShData shData, String applicationServerIdentity, RequestedData requestedData){
        LOGGER.debug("entering");
        this.requestedData = requestedData;
        this.applicationServerIdentity = applicationServerIdentity;
        this.publicUserIdentity = userIdentity;
        this.shData = shData;
        LOGGER.debug("exiting");
    }

    /**
     * It performs the appropriate operation triggerd by PUR command. It loads the required
     * data from database and updates the database as indicated by the 
     * requested data value.
     * @return null 
     * @throws DiameterException
     */
    public Object execute() throws DiameterException{
        
    	if (LOGGER.isDebugEnabled()){
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        try{
        	HibernateUtil.beginTransaction();
        	loadApsvr();
        	loadUserProfile();
        	switch (requestedData.getValue()){
        		case RequestedData._REPOSITORYDATA:
        			if (asPermList.isUpdRepData()){
        				updateRepData();
        			}
        			else{
        				throw new UserDataCannotBeModified();
        			}
        			break;

        		default:
        			throw new InvalidAvpValue();
        	}
        }
        finally{
        	HibernateUtil.commitTransaction();
        	HibernateUtil.closeSession();
        }
        LOGGER.debug("exiting");

        return null;
    }

    /**
     * Update Repository Data
     * @throws DiameterException
     */
    private void updateRepData() throws DiameterException{
        LOGGER.debug("entering");
        RepDataBO repDataBO = new RepDataBO(shData, apsvr, userProfil);
        repDataBO.updateRepData();
        LOGGER.debug("exiting");
    }
}
