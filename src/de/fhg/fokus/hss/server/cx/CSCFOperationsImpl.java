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
package de.fhg.fokus.hss.server.cx;

import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CSCFOperations;
import de.fhg.fokus.cx.ChargingInfoSet;
import de.fhg.fokus.cx.DeregistrationReason;
import de.fhg.fokus.cx.DeregistrationSet;
import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.diam.cx.PPRCommandAction;
import de.fhg.fokus.hss.diam.cx.RTRCommandAction;


/**
 * This class represents the implementation of CSCF specific commands RTR and PPR.
 * For more informations on RTR and PPR please refer to 3GPP TS 29.229.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class CSCFOperationsImpl implements CSCFOperations{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(CSCFOperationsImpl.class);

    /**
     * It updates subscriber data. It is the implemenatation of PPR request which
     * is sent from HSS to SCSCF to indicate that a user profile is updated.
     * 
     * @param _privateUserIdentity private identity
     * @param _userData the user data
     * @param _chargingInfoSet chanrging information set 
     * @param _scscfName the name of serving call session control function
     * @throws DiameterException
     */
    public void cxUpdateSubscriberData(URI _privateUserIdentity, IMSSubscription _userData,
        ChargingInfoSet _chargingInfoSet, String _scscfName) throws DiameterException{
        
    	LOGGER.debug("entering");
        PPRCommandAction commandAction = new PPRCommandAction(_privateUserIdentity, _userData, _chargingInfoSet, _scscfName);
        commandAction.execute();
        LOGGER.debug("exiting");
    }


    /**
     * It assists the deregistration process. It is the implementation of
     * RTR command which is sent from HSS to SCSCF.
     * 
     * @param deregistrationSet deregistration set
     * @param deregistrationReason reason for deregistration
     * @param scscfName the name of serving call session control function
     * @throws DiameterException
     */
	 public void cxDeregister(DeregistrationSet deregistrationSet, DeregistrationReason deregistrationReason, String scscfName) 
	 	throws DiameterException {
		 
		 LOGGER.debug("entering");
		 RTRCommandAction commandAction = new RTRCommandAction(deregistrationSet, deregistrationReason, scscfName);
		 commandAction.execute();
		 LOGGER.debug("exiting");
	}
}
