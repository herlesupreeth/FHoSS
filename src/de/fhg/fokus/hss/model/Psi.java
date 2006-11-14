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
 * This class represents the psi table in the database.Hibernate uses it during 
 * transaction of public service identities.
 * @author Hibernate CodeGenerator 
 */
public class Psi implements Serializable {

    /** identifier field */
    private Integer psiId;

    /** persistent field */
    private String name;

    /** persistent field */
    private String wildcard;

    /** persistent field */
    private de.fhg.fokus.hss.model.PsiTempl psiTempl;


    /** nullable persistent field */
    private de.fhg.fokus.hss.model.Impu impuRoot;
    
    /** persistent field */
    private Set impus;

    /** 
     * full constructor 
     * @param psiId internal id of public service identity
     * @param name name
     * @param wildcard the wildcard
     * @param psiTempl public service identity template
     * @param impus public identities
     * @param impuRoot root public identity
     */
    public Psi(Integer psiId, String name, String wildcard, de.fhg.fokus.hss.model.PsiTempl psiTempl, Impu impuRoot, Set impus) {
        this.psiId = psiId;
        this.name = name;
        this.wildcard = wildcard;
        this.psiTempl = psiTempl;
        this.impus = impus;
        this.impuRoot = impuRoot;
    }

    /** default constructor */
    public Psi() {
    }

   /**
    * Getter method for psiId
    * @return the internal Id of public service identity 
    */
    public Integer getPsiId() {
        return this.psiId;
    }

   /**
    * Setter method for psiId
    * @param psiId internal Id of public service identity
    */
    public void setPsiId(Integer psiId) {
        this.psiId = psiId;
    }

   /**
    * Getter method for name
    * @return the name of public service identity
    */
    public String getName() {
        return this.name;
    }

   /**
    * Setter method for name
    * @param name public service identity name
    */
    public void setName(String name) {
        this.name = name;
    }

   /**
    * Getter method for wildcard
    * @return the wildcard 
    */
    public String getWildcard() {
        return this.wildcard;
    }

   /**
    * Setter method for wildcard
    * @param wildcard wildcard
    */
    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }

   /**
    * Getter method for psiTempl
    * @return the public service identity template
    */
    public de.fhg.fokus.hss.model.PsiTempl getPsiTempl() {
        return this.psiTempl;
    }

   /**
    * Setter method for psiTempl
    * @param psiTempl public service identity template
    */
    public void setPsiTempl(de.fhg.fokus.hss.model.PsiTempl psiTempl) {
        this.psiTempl = psiTempl;
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
    * @param impus public identities
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
            .append("psiId", getPsiId())
            .toString();
    }

   /**
    * Getter method for impuRoot
    * @return the root public identity 
    */
	public de.fhg.fokus.hss.model.Impu getImpuRoot() {
		return impuRoot;
	}

   /**
    * Setter method for impuRoot
    * @param impuRoot root public identity
    */
	public void setImpuRoot(de.fhg.fokus.hss.model.Impu impuRoot) {
		this.impuRoot = impuRoot;
	}

}
