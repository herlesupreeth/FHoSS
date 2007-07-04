/*
  *  Copyright (C) 2004-2007 FhG Fokus
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

package de.fhg.fokus.hss.web.form;


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class SPT_Form extends ActionForm implements Serializable{
	private static final long serialVersionUID=1L;
	
	// SPT properties
	private int spt_reg_type;
    private int sptId;
    private String sipMethod;
    private String sipHeader;
    private String sipHeaderContent;
    private String requestUri;
    private String sessionDescLine;
    private String sessionDescContent;
    private int sessionCase;
    private boolean neg;
    private int group;
    private int type;
    private int trigptId;

    private boolean rtype_reg;
    private boolean rtype_re_reg;
    private boolean rtype_de_reg;

    private static List sptTypeList;
	private static ArrayList sipMethodList;
	private static ArrayList directionOfRequestList;
	
	static{
		sptTypeList = WebConstants.select_spt_type;
		sipMethodList = WebConstants.select_spt_method_type;
		directionOfRequestList = WebConstants.select_direction_of_request;
	}
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
		this.sptId = -1;
    	this.neg = false;
    	this.group = 1;
    	this.sipHeader = null;
    	this.sipHeaderContent = null;
    	this.sipMethod = null;
    	this.spt_reg_type = 0;
    	this.requestUri = null;
    	this.sessionDescContent = null;
    	this.sessionDescLine = null;
    	this.sessionCase = 0;
    	
    	//this.rtype = null;
    	this.rtype_reg = false;
    	this.rtype_re_reg = false;
    	this.rtype_de_reg = false;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        // [validation for SPT: to be added]
        return actionErrors;
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

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public List getSptTypeList() {
		return sptTypeList;
	}

	public void setSptTypeList(List sptTypeList) {
		SPT_Form.sptTypeList = sptTypeList;
	}


	public int getSessionCase() {
		return sessionCase;
	}

	public void setSessionCase(int sessionCase) {
		this.sessionCase = sessionCase;
	}

	public String getSessionDescContent() {
		return sessionDescContent;
	}

	public void setSessionDescContent(String sessionDescContent) {
		this.sessionDescContent = sessionDescContent;
	}

	public String getSessionDescLine() {
		return sessionDescLine;
	}

	public void setSessionDescLine(String sessionDescLine) {
		this.sessionDescLine = sessionDescLine;
	}

	public String getSipHeader() {
		return sipHeader;
	}

	public void setSipHeader(String sipHeader) {
		this.sipHeader = sipHeader;
	}

	public String getSipHeaderContent() {
		return sipHeaderContent;
	}

	public void setSipHeaderContent(String sipHeaderContent) {
		this.sipHeaderContent = sipHeaderContent;
	}

	public String getSipMethod() {
		return sipMethod;
	}

	public void setSipMethod(String sipMethod) {
		this.sipMethod = sipMethod;
	}

	public int getSpt_reg_type() {
		return spt_reg_type;
	}

	public void setSpt_reg_type(int spt_reg_type) {
		this.spt_reg_type = spt_reg_type;
	}


	public int getSptId() {
		return sptId;
	}

	public void setSptId(int sptId) {
		this.sptId = sptId;
	}

	public int getTrigptId() {
		return trigptId;
	}

	public void setTrigptId(int trigptId) {
		this.trigptId = trigptId;
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
		SPT_Form.sipMethodList = sipMethodList;
	}
 

	public boolean isRtype_de_reg() {
		return rtype_de_reg;
	}

	public void setRtype_de_reg(boolean rtype_de_reg) {
		this.rtype_de_reg = rtype_de_reg;
	}

	public boolean isRtype_re_reg() {
		return rtype_re_reg;
	}

	public void setRtype_re_reg(boolean rtype_re_reg) {
		this.rtype_re_reg = rtype_re_reg;
	}

	public boolean isRtype_reg() {
		return rtype_reg;
	}

	public void setRtype_reg(boolean rtype_reg) {
		this.rtype_reg = rtype_reg;
	}

	public ArrayList getDirectionOfRequestList() {
		return directionOfRequestList;
	}

	public void setDirectionOfRequestList(ArrayList directionOfRequestList) {
		SPT_Form.directionOfRequestList = directionOfRequestList;
	}
	
}
