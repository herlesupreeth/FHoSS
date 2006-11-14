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
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * This class represents the uss table in the database.  Hibernate
 * uses it during transaction of user security setting specific data.
 * @author Hibernate CodeGenerator 
 */
public class UserSecSettings implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private int ussType;

    /** nullable persistent field */
    private Integer flag;

    /** persistent field */
    private Integer impiId;

    /** persistent field */ 
    private String nafGroup;
    
     /** 
     * full constructor 
     * @param id internal id of user security setting
     * @param ussType the uss type
     * @param flag GAA application type specific Authorization flag code
     * @param impiId id of assigned impi
     * @param nafGroup name of the NAF server
     */
    public UserSecSettings(Integer id, int ussType, Integer flag, Integer impiId, String nafGroup) {
        this.id = id;
        this.ussType = ussType;
        this.flag = flag;
        this.impiId = impiId;
        this.nafGroup = nafGroup;
    }

    /** default constructor */
    public UserSecSettings() {
    }

    /** 
     * minimal constructor 
     * @param id internal id of user security setting
     * @param ussType the uss type
     * @param impiId id of assigned impi
     */
    public UserSecSettings(Integer id, Integer ussType, int impiId) {
        this.id = id;
        this.ussType = ussType;
        this.impiId = impiId;
    }

   /**
    * Getter method for id
    * @return the id of user security setting
    */
    public Integer getId() {
        return this.id;
    }

   /**
    * Setter method for id
    * @param id the id of user security setting 
    */
    public void setId(Integer id) {
        this.id = id;
    }

   /**
    * Getter method for ussType
    * @return the user security setting type
    */
    public int getUssType() {
        return this.ussType;
    }

   /**
    * Setter method for ussType
    * @param ussType the user security setting type
    */
    public void setUssType(int ussType) {
        this.ussType = ussType;
    }

   /**
    * Getter method for flag
    * @return the GAA application type specific Authorization flag code
    */
    public Integer getFlag() {
        return this.flag;
    }

   /**
    * Setter method for flag
    * @param flag the GAA application type specific Authorization flag code
    */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

   /**
    * Getter method for impiId
    * @return the impiId
    */
    public Integer getImpiId() {
        return this.impiId;
    }

   /**
    * Setter method for impiId
    * @param impiId the impi Id
    */
    public void setImpiId(Integer impiId) {
        this.impiId = impiId;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

   /**
    * Getter method for nafGroup
    * @return the name of NAF server
    */
	public String getNafGroup() {
		return nafGroup;
	}

   /**
    * Setter method for nafGroup
    * @param nafGroup the name of NAF server
    */
	public void setNafGroup(String nafGroup) {
		this.nafGroup = nafGroup;
	}

}
