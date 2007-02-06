/*
 * $Id$
 *
 * Copyright (C) 2004-2006 FhG Fokus
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
package de.fhg.fokus.hss.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;

import de.fhg.fokus.hss.form.GussForm;
import de.fhg.fokus.hss.form.UssForm;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.UserSecSettings;
import de.fhg.fokus.hss.util.HibernateUtil;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class GussShowAction extends HssAction
{
	private static final Logger LOGGER = Logger.getLogger(GussShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) throws Exception {

		LOGGER.debug("entering");

		GussForm form = (GussForm) actionForm;
		
		try{
			HibernateUtil.beginTransaction();
			Impi impi = (Impi) HibernateUtil.getCurrentSession().load(Impi.class, form.getPrimaryKey());

			// Load the uss list
			Query query = HibernateUtil.getCurrentSession()
				.createQuery("select uss from de.fhg.fokus.hss.model.UserSecSettings as uss where uss.impiId = ?");
			query.setInteger(0, form.getPrimaryKey());

			List gussList = query.list();
			
			ArrayList ussList = new ArrayList();
			Iterator it = gussList.iterator();

			while (it.hasNext()){
				UserSecSettings uss = (UserSecSettings) it.next();
				UssForm ussForm = new UssForm();
				ussForm.setId(convString(uss.getId()));
				ussForm.setFlag(uss.getFlag().intValue());
				ussForm.setUssType(uss.getUssType());
				ussForm.setNafGroup(uss.getNafGroup());
				ussList.add(ussForm);
			}

			if ((request.getParameter("addUss") != null) && (request.getParameter("addUss").equals("true"))){
				UssForm ussForm = new UssForm();
				ussForm.setId("-1");
				ussList.add(ussForm);
			}

			form.setUssList(ussList);

			// add the impi values
			form.setUiccType(impi.getUiccType().intValue());
			form.setImpiString(impi.getImpiString());

			if (impi.getKeyLifeTime() != null){
				form.setKeyLifeTime(impi.getKeyLifeTime());
			}
			HibernateUtil.commitTransaction();
		}
		finally{
			HibernateUtil.closeSession();
		}
		
		LOGGER.debug("exiting");
		return mapping.findForward(FORWARD_SUCCESS);
	}
}
