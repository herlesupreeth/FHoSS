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
package de.fhg.fokus.hss.model;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.UserUnknown;


/**
 * The IMS ShUserProfil contains the private identity and assigned public identity.
 * Also it offers method to get the barring status or the roaming allowed status
 * to specific networks (represented by string, e.g. fhg.fokus.de).
 * A given public identity can directly accessed by getter and setter methods.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ShUserProfil
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ShUserProfil.class);

    /**
     * The private user identity
     */
    private Impi impi = null;

    /**
     * List of assigned public user identities
     */
    private Set impuList = null;

    /**
     * a given public user identity pointet for direct access.
     */
    private Impu impu = null;

    /**
     * Called uri.
     */
    private URI uri = null;
    /**
     * The hibernate database session
     */
    private Session session = null;

    /**
     * constructor
     * @param publicUserIdentity public identity
     * @throws DiameterException
     */
    public ShUserProfil(URI publicUserIdentity) throws DiameterException
    {
        LOGGER.debug("entering");
        this.uri = publicUserIdentity;
        session = HibernateUtil.getCurrentSession();
        loadByPublicIdentity(publicUserIdentity);
        LOGGER.debug("exiting");
    }

    /**
     * It loads the necessary data with the help of public identity
     * provided as argument
     * @param publicIdentity public identity 
     * @throws DiameterException
     */
    private void loadByPublicIdentity(URI publicIdentity)
        throws DiameterException
    {
        LOGGER.debug("entering");

        Query query =
            session.createQuery(
                "select impu from de.fhg.fokus.hss.model.Impu as impu where impu.sipUrl = ?");
        query.setString(0, "sip:" + publicIdentity.getPath());

        List resultList = query.list();

        if (resultList.size() > 0)
        {
            this.impu = (Impu) resultList.get(0);
        }
        else
        {
            throw new UserUnknown();
        }

        if (this.impu.getImpis() == null)
        {
            throw new UserUnknown();
        }
        else
        {
            try
            {
                this.impi = ((Impi) this.impu.getImpis().iterator().next());
            }
            catch (NullPointerException e)
            {
                throw new UserUnknown();
            }
        }

        impuList = impi.getImpus();

        LOGGER.debug("exiting");
    }

    /**
     * Check if the network identifier is assigned by the user.
     * @param visitedNetworkIdentifier String contains the name of the visited network.
     * @return true is roaming is allowed, else false.
     */    
    public boolean isRoamingAllowed(String visitedNetworkIdentifier)
    {
        Set roams = impi.getRoams();

        if (roams == null)
        {
            return false;
        }

        Iterator it = roams.iterator();
        boolean roamingNotAllowed = false;

        while (it.hasNext())
        {
            Network roam = (Network) it.next();

            if (roam.getNetworkString().equals(visitedNetworkIdentifier))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if user is barred. User means the request impu or any impi assigned impu.
     * @return true if user is barred from the network, else false;
     */
    public boolean isBarred()
    {
        LOGGER.debug("entering");

        if (impuList != null)
        {
            Iterator it = impuList.iterator();

            while (it.hasNext())
            {
                Impu next = (Impu) it.next();

                if (next.getBarringIndication().booleanValue() == true)
                {
                    LOGGER.debug("exiting (user is barred)");

                    return true;
                }
            }
        }
        else
        {
            return impu.getBarringIndication();
        }

        LOGGER.debug("exiting");

        return false;
    }

    /**
     * Update current user profil data to database.
     */
    public void update()
    {
        LOGGER.debug("entering");

        if (impi != null)
        {
            session.update(impi);
        }

        if (impu != null)
        {
            session.update(impu);
        }

        LOGGER.debug("exiting");
    }

    /**
     * Getter method for private identity
     * @return private identity
     */
    public Impi getImpi()
    {
        return impi;
    }
  
    /**
     * Setter method for private identity
     * @param impi private identity
     */     
    public void setImpi(Impi impi)
    {
        this.impi = impi;
    }

    /**
     * Getter method for public identity
     * @return public identity
     */
    public Impu getImpu()
    {
        return impu;
    }

    /**
     * Setter method for public identity
     * @param impu public identity
     */
    public void setImpu(Impu impu)
    {
        this.impu = impu;
    }

    /**
     * Getter method for list of public identities
     * @return list of public identities
     */
    public Set getImpuList()
    {
        return impuList;
    }

    /**
     * Setter method for list of public identities
     * @param impuList list of public identities
     */ 
    public void setImpuList(Set impuList)
    {
        this.impuList = impuList;
    }

    /**
     * Getter method for Uri
     * @return uri
     */
	public URI getUri() {
		return uri;
	}
}
