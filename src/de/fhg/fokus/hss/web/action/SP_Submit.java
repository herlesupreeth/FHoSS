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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.Session;


import de.fhg.fokus.hss.db.model.SP;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.SP_Shared_IFC_Set;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_Shared_IFC_Set_DAO;
import de.fhg.fokus.hss.db.op.Shared_IFC_Set_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.SP_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class SP_Submit extends Action{
	private static Logger logger = Logger.getLogger(SP_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		SP_Form form = (SP_Form) actionForm;
		String nextAction = form.getNextAction();
		int id = form.getId();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
					
			if (id != -1){
				if (SP_Load.testForDelete(session, id)){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
			}			
		
			if (nextAction.equals("save")){
				if (id == -1){
					// create
					SP sp = new SP();
					sp.setName(form.getName());
					sp.setCn_service_auth(form.getCn_service_auth());
					SP_DAO.insert(session, sp);
					
					id = sp.getId();
					form.setId(id);
				}
				else{
					// update
					SP sp = SP_DAO.get_by_ID(session, id);
					sp.setName(form.getName());
					sp.setCn_service_auth(form.getCn_service_auth());
				}
				
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("refresh")){
				SP sp = (SP) SP_DAO.get_by_ID(session, id);
				SP_Load.setForm(form, sp);

				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("delete")){
				SP_DAO.delete_by_ID(session, id);
				forward  = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("attach_ifc")){
				SP_IFC sp_ifc = new SP_IFC();
				sp_ifc.setId_sp(id);
				sp_ifc.setId_ifc(form.getIfc_id());
				sp_ifc.setPriority(form.getSp_ifc_priority());
				SP_IFC_DAO.insert(session, sp_ifc);
				
				// perform refresh
				SP sp = (SP) SP_DAO.get_by_ID(session, id);
				if (!SP_Load.setForm(form, sp)){
					logger.error("The Service Profile withe the ID:" + id + " was not loaded from database!");
				}
				
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("attach_shared_ifc")){
				SP_Shared_IFC_Set sp_shared_ifc = new SP_Shared_IFC_Set();
				sp_shared_ifc.setId_sp(id);
				sp_shared_ifc.setId_shared_ifc_set(form.getShared_ifc_id());
				SP_Shared_IFC_Set_DAO.insert(session, sp_shared_ifc);

				// perform refresh
				SP sp = (SP) SP_DAO.get_by_ID(session, id);
				if (!SP_Load.setForm(form, sp)){
					logger.error("The Service Profile withe the ID:" + id + " was not loaded from database!");
				}
				
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("detach_ifc")){
				SP_IFC_DAO.delete_by_SP_and_IFC_ID(session, id, form.getAssociated_ID());

				// perform refresh
				SP sp = (SP) SP_DAO.get_by_ID(session, id);
				if (!SP_Load.setForm(form, sp)){
					logger.error("The Service Profile withe the ID:" + id + " was not loaded from database!");
				}
				
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("detach_shared_ifc")){
				SP_Shared_IFC_Set_DAO.delete_by_SP_and_Shared_IFC_ID(session, id, form.getAssociated_ID());

				// perform refresh
				SP sp = (SP) SP_DAO.get_by_ID(session, id);
				if (!SP_Load.setForm(form, sp)){
					logger.error("The Service Profile withe the ID:" + id + " was not loaded from database!");
				}
				
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}

			// add parameters to request & refresh select properties

			// set select_ifc & select_shared_ifc
			List select_ifc = null;
			select_ifc = IFC_DAO.get_all(session);
			form.setSelect_ifc(select_ifc);			
			
			List select_shared_ifc = null;
			select_shared_ifc = Shared_IFC_Set_DAO.get_all_Sets(session);	
			form.setSelect_shared_ifc(select_shared_ifc);

			// set the collections of attached ifc & shared_ifc	
			List attached_ifc_list = SP_IFC_DAO.get_all_IFC_by_SP_ID(session, id);
			List attached_shared_ifc_list = SP_Shared_IFC_Set_DAO.get_all_Shared_IFC_by_SP_ID(session, id);

			if (attached_shared_ifc_list != null){
				request.setAttribute("attached_ifc_list", attached_ifc_list);
			}
			else{
				request.setAttribute("attached_ifc_list", new ArrayList());
			}
			
			if (attached_shared_ifc_list != null){
				request.setAttribute("attached_shared_ifc_list", attached_shared_ifc_list);
			}
			else{
				request.setAttribute("attached_shared_ifc_list", new ArrayList());
			}

			if (SP_Load.testForDelete(session, form.getId())){
				request.setAttribute("deleteDeactivation", "false");
			}
			else{
				request.setAttribute("deleteDeactivation", "true");
			}
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
