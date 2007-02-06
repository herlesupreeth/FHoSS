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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.fhg.fokus.cx.ChargingInfoSet;
import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.HssDiameterStack;
import de.fhg.fokus.hss.diam.Constants.Application;
import de.fhg.fokus.hss.diam.CommandAction;
import de.fhg.fokus.hss.util.Util;

/**
 * This class implements the PPR Command Action. For more imformations about PPR
 * please refer to 3GPP TS 29.229. 
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PPRCommandAction extends CommandAction
{
    /** counter */
    public static long counter = 0;
    /** counter */
    public static long rCounter = 0;
    /** the logger */
    private static final Logger LOGGER =
        Logger.getLogger(PPRCommandAction.class);
    /** the command id for PPR */     
    private static final int COMMAND_ID = Constants.Command.PPR;
    /** the private user identity */
    private URI privateUserIdentity;
    /** ims subscription */
    private IMSSubscription userData;
    /** Charging Info set*/
    private ChargingInfoSet chargingInfoSet;
    /** Name of Serving call session control function*/
    private String scscfName;

    /**
     * constructor
     * @param _privateUserIdentity Private Identity
     * @param _userData ims subscription
     * @param _chargingInfoSet chanrging info set
     * @param _scscfName name of scscf
     */      
    public PPRCommandAction(
        URI _privateUserIdentity, IMSSubscription _userData,
        ChargingInfoSet _chargingInfoSet, String _scscfName)
    {
        LOGGER.debug("entering");
        this.privateUserIdentity = _privateUserIdentity;
        this.userData = _userData;
        this.chargingInfoSet = _chargingInfoSet;
        this.scscfName = _scscfName;
        LOGGER.debug("exiting");
    }

    /**
     *  The default constructor 
     */
    public PPRCommandAction()
    {
        LOGGER.debug("entering");
        LOGGER.debug("exiting");
    }

    
    /**
     * This method recieves the diameter message PPR. It does nothing else other 
     * than incrementing the message counter.
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
     *
     */
    public void execute(){
        LOGGER.debug("entering");
        rCounter++;
        DiameterMessage message;

        try{
            message = HssDiameterStack.diameterPeer.newRequest(COMMAND_ID, Application.CX);
            AVP a, b;
            
    		/* session-id */
    		a = new AVP(263,true,0);
    		a.setData(privateUserIdentity.getPath() + ";11271298949;" + System.currentTimeMillis());
    		message.addAVP(a);

    		/* vendor-specific app id */
            AVP vendorSpecificApplicationID = new AVP(Constants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, Constants.Vendor.DIAM);
            AVP vendorID = new AVP(Constants.AVPCode.VENDOR_ID, true, Constants.Vendor.DIAM);
            vendorID.setData(Constants.Vendor.V3GPP);
            vendorSpecificApplicationID.addChildAVP(vendorID);
            AVP applicationID = new AVP(Constants.AVPCode.AUTH_APPLICATION_ID, true,  Constants.Vendor.DIAM);
            applicationID.setData(Constants.Application.CX);
            vendorSpecificApplicationID.addChildAVP(applicationID);
            message.addAVP(vendorSpecificApplicationID);
    		
    		
    		/* auth-session-state, no state maintained */
            AVP authenticationSessionState = new AVP(Constants.AVPCode.AUTH_SESSION_STATE, true, Constants.Vendor.DIAM);
            authenticationSessionState.setData(1);
            message.addAVP(authenticationSessionState);

    		/* destionation host and realm */
            AVP destHostAVP = new AVP(Constants.AVPCode.DESTINATION_HOST, true, Constants.Vendor.DIAM);
            destHostAVP.setData(Util.getHost(scscfName));
            message.addAVP(destHostAVP);
            
            AVP destRealm = new AVP(283,true,0);
            destRealm.setData(Util.getRealm(scscfName));
            message.addAVP(destRealm);
    		
            /* username */ 
            AVP privateUserAVP = new AVP(Constants.AVPCode.PRIVATE_USER_IDENTITY, true, Constants.Vendor.DIAM);
            privateUserAVP.setData(privateUserIdentity.getPath());
            message.addAVP(privateUserAVP);

            /* userdata */ 
            
            if (userData != null){
                SARCommandListener.saveIMSSubscription(userData, message);
            }
            else{
                SARCommandListener.saveChargingInfoSet(chargingInfoSet, message);
            }

            sendMessage(message, scscfName, true);
        }
        catch (MarshalException e)
        {
            LOGGER.error(this, e);
        }
        catch (ValidationException e)
        {
            LOGGER.error(this, e);
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
        }        

        LOGGER.debug("exiting");
    }
}
