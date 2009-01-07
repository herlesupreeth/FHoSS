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


import de.fhg.fokus.hss.db.model.DSAI;
import de.fhg.fokus.hss.db.model.DSAI_IFC;
import de.fhg.fokus.hss.db.model.DSAI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.DSAI_Form;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.sh.ShConstants;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */


public class DSAI_Submit extends Action{
	private static Logger logger = Logger.getLogger(DSAI_Submit.class);

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to...
	 *
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param reponse
	 * @return forward
	 */

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {

		DSAI_Form form = (DSAI_Form) actionForm;
		String nextAction = form.getNextAction();
		int id = form.getId();
		ActionForward forward = null;

		boolean dbException = false;
		try{

			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			if (id != -1){
				if (DSAI_Load.testForDelete(session, id)){
				request.setAttribute("deleteDeactivation", "false");
				}
				else{
					request.setAttribute("deleteDeactivation", "true");
				}
			}

			if (nextAction.equals("save")){

				if (id == -1){
					// create
					DSAI dsai = new DSAI();
					dsai.setDsai_tag(form.getDsai_tag());
					DSAI_DAO.insert(session, dsai);
					id = dsai.getId();
					form.setId(id);
				}
				else{
					// update
					DSAI dsai = DSAI_DAO.get_by_ID(session, id);
					dsai.setDsai_tag(form.getDsai_tag());
				}

				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() + "?id=1");
			}
			else if (nextAction.equals("refresh")){

				DSAI dsai = (DSAI) DSAI_DAO.get_by_ID(session, id);
				DSAI_Load.setForm(form, dsai);

				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("delete")){
				DSAI_DAO.delete_by_ID(session, id);
				forward  = actionMapping.findForward(WebConstants.FORWARD_DELETE);
			}
			else if (nextAction.equals("attach_ifc")){
				DSAI_IFC dsai_ifc = new DSAI_IFC();
				dsai_ifc.setId_dsai (id);
				dsai_ifc.setId_ifc(form.getIfc_id());
				DSAI_IFC_DAO.insert(session, dsai_ifc);

				// perform refresh
				DSAI dsai = (DSAI) DSAI_DAO.get_by_ID(session, id);

				if (!DSAI_Load.setForm(form, dsai)){
								logger.error("The Dynamic Service Activation Information withe the ID:" + id + " was not loaded from database!");
				}
				//this line recovers all impu associated to that DSAI and that iFC whose DSAI-value is 0 (inactive).
				int dsaivalue= ShConstants.DSAI_value_Inactive;
				List attached_impu=IMPU_DAO.get_all_by_DSAI_IFC_and_DSAI_value(session, id, form.getIfc_id(), dsaivalue);
				Iterator it=attached_impu.iterator();
				//Send a notification for each of the IMPUs whose state is inactive.
				//Insert also notification for the SCSCF
				while (it.hasNext()){
					IMPU impu = (IMPU) it.next();
					ShNotification_DAO.insert_notif_for_iFC(session, impu);
					CxEvents_DAO.insert_CxEvent(session, impu.getId());
				}
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("attach_impu")){

				DSAI_IMPU dsai_impu = new DSAI_IMPU();
				dsai_impu.setId_dsai(id);
				dsai_impu.setId_impu(form.getImpu_id());
				dsai_impu.setDsai_value(form.getDsai_value());

				DSAI_IMPU_DAO.insert(session, dsai_impu);

				// perform refresh
				DSAI dsai = (DSAI) DSAI_DAO.get_by_ID(session, id);
				if (!DSAI_Load.setForm(form, dsai)){
								logger.error("The Dynamic Service Activation Information withe the ID:" + id + " was not loaded from database!");
				}
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("detach_ifc")){
				List dsai_ifc_list=new ArrayList();  //List of IFCs associated to the DSAI except the one the user wants to detach
				List impu_ifc_list=new ArrayList();  //List of IMPUs that have in their Service Profile at least one of the IFCs from dsai_ifc_list
				List attached_impu=new ArrayList(); //List of IMPUs attached to the DSAI

				dsai_ifc_list=DSAI_IFC_DAO.get_all_IFC_associated_except_IFC_given(session, form.getAssociated_ID(), id);
				attached_impu=DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id);

				if (dsai_ifc_list.isEmpty()){ //Only one IFC is attached to the DSAI (the one the user wants to detach).
					if(attached_impu.isEmpty()){
						DSAI_IFC_DAO.delete_by_DSAI_and_IFC_ID(session, id, form.getAssociated_ID());
					}
				}
				else{  // More than one IFC is attached to the DSAI

					impu_ifc_list=DSAI_IMPU_DAO.get_IMPU_by_DSAI_for_IFC_list(session, id, dsai_ifc_list);
					boolean same_list=true;  	//same_list=true if IMPUs attached to DSAI have in their Service Profile at
												//least one of the IFCs in dsai_ifc_list.
					Iterator attached_impu_it=attached_impu.iterator();
					while (attached_impu_it.hasNext()){
						if(!impu_ifc_list.contains(attached_impu_it.next())){
							same_list=false;
							break;
						}
					}
					if(same_list){
						while (attached_impu_it.hasNext()){
							IMPU impu = (IMPU) attached_impu_it.next();
							DSAI_IMPU dsai_impu = DSAI_IMPU_DAO.get_by_DSAI_and_IMPU_ID(session, id, impu.getId());
							//check if the ifc is attached to that impu and if the impu is inactive.
							int dsaivalue= ShConstants.DSAI_value_Inactive;
							if (dsai_impu.getDsai_value()==dsaivalue){

								ShNotification_DAO.insert_notif_for_iFC(session, impu);
								CxEvents_DAO.insert_CxEvent(session, impu.getId());
							}
						}
						DSAI_IFC_DAO.delete_by_DSAI_and_IFC_ID(session, id, form.getAssociated_ID());
					}
				}
				// perform refresh
				DSAI dsai = (DSAI) DSAI_DAO.get_by_ID(session, id);
				if (!DSAI_Load.setForm(form, dsai)){
				logger.error("The Dynamic Service Activation Information withe the ID:" + id + " was not loaded from database!");
				}

				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("detach_impu")){

				DSAI_IMPU dsai_impu = DSAI_IMPU_DAO.get_by_DSAI_and_IMPU_ID(session, id, form.getAssociated_ID());
				int dsaivalueinactive= ShConstants.DSAI_value_Inactive;
				if (dsai_impu.getDsai_value()==dsaivalueinactive){
					//send a notification for that impu and that dsai changing the dsai-value because it is inactive.
					int dsaivalueactive= ShConstants.DSAI_value_Active;
					dsai_impu.setDsai_value(dsaivalueactive);
					ShNotification_DAO.insert_notif_for_DSAI(session, dsai_impu);
					//Insert also notification for the AS related to that active IMPU and iFC.
					IMPU impu = IMPU_DAO.get_by_ID(session, form.getAssociated_ID());
					ShNotification_DAO.insert_notif_for_iFC(session, impu);
					//Insert also notification for the SCSCF
					CxEvents_DAO.insert_CxEvent(session, impu.getId());
					dsai_impu.setDsai_value(dsaivalueinactive);
				}
				DSAI_IMPU_DAO.delete_by_DSAI_and_IMPU_ID(session, id, form.getAssociated_ID());
				// perform refresh
				DSAI dsai = (DSAI) DSAI_DAO.get_by_ID(session, id);
				if (!DSAI_Load.setForm(form, dsai)){
				logger.error("The Dynamic Service Activation Information withe the ID:" + id + " was not loaded from database!");
				}
				forward  = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward =  new ActionForward(forward.getPath() + "?id=" + id);
			}
			else if (nextAction.equals("change_dsai_value")){
				int dsai_value;
				DSAI_IMPU dsai_impu = new DSAI_IMPU();
				dsai_impu=DSAI_IMPU_DAO.get_by_DSAI_and_IMPU_ID(session, id, form.getAssociated_ID());
				dsai_value=dsai_impu.getDsai_value();
				if (dsai_value==0){
					dsai_value=1;
				}
				else if (dsai_value==1){
					dsai_value=0;
				}
				else {
					logger.error("Incorrect dsai_value");
				}
				dsai_impu.setDsai_value(dsai_value);
				DSAI_IMPU_DAO.update(session, dsai_impu); //Change dsai_value in database
				//propagation
				ShNotification_DAO.insert_notif_for_DSAI(session, dsai_impu);

				//Insert also notification for the ASs related to that active IMPU and iFC.
				IMPU impu = IMPU_DAO.get_by_ID(session, form.getAssociated_ID());
				ShNotification_DAO.insert_notif_for_iFC(session, impu);
				//Insert also notification for the SCSCF
				CxEvents_DAO.insert_CxEvent(session, impu.getId());

				// perform refresh
				DSAI dsai = DSAI_DAO.get_by_ID(session, id);
				if (!DSAI_Load.setForm(form, dsai)){
					logger.error("The Dynamic Service Activation Information withe the ID:" + id + " was not loaded from database!");
				}

				forward = actionMapping.findForward(WebConstants.FORWARD_SUCCESS);
				forward = new ActionForward(forward.getPath() + "?id=" + id);
			}

			// add parameters to request & refresh select properties

			// set select_ifc & impu
			DSAI dsai = DSAI_DAO.get_by_ID(session, id);
			DSAI_Load.setForm(form, dsai);

			List attached_ifc_list = DSAI_IFC_DAO.get_all_IFC_by_DSAI_ID(session, id);
			List select_ifc =null;
			if (attached_ifc_list != null){
				select_ifc=IFC_DAO.get_all_by_Same_AS_ID(session,attached_ifc_list); //select_ifc only shows IFCs associated to the
																					 //same AS of the IFCs attached to the DSAI.
				request.setAttribute("attached_ifc_list", attached_ifc_list);
			}
			else{
				select_ifc = IFC_DAO.get_all(session);
				request.setAttribute("attached_ifc_list", new ArrayList());
			}
			form.setSelect_ifc(select_ifc);
			List select_impu = IMPU_DAO.get_all_IMPU_for_IFC_list(session, attached_ifc_list); //select_impu only shows IMPUs that have
																							   //in their SPs at least one of the IFCs
																							   //attached to the DSAI.
			form.setSelect_impu(select_impu);
			List attached_impu_list = DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id);

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
