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

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * This class represents the impu table in the database. Hibernate
 *  uses it during transaction of public user identity specific data.
 * @author Hibernate CodeGenerator 
 */
public class Impu extends NotifySupport implements Serializable
{
    /** an integer value which symbolizes the not_registered status of user */
    public final static String USER_STATUS_NOT_REGISTERED = "0";
    /** an integer value which symbolizes the unregistered status of user */
    public final static String USER_STATUS_UNREGISTERED = "2";
    /** an integer value which symbolizes the registered status of user */
    public final static String USER_STATUS_REGISTERED = "1";

    /** identifier field */
    private Integer impuId;

    /** persistent field */
    private String userStatus;

    /** persistent field */
    private String sipUrl;

    /** persistent field */
    private String telUrl;

    /** persistent field */
    private Boolean barringIndication;

    /** persistent field */
    private boolean psi;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Psi assignedPsi;

    /** persistent field */
    private de.fhg.fokus.hss.model.Svp svp;

    /** persistent field */
    private Set impis;

    /** persistent field */
    private Set notifyImsUserStates;
    /** a field which tells whether a user is deregistered or not*/
    private boolean deregistered = false;
    /** a field which tells whether a users state is changed or not*/
    private boolean changeUserState = false;

    /** persistent field */
    private Set notifyScscfnames;

    /** 
     * full constructor 
     * @param userStatus Status of user
     * @param sipUrl  sip URL of the public identity
     * @param telUrl  telUrl of the public identity
     * @param barringIndication barringIndication
     * @param psi a flag to identify if the public identity 
     *        identifies a public service identity
     * @param assignedPsi assigned public service identity
     * @param svp service profile
     * @param impis public identities
     * @param notifyImsUserStates 
     * @param notifyScscfnames 
     */
    public Impu(
        String userStatus, String sipUrl, String telUrl,
        Boolean barringIndication, boolean psi,
        de.fhg.fokus.hss.model.Psi assignedPsi, de.fhg.fokus.hss.model.Svp svp,
        Set impis, Set notifyImsUserStates, Set notifyScscfnames)
    {
        this.userStatus = userStatus;
        this.sipUrl = sipUrl;
        this.telUrl = telUrl;
        this.barringIndication = barringIndication;
        this.psi = psi;
        this.assignedPsi = assignedPsi;
        this.svp = svp;
        this.impis = impis;
        this.notifyImsUserStates = notifyImsUserStates;
        this.notifyScscfnames = notifyScscfnames;
    }

    /** 
     * minimal constructor 
     * @param userStatus Status of user
     * @param sipUrl  sip URL of the public identity
     * @param telUrl  telUrl of the public identity
     * @param barringIndication barringIndication
     * @param psi a flag to identify if the public identity 
     *        identifies a public service identity
     * @param svp service profile
     * @param impis public identities
     * @param notifyImsUserStates 
     * @param notifyScscfnames 
     */
    public Impu(
        String userStatus, String sipUrl, String telUrl,
        Boolean barringIndication, boolean psi, de.fhg.fokus.hss.model.Svp svp,
        Set impis, Set notifyImsUserStates, Set notifyScscfnames)
    {
        this.userStatus = userStatus;
        this.sipUrl = sipUrl;
        this.telUrl = telUrl;
        this.barringIndication = barringIndication;
        this.psi = psi;
        this.svp = svp;
        this.impis = impis;
        this.notifyImsUserStates = notifyImsUserStates;
        this.notifyScscfnames = notifyScscfnames;
    }

    /** 
     * minimal constructor 
     * @param userStatus Status of user
     * @param sipUrl  sip URL of the public identity
     * @param telUrl  telUrl of the public identity
     * @param barringIndication barringIndication
     * @param impis public identities
     * @param notifyImsUserStates 
     * @param notifyScscfnames 
     * @param assignedPsi assigned public service identity
     * @param psi a flag to identify if the public identity 
     *        identifies a public service identity
     */
    public Impu(
        String userStatus, String sipUrl, String telUrl,
        Boolean barringIndication, Set impis, Set notifyImsUserStates,
        Set notifyScscfnames, boolean psi, Psi assignedPsi)
    {
        this.userStatus = userStatus;
        this.sipUrl = sipUrl;
        this.telUrl = telUrl;
        this.barringIndication = barringIndication;
        this.impis = impis;
        this.notifyImsUserStates = notifyImsUserStates;
        this.notifyScscfnames = notifyScscfnames;
        this.assignedPsi = assignedPsi;
        this.psi = psi;
    }

    /** default constructor */
    public Impu()
    {
    }

   /**
    * Getter method for notifyScscfnames 
    * @return notifyScscfnames
    */
    public Set getNotifyScscfnames()
    {
        return notifyScscfnames;
    }

