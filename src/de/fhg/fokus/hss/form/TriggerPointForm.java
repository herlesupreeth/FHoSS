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

import de.fhg.fokus.hss.model.TrigptBO;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class TriggerPointForm extends HssForm
{
    /**
     * The Logger
     */
    private static final Logger LOGGER =
        Logger.getLogger(TriggerPointForm.class);
    private static ArrayList typeList;

    static
    {
        typeList = new ArrayList();
        typeList.add(new Tupel(TrigptBO.TYPE_URI, "Request URI"));
        typeList.add(new Tupel(TrigptBO.TYPE_SIP_METHOD, "Sip Method"));
        typeList.add(new Tupel(TrigptBO.TYPE_SESSION_CASE, "Session Case"));
        typeList.add(new Tupel(TrigptBO.TYPE_SESSION_DESC, "Session Desc"));
        typeList.add(new Tupel(TrigptBO.TYPE_SIP_HEADER, "Sip Header"));
    }

    private String trigPtId;
    private String trigPtName;
    private List spts;
    private int cnf;
    private int type;
    private int group;

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getCnf()
    {
        return cnf;
    }

    public void setCnf(int cnf)
    {
        this.cnf = cnf;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        trigPtId = null;
        trigPtName = null;
        cnf = 0;
        type = -1;
        group = -1;

        Factory factory =
            new Factory()
            {
                public Object create()
                {
                    return new SptForm();
                }
            };

        spts = LazyList.decorate(new ArrayList(), factory);
        LOGGER.debug("exiting");
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");

        ActionErrors actionErrors = new ActionErrors();

        if ((getTrigPtName() == null) || (getTrigPtName().length() < 1))
        {
            actionErrors.add(
                "trigPtName", new ActionMessage("triggerPoint.missing.name"));
        }

        Iterator it = spts.iterator();

        while (it.hasNext())
        {
            SptForm sptFrom = (SptForm) it.next();
            ActionErrors nested = sptFrom.validate(arg0, arg1);

            if (nested.size() > 0)
            {
                actionErrors.add(nested);
            }
        }

        LOGGER.debug("exiting");

        return actionErrors;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(getTrigPtId());
    }

    public String getTrigPtId()
    {
        return trigPtId;
    }

    public void setTrigPtId(String trigPtId)
    {
        this.trigPtId = trigPtId;
    }

    public String getTrigPtName()
    {
        return trigPtName;
    }

    public void setTrigPtName(String trigPtName)
    {
        this.trigPtName = trigPtName;
    }

    public List getSpts()
    {
        return spts;
    }

    public void setSpts(List spts)
    {
        this.spts = spts;
    }

    public ArrayList getTypeList()
    {
        return typeList;
    }

    public void setTypeList(ArrayList typeList)
    {
        // doNothing();
    }
}
