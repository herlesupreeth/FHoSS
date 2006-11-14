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
 * This class represents the chrginfo table in the database. Hibernate
 * uses it during transaction of accounting data.
 * @author Hibernate CodeGenerator 
 */
public class Chrginfo implements Serializable {

    /** identifier field */
    private Integer chrgId;

    /** persistent field */
    private String priChrgCollFnName;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String secChrgCollFnName;

    /** persistent field */
    private String priEventChrgFnName;

    /** nullable persistent field */
    private String secEventChrgFnName;

    /** 
     * full constructor 
     * @param chrgId charging Id
     * @param priChrgCollFnName primary charging collection function name
     * @param name name
     * @param secChrgCollFnName secondary charging collection function name
     * @param priEventChrgFnName primary event charging function name
     * @param secEventChrgFnName secondary event charging function name
     */
    public Chrginfo(Integer chrgId, String priChrgCollFnName, String name, String secChrgCollFnName, String priEventChrgFnName, String secEventChrgFnName) {
        this.chrgId = chrgId;
        this.priChrgCollFnName = priChrgCollFnName;
        this.name = name;
        this.secChrgCollFnName = secChrgCollFnName;
        this.priEventChrgFnName = priEventChrgFnName;
        this.secEventChrgFnName = secEventChrgFnName;
    }

    /** default constructor */
    public Chrginfo() {
    }

    /** 
     * minimal constructor 
     * @param chrgId charging Id
     * @param priChrgCollFnName primary charging collection function name
     * @param name name
     * @param priEventChrgFnName primary event charging function name
     */
    public Chrginfo(Integer chrgId, String priChrgCollFnName, String name, String priEventChrgFnName) {
        this.chrgId = chrgId;
        this.priChrgCollFnName = priChrgCollFnName;
        this.name = name;
        this.priEventChrgFnName = priEventChrgFnName;
    }

   /**
    * Get method for charging id 
    * @return charging id
    */   
    public Integer getChrgId() {
        return this.chrgId;
    }

   /**
    * Set method for charging id
    * @param chrgId charging id
    */
    public void setChrgId(Integer chrgId) {
        this.chrgId = chrgId;
    }

   /**
    * Get method for primary charging collection function name
    * @return primary charging collection function name
    */  
    public String getPriChrgCollFnName() {
        return this.priChrgCollFnName;
    }

   /**
    * Set method for primary charging collection function name
    * @param priChrgCollFnName name of primary charging collection function
    */
    public void setPriChrgCollFnName(String priChrgCollFnName) {
        this.priChrgCollFnName = priChrgCollFnName;
    }

   /**
    * Get method for Name
    * @return name
    */  
    public String getName() {
        return this.name;
    }

   /**
    * Set method for name
    * @param name name
    */
    public void setName(String name) {
        this.name = name;
    }

   /**
    * Get method for secondary charging collection function name
    * @return name of secondary charging collection function
    */  
    public String getSecChrgCollFnName() {
        return this.secChrgCollFnName;
    }

   /**
    * Set method for secondary charging collection function name
    * @param secChrgCollFnName secondary charging collection function name
    */
    public void setSecChrgCollFnName(String secChrgCollFnName) {
        this.secChrgCollFnName = secChrgCollFnName;
    }

   /**
    * Get method for primary event charging function name
    * @return primary event charging function name
    */  
    public String getPriEventChrgFnName() {
        return this.priEventChrgFnName;
    }

   /**
    * Set method for primary event charging function name
    * @param priEventChrgFnName primary event charging function name
    */
    public void setPriEventChrgFnName(String priEventChrgFnName) {
        this.priEventChrgFnName = priEventChrgFnName;
    }

   /**
    * Get method for secondary event charging function name 
    * @return secondary event charging function name
    */  
    public String getSecEventChrgFnName() {
        return this.secEventChrgFnName;
    }

   /**
    * Set method for secondary event charging function name
    * @param secEventChrgFnName secondary event charging function name
    */
    public void setSecEventChrgFnName(String secEventChrgFnName) {
        this.secEventChrgFnName = secEventChrgFnName;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("chrgId", getChrgId())
            .toString();
    }

}
