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

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import de.fhg.fokus.cx.exceptions.DiameterException;


/**
 * This class provides database specific functions for trigger point
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class TrigptBO extends HssBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(Trigpt.class);
    /** constant for uri */
    public static final int TYPE_URI = 1;
    /** constant for sip method */
    public static final int TYPE_SIP_METHOD = 2;
    /** constant for session case */
    public static final int TYPE_SESSION_CASE = 3;
    /** constant for session description */
    public static final int TYPE_SESSION_DESC = 4;
    /** constant for sip header */
    public static final int TYPE_SIP_HEADER = 5;


    /**
     * It loads the trigger point from database with the help of
     * provided primary key
     * @param primaryKey primary key
     * @return trigger point
     */    
    public Trigpt load(Serializable primaryKey)
    {
        Trigpt trigpt = (Trigpt) getSession().load(Trigpt.class, primaryKey);
        trigpt.addPropertyChangeListener(trigpt);

        return trigpt;
    }


    /**
     * It saves or updates the trigger point provided as argument 
     * and performs sh and cx specific functions if necessary
     * @param trigpt trigger point
     */
    public void saveOrUpdate(Trigpt trigpt)
    {
        LOGGER.debug("entering");

        try
        {
            beginnTx();
            getSession().saveOrUpdate(trigpt);
            endTx();

            if (trigpt.isChange())
            {
                Query query =
                    getSession().createQuery(
                        "select ifc from de.fhg.fokus.hss.model.Ifc as ifc where ifc.trigpt.trigptId = ?");
                query.setInteger(0, trigpt.getTrigptId());

                List ifcList = query.list();

                if (ifcList.isEmpty() == false)
                {
                    commitCxShChanges(ifcList);
                }
            }
        }
        catch (DiameterException e)
        {
            LOGGER.error(TrigptBO.class, e);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error(TrigptBO.class, e);
        }
        finally
        {
            closeSession();
        }

        LOGGER.debug("exiting");
    }

    /**
     * commit cx and sh specific changes 
     * @param ifcList list of initial filter criterias
     * @throws DiameterException
     * @throws URISyntaxException
     */
    private void commitCxShChanges(List ifcList)
        throws DiameterException, URISyntaxException
    {
        LOGGER.debug("entering");

        Iterator it = ifcList.iterator();
        IfcBO ifcBO = new IfcBO();

        ifcBO.setSession(getSession());

        Ifc ifc = null;

        while (it.hasNext())
        {
            ifc = (Ifc) it.next();
            ifcBO.commitCxChanges(ifc);
            ifcBO.commitShChanges(ifc);
        }

        LOGGER.debug("exiting");
    }
}
