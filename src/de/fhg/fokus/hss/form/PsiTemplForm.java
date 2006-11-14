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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Action Form represents a Public Service Identity Template
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PsiTemplForm extends HssForm
{
    private static final Logger LOGGER = Logger.getLogger(PsiTemplForm.class);
    private String psiTemplId;
    private String psiTemplName;
    private String username;
    private String hostname;
    private List psis;
    private List apsvrs;
    private String apsvrId;
    private String apsvrName;

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        psiTemplId = null;
        psiTemplName = null;
        username = null;
        hostname = null;
        apsvrId = null;
        apsvrName = null;
        psis = new ArrayList();
        apsvrs = new ArrayList();
        LOGGER.debug("exiting");
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
    	LOGGER.debug("entering");
      ActionErrors actionErrors = new ActionErrors();

      if ((getPsiTemplName() == null) || (getPsiTemplName().length() < 1))
      {
          actionErrors.add(
              "psiTemplName", new ActionMessage(
                  "psiTempl.error.missing.psiTemplName"));
      }
      if ((getHostname() == null) || (getHostname().length() < 1))
      {
          actionErrors.add(
              "hostname", new ActionMessage(
                  "psiTempl.error.missing.hostname"));
      }   
      if ((getUsername() == null) || (getUsername().length() < 1))
      {
          actionErrors.add(
              "username", new ActionMessage(
                  "psiTempl.error.missing.username"));
      }
      if ((getApsvrId() == null) || (getApsvrId().length() < 1))
      {
          actionErrors.add(
              "apsvr", new ActionMessage(
                  "psiTempl.error.missing.apsvr"));
      }        
      LOGGER.debug("exiting");
      return actionErrors;
    }
    
    public List getPsis()
    {
        return psis;
    }

    public void setPsis(List psis)
    {
        this.psis = psis;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getPsiTemplId()
    {
        return psiTemplId;
    }

    public void setPsiTemplId(String psiTemplId)
    {
        this.psiTemplId = psiTemplId;
    }

    public String getPsiTemplName()
    {
        return psiTemplName;
    }

    public void setPsiTemplName(String psiTemplName)
    {
        this.psiTemplName = psiTemplName;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(getPsiTemplId());
    }

		public String getApsvrId() {
			return apsvrId;
		}

		public void setApsvrId(String apsvrId) {
			this.apsvrId = apsvrId;
		}

		public List getApsvrs() {
			return apsvrs;
		}

		public void setApsvrs(List apsvrs) {
			this.apsvrs = apsvrs;
		}

		public String getApsvrName() {
			return apsvrName;
		}

		public void setApsvrName(String apsvrName) {
			this.apsvrName = apsvrName;
		}
}
