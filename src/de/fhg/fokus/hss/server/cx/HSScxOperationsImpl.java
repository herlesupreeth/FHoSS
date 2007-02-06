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

import de.fhg.fokus.cx.AuthenticationVector;
import de.fhg.fokus.cx.CxAuthDataResponse;
import de.fhg.fokus.cx.CxLocationQueryResponse;
import de.fhg.fokus.cx.CxSCSCFNotificationResponse;
import de.fhg.fokus.cx.CxUserRegistrationStatusResponse;
import de.fhg.fokus.cx.HSScxOperations;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.server.cx.op.AuthCxOperation;
import de.fhg.fokus.hss.server.cx.op.LocationCxOperation;
import de.fhg.fokus.hss.server.cx.op.PullCxOperation;
import de.fhg.fokus.hss.server.cx.op.QueryCxOperation;


/**
 * Implementation of the hss cx interface, supports the following commands:
 * UAR/UUA
 * LIR/LIA
 * MAR/MAA
 * SAR/SAA
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HSScxOperationsImpl implements HSScxOperations{
    private static final Logger LOGGER = Logger.getLogger(HSScxOperationsImpl.class);

    /**
     * Implementation of the UAR/UAA.
     * @param publicIdentity the public identity
     * @param visitedNetworkIdentifier identifier of visited network
     * @param typeOfAuthorization authorization type
     * @param privateUserIdentity private identity
     * @return response object contains all UAA values
     * @throws DiameterException
     */
    public CxUserRegistrationStatusResponse cxQuery(PublicIdentity publicIdentity, String visitedNetworkIdentifier,
        int typeOfAuthorization, URI privateUserIdentity) throws DiameterException {

    	LOGGER.debug("entering");
        QueryCxOperation query = new QueryCxOperation(publicIdentity, visitedNetworkIdentifier, typeOfAuthorization,
                privateUserIdentity);
        CxUserRegistrationStatusResponse response = (CxUserRegistrationStatusResponse) query.execute();
        LOGGER.debug("exiting");
        return response;
    }

    /**
     * Implemenation of the SAR/SAA Command.
     * @param publicIdentity the public identity
     * @param serverName name of server
     * @param privateUserIdentity private identity
     * @param serverAssignmentType type of server assignment
     * @param userDataRequestType  type of user data request
     * @param userDataAlreadyAvailable an integer value indicating whether user data
     *        is already available or not
     * @return response object contains all SAA values
     * @throws DiameterException
     */
    public CxSCSCFNotificationResponse cxPull(PublicIdentity publicIdentity, String serverName, 
    		URI privateUserIdentity, int serverAssignmentType, int userDataRequestType, int userDataAlreadyAvailable)
        throws DiameterException{
    	
        LOGGER.debug("entering");
        PullCxOperation pullCxOperation = new PullCxOperation(publicIdentity, serverName, privateUserIdentity,
        		serverAssignmentType, userDataRequestType,userDataAlreadyAvailable);
        CxSCSCFNotificationResponse response = (CxSCSCFNotificationResponse) pullCxOperation.execute();

        LOGGER.debug("exiting");
        return response;
    }

    /**
     * Implementation of the LIR/LIA
     * @param publicIdentity the public identity
     * @return response object contains all LIA values
     * @throws DiameterException
     */
    public CxLocationQueryResponse cxLocationQuery(PublicIdentity publicIdentity) throws DiameterException{

    	LOGGER.debug("entering");
        LocationCxOperation locationCxOperation = new LocationCxOperation(publicIdentity);
        CxLocationQueryResponse response = (CxLocationQueryResponse) locationCxOperation.execute();
        LOGGER.debug("exiting");
        return response;
    }

    /**
     * Implementation of the MAR/MAA Command
     * @param publicIdentity the public identity
     * @param privateUserIdentity private identity
     * @param numberOfAuthVectors number of authentication vectors
     * @param authenticationVector the authentication vector
     * @param scscfName name of scscf
     * @return response object contains all MAA values
     * @throws DiameterException
     */
    public CxAuthDataResponse cxAuthData(PublicIdentity publicIdentity, URI privateUserIdentity,
        Long numberOfAuthVectors, AuthenticationVector authenticationVector, String scscfName) throws DiameterException{
        
    	LOGGER.debug("entering");
        AuthCxOperation authCxOperation =
            new AuthCxOperation(publicIdentity, privateUserIdentity, numberOfAuthVectors, authenticationVector, scscfName);
        CxAuthDataResponse response = (CxAuthDataResponse) authCxOperation.execute();
        LOGGER.debug("exiting");
        return response;
    }
}
