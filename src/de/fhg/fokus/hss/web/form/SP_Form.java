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

import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.SP;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.SP_Shared_IFC_Set;
import de.fhg.fokus.hss.db.op.SP_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_Shared_IFC_Set_DAO;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class SP_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(SP_Form.class);
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private int cn_service_auth;

	private int ifc_id;
	private int shared_ifc_id;
	private int sp_ifc_priority;
	private List select_ifc;
	private List select_shared_ifc;
	private int associated_ID;
	private String nextAction;
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = null;
    	this.cn_service_auth = 0;
    	
    	this.ifc_id = -1;
    	this.shared_ifc_id = -1;
    	this.select_ifc = null;
    	this.select_shared_ifc = null;
    	this.associated_ID = -1;
    	this.nextAction = null;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

    	boolean dbException = false;
    	try{
    		Session session = HibernateUtil.getCurrentSession();
    		HibernateUtil.beginTransaction();

    		if (nextAction.equals("save")){
    	        if (name == null || name.equals("")){
    	        	actionErrors.add("sp.error.name", new ActionMessage("sp.error.name"));
    	        }

    			// test for SP Name duplication
    			SP sp = SP_DAO.get_by_Name(session, name);
    			if (sp != null && sp.getId() != id){
    				actionErrors.add("sp.error.duplicate_name", new ActionMessage("sp.error.duplicate_name"));
    			}
    			
    			if (cn_service_auth  < 0){
    				actionErrors.add("sp.error.invalid_cn_service_auth", new ActionMessage("sp.error.invalid_cn_service_auth"));
    			}
    		}
    		else if (nextAction.equals("attach_ifc")){
        		// validation regarding the associated IFCs
    			if (sp_ifc_priority < 0){
    				actionErrors.add("sp.error.invalid_priority_value", new ActionMessage("sp.error.invalid_priority_value"));
    			}
    			
    			if (ifc_id == -1){
    				actionErrors.add("sp.error.invalid_ifc_selection", new ActionMessage("sp.error.invalid_ifc_selection"));
    			}
    			// check if this association already exists
    			SP_IFC sp_ifc = SP_IFC_DAO.get_by_SP_and_IFC_ID(session, id, ifc_id);
    			if (sp_ifc != null){
    				actionErrors.add("sp.error.duplicate_ifc_association", new ActionMessage("sp.error.duplicate_ifc_association"));
    			}
    	
    			sp_ifc = SP_IFC_DAO.get_by_SP_ID_and_Priority(session, id, sp_ifc_priority);
    			if (sp_ifc != null){
    				actionErrors.add("sp.error.duplicate_sp_ifc_priority", new ActionMessage("sp.error.duplicate_sp_ifc_priority"));
    			}
    		}
    		else if (nextAction.equals("attach_shared_ifc")){
    			
    			if (shared_ifc_id == -1){
    				actionErrors.add("sp.error.invalid_shared_ifc_id", new ActionMessage("sp.error.invalid_shared_ifc_id"));
    			}
    			
    			SP_Shared_IFC_Set shared_ifc_set = null;
    			try{
    				shared_ifc_set = 
    					SP_Shared_IFC_Set_DAO.get_by_SP_and_Shared_IFC_Set_ID(session, id, shared_ifc_id);
    			}
    			catch(org.hibernate.NonUniqueResultException e){
    				logger.error("Query did not returned an unique result! You have a duplicate in the database!");
    				e.printStackTrace();
    			}
    			
    			if (shared_ifc_set != null){
    				actionErrors.add("sp.error.duplicate_shared_ifc_association", new ActionMessage("sp.error.duplicate_shared_ifc_association"));
    			}    			
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
	public int getCn_service_auth() {
		return cn_service_auth;
	}

	public void setCn_service_auth(int cn_service_auth) {
		this.cn_service_auth = cn_service_auth;
	}

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

	public int getIfc_id() {
		return ifc_id;
	}

	public void setIfc_id(int ifc_id) {
		this.ifc_id = ifc_id;
	}

	public List getSelect_ifc() {
		return select_ifc;
	}

	public void setSelect_ifc(List select_ifc) {
		this.select_ifc = select_ifc;
	}

	public int getSp_ifc_priority() {
		return sp_ifc_priority;
	}

	public void setSp_ifc_priority(int sp_ifc_priority) {
		this.sp_ifc_priority = sp_ifc_priority;
	}

	public List getSelect_shared_ifc() {
		return select_shared_ifc;
	}

	public void setSelect_shared_ifc(List select_shared_ifc) {
		this.select_shared_ifc = select_shared_ifc;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}

	public int getShared_ifc_id() {
		return shared_ifc_id;
	}

	public void setShared_ifc_id(int shared_ifc_id) {
		this.shared_ifc_id = shared_ifc_id;
	}
	
}
