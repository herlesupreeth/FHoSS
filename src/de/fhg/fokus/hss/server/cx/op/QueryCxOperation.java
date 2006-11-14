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

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CxUserRegistrationStatusResponse;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.ims.RoamingNotAllowed;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.diam.UserAuthorizationTypeAVP;


/**
 * Operation implementation mapped to UAR command
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class QueryCxOperation extends CxOperation
{
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(QueryCxOperation.class);

    /**
     * The default scscf name
     */
    public static final String DEFAULT_SCSCF_NAME = "lupine.umts-at-fokus.de";
    /** identifier for visited network */
    private String visitedNetworkIdentifier;
    /** integer value representing type of authorization */
    private int typeOfAuthorization;


    /**
     * constructor
     * @param _publicIdentity public user identity
     * @param _visitedNetworkIdentifier identifier for visited network
     * @param _typeOfAuthorization type of authorization
     * @param _privateUserIdentity private user identity
     */ 
    public QueryCxOperation(
        PublicIdentity _publicIdentity, String _visitedNetworkIdentifier,
        int _typeOfAuthorization, URI _privateUserIdentity)
    {
        LOGGER.debug("entering");
        this.publicIdentity = _publicIdentity;
        this.privateUserIdentity = _privateUserIdentity;
        this.visitedNetworkIdentifier = _visitedNetworkIdentifier;
        this.typeOfAuthorization = _typeOfAuthorization;
        LOGGER.debug("exiting");
    }


    /**
     * it performs the required opeartion triggered by UAR command
     * @return an object providing UAA specific information 
     * @throws DiameterException
     */   
    public Object execute() throws DiameterException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        CxUserRegistrationStatusResponse registrationStatusResponse = null;

        try
        {
            loadUserProfile();

            // HSS shall check that the user is allowed to roam in the visited
            // network on REGISTRATION
            if (
                (
                        typeOfAuthorization == UserAuthorizationTypeAVP._REGISTRATION
                    )
                    || (
                        typeOfAuthorization == UserAuthorizationTypeAVP._REGISTRATION_AND_CAPABILITIES
                    ))
            {
                if (
                    userProfil.isRoamingAllowed(visitedNetworkIdentifier) == false)
                {
                    throw new RoamingNotAllowed();
                }

                registrationStatusResponse =
                    new CxUserRegistrationStatusResponse(
                        ResultCode._DIAMETER_SUBSEQUENT_REGISTRATION, false);
            }
            else
            {
                // DE_REGISTRATION
                registrationStatusResponse =
                    new CxUserRegistrationStatusResponse(
                        ResultCode._DIAMETER_SUCCESS, true);
            }

            if (userProfil.getImpi().getScscfName() != null)
            {
                registrationStatusResponse.setAssignedSCSCFName(
                    userProfil.getImpi().getScscfName());
            }
            else
            {
                registrationStatusResponse.setAssignedSCSCFName(
                    DEFAULT_SCSCF_NAME);
            }
        }
        finally
        {
        	if(getUserProfil() != null){
            getUserProfil().closeSession();
        	}
            LOGGER.debug("exiting");
        }

        return registrationStatusResponse;
    }
}
