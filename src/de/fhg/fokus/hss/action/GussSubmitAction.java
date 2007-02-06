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

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.form.GussForm;
import de.fhg.fokus.hss.form.UssForm;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.UserSecSettings;
import de.fhg.fokus.hss.util.HibernateUtil;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class GussSubmitAction extends HssAction
{
	private static final Logger LOGGER = Logger
			.getLogger(GussSubmitAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		LOGGER.debug("entering");

		GussForm form = (GussForm) actionForm;

		Impi impi;
		try{
			HibernateUtil.beginTransaction();
			impi = (Impi) HibernateUtil.getCurrentSession().load(Impi.class, form.getPrimaryKey());
			impi.setUiccType(new Integer(form.getUiccType()));
			impi.setKeyLifeTime(new Integer(form.getKeyLifeTime()));

			Iterator it = form.getUssList().iterator();
			while (it.hasNext())
			{
				UssForm ussForm = (UssForm) it.next();
				UserSecSettings userSecSettings = null;
				Integer primaryKey = ussForm.getPrimaryKey();

				if (primaryKey.intValue() != -1){
					// Load USS
					userSecSettings = (UserSecSettings) HibernateUtil.getCurrentSession().load(UserSecSettings.class, primaryKey);
				} 
				else{
					if (ussForm.isDelete() == false){
						// Create new if no delete
						userSecSettings = new UserSecSettings();
						userSecSettings.setImpiId(impi.getImpiId());
					}
				}

				if (ussForm.isDelete()){
					if (primaryKey.intValue() != -1){
						// Delete if delete seleceted and id is known
						HibernateUtil.getCurrentSession().delete(userSecSettings);
					}
				} 
				else{
					// Save or update if no delete selected
					userSecSettings.setFlag(ussForm.getFlag());
					userSecSettings.setNafGroup(ussForm.getNafGroup());
					userSecSettings.setUssType(new Integer(ussForm.getUssType()));
					HibernateUtil.getCurrentSession().saveOrUpdate(userSecSettings);
				}
			}

			HibernateUtil.getCurrentSession().update(impi);
			HibernateUtil.commitTransaction();
		}
		finally{
			HibernateUtil.closeSession();
		}

		ActionForward forward = mapping.findForward(FORWARD_SUCCESS);
		forward = new ActionForward(forward.getPath() + "?impiId=" + impi.getImpiId(), true);

		LOGGER.debug("exiting");
		return forward;
	}
}
