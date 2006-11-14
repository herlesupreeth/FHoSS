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
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PsiForm extends ImpuSubSelectForm
{
    private static final Logger LOGGER = Logger.getLogger(PsiForm.class);
    private String psiId;
    private String psiName;
    private String wildcard;
    private List psiTempls;
    private String psiTemplId;
    private String impuName;

    
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        psiId = null;
        psiName = null;
        wildcard = null;
        psiTempls = new ArrayList();
        psiTemplId = null;
        impuName = null;
        super.reset(arg0, arg1);
        LOGGER.debug("exiting");
    }

    
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
      LOGGER.debug("entering");

      ActionErrors actionErrors = new ActionErrors();

      if ((getPsiName() == null) || (getPsiName().length() < 1))
      {
          actionErrors.add(
              "psiName", new ActionMessage("psi.error.missing.psiName"));
      }
      if ((getPsiTemplId() == null) || (getPsiTemplId().length() < 1))
      {
          actionErrors.add(
              "psiTemplId", new ActionMessage("psi.error.missing.template"));
      }
      if ((getWildcard() == null) || (getWildcard().length() < 1))
      {
          actionErrors.add(
              "wildcard", new ActionMessage("psi.error.missing.wildcard"));
      }
      LOGGER.debug("exiting");

      return actionErrors;
    }
    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(getPsiId());
    }

   
    public String getPsiId()
    {
        return psiId;
    }

    public void setPsiId(String psiId)
    {
        this.psiId = psiId;
    }

    public String getPsiName()
    {
        return psiName;
    }

    public void setPsiName(String psiName)
    {
        this.psiName = psiName;
    }

    public String getPsiTemplId()
    {
        return psiTemplId;
    }

    public void setPsiTemplId(String psiTemplId)
    {
        this.psiTemplId = psiTemplId;
    }

    public List getPsiTempls()
    {
        return psiTempls;
    }

    public void setPsiTempls(List psiTempls)
    {
        this.psiTempls = psiTempls;
    }

    public String getWildcard()
    {
        return wildcard;
    }

    public void setWildcard(String wildcard)
    {
        this.wildcard = wildcard;
    }


		public String getImpuName() {
			return impuName;
		}


		public void setImpuName(String impuName) {
			this.impuName = impuName;
		}
}
