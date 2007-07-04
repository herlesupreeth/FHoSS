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


public class SP_Load extends Action {
	private static Logger logger = Logger.getLogger(SP_Load.class);

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		SP_Form form = (SP_Form) actionForm;
		int id = form.getId();
		List attached_ifc_list;
		List attached_shared_ifc_list;
		
		List select_ifc = new ArrayList();
		List select_shared_ifc = new ArrayList();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			// load
			SP sp = SP_DAO.get_by_ID(session, id); 
			SP_Load.setForm(form, sp);
			
			// set select_ifc & select_shared_ifc
			select_ifc = IFC_DAO.get_all(session);
			form.setSelect_ifc(select_ifc);

			select_shared_ifc = Shared_IFC_Set_DAO.get_all_Sets(session);	
			form.setSelect_shared_ifc(select_shared_ifc);
			
			attached_ifc_list = SP_IFC_DAO.get_all_IFC_by_SP_ID(session, id);
			attached_shared_ifc_list = SP_Shared_IFC_Set_DAO.get_all_Shared_IFC_by_SP_ID(session, id);
			
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
			request.setAttribute("attached_IFC_list", attached_ifc_list);
			
			forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
			forward = new ActionForward(forward.getPath() + "?id=" + id);
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
	
	
	public static boolean testForDelete(Session session, int id){
		List result = SP_IFC_DAO.get_all_IFC_by_SP_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		result = SP_Shared_IFC_Set_DAO.get_all_Shared_IFC_by_SP_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		
		return true;
	}
	
	public static boolean setForm(SP_Form form, SP sp){
		boolean exitCode = false;
		
		if (sp != null){
			exitCode = true;
			form.setId(sp.getId());
			form.setName(sp.getName());
			form.setCn_service_auth(sp.getCn_service_auth());
			
		}
		return exitCode;
	}	
}
