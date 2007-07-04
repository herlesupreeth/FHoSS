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


import de.fhg.fokus.hss.db.model.CapabilitiesSet;
import de.fhg.fokus.hss.db.op.CapabilitiesSet_DAO;
import de.fhg.fokus.hss.db.op.Capability_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.CapS_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class CapS_Load extends Action {
	private static Logger logger = Logger.getLogger(CapS_Load.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		CapS_Form form = (CapS_Form) actionForm;
		int id = form.getId_set();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			if (id != -1){
				// load
				CapabilitiesSet cap_s = CapabilitiesSet_DAO.get_by_set_ID(session, id);
				CapS_Load.setForm(form, cap_s);
			}

			CapS_Load.prepareForward(session, form, request, id);
			
			forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
			forward = new ActionForward(forward.getPath() + "?id_set=" + id);
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
	
	public static boolean setForm(CapS_Form form, CapabilitiesSet cap_s){
		boolean exitCode = false;
		
		if (cap_s != null){
			exitCode = true;
			form.setId_set(cap_s.getId_set());
			form.setName(cap_s.getName());
		}
		return exitCode;
	}
	
	public static boolean testForDelete(Session session, int id){
		int cnt = CapabilitiesSet_DAO.get_cnt_for_set(session, id);
		if (cnt > 1){
			return false;
		}
		
		cnt = IMSU_DAO.get_IMSU_cnt_by_Cap_Set_ID(session, id);
		if (cnt > 0){
			return false;
		}
		
		return true;
	}
	
	public static void prepareForward(Session session, CapS_Form form, HttpServletRequest request, int id){
		List select_cap = Capability_DAO.get_all(session);
		form.setSelect_cap(select_cap);
		
		if (testForDelete(session, id)){
			request.setAttribute("deleteDeactivation", "false");
			request.setAttribute("detachDeactivation", "false");
		}
		else{
			request.setAttribute("deleteDeactivation", "true");
			int associated_cap_cnt = CapabilitiesSet_DAO.get_cnt_for_set(session, id);
			if (associated_cap_cnt > 1){
				request.setAttribute("detachDeactivation", "false");	
			}
			else{
				request.setAttribute("detachDeactivation", "true");
			}
			
		}
		List attached_cap = CapabilitiesSet_DAO.get_all_from_set(session, id);
		if (attached_cap != null){
				request.setAttribute("attached_cap", attached_cap);
		}
		else{
				request.setAttribute("attached_cap", new ArrayList());
		}
	}
	
}
