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


import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.ChargingInfo_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_VisitedNetwork_DAO;
import de.fhg.fokus.hss.db.op.SP_DAO;
import de.fhg.fokus.hss.db.op.VisitedNetwork_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.IMPU_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IMPU_Load extends Action {
	private static Logger logger = Logger.getLogger(IMPU_Load.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse)
			throws Exception{

		IMPU_Form form = (IMPU_Form) actionForm;
		int id = form.getId();
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();
			
			// actions performed in all the situations
			List sp_list;
			sp_list = SP_DAO.get_all(session);
			form.setSelect_sp(sp_list);

			List chg_list;
			chg_list = ChargingInfo_DAO.get_all(session);
			form.setSelect_charging_info(chg_list);

			List vn_list;
			vn_list = VisitedNetwork_DAO.get_all(session);
			form.setSelect_vn(vn_list);

			if (id != -1){
				// add the value of deleteDeactivation	
				if (IMPU_Load.testForDelete(session, form.getId())){
					request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
				
				IMPU impu = (IMPU) session.load(IMPU.class, form.getId());
				
				impu.convert_wildcard_from_ims_to_sql();
				
				List implicitset_IMPUs = IMPU_DAO.get_all_from_set(session, impu.getId_implicit_set());
				request.setAttribute("implicitset_IMPUs", implicitset_IMPUs);
				
				List associated_IMPIs = IMPI_IMPU_DAO.get_all_IMPI_by_IMPU_ID(session, id);
				request.setAttribute("associated_IMPIs", associated_IMPIs);
				
				List visitedNetworks = IMPU_DAO.get_all_VisitedNetworks_by_IMPU_ID(session, id);
				request.setAttribute("visitedNetworks", visitedNetworks);
				
				IMPU_Load.setForm(form, impu);
			}

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
		List result = IMPI_IMPU_DAO.get_all_IMPI_by_IMPU_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		result = IMPU_VisitedNetwork_DAO.get_all_VN_by_IMPU_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		
		return true;
	}
	
	public static boolean setForm(IMPU_Form form, IMPU impu){
		boolean exitCode = false;
		
		if (impu != null){
			exitCode = true;
			form.setId(impu.getId());
			form.setIdentity(impu.getIdentity());
			
			if (impu.getBarring() == 1){
				form.setBarring(true);
			}
			else{
				form.setBarring(false);
			}
			form.set_Id_sp(impu.getId_sp());
			form.setId_charging_info(impu.getId_charging_info());
			form.setId_impu_implicitset(impu.getId_implicit_set());
			
			if (impu.getCan_register() == 1){	
				form.setCan_register(true);
			}
			else{
				form.setCan_register(false);
			}
			form.setType(impu.getType());
			
			impu.convert_wildcard_from_sql_to_ims();
			form.setWildcard_psi(impu.getWildcard_psi());
			impu.convert_wildcard_from_ims_to_sql();
			
			if (impu.getPsi_activation() == 1){
				form.setPsi_activation(true);	
			}
			else{
				form.setPsi_activation(false);
			}
			form.setDisplay_name(impu.getDisplay_name());
			form.setUser_state(impu.getUser_state());
		}
		
		return exitCode;
	}
}
