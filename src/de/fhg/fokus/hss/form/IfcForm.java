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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class IfcForm extends HssForm
{
    private static final Logger LOGGER = Logger.getLogger(IfcForm.class);
    private String ifcId;
    private String ifcName;
    private String apsvrId;
    private String apsvrName;
    private String triggerPointId;
    private String triggerPointName;
    /**
     * user profil specific value
     */
    private String priority;
    private List triggerPoints;
    private List apsvrs;
    
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        ifcId = null;
        ifcName = null;
        apsvrId = null;
        apsvrName = null;
        triggerPointId = null;
        triggerPointName = null;
        priority = null;
        triggerPoints = new ArrayList();
        apsvrs = new ArrayList();
        LOGGER.debug("exiting");
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
    		LOGGER.debug("entering");
        ActionErrors actionErrors = new ActionErrors();

        if ((getIfcName() == null) || (getIfcName().length() < 1))
        {
            actionErrors.add(
                "ifcName", new ActionMessage(
                    "ifc.error.missing.ifcName"));
        }
        if ((getApsvrId() == null) || (getApsvrId().length() < 1))
        {
            actionErrors.add(
                "apsvrId", new ActionMessage(
                    "ifc.error.missing.apsvr"));
        }
        if ((getTriggerPointId() == null) || (getTriggerPointId().length() < 1))
        {
            actionErrors.add(
                "triggerPointId", new ActionMessage(
                    "ifc.error.missing.triggerPoint"));
        }
        LOGGER.debug("exiting");
        return actionErrors;
    }
    
    public String getApsvrId()
    {
        return apsvrId;
    }

    public void setApsvrId(String apsvrId)
    {
        this.apsvrId = apsvrId;
    }

    public String getApsvrName()
    {
        return apsvrName;
    }

    public void setApsvrName(String apsvrName)
    {
        this.apsvrName = apsvrName;
    }

    public String getIfcId()
    {
        return ifcId;
    }

    public void setIfcId(String ifcId)
    {
        this.ifcId = ifcId;
    }

    public String getTriggerPointId()
    {
        return triggerPointId;
    }

    public void setTriggerPointId(String triggerPointId)
    {
        this.triggerPointId = triggerPointId;
    }

    public String getTriggerPointName()
    {
        return triggerPointName;
    }

    public void setTriggerPointName(String triggerPointName)
    {
        this.triggerPointName = triggerPointName;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(getIfcId());
    }

    public String getIfcName()
    {
        return ifcName;
    }

    public void setIfcName(String ifcName)
    {
        this.ifcName = ifcName;
    }

    public List getApsvrs()
    {
        return apsvrs;
    }

    public void setApsvrs(List apsvrs)
    {
        this.apsvrs = apsvrs;
    }

    public List getTriggerPoints()
    {
        return triggerPoints;
    }

    public void setTriggerPoints(List triggerPoints)
    {
        this.triggerPoints = triggerPoints;
    }

		public String getPriority() {
			return priority;
		}

		public void setPriority(String priority) {
			this.priority = priority;
		}
}
