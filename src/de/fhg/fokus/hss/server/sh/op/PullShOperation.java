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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.model.Chrginfo;
import de.fhg.fokus.hss.model.Ifc;
import de.fhg.fokus.hss.model.Ifc2svp;
import de.fhg.fokus.hss.model.IfcBO;
import de.fhg.fokus.hss.model.Impu;
import de.fhg.fokus.hss.model.RepData;
import de.fhg.fokus.hss.model.RepDataPK;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.hss.util.InfrastructureException;
import de.fhg.fokus.sh.CurrentLocation;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.InvalidAvpValue;
import de.fhg.fokus.sh.MissingAVP;
import de.fhg.fokus.sh.RequestedData;
import de.fhg.fokus.sh.RequestedDomain;
import de.fhg.fokus.sh.UnableToComply;
import de.fhg.fokus.sh.UserDataCannotBeRead;

import de.fhg.fokus.sh.data.ChargingInformation;
import de.fhg.fokus.sh.data.PublicIdentifiers;
import de.fhg.fokus.sh.data.RepositoryData;
import de.fhg.fokus.sh.data.ServiceData;
import de.fhg.fokus.sh.data.ShData;
import de.fhg.fokus.sh.data.ShIMSData;
import de.fhg.fokus.sh.data.types.TIMSUserState;


