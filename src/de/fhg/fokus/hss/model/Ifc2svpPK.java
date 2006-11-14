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
 * This is just a helping class for hibernate database transactions. It is used during
 * transaction of initial filter criteria and service profile specific data.
 * @author Hibernate CodeGenerator 
 */
public class Ifc2svpPK implements Serializable {

    /** identifier field */
    private Integer ifcId;

    /** identifier field */
    private Integer svpId;

    /** 
     * full constructor 
     * @param ifcId Initial filter criteria Id
     * @param svpId service profile Id 
     */
    public Ifc2svpPK(Integer ifcId, Integer svpId) {
        this.ifcId = ifcId;
        this.svpId = svpId;
    }

    /** default constructor */
    public Ifc2svpPK() {
    }

    /**
     * Getter method for ifcId
     * @return the initial filter criteria id
     */
    public Integer getIfcId() {
        return this.ifcId;
    }

   /**
    * Set method for ifcId
    * @param ifcId the initial filter criteria id
    */
    public void setIfcId(Integer ifcId) {
        this.ifcId = ifcId;
    }

    /**
     * Getter method for svpId
     * @return the service profile id
     */
    public Integer getSvpId() {
        return this.svpId;
    }

   /**
    * Set method for svpId
    * @param svpId the service profile Id
    */
    public void setSvpId(Integer svpId) {
        this.svpId = svpId;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("ifcId", getIfcId())
            .append("svpId", getSvpId())
            .toString();
    }

   /**
    * This method compares two objects
    * @return true if equal else false 
    */
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Ifc2svpPK) ) return false;
        Ifc2svpPK castOther = (Ifc2svpPK) other;
        return new EqualsBuilder()
            .append(this.getIfcId(), castOther.getIfcId())
            .append(this.getSvpId(), castOther.getSvpId())
            .isEquals();
    }

   /**
    * This method calculates hash code
    * @return the hash code
    */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getIfcId())
            .append(getSvpId())
            .toHashCode();
    }

}
