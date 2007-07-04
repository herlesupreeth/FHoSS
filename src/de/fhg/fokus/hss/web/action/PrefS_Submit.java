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

package de.fhg.fokus.hss.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.Preferred_SCSCF_Set;
import de.fhg.fokus.hss.db.op.Preferred_SCSCF_Set_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.PrefS_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class PrefS_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(PrefS_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		PrefS_Form form = (PrefS_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id_set = form.getId_set();

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
					
			if (nextAction.equals("save")){
				Preferred_SCSCF_Set preferred_scscf_set;

				if (id_set == -1){
					// create
					preferred_scscf_set = new Preferred_SCSCF_Set();
					preferred_scscf_set.setName(form.getName());
					preferred_scscf_set.setPriority(form.getPriority());
					preferred_scscf_set.setScscf_name(form.getScscf_name());
					
					int max_id_set = Preferred_SCSCF_Set_DAO.get_max_id_set(session);
					preferred_scscf_set.setId_set(max_id_set + 1);
					Preferred_SCSCF_Set_DAO.insert(session, preferred_scscf_set);
					id_set = max_id_set + 1;
					form.setId_set(id_set);
					
				}	
				else{
					// update
					Preferred_SCSCF_Set_DAO.update_all_from_set(session, id_set, form.getName());
				}	
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_set=" + id_set);
			}
			else if (nextAction.equals("refresh")){
				Preferred_SCSCF_Set preferred_scscf_set = (Preferred_SCSCF_Set) Preferred_SCSCF_Set_DAO.get_by_set_ID(session, id_set);

				if (!PrefS_Load.setForm(form, preferred_scscf_set)){
					logger.error("The Preferred-SCSCF-Set with the ID:" + id_set + " was not loaded from database!");
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_set=" + id_set);
			}
			else if (nextAction.equals("delete")){
				Preferred_SCSCF_Set_DAO.delete_set_by_ID(session, id_set);
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("add_scscf")){
				Preferred_SCSCF_Set preferred_scscf_set = new Preferred_SCSCF_Set();
				preferred_scscf_set.setId_set(id_set);
				preferred_scscf_set.setName(form.getName());
				preferred_scscf_set.setPriority(form.getPriority());	
				preferred_scscf_set.setScscf_name(form.getScscf_name());
				Preferred_SCSCF_Set_DAO.insert(session, preferred_scscf_set);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id_set=" + id_set);
			}
			else if (nextAction.equals("delete_scscf")){
				int cnt = Preferred_SCSCF_Set_DAO.get_cnt_for_set(session, id_set); 
				Preferred_SCSCF_Set_DAO.delete_scscf_from_set(session, form.getAssociated_ID());
				
				if (cnt == 1){
					forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
				}
				else{
					forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
					forward = new ActionForward(forward.getPath() +"?id_set=" + id_set);
				}
				
			}
			
			PrefS_Load.prepareForward(session, form, request, id_set);
		}
		catch(DatabaseException e){
			logger.error("Database Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
			
			forward = actionMapping.findForward(WebConstants.FORWARD_FAILURE);
		}
		
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
			
			forward = actionMapping.findForward(WebConstants.FORWARD_FAILURE);
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}
		return forward;
	}
}
