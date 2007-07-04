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

import de.fhg.fokus.hss.db.model.Shared_IFC_Set;
import de.fhg.fokus.hss.db.op.Shared_IFC_Set_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.S_IFC_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class S_IFC_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(S_IFC_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		S_IFC_Form form = (S_IFC_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id_set = form.getId_set();

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
					
			if (nextAction.equals("save")){
				Shared_IFC_Set shared_ifc_set;

				if (id_set == -1){
					// create
					shared_ifc_set = new Shared_IFC_Set();
					shared_ifc_set.setName(form.getName());
					shared_ifc_set.setPriority(form.getPriority());
					shared_ifc_set.setId_ifc(form.getId_ifc());
					
					int max_id_set = Shared_IFC_Set_DAO.get_max_id_set(session);
					shared_ifc_set.setId_set(max_id_set + 1);
					Shared_IFC_Set_DAO.insert(session, shared_ifc_set);
					id_set = max_id_set + 1;
					form.setId_set(id_set);
					
				}	
				else{
					// update
					Shared_IFC_Set_DAO.update_all_from_set(session, id_set, form.getName());
				}	
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_set=" + id_set);
			}
			else if (nextAction.equals("refresh")){
				Shared_IFC_Set shared_ifc_set = (Shared_IFC_Set) Shared_IFC_Set_DAO.get_by_set_ID(session, id_set);

				if (!S_IFC_Load.setForm(form, shared_ifc_set)){
					logger.error("The Shared IFC Set with the ID:" + id_set + " was not loaded from database!");
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_set=" + id_set);
			}
			else if (nextAction.equals("delete")){
				Shared_IFC_Set_DAO.delete_set_by_ID(session, id_set);
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("attach_ifc")){
				Shared_IFC_Set shared_ifc_set = new Shared_IFC_Set();
				shared_ifc_set.setId_set(id_set);
				shared_ifc_set.setName(form.getName());
				shared_ifc_set.setId_ifc(form.getId_ifc());
				shared_ifc_set.setPriority(form.getPriority());	
				Shared_IFC_Set_DAO.insert(session, shared_ifc_set);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id_set=" + id_set);
			}
			else if (nextAction.equals("detach_ifc")){
				int cnt = Shared_IFC_Set_DAO.get_cnt_for_set(session, id_set); 
				Shared_IFC_Set_DAO.delete_ifc_from_set(session, id_set, form.getAssociated_ID());
				if (cnt == 1){
					forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
				}
				else{
					forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
					forward = new ActionForward(forward.getPath() +"?id_set=" + id_set);
				}
			}
			
			S_IFC_Load.prepareForward(session, form, request, id_set);
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
