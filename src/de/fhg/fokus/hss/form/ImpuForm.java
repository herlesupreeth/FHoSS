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

import de.fhg.fokus.hss.model.Impu;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpuForm extends HssForm
{
    private static ArrayList userStatusList;

    static
    {
        userStatusList = new ArrayList();
        userStatusList.add(
            new Tupel(Impu.USER_STATUS_NOT_REGISTERED, "NOT_REGISTERED"));
        userStatusList.add(
            new Tupel(Impu.USER_STATUS_UNREGISTERED, "UNREGISTERED"));
        userStatusList.add(
            new Tupel(Impu.USER_STATUS_REGISTERED, "REGISTERED"));
    }

    private String sipUrl = null;
    private String telUrl = null;
    private String impuId = null;
    private String impiId = null;
    private boolean barred = false;
    private String userStatusId;
    private String svpId;
    private List svps;
    
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        sipUrl = null;
        telUrl = null;
        impuId = null;
        impiId = null;
        barred = false;
        userStatusId = null;
        svps = new ArrayList();
        svpId = null;
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        ActionErrors actionErrors = new ActionErrors();

        if ((getSipUrl() == null) || (getSipUrl().length() < 1))
        {
            actionErrors.add(
                "sipUrl", new ActionMessage("impu.error.missing.sipUrl"));
        }
        if ((getSipUrl() != null) || (getSipUrl().length() > 1))
        {
        	if(getSipUrl().toLowerCase().startsWith("sip:") == false){
            actionErrors.add(
                "sipUrl", new ActionMessage("impu.error.format.sipUrl"));
        	}
        }
        

        return actionErrors;
    }

    public Integer getPrimaryKey()
    {
        return getPrimaryKey(getImpuId());
    }

    public String getImpuId()
    {
        return impuId;
    }

    public void setImpuId(String impuId)
    {
        this.impuId = impuId;
    }

    public String getSipUrl()
    {
        return sipUrl;
    }

    public void setSipUrl(String sipUrl)
    {
        this.sipUrl = sipUrl;
    }

    public String getImpiId()
    {
        return impiId;
    }

    public void setImpiId(String impiId)
    {
        this.impiId = impiId;
    }

    public boolean isBarred()
    {
        return barred;
    }

    public void setBarred(boolean barred)
    {
        this.barred = barred;
    }

    public String getTelUrl()
    {
        return telUrl;
    }

    public void setTelUrl(String telUrl)
    {
        this.telUrl = telUrl;
    }

    public String getUserStatusId()
    {
        return userStatusId;
    }

    public void setUserStatusId(String userStatusId)
    {
        this.userStatusId = userStatusId;
    }

    public ArrayList getUserStatusList()
    {
        return userStatusList;
    }

    public void setUserStatusList(ArrayList userStatusList)
    {
        // void
    }
    
		public String getSvpId() {
			return svpId;
		}

		public void setSvpId(String svpId) {
			this.svpId = svpId;
		}

		public List getSvps() {
			return svps;
		}

		public void setSvps(List svps) {
			this.svps = svps;
		}
}
