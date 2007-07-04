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


import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.Capability;
import de.fhg.fokus.hss.db.op.Capability_DAO;
import de.fhg.fokus.hss.web.form.Cap_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class Cap_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(Cap_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		Cap_Form form = (Cap_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id = form.getId();
		boolean new_created = false;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			// all the possible actions
			if (nextAction.equals("save")){
				Capability cap;

				if (id == -1){
					// create
					cap = new Capability();
				}	
				else{
					// update
					cap = Capability_DAO.get_by_ID(session, id);
					if (cap == null){
						cap = new Capability();
						new_created = true;
					}
				}	
				
				// make the changes
				cap.setName(form.getName());
				if (form.getId() != -1){
					cap.setId(form.getId());
				}
				
				if (id == -1 || new_created == true){
					Capability_DAO.insert(session, cap);
					if (cap.getId() == 0){
						cap = Capability_DAO.get_by_Name(session, form.getName());
					}
					id = cap.getId();
					form.setId(id);
				}
				else{
					Capability_DAO.update(session, cap);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + id);
			}
			else if (nextAction.equals("refresh")){
				Capability cap = (Capability) Capability_DAO.get_by_ID(session, id);

				if (!Cap_Load.setForm(form, cap)){
					logger.error("The CS withe the ID:" + id + " was not loaded from database!");
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + id);
				
			}
			else if (nextAction.equals("delete")){
				Capability_DAO.delete_by_ID(session, form.getId());
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			Cap_Load.prepareForward(session, form, request, id);
		
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
