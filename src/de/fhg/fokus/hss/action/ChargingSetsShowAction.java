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

import de.fhg.fokus.hss.form.ChrginfoForm;
import de.fhg.fokus.hss.model.Chrginfo;

import org.apache.log4j.Logger;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This action loads all network objects from persitence and stored these in a
 * request.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ChargingSetsShowAction extends HssAction
{
	private static final Logger LOGGER = Logger
			.getLogger(ChargingSetsShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		LOGGER.debug("entering");

		ActionForward forward = null;
		List chrginfoList = getSession().createCriteria(Chrginfo.class).list();

		ChrginfoForm chrgForm = (ChrginfoForm) form;

		try
		{
			if (chrgForm.getAction() != null)
			{
				Integer primaryKey = Integer.valueOf(request
						.getParameter("chrgId"));

				if (chrgForm.getAction().equals(ChrginfoForm.ACTION_EDIT))
				{
					Chrginfo chrginfo = (Chrginfo) getSession().get(
							Chrginfo.class, primaryKey);
					chrgForm.setChrgId(convString(chrginfo.getChrgId()));
					chrgForm.setName(chrginfo.getName());
					chrgForm.setPriChrgCollFnName(chrginfo
							.getPriChrgCollFnName());
					chrgForm.setSecChrgCollFnName(chrginfo
							.getSecChrgCollFnName());
					chrgForm.setPriEventChrgFnName(chrginfo
							.getPriEventChrgFnName());
					chrgForm.setSecEventChrgFnName(chrginfo
							.getSecEventChrgFnName());
				} else if (chrgForm.getAction().equals(
						ChrginfoForm.ACTION_SUBMIT))
				{
					Chrginfo chrginfo = null;

					if (primaryKey.intValue() != -1)
					{
						chrginfo = (Chrginfo) getSession().get(Chrginfo.class,
								primaryKey);
					} else
					{
						chrginfo = new Chrginfo();
						chrginfoList.add(chrginfo);
					}

					chrginfo.setName(chrgForm.getName());
					chrginfo.setPriChrgCollFnName(chrgForm
							.getPriChrgCollFnName());
					chrginfo.setPriEventChrgFnName(chrgForm
							.getPriEventChrgFnName());
					chrginfo.setSecChrgCollFnName(chrgForm
							.getSecChrgCollFnName());
					chrginfo.setSecEventChrgFnName(chrgForm
							.getSecEventChrgFnName());
					beginnTx();
					getSession().saveOrUpdate(chrginfo);
					endTx();
				} else if (chrgForm.getAction().equals(
						ChrginfoForm.ACTION_DELETE))
				{
					Chrginfo chrginfo = (Chrginfo) getSession().get(
							Chrginfo.class, primaryKey);
					beginnTx();
					getSession().delete(chrginfo);
					chrginfoList.remove(chrginfo);
					endTx();
				}
			}

			forward = mapping.findForward(FORWARD_SUCCESS);
		} catch (ConstraintViolationException e)
		{
			LOGGER.debug(this, e);

			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage(
					"error.duplicate"));
			saveMessages(request, actionMessages);
			forward = mapping.findForward(FORWARD_FAILURE);
		} finally
		{
			closeSession();
		}

		request.setAttribute("chrginfos", chrginfoList);

		LOGGER.debug("exiting");

		return mapping.findForward(FORWARD_SUCCESS);
	}
}
