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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class USS_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(USS_Form.class);
	private static final long serialVersionUID=1L;

	private int id_uss;
	private int type;
	//private int flags;
	private boolean auth_allowed;
	private boolean non_repudiation_allowed;
	private String nafGroup;
	
	private static List select_uss_type;
	static{
    	select_uss_type = WebConstants.select_uss_type;
	}
	
    public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.type = 0;
    	this.auth_allowed = false;
    	this.non_repudiation_allowed = false;
    	this.nafGroup = null;
 	}
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        return actionErrors;
    }

	public int getId_uss() {
		return id_uss;
	}

	public void setId_uss(int id_uss) {
		this.id_uss = id_uss;
	}

	public boolean isAuth_allowed() {
		return auth_allowed;
	}

	public void setAuth_allowed(boolean auth_allowed) {
		this.auth_allowed = auth_allowed;
	}

	public boolean isNon_repudiation_allowed() {
		return non_repudiation_allowed;
	}

	public void setNon_repudiation_allowed(boolean non_repudiation_allowed) {
		this.non_repudiation_allowed = non_repudiation_allowed;
	}

	public String getNafGroup() {
		return nafGroup;
	}

	public void setNafGroup(String nafGroup) {
		this.nafGroup = nafGroup;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List getSelect_uss_type() {
		return select_uss_type;
	}

	public void setSelect_uss_type(List select_uss_type) {
		this.select_uss_type = select_uss_type;
	}
}
