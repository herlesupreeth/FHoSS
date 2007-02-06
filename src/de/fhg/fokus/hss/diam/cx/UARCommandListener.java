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
package de.fhg.fokus.hss.diam.cx;

import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CxUserRegistrationStatusResponse;
import de.fhg.fokus.cx.HSScxOperations;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.UserAuthorizationTypeAVP;


/**
 * Implementation of a User Authorization Request/Answer - Command (UAR/UAA).
 * For more imformations about UAR please refer to 3GPP TS 29.229. It contains a method 
 * recvMessage which processes the request after making sure that it is a UAR command 
 * and ultimately sends the  response to the communicating peer.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class UARCommandListener extends CxCommandListener
{
    /** counter */
    public static long counter = 0;
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(UARCommandListener.class);
    /** command id for UAR */    
    private static final int COMMAND_ID = Constants.Command.UAR;
    /** an Object representing the Cx operations of HSS*/
    private HSScxOperations operations;

    /**
     * constructor
     * @param _operations HssCxOpeation
     * @param _diameterPeer diameterPeer
     */     
    public UARCommandListener(
        HSScxOperations _operations, DiameterPeer _diameterPeer)
    {
        super(_diameterPeer);
        this.operations = _operations;
    }

    /** 
     * This method recieves and processes the diameter message UAR. And it
     * sends the appropriate response to the communicating peer.
     * 
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter message
     * @see de.fhg.fokus.diameter.DiameterPeer.EventListener#recvMessage(java.lang.String, de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage)
     */
    public void recvMessage(String FQDN, DiameterMessage requestMessage)
    {
        if (requestMessage.commandCode == COMMAND_ID)
        {
            counter++;
            LOGGER.debug("entering");
            LOGGER.debug(FQDN);

            try
            {
                PublicIdentity publicIdentity =
                    loadPublicIdentity(requestMessage);
                URI privateUserIdentity =
                    loadPrivateUserIdentity(requestMessage);

                // Check for Type of Authorization, if null default is set to REGISTRATION.
                AVP typeOfAuthAVP = requestMessage.findAVP(Constants.AVPCode.USER_AUTHORIZATION_TYPE, true, 
                		Constants.Vendor.V3GPP);
                int typeOfAuthorization = UserAuthorizationTypeAVP._REGISTRATION;
                if (typeOfAuthAVP != null){
                    typeOfAuthorization = typeOfAuthAVP.int_data;
                }

                String visitedNetworkIdentifier = new String(
                		requestMessage.findAVP(Constants.AVPCode.VISITED_NETWORK_IDENTIFIER, true,Constants.Vendor.V3GPP).data);

                CxUserRegistrationStatusResponse response = null;
                AVP resultCode = null;
                response = operations.cxQuery(publicIdentity, visitedNetworkIdentifier, typeOfAuthorization, privateUserIdentity);

                if (response == null){
                    throw new UnableToComply();
                }

                DiameterMessage responseMessage = diameterPeer.newResponse(requestMessage);

        		/* vendor-specific app id */
                AVP vendorSpecificApplicationID = new AVP(Constants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, Constants.Vendor.DIAM);
                AVP vendorID = new AVP(Constants.AVPCode.VENDOR_ID, true, Constants.Vendor.DIAM);
                vendorID.setData(Constants.Vendor.V3GPP);
                vendorSpecificApplicationID.addChildAVP(vendorID);
                AVP applicationID = new AVP(Constants.AVPCode.AUTH_APPLICATION_ID, true,  Constants.Vendor.DIAM);
                applicationID.setData(Constants.Application.CX);
                vendorSpecificApplicationID.addChildAVP(applicationID);
                responseMessage.addAVP(vendorSpecificApplicationID);
        		
        		/* auth-session-state, no state maintained */
                AVP authenticationSessionState = new AVP(Constants.AVPCode.AUTH_SESSION_STATE, true, Constants.Vendor.DIAM);
                authenticationSessionState.setData(1);
                responseMessage.addAVP(authenticationSessionState);
                
                // Add assigned Server Name
                if ( (response.getAssignedSCSCFName() != null) && (response.getAssignedSCSCFName().length() > 0)){
                	AVP assginedSCSCFName = new AVP(Constants.AVPCode.CX_SERVER_NAME, true, Constants.Vendor.V3GPP);
                    assginedSCSCFName.setData(response.getAssignedSCSCFName());
                    responseMessage.addAVP(assginedSCSCFName);
                }

                // Add result code
                resultCode = getResultCodeAVP(response.getResultCode(), response.resultCodeIsBase());
                responseMessage.addAVP(resultCode);
                
                diameterPeer.sendMessage(FQDN, responseMessage);
                LOGGER.debug("exiting");
            }
            catch (DiameterException e){
                LOGGER.warn(this, e);
                sendDiameterException(FQDN, requestMessage, e);
            }
            catch (Exception e){
                LOGGER.error(this, e);
                sendUnableToComply(FQDN, requestMessage);
            }
        }
    }
}
