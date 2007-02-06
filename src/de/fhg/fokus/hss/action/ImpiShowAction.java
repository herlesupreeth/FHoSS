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
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.fhg.fokus.hss.form.ImpiForm;
import de.fhg.fokus.hss.form.ImpuSubSelectForm;
import de.fhg.fokus.hss.model.Chrginfo;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.Network;
import de.fhg.fokus.hss.util.HibernateUtil;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpiShowAction extends HssAction
{
	private static final Logger LOGGER = Logger.getLogger(ImpiShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse)
			throws Exception
	{
		LOGGER.debug("entering");

		ImpiForm form = (ImpiForm) actionForm;
		LOGGER.debug(form);
		
		try
		{
			Integer primaryKey = form.getPrimaryKey();

			HibernateUtil.beginTransaction();
			if (primaryKey.intValue() != -1)
			{
				Impi impi = (Impi) HibernateUtil.getCurrentSession().load(Impi.class,
						form.getPrimaryKey());

				// copy basic values
				copyValues(form, impi);

				// Extract all impus from appended server profiles
				addImpus(form, impi);

				// Extract roaming network strings
				addRoams(form, impi);

				// Add impu selector values
				PsiShowAction.doImpuSelection(HibernateUtil.getCurrentSession(), form, impi.getImpus());
				
			}
			// add charging functions
			addChrgInfos(form);
			
			HibernateUtil.commitTransaction();
			
		} 
		finally{
			HibernateUtil.closeSession();
		}

		LOGGER.debug("exiting");

		return mapping.findForward(FORWARD_SUCCESS);
	}


	private void addChrgInfos(ImpiForm form){
		LOGGER.debug("entering");
		form.setChrgInfos(new ArrayList(HibernateUtil.getCurrentSession().createCriteria(Chrginfo.class).list()));
		LOGGER.debug("exiting");
	}

	/**
	 * Adds a impus
	 * @param form
	 * @param impi
	 */
	private void addImpus(ImpiForm form, Impi impi){
		if (impi.getImpus().size() > 1){
			form.setImpus(impi.getImpus());
		}
		else{
			form.getImpus().addAll(impi.getImpus());
		}
	}

	/**
	 * AddRoams
	 * @param form
	 * @param impi
	 */
	private void addRoams(ImpiForm form, Impi impi){
		Set roamNIds = impi.getRoams();
		Iterator it = roamNIds.iterator();

		while (it.hasNext()){
			Network roam = (Network) it.next();

			if (form.getRoamNetworkIdentifiers() == null){
				Set roams = new TreeSet();
				roams.add(roam.getNetworkString());
				form.setRoamNetworkIdentifiers(roams);
			} 
			else{
				form.getRoamNetworkIdentifiers().add(roam.getNetworkString());
			}
		}
	}

	/**
	 * CopyValues
	 * @param form
	 * @param impi
	 */
	private void copyValues(ImpiForm form, Impi impi){
		form.setImpiString(impi.getImpiString());
		form.setImsi(impi.getImsi());
		form.setScscfName(impi.getScscfName());
		form.setAlgorithm(impi.getAlgorithm());
		form.setAmf(impi.getAmf());
		form.setOperatorId(impi.getOperatorId());
		form.setSkey(impi.getSkey());
		form.setAuthScheme(impi.getAuthScheme());
		form.setSqn(impi.getSqn());
		form.setImsuId(impi.getImsu().getImsuId());

		if (impi.getChrginfo() != null){
			form.setChrgInfoId(convString(impi.getChrginfo().getChrgId()));
		}
	}
	
	
}
