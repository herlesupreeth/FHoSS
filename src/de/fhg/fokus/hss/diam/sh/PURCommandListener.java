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

import java.io.StringReader;
import java.net.URI;

import org.apache.log4j.Logger;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.diam.AVPCodes;
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.Constants.Vendor;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.HSSOperations;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.data.ShData;


/**
 * This class represents PUR Command listener. For more imformations about PNR 
 * plese refer to 3GPP TS 29.229
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PURCommandListener extends ShCommandListener
{
    /** Logger */
    private static final Logger LOGGER =
        Logger.getLogger(PURCommandListener.class);
    /** counter to count calls*/
    public static long counter = 0;
    /** command id for PUR */
    private static final int COMMAND_ID = Constants.COMMAND.PUR;
    /** an object representing the HSS operations */
	private HSSOperations operations;
    
    /**
     * constructor
     * @param _operations the HSS operations
     * @param _diameterPeer diameterpeer
     */
    public PURCommandListener(
        HSSOperations _operations, DiameterPeer _diameterPeer)
    {
        super( _diameterPeer);
        this.operations = _operations;
    }

    /** 
     * This method recieves the diameter message PUR, increments the counter,
     * processes it and sends out the appropriate response.
     * 
     * @param FQDN fully qualified domain name
     * @param requestMessage the diameter message   
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
               URI publicIdentity = loadPublicIdentity(requestMessage);
               String applicationServerIdentity = loadOriginHost(requestMessage);
               RequestedData requestedData = loadDataReference(requestMessage);
               
               AVP shDataAVP = avpLookUp(requestMessage, AVPCodes._SH_USER_DATA, true, Vendor.V3GPP);
               String shDataString = new String(shDataAVP.data);
               ShData shData = (ShData) ShData.unmarshal(new StringReader(shDataString));
 
               operations.shUpdate(publicIdentity, shData, applicationServerIdentity, requestedData);

               DiameterMessage responseMessage =
                   diameterPeer.newResponse(requestMessage);

               addDiameterSuccess(responseMessage);
               diameterPeer.sendMessage(FQDN, responseMessage);
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
}
