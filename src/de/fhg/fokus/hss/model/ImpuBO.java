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

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.cx.DeregistrationReason;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.hss.server.cx.op.DeregisterCxOperation;
import de.fhg.fokus.hss.server.cx.op.UpdateCxOperation;
import de.fhg.fokus.hss.server.sh.ASshOperationsImpl;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.hss.util.Util;
import de.fhg.fokus.sh.data.ShData;
import de.fhg.fokus.sh.data.ShIMSData;
import de.fhg.fokus.sh.data.types.TCSUserState;
import de.fhg.fokus.sh.data.types.TIMSUserState;

/**
 * This class creates loads saves and updates the public identity.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpuBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ImpuBO.class);
    /**
     * It creates public identity
     * @return public identity
     */
    public static Impu create()
    {
        Impu impu = new Impu();
        impu.setUserStatus(Impu.USER_STATUS_NOT_REGISTERED);
        return impu;
    }

    /**
     * It loads the public identity from database with the help of
     * provided primary key
     * @param primaryKey primary key
     * @return public identity 
     */
    public Impu load(Serializable primaryKey)
    {
        LOGGER.debug("entering");

        Impu impu = (Impu) HibernateUtil.getCurrentSession().load(Impu.class, primaryKey);
        impu.addPropertyChangeListener(impu);
        LOGGER.debug("exiting");

        return impu;
    }

    /**
     * It saves or updates the public identity provided as argument 
     * and performs cx and sh specific functions if necessary
     * @param impu public identity 
     */
    public void saveOrUpdate(Impu impu)
    {
        LOGGER.debug("entering");

        HibernateUtil.getCurrentSession().saveOrUpdate(impu);

        if (impu.isChange() && (impu.isChangedSVP() || impu.isChangedBarring())
                && (impu.getUserStatus().equals(Impu.USER_STATUS_NOT_REGISTERED) == false)){
        	commitCxChanges(impu);
        }
        else if (impu.isDeregistered())
        {
            commitCxDeregister(impu);
        }

        if(impu.isChangeUserState()){
        	commitShChanges(impu);
        }
        
        LOGGER.debug("exiting");
    }

    /**
     * It commits Sh specific changes implied by impu provided as argument
     * @param impu public identity
     */
    private void commitShChanges(Impu impu) {
			LOGGER.debug("entering");
			if((impu.getNotifyImsUserStates() != null)&&(impu.getNotifyImsUserStates().isEmpty() == false)){
				Iterator it = impu.getNotifyImsUserStates().iterator();
				ASshOperationsImpl operationsImpl = new ASshOperationsImpl();

	            ShData shData = new ShData();
	            ShIMSData shIMSData = new ShIMSData();
	            
	            switch (Integer.parseInt(impu.getUserStatus())){
	            
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
				//shData.setCSUserState((byte)Integer.parseInt(impu.getUserStatus()));
				while(it.hasNext()){
					NotifyImsUserState notifyImsUserState = (NotifyImsUserState) it.next();
					Apsvr notifApsvr = (Apsvr) HibernateUtil.getCurrentSession()
						.get(Apsvr.class, notifyImsUserState.getComp_id().getApsvrId());
					
					try {
						operationsImpl.shNotif(new URI(impu.getSipUrl()), shData, Util.getHost(notifApsvr.getAddress()));
					} 
					catch (Exception e) {
						LOGGER.warn(this, e);
					}					
				}
				
			}
			LOGGER.debug("exiting");
			
		}


    /**
     * It commits Cx specific changes implied by impi provided as argument
     * @param impu public identity
     */
	private void commitCxChanges(Impu impu)
    {
        LOGGER.debug("entering");

        try
        {
            if ((impu.getImpis() != null)&&(impu.getImpis().isEmpty() == false))
            {
                PublicIdentity pi = new PublicIdentity();
                pi.setIdentity(impu.getSipUrl());

                CxUserProfil cxUserProfil = new CxUserProfil(pi);
                String privateIdentity = cxUserProfil.getImpi().getImpiString();
                String publicIdentity =
                	cxUserProfil.getImpu().getSipUrl();
                UpdateCxOperation cxOperation =
                    new UpdateCxOperation(cxUserProfil, privateIdentity, publicIdentity);
                cxOperation.execute();
            }
        }
        catch (DiameterException e)
        {
            LOGGER.error(impu, e);
        }

        LOGGER.debug("exiting");
    }

    /**
     * Listen for de/not_registered user status
     * @param impu public identity
     */
    private void commitCxDeregister(Impu impu)
    {
        try{
            DeregistrationReason reason = new DeregistrationReason(0);
            reason.setDeregistrationInfo("Registration was canceled by HSS.");

            if (impu.getImpis().iterator().hasNext()){
                Impi impi = (Impi) impu.getImpis().iterator().next();
                CxUserProfil cxUserProfil = new CxUserProfil(impi);
                DeregisterCxOperation cxOperation = new DeregisterCxOperation(cxUserProfil, reason);
                cxOperation.execute();
            }
        }
        catch (DiameterException e){
            LOGGER.error(this, e);
        }
    }

    /**
     * Do link or unlink impu to impi
     * @param impiPk primary key of private identity
     * @param impuPk primary key of public identity
     * @param isLink indicator of link or unlink
     */
    public void linkImpu2Impi(Integer impiPk, Integer impuPk, boolean isLink)
    {
        Session session = HibernateUtil.getCurrentSession();

        Impi impi = (Impi) session.load(Impi.class, impiPk, LockMode.UPGRADE);
        Impu impu = (Impu) session.load(Impu.class, impuPk);

        if (isLink){
            LOGGER.debug("add impu to impi");
            impi.getImpus().add(impu);
        }
        else{
            LOGGER.debug("remove impu from impi");
            impi.getImpus().remove(impu);
            impu.getImpis().remove(impi);
        }

        session.saveOrUpdate(impi);
    }
}
