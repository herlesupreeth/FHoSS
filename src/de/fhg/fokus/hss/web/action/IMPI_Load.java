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


import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.web.form.IMPI_Form;
import de.fhg.fokus.hss.web.util.WebConstants;
import de.fhg.fokus.hss.auth.HexCodec;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IMPI_Load extends Action {
	private static Logger logger = Logger.getLogger(IMPI_Load.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) {

		IMPI_Form form = (IMPI_Form) actionForm;
		int id = form.getId();
		List associated_IMPUs = new ArrayList();
		IMSU associated_IMSU = null;
		ActionForward forward = null;
		
		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

	    	form.setSelect_auth_scheme(WebConstants.select_auth_scheme);

	    	// RTR & PPR
	    	form.setRtr_select_identities(IMPI_IMPU_DAO.get_all_registered_IMPU_by_IMPI_ID(session, id));
	    	
			if (id != -1){
				// load
				IMPI impi = IMPI_DAO.get_by_ID(session, id); 

				// associated IMPUs
				associated_IMPUs = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI_ID(session, id);
				associated_IMSU = IMSU_DAO.get_by_ID(session, impi.getId_imsu());
				IMPI_Load.setForm(form, impi);
			}
			else{ 
				form.setAka1(true);
			}
			request.setAttribute("associated_IMSU", associated_IMSU);
			request.setAttribute("associated_IMPUs", associated_IMPUs);

			if (IMPI_Load.testForDelete(session, form.getId())){
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
	
	public static boolean setForm(IMPI_Form form, IMPI impi){
		boolean exitCode = false;
		
		if (impi != null){
			exitCode = true;
			form.setIdentity(impi.getIdentity());
			form.setId_imsu(impi.getId_imsu());
			if (impi.getK().length==16)
				form.setSecretKey(HexCodec.encode(impi.getK()));
			else 
				form.setSecretKey(new String(impi.getK()));
			form.setAmf(HexCodec.encode(impi.getAmf()));
			form.setOp(HexCodec.encode(impi.getOp()));
			form.setSqn(impi.getSqn());
			form.setIp(impi.getIp());
			form.setDefault_auth_scheme(impi.getDefault_auth_scheme());
		
			int auth_scheme = impi.getAuth_scheme();
			if ((auth_scheme & 255) == 255){
				form.setAll(true);
			}
			else{
				if ((auth_scheme & CxConstants.Auth_Scheme_AKAv1) != 0){
					form.setAka1(true);
				}
			
				if ((auth_scheme & CxConstants.Auth_Scheme_AKAv2) != 0){
					form.setAka2(true);
				}
				
				if ((auth_scheme & CxConstants.Auth_Scheme_MD5) != 0){
					form.setMd5(true);
				}

				if ((auth_scheme & CxConstants.Auth_Scheme_Digest) != 0){
					form.setDigest(true);
				}
				
				if ((auth_scheme & CxConstants.Auth_Scheme_SIP_Digest) != 0){
					form.setSip_digest(true);
				}
				
				if ((auth_scheme & CxConstants.Auth_Scheme_HTTP_Digest_MD5) != 0){
					form.setHttp_digest(true);
				}
				
				if ((auth_scheme & CxConstants.Auth_Scheme_Early) != 0){
					form.setEarly(true);
				}
				
				if ((auth_scheme & CxConstants.Auth_Scheme_NASS_Bundled) != 0){
					form.setNass_bundle(true);
				}
				
			}		
		}
		return exitCode;
	}	

	public static boolean testForDelete(Session session, int id){
		List result = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI_ID(session, id);
		if (result != null && result.size() > 0){
			return false;
		}
		return true;
	}
}
