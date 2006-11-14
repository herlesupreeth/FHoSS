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
 * This class represents the svp table in the database. Hibernate
 * uses it during transaction of service profile specific data.
 * @author Hibernate CodeGenerator 
 */
public class Svp extends NotifySupport implements Serializable {

    /** identifier field */
    private Integer svpId;

    /** persistent field */
    private String name;

    /** persistent field */
    private Set ifc2svps;

    /** persistent field */
    private Set impus;

    /** 
     * full constructor 
     * @param svpId internal id of service profile
     * @param name name of service profile
     * @param ifc2svps a set of initial filter criteria 2 service profile mappings
     * @param impus a set of public identities
     */
    public Svp(Integer svpId, String name, Set ifc2svps, Set impus) {
        this.svpId = svpId;
        this.name = name;
        this.ifc2svps = ifc2svps;
        this.impus = impus;
    }

    /** default constructor */
    public Svp() {
    }

   /**
    * Getter method for svpId
    * @return the internal id of service profile
    */
    public Integer getSvpId() {
        return this.svpId;
    }

   /**
    * Setter method for svpId
    * @param svpId internal service profile Id
    */
    public void setSvpId(Integer svpId) {
        this.svpId = svpId;
    }

   /**
    * Getter method for name
    * @return the name of service profile
    */
    public String getName() {
        return this.name;
    }

   /**
    * Setter method for name
    * @param name the service profile name
    */
    public void setName(String name) {
        this.name = name;
    }

   /**
    * Getter method for ifc2svps
    * @return the set of ifc2svp mappings
    */
    public Set getIfc2svps() {
        return this.ifc2svps;
    }

   /**
    * Setter method for ifc2svps
    * @param ifc2svps a set of ifc2svp mappings
    */
    public void setIfc2svps(Set ifc2svps) {
        this.ifc2svps = ifc2svps;
    }

   /**
    * Getter method for impus
    * @return the public identities
    */
    public Set getImpus() {
        return this.impus;
    }
    
   /**
    * Setter method for impus
    * @param impus a set of public identities
    */
    public void setImpus(Set impus) {
        this.impus = impus;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("svpId", getSvpId())
            .toString();
    }

}
