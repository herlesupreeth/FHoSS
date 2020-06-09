/*
 * $Id: TomcatServer.java 387 2007-07-06 16:14:26Z adp $
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


import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;

import org.apache.log4j.Logger;

/**
 * Embedded Tomcat Server, as part of the FHoSS
 * The following XML code snippet contains the hierarchy of the Tomcat containers:
 *                &lt;Server&gt;
 *                  &ltService&gt;
 *                    &ltConnector/&gt;
 *                    &ltEngine>
 *                      &ltHost>
 *                        &ltContext/&gt;
 *                      &lt/Host&gt;
 *                    &lt/Engine&gt;
 *                  &lt/Service&gt;
 *                &ltServer&gt;
 * The server runs in a endless thread.
 * @see  a href="http://www.onjava.com/pub/a/onjava/2002/04/03/tomcat.html">Tomcat Documentation</a>
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class TomcatServer
{
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(TomcatServer.class);

    /**
     * The tomcat server object
     */
    private Embedded embedded = null;

    /**
     * The tomcat host object
     */
    private Host host = null;

    /**
     * Points to the catalina home path.
     */
    private String path = null;

    /**
     * start the tomcat
     * @throws Exception if some exception occurs
     */
    public void startTomcat() throws Exception
    {
        LOGGER.info("Tomcat-Server is started.");

        Engine engine = null;

        System.setProperty("catalina.home", this.path);

        // Create an embedded server
        embedded = new Embedded();
        // Add a realm
        MemoryRealm memRealm = new MemoryRealm();
        embedded.setRealm(memRealm);

        // Create an engine
        engine = embedded.createEngine();
        engine.setDefaultHost(HSSProperties.TOMCAT_HOST);
        
        // Install the containers
        embedded.addEngine(engine);

        // Create a connector
        Connector connector = embedded.createConnector(HSSProperties.TOMCAT_HOST, Integer.parseInt(HSSProperties.TOMCAT_PORT), false);
        // enable DNS lookups
        connector.setEnableLookups(true);
        embedded.addConnector(connector);
        embedded.start();

        // Create default virutal host
        host = embedded.createHost(HSSProperties.TOMCAT_HOST, this.path + "/webapps");
        engine.addChild(host);

        // Create the ROOT context
        Context context = embedded.createContext("", this.path + "/ROOT");
        host.addChild(context);
        
        // Create the Manager context
        context = embedded.createContext("/manager", this.path + "/manager");
        host.addChild(context);
        
        context = embedded.createContext("/hss.web.console", this.path + "/hss.web.console");
        context.setReloadable(true);
        host.addChild(context);
        LOGGER.info("WebConsole of FHoSS was started !");
    }


    /**
     * It stops tomcat
     * @throws Exception
     */
    public void stopTomcat() throws Exception
    {
        embedded.stop();
        LOGGER.info("Tomcat was stoped !");
    }

    /**
     *  It returns the path
     *  @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * It sets the path
     * @param path the path
     */
    public void setPath(String path)
    {
        this.path = path;
    }
}
