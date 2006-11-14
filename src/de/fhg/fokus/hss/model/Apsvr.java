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

import java.io.Serializable;


/** 
 *  This class represents the application server table for hibernate. Hibernate
 *  uses it during transaction of application server specific data.
 *  
 *  @author Hibernate CodeGenerator 
 * 
 **/
public class Apsvr extends NotifySupport implements Serializable
{
    /** identifier field */
    private Integer apsvrId;

    /** persistent field */
    private String name;

    /** persistent field */ 
    private String address;

    /** persistent field */
    private int defaultHandling;

    /** full constructor 
     *  @param apsvrId Id of application Server
     *  @param name name of application Server
     *  @param address address of application Server
     *  @param defaultHandling an integer which is mapped to defaulthandling
     */
    public Apsvr(
        Integer apsvrId, String name, String address, int defaultHandling)
    {
        this.apsvrId = apsvrId;
        this.name = name;
        this.address = address;
        this.defaultHandling = defaultHandling;
    }

    /** default constructor */
    public Apsvr()
    {
    }

   /**
    * Get method for id of application server 
    * @return id of application server
    */
    public Integer getApsvrId()
    {
        return this.apsvrId;
    }

   /**
    * Set method for id of application server
    * @param apsvrId id of application server
    */
    public void setApsvrId(Integer apsvrId)
    {
        this.apsvrId = apsvrId;
    }

   /**
    * Get method for name of application server 
    * @return name of application server
    */
    public String getName()
    {
        return this.name;
    }

   /**
    * Set method for name of application server
    * @param name name of application server
    */
    public void setName(String name)
    {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

   /**
    * Get method for address of application server 
    * @return address of application server
    */
    public String getAddress()
    {
        return this.address;
    }

   /**
    * Set method for address of application server
    * @param address address of application server
    */
    public void setAddress(String address)
    {
        String oldAdress = this.address;
        this.address = address;
        changeSupport.firePropertyChange("address", oldAdress, address);
    }

   /**
    * Get method for default handling of application server 
    * @return default handling of application server
    */
    public int getDefaultHandling()
    {
        return this.defaultHandling;
    }

   /**
    * Set method for default handling of application server
    * @param defaultHandling default handling of application server
    */
    public void setDefaultHandling(int defaultHandling)
    {
        int oldDefaultHandling = this.defaultHandling;
        this.defaultHandling = defaultHandling;
        changeSupport.firePropertyChange(
            "defaultHandling", oldDefaultHandling, defaultHandling);
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("apsvrId", getApsvrId())
                                        .toString();
    }
}
