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

import de.fhg.fokus.hss.action.HssAction;
import de.fhg.fokus.hss.model.AsPermList;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;


/**
 * Application server form.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class AsForm extends HssForm
{
    private static final Logger LOGGER = Logger.getLogger(AsForm.class);

    /**
     * Application server name
     */
    private String asName;

    /**
     * Application server id
     */
    private String asId;

    /**
     * Application server address
     */
    private String asAddress;
    private String defaultHandling;

    /** persistent field */
    private boolean pull;

    /** persistent field */
    private boolean pullRepData;

    /** persistent field */
    private boolean pullImpu;

    /** persistent field */
    private boolean pullImpuUserState;

    /** persistent field */
    private boolean pullScscfName;

    /** persistent field */
    private boolean pullIfc;

    /** persistent field */
    private boolean pullLocInfo;

    /** persistent field */
    private boolean pullUserState;

    /** persistent field */
    private boolean pullCharging;

    /** persistent field */
    private boolean pullPsi;

    /** persistent field */
    private boolean pullMsisdn;

    /** persistent field */
    private boolean updRepData;

    /** persistent field */
    private boolean updPsi;

    /** persistent field */
    private boolean subRepData;

    /** persistent field */
    private boolean subImpuUserState;

    /** persistent field */
    private boolean subScscfname;

    /** persistent field */
    private boolean subIfc;

    /** persistent field */
    private boolean subPsi;

    public boolean isPull()
    {
        return pull;
    }

    public void setPull(boolean pull)
    {
        this.pull = pull;
    }

    public boolean isPullCharging()
    {
        return pullCharging;
    }

    public void setPullCharging(boolean pullCharging)
    {
        this.pullCharging = pullCharging;
    }

    public boolean isPullIfc()
    {
        return pullIfc;
    }

    public void setPullIfc(boolean pullIfc)
    {
        this.pullIfc = pullIfc;
    }

    public boolean isPullImpu()
    {
        return pullImpu;
    }

    public void setPullImpu(boolean pullImpu)
    {
        this.pullImpu = pullImpu;
    }

    public boolean isPullImpuUserState()
    {
        return pullImpuUserState;
    }

    public void setPullImpuUserState(boolean pullImpuUserState)
    {
        this.pullImpuUserState = pullImpuUserState;
    }

    public boolean isPullLocInfo()
    {
        return pullLocInfo;
    }

    public void setPullLocInfo(boolean pullLocInfo)
    {
        this.pullLocInfo = pullLocInfo;
    }

    public boolean isPullMsisdn()
    {
        return pullMsisdn;
    }

    public void setPullMsisdn(boolean pullMsisdn)
    {
        this.pullMsisdn = pullMsisdn;
    }

    public boolean isPullPsi()
    {
        return pullPsi;
    }

    public void setPullPsi(boolean pullPsi)
    {
        this.pullPsi = pullPsi;
    }

    public boolean isPullRepData()
    {
        return pullRepData;
    }

    public void setPullRepData(boolean pullRepData)
    {
        this.pullRepData = pullRepData;
    }

    public boolean isPullScscfName()
    {
        return pullScscfName;
    }

    public void setPullScscfName(boolean pullScscfName)
    {
        this.pullScscfName = pullScscfName;
    }

    public boolean isPullUserState()
    {
        return pullUserState;
    }

    public void setPullUserState(boolean pullUserState)
    {
        this.pullUserState = pullUserState;
    }

    public boolean isSubIfc()
    {
        return subIfc;
    }

    public void setSubIfc(boolean subIfc)
    {
        this.subIfc = subIfc;
    }

    public boolean isSubImpuUserState()
    {
        return subImpuUserState;
    }

    public void setSubImpuUserState(boolean subImpuUserState)
    {
        this.subImpuUserState = subImpuUserState;
    }

    public boolean isSubPsi()
    {
        return subPsi;
    }

    public void setSubPsi(boolean subPsi)
    {
        this.subPsi = subPsi;
    }

    public boolean isSubRepData()
    {
        return subRepData;
    }

    public void setSubRepData(boolean subRepData)
    {
        this.subRepData = subRepData;
    }

    public boolean isSubScscfname()
    {
        return subScscfname;
    }

    public void setSubScscfname(boolean subScscfname)
    {
        this.subScscfname = subScscfname;
    }

    public boolean isUpdPsi()
    {
        return updPsi;
    }

    public void setUpdPsi(boolean updPsi)
    {
        this.updPsi = updPsi;
    }

    public boolean isUpdRepData()
    {
        return updRepData;
    }

    public void setUpdRepData(boolean updRepData)
    {
        this.updRepData = updRepData;
    }

    public String getDefaultHandling()
    {
        return defaultHandling;
    }

    public void setDefaultHandling(String defaultHandling)
    {
        this.defaultHandling = defaultHandling;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");

        asName = null;
        asId = null;
        asAddress = null;
        defaultHandling = "0";
        pull = false;
        pullRepData = false;
        pullImpu = false;
        pullImpuUserState = false;
        pullScscfName = false;
        pullIfc = false;
        pullLocInfo = false;
        pullUserState = false;
        pullCharging = false;
        pullPsi = false;
        pullMsisdn = false;
        updRepData = false;
        updPsi = false;
        subRepData = false;
        subImpuUserState = false;
        subScscfname = false;
        subIfc = false;
        subPsi = false;
        LOGGER.debug("exiting");
    }
    
  	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
  		LOGGER.debug("entering");
  		ActionErrors actionErrors = new ActionErrors();

      if ((getAsName() == null) || (getAsName().length() < 1))
      {
          actionErrors.add(
              "asName", new ActionMessage(
                  "as.error.missing.name"));
      }
      LOGGER.debug("extiting");
  		return actionErrors;
  		
  	}

    public Integer getPrimaryKey()
    {
        return getPrimaryKey(getAsId());
    }

    public String getAsAddress()
    {
        return asAddress;
    }

    public void setAsAddress(String asAddress)
    {
        this.asAddress = asAddress;
    }

    public String getAsId()
    {
        return asId;
    }

    public void setAsId(String asId)
    {
        this.asId = asId;
    }

    public String getAsName()
    {
        return asName;
    }

    public void setAsName(String asName)
    {
        this.asName = asName;
    }
}
