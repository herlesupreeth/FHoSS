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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.server.sh.ASshOperationsImpl;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.hss.util.Util;
import de.fhg.fokus.sh.data.ApplicationServer;
import de.fhg.fokus.sh.data.InitialFilterCriteria;
import de.fhg.fokus.sh.data.SIPHeader;
import de.fhg.fokus.sh.data.SPT;
import de.fhg.fokus.sh.data.SessionDescription;
import de.fhg.fokus.sh.data.ShData;
import de.fhg.fokus.sh.data.ShIMSData;
import de.fhg.fokus.sh.data.TSePoTriChoice;
import de.fhg.fokus.sh.data.TriggerPoint;
import de.fhg.fokus.sh.data.types.TDirectionOfRequest;


/**
 * This a helping class which assists loading, updating or saving
 * initial filter criteria from or in the database. Further it performs
 * other Cx and Sh specific functions.
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class IfcBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(IfcBO.class);

    /**
     * It creates a new initial filter criteria object
     * @return initial filter criteria 
     */
    public static Ifc create()
    {
        Ifc ifc = new Ifc();

        return ifc;
    }

    /**
     * It loads the initial filter criteria from database with the help of
     * provided primary key
     * @param primaryKey primary key
     * @return initial filter criteria 
     */
    public Ifc load(Serializable primaryKey)
    {
        Ifc ifc = (Ifc) HibernateUtil.getCurrentSession().load(Ifc.class, primaryKey);
        ifc.addPropertyChangeListener(ifc);
        return ifc;
    }

    /**
     * It saves or updates the initial filter criteria provided as argument
     * and performs cx and sh specific functions if necessary
     * @param ifc initial filter criteria 
     */
    public void saveOrUpdate(Ifc ifc)
    {
        HibernateUtil.getCurrentSession().saveOrUpdate(ifc);

        if (ifc.isChange()){
            commitShChanges(ifc);
            commitCxChanges(ifc);
        }
    }

    /**
     * It commits Sh specific changes implied by ifc provided as argument
     * @param ifc initial filter criteria 
     */
    public void commitShChanges(Ifc ifc)
    {
        LOGGER.debug("entering");

        List notifyIfcs = HibernateUtil.getCurrentSession()
                .createQuery(
                "select nifc from de.fhg.fokus.hss.model.NotifyIfc as nifc where nifc.comp_id.ifcApsvrId = ?")
                .setInteger(0, ifc.getApsvr().getApsvrId()).list();

        if ((notifyIfcs != null) && (notifyIfcs.isEmpty() == false))
        {
            ASshOperationsImpl operationsImpl = new ASshOperationsImpl();
            ShData shData = new ShData();
            ShIMSData shIMSData = new ShIMSData();
            addIfc2shIMSData(shIMSData, ifc, 0);
            shData.setShIMSData(shIMSData);

            Iterator it = notifyIfcs.iterator();

            while (it.hasNext()){
                NotifyIfc notifyIfc = (NotifyIfc) it.next();
                Impu impu = notifyIfc.getImpu();
                Apsvr notifApsvr = (Apsvr) HibernateUtil.getCurrentSession().get(Apsvr.class, notifyIfc.getComp_id().getApsvrId());

                try{
                    operationsImpl.shNotif(new URI(impu.getSipUrl()), shData, Util.getHost(notifApsvr.getAddress()));
                }
                catch (URISyntaxException e){
                    LOGGER.warn(IfcBO.class, e);
                }
                catch (de.fhg.fokus.sh.DiameterException e){
                    LOGGER.warn(IfcBO.class, e);
                }
            }
        }

        LOGGER.debug("exiting");
    }

    /**
     * Commit the IFC changes to all registered Cx Listener.
     * @param ifc
     */
    public void commitCxChanges(Ifc ifc)
    {
        LOGGER.debug("entering");

        SvpBO svpBO = new SvpBO();

        // Forward to assigned Service Profiles
        if (ifc.getSvp() != null)
        {
            Iterator it = ifc.getSvp().iterator();
            Svp svp = null;

            while (it.hasNext())
            {
                svp = (Svp) it.next();
                svpBO.commitCxChanges(svp);
            }
        }

        LOGGER.debug("exiting");
    }

    /**
     * Convert the Ifc-Java-Object to IMSData model, represented also by an Java Object with
     * supporting of xml-Marshalling.
     *
     * @param shIMSData XML-Marshable Ifc Data
     * @param ifc Ifc to be presented
     * @param prio Prioritie of current assigned Ifc.
     */
    public static void addIfc2shIMSData(ShIMSData shIMSData, Ifc ifc, int prio)
    {
        InitialFilterCriteria initialFilterCriteria =
            new InitialFilterCriteria();

        // set priority
        initialFilterCriteria.setPriority(prio);

        // set application server
        ApplicationServer applicationServer = new ApplicationServer();
        applicationServer.setServerName(ifc.getApsvr().getAddress());
        initialFilterCriteria.setApplicationServer(applicationServer);

        // set trigger point
        Trigpt trigpt = ifc.getTrigpt();
        TriggerPoint triggerPoint = new TriggerPoint();

        if (trigpt.getSpts() != null)
        {
            Iterator itSpt = trigpt.getSpts().iterator();
            Spt sptData = null;
            TSePoTriChoice choice = null;
            SPT spt = null;

            while (itSpt.hasNext())
            {
                sptData = (Spt) itSpt.next();
                choice = new TSePoTriChoice();
                spt = new SPT();
                spt.addGroup(sptData.getGroupId());
                spt.setConditionNegated(sptData.isNeg());

                switch (sptData.getType())
                {
                case TrigptBO.TYPE_URI:
                    choice.setRequestURI(sptData.getReqUri());

                    break;

                case TrigptBO.TYPE_SIP_METHOD:
                    choice.setMethod(sptData.getSipMethod());

                    break;

                case TrigptBO.TYPE_SESSION_CASE:

                    TDirectionOfRequest directionOfRequest =
                        TDirectionOfRequest.valueOf(
                            String.valueOf(sptData.getSessionCase()));
                    choice.setSessionCase(directionOfRequest);

                    break;

                case TrigptBO.TYPE_SESSION_DESC:

                    SessionDescription sessionDescription =
                        new SessionDescription();
                    sessionDescription.setContent(
                        sptData.getSessionDescContent());
                    sessionDescription.setLine(sptData.getSessionDescLine());
                    choice.setSessionDescription(sessionDescription);

                    break;

                case TrigptBO.TYPE_SIP_HEADER:

                    SIPHeader header = new SIPHeader();
                    header.setHeader(sptData.getSipHeader());
                    header.setContent(sptData.getSipHeaderContent());
                    choice.setSIPHeader(header);

                    break;
                }

                spt.setTSePoTriChoice(choice);
                triggerPoint.addSPT(spt);
            }
        }

        triggerPoint.setConditionTypeCNF(trigpt.getCnf() == 1);
        initialFilterCriteria.setTriggerPoint(triggerPoint);

        shIMSData.addInitialFilterCriteria(initialFilterCriteria);
    }
}
