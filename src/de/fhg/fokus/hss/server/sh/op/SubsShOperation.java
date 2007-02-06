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
package de.fhg.fokus.hss.server.sh.op;

import java.net.URI;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.model.Apsvr;
import de.fhg.fokus.hss.model.NotifyIfc;
import de.fhg.fokus.hss.model.NotifyIfcPK;
import de.fhg.fokus.hss.model.NotifyImsUserState;
import de.fhg.fokus.hss.model.NotifyImsUserStatePK;
import de.fhg.fokus.hss.model.NotifyRepData;
import de.fhg.fokus.hss.model.NotifyRepDataPK;
import de.fhg.fokus.hss.model.NotifyScscfname;
import de.fhg.fokus.hss.model.NotifyScscfnamePK;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.InvalidAvpValue;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.SubscriptionRequestType;
import de.fhg.fokus.sh.UnableToComply;
import de.fhg.fokus.sh.UserDataCannotBeNotified;


/**
 * This class represents the sh-operation which is triggered by SNR command
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class SubsShOperation extends ShOperation
{
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(SubsShOperation.class);
    /** requested data */    
    private RequestedData requestedData;
    /** service indication */
    byte[] serviceIndication;
    /** type of subscription request */
    private SubscriptionRequestType subscriptionRequestType;
    /** application server uri */
    private URI applicationServerName;


	/**
	 * constructor
	 * @param userIdentity public user identity
	 * @param requestedData sh specific requested data
	 * @param subscriptionRequestType type of subscription request
	 * @param serviceIndication service indication
	 * @param applicationServerIdentity identity of application server
	 * @param applicationServerName uri of application server
	 */
    public SubsShOperation(URI userIdentity, RequestedData requestedData, SubscriptionRequestType subscriptionRequestType,
        byte[] serviceIndication, String applicationServerIdentity, URI applicationServerName){
    	
        LOGGER.debug("entering");
        this.requestedData = requestedData;
        this.applicationServerIdentity = applicationServerIdentity;
        this.publicUserIdentity = userIdentity;
        this.subscriptionRequestType = subscriptionRequestType;
        this.applicationServerName = applicationServerName;
        this.serviceIndication = serviceIndication;
        LOGGER.debug("exiting");
    }

    /**
     * It performs the appropriate operation triggerd by SNR command. It loads the required
     * data from database and notifies the database as indicated by the 
     * requested data value.
     * @return null 
     * @throws DiameterException
     */
    public Object execute() throws DiameterException{
    	
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        try{
        	HibernateUtil.beginTransaction();
        	loadApsvr();
        	loadUserProfile();

        	switch (requestedData.getValue()){

        		case RequestedData._REPOSITORYDATA:
        			if (asPermList.isSubRepData()){
        				subRepositoryData();
        			}
        			else{
        				throw new UserDataCannotBeNotified();
        			}
        			break;

        		case RequestedData._IMSUSERSTATE:
        			if (asPermList.isSubImpuUserState()){
        				subImsUserState();
        			}
        			else{
        				throw new UserDataCannotBeNotified();
        			}
        			break;

        		case RequestedData._SCSCFNAME:

        			if (asPermList.isSubImpuUserState()){
        				subScscfName();
        			}
        			else{
        				throw new UserDataCannotBeNotified();
        			}
        			break;

        		case RequestedData._INITIALFILTERCRITERIA:
        			if (asPermList.isSubIfc()){
        				subIfc();
        			}
        			else{
        				throw new UserDataCannotBeNotified();
        			}
        			break;
        			
        			// to do: not all values are supported here...
        		default:
        			throw new InvalidAvpValue();
        	}
        }
        finally{
        	HibernateUtil.commitTransaction();
        	HibernateUtil.closeSession();
        }

        LOGGER.debug("exiting");
        return null;
    }

    /**
     * It subscribes or unsubscribes the IFC notification depending on the value
     * of subscriptionRequestType
     * @throws DiameterException
     */
    private void subIfc() throws DiameterException
    {
        LOGGER.debug("entering");

        Apsvr ifcApsvr = (Apsvr) HibernateUtil.getCurrentSession()
        	.createQuery("select apsvr from de.fhg.fokus.hss.model.Apsvr as apsvr where apsvr.name = ?")
        	.setString(0, applicationServerName.getPath())
            .uniqueResult();

        if (ifcApsvr == null){
            LOGGER.warn(this, new NullPointerException("Unknown application server requested for Ifc-AppSrv-Notif."));
            throw new UnableToComply();
        }

        NotifyIfcPK primaryKey = new NotifyIfcPK();
        primaryKey.setApsvrId(apsvr.getApsvrId());
        primaryKey.setImpuId(userProfil.getImpu().getImpuId());
        primaryKey.setIfcApsvrId(ifcApsvr.getApsvrId());

        NotifyIfc notifyIfc = (NotifyIfc) HibernateUtil.getCurrentSession().get(NotifyIfc.class, primaryKey);

        if (subscriptionRequestType.getValue() == SubscriptionRequestType._SUBSCRIBE){
            // Subscribe
            if (notifyIfc == null){
                notifyIfc = new NotifyIfc(primaryKey);
                HibernateUtil.getCurrentSession().save(notifyIfc);
            }
        }
        else{
            // Un-Subscribe
            if (notifyIfc != null){
                HibernateUtil.getCurrentSession().delete(notifyIfc);
            }
        }
        LOGGER.debug("exiting");
    }

    /**
     * It subscribes or unsubscribes the SCSCF notification depending on the value
     * of subscriptionRequestType
     */
    private void subScscfName(){
        LOGGER.debug("entering");

        Session session = HibernateUtil.getCurrentSession();
        NotifyScscfnamePK primaryKey = new NotifyScscfnamePK();
        primaryKey.setApsvrId(apsvr.getApsvrId());
        primaryKey.setImpuId(userProfil.getImpu().getImpuId());

        NotifyScscfname notifyScscfname = (NotifyScscfname) session.get(NotifyScscfname.class, primaryKey);

        if (subscriptionRequestType.getValue() == SubscriptionRequestType._SUBSCRIBE){
            // Subscribe
            if (notifyScscfname == null){
                notifyScscfname = new NotifyScscfname(primaryKey);
                HibernateUtil.getCurrentSession().save(notifyScscfname);
            }
        }
        else{
            // Un-Subscribe
            if (notifyScscfname != null){
                HibernateUtil.getCurrentSession().delete(notifyScscfname);
            }
        }
        LOGGER.debug("exiting");
    }

    /**
     * It subscribes or unsubscribes the ims user state notification depending 
     * on the value of subscriptionRequestType
     */
    private void subImsUserState(){
        LOGGER.debug("entering");

        Session session = HibernateUtil.getCurrentSession();
        NotifyImsUserStatePK primaryKey = new NotifyImsUserStatePK();
        primaryKey.setApsvrId(apsvr.getApsvrId());
        primaryKey.setImpuId(userProfil.getImpu().getImpuId());

        NotifyImsUserState notifyImsUserState = (NotifyImsUserState) session.get(NotifyImsUserState.class, primaryKey);

        if (subscriptionRequestType.getValue() == SubscriptionRequestType._SUBSCRIBE){
            // Subscribe
            if (notifyImsUserState == null){
                notifyImsUserState = new NotifyImsUserState(primaryKey);
                HibernateUtil.getCurrentSession().save(notifyImsUserState);
            }
        }
        else{
            // Un-Subscribe
            if (notifyImsUserState != null){
                HibernateUtil.getCurrentSession().delete(notifyImsUserState);
            }
        }

        LOGGER.debug("exiting");
    }

    /**
     * It subscribes or unsubscribes the repository data notification 
     * depending on the value of subscriptionRequestType
     */
    private void subRepositoryData(){
        LOGGER.debug("entering");
        Session session = HibernateUtil.getCurrentSession();

        //	Check for exiting Notify
        NotifyRepDataPK primaryKey = new NotifyRepDataPK();
        primaryKey.setImpuId(userProfil.getImpu().getImpuId());
        primaryKey.setSvcInd(new String(serviceIndication));
        primaryKey.setApsvrId(apsvr.getApsvrId());

        NotifyRepData notifyRepData = (NotifyRepData) session.get(NotifyRepData.class, primaryKey);

        if (subscriptionRequestType.getValue() == SubscriptionRequestType._SUBSCRIBE){
            // Subscribe
            if (notifyRepData == null){
                notifyRepData = new NotifyRepData(primaryKey);
                HibernateUtil.getCurrentSession().save(notifyRepData);
            }
        }
        else{
            // Un-Subscribe
            if (notifyRepData != null){
                HibernateUtil.getCurrentSession().delete(notifyRepData);
            }
        }

        LOGGER.debug("exiting");
    }
}
