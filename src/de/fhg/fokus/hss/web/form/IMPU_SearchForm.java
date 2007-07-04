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

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class IMPU_SearchForm extends ActionForm implements Serializable{
	private static final long serialVersionUID=1L;
	
	private String impu_id;
	private String identity;
	private String id_impu_implicitset;
	private int user_state;
	
	private String crtPage;
	private String rowsPerPage;
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
  		
  		this.id_impu_implicitset = null;
  		this.identity = null;
  		this.impu_id = null;
  		crtPage = "1";
  		rowsPerPage = "20";
  	}

	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public int getUser_state() {
		return user_state;
	}
	public void setUser_state(int user_state) {
		this.user_state = user_state;
	}
	public String getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
  	public String getCrtPage() {
		return crtPage;
	}
	public void setCrtPage(String crtPage) {
		this.crtPage = crtPage;
	}
	public String getId_impu_implicitset() {
		return id_impu_implicitset;
	}
	public void setId_impu_implicitset(String id_impu_implicitset) {
		this.id_impu_implicitset = id_impu_implicitset;
	}
	public String getImpu_id() {
		return impu_id;
	}
	public void setImpu_id(String impu_id) {
		this.impu_id = impu_id;
	}
	
}
