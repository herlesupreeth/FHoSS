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
package de.fhg.fokus.hss.diam.zh;

import java.io.StringWriter;
import java.net.URI;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.AVPCodes;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.model.AuthSchemeBO;
import de.fhg.fokus.zh.AuthenticationVector;
import de.fhg.fokus.zh.ZhAuthDataResponse;
import de.fhg.fokus.zh.ZhOperations;
import de.fhg.fokus.zh.data.Guss;
import de.fhg.fokus.zh.exceptions.DiameterException;


/**
 * Implementation of the Zh-Interface specified MAR Command as an extension
 * of the Cx-Interface MAR Command.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class MARzhCommandListener extends ZhCommandListener
{
    /** counter to count calls */
    public static long counter = 0;
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(MARzhCommandListener.class);
    /** command id for MARzh */    
    private static final int COMMAND_ID = Constants.COMMAND.MARzh;
    /** an object representing zh operations */
    private ZhOperations zhOperations;

    /**
     * constructor
     * @param zhImpl Zh Opeations
     * @param _diameterPeer diameterPeer
     */ 
    public MARzhCommandListener(
        ZhOperations zhImpl, DiameterPeer _diameterPeer)
    {
        super(_diameterPeer);
        this.zhOperations = zhImpl;
    }

    /** 
     * This method recieves and processes the diameter message MARzh
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter message   
     */ 
    public void recvMessage(String FQDN, DiameterMessage requestMessage)
    {
        if (requestMessage.commandCode == COMMAND_ID)
        {
            counter++;
            if(LOGGER.isDebugEnabled()){
            	LOGGER.debug("entering");
            	LOGGER.debug(FQDN);
            	LOGGER.debug(requestMessage);
            }

            try
            {
                AVP resultCode = null;

                URI privateUserIdentity =
                    loadPrivateUserIdentity(requestMessage);
                Long numberOfAuthVectors =
                    loadNumberOfAuthVectors(requestMessage);
                AuthenticationVector authenticationVector = null;

                ZhAuthDataResponse authDataResponse = null;

                authDataResponse =
                    zhOperations.zhAuthData(
                        privateUserIdentity, numberOfAuthVectors,
                        authenticationVector, null);

                DiameterMessage responseMessage =
                    diameterPeer.newResponse(requestMessage);

                final int SESSION_ID_AVP = 263;
                AVP sessionId =
                    requestMessage.findAVP(
                        SESSION_ID_AVP, true, Constants.Vendor.DIAM);

                if (sessionId != null)
                {
                    responseMessage.addAVP(sessionId);
                }
                else
                {
                    LOGGER.debug(
                        this, new NullPointerException("Session ID missed."));
                }

                final int VENDOR_APP_ID_AVP = 260;
                AVP vendorId =
                    requestMessage.findAVP(
                        VENDOR_APP_ID_AVP, true, Constants.Vendor.DIAM);

                if (vendorId != null)
                {
                    responseMessage.addAVP(vendorId);
                }
                else
                {
                    LOGGER.warn(
                        this, new NullPointerException("Vendor ID missed."));
                }

                final int AUTH_STATE_AVP = 277;
                AVP authStateId =
                    requestMessage.findAVP(
                        AUTH_STATE_AVP, true, Constants.Vendor.DIAM);

                if (authStateId != null)
                {
                    responseMessage.addAVP(authStateId);
                }
                else
                {
                    LOGGER.warn(
                        this, new NullPointerException("Auth State ID missed."));
                }

                saveAuthData(authDataResponse, responseMessage);
                saveGussData(authDataResponse, responseMessage);

                // Add result code
                resultCode =
                    saveResultCode(
                        authDataResponse.getResultCode(),
                        authDataResponse.resultCodeIsBase());

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

            LOGGER.debug("exiting");
        }
    }

    /**
     * Store GUSS data in response message
     * @param authDataResponse
     * @param responseMessage
     * @throws MarshalException On marshal the Guss to the AVP failure
     * @throws ValidationException On marshal the Guss to the AVP failure
     */
    private void saveGussData(
        ZhAuthDataResponse authDataResponse, DiameterMessage responseMessage)
        throws MarshalException, ValidationException
    {
        Guss guss = authDataResponse.getGuss();

        if (guss != null)
        {
            AVP gussAVP = AVPCodes.getAVP(AVPCodes._ZH_GUSS);
            StringWriter sw = new StringWriter();
            guss.marshal(sw);
            gussAVP.setData(sw.getBuffer().toString());
            responseMessage.addAVP(gussAVP);
        }
    }

    /**
     * Help method which saves authentication data 
     * from authentication data response into the diameter message
     * @param authDataResponse 
     * @param responseMessage
     */
    private void saveAuthData(
        ZhAuthDataResponse authDataResponse, DiameterMessage responseMessage)
    {
        // Iterete and add vectors to response message
        Iterator it = authDataResponse.getAuthenticationVectors().iterator();
        int ix = 0;

        while (it.hasNext())
        {
            // store one vector in message as Auth Data Item
            AuthenticationVector vector = (AuthenticationVector) it.next();

            AVP authDataItem = AVPCodes.getAVP(AVPCodes._SIP_AUTH_DATA_ITEM);

            //Constants.AVPCodes
            AVP itemNumber = AVPCodes.getAVP(AVPCodes._SIP_ITEM_NUMBER);
            itemNumber.setData(ix);
            authDataItem.addChildAVP(itemNumber);

            AVP authScheme =
                AVPCodes.getAVP(AVPCodes._SIP_AUTHENTICATION_SCHEME);
            authScheme.setData(vector.authenticationScheme);
            authDataItem.addChildAVP(authScheme);

            AVP authenticate = AVPCodes.getAVP(AVPCodes._SIP_AUTHENTICATE);
            authenticate.setData(vector.sipAuthenticate);
            authDataItem.addChildAVP(authenticate);

            AVP authorization = AVPCodes.getAVP(AVPCodes._SIP_AUTHORIZATION);
            authorization.setData(vector.sipAuthorization);
            authDataItem.addChildAVP(authorization);

            AVP confidentialityKey =
                AVPCodes.getAVP(AVPCodes._CONFIDENTIALITY_KEY);
            confidentialityKey.setData(vector.confidentialityityKey);
            authDataItem.addChildAVP(confidentialityKey);

            AVP integrityKey = AVPCodes.getAVP(AVPCodes._INTEGRITY_KEY);
            integrityKey.setData(vector.integrityKey);
            authDataItem.addChildAVP(integrityKey);

            responseMessage.addAVP(authDataItem);
            ix++;
        }

        // Save the number of items.
        AVP numberOfItems = AVPCodes.getAVP(AVPCodes._SIP_NUMBER_AUTH_ITEMS);
        numberOfItems.setData(ix);
        responseMessage.addAVP(numberOfItems);
    }

    /**
     * It loads the number of authentication vectors in the message
     * provided as argument
     * @param requestMessage
     * @return the number of authentication vectors
     * @throws DiameterException
     */
    private Long loadNumberOfAuthVectors(DiameterMessage requestMessage)
        throws DiameterException
    {
        //        Long numberOfAuthVectors = null;
        //
        //        try
        //        {
        //            numberOfAuthVectors =
        //                Long.valueOf(
        //                    avpLookUp(
        //                        requestMessage, AVPCodes._SIP_NUMBER_AUTH_ITEMS, true,
        //                        Constants.Vendor.V3GPP).int_data);
        //        }
        //        catch (NumberFormatException e)
        //        {
        //            LOGGER.warn(this, e);
        //            throw new DiameterBaseException(InvalidAvpValue.ERRORCODE);
        //        }
        //
        //        return numberOfAuthVectors;
        return new Long(1);
    }

    /**
     * It loads authentication vector with the help of argument provided.
     * @param requestMessage
     * @return the authentication vector
     * @throws DiameterException
     */
    private AuthenticationVector loadAuthVector(DiameterMessage requestMessage)
        throws DiameterException
    {
        //        AuthenticationVector authenticationVector = null;
        //        AVP authVectorAVP =
        //            avpLookUp(
        //                requestMessage, AVPCodes._SIP_AUTH_DATA_ITEM, true, Vendor.V3GPP);
        //
        //        AVP sipAuthorization =
        //            authVectorAVP.findChildAVP(
        //                AVPCodes._SIP_AUTHORIZATION, true, Vendor.V3GPP);
        //
        //        if (sipAuthorization != null)
        //        {
        //            authenticationVector =
        //                new AuthenticationVector(sipAuthorization.data);
        //        }
        //
        //        return authenticationVector;
        return new AuthenticationVector(AuthSchemeBO.AUTH_ALGO_MD5.getBytes());
    }
}
