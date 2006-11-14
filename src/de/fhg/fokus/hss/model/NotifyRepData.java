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
 * This class represents the notify_rep_data table in the database. Hibernate
 * uses it during transaction of repository data.
 * 
 * @author Hibernate CodeGenerator 
 */
public class NotifyRepData implements Serializable {

    /** identifier field */
    private de.fhg.fokus.hss.model.NotifyRepDataPK comp_id;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.RepData repData;

    /** 
     * full constructor 
     * @param comp_id helping variable
     * @param repData repository data
     */
    public NotifyRepData(de.fhg.fokus.hss.model.NotifyRepDataPK comp_id, de.fhg.fokus.hss.model.RepData repData) {
        this.comp_id = comp_id;
        this.repData = repData;
    }

    /** default constructor */
    public NotifyRepData() {
    }

    /** 
     * minimal constructor 
     * @param comp_id helping variable
     */
    public NotifyRepData(de.fhg.fokus.hss.model.NotifyRepDataPK comp_id) {
        this.comp_id = comp_id;
    }

   /**
    * Getter method for comp_id
    * @return the helping class variable comp_id 
    */
    public de.fhg.fokus.hss.model.NotifyRepDataPK getComp_id() {
        return this.comp_id;
    }

   /**
    * Setter method for comp_id
    * @param comp_id helping class variable 
    */
    public void setComp_id(de.fhg.fokus.hss.model.NotifyRepDataPK comp_id) {
        this.comp_id = comp_id;
    }

   /**
    * Getter method for repData
    * @return the repository data 
    */
    public de.fhg.fokus.hss.model.RepData getRepData() {
        return this.repData;
    }

   /**
    * Setter method for repData
    * @param repData the repository data
    */
    public void setRepData(de.fhg.fokus.hss.model.RepData repData) {
        this.repData = repData;
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
        if ( !(other instanceof NotifyRepData) ) return false;
        NotifyRepData castOther = (NotifyRepData) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

   /**
    * It calculates hash code
    * @return the calculated hash code
    */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
