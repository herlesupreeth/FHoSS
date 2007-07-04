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
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class IFC_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(IFC_Form.class);
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private int profile_part_ind;
	private int id_application_server;
	private int id_tp;

	private List select_profile_part_indicator = WebConstants.select_profile_part_indicator;
	private List select_tp;
	private List select_as;
	private String nextAction;
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = null;
    	this.profile_part_ind = CxConstants.Profile_Part_Indicator_Registered;
    	this.id_application_server = -1;
    	this.id_tp = -1;
    	this.nextAction = null;
    	this.select_as = null;
    	this.select_tp = null;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        
        	if (name == null || name.equals("")){
        		actionErrors.add("ifc.error.name", new ActionMessage("ifc.error.name"));
        	}
        	if (id_application_server == -1){
        		actionErrors.add("ifc.error.invalid_application_server_id", new ActionMessage("ifc.error.invalid_application_server_id"));        		
        	}
        	
        	IFC ifc = IFC_DAO.get_by_Name(session, name);
        	if (ifc != null && ifc.getId() != id){
        		actionErrors.add("ifc.error.duplicate_name", new ActionMessage("ifc.error.duplicate_name"));
        	}
        }
		catch(DatabaseException e){
			logger.error("Database Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}
        
        return actionErrors;
    }
    
    
    // getters & setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public int getId_application_server() {
		return id_application_server;
	}

	public void setId_application_server(int id_application_server) {
		this.id_application_server = id_application_server;
	}

	public int getId_tp() {
		return id_tp;
	}

	public void setId_tp(int id_tp) {
		this.id_tp = id_tp;
	}

	public int getProfile_part_ind() {
		return profile_part_ind;
	}

	public void setProfile_part_ind(int profile_part_ind) {
		this.profile_part_ind = profile_part_ind;
	}

	public List getSelect_profile_part_indicator() {
		return select_profile_part_indicator;
	}

	public void setSelect_profile_part_indicator(List select_profile_part_indicator) {
		this.select_profile_part_indicator = select_profile_part_indicator;
	}

	public List getSelect_as() {
		return select_as;
	}

	public void setSelect_as(List select_as) {
		this.select_as = select_as;
	}

	public List getSelect_tp() {
		return select_tp;
	}

	public void setSelect_tp(List select_tp) {
		this.select_tp = select_tp;
	}

	
}
