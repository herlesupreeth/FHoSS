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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.Shared_IFC_Set_DAO;
import de.fhg.fokus.hss.db.op.TP_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.IFC_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IFC_Load extends Action {
	private static Logger logger = Logger.getLogger(IFC_Load.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		IFC_Form form = (IFC_Form) actionForm;
		int id = form.getId();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (id != -1){
				// load
				IFC ifc = IFC_DAO.get_by_ID(session, id);
				if (!IFC_Load.setForm(form, ifc)){
					// print some error message
				}			
			}
			prepareForward(session, form, request, id);

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
	
	public static boolean setForm(IFC_Form form, IFC ifc){
		boolean exitCode = false;
		
		if (ifc != null){
			exitCode = true;
			
			form.setId(ifc.getId());
			form.setName(ifc.getName());
			form.setProfile_part_ind(ifc.getProfile_part_ind());
			form.setId_application_server(ifc.getId_application_server());
			form.setId_tp(ifc.getId_tp());
		}
		return exitCode;
	}
	
	public static boolean testForDelete(Session session, int id){
		List result = SP_IFC_DAO.get_all_SP_by_IFC_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		result = Shared_IFC_Set_DAO.get_all_by_IFC_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		return true;
	}
	
	public static void prepareForward(Session session, IFC_Form form, HttpServletRequest request, int id){
		List list = ApplicationServer_DAO.get_all(session);
		form.setSelect_as(list);
		
		list = TP_DAO.get_all(session);
		form.setSelect_tp(list);
		
		if (IFC_Load.testForDelete(session, id)){
			request.setAttribute("deleteDeactivation", "false");
		}
		else{
			request.setAttribute("deleteDeactivation", "true");
		}
		
		
	}
}
