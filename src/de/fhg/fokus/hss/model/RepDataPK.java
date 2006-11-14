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
 * This is just a help class for repository data specific database transactions.
 * @author Hibernate CodeGenerator 
 */
public class RepDataPK implements Serializable {

    /** identifier field */
    private Integer impuId;

    /** identifier field */
    private String svcInd;

    /** 
     * full constructor 
     * @param impuId internal id of public identity
     * @param svcInd service identity
     */
    public RepDataPK(Integer impuId, String svcInd) {
        this.impuId = impuId;
        this.svcInd = svcInd;
    }

    /** default constructor */
    public RepDataPK() {
    }

   /**
    * Getter method for impuId
    * @return the internal identifier of public identity
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
    * Getter method for svcInd
    * @return the internal id of service identity
    */
    public String getSvcInd() {
        return this.svcInd;
    }

   /**
    * Setter method for svcInd
    * @param svcInd the internal id of service identity
    */
    public void setSvcInd(String svcInd) {
        this.svcInd = svcInd;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("impuId", getImpuId())
            .append("svcInd", getSvcInd())
            .toString();
    }

   /**
    * This method compares two objects
    * @return true if equal else false 
    */
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof RepDataPK) ) return false;
        RepDataPK castOther = (RepDataPK) other;
        return new EqualsBuilder()
            .append(this.getImpuId(), castOther.getImpuId())
            .append(this.getSvcInd(), castOther.getSvcInd())
            .isEquals();
    }

   /**
    * It calculates hash code
    * @return the calculated hash code
    */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getImpuId())
            .append(getSvcInd())
            .toHashCode();
    }

}
