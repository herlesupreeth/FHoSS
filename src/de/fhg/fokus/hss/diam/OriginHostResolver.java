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

import java.util.Iterator;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.model.DiamServer;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * Origin Host to Sip Server resolver.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class OriginHostResolver
{
    /** the Logger */
    private static final Logger LOGGER =
        Logger.getLogger(OriginHostResolver.class);

    /**
     * Map contains the hosts
     */
    private static TreeMap hosts;

    static{
    	init();
    }
    
    /**
     *  Initialization method
     */
    public static void init()
    {
        int size = 0;
        hosts = new TreeMap();

        Session session = HibernateUtil.currentSession();
        Iterator it =
            session.createCriteria(DiamServer.class).list().iterator();

        while (it.hasNext())
        {
            DiamServer diamServer = (DiamServer) it.next();
            hosts.put(diamServer.getServer(), diamServer.getHost());
            size++;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(size + " server/hosts added");
        }

        HibernateUtil.closeSession();
    }

    /**
     * Returns the origin host name of assigned sip server name
     * @param serverName sip server name
     * @return origin host name
     */
    public static String getOriginHost(String serverName)
    {
        return (String) hosts.get(serverName);
    }

    /**
     * Store a Origin Host by sip server name
     * @param serverName sip server name
     * @param originHost origin host name
     */
    public static void setOriginHost(String serverName, String originHost)
    {
        LOGGER.debug("Add: " + serverName + " To: " + originHost);

        Object oldOriginHostObj = hosts.get(serverName);

        if (oldOriginHostObj == null)
        {
            // Create a new resolver entry
            hosts.put(serverName, originHost);
            Session session = HibernateUtil.currentSession();
            Transaction tx = session.beginTransaction();
            DiamServer diamServer = new DiamServer();
            diamServer.setServer(serverName);
            diamServer.setHost(originHost);
            session.save(diamServer);
            tx.commit();
            session.flush();
            HibernateUtil.closeSession();
        }
        else
        {
            String oldOriginHost = (String) oldOriginHostObj;

            if (originHost.equals(oldOriginHost) == false)
            {
                // Update a resolver entry
                hosts.put(serverName, originHost);

                Session session = HibernateUtil.currentSession();
                Transaction tx = session.beginTransaction();
                DiamServer diamServer =
                    (DiamServer) session.load(DiamServer.class, serverName);
                diamServer.setHost(originHost);
                session.save(diamServer);
                tx.commit();
                session.flush();
                HibernateUtil.closeSession();
            }
        }
    }

}
