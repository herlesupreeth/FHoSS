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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.model.Ifc;
import de.fhg.fokus.hss.model.Ifc2svp;
import de.fhg.fokus.hss.model.Ifc2svpPK;
import de.fhg.fokus.hss.model.Svp;
import de.fhg.fokus.hss.model.SvpBO;
import de.fhg.fokus.hss.util.HibernateUtil;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class Ifc2svpSubmitAction extends HssAction
{
	private static final Logger LOGGER = Logger
			.getLogger(Ifc2svpSubmitAction.class);

	/**
	 * Get the roam string list from request and store them to selected impi.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("entering");

		// Get assigned ifcs strings list from request
		String[] assignedIfcs = request.getParameterValues("assignedIfcs");
		Integer svpId = Integer.valueOf(request.getParameter("svpId"));

		Iterator it = null;
		Svp svp;

		try{
			boolean commitSvpChanges = false;
			
			HibernateUtil.beginTransaction();
			svp = (Svp) HibernateUtil.getCurrentSession().get(Svp.class, svpId);
			Set oldIfcsList = svp.getIfc2svps();

			if (assignedIfcs != null){
				// get a list with all roaming network
				it = HibernateUtil.getCurrentSession()
						.createQuery("select ifc from de.fhg.fokus.hss.model.Ifc as ifc where ifc.ifcId in (:ifcsList)")
						.setParameterList("ifcsList", assignedIfcs).list()
						.iterator();

				// Iterete throw selected Ifcs and add them with priority to
				while (it.hasNext()){
					Ifc ifc = (Ifc) it.next();
					Ifc2svp ifc2svp = null;
					Ifc2svpPK pk = new Ifc2svpPK(ifc.getIfcId(), svpId);

					ifc2svp = (Ifc2svp) HibernateUtil.getCurrentSession().get(Ifc2svp.class, pk);

					if (ifc2svp == null){
						ifc2svp = new Ifc2svp(pk, calculatePriorityId(assignedIfcs, ifc), svp, ifc);
						HibernateUtil.getCurrentSession().save(ifc2svp);
						commitSvpChanges = true;
					} 
					else{
						// remove from deselection list
						oldIfcsList.remove(ifc2svp);
						int oldPriority = ifc2svp.getPriority();
						int newPriority = calculatePriorityId(assignedIfcs, ifc);
						ifc2svp.setPriority(newPriority);
						commitSvpChanges = commitSvpChanges || (oldPriority != newPriority);
						HibernateUtil.getCurrentSession().update(ifc2svp);
					}
				}
			}
			commitSvpChanges = commitSvpChanges || deselectIfcs(oldIfcsList);
			HibernateUtil.commitTransaction();
			
			if (commitSvpChanges){
				SvpBO svpBO = new SvpBO();
				svpBO.commitCxChanges(svp);
			}
		}
		finally{
			HibernateUtil.closeSession();
		}
		
		// forward to impiShow with specific svpid
		ActionForward forward = mapping.findForward(FORWARD_SUCCESS);
		forward = new ActionForward(forward.getPath() + "?svpId=" + svpId, true);
		LOGGER.debug("exiting");
		
		return forward;
	}

	/**
	 * Calculates the priority id
	 * @param assignedIfcs
	 * @param ifc
	 * @return
	 */
	private int calculatePriorityId(String[] assignedIfcs, Ifc ifc)
	{
		for (int ix = 0; ix < assignedIfcs.length; ix++){
			if (ifc.getIfcId().intValue() == Integer.parseInt(assignedIfcs[ix])){
				return ix;
			}
		}

		return 0;
	}

	/**
	 * Removes the Ifc
	 * @param oldIfcsList
	 */
	private boolean deselectIfcs(Set oldIfcsList){
		Iterator it;
		it = oldIfcsList.iterator();
		boolean deleted = false;

		while (it.hasNext()){
			HibernateUtil.getCurrentSession().delete((Ifc2svp) it.next());
			deleted = true;
		}
		return deleted;
	}
}
