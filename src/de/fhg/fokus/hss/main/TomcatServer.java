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

import de.fhg.fokus.hss.server.HSSProperties;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.jmx.HierarchyDynamicMBean;
import org.apache.log4j.spi.LoggerRepository;

import java.io.FileInputStream;

import java.util.Enumeration;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;


/**
 * Embeds tomcat ass part of the hss application.
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
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class TomcatServer
{
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(HssServer.class);

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
        LOGGER.debug("entering");

        String hostProperty = HSSProperties.TOMCAT_HOST;
        String appPath =  HSSProperties.APP_PATH;

        Engine engine = null;

        System.setProperty("catalina.home", getPath());

        // Create a embedded server
        embedded = new Embedded();

        // Add realm
        MemoryRealm memRealm = new MemoryRealm();

        embedded.setRealm(memRealm);

        // Create an engine
        engine = embedded.createEngine();
        engine.setDefaultHost("localhost");

        // Create default virutal host
        host = embedded.createHost(hostProperty, getPath() + "/webapps");

        engine.addChild(host);

        // Create the ROOT context
        Context context = embedded.createContext("", getPath() + "/ROOT");
        host.addChild(context);

        // Install the containers
        embedded.addEngine(engine);

        // Create a connector
        Connector connector =
            embedded.createConnector(hostProperty, 8080, false);
        embedded.addConnector(connector);

        embedded.start();

        // Create the Manager context
        context = embedded.createContext("/manager", getPath() + "/manager");
        host.addChild(context);

        LOGGER.debug("Starting App on path: " + appPath);
        context = embedded.createContext("/hss.web.console", getPath() + appPath);
        context.setReloadable(true);
        host.addChild(context);

        //startMBeanServer();
        LOGGER.debug("exiting");
    }

    /**
     * @throws MalformedObjectNameException
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     * @throws NotCompliantMBeanException
     */
    private void startMBeanServer()
        throws MalformedObjectNameException, InstanceAlreadyExistsException, 
            MBeanRegistrationException, NotCompliantMBeanException
    {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();

        // Create and Register the top level Log4J MBean
        HierarchyDynamicMBean hdm = new HierarchyDynamicMBean();
        ObjectName mbo = new ObjectName("log4j:hiearchy=default");
        mbs.registerMBean(hdm, mbo);

        // Add the root logger to the Hierarchy MBean
        Logger rootLogger = Logger.getRootLogger();
        hdm.addLoggerMBean(rootLogger.getName());

        // Get each logger from the Log4J Repository and add it to
        // the Hierarchy MBean created above.
        LoggerRepository r = LogManager.getLoggerRepository();
        Enumeration en = r.getCurrentLoggers();
        Logger logger = null;

        while (en.hasMoreElements())
        {
            logger = (Logger) en.nextElement();
            hdm.addLoggerMBean(logger.getName());
        }
    }

    /**
     * It stops tomcat
     * @throws Exception
     */
    public void stopTomcat() throws Exception
    {
        LOGGER.debug("entering");
        embedded.stop();
        LOGGER.info("tomcat stoped");
        LOGGER.debug("exiting");
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
