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
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.form.IfcForm;
import de.fhg.fokus.hss.form.SvpForm;
import de.fhg.fokus.hss.model.Ifc2svp;
import de.fhg.fokus.hss.model.Svp;
import de.fhg.fokus.hss.util.HibernateUtil;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class SvpShowAction extends HssAction{
	private static final Logger LOGGER = Logger.getLogger(SvpShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) throws Exception{
		
		LOGGER.debug("entering");

		SvpForm form = (SvpForm) actionForm;
		LOGGER.debug(form);

		try{
			Integer primaryKey = form.getPrimaryKey();
			HibernateUtil.beginTransaction();
			if (primaryKey.intValue() != -1){
				Svp svp = (Svp) HibernateUtil.getCurrentSession().get(Svp.class, primaryKey);
				form.setName(svp.getName());
				addIfcs(svp, form);
			}
			HibernateUtil.commitTransaction();
		}
		catch (NullPointerException e){
			LOGGER.error("NULL pointer exception!");
			e.printStackTrace();
		}

		finally{
			HibernateUtil.closeSession();
		}

		LOGGER.debug("exiting");
		return mapping.findForward(FORWARD_SUCCESS);
	}

	private void addIfcs(Svp svp, SvpForm form)
	{
		Iterator it = svp.getIfc2svps().iterator();
		while (it.hasNext()){
			Ifc2svp ifc2svp = (Ifc2svp) it.next();
			IfcForm ifcForm = new IfcForm();
			ifcForm.setIfcId(convString(ifc2svp.getIfc().getIfcId().intValue()));
			ifcForm.setIfcName(ifc2svp.getIfc().getName());
			ifcForm.setPriority(convString(ifc2svp.getPriority()));
			form.getIfcs().add(ifcForm);
		}

		ArrayList lst = form.getIfcs();
		int length = lst.size();

		IfcForm temp;
		int a, b;
		for (int x = 0; x < length - 1; x++){
			for (int y = 0; y < length - 1 - x; y++){

				IfcForm form1 = (IfcForm) lst.get(y);
				IfcForm form2 = (IfcForm) lst.get(y + 1);
				a = Integer.parseInt(form2.getPriority());
				b = Integer.parseInt(form1.getPriority());
				if (a < b){
					temp = (IfcForm) lst.get(y);
					lst.set(y, (IfcForm) lst.get(y + 1));
					lst.set(y + 1, temp);
				}
			}

		}
		form.setIfcs(lst);
	}
}