   /**
    * Setter method for notifyScscfnames
    * @param notifyScscfnames  
    */
    public void setNotifyScscfnames(Set notifyScscfnames)
    {
        this.notifyScscfnames = notifyScscfnames;
    }

   /**
    * Getter method for deregistered
    * @return boolean value for deregistered
    */
    public boolean isDeregistered()
    {
        return deregistered;
    }

   /**
    * Getter method for impuId
    * @return id of public identity
    */
    public Integer getImpuId()
    {
        return this.impuId;
    }

   /**
    * Setter method for impuId
    * @param impuId internal Id of public Identity  
    */
    public void setImpuId(Integer impuId)
    {
        this.impuId = impuId;
    }

   /**
    * Getter method for user status 
    * @return status of user
    */
    public String getUserStatus()
    {
        return this.userStatus;
    }

   /**
    * Setter method for userStatus
    * @param userStatus status of user  
    */
    public void setUserStatus(String userStatus)
    {
        fire("userStatus", this.userStatus, userStatus);
        this.userStatus = userStatus;
    }

   /**
    * Getter method for id sipUrl 
    * @return sip Url of public Identity
    */
    public String getSipUrl()
    {
        return this.sipUrl;
    }

   /**
    * Setter method for sipUrl
    * @param sipUrl sipUrl of public identity 
    */
    public void setSipUrl(String sipUrl)
    {
        fire("sipUrl", this.sipUrl, sipUrl);
        this.sipUrl = sipUrl;
    }

   /**
    * Getter method for telUrl
    * @return telUrl of public identity
    */
    public String getTelUrl()
    {
        return this.telUrl;
    }

   /**
    * Setter method for telUrl
    * @param telUrl telUrl of public identity 
    */
    public void setTelUrl(String telUrl)
    {
        this.telUrl = telUrl;
    }

   /**
    * Getter method for barringIndication 
    * @return true if user is barred else false
    */
    public Boolean getBarringIndication()
    {
        return this.barringIndication;
    }

   /**
    * Setter method for barringIndication
    * @param barringIndication barringIndication 
    */
    public void setBarringIndication(Boolean barringIndication)
    {
        fire("barringIndication", this.barringIndication, barringIndication);
        this.barringIndication = barringIndication;
    }

   /**
    * Getter method for impis
    * @return private indentities
    */
    public Set getImpis()
    {
        return this.impis;
    }

   /**
    * Setter method for impis
    * @param impis private identities 
    */
    public void setImpis(Set impis)
    {
        this.impis = impis;
    }

   /**
    * Getter method for notifyImsUserStatus
    * @return notifyImsUserStatus
    */
    public Set getNotifyImsUserStates()
    {
        return this.notifyImsUserStates;
    }

   /**
    * Setter method for notifyImsUserStatus
    * @param notifyImsUserStates notifyImsUserStatus 
    */
    public void setNotifyImsUserStates(Set notifyImsUserStates)
    {
        this.notifyImsUserStates = notifyImsUserStates;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("impuId", getImpuId()).toString();
    }

   /**
    * Method to change property
    * @param evt Property change event   
    */
    public void propertyChange(PropertyChangeEvent evt)
    {
        super.propertyChange(evt);

        if (evt.getPropertyName().equals("userStatus"))
        {
            changeUserState = true;

            if (
                (evt.getNewValue().equals(USER_STATUS_NOT_REGISTERED))
                    && (evt.getOldValue() != null))
            {
                deregistered = true;
            }
        }
    }

   /**
    * Getter method for changeUserState 
    * @return boolean value of changeUserState
    */
    public boolean isChangeUserState()
    {
        return changeUserState;
    }

   /**
    * Getter method for assignedPsi
    * @return assigned public service identity
    */
    public de.fhg.fokus.hss.model.Psi getAssignedPsi()
    {
        return assignedPsi;
    }

   /**
    * Setter method for assignedPsi
    * @param assignedPsi the assigned public service identity 
    */
    public void setAssignedPsi(de.fhg.fokus.hss.model.Psi assignedPsi)
    {
        this.assignedPsi = assignedPsi;
    }

   /**
    * Getter method for psi 
    * @return the boolean value of psi
    */
    public boolean getPsi()
    {
        return psi;
    }

   /**
    * Setter method for psi
    * @param psi boolean value for psi 
    */
    public void setPsi(boolean psi)
    {
        this.psi = psi;
    }

   /**
    * Getter method for svp 
    * @return service profiles
    */
	public de.fhg.fokus.hss.model.Svp getSvp() {
		return svp;
	}

   /**
    * Setter method for svp
    * @param svp service profile 
    */
	public void setSvp(de.fhg.fokus.hss.model.Svp svp) {
		this.svp = svp;
	}
}
