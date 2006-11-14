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
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * This class represents the imsu table in the database. Hibernate
 * uses it during transaction of subscription specific data.
 * @author Hibernate CodeGenerator 
 */
public class Imsu implements Serializable {

    /** identifier field */
    private Integer imsuId;

    /** persistent field */
    private String name;

    /** persistent field */
    private Set impis;

    /** 
     * full constructor 
     * @param name Name of the subscription
     * @param impis the assigned private identities
     */
    public Imsu(String name, Set impis) {
        this.name = name;
        this.impis = impis;
    }

    /** default constructor */
    public Imsu() {
    }
    
   /**
    * Getter method for imsuId
    * @return internal Id of the ims subscription
    */
    public Integer getImsuId() {
        return this.imsuId;
    }

   /**
    * Setter method for imsuId
    * @param imsuId internal Id of the ims subscription  
    */
    public void setImsuId(Integer imsuId) {
        this.imsuId = imsuId;
    }

   /**
    * Getter method for name
    * @return the ims subscription name
    */
    public String getName() {
        return this.name;
    }

   /**
    * Setter method for name of ims subscription
    * @param lastname the subscription name  
    */
    public void setName(String lastname) {
        this.name = lastname;
    }

   /**
    * Getter method for impis
    * @return the private identities assigned to the subscription
    */    
    public Set getImpis() {
        return this.impis;
    }

   /**
    * Setter method for impis
    * @param impis private identities 
    */
    public void setImpis(Set impis) {
        this.impis = impis;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("imsuId", getImsuId())
            .toString();
    }

}
