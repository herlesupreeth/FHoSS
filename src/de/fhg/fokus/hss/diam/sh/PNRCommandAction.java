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
package de.fhg.fokus.hss.diam.sh;

import java.io.StringWriter;
import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.AVPCodes;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.HssDiameterStack;
import de.fhg.fokus.hss.diam.Constants.Application;
import de.fhg.fokus.sh.data.ShData;


/**
 * Implementation of the SH-Command PNR
 * Send a PNR Command per Diameter to the assigned Application Server.
 * For more imformations about PNR plese refer to 3GPP TS 29.229
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PNRCommandAction extends ShCommandAction
{
    /** Logger */
    private static final Logger LOGGER =
        Logger.getLogger(PNRCommandAction.class);
    /** counter to count calls*/
    public static long counter = 0;
    /** command id for PNR */
    private static final int COMMAND_ID = Constants.COMMAND.PNR;
    /** address of application server*/
    private String asAddress;
    /** the public identity*/
    private URI publicIdentity;
    /** data */
    private ShData shData;
    
    /** default constructor */
    public PNRCommandAction(){
    	// void();
    }
    
    /**
     * constructor
     * @param _asAddress Name of the assigned application server.
     * @param _publicIdentity Public Identity of the user.
     * @param _shData Changed data how notified.
     */
    public PNRCommandAction(String _asAddress, URI _publicIdentity, ShData _shData)
    {
        LOGGER.debug("entering");
        this.asAddress = _asAddress;
        this.publicIdentity = _publicIdentity;
        this.shData = _shData;
        LOGGER.debug("exiting");
    }

    /**
     * Receive PNR - Answers, count them.
     * @param FQDN fully qualified domain name
     * @param msg diametermessage
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
     * It processes the recieved diameter request and sends out the appropriate 
     * diameter response to the communicating peer.
     *
     */
    public void execute()
    {
        LOGGER.debug("entering");

        DiameterMessage message = null;

        try
        {
            message =
                HssDiameterStack.diameterPeer.newRequest(
                    COMMAND_ID, Application.SH);
            
            AVP userDataAVP = AVPCodes.getAVP(AVPCodes._SH_USER_DATA);
            StringWriter sw = new StringWriter();
            shData.marshal(sw);
            userDataAVP.setData(sw.getBuffer().toString());
            message.addAVP(userDataAVP);
            
            AVP userIdentityAVP = AVPCodes.getAVP(AVPCodes._SH_USER_IDENTITY);
            AVP publicIdentityAVP = AVPCodes.getAVP(AVPCodes._SH_PUBLIC_IDENTITY);
            
            // XLB
            //publicIdentityAVP.setData(publicIdentity.getPath());
            publicIdentityAVP.setData((publicIdentity.toString()).trim());
            
            userIdentityAVP.addChildAVP(publicIdentityAVP);
            message.addAVP(userIdentityAVP);
            
            sendMessage(message, asAddress, false);
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
        }

        LOGGER.debug("exiting");
    }
}
