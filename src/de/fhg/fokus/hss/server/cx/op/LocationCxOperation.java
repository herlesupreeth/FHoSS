
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

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CxLocationQueryResponse;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.ims.IdentityNotRegistered;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * Implementation of the Location procedure mapped to the LIR/LIA command.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class LocationCxOperation extends CxOperation{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(LocationCxOperation.class);
    /**
     * constructor
     * @param publicIdentity public user identity
     */
    public LocationCxOperation(PublicIdentity publicIdentity){
        LOGGER.debug("entering");
        this.publicIdentity = publicIdentity;
        LOGGER.debug("exiting");
    }

    /**
     * it performs the location information retrieval opeartion triggered by LIR command
     * @return an object containing the location information
     * @throws DiameterException
     */	   
    public Object execute() throws DiameterException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        CxLocationQueryResponse locationQueryResponse = null;

        try
        {
        	HibernateUtil.beginTransaction();
            loadUserProfile();
            String serverName = null;

            if (getUserProfil().getImpu().getPsi()){
                serverName = getUserProfil().getImpu().getAssignedPsi().getPsiTempl().getApsvr().getAddress();
            }
            else{
                if (getUserProfil().getImpi() != null){
                    serverName = getUserProfil().getImpi().getScscfName();
                }
            }

            if (serverName != null && serverName.equals("") == false){
                locationQueryResponse = new CxLocationQueryResponse(ResultCode._DIAMETER_SUCCESS, true);
                locationQueryResponse.setAssignedSCSCFName(serverName);
            }
            else{
            	LOGGER.info("User not registered, sending LIA: DIAMETER_ERROR_IDENTITY_NOT_REGISTERED !");
            	throw new IdentityNotRegistered();
            }
            
        }
        finally{
        	HibernateUtil.commitTransaction();
        	HibernateUtil.closeSession();	
        }

        LOGGER.debug("exiting");

        return locationQueryResponse;
    }
}
