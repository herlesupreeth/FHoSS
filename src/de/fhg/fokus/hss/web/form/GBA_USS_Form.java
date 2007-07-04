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

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.zh.ZhConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class GBA_USS_Form extends ActionForm implements Serializable{
	private static final long serialVersionUID=1L;
	private static Logger logger = Logger.getLogger(GBA_USS_Form.class);
	
	private String identity;
	private int id_impi;
    private int uicc_type;
    private int key_life_time;
    private int default_auth_scheme;
    private transient List ussList;
    
	private String nextAction;
	private int associated_ID;
	private static List select_uicc_type;
	private static List select_zh_auth_scheme;
	
	static{
    	select_uicc_type = WebConstants.select_uicc_type;
    	select_zh_auth_scheme = WebConstants.select_zh_auth_scheme;
	}
    public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id_impi = 0;
    	this.identity = null;
    	this.uicc_type = ZhConstants.UICC_Type_Basic_GBA;
    	this.key_life_time = ZhConstants.Default_Key_Life_Time;
    	this.default_auth_scheme = CxConstants.Auth_Scheme_AKAv1;
    	
    	Factory factory = new Factory() {
            public Object create() {
                return new USS_Form();
            }
    	};

    	this.ussList = LazyList.decorate(new ArrayList(), factory);
 	}
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        return actionErrors;
    }

	public int getKey_life_time() {
		return key_life_time;
	}

	public void setKey_life_time(int key_life_time) {
		this.key_life_time = key_life_time;
	}

	public int getUicc_type() {
		return uicc_type;
	}

	public void setUicc_type(int uicc_type) {
		this.uicc_type = uicc_type;
	}

	public List getUssList() {
		return ussList;
	}

	public void setUssList(List ussList) {
		this.ussList = ussList;
	}

	public int getId_impi() {
		return id_impi;
	}

	public void setId_impi(int id_impi) {
		this.id_impi = id_impi;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public int getDefault_auth_scheme() {
		return default_auth_scheme;
	}

	public void setDefault_auth_scheme(int default_auth_scheme) {
		this.default_auth_scheme = default_auth_scheme;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public List getSelect_uicc_type() {
		return select_uicc_type;
	}

	public List getSelect_zh_auth_scheme() {
		return select_zh_auth_scheme;
	}

	
}
