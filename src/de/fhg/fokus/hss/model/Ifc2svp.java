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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * This class represents the ifc2svp table in the database. Hibernate
 * uses it during transaction of mapping between initial filter criteria and service
 * profile.
 * @author Hibernate CodeGenerator 
 */
public class Ifc2svp implements Serializable {

    /** identifier field */
    private de.fhg.fokus.hss.model.Ifc2svpPK comp_id;

    /** persistent field */
    private int priority;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Svp svp;

    /** persistent field */
    private de.fhg.fokus.hss.model.Ifc ifc;

    /** 
     * full constructor 
     * @param comp_id id for internal purpose
     * @param priority Priority
     * @param svp Service Profile
     * @param ifc Initial Filter Criteria
     */
    public Ifc2svp(de.fhg.fokus.hss.model.Ifc2svpPK comp_id, int priority, de.fhg.fokus.hss.model.Svp svp, de.fhg.fokus.hss.model.Ifc ifc) {
        this.comp_id = comp_id;
        this.priority = priority;
        this.svp = svp;
        this.ifc = ifc;
    }

    /** default constructor */
    public Ifc2svp() {
    }

    /** minimal constructor 
     * @param comp_id id for internal purpose
     * @param priority Priority
     * @param ifc Initial Filter Criteria
     */
    public Ifc2svp(de.fhg.fokus.hss.model.Ifc2svpPK comp_id, int priority, de.fhg.fokus.hss.model.Ifc ifc) {
        this.comp_id = comp_id;
        this.priority = priority;
        this.ifc = ifc;
    }

   /**
    * Get method for comp_id
    * @return comp_id
    */
    public de.fhg.fokus.hss.model.Ifc2svpPK getComp_id() {
        return this.comp_id;
    }

   /**
    * Set method for comp_id
    * @param comp_id 
    */
    public void setComp_id(de.fhg.fokus.hss.model.Ifc2svpPK comp_id) {
        this.comp_id = comp_id;
    }

   /**
    * Get method for priority
    * @return priority
    */
    public int getPriority() {
        return this.priority;
    }

   /**
    * Set method for priority
    * @param priority
    */
    public void setPriority(int priority) {
        this.priority = priority;
    }

   /**
    * Get method for service profile
    * @return service profile
    */
    public de.fhg.fokus.hss.model.Svp getSvp() {
        return this.svp;
    }

   /**
    * Set method for service profile
    * @param svp service profile
    */
    public void setSvp(de.fhg.fokus.hss.model.Svp svp) {
        this.svp = svp;
    }

   /**
    * Get method for Initial Filter Criteria
    * @return Initial Filter Criteria
    */
    public de.fhg.fokus.hss.model.Ifc getIfc() {
        return this.ifc;
    }

   /**
    * Set method for Initial Filter Criteria
    * @param ifc Initial Filter Criteria
    */
    public void setIfc(de.fhg.fokus.hss.model.Ifc ifc) {
        this.ifc = ifc;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

   /**
    * This method compares two objects
    * @return true if equal else false 
    */
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Ifc2svp) ) return false;
        Ifc2svp castOther = (Ifc2svp) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

   /**
    * This method calculates hash code
    * @return the hash code
    */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
