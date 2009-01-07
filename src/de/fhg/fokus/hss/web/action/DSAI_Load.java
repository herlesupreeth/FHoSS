/*
  *  Copyright (C) 2004-2007 FhG Fokus
  *
  * Developed by Instrumentacion y Componentes S.A. (Inycom). Contact at: ims at inycom dot es
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

import de.fhg.fokus.hss.db.model.DSAI;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.DSAI_Form;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */

public class DSAI_Load extends Action {
	private static Logger logger = Logger.getLogger(DSAI_Load.class);

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to...
	 *
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 */

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {

		DSAI_Form form = (DSAI_Form) actionForm;
		int id = form.getId();
		List attached_ifc_list;
		List attached_impu_list;

		List select_ifc = new ArrayList();
		List select_impu = new ArrayList();
		ActionForward forward = null;

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();


			// load
			DSAI dsai = DSAI_DAO.get_by_ID(session, id);
			DSAI_Load.setForm(form, dsai);

			attached_ifc_list = DSAI_IFC_DAO.get_all_IFC_by_DSAI_ID(session, id);

			if ((attached_ifc_list != null) && (attached_ifc_list.size()>0)){ //There is at least one ifc attached to the dsai
				select_ifc=IFC_DAO.get_all_by_Same_AS_ID(session,attached_ifc_list); //Select only shows ifcs with the same as of the
																					 //ifc attached.
				request.setAttribute("attached_ifc_list", attached_ifc_list);
			}
			else{   //No ifc is attached to the dsai yet. Select shows every ifc of the system.
				select_ifc = IFC_DAO.get_all(session);
				request.setAttribute("attached_ifc_list", new ArrayList());
			}
			form.setSelect_ifc(select_ifc);
			select_impu = IMPU_DAO.get_all_IMPU_for_IFC_list(session, attached_ifc_list); //select_impu only shows impus that have
																						  //in their Service Profile at least one of
																						  //the ifcs attached to the dsai.
			form.setSelect_impu(select_impu);
			attached_impu_list = DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id);

			if (attached_impu_list != null){
				request.setAttribute("attached_impu_list", attached_impu_list);
			}
			else{
				request.setAttribute("attached_impu_list", new ArrayList());
			}

			if (DSAI_Load.testForDelete(session, form.getId())){
				request.setAttribute("deleteDeactivation", "false");
			}
			else{
				request.setAttribute("deleteDeactivation", "true");
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

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to...
	 *
	 * @param session	Hibernate session
	 * @param id	identifier
	 * @return true, if
	 */

	public static boolean testForDelete(Session session, int id){
		List result = DSAI_IFC_DAO.get_all_IFC_by_DSAI_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		result = DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}

		return true;
	}

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to...
	 *
	 * @param form
	 * @param dsai
	 * @return true, if
	 */

	public static boolean setForm(DSAI_Form form, DSAI dsai){
		boolean exitCode = false;

		if (dsai != null){
			exitCode = true;
			form.setId(dsai.getId());
			form.setDsai_tag(dsai.getDsai_tag());
		}
		return exitCode;
	}
}
