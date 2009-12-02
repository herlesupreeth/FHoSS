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
import java.util.Iterator;
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


import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.SPT;
import de.fhg.fokus.hss.db.model.TP;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.SPT_DAO;
import de.fhg.fokus.hss.db.op.TP_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.SPT_Form;
import de.fhg.fokus.hss.web.form.TP_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class TP_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(TP_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		TP_Form form = (TP_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id = form.getId();

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			if (id != -1){
				if (TP_Load.testForDelete(session, id)){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
			}			
			
			if (nextAction.equals("save")){
				TP tp;
				if (id == -1){
					// create
					tp = new TP();
				}	
				else{
					// update
					tp = TP_DAO.get_by_ID(session, id);
				}	
				
				// make the changes
				tp.setName(form.getName());
				tp.setCondition_type_cnf(form.getCondition_type_cnf());
				
				if (id == -1){
					TP_DAO.insert(session, tp);
					id = tp.getId();
					form.setId(id);
				}
				else{
					TP_DAO.update(session, tp);
				}

				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("refresh")){
				TP tp = (TP) TP_DAO.get_by_ID(session, id);

				if (!TP_Load.setForm(form, tp)){
					logger.error("The TP with the ID:" + id + " was not loaded from database!");
				}
				// reload SPTs
				List spt_Form_List = TP_Load.getSpts(session, id);
				form.setSpts(spt_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("delete")){
				TP_DAO.delete_by_ID(session, id);
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("attach_ifc")){
				IFC ifc = IFC_DAO.get_by_ID(session, form.getIfc_id());
				if (ifc != null){
					ifc.setId_tp(id);
					IFC_DAO.update(session, ifc);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			else if (nextAction.equals("detach_ifc")){
				IFC ifc = IFC_DAO.get_by_ID(session, form.getAssociated_ID());
				if (ifc != null){
					ifc.setId_tp(-1);
					IFC_DAO.update(session, ifc);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}			
			else if (nextAction.equals("attach_spt")){
				if ((form.getGroup() != -1) && (form.getType() != -1)){
					// add new SPT
					
					SPT spt = new SPT();
					spt.setGrp(form.getGroup());
					spt.setType(form.getType());
					spt.setId_tp(id);
					spt.setCondition_negated(0);

					switch (form.getType()){
						case CxConstants.SPT_Type_RequestURI:
							spt.setRequesturi("");
							break;

						case CxConstants.SPT_Type_Method:
							spt.setMethod("INVITE");
							break;

						case CxConstants.SPT_Type_SessionCase:
							spt.setSession_case(0);
							break;

						case CxConstants.SPT_Type_SessionDescription:
							spt.setSdp_line("");
							spt.setSdp_line_content("");
							break;

						case CxConstants.SPT_Type_SIPHeader:
							spt.setHeader("");
							spt.setHeader_content("");
							break;
					}
					SPT_DAO.insert(session, spt);

					// reload SPTs
					List spt_Form_List = TP_Load.getSpts(session, id);
					form.setSpts(spt_Form_List);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
				
			}
			else if (nextAction.equals("save_spt")){
				if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){	
					saveSpts(session, form, id);
				}
				// reload SPTs
				List spt_Form_List = TP_Load.getSpts(session, id);
				form.setSpts(spt_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			else if (nextAction.equals("delete_spt")){
				SPT_DAO.delete_by_ID(session, form.getAssociated_ID());
				// reload SPTs
				List spt_Form_List = TP_Load.getSpts(session, id);
				form.setSpts(spt_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id=" + id);
			}
			
			TP_Load.prepareForward(session, form, request, id);
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
	
		
	private void saveSpts(Session session, TP_Form form, int id_tp){
				
		Iterator itSptForms = form.getSpts().iterator();

		int newGroupId = -1;
		int formGroupId = -1;
		SPT spt = null;
		SPT_Form sptForm = null;
		

		while (itSptForms.hasNext()){
			
			// perform update on existing spts
			
			sptForm = (SPT_Form) itSptForms.next();
			int sptID = sptForm.getSptId();
			spt = SPT_DAO.get_by_ID(session, sptID);

			if (spt == null)
				continue;
				
			switch (spt.getType()){

					case CxConstants.SPT_Type_RequestURI:
						spt.setRequesturi(sptForm.getRequestUri());
						break;

					case CxConstants.SPT_Type_Method:
						spt.setMethod(sptForm.getSipMethod());
						break;

					case CxConstants.SPT_Type_SessionCase:
						spt.setSession_case(sptForm.getSessionCase());
						break;

					case CxConstants.SPT_Type_SessionDescription:
						spt.setSdp_line(sptForm.getSessionDescLine());
						spt.setSdp_line_content(sptForm.getSessionDescContent());
						break;

					case CxConstants.SPT_Type_SIPHeader:
						spt.setHeader(sptForm.getSipHeader());
						spt.setHeader_content(sptForm.getSipHeaderContent());
						break;
				}
				
				
				if (formGroupId != sptForm.getGroup()){
					formGroupId = sptForm.getGroup();
					newGroupId++;
				}

				spt.setGrp(newGroupId);
				spt.setCondition_negated(sptForm.isNeg()?1:0);
				
				int reg_type = spt.generateRegistrationType(sptForm.isRtype_reg(), sptForm.isRtype_re_reg(), 
						sptForm.isRtype_de_reg());
				spt.setRegistration_type(reg_type);
				SPT_DAO.update(session, spt);
		}// while

	}
	
}
