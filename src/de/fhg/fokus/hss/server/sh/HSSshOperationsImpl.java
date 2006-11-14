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
package de.fhg.fokus.hss.server.sh;

import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.server.sh.op.PullShOperation;
import de.fhg.fokus.hss.server.sh.op.SubsShOperation;
import de.fhg.fokus.hss.server.sh.op.UpdateShOperation;
import de.fhg.fokus.sh.CurrentLocation;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.HSSOperations;
import de.fhg.fokus.sh.OperationNotAllowed;
import de.fhg.fokus.sh.PriorUpdateInProgress;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.RequestedDomain;
import de.fhg.fokus.sh.SubscriptionRequestType;
import de.fhg.fokus.sh.TooMuchData;
import de.fhg.fokus.sh.TransparentDataOutOfSync;
import de.fhg.fokus.sh.UserDataCannotBeModified;
import de.fhg.fokus.sh.UserDataCannotBeRead;
import de.fhg.fokus.sh.UserUnknown;
import de.fhg.fokus.sh.data.ShData;


/**
 * This class represents the implementation of sh-interface specific HSS operations 
 * triggerd by sh specific commands. For more information about Sh-specific comands
 * please refer to 3GPP TS 29.229 
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HSSshOperationsImpl implements HSSOperations
{
    /** Logger */
    private static final Logger LOGGER =
        Logger.getLogger(HSSshOperationsImpl.class);

	/**
	 * this methods represents the implementation of UDR command
	 * @param userIdentity public user identity
	 * @param requestedData sh specific requested data
	 * @param requestedDomain requested domain
	 * @param currentLocation current location
	 * @param serviceIndication service indication
	 * @param applicationServerIdentity identity of application server
	 * @param applicationServerName uri of application server
	 * @throws DiameterException
	 * @throws OperationNotAllowed
	 * @throws UserUnknown
	 * @throws UserDataCannotBeRead
	 */    
    public ShData shPull(
        URI userIdentity, RequestedData requestedData,
        RequestedDomain requestedDomain, CurrentLocation currentLocation,
        byte[] serviceIndication, String applicationServerIdentity,
        URI applicationServerName)
        throws DiameterException, OperationNotAllowed, UserUnknown, 
            UserDataCannotBeRead
    {
       
        LOGGER.debug("entering");

        PullShOperation pullShOperation =
            new PullShOperation(
                userIdentity, requestedData, requestedDomain, currentLocation,
                serviceIndication, applicationServerIdentity,
                applicationServerName);
        ShData shData = (ShData) pullShOperation.execute();
        LOGGER.debug("exiting");

        return shData;
    }

	/**
	 * this methods represents the implementation of PUR command
	 * @param userIdentity public user identity
	 * @param shData sh specific data
	 * @param applicationServerIdentity identity of application server
	 * @param requestedData requested data
	 * @throws DiameterException
	 * @throws OperationNotAllowed
	 * @throws UserUnknown
	 * @throws UserDataCannotBeModified
	 * @throws PriorUpdateInProgress
	 * @throws TransparentDataOutOfSync
	 * @throws TooMuchData
	 * 
	 */   
    public void shUpdate(
        URI userIdentity, ShData shData, String applicationServerIdentity,
        RequestedData requestedData)
        throws DiameterException, OperationNotAllowed, UserUnknown, 
            UserDataCannotBeModified, PriorUpdateInProgress, 
            TransparentDataOutOfSync, TooMuchData
    {
        
        LOGGER.debug("entering");

        UpdateShOperation updateShOperation =
            new UpdateShOperation(
                userIdentity, shData, applicationServerIdentity, requestedData);
        updateShOperation.execute();
        LOGGER.debug("exiting");
    }


	/**
	 * this methods represents the implementation of SNR command
	 * @param userIdentity public user identity
	 * @param requestedData sh specific requested data
	 * @param subscriptionRequestType type of subscription request
	 * @param serviceIndication service indication
	 * @param applicationServerIdentity identity of application server
	 * @param applicationServerName uri of application server
	 * @throws DiameterException
	 */ 
    public void shSubsNotif(
        URI userIdentity, RequestedData requestedData,
        SubscriptionRequestType subscriptionRequestType,
        byte[] serviceIndication, String applicationServerIdentity,
        URI applicationServerName) throws DiameterException
    {
        LOGGER.debug("entering");

        SubsShOperation subsShOperation =
            new SubsShOperation(
                userIdentity, requestedData, subscriptionRequestType,
                serviceIndication, applicationServerIdentity,
                applicationServerName);
        subsShOperation.execute();
        LOGGER.debug("exiting");
    }
}
