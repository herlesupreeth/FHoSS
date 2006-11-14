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

import org.apache.commons.lang.builder.ToStringBuilder;

import java.beans.PropertyChangeEvent;

import java.io.Serializable;
import java.net.Inet4Address;

import java.util.Set;


/** 
 * This class represents the impi table in the database. Hibernate
 * uses it during transaction of private user identity specific data.
 * @author Hibernate CodeGenerator 
 */
public class Impi extends NotifySupport implements Serializable
{
    /** identifier field */
    private Integer impiId;

    /** persistent field */
    private String impiString;

    /** persistent field */
    private String imsi;

    /** nullable persistent field */
    private String scscfName;
    
    /** nullable persistent field */
    private Inet4Address ip;

    /** nullable persistent field */
    private String skey;

    /** nullable persistent field */
    private String authScheme;

    /** nullable persistent field */
    private String amf;

    /** nullable persistent field */
    private String algorithm;

    /** nullable persistent field */
    private String operatorId;

    /** nullable persistent field */
    private String sqn;

    /** persistent field */
    private de.fhg.fokus.hss.model.Imsu imsu;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Chrginfo chrginfo;

    /** persistent field */
    private Set impus;

    /** persistent field */
    private Set roams;

    /** nullable persistent field */
    private Integer uiccType;

    /** nullable persistent field */
    private Integer keyLifeTime;

    /** flag about changed scscf name **/
    private boolean changeScscfName;

    /** 
     * full constructor 
     * @param impiString URL of impi in String representation
     * @param imsi  ISDN User Part ID
     * @param scscfName Name of the assigned scscf
     * @param skey secret key
     * @param authScheme authentication scheme
     * @param amf authentication management field
     * @param algorithm the preferred algorithm
     * @param operatorId operator Id
     * @param sqn sequence number
     * @param uiccType GUSS specific value, it may be GBA or GBA_U
     * @param keyLifeTime life time of key
     * @param imsu ims subscription
     * @param ip ip address used for early ims
     * @param chrginfo charging information
     * @param impus public identities assigned to this private identity
     * @param roams roaming networks
     */
    public Impi(
        String impiString, String imsi, String scscfName, String skey,
        String authScheme, String amf, String algorithm, String operatorId,
        String sqn, Integer uiccType, Integer keyLifeTime,
        de.fhg.fokus.hss.model.Imsu imsu,Inet4Address ip,
        de.fhg.fokus.hss.model.Chrginfo chrginfo, Set impus, Set roams)
    {
        this.impiString = impiString;
        this.imsi = imsi;
        this.scscfName = scscfName;
        this.skey = skey;
        this.authScheme = authScheme;
        this.amf = amf;
        this.algorithm = algorithm;
        this.operatorId = operatorId;
        this.sqn = sqn;
        this.uiccType = uiccType;
        this.keyLifeTime = keyLifeTime;
        this.imsu = imsu;
        this.ip = ip;
        this.chrginfo = chrginfo;
        this.impus = impus;
        this.roams = roams;
    }

    /** default constructor */
    public Impi()
    {
    }

    /** 
     * minimal constructor 
     * @param impiString URL of impi in String representation
     * @param imsi  ISDN User Part ID
     * @param imsu ims subscription
     * @param impus public identities assigned to this private identity
     * @param roams roaming networks
     */
    public Impi(
        String impiString, String imsi, de.fhg.fokus.hss.model.Imsu imsu,
        Set impus, Set roams)
    {
        this.impiString = impiString;
        this.imsi = imsi;
        this.imsu = imsu;
        this.impus = impus;
        this.roams = roams;
    }


   /**
    * Getter method for impiId
    * @return private identity Id  
    */
    public Integer getImpiId()
    {
        return this.impiId;
    }

   /**
    * Setter method for impiId
    * @param impiId private identity id   
    */
    public void setImpiId(Integer impiId)
    {
        this.impiId = impiId;
    }

   /**
    * Getter method for impiString
    * @return string representation of URL of private identity
    */
    public String getImpiString()
    {
        return this.impiString;
    }

   /**
    * Setter method for impiString
    * @param impiString string representation of URL of private identity 
    */
    public void setImpiString(String impiString)
    {
        this.impiString = impiString;
    }

   /**
    * Getter method for imsi
    * @return ISDN User Part ID 
    */
    public String getImsi()
    {
        return this.imsi;
    }

