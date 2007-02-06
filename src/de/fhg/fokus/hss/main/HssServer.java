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
package de.fhg.fokus.hss.main;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import de.fhg.fokus.hss.diam.HssDiameterStack;


/**
 * HSS Server:
 * 
 * It is the main class which is starting FHoSS:
 * 		- It starts the Tomcat Server
 * 		- It starts the Diameter Stack
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class HssServer
{
    private static final Logger LOGGER = Logger.getLogger(HssServer.class);
    
    
    /**
     * the current time at start up
     */
    public static long STARTUP_TIME = System.currentTimeMillis();

    /**
     * The main method
     * @param args
     */
    public static void main(String[] args)
    {
    	try{
    		Thread.sleep(1000);
    	}
    	catch(InterruptedException e){
    		
    	}
    	LOGGER.debug("Starting FHoSS...");
        
        try{
        	// Starting Tomcat
        	
            TomcatServer tomcatServer = new TomcatServer();
            tomcatServer.setPath("./");
            tomcatServer.startTomcat();
            STARTUP_TIME = System.currentTimeMillis();
            
            // Starting Diameter Stack
            
            HssDiameterStack diameterStack = new HssDiameterStack();
            diameterStack.startup();
            LOGGER.info("FHoSS was started and is ready for use!");
            waitForExit();
            
            // stoping FHoSS
            tomcatServer.stopTomcat();
           	diameterStack.shutdown();
            LOGGER.info("FHoSS was stopped!");
            
            System.exit(0);
        }
        catch (Exception e){
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

	/**
	 * This method waits until exit is typed in the console
	 * If wait is typed, then it returns.
	 */
	private static void waitForExit() {
		byte[] buffer = new byte[80]; 
		int read;
		String input="";
		while (true){
		    try{
		        LOGGER.info("Type 'exit' if you want to exit.");			       
		        read = System.in.read(buffer, 0, 80);			        
		        input = new String(buffer, 0, read);
		        input  = input.trim();
		    }
		    catch (IOException e){
		        e.printStackTrace();
		    }
			   
		    if(input.equals("exit")){
		    	return;
		    }
		}
	}
}
