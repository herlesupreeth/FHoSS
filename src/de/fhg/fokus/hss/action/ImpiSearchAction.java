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

import de.fhg.fokus.hss.form.ImpiSearchForm;
import de.fhg.fokus.hss.util.HibernateUtil;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.hibernate.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpiSearchAction extends HssAction
{
	private static final Logger LOGGER = Logger
			.getLogger(ImpiSearchAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse)
			throws Exception
	{
		LOGGER.debug("entering");

		ImpiSearchForm form = (ImpiSearchForm) actionForm;
		LOGGER.debug(form);

		try
		{
			HibernateUtil.beginTransaction();
			// Prepare Querry
			Query query = HibernateUtil.getCurrentSession()
					.createQuery("select impi from de.fhg.fokus.hss.model.Impi as impi where impi.impiString like ? order by impi.impiString");
			query.setString(0, form.getImpiString() + "%");

			// Check page browsing values
			int rowPerPage = Integer.parseInt(form.getRowsPerPage());
			int currentPage = Integer.parseInt(form.getPage()) - 1;
			int maxPages = (int) ((query.list().size() - 1) / rowPerPage) + 1;

			if (currentPage > maxPages)
			{
				currentPage = 0;
			}

			int firstResult = currentPage * rowPerPage;

			// Edit page browsing parameters
			query.setMaxResults(rowPerPage);
			query.setFirstResult(firstResult);

			// store attributes in request
			request.setAttribute("result", query.list());

			request.setAttribute("maxPages", String.valueOf(maxPages));
			request.setAttribute("currentPage", String.valueOf(currentPage));
			request.setAttribute("rowPerPage", String.valueOf(rowPerPage));
			
			HibernateUtil.commitTransaction();
		} finally
		{
			HibernateUtil.closeSession();
		}

		LOGGER.debug("exiting");

		return mapping.findForward(FORWARD_SUCCESS);
	}
}
