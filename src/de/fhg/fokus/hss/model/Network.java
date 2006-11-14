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
 * This class represents the networks table in the database. Hibernate
 * uses it during transaction of network specific data.
 * @author Hibernate CodeGenerator 
 */
public class Network implements Serializable {

    /** identifier field */
    private Integer nwId;

    /** persistent field */
    private String networkString;

    /** persistent field */
    private Set roams;

    /** 
     * full constructor 
     * @param networkString Name of the network
     * @param roams roaming networks
     */
    public Network(String networkString, Set roams) {
        this.networkString = networkString;
        this.roams = roams;
    }

    /** default constructor */
    public Network() {
    }

   /**
    * Getter method for nwId
    * @return internal Id of the network
    */
    public Integer getNwId() {
        return this.nwId;
    }
   /**
    * Setter method for nwId
    * @param nwId internal Id of the network 
    */
    public void setNwId(Integer nwId) {
        this.nwId = nwId;
    }

   /**
    * Getter method for networkString
    * @return name of the network 
    */
    public String getNetworkString() {
        return this.networkString;
    }
   /**
    * Setter method for networkString
    * @param networkString name of the network 
    */
    public void setNetworkString(String networkString) {
        this.networkString = networkString;
    }

   /**
    * Getter method for roams
    * @return the roaming networks
    */
    public Set getRoams() {
        return this.roams;
    }
   /**
    * Setter method for roams
    * @param roams roaming networks  
    */
    public void setRoams(Set roams) {
        this.roams = roams;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("nwId", getNwId())
            .toString();
    }

}
