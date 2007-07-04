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


public class TP_Load extends Action {
	private static Logger logger = Logger.getLogger(TP_Load.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		TP_Form form = (TP_Form) actionForm;
		int id = form.getId();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (id != -1){
				// load
				TP tp = TP_DAO.get_by_ID(session, id);
				TP_Load.setForm(form, tp);
				List spt_Form_List = TP_Load.getSpts(session, id);
				form.setSpts(spt_Form_List);
			}
			TP_Load.prepareForward(session, form, request, id);

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
	
	public static boolean setForm(TP_Form form, TP tp){
		boolean exitCode = false;
		
		if (tp != null){
			exitCode = true;
			form.setId(tp.getId());
			form.setName(tp.getName());
			form.setCondition_type_cnf(tp.getCondition_type_cnf());
		}
		
		return exitCode;
	}
	
	public static boolean testForDelete(Session session, int id){
		List l = IFC_DAO.get_all_by_TP_ID(session, id);
		if (l != null && l.size() > 0){
			return false;
		}
		l = SPT_DAO.get_all_by_TP_ID(session, id);
		if (l != null && l.size() > 0){
			return false;
		}
		
		return true;
	}
	
	public static void prepareForward(Session session, TP_Form form, HttpServletRequest request, int id){
		List select_ifc = IFC_DAO.get_all(session);
		form.setSelect_ifc(select_ifc);
		
		List attached_ifc_list = null;
		attached_ifc_list = IFC_DAO.get_all_by_TP_ID(session, id);
		if (attached_ifc_list != null)
			request.setAttribute("attached_ifc_list", attached_ifc_list);
		else	
			request.setAttribute("attached_ifc_list", new ArrayList());
		
		if (id != -1){
			if (TP_Load.testForDelete(session, id)){
				request.setAttribute("deleteDeactivation", "false");
			}
			else{
				request.setAttribute("deleteDeactivation", "true");
			}
		}			
	}

	public static List<SPT_Form> getSpts(Session session, int id_tp){
		
		List result = new ArrayList();
		
		List sptList = SPT_DAO.get_all_by_TP_ID(session, id_tp);
		Iterator it = sptList.iterator();

		while (it.hasNext()){
			
			SPT spt = (SPT) it.next();
			SPT_Form sptForm = new SPT_Form();

			switch (spt.getType()){
				case CxConstants.SPT_Type_RequestURI:
					sptForm.setRequestUri(spt.getRequesturi());
					break;

				case CxConstants.SPT_Type_Method:
					sptForm.setSipMethod(spt.getMethod());
					break;

				case CxConstants.SPT_Type_SIPHeader:
					sptForm.setSipHeader(spt.getHeader());
					sptForm.setSipHeaderContent(spt.getHeader_content());
					break;

				case CxConstants.SPT_Type_SessionCase:
					sptForm.setSessionCase(spt.getSession_case());
					break;

				case CxConstants.SPT_Type_SessionDescription:
					sptForm.setSessionDescContent(spt.getSdp_line_content());
					sptForm.setSessionDescLine(spt.getSdp_line());
					break;
			}

			sptForm.setSptId(spt.getId());
			sptForm.setType(spt.getType());
			sptForm.setGroup(spt.getGrp());
			sptForm.setNeg(spt.getCondition_negated()==1?true:false);
			
			int reg_type = spt.getRegistration_type();
			
			if ((reg_type & CxConstants.RType_Reg_Mask) != 0){
				sptForm.setRtype_reg(true);
			}
			if ((reg_type & CxConstants.RType_Re_Reg_Mask) != 0){
				sptForm.setRtype_re_reg(true);
			}
			if ((reg_type & CxConstants.RType_De_Reg_Mask) != 0){
				sptForm.setRtype_de_reg(true);
			}
			result.add(sptForm);
		}

		return result;
	}
	
}
