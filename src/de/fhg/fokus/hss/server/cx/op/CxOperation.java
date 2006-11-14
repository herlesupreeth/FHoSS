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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.BaseAuthorizationRejected;
import de.fhg.fokus.cx.exceptions.base.MissingUserId;
import de.fhg.fokus.cx.exceptions.ims.IdentitiesDontMatch;
import de.fhg.fokus.hss.model.CxUserProfil;


/**
 * This abstract class represents the cx specific operations
 * @author Andre Charton  (dev -at- open-ims dot org)
 */
public abstract class CxOperation implements PropertyChangeListener
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(CxOperation.class);
    /** cx specific user profile */
    protected CxUserProfil userProfil = null;
    /** a boolean value indicating if a update is required or not */
    protected boolean needUpdate = false;
    /** public user identity */
    protected PublicIdentity publicIdentity = null;
    /** private user identity */
    protected URI privateUserIdentity = null;
    /** support for property change */
    PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    /**
     * It performs cx interface specific update 
     * @param evt property change event
     */
    
    public void propertyChange(PropertyChangeEvent evt)
    {
        LOGGER.debug("entering");

        try
        {
            UpdateCxOperation cxOperation =
                new UpdateCxOperation(getUserProfil(), privateUserIdentity);
            cxOperation.execute();
        }
        catch (DiameterException e)
        {
            LOGGER.error(this, e);
        }

        LOGGER.debug("exiting");
    }

    /**
     * It adds property change listener
     * @param l property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.addPropertyChangeListener(l);
    }

    /**
     * It removes property change listener
     * @param l property change listener
     */
    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        changeSupport.removePropertyChangeListener(l);
    }

    /**
     * It is string converter
     * @return converted string
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(
            this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * An anstarct method which executes certain cx operations
     * @return an object containing the result of the performed operations
     * @throws DiameterException
     */
    public abstract Object execute() throws DiameterException;

    /**
     * Load User Profile from database and check the barring indication.
     * @throws IdentitiesDontMatch
     * @throws DiameterException
     */
    protected void loadUserProfile()
        throws IdentitiesDontMatch, DiameterException
    {
        if ((privateUserIdentity != null) && (publicIdentity != null))
        {
            userProfil = new CxUserProfil(privateUserIdentity, publicIdentity);
        }
        else if (publicIdentity != null)
        {
            userProfil = new CxUserProfil(publicIdentity);
        }
        else
        {
            throw new MissingUserId();
        }

        // Check whether the public identity received in the request 
        // is barred for the establishment of multimedia sessions.
        if (getUserProfil().isBarred() == true)
        {
            throw new BaseAuthorizationRejected();
        }
    }

    /**
     * Markup user profil update flag.
     *
     */
    protected void markUpdateUserProfile()
    {
        needUpdate = true;
    }

    /**
     * Update user profil if flag is set to true.
     *
     */
    protected void updateUserProfile()
    {
        if (needUpdate == true)
        {
            userProfil.update();
        }
    }

   /**
    * Getter method for user profile
    * @return the user profile
    */
    public CxUserProfil getUserProfil()
    {
        return userProfil;
    }

   /**
    * Setter method for user profile
    * @param userProfil the user profile
    */
    public void setUserProfil(CxUserProfil userProfil)
    {
        this.userProfil = userProfil;
    }
}
