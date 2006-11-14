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

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.fhg.fokus.hss.model.SptBO;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class SptForm extends HssForm
{
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(SptForm.class);
    private String sptId;
    private String sipMethod;
    private String sipHeader;
    private String sipHeaderContent;
    private String requestUri;
    private String sessionDescLine;
    private String sessionDescContent;
    private String sessionCase;
    private boolean neg;
    private int group;
    private int type;
    private boolean delete = false;
    private int trigptId;

    private static ArrayList sipMethodList;
    
    static {
    	sipMethodList = new ArrayList();
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.INVITE, "INVITE"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.ACK, "ACK"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.INFO, "INFO"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.BYE, "BYE"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.CANCEL, "CANCEL"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.MESSAGE, "MESSAGE"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.REGISTER, "REGISTER"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.OPTION, "OPTION"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.SUBSCRIBE, "SUBSCRIBE"));
    	sipMethodList.add(new Tupel(SptBO.SIP_METHOD.PUBLISH, "PUBLISH"));
    }
    
    public int getTrigptId() {
			return trigptId;
		}

		public void setTrigptId(int trigptId) {
			this.trigptId = trigptId;
		}

		public String getRequestUri()
    {
        return requestUri;
    }

    public void setRequestUri(String requestUri)
    {
        this.requestUri = requestUri;
    }

    public String getSessionCase()
    {
        return sessionCase;
    }

    public void setSessionCase(String sessionCase)
    {
        this.sessionCase = sessionCase;
    }

    public String getSessionDescLine()
    {
        return sessionDescLine;
    }

    public void setSessionDescLine(String sessionDesc)
    {
        this.sessionDescLine = sessionDesc;
    }

    public String getSipHeader()
    {
        return sipHeader;
    }

    public void setSipHeader(String sipHeader)
    {
        this.sipHeader = sipHeader;
    }

    public String getSipMethod()
    {
        return sipMethod;
    }

    public void setSipMethod(String sipMethod)
    {
        this.sipMethod = sipMethod;
    }

    public String getSptId()
    {
        return sptId;
    }

    public void setSptId(String sptId)
    {
        this.sptId = sptId;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        return getPrimaryKey(getSptId());
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        sptId = null;
        sessionCase = "";
        sessionDescLine = null;
        sipHeader = null;
        sipMethod = "";
        requestUri = null;
        sipHeaderContent = null;
        delete = false;
        neg = false;
        group = 0;
        trigptId = -1;
        LOGGER.debug("exiting");
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");

        ActionErrors actionErrors = new ActionErrors();
        
        if (
            (
                    (getSessionDescContent() != null)
                    && (getSessionDescContent().length() > 0)
                )
                && (
                    (getSessionDescLine() == null)
                    || (getSessionDescLine().length() < 1)
                ))
        {
            actionErrors.add(
                "sessionDescContent", new ActionMessage(
                    "spt.error.sessionDesc"));
        }

        if (
            (
                    (getSessionDescContent() == null)
                    || (getSessionDescContent().length() < 1)
                )
                && (
                    (getSessionDescLine() != null)
                    && (getSessionDescLine().length() > 0)
                ))
        {
            actionErrors.add(
                "sessionDescContent", new ActionMessage(
                    "spt.error.sessionDesc"));
        }

        if (
            ((getSipHeader() == null) || (getSipHeader().length() < 1))
                && (
                    (getSipHeaderContent() != null)
                    && (getSipHeaderContent().length() > 0)
                ))
        {
            actionErrors.add(
                "sipHeader", new ActionMessage("spt.error.sipHeader"));
        }

        if (
            (
                    (getSipHeaderContent() == null)
                    || (getSipHeaderContent().length() < 1)
                )
                && ((getSipHeader() != null) && (getSipHeader().length() > 0)))
        {
            actionErrors.add(
                "sipHeader", new ActionMessage("spt.error.sipHeader"));
        }

        LOGGER.debug("exiting");

        return actionErrors;
    }

    public String getSipHeaderContent()
    {
        return sipHeaderContent;
    }

    public void setSipHeaderContent(String sipHeaderContent)
    {
        this.sipHeaderContent = sipHeaderContent;
    }

    public boolean isDelete()
    {
        return delete;
    }

    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }

    public String getSessionDescContent()
    {
        return sessionDescContent;
    }

    public void setSessionDescContent(String sessionDescContent)
    {
        this.sessionDescContent = sessionDescContent;
    }

		public int getGroup() {
			return group;
		}

		public void setGroup(int group) {
			this.group = group;
		}

		public boolean isNeg() {
			return neg;
		}

		public void setNeg(boolean neg) {
			this.neg = neg;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public ArrayList getSipMethodList() {
			return sipMethodList;
		}

		public void setSipMethodList(ArrayList sipMethodList) {
			// doNothing();
		}
}
