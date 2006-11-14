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
 * This class represents the trigpt table in the database. Hibernate
 * uses it during transaction of trigger point specific data.
 * @author Hibernate CodeGenerator 
 */
public class Trigpt extends NotifySupport implements Serializable
{
    /** identifier field */
    private Integer trigptId;

    /** persistent field */
    private String name;

    /** persistent field */
    private int cnf;
    
    /** persistent field */
    private Set spts;

    /** 
     * full constructor 
     * @param name name of trigger point
     * @param spts a set of service point triggers
     */
    public Trigpt(String name, Set spts)
    {
        super();
        this.name = name;
        this.spts = spts;
    }

    /** default constructor */
    public Trigpt()
    {
        super();
    }

   /**
    * Getter method for trigpt
    * @return the id of trigger point
    */
    public Integer getTrigptId()
    {
        return this.trigptId;
    }

   /**
    * Setter method for trigptId
    * @param trigptId id of trigger point
    */
    public void setTrigptId(Integer trigptId)
    {
        this.trigptId = trigptId;
    }

   /**
    * Getter method for name
    * @return the name of triggerpoint
    */
    public String getName()
    {
        return this.name;
    }

   /**
    * Setter method for name
    * @param name name of trigger point
    */
    public void setName(String name)
    {
        this.name = name;
    }

   /**
    * Getter method for spts
    * @return the set of service point triggers
    */
    public Set getSpts()
    {
        return this.spts;
    }

   /**
    * Setter method for spts
    * @param spts the set of service point triggers
    */
    public void setSpts(Set spts)
    {
        this.spts = spts;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("trigptId", getTrigptId())
                                        .toString();
    }

   /**
    * Getter method for cnf
    * @return cnf the integer value representing the normal form:
    *         1 for cnf and 0 for dnf
    */
	public int getCnf() {
		return cnf;
	}

   /**
    * Setter method for cnf
    * @param cnf the normal form:1 for cnf and 0 for dnf
    */
	public void setCnf(int cnf) {
		this.cnf = cnf;
	}

   /**
    * it marks the change
    * @param value the boolean value to indicate the change
    */	
	public void markChanged(boolean value) {
		fire("spts", false, value);
	}
}
