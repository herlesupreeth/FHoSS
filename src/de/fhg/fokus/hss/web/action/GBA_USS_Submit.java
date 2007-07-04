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


import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.Zh_USS;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.Zh_USS_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.GBA_USS_Form;
import de.fhg.fokus.hss.web.form.USS_Form;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.zh.ZhConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class GBA_USS_Submit extends Action{
	
	private static Logger logger = Logger.getLogger(GBA_USS_Submit.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {
		
		GBA_USS_Form form = (GBA_USS_Form) actionForm;
		String nextAction = form.getNextAction();
		ActionForward forward = null;
		int id_impi = form.getId_impi();

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (nextAction.equals("save")){
				IMPI impi = IMPI_DAO.get_by_ID(session, id_impi);
				// make the changes
				impi.setZh_uicc_type(form.getUicc_type());
				impi.setZh_key_life_time(form.getKey_life_time());
				impi.setZh_default_auth_scheme(form.getDefault_auth_scheme());
				
				IMPI_DAO.update(session, impi);
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_impi=" + id_impi);
			}
			else if (nextAction.equals("refresh")){
				IMPI impi = IMPI_DAO.get_by_ID(session, id_impi);

				if (!GBA_USS_Load.setForm(form, impi)){
					logger.error("The IMPI with the ID:" + id_impi + " was not loaded from database!");
				}
				// reload the uss List
				List uss_Form_List = GBA_USS_Load.getUSSList(session, id_impi);
				form.setUssList(uss_Form_List);

				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id_impi=" + id_impi);
			}
			else if (nextAction.equals("add_uss")){
				// add new USS

				Zh_USS zh_uss = new Zh_USS();
				zh_uss.setId_impi(id_impi);		
				Zh_USS_DAO.insert(session, zh_uss);

				// reload the uss List				
				List uss_Form_List = GBA_USS_Load.getUSSList(session, id_impi);
				form.setUssList(uss_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id_impi=" + id_impi);
				
			}
			else if (nextAction.equals("save_uss")){
				if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					saveUSSList(session, form);
				}
				// 	reload the uss List				
				List uss_Form_List = GBA_USS_Load.getUSSList(session, id_impi);
				form.setUssList(uss_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id_impi=" + id_impi);
			}
			else if (nextAction.equals("delete_uss")){
				Zh_USS_DAO.delete_by_ID(session, form.getAssociated_ID());

				// reload the uss List				
				List uss_Form_List = GBA_USS_Load.getUSSList(session, id_impi);
				form.setUssList(uss_Form_List);
				
				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() +"?id_impi=" + id_impi);
			}
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
	
		
	private static void saveUSSList(Session session, GBA_USS_Form form){
				
		Iterator itUSSForms = form.getUssList().iterator();
		Zh_USS uss = null;
		USS_Form ussForm = null;
		

		while (itUSSForms.hasNext()){
			// perform update on existing spts
			ussForm = (USS_Form) itUSSForms.next();
			
			int ussID = ussForm.getId_uss();
			uss = Zh_USS_DAO.get_by_ID(session, ussID);
			
			if (uss == null)
				continue;
			uss.setType(ussForm.getType());
			int flags = 0;
			if (ussForm.isAuth_allowed()){
				flags |= ZhConstants.GAA_Authorization_Authentication_Allowed;
			}
			if (ussForm.isNon_repudiation_allowed()){
				flags |= ZhConstants.GAA_Authorization_Non_Repudiation_Allowed;
			}
			uss.setFlags(flags);
			uss.setNaf_group(ussForm.getNafGroup());
			Zh_USS_DAO.update(session, uss);
		}// while
	}
	
}
