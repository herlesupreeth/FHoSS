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
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.server.sh.ASshOperationsImpl;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.sh.data.ShData;
import de.fhg.fokus.sh.data.ShIMSData;
import de.fhg.fokus.hss.util.*;

/**
 * This a helping class which assists loading, updating or saving
 * private identity from or in the database. Further it performs
 * other Sh specific functions.
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpiBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ImpiBO.class);
    /**
     * It loads the private identity from database with the help of
     * provided primary key
     * @param primaryKey primary key
     * @return private identity 
     */
    public Impi load(Serializable primaryKey)
    {
        Impi impi = (Impi) HibernateUtil.getCurrentSession().get(Impi.class, primaryKey);
        impi.addPropertyChangeListener(impi);

        return impi;
    }

    /**
     * It saves or updates the private identity provided as argument 
     * and performs sh specific functions if necessary
     * @param impi private identity 
     */
    public void saveOrUpdate(Impi impi)
    {
        LOGGER.debug("entering");

        HibernateUtil.getCurrentSession().saveOrUpdate(impi);

        if (impi.isChangeScscfName()){
                commitShChanges(impi);
        }

        LOGGER.debug("exiting");
    }

    /**
     * It commits Sh specific changes implied by impi provided as argument
     * @param impi private identity
     */
    private void commitShChanges(Impi impi)
    {
        LOGGER.debug("entering");

        if (impi.getImpus().isEmpty() == false){
            ASshOperationsImpl operationsImpl = new ASshOperationsImpl();
            ShData shData = new ShData();
            ShIMSData shIMSData = new ShIMSData();
            shIMSData.setSCSCFName(impi.getScscfName());
            shData.setShIMSData(shIMSData);

            Iterator impuIt = impi.getImpus().iterator();
            while (impuIt.hasNext()){
                Impu impu = (Impu) impuIt.next();
                if (impu.getNotifyScscfnames().isEmpty() == false){
                    Iterator it = impu.getNotifyScscfnames().iterator();
                    while (it.hasNext()){
                    	NotifyScscfname notifyScscfName = (NotifyScscfname) it.next();
                        Apsvr notifApsvr = (Apsvr) HibernateUtil.getCurrentSession().get(Apsvr.class,
                        		notifyScscfName.getComp_id().getApsvrId());                    	
                        try{
                        	operationsImpl.shNotif(new URI(impu.getSipUrl()), shData, Util.getHost(notifApsvr.getAddress()));
                        }
                        catch (Exception e){
                            LOGGER.warn(this, e);
                        }
                    }
                }
            }
        }

        LOGGER.debug("exiting");
    }
}
