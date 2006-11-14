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

import de.fhg.fokus.hss.model.GussBO;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionMapping;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class UssForm extends HssForm
{
    private static final Logger LOGGER = Logger.getLogger(UssForm.class);
    private static final ArrayList ussTypeList;

    static
    {
        ussTypeList = new ArrayList();
        ussTypeList.add(
            new Tupel(
                String.valueOf(GussBO.SVC_TYPE_Unspecific_service), "Unspecific"));
        ussTypeList.add(
            new Tupel(String.valueOf(GussBO.SVC_TYPE_PKI_Portal), "PKI Protal"));
        ussTypeList.add(
            new Tupel(
                String.valueOf(GussBO.SVC_TYPE_Authentication_Proxy),
                "Authentication Proxy"));
        ussTypeList.add(
            new Tupel(String.valueOf(GussBO.SVC_TYPE_Presence), "Presence"));
        ussTypeList.add(
            new Tupel(String.valueOf(GussBO.SVC_TYPE_MBMS), "MBMS"));
    }

    private String id;
    private int ussType;
    private int flag;
    private String nafGroup;
    private boolean delete;

    public boolean isDelete()
    {
        return delete;
    }

    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(id);
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        id = null;
        ussType = GussBO.SVC_TYPE_Unspecific_service;
        flag = GussBO.FLAG_Authentication_allowed;
        nafGroup = null;
        delete = false;
        LOGGER.debug("exiting");
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNafGroup()
    {
        return nafGroup;
    }

    public void setNafGroup(String nafGroup)
    {
        this.nafGroup = nafGroup;
    }
    public int getUssType()
    {
        return ussType;
    }

    public void setUssType(int ussType)
    {
        this.ussType = ussType;
    }

    public ArrayList getUssTypeList()
    {
        return ussTypeList;
    }

    public void setUssTypeList(ArrayList ussType)
    {
        // doNothing();
    }
}
