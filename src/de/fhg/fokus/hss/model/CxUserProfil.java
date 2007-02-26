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
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.cx.ChargingInfoSet;
import de.fhg.fokus.cx.datatypes.ApplicationServer;
import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.cx.datatypes.InitialFilterCriteria;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.datatypes.SIPHeader;
import de.fhg.fokus.cx.datatypes.SPT;
import de.fhg.fokus.cx.datatypes.ServiceProfile;
import de.fhg.fokus.cx.datatypes.SessionDescription;
import de.fhg.fokus.cx.datatypes.TSePoTriChoice;
import de.fhg.fokus.cx.datatypes.TriggerPoint;
import de.fhg.fokus.cx.datatypes.types.TDirectionOfRequest;
import de.fhg.fokus.cx.datatypes.types.TDefaultHandling;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.MissingUserId;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.cx.exceptions.ims.IdentitiesDontMatch;
import de.fhg.fokus.cx.exceptions.ims.UserUnknown;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * The IMS CxUserProfil contains the private identity and assigned public identity.
 * Also it offers method to get the barring status or the roaming allowed status
 * to specific networks (represented by string, e.g. fhg.fokus.de).
 * A given public identity can directly accessed by getter and setter methods.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class CxUserProfil
{
    private static final Logger LOGGER = Logger.getLogger(CxUserProfil.class);

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
     * constructor
     * @param privateUserIdentity Private User Identity
     * @param publicIdentity To PI appropriated Public User Identity
     *
     * @throws IdentitiesDontMatch
     * @throws DiameterException
     */
    public CxUserProfil(URI privateUserIdentity, PublicIdentity publicIdentity)
        throws IdentitiesDontMatch, DiameterException
    {
        LOGGER.debug("entering");
        loadByPrivateIdentity(privateUserIdentity, publicIdentity);
        LOGGER.debug("exiting");
    }

    /**
     * minimal constructor
     * @param publicIdentity To PI appropriated Public User Identity
     *
     * @throws IdentitiesDontMatch
     * @throws DiameterException
     */
    public CxUserProfil(PublicIdentity publicIdentity)
        throws IdentitiesDontMatch, DiameterException
    {
        LOGGER.debug("entering");
        loadByPublicIdentity(publicIdentity);
        LOGGER.debug("exiting");
    }

    /**
     * minimal constructor
     * @param session session
     * @param impu public identity 
     *
     * @throws IdentitiesDontMatch
     * @throws DiameterException
     */
    public CxUserProfil(Impu impu)throws IdentitiesDontMatch, DiameterException
    {
        LOGGER.debug("entering");
        setImpu(impu);
        loadAssignedImpi();
        LOGGER.debug("exiting");
    }

    /**
     * minimal constructor
     * @param _impi private identity 
     *
     * @throws DiameterException
     */
    public CxUserProfil(Impi _impi) throws DiameterException
    {
        LOGGER.debug("entering");

        this.impi = (Impi) HibernateUtil.getCurrentSession().load(Impi.class, _impi.getImpiId());
        impuList = this.impi.getImpus();

        if ((impuList != null) && (impuList.size() > 0))
        {
            this.impu = (Impu) impuList.iterator().next();
        }

        LOGGER.debug("exiting");
    }

    /**
     * loads the necessary data with the help of public identity
     * @param publicIdentity public identity 
     *
     * @throws UserUnknown
     */
    private void loadByPublicIdentity(PublicIdentity publicIdentity)
        throws UserUnknown
    {
        LOGGER.debug("entering");

        Query query = HibernateUtil.getCurrentSession().createQuery(
                "select impu from de.fhg.fokus.hss.model.Impu as impu where impu.sipUrl = ?");
        query.setString(0, publicIdentity.getIdentity());

        List resultList = query.list();

        if (resultList.size() > 0)
        {
            this.impu = (Impu) resultList.get(0);
        }
        else
        {
            throw new UserUnknown();
        }

        loadAssignedImpi();

        LOGGER.debug("exiting");
    }

    /**
     * loads assigned impi
     * @throws UserUnknown
     */
    private void loadAssignedImpi() throws UserUnknown
    {
        try
        {
            if (this.impu.getImpis().isEmpty() == false)
            {
                this.impi = ((Impi) this.impu.getImpis().iterator().next());
                this.impi.addPropertyChangeListener(this.impi);
                this.impuList = impi.getImpus();
            }
        }
        catch (NullPointerException e)
        {
            throw new UserUnknown();
        }
    }

    /**
     * Load Private and Public Identifiers.
     * Check whether the Private and Public Identities belong the same user. If not Experimental-Result-Code
     * shall be set to DIAMETER_ERROR_ID_DONT_MATCH.
     * @param privateUserIdentity
     * @param publicIdentity
     * @return impi ado object
     * @throws IdentitiesDontMatch If private and public identifier belong not to the same user.
     * @throws DiameterException Includes failure cases, like MISSING_USER_ID, or UNABLE_TO_COMPLY on DB-error.
     */
    private void loadByPrivateIdentity(
        URI privateUserIdentity, PublicIdentity publicIdentity)
        throws IdentitiesDontMatch, DiameterException
    {
        LOGGER.debug("entering");

        if (privateUserIdentity == null)
        {
            throw new MissingUserId();
        }

        Impi impi;

        try
        {
            impi =
                (Impi) HibernateUtil.getCurrentSession().createQuery(
                		"select impi from de.fhg.fokus.hss.model.Impi as impi where impi.impiString = ?")
                           .setString(0, privateUserIdentity.getPath())
                           .uniqueResult();

            if (impi == null)
            {
                throw new UserUnknown();
            }

            impi.addPropertyChangeListener(impi);
        }
        catch (HibernateException e)
        {
            LOGGER.error(e);
            throw new UnableToComply();
        }

        this.impi = impi;

        LOGGER.debug("impi loaded");

        if (impi == null)
        {
            
            throw new IdentitiesDontMatch();
        }

        // Get all assignend public user identitys
        if ((impi.getImpus() == null) || (impi.getImpus().size() == 0))
        {
           
            throw new IdentitiesDontMatch();
        }

        this.impuList = impi.getImpus();

        // Lookup for given impu
        Impu[] impus =
            (Impu[]) this.impuList.toArray(new Impu[impi.getImpus().size()]);

        // Check for given public identity
        if (publicIdentity != null)
        {
            // set indactor flag
            boolean unknownUserFlag = true;

            for (int ix = 0; ix < impus.length; ix++)
            {
               
                if (impus[ix].getSipUrl().equals(publicIdentity.getIdentity()))
                {
                    // clear indicator flag
                    unknownUserFlag = false;
                    this.impu = impus[ix];

                    break;
                }
            }

            if (unknownUserFlag == true)
            {
                throw new IdentitiesDontMatch();
            }
        }

        LOGGER.debug("exiting");
    }

    /**
     * Check if the network identifier is assigned by the user.
     * @param visitedNetworkIdentifier String contains the name of the visited network.
     * @return true is roaming is allowed, else false.
     */
    public boolean isRoamingAllowed(String visitedNetworkIdentifier)
    {
        LOGGER.debug("entering");

        boolean roamingAllowed = false;
        Set roams = impi.getRoams();

        if (roams == null)
        {
            roamingAllowed = false;
        }
        else
        {
            Iterator it = roams.iterator();

            while (it.hasNext())
            {
                Network roam = (Network) it.next();

                if (roam.getNetworkString().equals(visitedNetworkIdentifier))
                {
                    roamingAllowed = true;

                    break;
                }
            }
        }

        LOGGER.debug("exiting (" + roamingAllowed + ")");

        return roamingAllowed;
    }

    /**
     * Check if user is barred. User means the request impu or any impi assigned impu.
     * @return true if user is barred from the network, else false;
     */
    public boolean isBarred()
    {
        LOGGER.debug("entering");

        boolean barring = false;

        if (impuList != null)
        {
            Iterator it = impuList.iterator();

            while (it.hasNext())
            {
                Impu next = (Impu) it.next();

                if (next.getBarringIndication().booleanValue() == true)
                {
                    barring = true;
                }
            }
        }
        else
        {
            barring = impu.getBarringIndication();
        }

        LOGGER.debug("exiting");

        return barring;
    }

   /**
    * Help method to check if the user is registered
    * @return true if user is registered else false
    */
    public boolean isRegistered()
    {
        LOGGER.debug("entering");

        if (
            impu.getUserStatus().equals(Impu.USER_STATUS_NOT_REGISTERED) == false)
        {
            return true;
        }
        else
        {
            if (impuList != null)
            {
                Iterator it = impuList.iterator();

                while (it.hasNext())
                {
                    Impu next = (Impu) it.next();

                    if (
                        next.getUserStatus().equals(
                                Impu.USER_STATUS_NOT_REGISTERED) == false)
                    {
                        return true;
                    }
                }
            }
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
            ImpiBO impiBO = new ImpiBO();
            impiBO.saveOrUpdate(impi);
        }

        if (impu != null)
        {
            ImpuBO impuBO = new ImpuBO();
            impuBO.saveOrUpdate(impu);
        }
        LOGGER.debug("exiting");
    }

    public Impi getImpi()
    {
        return impi;
    }

    public void setImpi(Impi impi)
    {
        this.impi = impi;
    }

    public Impu getImpu()
    {
        return impu;
    }

    public void setImpu(Impu impu)
    {
        this.impu = impu;
    }

    public Set getImpuList()
    {
        return impuList;
    }

    public void setImpuList(Set impuList)
    {
        this.impuList = impuList;
    }

    /**
     * it provides the charging info set
     * @return charging info set
     * @throws URISyntaxException
     */
    public ChargingInfoSet getChargingInfoSet()
    {
        ChargingInfoSet chargingInfoSet = null;
        Chrginfo chrginfo = getImpi().getChrginfo();

        try
        {
            if (chrginfo != null)
            {
                if (chrginfo.getPriChrgCollFnName() != null)
                {
                    chargingInfoSet =
                        new ChargingInfoSet(
                            new URI(chrginfo.getPriChrgCollFnName()));

                    if (chrginfo.getSecChrgCollFnName() != null)
                    {
                        chargingInfoSet.setSec_chrg_coll_fn_name(
                            new URI(chrginfo.getSecChrgCollFnName()));
                    }

                    if (chrginfo.getPriEventChrgFnName() != null)
                    {
                        chargingInfoSet.setPri_event_chrg_fn_name(
                            new URI(chrginfo.getPriEventChrgFnName()));
                    }

                    if (chrginfo.getSecEventChrgFnName() != null)
                    {
                        chargingInfoSet.setSec_event_chrg_fn_name(
                            new URI(chrginfo.getSecEventChrgFnName()));
                    }
                }
            }
        }
        catch (URISyntaxException e)
        {
            LOGGER.error(this, e);
        }

        return chargingInfoSet;
    }

    /**
     * Generate a IMSSubscription from Database.
     *
     * @return IMSSubscription Object
     */
    public IMSSubscription getIMSSubscription(String publicUserIdentity)
    {
        IMSSubscription subscription = new IMSSubscription();
        subscription.setPrivateID(getImpi().getImpiString());

        ServiceProfile serviceProfile = null;

        Impu impu = null;
        Iterator it = impuList.iterator(); 
        while (it.hasNext()){
        	impu = (Impu) it.next();
        	if (impu.getSipUrl().equals(publicUserIdentity))
        		break;
        }
        

                if (impu.getSvp() != null)
                {
                    Svp svp = impu.getSvp();
                    Integer key = svp.getSvpId();

                        serviceProfile = new ServiceProfile();

                        if (svp.getIfc2svps() != null)
                        {
                            Iterator itSvp = svp.getIfc2svps().iterator();

                            while (itSvp.hasNext())
                            {
                                InitialFilterCriteria initialFilterCriteria =
                                    new InitialFilterCriteria();

                                Ifc2svp ifc2svp = (Ifc2svp) itSvp.next();
                                Ifc ifc = ifc2svp.getIfc();

                                // set priority
                                initialFilterCriteria.setPriority(
                                    ifc2svp.getPriority());

                                // set application server
                                ApplicationServer applicationServer =
                                    new ApplicationServer();
                                applicationServer.setServerName(
                                    ifc.getApsvr().getAddress());
                                
                                
                                switch (ifc.getApsvr().getDefaultHandling()){
                                	case TDefaultHandling.VALUE_0_TYPE:
                                		applicationServer.setDefaultHandling(TDefaultHandling.VALUE_0);
                                		break;
                                	case TDefaultHandling.VALUE_1_TYPE:
                                		applicationServer.setDefaultHandling(TDefaultHandling.VALUE_1);
                                		break;
                                }
                                
/*                                applicationServer.setDefaultHandling(
                                    (byte) ifc.getApsvr().getDefaultHandling());*/
                                initialFilterCriteria.setApplicationServer(
                                    applicationServer);

                                Trigpt trigpt = ifc.getTrigpt();
                                TriggerPoint triggerPoint =
                                    getTriggerPoint(trigpt);
                                initialFilterCriteria.setTriggerPoint(
                                    triggerPoint);

                                serviceProfile.addInitialFilterCriteria(
                                    initialFilterCriteria);
                            }
                            
                            
                            PublicIdentity publicIdentity = new PublicIdentity();
                            publicIdentity.setIdentity(impu.getSipUrl());
                            publicIdentity.setBarringIndication(
                                impu.getBarringIndication());
                            serviceProfile.addPublicIdentity(publicIdentity);
                        }
                        
                }
        subscription.addServiceProfile(serviceProfile);        
        return subscription;
    }

    /**
     * Create a TriggerPoint-XML-Obj from a Trigpt-Obj.
     * @param trigpt The Trigpt-Obj
     * @return The TriggerPoint-Obj
     */
    public static TriggerPoint getTriggerPoint(Trigpt trigpt)
    {
        // set trigger point
        TriggerPoint triggerPoint = new TriggerPoint();
        triggerPoint.setConditionTypeCNF(trigpt.getCnf() == 1);

        if ((trigpt.getSpts() != null) && (trigpt.getSpts().isEmpty() == false)){
            Iterator itSpt = trigpt.getSpts().iterator();
            TSePoTriChoice choice = null;
            Spt sptData = null;
            SPT spt = null;

            while (itSpt.hasNext()){
                sptData = (Spt) itSpt.next();
                choice = new TSePoTriChoice();
                spt = new SPT();
                spt.setConditionNegated(sptData.isNeg());
                spt.addGroup(sptData.getGroupId());

                switch (sptData.getType()){
                
                	case TrigptBO.TYPE_URI:
                		choice.setRequestURI(sptData.getReqUri());
                		break;

                	case TrigptBO.TYPE_SIP_METHOD:
                		choice.setMethod(sptData.getSipMethod());
                		break;

                	case TrigptBO.TYPE_SESSION_CASE:

                		TDirectionOfRequest dir =
                			TDirectionOfRequest.valueOf(
                					String.valueOf(sptData.getSessionCase()));
                		choice.setSessionCase(dir);
                		break;

                	case TrigptBO.TYPE_SESSION_DESC:
                		SessionDescription sessionDescription =
                			new SessionDescription();
                		sessionDescription.setContent(sptData.getSessionDescContent());
                		sessionDescription.setLine(sptData.getSessionDescLine());
                		choice.setSessionDescription(sessionDescription);
                		break;

                	case TrigptBO.TYPE_SIP_HEADER:
                		SIPHeader header = new SIPHeader();
                		header.setContent(sptData.getSipHeaderContent());
                		header.setHeader(sptData.getSipHeader());
                		choice.setSIPHeader(header);
                		break;
                }

                spt.setTSePoTriChoice(choice);
                triggerPoint.addSPT(spt);
            }
        }

        return triggerPoint;
    }
}
