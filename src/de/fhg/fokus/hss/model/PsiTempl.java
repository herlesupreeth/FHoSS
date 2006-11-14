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
 * This class represents the psi_templ table in the database. Hibernate
 * uses it during transaction of public service identitiy templates.
 * @author Hibernate CodeGenerator 
 */
public class PsiTempl implements Serializable {

    /** identifier field */
    private Integer templId;

    /** persistent field */
    private String templName;

    /** persistent field */
    private String username;

    /** persistent field */
    private String hostname;

    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Apsvr apsvr;

    /** persistent field */
    private Set psis;

    /** 
     * full constructor 
     * @param templId internal id of public service identity template
     * @param templName name of template
     * @param username user name
     * @param hostname host name
     * @param apsvr application server
     * @param psis public service identities
     */
    public PsiTempl(Integer templId, String templName, String username, String hostname, de.fhg.fokus.hss.model.Apsvr apsvr, Set psis) {
        this.templId = templId;
        this.templName = templName;
        this.username = username;
        this.hostname = hostname;
        this.apsvr = apsvr;
        this.psis = psis;
    }

    /** default constructor */
    public PsiTempl() {
    }

    /** 
     * minimal constructor 
     * @param templId internal id of public service identity template
     * @param templName name of template
     * @param username user name
     * @param hostname host name
     * @param psis public service identities
     */
    public PsiTempl(Integer templId, String templName, String username, String hostname, Set psis) {
        this.templId = templId;
        this.templName = templName;
        this.username = username;
        this.hostname = hostname;
        this.psis = psis;
    }

   /**
    * Getter method for templId
    * @return the internal Id of public service identity template
    */
    public Integer getTemplId() {
        return this.templId;
    }

   /**
    * Setter method for templId
    * @param templId internal id of public service identity template
    */
    public void setTemplId(Integer templId) {
        this.templId = templId;
    }

   /**
    * Getter method for name
    * @return the name of public service identity template
    */
    public String getTemplName() {
        return this.templName;
    }

   /**
    * Setter method for templName
    * @param templName template name
    */
    public void setTemplName(String templName) {
        this.templName = templName;
    }

   /**
    * Getter method for username
    * @return the user name 
    */
    public String getUsername() {
        return this.username;
    }

   /**
    * Setter method for username
    * @param username user name
    */
    public void setUsername(String username) {
        this.username = username;
    }

   /**
    * Getter method for hostname
    * @return the hostname 
    */
    public String getHostname() {
        return this.hostname;
    }

   /**
    * Setter method for hostname
    * @param hostname hostname
    */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

   /**
    * Getter method for apsvr
    * @return application server 
    */
    public de.fhg.fokus.hss.model.Apsvr getApsvr() {
        return this.apsvr;
    }

   /**
    * Setter method for apsvr
    * @param apsvr the application server
    */
    public void setApsvr(de.fhg.fokus.hss.model.Apsvr apsvr) {
        this.apsvr = apsvr;
    }

   /**
    * Getter method for psis
    * @return the public service identities 
    */
    public Set getPsis() {
        return this.psis;
    }

   /**
    * Setter method for psis
    * @param psis public service identities
    */
    public void setPsis(Set psis) {
        this.psis = psis;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("templId", getTemplId())
            .toString();
    }

}
