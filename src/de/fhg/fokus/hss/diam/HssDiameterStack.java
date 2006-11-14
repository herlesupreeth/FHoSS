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
package de.fhg.fokus.hss.diam;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.HSScxOperations;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.hss.diam.cx.LIRCommandListener;
import de.fhg.fokus.hss.diam.cx.MARCommandListener;
import de.fhg.fokus.hss.diam.cx.PPRCommandAction;
import de.fhg.fokus.hss.diam.cx.RTRCommandAction;
import de.fhg.fokus.hss.diam.cx.SARCommandListener;
import de.fhg.fokus.hss.diam.cx.UARCommandListener;
import de.fhg.fokus.hss.diam.sh.PNRCommandAction;
import de.fhg.fokus.hss.diam.sh.PURCommandListener;
import de.fhg.fokus.hss.diam.sh.SNRCommandListener;
import de.fhg.fokus.hss.diam.sh.UDRCommandListener;
import de.fhg.fokus.hss.diam.zh.MARzhCommandListener;
import de.fhg.fokus.hss.server.cx.HSScxOperationsImpl;
import de.fhg.fokus.hss.server.sh.HSSshOperationsImpl;
import de.fhg.fokus.hss.server.zh.HSSzhOperationsImpl;
import de.fhg.fokus.sh.HSSOperations;
import de.fhg.fokus.zh.ZhOperations;


/**
 * Diameter Stack starts the HSS - Diameter Peer and adds the command listeners 
 * to the peer which listen to cx, sh and zh specific requests and react upon them in
 * appropriate manner.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HssDiameterStack
{
    /** The Logger */
    static final Logger LOGGER = Logger.getLogger(HssDiameterStack.class);
    /** An Object representing the CxOperations of HSS */
    HSScxOperations hssCxOperations = null;
    /** An Object representing the ShOperations of HSS */
    HSSOperations hssShOperations = null;
    /** An Object representing the ZhOperations of HSS */
    ZhOperations hssZhOperations = null;
    /** The diameter peer */
    public static DiameterPeer diameterPeer;

    /**
     * This method starts up the HSS
     * @throws InterruptedException
     */
    public void startup() throws InterruptedException
    {
        LOGGER.debug("entering");
        hssCxOperations = new HSScxOperationsImpl();
        hssShOperations = new HSSshOperationsImpl();
        hssZhOperations = new HSSzhOperationsImpl();
        
        diameterPeer = new DiameterPeer("DiameterPeerHSS.xml");
        // Add HSS-cx Commands
        diameterPeer.addEventListener(new UARCommandListener(hssCxOperations, diameterPeer));
        diameterPeer.addEventListener(new MARCommandListener(hssCxOperations, diameterPeer));
        diameterPeer.addEventListener(new LIRCommandListener(hssCxOperations, diameterPeer));
        diameterPeer.addEventListener(new SARCommandListener(hssCxOperations, diameterPeer));
        // Add CSCF-cx Command-Action(Listener)
        diameterPeer.addEventListener(new RTRCommandAction());
        diameterPeer.addEventListener(new PPRCommandAction());
        // Add HSS-sh Commands
        diameterPeer.addEventListener(new UDRCommandListener(hssShOperations, diameterPeer));
        diameterPeer.addEventListener(new PURCommandListener(hssShOperations, diameterPeer));
        diameterPeer.addEventListener(new SNRCommandListener(hssShOperations, diameterPeer));
        // Add AS-Sh Command-Action(Listener)
        diameterPeer.addEventListener(new PNRCommandAction());
        // Add HSS-zh Commands
        diameterPeer.addEventListener(new MARzhCommandListener(hssZhOperations, diameterPeer));
        OriginHostResolver.init();
        LOGGER.debug("exiting");
    }

    /**
     * This method shuts down the HSS
     */
    public void shutdown()
    {
        LOGGER.debug("entering");
        diameterPeer.shutdown();
        LOGGER.debug("exiting");
    }
}
