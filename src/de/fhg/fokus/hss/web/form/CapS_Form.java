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
import de.fhg.fokus.hss.db.model.CapabilitiesSet;
import de.fhg.fokus.hss.db.op.CapabilitiesSet_DAO;
import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class CapS_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(CapS_Form.class);
	private static final long serialVersionUID=1L;

	private String name;
	private int id_set;
	
	private int cap_type;
	private List select_cap_type;
	private int id_cap;

	private List select_cap;
	private int associated_ID;
	private String nextAction;
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id_set = -1;
    	this.name = null;
    	this.id_cap = -1;
    	this.cap_type = 0;
    	this.select_cap_type = WebConstants.select_cap_type;
    	this.select_cap = null;
    	this.associated_ID = -1;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();

        	if (nextAction.equals("save")){
                if (name == null || name.equals("")){
                	actionErrors.add("cap_set.error.name", new ActionMessage("cap_set.error.name"));
                }
                if (id_set == -1 && id_cap == -1){
                	actionErrors.add("cap_set.error.id_cap", new ActionMessage("cap_set.error.id_cap"));
                }
                
                
                if (CapabilitiesSet_DAO.test_unused_name(session, name, id_set) == false){
                	actionErrors.add("cap_set.error.duplicate_set_name", new ActionMessage("cap_set.error.duplicate_set_name"));
                }
        	}
        	else if (nextAction.equals("attach_cap")){
        		if (id_cap == -1 || cap_type == -1){
        			actionErrors.add("cap_set.error.provide_cap_and_type", new ActionMessage("cap_set.error.provide_cap_and_type"));
        		}
        		
        		CapabilitiesSet cap_set = CapabilitiesSet_DAO.get_by_Capability_and_Set_ID(session, id_cap, id_set);
        		if (cap_set != null){
        			actionErrors.add("cap_set.error.cap_already_attached", new ActionMessage("cap_set.error.cap_already_attached"));
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

	public int getCap_type() {
		return cap_type;
	}

	public void setCap_type(int cap_type) {
		this.cap_type = cap_type;
	}

	public List getSelect_cap_type() {
		return select_cap_type;
	}

	public void setSelect_cap_type(List select_cap_type) {
		this.select_cap_type = select_cap_type;
	}

	public int getId_cap() {
		return id_cap;
	}

	public void setId_cap(int id_cap) {
		this.id_cap = id_cap;
	}

	public int getId_set() {
		return id_set;
	}

	public void setId_set(int id_set) {
		this.id_set = id_set;
	}

	public List getSelect_cap() {
		return select_cap;
	}

	public void setSelect_cap(List select_cap) {
		this.select_cap = select_cap;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}
	
}
