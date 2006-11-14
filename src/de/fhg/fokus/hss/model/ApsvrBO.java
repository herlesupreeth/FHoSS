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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.server.cx.op.UpdateCxOperation;


/**
 * Contains buisness method and logic for an application server.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ApsvrBO extends HssBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ApsvrBO.class);

    /**
     * Value for default handling session continued
     */
    public static final int DH_SESSION_CONTINUED = 0;

    /**
     * Value for default handling session terminated
     */
    public static final int DH_SESSION_TERMINATED = 1;

    /**
     * loads application server with the help of provided primary key
     * @param primaryKey
     *
     * @return application server
     */
    public Apsvr load(Serializable primaryKey)
    {
        Apsvr apsvr = (Apsvr) getSession().load(Apsvr.class, primaryKey);
        apsvr.addPropertyChangeListener(apsvr);

        return apsvr;
    }

    /**
     * loads the application server permission list 
     * with the help of provided primary key
     *
     * @param primaryKey
     *
     * @return the application server permission list
     */
    public AsPermList loadAsPermList(Serializable primaryKey)
    {
        AsPermList asPermList = (AsPermList) getSession()
                                                 .load(AsPermList.class,
                primaryKey);

        return asPermList;
    }

    /**
     * saves or updates the provides application server and 
     * application server permission list
     *
     * @param apsvr application server
     * @param asPermList application server permission list
     */
    public void saveOrUpdate(Apsvr apsvr, AsPermList asPermList)
    {
        LOGGER.debug("entering");

        beginnTx();
        getSession().saveOrUpdate(apsvr);
        asPermList.setApsvrId(apsvr.getApsvrId());
        getSession().saveOrUpdate(asPermList);
        endTx();

        if (apsvr.isChange() == true)
        {
            commitCxChanges(apsvr);
        }

        closeSession();
        LOGGER.debug("exiting");
    }

    /**
     * commits cx changes in the database
     * @param apsvr application server
     */
    private void commitCxChanges(Apsvr apsvr)
    {
        LOGGER.debug("entering");

        try
        {
            Query query = getSession()
                              .createQuery("select ifc from de.fhg.fokus.hss.model.Ifc as ifc where ifc.apsvr.apsvrId = ?");
            query.setInteger(0, apsvr.getApsvrId());

            Iterator itIfc = query.list().iterator();

            ArrayList impuIds = new ArrayList();

            while (itIfc.hasNext())
            {
                Ifc ifc = (Ifc) itIfc.next();

                if (ifc.getSvp() != null)
                {
                    Iterator it = ifc.getSvp().iterator();

                    while (it.hasNext())
                    {
                        Svp svp = (Svp) it.next();

                        Iterator itImpu = svp.getImpus().iterator();

                        while (itImpu.hasNext())
                        {
                            Impu impu = (Impu) itImpu.next();

                            if (impuIds.equals(impu.getImpuId()) == false)
                            {
                                PublicIdentity pi = new PublicIdentity();
                                pi.setIdentity(impu.getSipUrl());

                                CxUserProfil cxUserProfil = new CxUserProfil(pi);

                                if (cxUserProfil.isRegistered())
                                {
                                    URI privateIdentity = new URI(cxUserProfil.getImpi()
                                                                              .getImpiString());
                                    UpdateCxOperation cxOperation = new UpdateCxOperation(cxUserProfil,
                                            privateIdentity);
                                    cxOperation.execute();
                                    impuIds.add(impu.getImpuId());
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (DiameterException e)
        {
            LOGGER.error(this, e);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error(this, e);
        }

        LOGGER.debug("exiting");
    }
}
