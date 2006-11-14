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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.form.AsForm;
import de.fhg.fokus.hss.model.Apsvr;
import de.fhg.fokus.hss.model.AsPermList;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class AsShowAction extends HssAction
{
	private static final Logger LOGGER = Logger.getLogger(AsShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse)
			throws Exception
	{
		LOGGER.debug("entering");

		AsForm form = (AsForm) actionForm;
		// LOGGER.debug(form);

		Apsvr apsvr = (Apsvr) getSession().load(Apsvr.class,
				form.getPrimaryKey());
		form.setAsAddress(apsvr.getAddress());
		form.setAsName(apsvr.getName());
		form.setAsId(String.valueOf(apsvr.getApsvrId().intValue()));
		form.setDefaultHandling(String.valueOf(apsvr.getDefaultHandling()));

		AsPermList asPermList = (AsPermList) getSession().get(AsPermList.class,
				form.getPrimaryKey());
		if (asPermList != null)
		{
			form.setPullRepData(asPermList.isPullRepData());
			form.setPull(asPermList.isPull());
			form.setPullCharging(asPermList.isPullCharging());
			form.setPullIfc(asPermList.isPullIfc());
			form.setPullImpu(asPermList.isPullImpu());
			form.setPullImpuUserState(asPermList.isPullImpuUserState());
			form.setPullUserState(asPermList.isPullUserState());
			form.setPullScscfName(asPermList.isPullScscfName());
			form.setPullMsisdn(asPermList.isPullMsisdn());
			form.setPullPsi(asPermList.isPullPsi());
			form.setPullLocInfo(asPermList.isPullLocInfo());
			form.setUpdPsi(asPermList.isUpdPsi());
			form.setUpdRepData(asPermList.isUpdRepData());
			form.setSubIfc(asPermList.isSubIfc());
			form.setSubImpuUserState(asPermList.isSubImpuUserState());
			form.setSubRepData(asPermList.isSubRepData());
			form.setSubScscfname(asPermList.isSubScscfname());
			form.setSubPsi(asPermList.isSubPsi());
		}

		closeSession();

		LOGGER.debug("exiting");

		return mapping.findForward(FORWARD_SUCCESS);
	}

}
