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

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.diam.Constants.Vendor;
import de.fhg.fokus.sh.CurrentLocation;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.HSSOperations;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.RequestedDomain;
import de.fhg.fokus.sh.data.ShData;


/**
 * UDR Command listener. Listen for UDR Commands. Extract the Diameter specfic AVPs and
 * generate Java objects. Call the specific operation and generate from Java Answers the
 * Diameter AVP's and send the response message.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class UDRCommandListener extends ShCommandListener{
    
	/** logger */
    private static final Logger LOGGER = Logger.getLogger(UDRCommandListener.class);
    /** Counter to count calls.*/
    public static long counter = 0;
    /** the command id for UDR */
    private static final int COMMAND_ID = Constants.Command.UDR;
    /** an object representing hss operations */
    private HSSOperations operations;

    /**
     * constructor
     * @param _operations HssOpeations
     * @param _diameterPeer diameterPeer
     */ 
    public UDRCommandListener(HSSOperations _operations, DiameterPeer _diameterPeer){
        super(_diameterPeer);
        this.operations = _operations;
    }

    /** 
     * This method recieves and processes the diameter message UDR
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter message   
     */ 
    public void recvMessage(String FQDN, DiameterMessage requestMessage){
        if (requestMessage.commandCode == COMMAND_ID){
        	
            counter++;
            LOGGER.debug("entering");
            LOGGER.debug(FQDN);

            try{
            	
            	// request extract
                URI publicIdentity = loadPublicIdentity(requestMessage);
                RequestedData requestedData = loadDataReference(requestMessage);
                RequestedDomain requestedDomain = null;
                CurrentLocation currentLocation = null;
                byte[] serviceIndication = null;
                
                AVP svcIndAVP = requestMessage.findAVP(Constants.AVPCode.SH_SERVICE_INDICATION, true, Vendor.V3GPP);
                if(svcIndAVP != null){
                	serviceIndication = svcIndAVP.data;
                }
                
                String applicationServerIdentity = loadOriginHost(requestMessage);
                URI applicationServerName = null;
                AVP asNameAVP = requestMessage.findAVP(Constants.AVPCode.SH_SERVER_NAME, true, Vendor.V3GPP);
                if(asNameAVP != null){
                	applicationServerName = new URI(new String(asNameAVP.data));
                }
                
                // response generate
                ShData shData = operations.shPull(publicIdentity, requestedData, requestedDomain,currentLocation, 
                		serviceIndication, applicationServerIdentity, applicationServerName);

                // create the diameter response message 
                DiameterMessage responseMessage = diameterPeer.newResponse(requestMessage);

                /* add Vendor-Specific app id */
                AVP vendorSpecificApplicationID = new AVP(Constants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true, Constants.Vendor.DIAM);
                AVP vendorID = new AVP(Constants.AVPCode.VENDOR_ID, true, Constants.Vendor.DIAM);
                vendorID.setData(Constants.Vendor.V3GPP);
                vendorSpecificApplicationID.addChildAVP(vendorID);
                AVP applicationID = new AVP(Constants.AVPCode.AUTH_APPLICATION_ID, true,  Constants.Vendor.DIAM);
                applicationID.setData(Constants.Application.SH);
                vendorSpecificApplicationID.addChildAVP(applicationID);
                responseMessage.addAVP(vendorSpecificApplicationID);
        		
        		/* add Auth-Session-State, no state maintained */
                AVP authenticationSessionState = new AVP(Constants.AVPCode.AUTH_SESSION_STATE, true, Constants.Vendor.DIAM);
                authenticationSessionState.setData(1);
                responseMessage.addAVP(authenticationSessionState);

                /* add User-Data */
                AVP userDataAVP = new AVP(Constants.AVPCode.SH_USER_DATA, true, Constants.Vendor.V3GPP);
                StringWriter sw = new StringWriter();
                shData.marshal(sw);
                userDataAVP.setData(sw.getBuffer().toString());
                responseMessage.addAVP(userDataAVP);

                /* add result code */
                AVP responseCode = new AVP(Constants.AVPCode.RESULT_CODE, true, Constants.Vendor.DIAM);
                responseCode.setData(ResultCode._DIAMETER_SUCCESS);
                responseMessage.addAVP(responseCode);
                
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
