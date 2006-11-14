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

import java.util.Iterator;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.DeregistrationReason;
import de.fhg.fokus.cx.DeregistrationSet;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.AVPCodes;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.HssDiameterStack;
import de.fhg.fokus.hss.diam.Constants.Application;


/**
 * This class implements the RTR Command Action. For more imformations about RTR
 * please refer to 3GPP TS 29.229. 
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class RTRCommandAction extends CxCommandAction
{
    /** counter */
    public static long counter = 0;
    /** counter */
    public static long rCounter = 0;
    /** The Logger */
    private static final Logger LOGGER =
        Logger.getLogger(RTRCommandAction.class);
    /** the command id for RTR */    
    private static final int COMMAND_ID = Constants.COMMAND.RTR;
    /** deregistration set*/
    private DeregistrationSet deregistrationSet;
    /** deregistration reason */
    private DeregistrationReason deregistrationReason;
    /** name of Serving call session control function */
    private String scscfName;


    /**
     * constructor
     * @param deregistrationSet deregistration set
     * @param deregistrationReason deregistration reason
     * @param scscfName name of scscf
     */ 
    public RTRCommandAction(
        DeregistrationSet deregistrationSet,
        DeregistrationReason deregistrationReason, String scscfName)
    {
        LOGGER.debug("entering");
        this.deregistrationSet = deregistrationSet;
        this.deregistrationReason = deregistrationReason;
        this.scscfName = scscfName;
        LOGGER.debug("exiting");
    }

    /** default constructor */
    public RTRCommandAction()
    {
        LOGGER.debug("entering");
        LOGGER.debug("exiting");
    }


    /**
     * This method recieves the diameter message RTR and increments the message counter.
     * 
     * @param FQDN fully qualified domain name
     * @param msg the diameter message
     */ 
    public void recvMessage(String FQDN, DiameterMessage msg)
    {
        if (msg.commandCode == COMMAND_ID)
        {
            LOGGER.debug("entering");
            counter++;
            LOGGER.debug("exiting");
        }
    }

    /**
     *  It processes the recieved diameter message and sends appropriate response
     *  to the peer
     */
    public void execute()
    {
        LOGGER.debug("entering");
        rCounter++;
        
        DiameterMessage message;
        try
        {
            message =
                HssDiameterStack.diameterPeer.newRequest(
                    COMMAND_ID, Application.CX);

            Iterator it = deregistrationSet.getPublicIds().iterator();

            while (it.hasNext())
            {
                AVP publicIdAVP = AVPCodes.getAVP(AVPCodes._CX_PUBLIC_IDENTITY);
                publicIdAVP.setData((String) it.next());
                message.addAVP(publicIdAVP);
            }

            AVP privateIdAVP = AVPCodes.getAVP(AVPCodes._PRIVATE_USER_IDENTITY);
            privateIdAVP.setData(
                (String) deregistrationSet.getUserName().getPath());
            message.addAVP(privateIdAVP);

            AVP deregAVP = AVPCodes.getAVP(AVPCodes._DEREGISTRATION_REASON);
            AVP deregCode = AVPCodes.getAVP(AVPCodes._REASON_CODE);
            deregCode.setData(deregistrationReason.getDeregistrationCode());
            deregAVP.addChildAVP(deregCode);

            AVP deregInfo = AVPCodes.getAVP(AVPCodes._REASON_INFO);
            deregInfo.setData(deregistrationReason.getDeregistrationInfo());
            deregAVP.addChildAVP(deregInfo);
            message.addAVP(deregAVP);

            AVP destHostAVP = AVPCodes.getAVP(AVPCodes._DESTINATION_HOST);
            destHostAVP.setData(scscfName);
            message.addAVP(destHostAVP);

            sendMessage(message, scscfName, true);
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
        }

        LOGGER.debug("exiting");
    }

}
