
package de.fhg.fokus.hss.diam.sh;
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
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.AVPDecodeException;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.AVPCodes;
import de.fhg.fokus.hss.diam.CommandListener;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.diam.Constants.Vendor;
import de.fhg.fokus.sh.DiameterBaseException;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.InvalidAvpValue;
import de.fhg.fokus.sh.MissingAVP;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.UnableToComply;


/**
 * It represenst the command listener for Sh Interface. All
 * sh specific request command listeners need to extend this class.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class ShCommandListener extends CommandListener
{
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(ShCommandListener.class);
    /**
     * constructor
     * @param _diameterPeer diameterpeer
     */
    public ShCommandListener(DiameterPeer _diameterPeer)
    {
        super(_diameterPeer);
    }

    /**
     * abstract method which recieves diameter message
     * @param FQDN fully qualified domain name
     * @param msg diameter message
     */
    public abstract void recvMessage(String FQDN, DiameterMessage msg);

    /**
     * it loads the public identity
     * @param msg Diameter Message
     * @return the extracted Public Identity
     * @throws DiameterException DIAMETER_MISSING_AVP
     */
    protected URI loadPublicIdentity(DiameterMessage msg)
        throws DiameterException
    {
        AVP avp =
            avpLookUp(
                msg, AVPCodes._SH_USER_IDENTITY, true, Constants.Vendor.V3GPP);
        // XLB
        try {
			avp.ungroup();
		} catch (AVPDecodeException e) {
            LOGGER.error(this, e);
            throw new UnableToComply();
		}
        
        AVP sipUriAVP = avp.findChildAVP(AVPCodes._SH_PUBLIC_IDENTITY, true, Constants.Vendor.V3GPP);
        
        URI publicIdentity = null;

        try
        {
            String uriString = new String(sipUriAVP.data);
            if (uriString.toLowerCase().startsWith("sip:"))
            {
                uriString = uriString.substring(4);
            }
            publicIdentity = new URI(uriString);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }

        return publicIdentity;
    }

    /**
     * Find a AVP in a request. If null return a DiameterException with DIAMETER_MISSING_AVP-Code.
     * @param msg
     * @param avpCode
     * @param isVendorSpecific
     * @param vendorId
     * @return AVP given by avpCode and Vendor.
     * @throws DiameterException
     */
    protected AVP avpLookUp(
        DiameterMessage msg, int avpCode, boolean isVendorSpecific, int vendorId)
        throws DiameterException
    {
        AVP avp = msg.findAVP(avpCode, isVendorSpecific, vendorId);

        if (avp == null)
        {
        	LOGGER.debug("Missing AVP: "+avpCode);
            throw new MissingAVP();
        }

        return avp;
    }

    /**
     * Try to send a Diameter Unable To Comply Message
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter request message
     * @param diameterException diameter exception
     */
    protected void sendDiameterException(
        String FQDN, DiameterMessage requestMessage,
        DiameterException diameterException)
    {
        try
        {
            if (
                (diameterPeer != null) && (FQDN != null)
                    && (requestMessage != null))
            {
                DiameterMessage msg = diameterPeer.newResponse(requestMessage);
                AVP resultAVP =
                    saveResultCode(
                        (int) diameterException.getCode(),
                        (diameterException instanceof DiameterBaseException));
                msg.addAVP(resultAVP);
                diameterPeer.sendMessage(FQDN, msg);
            }
            else
            {
                LOGGER.error(
                    "Unable To Send Unable_To_Comply_Message, cause missing mandatory param.",
                    new NullPointerException());
            }
        }
        catch (RuntimeException e)
        {
            LOGGER.error(this, e);
        }
    }

    /**
     * adds the DIAMETER_SUCCESS to the response
     * @param responseMessage the diameter response message to which the 
     *                         DIAMETER_SUCCESS AVP is added.
     */
    protected void addDiameterSuccess(DiameterMessage responseMessage)
    {
        AVP resultCode = AVPCodes.getAVP(AVPCodes._RESULT_CODE);
        resultCode.setData(ResultCode._DIAMETER_SUCCESS);
        responseMessage.addAVP(resultCode);
    }

    /**
     * loads data reference
     * @param requestMessage the diameter request message
     * @return The requested data reference.
     * @throws DiameterException
     */
    protected RequestedData loadDataReference(DiameterMessage requestMessage)
        throws DiameterException
    {
        AVP dataReferenceAVP =
            avpLookUp(
                requestMessage, AVPCodes._SH_DATA_REFERENCE, true, Vendor.V3GPP);

        switch (dataReferenceAVP.int_data)
        {
        case RequestedData._CHARGINGINFORMATION:
            return RequestedData.CHARGINGINFORMATION;

        case RequestedData._IMSUSERSTATE:
            return RequestedData.IMSUSERSTATE;

        case RequestedData._INITIALFILTERCRITERIA:
            return RequestedData.INITIALFILTERCRITERIA;

        case RequestedData._LOCATIONINFORMATION:
            return RequestedData.LOCATIONINFORMATION;

        case RequestedData._PUBLICIDENTIFIERS:
            return RequestedData.PUBLICIDENTIFIERS;

        case RequestedData._REPOSITORYDATA:
            return RequestedData.REPOSITORYDATA;

        case RequestedData._SCSCFNAME:
            return RequestedData.SCSCFNAME;

        case RequestedData._USERSTATE:
            return RequestedData.USERSTATE;
        }

        throw new InvalidAvpValue();
    }

    /**
     * Load the origin host from the request.
     * @param requestMessage the diameter request message
     * @return the origin host
     * @throws DiameterException
     */
    protected String loadOriginHost(DiameterMessage requestMessage)
        throws DiameterException
    {
        AVP originHostAVP =
            avpLookUp(
                requestMessage, AVPCodes._ORIGIN_HOST, false, Vendor.DIAM);
        String originHost = new String(originHostAVP.data);

        return originHost;
    }
}
