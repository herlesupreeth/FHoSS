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
 * This is just a help class assisting during database transactions of notification data.
 * @author Hibernate CodeGenerator 
 */
public class NotifyIfcPK implements Serializable {

    /** identifier field */
    private Integer impuId;

    /** identifier field */
    private Integer ifcApsvrId;

    /** identifier field */
    private Integer apsvrId;

    /** 
     * full constructor 
     * @param impuId internal id of public identity
     * @param ifcApsvrId 
     * @param apsvrId id of application server
     */
    public NotifyIfcPK(Integer impuId, Integer ifcApsvrId, Integer apsvrId) {
        this.impuId = impuId;
        this.ifcApsvrId = ifcApsvrId;
        this.apsvrId = apsvrId;
    }

    /** default constructor */
    public NotifyIfcPK() {
    }

   /**
    * Getter method for impuId
    * @return the internal Id of public identity 
    */
    public Integer getImpuId() {
        return this.impuId;
    }

   /**
    * Setter method for impuId
    * @param impuId the internal id of public identity 
    */
    public void setImpuId(Integer impuId) {
        this.impuId = impuId;
    }

   /**
    * Getter method for ifcApsvrId
    * @return the ifcApsvrId
    */
    public Integer getIfcApsvrId() {
        return this.ifcApsvrId;
    }

   /**
    * Setter method for ifcApsvrId
    * @param ifcApsvrId 
    */
    public void setIfcApsvrId(Integer ifcApsvrId) {
        this.ifcApsvrId = ifcApsvrId;
    }

   /**
    * Getter method for apsvrId
    * @return the id of application server
    */
    public Integer getApsvrId() {
        return this.apsvrId;
    }

   /**
    * Setter method for apsvrId
    * @param apsvrId id of application server
    */
    public void setApsvrId(Integer apsvrId) {
        this.apsvrId = apsvrId;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("impuId", getImpuId())
            .append("ifcApsvrId", getIfcApsvrId())
            .append("apsvrId", getApsvrId())
            .toString();
    }

   /**
    * This method compares two objects
    * @return true if equal else false 
    */
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof NotifyIfcPK) ) return false;
        NotifyIfcPK castOther = (NotifyIfcPK) other;
        return new EqualsBuilder()
            .append(this.getImpuId(), castOther.getImpuId())
            .append(this.getIfcApsvrId(), castOther.getIfcApsvrId())
            .append(this.getApsvrId(), castOther.getApsvrId())
            .isEquals();
    }

   /**
    * It calculates hash code
    * @return the calculated hash code
    */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getImpuId())
            .append(getIfcApsvrId())
            .append(getApsvrId())
            .toHashCode();
    }

}
