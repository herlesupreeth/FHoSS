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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.cx.exceptions.ims.IdentitiesDontMatch;
import de.fhg.fokus.hss.model.Apsvr;
import de.fhg.fokus.hss.model.AsPermList;
import de.fhg.fokus.hss.model.ShUserProfil;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.sh.DiameterException;
import de.fhg.fokus.sh.MissingUserId;
import de.fhg.fokus.sh.UnableToComply;
import de.fhg.fokus.sh.UserDataCannotBeRead;


/**
 * This abstarct class represents Sh-specific operations
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class ShOperation
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ShOperation.class);
    /** user profile */
    protected ShUserProfil userProfil = null;
    /** a flag indicating whether update is required or not */
    protected boolean needUpdate = false;
    /** public user identity */
    protected URI publicUserIdentity;
    /** application server identity */
    protected String applicationServerIdentity;
    /** application server permission list */
    protected AsPermList asPermList;
    /** application server */
    protected Apsvr apsvr;

    /**
     *  This is string converter
     *  @return the string
     */
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * It performs the apprpriate operation and returns an object containing
     * the results of the operation
     * @return an object containing the result
     * @throws DiameterException
     */
    public abstract Object execute() throws DiameterException;

    /**
     * Load User Profile from persitenz.
     * @throws IdentitiesDontMatch
     * @throws DiameterException
     */
    protected void loadUserProfile() throws DiameterException{
        LOGGER.debug("entering");

        if (publicUserIdentity != null){
            userProfil = new ShUserProfil(publicUserIdentity);
            LOGGER.debug("exititng");
            return;
        }

        LOGGER.debug("exititng");
        throw new MissingUserId();
    }

    /**
     * Markup user profil update flag.
     *
     */
    protected void markUpdateUserProfile(){
        needUpdate = true;
    }

    /**
     * Update user profil if flag is set to true.
     *
     */
    protected void updateUserProfile(){
        if (needUpdate == true){
            userProfil.update();
        }
    }

    /**
     * Load application server and it's permission list
     * @throws DiameterException
     */
    protected void loadApsvr() throws DiameterException
    {
        apsvr = (Apsvr) HibernateUtil.getCurrentSession()
        	.createQuery("select apsvr from de.fhg.fokus.hss.model.Apsvr as apsvr where apsvr.address like ?")
            .setString(0, "%" + applicationServerIdentity + "%")
            .uniqueResult();
        
        if (apsvr == null){
            LOGGER.warn(this,new NullPointerException("Unknown application server: " + applicationServerIdentity));
            throw new UnableToComply();
        }

        asPermList = (AsPermList) HibernateUtil.getCurrentSession().get(AsPermList.class, apsvr.getApsvrId());
        
        if (asPermList == null){
            throw new UserDataCannotBeRead();
        }
    }
}
