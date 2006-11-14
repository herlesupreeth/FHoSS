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
package de.fhg.fokus.hss.server.cx.op;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.ChargingInfoSet;
import de.fhg.fokus.cx.CxSCSCFNotificationResponse;
import de.fhg.fokus.cx.datatypes.ApplicationServer;
import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.cx.datatypes.InitialFilterCriteria;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.datatypes.SPT;
import de.fhg.fokus.cx.datatypes.ServiceProfile;
import de.fhg.fokus.cx.datatypes.TSePoTriChoice;
import de.fhg.fokus.cx.datatypes.TriggerPoint;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.cx.exceptions.ims.IdentitiesDontMatch;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.diam.ServerAssignmentTypeAVP;
import de.fhg.fokus.hss.model.Apsvr;
import de.fhg.fokus.hss.model.Impu;
import de.fhg.fokus.hss.model.Psi;
import de.fhg.fokus.hss.model.PsiBO;


/**
 * Operation implementation mapped to the SAR Command.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PullCxOperation extends CxOperation
{
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(PullCxOperation.class);
    /** name of server */    
    private String serverName;
    /** type of server assignment in integer value representation */
    private int serverAssignmentType;
    /** type of user data request in integer value representation */
    int userDataRequestType;
    /** integer value which gives information about user data availability */
    int userDataAlreadyAvailable;

    /**
     * constructor
     * @param _publicIdentity public user identity
     * @param _serverName name of server
     * @param _privateUserIdentity private user identity
     * @param _serverAssignmentType type of server assignment
     * @param _userDataRequestType type of user data request
     * @param _userDataAlreadyAvailable an int value for the availability or 
     *                                  unavailability of user data
     */ 
    public PullCxOperation(
        PublicIdentity _publicIdentity, String _serverName,
        URI _privateUserIdentity, int _serverAssignmentType,
        int _userDataRequestType, int _userDataAlreadyAvailable)
    {
        LOGGER.debug("entering");
        this.publicIdentity = _publicIdentity;
        this.privateUserIdentity = _privateUserIdentity;
        this.serverName = _serverName;
        this.serverAssignmentType = _serverAssignmentType;
        this.userDataAlreadyAvailable = _userDataAlreadyAvailable;
        this.userDataRequestType = _userDataRequestType;
        addPropertyChangeListener(this);
        LOGGER.debug("exiting");
    }

    /**
     * it performs the required opeartion triggered by SAR command
     * @return an object providing SAA specific information 
     * @throws DiameterException
     */	
    public Object execute() throws DiameterException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        CxSCSCFNotificationResponse notificationResponse = null;

        try
        {
            loadUserProfile();
            Impu impu1=getUserProfil().getImpu();
            String impu_status=impu1.getUserStatus();
    	    if(impu_status.equals(impu1.USER_STATUS_NOT_REGISTERED))
                    LOGGER.info("The current status of IMPU "+ publicIdentity.getIdentity() +" is "
                                 +"not registered"); 
            else if(impu_status.equals(impu1.USER_STATUS_REGISTERED))
                    LOGGER.info("The current status of IMPU "+ publicIdentity.getIdentity() +" is "
                                 +"registered");
            else if(impu_status.equals(impu1.USER_STATUS_UNREGISTERED))
                    LOGGER.info("The current status of IMPU "+ publicIdentity.getIdentity() +" is "
                                 +"unregistered");            

            if (getUserProfil().getImpu().getPsi() == true)
            {
                // Look if Public User is PSI
                notificationResponse = handlePSI();
            }
            else
            {
                notificationResponse = handleUserProfil();
            }
        }
        finally
        {
        	if(getUserProfil() != null){
            getUserProfil().closeSession();
        	}
        }

        return notificationResponse;
    }

    /**
     * Handle Pull for a UserProfile
     * @return an object containing SAA specific information
     * @throws DiameterException
     * @throws IdentitiesDontMatch
     */
    private CxSCSCFNotificationResponse handleUserProfil()
        throws IdentitiesDontMatch, DiameterException
    {
        CxSCSCFNotificationResponse notificationResponse = null;

        boolean needUserProfil = false;

        if (serverAssignmentType == ServerAssignmentTypeAVP._UNREGISTERED_USER)
        {
            userProfil.getImpu().setUserStatus(Impu.USER_STATUS_UNREGISTERED);
            userProfil.getImpi().setScscfName(serverName);
            markUpdateUserProfile();
            LOGGER.info("user status of "+userProfil.getImpu().getSipUrl()+" set to unregistered");
            needUserProfil = true;
        }
        else if (
            (serverAssignmentType == ServerAssignmentTypeAVP._REGISTRATION)
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._RE_REGISTRATION
                ))
        {
            userProfil.getImpu().setUserStatus(Impu.USER_STATUS_REGISTERED);
            userProfil.getImpi().setScscfName(serverName);
            markUpdateUserProfile();
            LOGGER.info("user status of "+userProfil.getImpu().getSipUrl()+" set to registered");
            needUserProfil = true;
        }
        else if (
            (
                    serverAssignmentType == ServerAssignmentTypeAVP._TIMEOUT_DEREGISTRATION
                )
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._USER_DEREGISTRATION
                )
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._DEREGISTRATION_TOO_MUCH_DATA
                )
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._ADMINISTRATIVE_DEREGISTRATION
                ))
        {
            if (
                (
                        userProfil.getImpu().getUserStatus().equals(
                            Impu.USER_STATUS_REGISTERED)
                    )
                    || (
                        userProfil.getImpu().getUserStatus().equals(
                            Impu.USER_STATUS_UNREGISTERED)
                    ))
            {
                if (userProfil.getImpu().getImpis().size() == 1)
                {
                    userProfil.getImpu().setUserStatus(
                        Impu.USER_STATUS_NOT_REGISTERED);
                    userProfil.getImpi().setScscfName("");
                    LOGGER.info("user status of "+userProfil.getImpu().getSipUrl()+" set to not registered");
                    markUpdateUserProfile();
                }
                else
                {
                    LOGGER.info(
                        "user status not updated, because impu assigned to multiple impis");
                }

                needUserProfil = false;
            }
        }
        else if (
            (
                    serverAssignmentType == ServerAssignmentTypeAVP._TIMEOUT_DEREGISTRATION_STORE_SERVER_NAME
                )
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._USER_DEREGISTRATION_STORE_SERVER_NAME
                ))
        {
            if (userProfil.getImpu().getImpis().size() == 1)
            {
                userProfil.getImpu().setUserStatus(
                    Impu.USER_STATUS_UNREGISTERED);
                LOGGER.info("user status of "+userProfil.getImpu().getSipUrl()+" set to unregistered");
                markUpdateUserProfile();
            }

            needUserProfil = false;
        }
        else if (serverAssignmentType == ServerAssignmentTypeAVP._NO_ASSIGNMENT)
        {
            if (userProfil.getImpi().getScscfName().equals(serverName))
            {
                needUserProfil = true;
            }
            else
            {
                throw new UnableToComply();
            }
        }
        else if (
            (
                    serverAssignmentType == ServerAssignmentTypeAVP._AUTHENTICATION_FAILURE
                )
                || (
                    serverAssignmentType == ServerAssignmentTypeAVP._AUTHENTICATION_TIMEOUT
                ))
        {
            if (userProfil.getImpu() != null)
            {
                userProfil.getImpu().setUserStatus(
                    Impu.USER_STATUS_NOT_REGISTERED);
                markUpdateUserProfile();
                LOGGER.info("user status of "+userProfil.getImpu().getSipUrl()+" set to not registered");
            }
            else
            {
                Iterator it = userProfil.getImpuList().iterator();

                while (it.hasNext())
                {
                    Impu next = (Impu) it.next();
                    next.setUserStatus(Impu.USER_STATUS_NOT_REGISTERED);
                }

                markUpdateUserProfile();
            }
        }

        notificationResponse =
            new CxSCSCFNotificationResponse(ResultCode._DIAMETER_SUCCESS, true);

        if (needUserProfil == true)
        {
            // Store the private user identity
            try
            {
                notificationResponse.setPrivateUserIdentity(
                    new URI(userProfil.getImpi().getImpiString()));
            }
            catch (URISyntaxException e)
            {
                throw new UnableToComply();
            }

            // Download the user profile and the charging functions
            notificationResponse.setUserProfile(
                userProfil.getIMSSubscription(), userProfil.getChargingInfoSet());
        }

        updateUserProfile();

        return notificationResponse;
    }

   /**
    * handles PSI related tasks for the user
    * @return an object containing SAA specific informations
    */

    private CxSCSCFNotificationResponse handlePSI()
    {
        LOGGER.debug("entering");

        CxSCSCFNotificationResponse notificationResponse = null;

        Psi psi = getUserProfil().getImpu().getAssignedPsi();
        Apsvr apsvr = psi.getPsiTempl().getApsvr();
        notificationResponse =
            new CxSCSCFNotificationResponse(ResultCode._DIAMETER_SUCCESS);

        IMSSubscription subscription = new IMSSubscription();
        subscription.setPrivateID(PsiBO.DEFAULT_PSI_IMPI.getPath());

        ServiceProfile serviceProfile = new ServiceProfile();
        serviceProfile.addPublicIdentity(publicIdentity);

        InitialFilterCriteria ifc = new InitialFilterCriteria();
        ifc.setPriority(0);

        ApplicationServer applicationServer = new ApplicationServer();
        applicationServer.setServerName(apsvr.getAddress());
        applicationServer.setDefaultHandling((byte) apsvr.getDefaultHandling());
        ifc.setApplicationServer(applicationServer);

        TriggerPoint triggerPoint = new TriggerPoint();
        triggerPoint.setConditionTypeCNF(true);

        SPT spt = new SPT();
        spt.addGroup(0);

        TSePoTriChoice choice = new TSePoTriChoice();
        choice.setRequestURI(publicIdentity.getIdentity());
        spt.setTSePoTriChoice(choice);
        triggerPoint.addSPT(spt);
        ifc.setTriggerPoint(triggerPoint);
        serviceProfile.addInitialFilterCriteria(ifc);
        subscription.addServiceProfile(serviceProfile);
        notificationResponse.setPrivateUserIdentity(PsiBO.DEFAULT_PSI_IMPI);

        ChargingInfoSet chargingInfoSet =
            new ChargingInfoSet(PsiBO.DEFAULT_PRI_COLL_CHRG_FN);
        notificationResponse.setUserProfile(subscription, chargingInfoSet);

        LOGGER.debug("exiting");

        return notificationResponse;
    }
}
