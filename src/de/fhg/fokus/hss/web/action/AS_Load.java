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
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.AS_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class AS_Load extends Action {
	private static Logger logger = Logger.getLogger(AS_Load.class);
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		ActionForward forward = null;
		AS_Form form = (AS_Form) actionForm;
		int id = form.getId();

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (id != -1){
				// load
					ApplicationServer as = ApplicationServer_DAO.get_by_ID(session, id);
					AS_Load.setForm(form, as);
			}
			else{
				form.setUdr(true);
				form.setPur(true);
				form.setSnr(true);
			}
			
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
			forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
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
				
		forward = new ActionForward(forward.getPath() + "?id=" + id);
		return forward;
	}
	
	public static boolean setForm(AS_Form form, ApplicationServer as){
		boolean exitCode = false;
		
		if (as != null){
			exitCode = true;
			
			// set the basic parameters
			form.setId(as.getId());
			form.setName(as.getName());
			form.setServer_name(as.getServer_name());
			form.setDefault_handling(as.getDefault_handling());
			form.setDiameter_address(as.getDiameter_address());
			form.setService_info(as.getService_info());
			form.setRep_data_size_limit(as.getRep_data_size_limit());
			
			// set the Sh permissions
			form.setUdr((as.getUdr() == 1));
			form.setPur((as.getPur() == 1));
			form.setSnr((as.getSnr() == 1));
			form.setUdr_rep_data(as.getUdr_rep_data() == 1);
			form.setUdr_impu((as.getUdr_impu() == 1));
			form.setUdr_ims_user_state((as.getUdr_ims_user_state() == 1));
			form.setUdr_scscf_name((as.getUdr_scscf_name() == 1));
			form.setUdr_ifc((as.getUdr_ifc() == 1));
			form.setUdr_location((as.getUdr_location() == 1));
			form.setUdr_user_state((as.getUdr_user_state() == 1));
			form.setUdr_charging_info((as.getUdr_charging_info() == 1));
			form.setUdr_msisdn((as.getUdr_msisdn() == 1));
			form.setUdr_psi_activation((as.getUdr_psi_activation() == 1));
			form.setUdr_dsai((as.getUdr_dsai() == 1));
			form.setUdr_aliases_rep_data((as.getUdr_aliases_rep_data()) == 1);
			form.setPur_rep_data((as.getPur_rep_data() == 1));
			form.setPur_psi_activation((as.getPur_psi_activation() == 1));
			form.setPur_dsai((as.getPur_dsai() == 1));
			form.setPur_aliases_rep_data((as.getPur_aliases_rep_data() == 1));
			form.setSnr_rep_data((as.getSnr_rep_data() == 1));
			form.setSnr_impu((as.getSnr_rep_data() == 1));
			form.setSnr_impu((as.getSnr_impu() == 1));
			form.setSnr_ims_user_state((as.getSnr_ims_user_state() == 1));
			form.setSnr_scscf_name((as.getSnr_scscf_name() == 1));
			form.setSnr_ifc((as.getSnr_ifc() == 1));
			form.setSnr_psi_activation((as.getSnr_psi_activation() == 1));
			form.setSnr_dsai((as.getSnr_dsai() == 1));
			form.setSnr_aliases_rep_data((as.getSnr_rep_data() == 1));
			form.setInclude_register_request((as.getInclude_register_request() == 1));
			form.setInclude_register_response((as.getInclude_register_response() == 1));
			
		}
		return exitCode;
	}
	
	public static boolean testForDelete(Session session, int id){
		List l = IFC_DAO.get_all_by_AS_ID(session, id);
		if (l != null && l.size() > 0){
			return false;
		}
		return true;
	}
}
