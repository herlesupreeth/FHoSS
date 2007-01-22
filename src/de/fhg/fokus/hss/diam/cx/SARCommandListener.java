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

import java.io.StringWriter;
import java.net.URI;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.fhg.fokus.cx.ChargingInfoSet;
import de.fhg.fokus.cx.CxSCSCFNotificationResponse;
import de.fhg.fokus.cx.HSScxOperations;
import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.MissingAVP;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.Constants;


/**
 * Implementation of a Server Assignment Request/Answer - Command (SAR/SAA).
 * For more imformations about SAR please refer to 3GPP TS 29.229. It contains a method 
 * recvMessage which processes the request after making sure that it is a SAR command 
 * and ultimately sends the  response to the communicating peer.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class SARCommandListener extends CxCommandListener
{
    /** counter */
    public static long counter = 0;
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(SARCommandListener.class);
    /** the command id for SAR */     
    private static final int COMMAND_ID = Constants.Command.SAR;
    /** an object representing the Cx operations for HSS*/
    private HSScxOperations operations;

    /**
     * constructor
     * @param _operations HssCxOpeation
     * @param _diameterPeer diameterPeer
     */ 
    public SARCommandListener(
        HSScxOperations _operations, DiameterPeer _diameterPeer)
    {
        super(_diameterPeer);
        this.operations = _operations;
    }

    /**
     * This method recieves and processes the diameter message SAR. And it
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
                LOGGER.info("SAR of User: "+ publicIdentity.getIdentity() + " is being processed");                    
                URI privateUserIdentity;

                try
                {
                    privateUserIdentity =
                        loadPrivateUserIdentity(requestMessage);
                }
                catch (MissingAVP e)
                {
                    privateUserIdentity = null;
                }

                String scscfName = loadServerName(requestMessage);

                int serverAssignmentType = avpLookUp(requestMessage, Constants.AVPCode.SERVER_ASSIGNMENT_TYPE, true,
                        Constants.Vendor.V3GPP).int_data;

                // not supported set to 0
                int userDataRequestType = 0;
                int userDataAlreadyAvailable = avpLookUp(requestMessage, Constants.AVPCode.USER_DATA_ALREADY_AVAILABLE,
                        true, Constants.Vendor.V3GPP).int_data;
                CxSCSCFNotificationResponse response = null;
                AVP resultCode = null;

                response = operations.cxPull(
                        publicIdentity, scscfName, privateUserIdentity,
                        serverAssignmentType, userDataRequestType,
                        userDataAlreadyAvailable);

                if (response == null){
                    throw new UnableToComply();
                }

                DiameterMessage responseMessage = diameterPeer.newResponse(requestMessage);

                if (resultCode == null)
                {
                    if (response.getUserProfile() != null)
                    {
                        saveIMSSubscription(
                            response.getUserProfile(), responseMessage);
                        saveChargingInfoSet(
                            response.getChargingInfoSet(), responseMessage);
                    }

                    // Add result code
                    resultCode =
                        saveResultCode(
                            response.getResultCode(),
                            response.resultCodeIsBase());
                }

                responseMessage.addAVP(resultCode);

                diameterPeer.sendMessage(FQDN, responseMessage);
                LOGGER.debug("exiting");
            }
            catch (DiameterException e)
            {
                LOGGER.warn(this, e);
                sendDiameterException(FQDN, requestMessage, e);
            }
            catch (Exception e)
            {
                LOGGER.error(this, e);
                sendUnableToComply(FQDN, requestMessage);
            }
        }
    }

    /**
     * Help method to save ims subscription in diameter message
     * @param subscription
     * @param responseMessage
     * @throws MarshalException
     * @throws ValidationException
     */
    public static void saveIMSSubscription(
        IMSSubscription subscription, DiameterMessage responseMessage)
        throws MarshalException, ValidationException
    {
        AVP userProfileAVP = new AVP(Constants.AVPCode.CX_USER_DATA, true, Constants.Vendor.V3GPP);
        StringWriter sw = new StringWriter();
        subscription.marshal(sw);
        userProfileAVP.setData(sw.getBuffer().toString());
        responseMessage.addAVP(userProfileAVP);
    }

    /**
     * Help method to save charging info set in response message
     * @param chargingInfoSet
     * @param responseMessage
     */
    public static void saveChargingInfoSet(
        ChargingInfoSet chargingInfoSet, DiameterMessage responseMessage)
    {
        AVP chargingInfoAVP = new AVP(Constants.AVPCode.CHARGING_INFO, true, Constants.Vendor.V3GPP);
        AVP fnNameAVP = null;

        if (chargingInfoSet.getPri_chrg_coll_fn_name() != null){
            fnNameAVP = new AVP(Constants.AVPCode.PRI_CHRG_COLL_FN_NAME, true, Constants.Vendor.V3GPP);
            fnNameAVP.setData(chargingInfoSet.getPri_chrg_coll_fn_name().getPath());
            chargingInfoAVP.addChildAVP(fnNameAVP);
        }

        if (chargingInfoSet.getSec_chrg_coll_fn_name() != null){
            fnNameAVP = new AVP(Constants.AVPCode.SEC_CHRG_COLL_FN_NAME, true, Constants.Vendor.V3GPP);
            fnNameAVP.setData(chargingInfoSet.getSec_chrg_coll_fn_name().getPath());
            chargingInfoAVP.addChildAVP(fnNameAVP);
        }

        if (chargingInfoSet.getPri_event_chrg_fn_name() != null){
            fnNameAVP = new AVP(Constants.AVPCode.PRI_EVENT_CHARGING_FN_NAME, true, Constants.Vendor.V3GPP);
            fnNameAVP.setData(chargingInfoSet.getPri_event_chrg_fn_name().getPath());
            chargingInfoAVP.addChildAVP(fnNameAVP);
        }

        if (chargingInfoSet.getSec_event_chrg_fn_name() != null){
            fnNameAVP = new AVP(Constants.AVPCode.SEC_EVENT_CHARGING_FN_NAME, true, Constants.Vendor.V3GPP);
            fnNameAVP.setData(chargingInfoSet.getSec_event_chrg_fn_name().getPath());
            chargingInfoAVP.addChildAVP(fnNameAVP);
        }

        responseMessage.addAVP(chargingInfoAVP);
    }
}
