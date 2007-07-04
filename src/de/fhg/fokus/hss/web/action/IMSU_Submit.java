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


import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.IMSU_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IMSU_Submit extends Action{
	private static Logger logger = Logger.getLogger(IMSU_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		IMSU_Form form = (IMSU_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id = form.getId();			
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
					
			if (nextAction.equals("save")){
				IMSU imsu = null;
				if (id == -1){
					// create
					imsu = new IMSU();
				}
				else{
					// update
					imsu = IMSU_DAO.get_by_ID(session, id);
				}
				
				// make the changes
				imsu.setName(form.getName());
				imsu.setDiameter_name(form.getDiameter_name());
				imsu.setScscf_name(form.getScscf_name());
				imsu.setId_capabilities_set(form.getId_capabilities_set());
				imsu.setId_preferred_scscf_set(form.getId_preferred_scscf());
				
				if (id == -1){
					IMSU_DAO.insert(session, imsu);
					form.setId(imsu.getId());
					id = imsu.getId();
				}
				else{
					IMSU_DAO.update(session, imsu);
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("refresh")){
				IMSU imsu = IMSU_DAO.get_by_ID(session, id);
				IMSU_Load.setForm(form, imsu);
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("delete")){
				IMSU_DAO.delete_by_ID(session, form.getId());
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("delete_impi")){
				IMPI impi = IMPI_DAO.get_by_ID(session, form.getAssociated_ID());
				
				if (impi != null){
					impi.setId_imsu(-1);
					IMPI_DAO.update(session, impi);
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
			}
			
			else if (nextAction.equals("add_impi")){
				IMPI impi = IMPI_DAO.get_by_Identity(session, form.getImpi_identity());
				if (impi != null){
					impi.setId_imsu(id);
					IMPI_DAO.update(session, impi);
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
		
			if (id != -1){
				if (IMSU_Load.testForDelete(session, form.getId())){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
			}			
			
			IMSU_Load.prepareForward(session, form, request, id);
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
