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
package de.fhg.fokus.hss.util;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;


/**
 * This class does not only take care of the SessionFactory with its static initializer,
 * but also has a ThreadLocal variable which holds the Session for the current thread.
 * Make sure you understand the Java concept of a thread-local variable before you try
 * to use this helper.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HibernateUtil
{
    /** the logger */
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);
    /** the session factory */
    private static final SessionFactory sessionFactory;

    static
    {
        try
        {
            //Create the SessionFactory
            sessionFactory =
                new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex)
        {
            //Make sure you log the exception, as it might be swallowed
            LOGGER.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    /** thread local variable  */
    public static final ThreadLocal session = new ThreadLocal();

    /**
     *  this method provides the current session
     *  @return the current session
     */
    public static Session currentSession()
    {
        Session s = (Session) session.get();

        //	 Open a new Session, if this Thread has none yet
        if (s == null)
        {
            s = sessionFactory.openSession();
            session.set(s);
        }

        return s;
    }

    /**
     *  This method closes the Session
     */
    public static void closeSession()
    {
        Session s = (Session) session.get();

        if (s != null)
        {
            s.close();
        }

        session.set(null);
    }
}