   /**
    * Setter method for imsi
    * @param imsi ISDN User Part ID  
    */
    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }

   /**
    * Getter method for scscfName
    * @return name of assigned scscf
    */
    public String getScscfName()
    {
        return this.scscfName;
    }

   /**
    * Setter method for scscfName
    * @param scscfName name of scscf 
    */
    public void setScscfName(String scscfName)
    {
        fire("scscfName", this.scscfName, scscfName);
        this.scscfName = scscfName;
    }
 
   /**
    * Getter method for ip
    * @return ip address 
    */    
    public Inet4Address getIP()
    {
    	return this.ip;
    }

   /**
    * Setter method for ip
    * @param ip ip address  
    */    
    public void setIP(Inet4Address ip)
    {
    	this.ip = ip;
    }

   /**
    * Getter method for sKey
    * @return security key
    */
    public String getSkey()
    {
        return this.skey;
    }

   /**
    * Setter method for sKey
    * @param skey security key 
    */
    public void setSkey(String skey)
    {
        this.skey = skey;
    }

   /**
    * Getter method for authScheme
    * @return authentication scheme 
    */
    public String getAuthScheme()
    {
        return this.authScheme;
    }

   /**
    * Setter method for authScheme
    * @param authScheme authentication scheme  
    */
    public void setAuthScheme(String authScheme)
    {
        this.authScheme = authScheme;
    }

   /**
    * Getter method for amf
    * @return authentication management field 
    */
    public String getAmf()
    {
        return this.amf;
    }

   /**
    * Setter method for amf
    * @param amf authentication management field  
    */
    public void setAmf(String amf)
    {
        this.amf = amf;
    }

   /**
    * Getter method for algorithm
    * @return the used algorithm
    */
    public String getAlgorithm()
    {
        return this.algorithm;
    }

   /**
    * Setter method for algorithm
    * @param algorithm the used algorithm  
    */
    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

   /**
    * Getter method for operatorId
    * @return operator Id 
    */
    public String getOperatorId()
    {
        return this.operatorId;
    }

   /**
    * Setter method for operatorId
    * @param operatorId operatorId  
    */
    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

   /**
    * Getter method for sqn
    * @return sequence number
    */
    public String getSqn()
    {
        return this.sqn;
    }

   /**
    * Setter method for sqn
    * @param sqn sequence number  
    */
    public void setSqn(String sqn)
    {
        this.sqn = sqn;
    }

   /**
    * Getter method for imsu
    * @return ims subscription
    */
    public de.fhg.fokus.hss.model.Imsu getImsu()
    {
        return this.imsu;
    }

   /**
    * Setter method for imsu
    * @param imsu ims subscription  
    */
    public void setImsu(de.fhg.fokus.hss.model.Imsu imsu)
    {
        this.imsu = imsu;
    }

   /**
    * Getter method for chrginfo
    * @return charging information 
    */
    public de.fhg.fokus.hss.model.Chrginfo getChrginfo()
    {
        return this.chrginfo;
    }

   /**
    * Setter method for chrginfo
    * @param chrginfo charging information  
    */
    public void setChrginfo(de.fhg.fokus.hss.model.Chrginfo chrginfo)
    {
        this.chrginfo = chrginfo;
    }

   /**
    * Getter method for impus
    * @return public identities
    */
    public Set getImpus()
    {
        return this.impus;
    }

   /**
    * Setter method for impus
    * @param impus public identities  
    */
    public void setImpus(Set impus)
    {
        this.impus = impus;
    }

   /**
    * Getter method for roams
    * @return roaming networks 
    */
    public Set getRoams()
    {
        return this.roams;
    }

   /**
    * Setter method for roams
    * @param roams roaming networks 
    */
    public void setRoams(Set roams)
    {
        this.roams = roams;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("impiId", getImpiId()).toString();
    }

   /**
    * Method to change property
    * @param evt Property change event   
    */
    public void propertyChange(PropertyChangeEvent evt)
    {
        super.propertyChange(evt);

        if (
            (evt.getPropertyName().equals("scscfName"))
                && (evt.getOldValue() != null))
        {
            this.changeScscfName = true;
        }
    }

   /**
    * Getter method for changeScscfName
    * @return a boolean value
    */
    public boolean isChangeScscfName()
    {
        return changeScscfName;
    }

   /**
    * Getter method for keyLifeTime
    * @return key life time
    */
    public Integer getKeyLifeTime()
    {
        return keyLifeTime;
    }

   /**
    * Setter method for keyLifeTime
    * @param keyLifeTime key life time  
    */
    public void setKeyLifeTime(Integer keyLifeTime)
    {
        this.keyLifeTime = keyLifeTime;
    }

   /**
    * Getter method for uiccType
    * @return uiccType
    */
    public Integer getUiccType()
    {
        return uiccType;
    }

   /**
    * Setter method for uiccType
    * @param uiccType UiccType   
    */
    public void setUiccType(Integer uiccType)
    {
        this.uiccType = uiccType;
    }
}
