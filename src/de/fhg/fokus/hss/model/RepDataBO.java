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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.server.sh.ASshOperationsImpl;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.TransparentDataOutOfSync;
import de.fhg.fokus.sh.UnableToComply;
import de.fhg.fokus.sh.data.RepositoryData;
import de.fhg.fokus.sh.data.ServiceData;
import de.fhg.fokus.sh.data.ShData;


/**
 * Contains all bussiness methods for repository data, like update or notify.
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class RepDataBO extends HssBO
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(RepDataBO.class);
    /** sh specific repository data */
    private ShData shData;
    /** application server */
    private Apsvr apsvr;
    /** Sh specific user profile */
    private ShUserProfil userProfil;

    /**
     * constructor
     * @param _shData Repository Data
     * @param _apsvr Application Server
     * @param _userProfil User Profil contained assigned user
     */
    public RepDataBO(ShData _shData, Apsvr _apsvr, ShUserProfil _userProfil)
    {
        LOGGER.debug("entering");
        this.shData = _shData;
        this.apsvr = _apsvr;
        this.userProfil = _userProfil;
        LOGGER.debug("exiting");
    }

    /**
     * It updates the repository data
     *
     * @throws DiameterException
     */
    public void updateRepData() throws DiameterException
    {
        LOGGER.debug("entering");

        try
        {
            RepositoryData repositoryData = shData.getRepositoryData();
            int sqn = repositoryData.getSequenceNumber() % 65535;
            String serviceInd = repositoryData.getServiceIndication();
            ServiceData serviceData = repositoryData.getServiceData();
            byte[] bufRepData = null;

            if (serviceData != null){
                Object serviceDataObj = serviceData.getAnyObject();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(serviceDataObj);
                out.close();

                // Get the bytes of the serialized object
                bufRepData = bos.toByteArray();
            }

            RepDataPK repDataPK = new RepDataPK();
            repDataPK.setImpuId(userProfil.getImpu().getImpuId());
            repDataPK.setSvcInd(serviceInd);

            RepData repData = null;
            repData = (RepData) getSession().get(RepData.class, repDataPK);

            if ((repData == null) && (bufRepData != null)){
                // Create entry
                LOGGER.debug("create");

                if (sqn == 0){
                    beginnTx();
                    repData = new RepData();
                    repData.setComp_id(repDataPK);
                    repData.setSqn(new Integer(0));
                    repData.setSvcData(bufRepData);
                    
                    getSession().save(repData);
                    endTx();
                }
                else{
                	throw new TransparentDataOutOfSync();
                }
            }
            else if (repData != null && (bufRepData != null)){
            	// Update entry
                LOGGER.debug("update");

                if ((sqn - 1) == repData.getSqn().intValue()){
                	repData.setSvcData(bufRepData);
                	repData.setSqn(sqn);
                	beginnTx();
                	getSession().update(repData);
                	endTx();
                	commitShChanges(repData);
                }
                else{
                        LOGGER.warn(
                            "Repository-Data out of Sync! SH-SQN-1: " + (sqn - 1)
                            + " versus HSS-SQN: " + repData.getSqn().intValue());
                        throw new TransparentDataOutOfSync();
               }
            }
             else if (repData != null && bufRepData == null){
            	 LOGGER.debug("delete");
                 commitShChanges(repData);
                 
                 // Delete
                 beginnTx();
                 getSession().delete(repData);
                 endTx();
                 
            }
             else{
            	 // repository data is already null
            	 throw new UnableToComply();
             }
            closeSession();
        }
        catch (IOException e){
            LOGGER.error(this, e);
            throw new UnableToComply();
        }

        LOGGER.debug("exiting");
    }

    /**
     * Commit changes to sh interface.
     * @param repData
     */
    private void commitShChanges(RepData repData)
    {
        LOGGER.debug("entering");

        //BUG, the repData is null after some time... NULLPointerException is sent!!!         
        LOGGER.error("Notify: " + repData.getNotifyRepDatas());

        if (repData.getNotifyRepDatas().isEmpty() == false)
        {
            Iterator it = repData.getNotifyRepDatas().iterator();
            ASshOperationsImpl operationsImpl = new ASshOperationsImpl();

            while (it.hasNext())
            {
                NotifyRepData notifyRepData = (NotifyRepData) it.next();
                Apsvr notifApsvr = (Apsvr) getSession().get(Apsvr.class, notifyRepData.getComp_id().getApsvrId());

                try{
                    operationsImpl.shNotif(userProfil.getUri(), shData, notifApsvr.getName());
                }
                catch (DiameterException e){
                    LOGGER.error(this, e);
                }
            }
        }

        LOGGER.debug("exiting");
    }
}
