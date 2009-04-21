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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.hibernate.Session;


import de.fhg.fokus.hss.auth.HexCodec;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.CxEvents;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.IMPI_Form;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.zh.ZhConstants;


/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class IMPI_Submit extends Action{
	private static Logger logger = Logger.getLogger(IMPI_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		IMPI_Form form = (IMPI_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id = form.getId();
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (nextAction.equals("save")){
				int auth_scheme;
				IMPI impi;
				
				if (id == -1){
					// create
					impi = new IMPI();
					// set default values for the Zh variables 
					impi.setZh_default_auth_scheme(CxConstants.Auth_Scheme_AKAv1);
					impi.setZh_key_life_time(ZhConstants.Default_Key_Life_Time);
					impi.setZh_uicc_type(ZhConstants.UICC_Type_Basic_GBA);
				}
				else{
					// update
					impi = IMPI_DAO.get_by_ID(session, id);
				}
				auth_scheme = IMPI.generateAuthScheme(form.isAka1(), form.isAka2(), form.isMd5(), form.isDigest(), form.isSip_digest(),
                                    form.isHttp_digest(), form.isEarly(), form.isNass_bundle(), form.isAll());	
				
				impi.setIdentity(form.getIdentity());
				if (form.getSecretKey().length()==32) 
					impi.setK(HexCodec.decode(form.getSecretKey()));
				else
					impi.setK(form.getSecretKey().getBytes());
				impi.setAuth_scheme(auth_scheme);
				impi.setIp(form.getIp());
				impi.setAmf(HexCodec.decode(form.getAmf()));
				impi.setOp(HexCodec.decode(form.getOp()));
				impi.setSqn(form.getSqn());
				impi.setId_imsu(form.getId_imsu());
				impi.setDefault_auth_scheme(form.getDefault_auth_scheme());
				impi.setLine_identifier(form.getLine_identifier());
				if (id == -1){
					if (form.getAlready_assigned_imsu_id() > 0){
						impi.setId_imsu(form.getAlready_assigned_imsu_id());
						form.setAlready_assigned_imsu_id(-1);
					}
					
					IMPI_DAO.insert(session, impi);
					id = impi.getId();
					form.setId(id);
				}
				else{
					IMPI_DAO.update(session, impi);
				}
				
				if ((auth_scheme & 255) == 255){
					form.setAll(true);
					form.setAka1(false);
					form.setAka2(false);
					form.setMd5(false);
					form.setDigest(false);
					form.setHttp_digest(false);
					form.setEarly(false);
					form.setNass_bundle(false);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
				
			}
			else if (nextAction.equals("refresh")){
				IMPI impi = (IMPI) IMPI_DAO.get_by_ID(session, id);
				//List associated_IMPUs = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI_ID(session, id);
				
				IMPI_Load.setForm(form, impi);
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
				
			}
			else if (nextAction.equals("delete")){
				IMPI_DAO.delete_by_ID(session, form.getId());
				forward = actionMapping.findForward(WebConstants.FORWARD_DELETE);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("add_imsu")){
				IMPI impi = (IMPI) IMPI_DAO.get_by_ID(session, id);
				IMSU imsu = IMSU_DAO.get_by_Name(session, form.getImsu_name());
				
				if (imsu != null){
					impi.setId_imsu(imsu.getId());
					IMPI_DAO.update(session, impi);
				}
				else{
					ActionMessages actionMessages = new ActionMessages();
					actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage("impi.error.associated_imsu_not_found"));
					saveMessages(request, actionMessages);
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}			
			else if (nextAction.equals("delete_associated_IMSU")){
				IMPI impi = IMPI_DAO.get_by_ID(session, id);
				impi.setId_imsu(null);
				IMPI_DAO.update(session, impi);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("add_impu")){
				IMPU impu = IMPU_DAO.get_by_Identity(session, form.getImpu_identity());	
				if (impu != null){
					List listIMPUs = IMPU_DAO.get_all_from_set(session, impu.getId_implicit_set());
					if (listIMPUs != null){
						for (int i = 0; i < listIMPUs.size(); i++){
							IMPU crtIMPU = (IMPU) listIMPUs.get(i);
							IMPI_IMPU_DAO.insert(session, form.getId(), crtIMPU.getId(), CxConstants.IMPU_user_state_Not_Registered);		
						}
					}
				}
				else{
					ActionMessages actionMessages = new ActionMessages();
					actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage("impi.error.associated_impu_not_found"));
					saveMessages(request, actionMessages);
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("delete_associated_IMPU")){
				IMPU impu = IMPU_DAO.get_by_ID(session, form.getAssociated_ID());	
				
				if (impu != null){
					List listIMPUs = IMPU_DAO.get_all_from_set(session, impu.getId_implicit_set());
					if (listIMPUs != null){
						for (int i = 0; i < listIMPUs.size(); i++){
							IMPU crtIMPU = (IMPU) listIMPUs.get(i);
							IMPI_IMPU_DAO.delete_by_IMPI_and_IMPU_ID(session, id, crtIMPU.getId());
						}
					}
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("ppr")){
				// PPR
				logger.info("We are sending a PPR message for the user!");
				
				// if a PPR is to be sent for an IMPI, we are preparing more PPR messages, corresponding to each different implicit registration set!
				
				List implicit_sets_ids = IMPI_DAO.get_all_registered_implicit_sets(session, form.getId());
				int grp = CxEvents_DAO.get_max_grp(session);
				
				if (implicit_sets_ids != null && implicit_sets_ids.size() > 0){
					int id_implicit_set;
					for  (int i = 0; i < implicit_sets_ids.size(); i++){
						grp++;
						id_implicit_set = (Integer) implicit_sets_ids.get(i);
						
						// add row into rtr_ppr table coresponding to the implicit_set
						CxEvents rtr_ppr = new CxEvents();
						rtr_ppr.setId_impi(form.getId());
						rtr_ppr.setId_implicit_set(id_implicit_set);
						rtr_ppr.setId_impu(-1);
						// for PPR type is 2
						rtr_ppr.setType(2);
						rtr_ppr.setSubtype(form.getPpr_apply_for());
						rtr_ppr.setGrp(grp);
						CxEvents_DAO.insert(session, rtr_ppr);
					}
				}

				logger.info("PPR Event saved in the database!");
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}
			else if (nextAction.equals("rtr_all")){
				// RTR
				logger.info("RTR All");
				
				List list = null;
				if (form.getRtr_apply_for() == WebConstants.RTR_Apply_for_IMPUs){
					list = IMPI_IMPU_DAO.get_all_registered_IMPU_by_IMPI_ID(session, form.getId());
				}				
				else{
					list = IMPI_DAO.get_all_Registered_IMPIs_by_IMSU_ID(session, form.getId_imsu());
				}
				if (list != null && list.size() > 0){
					
					IMSU imsu = IMSU_DAO.get_by_ID(session, form.getId_imsu());
					int grp = CxEvents_DAO.get_max_grp(session) + 1;
					for (int i = 0; i < list.size(); i++){
						CxEvents rtr_ppr = new CxEvents();
						rtr_ppr.setGrp(grp);
						// type for RTR is 1
						rtr_ppr.setType(1);
						// Reason
						rtr_ppr.setSubtype(form.getRtr_reason());
						rtr_ppr.setReason_info(form.getReasonInfo());
						rtr_ppr.setDiameter_name(imsu.getDiameter_name());
						if (form.getRtr_apply_for() == WebConstants.RTR_Apply_for_IMPUs){
							IMPU impu = (IMPU)list.get(i);
							rtr_ppr.setId_impi(form.getId());
							rtr_ppr.setId_impu(impu.getId());
						}
						else{
							IMPI impi = (IMPI) list.get(i);
							rtr_ppr.setId_impi(impi.getId());
							rtr_ppr.setId_impu(-1);
						}
						CxEvents_DAO.insert(session, rtr_ppr);
					}
				}
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}	
			else if (nextAction.equals("rtr_selected")){
				// RTR
				logger.info("RTR Selected");
				String[] rtr_identities = form.getRtr_identities();
				
				if (rtr_identities != null){
					int grp = CxEvents_DAO.get_max_grp(session) +  1;
					IMSU imsu = IMSU_DAO.get_by_ID(session, form.getId_imsu());
					for (int i = 0; i < rtr_identities.length; i++){
						if (Integer.parseInt(rtr_identities[i]) == -1)
							break;
						CxEvents rtr_ppr = new CxEvents();
						rtr_ppr.setGrp(grp);
						// type for RTR is 1
						rtr_ppr.setType(1);
						// Reason
						rtr_ppr.setSubtype(form.getRtr_reason());
						rtr_ppr.setReason_info(form.getReasonInfo());
						rtr_ppr.setDiameter_name(imsu.getDiameter_name());
						if (form.getRtr_apply_for() == WebConstants.RTR_Apply_for_IMPUs){
							rtr_ppr.setId_impi(form.getId());
							rtr_ppr.setId_impu(Integer.parseInt(rtr_identities[i]));
						}
						else{
							rtr_ppr.setId_impi(Integer.parseInt(rtr_identities[i]));
							rtr_ppr.setId_impu(-1);
						}
						CxEvents_DAO.insert(session, rtr_ppr);
					}
				}
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() +"?id=" + form.getId());
			}	

			// actions performed in all the situations 

			// reload the associated IMPUs
			List list = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI_ID(session, id);
			if (list == null){
				list = new ArrayList();
			}
			request.setAttribute("associated_IMPUs", list);
			IMSU associated_IMSU = null;

			if (id != -1){
				IMPI impi = IMPI_DAO.get_by_ID(session, id);
				if (impi != null)
					associated_IMSU = IMSU_DAO.get_by_ID(session, impi.getId_imsu());
			}
			request.setAttribute("associated_IMSU", associated_IMSU);
	    	form.setSelect_auth_scheme(WebConstants.select_auth_scheme);
	    	
			if (IMPI_Load.testForDelete(session, form.getId())){
				request.setAttribute("deleteDeactivation", "false");
			}
			else{
				request.setAttribute("deleteDeactivation", "true");
			}

			// select RTR Identities
			if (form.getRtr_apply_for() == 0){
				// Apply for IMPUs
				form.setRtr_select_identities(IMPI_IMPU_DAO.get_all_registered_IMPU_by_IMPI_ID(session, form.getId()));
			}
			else{
				// Apply for IMPIs
				form.setRtr_select_identities(IMPI_DAO.get_all_Registered_IMPIs_by_IMSU_ID(session, form.getId_imsu()));
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
