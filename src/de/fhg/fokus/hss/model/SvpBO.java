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

import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.server.cx.op.UpdateCxOperation;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * This class provides database specific functions for service profiles
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class SvpBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(SvpBO.class);

    /**
     * It creates service profile
     * @return service profile
     */    
    public Svp create()
    {
        Svp svp = new Svp();
        return svp;
    }

    /**
     * It loads the service profile from database with the help of
     * provided primary key
     * @param primaryKey primary key
     * @return service profile
     */
    public Svp load(Serializable primaryKey)
    {
        Svp svp = (Svp) HibernateUtil.getCurrentSession().load(Svp.class, primaryKey);
        svp.addPropertyChangeListener(svp);
        return svp;
    }

    /**
     * It saves or updates the service profile provided as argument 
     * and performs sh and cx specific functions if necessary
     * @param svp service profile 
     */
    public void saveOrUpdate(Svp svp)
    {
        HibernateUtil.getCurrentSession().saveOrUpdate(svp);

        if (svp.isChange())
        {
            commitShChanges(svp);
            commitCxChanges(svp);
        }
    }

    /**
     * commit ShIterface specific changes
     * @param svp service profile
     */
    private void commitShChanges(Svp svp)
    {
      // to do
    }

    /**
     * It commits Cx specific changes implied by svp provided as argument
     * @param svp service profile
     */
    public void commitCxChanges(Svp svp)
    {
        LOGGER.debug("entering");
        CxUserProfil cxUserProfil = null;
        
        try{
            ArrayList operationsList = new ArrayList();
            Iterator itImpu = null;

            itImpu = svp.getImpus().iterator();
            while (itImpu.hasNext()){
                Impu impu = (Impu) itImpu.next();
                cxUserProfil = new CxUserProfil(impu);

                if (cxUserProfil.isRegistered()){
                    URI privateIdentity = new URI(cxUserProfil.getImpi().getImpiString());
                    UpdateCxOperation cxOperation = new UpdateCxOperation(cxUserProfil, privateIdentity);
                    operationsList.add(cxOperation);
                }
            }

            Iterator it = operationsList.iterator();
            while (it.hasNext()){
                UpdateCxOperation cxOperation = (UpdateCxOperation) it.next();
                cxOperation.execute();
            }
        }
        catch (DiameterException e){
            LOGGER.error(svp, e);
        }
        catch (URISyntaxException e){
            LOGGER.error(svp, e);
        }

        LOGGER.debug("exiting");
    }
}
