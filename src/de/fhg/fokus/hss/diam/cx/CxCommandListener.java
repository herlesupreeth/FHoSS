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
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.DiameterBaseException;
import de.fhg.fokus.cx.exceptions.base.InvalidAvpValue;
import de.fhg.fokus.cx.exceptions.base.MissingAVP;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.CommandListener;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.OriginHostResolver;
import de.fhg.fokus.hss.diam.ResultCode;


/**
 * Sub Class of Command Listener, implements Cx specific functions. All
 * cx specific request command listeners need to extend this class.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class CxCommandListener extends CommandListener
{
    private static final Logger LOGGER =
        Logger.getLogger(CommandListener.class);

    public CxCommandListener(DiameterPeer _diameterPeer){
        super(_diameterPeer);
    }

    /**
     * Abstarct method which recieves message
     * @param FQDN fully qualified domain name
     * @param msg diametermessage
     */
    public abstract void recvMessage(String FQDN, DiameterMessage msg);

    /**
     * It loads the public identity
     * @param msg Diameter Message
     * @return the extracted Public Identity
     * @throws DiameterException DIAMETER_MISSING_AVP
     */
    protected PublicIdentity loadPublicIdentity(DiameterMessage msg)
        throws DiameterException{
        AVP sipUriAVP = avpLookUp(msg, Constants.AVPCode.CX_PUBLIC_IDENTITY, true, Constants.Vendor.V3GPP);
        PublicIdentity publicIdentity = new PublicIdentity();
        publicIdentity.setIdentity(new String(sipUriAVP.data));

        return publicIdentity;
    }

    /**
     * It looks up for specific avp in the diameter message
     * @param msg
     * @param avpCode
     * @param isVendorSpecific
     * @param vendorId
     * @return the found avp if it is found otherwise exception
     * @throws DiameterException
     */
    protected AVP avpLookUp(DiameterMessage msg, int avpCode, boolean isVendorSpecific, int vendorId)
        throws DiameterException{
        AVP avp = msg.findAVP(avpCode, isVendorSpecific, vendorId);

        if (avp == null){
        	LOGGER.warn(String.valueOf(avpCode), new MissingAVP());
            throw new MissingAVP();
        }

        return avp;
    }

    /**
     * It loads the private user identity
     * @param msg Diameter Message
     * @return the extracted Private User Identity
     * @throws URISyntaxException
     * @throws DiameterException
     */
    protected URI loadPrivateUserIdentity(DiameterMessage msg)
        throws URISyntaxException, DiameterException{
        try{
            URI privateUserIdentity =
                new URI(new String(avpLookUp(msg, Constants.AVPCode.PRIVATE_USER_IDENTITY, true, Constants.Vendor.DIAM).data));

            return privateUserIdentity;
        }
        catch (URISyntaxException e){
            throw new InvalidAvpValue();
        }
    }

    
    protected String loadAuthenticationScheme(DiameterMessage msg)
    	throws DiameterException{
    	
       String authenticationScheme =
                new String(avpLookUp(msg, Constants.AVPCode.SIP_AUTHENTICATION_SCHEME, true, Constants.Vendor.DIAM).data);

        return authenticationScheme;
    }    
    
    
    /**
     * It loads the server name
     * @param requestMessage the diameter request message 
     * @return  the server name
     * @throws DiameterException
     */
    protected String loadServerName(DiameterMessage requestMessage)
        throws DiameterException{
    	
        String scscfName =
            new String(avpLookUp(requestMessage, Constants.AVPCode.CX_SERVER_NAME, true, Constants.Vendor.V3GPP).data);
        String originHost =
            new String(avpLookUp(requestMessage, Constants.AVPCode.ORIGIN_HOST, true, Constants.Vendor.DIAM).data);
        OriginHostResolver.setOriginHost(scscfName, originHost);

        return scscfName;
    }

    /**
     * Try to send a Diameter Unable To Comply Message
     * 
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter request message
     * @param diameterException the diameter exception
     */
    protected void sendDiameterException(
        String FQDN, DiameterMessage requestMessage,
        DiameterException diameterException){
    	
        try{
            if ((diameterPeer != null) && (FQDN != null) && (requestMessage != null)){
                DiameterMessage msg = diameterPeer.newResponse(requestMessage);
                AVP resultAVP = getResultCodeAVP( diameterException.getCode(),(diameterException instanceof DiameterBaseException));
                msg.addAVP(resultAVP);
                diameterPeer.sendMessage(FQDN, msg);
            }
            else{
                LOGGER.error("Unable To Send Unable_To_Comply_Message, cause missing mandatory param.", new NullPointerException());
            }
        }
        catch (RuntimeException e){
            LOGGER.error(this, e);
        }
    }

    /**
     * Try to send a Diameter Unable To Comply Message
     * 
     * @param FQDN fully qualified domain name
     * @param requestMessage the message
     */
    protected void sendUnableToComply(String FQDN, DiameterMessage requestMessage){
    	
        try{
            if ((diameterPeer != null) && (FQDN != null) && (requestMessage != null)){
            	
                DiameterMessage responseMessage = diameterPeer.newResponse(requestMessage);
                
                AVP resultAVP = new AVP(Constants.AVPCode.RESULT_CODE, true, Constants.Vendor.DIAM);
                resultAVP.setData(ResultCode._DIAMETER_UNABLE_TO_COMPLY);
                responseMessage.addAVP(resultAVP);
                
                AVP vendorSpecificApplicationID = new AVP(Constants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, 
                		Constants.Vendor.DIAM);
                AVP vendorID = new AVP(Constants.AVPCode.VENDOR_ID, true, Constants.Vendor.DIAM);
                vendorID.setData(Constants.Vendor.V3GPP);
                vendorSpecificApplicationID.addChildAVP(vendorID);
                AVP applicationID = new AVP(Constants.AVPCode.AUTH_APPLICATION_ID, true,  Constants.Vendor.DIAM);
                applicationID.setData(Constants.Application.CX);
                vendorSpecificApplicationID.addChildAVP(applicationID);
                responseMessage.addAVP(vendorSpecificApplicationID);                
                
                AVP authSessionState = new AVP(Constants.AVPCode.AUTH_SESSION_STATE, true, Constants.Vendor.DIAM);
                authSessionState.setData(1);
                responseMessage.addAVP(authSessionState);

                diameterPeer.sendMessage(FQDN, responseMessage);
            }
            else{
                LOGGER.error("Unable to send Unable-To-Comply message; missing mandatory param!");
            }
        }
        catch (RuntimeException e){
            LOGGER.error(this, e);
        }
    }

}
