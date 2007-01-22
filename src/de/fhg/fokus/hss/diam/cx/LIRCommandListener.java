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

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.CxLocationQueryResponse;
import de.fhg.fokus.cx.HSScxOperations;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.Constants;


/**
 * This class listens to Diameter LIR/LIA. For more imformations about LIR plese 
 * refer to 3GPP TS 29.229. It contains a method recvMessage which processes
 * the request after making sure that it is a LIR command and ultimately sends the 
 * response to the communicating peer.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class LIRCommandListener extends CxCommandListener{
    /** the counter for LIR commands */
    public static long counter = 0;
    /** the LOGGER */
    private static final Logger LOGGER =
        Logger.getLogger(LIRCommandListener.class);
    /** the command id for LIR*/    
    private static final int COMMAND_ID = Constants.Command.LIR;
    /** An object representing Cx Operations of HSS*/
    private HSScxOperations operations;

    /**
     * constructor
     * @param _operations HssCxOpeation
     * @param _diameterPeer diameterPeer
     */
    public LIRCommandListener(
        HSScxOperations _operations, DiameterPeer _diameterPeer){
        super(_diameterPeer);
        this.operations = _operations;
    }

    /**
     * This method recieves and processes the diameter message LIR
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter message
     * @see de.fhg.fokus.diameter.DiameterPeer.EventListener#recvMessage(java.lang.String, de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage)
     */
    public void recvMessage(String FQDN, DiameterMessage requestMessage){
    	
        if (requestMessage.commandCode == COMMAND_ID){
            counter++;
            try{
                PublicIdentity publicIdentity =
                    loadPublicIdentity(requestMessage);

                CxLocationQueryResponse response = null;
                AVP resultCode = null;

                response = operations.cxLocationQuery(publicIdentity);
                if (response == null){
                    throw new UnableToComply();
                }

                DiameterMessage responseMessage =
                    diameterPeer.newResponse(requestMessage);

                 // Add assigned Server Name
                AVP assginedSCSCFName = new AVP(Constants.AVPCode.CX_SERVER_NAME, true, Constants.Vendor.V3GPP);
                assginedSCSCFName.setData(response.getAssignedSCSCFName());
                responseMessage.addAVP(assginedSCSCFName);
                    
                AVP vendorSpecificApplicationID = new AVP(Constants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, Constants.Vendor.V3GPP);
                AVP vendorID = new AVP(Constants.AVPCode.VENDOR_ID, true, Constants.Vendor.DIAM);
                vendorSpecificApplicationID.addChildAVP(vendorID);
                
                AVP applicationID = new AVP(Constants.AVPCode.AUTH_APPLICATION_ID, true,  Constants.Vendor.V3GPP);
                vendorSpecificApplicationID.addChildAVP(applicationID);
                responseMessage.addAVP(vendorSpecificApplicationID);
                    
                AVP authenticationSessionState = new AVP(Constants.AVPCode.AUTH_SESSION_STATE, true, Constants.Vendor.V3GPP);
                authenticationSessionState.setData(1);
                responseMessage.addAVP(authenticationSessionState);
                
                resultCode = saveResultCode(response.getResultCode(), response.resultCodeIsBase());
                responseMessage.addAVP(resultCode);

                diameterPeer.sendMessage(FQDN, responseMessage);
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
