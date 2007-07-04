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
import de.fhg.fokus.hss.db.op.Preferred_SCSCF_Set_DAO;
import de.fhg.fokus.hss.db.model.Preferred_SCSCF_Set;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class PrefS_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(PrefS_Form.class);
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private int id_set;
	private int priority;
	private String scscf_name;
	
	private int associated_ID;
	private String nextAction;
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = null;
    	this.id_set = -1;
    	this.priority = 0;
    	this.scscf_name = null;
    	
    	this.nextAction = null;
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
        			actionErrors.add("pref_scscf.error.name", new ActionMessage("pref_scscf.error.name"));
        		}

        		if (id_set == -1){
        			if (scscf_name == null || scscf_name.equals("")){
        				actionErrors.add("pref_scscf.error.scscf_name", new ActionMessage("pref_scscf.error.scscf_name"));	
        			}
        		}
        		if ((name != null && !name.equals(""))){
        			boolean result = Preferred_SCSCF_Set_DAO.test_unused_name(session, name, id_set);
        			if (!result){
        				actionErrors.add("pref_scscf.error.name_used", new ActionMessage("pref_scscf.error.name_used"));		
        			}
        		}
        		
        	}
        	else if (nextAction.equals("add_scscf")){
    			if (scscf_name == null || scscf_name.equals("")){
    				actionErrors.add("pref_scscf.error.scscf_name", new ActionMessage("pref_scscf.error.scscf_name"));	
    			}
    			
    			if (scscf_name != null && !name.equals("")){
        			boolean result = Preferred_SCSCF_Set_DAO.test_unused_scscf_name(session, scscf_name, id_set);
        			if (!result){
        				actionErrors.add("pref_scscf.error.scscf_name_used", new ActionMessage("pref_scscf.error.scscf_name_used"));		
        			}
    			}
    			
    			Preferred_SCSCF_Set preferred_scscf_set = Preferred_SCSCF_Set_DAO.get_by_Priority_and_Set_ID(session, priority, id_set);
    			if (preferred_scscf_set != null){
    				actionErrors.add("pref_scscf.error.duplicate_priority", new ActionMessage("pref_scscf.error.duplicate_priority"));
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

	public int getId_set() {
		return id_set;
	}

	public void setId_set(int id_set) {
		this.id_set = id_set;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getScscf_name() {
		return scscf_name;
	}

	public void setScscf_name(String scscf_name) {
		this.scscf_name = scscf_name;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}
}
