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
package de.fhg.fokus.hss.form;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Abstract action form class, implements common methods.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class HssForm extends ActionForm implements Serializable
{

    public String toString()
    {
        return ToStringBuilder.reflectionToString(
            this, ToStringStyle.MULTI_LINE_STYLE);
    }

    protected Integer getPrimaryKey(String field)
    {
        if ((field == null) || (field.length() < 1))
        {
            throw new NullPointerException(
                "primary key required field is empty or null.");
        }

        int primaryKey = Integer.parseInt(field);

        return new Integer(primaryKey);
    }

    /**
     * Returns the form bean specific primary key if exists else null.
     *
     * @return the form bean specific primary key if exists else null.
     * @throws NullPointerException If required field null.
     */
    public abstract Integer getPrimaryKey() throws NullPointerException;

}
