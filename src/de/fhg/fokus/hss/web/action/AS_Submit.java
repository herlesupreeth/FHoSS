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


import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.AS_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class AS_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(AS_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		AS_Form form = (AS_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id = form.getId();
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			// for all the actions we test if the current element can be deleted or not
			if (id != -1){
				if (AS_Load.testForDelete(session, form.getId())){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
			}			
			
			if (nextAction.equals("save")){
				ApplicationServer as;

				if (id == -1){
					// create
					as = new ApplicationServer();
				}	
				else{
					// update
					as = ApplicationServer_DAO.get_by_ID(session, id);
				}	
				
				// make the changes
				as.setName(form.getName());
				as.setServer_name(form.getServer_name());
				as.setDefault_handling(form.getDefault_handling());
				as.setDiameter_address(form.getDiameter_address());
				
				if (form.getService_info() != null){
					as.setService_info(form.getService_info());
				}
				
				as.setRep_data_size_limit(form.getRep_data_size_limit());
				as.setUdr(form.isUdr()?1:0);
				as.setPur(form.isPur()?1:0);
				as.setSnr(form.isSnr()?1:0);
				
				as.setUdr_rep_data(form.isUdr_rep_data()?1:0);
				as.setUdr_impu(form.isUdr_impu()?1:0);
				as.setUdr_ims_user_state(form.isUdr_ims_user_state()?1:0);
				as.setUdr_scscf_name(form.isUdr_scscf_name()?1:0);
				as.setUdr_ifc(form.isUdr_ifc()?1:0);
				as.setUdr_location(form.isUdr_location()?1:0);
				as.setUdr_user_state(form.isUdr_user_state()?1:0);
				as.setUdr_charging_info(form.isUdr_charging_info()?1:0);
				as.setUdr_msisdn(form.isUdr_msisdn()?1:0);
				as.setUdr_psi_activation(form.isUdr_psi_activation()?1:0);
				as.setUdr_dsai(form.isUdr_dsai()?1:0);
				as.setUdr_aliases_rep_data(form.isUdr_aliases_rep_data()?1:0);
				as.setPur_rep_data(form.isPur_rep_data()?1:0);
				as.setPur_psi_activation(form.isPur_psi_activation()?1:0);
				as.setPur_dsai(form.isPur_dsai()?1:0);
				as.setPur_aliases_rep_data(form.isPur_aliases_rep_data()?1:0);
				as.setSnr_rep_data(form.isSnr_rep_data()?1:0);
				as.setSnr_impu(form.isSnr_rep_data()?1:0);
				as.setSnr_impu(form.isSnr_impu()?1:0);
				as.setSnr_ims_user_state(form.isSnr_ims_user_state()?1:0);
				as.setSnr_scscf_name(form.isSnr_scscf_name()?1:0);
				as.setSnr_ifc(form.isSnr_ifc()?1:0);
				as.setSnr_psi_activation(form.isSnr_psi_activation()?1:0);
				as.setSnr_dsai(form.isSnr_dsai()?1:0);
				as.setSnr_aliases_rep_data(form.isSnr_rep_data()?1:0);
				as.setInclude_register_request(form.isInclude_register_request()?1:0);
				as.setInclude_register_response(form.isInclude_register_response()?1:0);
				
				if (id == -1){
					ApplicationServer_DAO.insert(session, as);
					id = as.getId();
					form.setId(id);
				}
				else{
					ApplicationServer_DAO.update(session, as);
				}

				// make a refresh for the Application Server fields
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			
			else if (nextAction.equals("refresh")){
				ApplicationServer as = (ApplicationServer) ApplicationServer_DAO.get_by_ID(session, id);

				if (!AS_Load.setForm(form, as)){
					logger.error("The AS withe the ID:" + id + " was not loaded from database!");
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
				
			}
			else if (nextAction.equals("delete")){
				ApplicationServer_DAO.delete_by_ID(session, id);
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("detach_ifc")){
				IFC ifc = IFC_DAO.get_by_ID(session, form.getAssociated_ID());
				if (ifc != null){
					ifc.setId_application_server(-1);
					IFC_DAO.update(session, ifc);
				}
				
				// perform a refresh
				ApplicationServer as = ApplicationServer_DAO.get_by_ID(session, id);
				if (!AS_Load.setForm(form, as)){
					logger.error("The AS withe the ID:" + id + " was not loaded from database!");
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			else if (nextAction.equals("attach_ifc")){
				
				IFC ifc = IFC_DAO.get_by_ID(session, form.getIfc_id());
				if (ifc != null){
					ifc.setId_application_server(id);
					IFC_DAO.update(session, ifc);
				}

				// perform a refresh
				ApplicationServer as = ApplicationServer_DAO.get_by_ID(session, id);
				if (!AS_Load.setForm(form, as)){
					logger.error("The AS withe the ID:" + id + " was not loaded from database!");
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			
			// refresh the data structures
			List select_ifc = IFC_DAO.get_all(session);
			form.setSelect_ifc(select_ifc);
			
			List attached_ifc_list = null;
			attached_ifc_list = IFC_DAO.get_all_by_AS_ID(session, id);
			if (attached_ifc_list != null)
				request.setAttribute("attached_ifc_list", attached_ifc_list);
			else	
				request.setAttribute("attached_ifc_list", new ArrayList());

			if (id != -1){
				if (AS_Load.testForDelete(session, id)){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
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