/**
 * This class represents the pull operation which is triggered by UDR command
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PullShOperation extends ShOperation{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(PullShOperation.class);
    /** requested data */
    private RequestedData requestedData;
    /** requested domain */
    private RequestedDomain requestedDomain;
    /** current location */
    private CurrentLocation currentLocation;
    /** service indication */
    byte[] serviceIndication;
    /** uri of application server */
    private URI applicationServerName;

	/**
	 * constructor
	 * @param userIdentity public user identity
	 * @param requestedData sh specific requested data
	 * @param requestedDomain requested domain
	 * @param currentLocation current location
	 * @param serviceIndication service indication
	 * @param applicationServerIdentity identity of application server
	 * @param applicationServerName uri of application server
	 */     
    public PullShOperation(URI userIdentity, RequestedData requestedData, RequestedDomain requestedDomain, CurrentLocation currentLocation,
        byte[] serviceIndication, String applicationServerIdentity,URI applicationServerName){
    	
        LOGGER.debug("entering");
        this.requestedData = requestedData;
        this.requestedDomain = requestedDomain;
        this.applicationServerIdentity = applicationServerIdentity;
        this.currentLocation = currentLocation;
        this.publicUserIdentity = userIdentity;
        this.serviceIndication = serviceIndication;
        this.applicationServerName = applicationServerName;
        LOGGER.debug("exiting");
    }


    /**
     * It performs the pull operation triggerd by UDR command. It loads the required
     * data from database and pulls some data from database as indicated by the 
     * requested data value.
     * @return an object containing the pulled data
     * @throws DiameterException
     */
    public Object execute() throws DiameterException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        ShData shData = new ShData();
        
        try{
        	HibernateUtil.beginTransaction();
        	loadApsvr();
        	loadUserProfile();

        	// Check requested data reference
        	switch (requestedData.getValue()){
        		
        		case RequestedData._REPOSITORYDATA:
        			if (asPermList.isPullRepData()){
        				loadRepositoryData(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;

        		case RequestedData._PUBLICIDENTIFIERS:
        			
        			if (asPermList.isPullImpu()){
        				loadPublicIdentifiers(shData);
        			}else{
        				throw new UserDataCannotBeRead();
        			}
        			break;
            
        		case RequestedData._IMSUSERSTATE:
        			
        			if (asPermList.isPullImpuUserState()){
        				loadIMSUserState(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;
            
        		case RequestedData._SCSCFNAME:
        			if (asPermList.isPullScscfName()){
        				loadScscfName(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;

        		case RequestedData._INITIALFILTERCRITERIA:
        			if (asPermList.isPullIfc()){
        				loadIfc(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;
            
        		case RequestedData._LOCATIONINFORMATION:
        			
        			if (asPermList.isPullLocInfo()){
        				loadLocationInformation(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;
            
        		case RequestedData._USERSTATE:

        			if (asPermList.isPullUserState()){
        				loadUserState(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;

        		case RequestedData._CHARGINGINFORMATION:
        			if (asPermList.isPullCharging()){
        				loadChargingInformation(shData);
        			}
        			else{
        				throw new UserDataCannotBeRead();
        			}
        			break;

        		default:
        			throw new InvalidAvpValue("Unknown RequestedData");
        	}
        	
        }
        finally{
        	HibernateUtil.commitTransaction();
        	HibernateUtil.closeSession();
        }
        return shData;
    }


    /**
     * It loads the repository data from HSS
     * @param shData an object of class ShData which is filled with the repository data
     * @throws DiameterException
     */
    private void loadRepositoryData(ShData shData) throws DiameterException{
        LOGGER.debug("entering");

        try{
            if ((serviceIndication == null) || (serviceIndication.length < 1)){
                throw new MissingAVP();
            }

            RepDataPK repDataPK = new RepDataPK();
            repDataPK.setImpuId(userProfil.getImpu().getImpuId());

            String svcIndString = new String(serviceIndication);
            repDataPK.setSvcInd(svcIndString);

            RepData repData = null;
            repData = (RepData) HibernateUtil.getCurrentSession().get(RepData.class, repDataPK);
            if (repData == null)
            {
            	LOGGER.info("Repository data is null!");
            	LOGGER.warn(this, new NullPointerException());
                throw new UnableToComply();
            }

            RepositoryData repositoryData = new RepositoryData();
            repositoryData.setSequenceNumber(repData.getSqn());

            ServiceData serviceData = new ServiceData();
            ByteArrayInputStream bis = new ByteArrayInputStream(repData.getSvcData());
            ObjectInputStream ois = new ObjectInputStream(bis);
            serviceData.setAnyObject(ois.readObject());
            repositoryData.setServiceData(serviceData);
            ois.close();
            repositoryData.setServiceIndication(svcIndString);
            shData.setRepositoryData(repositoryData);
        }
        catch (DiameterException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }

        LOGGER.debug("exiting");
    }

    /**
     * It loads the ims user state from HSS
     * @param shData an object of class ShData which is filled with the ims user state
     */
    private void loadIMSUserState(ShData shData){
        LOGGER.debug("entering");

        ShIMSData shIMSData = new ShIMSData();
        switch (Integer.parseInt(userProfil.getImpu().getUserStatus())){
        
        	case TIMSUserState.VALUE_0_TYPE:
        		shIMSData.setIMSUserState(TIMSUserState.VALUE_0);
        		break;
        	case TIMSUserState.VALUE_1_TYPE:
        		shIMSData.setIMSUserState(TIMSUserState.VALUE_1);
        		break;
        	case TIMSUserState.VALUE_2_TYPE:
        		shIMSData.setIMSUserState(TIMSUserState.VALUE_2);
        		break;
        	case TIMSUserState.VALUE_3_TYPE:
        		shIMSData.setIMSUserState(TIMSUserState.VALUE_3);
        		break;
        }
        
        shData.setShIMSData(shIMSData);
        LOGGER.debug("exiting");
    }

    /**
     * It loads the name of scscf from HSS
     * @param shData an object of class ShData which is filled with scscf name
     */
    private void loadScscfName(ShData shData){
        LOGGER.debug("entering");
        ShIMSData shIMSData = new ShIMSData();
        shIMSData.setSCSCFName(userProfil.getImpi().getScscfName());
        shData.setShIMSData(shIMSData);
        LOGGER.debug("exiting");
    }

    /**
     * It loads the public identifiers from HSS
     * @param shData an object of class ShData which is filled with public identifiers
     */
    private void loadPublicIdentifiers(ShData shData){
        LOGGER.debug("entering");

        PublicIdentifiers publicIdentifiers = new PublicIdentifiers();
        Iterator it = userProfil.getImpuList().iterator();

        while (it.hasNext()){
            Impu impu = (Impu) it.next();
            publicIdentifiers.addIMSPublicIdentity(impu.getSipUrl());
        }

        shData.setPublicIdentifiers(publicIdentifiers);
        LOGGER.debug("exiting");
    }

    /**
     * It loads the location information from HSS
     * @param shData an object of class ShData which is filled with location information
     */
    private void loadLocationInformation(ShData shData){
        LOGGER.debug("entering");
        LOGGER.warn("unsuported feature was called: location of public identity");
        LOGGER.debug("exiting");
    }

    /**
     * It loads the user data from HSS
     * @shData an object of class ShData which is filled with the user state
     */
    private void loadUserState(ShData shData){
        LOGGER.debug("entering");
        LOGGER.warn("unsuported feature was called: user status of public identity");
        LOGGER.debug("exiting");
    }

    /**
     * Load the IFC from user profile
     * @param shData an object filled with appropriate retrieved data from HSS
     * @throws DiameterException
     */
    private void loadIfc(ShData shData) throws DiameterException{
        LOGGER.debug("entering");

        if (applicationServerName == null){
            throw new MissingAVP();
        }

        ShIMSData shIMSData = new ShIMSData();
        if (userProfil.getImpu().getSvp().getIfc2svps() != null){
            Iterator it = userProfil.getImpu().getSvp().getIfc2svps().iterator();
            while (it.hasNext()){
                Ifc2svp ifc2svp = (Ifc2svp) it.next();
                Ifc ifc = ifc2svp.getIfc();
                int prio = ifc2svp.getPriority();
                // Return IFC for the requested Application Server
                if (ifc.getApsvr().getAddress().equals(applicationServerName.getPath())){
                    IfcBO.addIfc2shIMSData(shIMSData, ifc, prio);
                }
            }
        }
        shData.setShIMSData(shIMSData);
        LOGGER.debug("exiting");
    }

    /**
     * Load the charinging function from user profile
     * @param shData an object filled with appropriate retrieved data from HSS
     */
    private void loadChargingInformation(ShData shData){
        LOGGER.debug("entering");
        ShIMSData shIMSData = new ShIMSData();
        Chrginfo chrginfo = userProfil.getImpi().getChrginfo();
        ChargingInformation chargingInformation = new ChargingInformation();
        chargingInformation.setPrimaryChargingCollectionFunctionName(chrginfo.getPriChrgCollFnName());
        chargingInformation.setPrimaryEventChargingFunctionName(chrginfo.getPriEventChrgFnName());
        chargingInformation.setSecondaryChargingCollectionFunctionName(chrginfo.getSecChrgCollFnName());
        chargingInformation.setSecondaryEventChargingFunctionName(chrginfo.getSecEventChrgFnName());
        shIMSData.setChargingInformation(chargingInformation);
        shData.setShIMSData(shIMSData);
        LOGGER.debug("exiting");
    }
}
