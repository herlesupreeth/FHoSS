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

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ChrginfoForm extends HssForm
{
    private static final Logger LOGGER = Logger.getLogger(ChrginfoForm.class);
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_SUBMIT = "submit";
    public static final String ACTION_DELETE = "del";

    private String chrgId;
    private String priChrgCollFnName;
    private String name;
    private String secChrgCollFnName;
    private String priEventChrgFnName;
    private String secEventChrgFnName;
    private String action;

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        chrgId = "-1";
        name = null;
        priChrgCollFnName = null;
        priEventChrgFnName = null;
        secChrgCollFnName = null;
        secEventChrgFnName = null;
        action = null;
        LOGGER.debug("exiting");
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");

        ActionErrors actionErrors = new ActionErrors();

        if ((getAction() != null)&&(getAction().equals(ACTION_SUBMIT)))
        {
            if ((getName() == null) || (getName().length() < 1))
            {
                actionErrors.add(
                    "name", new ActionMessage(
                        "chrginfo.set.error.missing.name"));
            }

            if (
                (getPriEventChrgFnName() == null)
                    || (getPriEventChrgFnName().length() < 1))
            {
                actionErrors.add(
                    "priEventChrgFnName",
                    new ActionMessage("chrginfo.set.error.missing.priEvent"));
            }
        }

        LOGGER.debug("exiting");

        return actionErrors;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(chrgId);
    }

    public String getChrgId()
    {
        return chrgId;
    }

    public void setChrgId(String chrgId)
    {
        this.chrgId = chrgId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPriChrgCollFnName()
    {
        return priChrgCollFnName;
    }

    public void setPriChrgCollFnName(String priChrgCollFnName)
    {
        this.priChrgCollFnName = priChrgCollFnName;
    }

    public String getPriEventChrgFnName()
    {
        return priEventChrgFnName;
    }

    public void setPriEventChrgFnName(String priEventChrgFnName)
    {
        this.priEventChrgFnName = priEventChrgFnName;
    }

    public String getSecChrgCollFnName()
    {
        return secChrgCollFnName;
    }

    public void setSecChrgCollFnName(String secChrgCollFnName)
    {
        this.secChrgCollFnName = secChrgCollFnName;
    }

    public String getSecEventChrgFnName()
    {
        return secEventChrgFnName;
    }

    public void setSecEventChrgFnName(String secEventChrgFnName)
    {
        this.secEventChrgFnName = secEventChrgFnName;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
}
