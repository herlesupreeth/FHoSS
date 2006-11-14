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

import java.util.Set;


/** 
 * This class represents the ifc table in the database. Hibernate
 * uses it during transaction of initial filter criteria specific data.
 * @author Hibernate CodeGenerator 
 */
public class Ifc extends NotifySupport implements Serializable
{
    /** identifier field */
    private Integer ifcId;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Apsvr apsvr;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Trigpt trigpt;

    /** persistent field */
    private Set svp;

    /** 
     * full constructor 
     * @param name Name of initial filter criteria
     * @param apsvr application server
     * @param trigpt point
     * @param svp service profiles
     */
    public Ifc(
        String name, de.fhg.fokus.hss.model.Apsvr apsvr,
        de.fhg.fokus.hss.model.Trigpt trigpt, Set svp)
    {
        super();
        this.name = name;
        this.apsvr = apsvr;
        this.trigpt = trigpt;
        this.svp = svp;
    }

    /** default constructor */
    public Ifc()
    {
        super();
    }

    /** 
     * minimal constructor 
     * @param name Name of initial filter criteria
     * @param svp service profiles
     */
    public Ifc(String name, Set svp)
    {
        super();
        this.name = name;
        this.svp = svp;
    }

   /**
    * Getter method for ifcId
    * @return initial filter criteria Id 
    */
    public Integer getIfcId()
    {
        return this.ifcId;
    }

   /**
    * Setter method for ifcId
    * @param ifcId inital filter criteria Id
    */
    public void setIfcId(Integer ifcId)
    {
        this.ifcId = ifcId;
    }

   /**
    * Getter method for name
    * @return name of initial filter criteria
    */
    public String getName()
    {
        return this.name;
    }

   /**
    * Setter method for name
    * @param name name of initial filter criteria
    */
    public void setName(String name)
    {
        fire("name", this.name, name);
        this.name = name;
    }

   /**
    * Getter method for apsvr
    * @return application server
    */
    public de.fhg.fokus.hss.model.Apsvr getApsvr()
    {
        return this.apsvr;
    }

   /**
    * Setter method for apsvr
    * @param apsvr application server
    */
    public void setApsvr(de.fhg.fokus.hss.model.Apsvr apsvr)
    {
        Apsvr oldApsvr = this.apsvr;
        this.apsvr = apsvr;
        changeSupport.firePropertyChange("apsvr", oldApsvr, apsvr);
    }

   /**
    * Getter method for trigpt
    * @return trigger point
    */
    public de.fhg.fokus.hss.model.Trigpt getTrigpt()
    {
        return this.trigpt;
    }

   /**
    * Setter method for trigpt
    * @param trigpt trigger point
    */
    public void setTrigpt(de.fhg.fokus.hss.model.Trigpt trigpt)
    {
        Trigpt oldTrigpt = this.trigpt;
        this.trigpt = trigpt;
        changeSupport.firePropertyChange("trigpt", oldTrigpt, trigpt);
    }

   /**
    * Getter method for svp
    * @return service profiles
    */
    public Set getSvp()
    {
        return this.svp;
    }

   /**
    * Setter method for svp
    * @param svp service profiles  
    */
    public void setSvp(Set svp)
    {
        this.svp = svp;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("ifcId", getIfcId()).toString();
    }

}
