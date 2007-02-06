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

import java.util.List;
import java.util.Set;

import de.fhg.fokus.hss.form.ImpuSubSelectForm;
import de.fhg.fokus.hss.form.PsiForm;
import de.fhg.fokus.hss.model.Psi;
import de.fhg.fokus.hss.model.PsiTempl;
import de.fhg.fokus.hss.util.HibernateUtil;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PsiShowAction extends HssAction
{
	private static final Logger LOGGER = Logger.getLogger(PsiShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		LOGGER.debug("entering");

		PsiForm form = (PsiForm) actionForm;
		LOGGER.debug(form);

		Integer primaryKey = form.getPrimaryKey();

		try
		{
			HibernateUtil.beginTransaction();
			if (primaryKey.intValue() != -1){
				Psi psi = (Psi) HibernateUtil.getCurrentSession().get(Psi.class, primaryKey);

				form.setPsiId(convString(psi.getPsiId()));
				form.setPsiName(psi.getName());
				form.setWildcard(psi.getWildcard());

				PsiTempl psiTempl = psi.getPsiTempl();
				form.setPsiTemplId(convString(psiTempl.getTemplId()));

				String impuName = psiTempl.getUsername() + psi.getWildcard() + "@" + psiTempl.getHostname();
				form.setImpuName(impuName);

				if (psi.getImpus().size() > 1){
					form.setImpus(psi.getImpus());
				} 
				else{
					form.getImpus().addAll(psi.getImpus());
				}
				doImpuSelection(HibernateUtil.getCurrentSession(), form, psi.getImpus());
			}
			form.setPsiTempls(HibernateUtil.getCurrentSession().createCriteria(PsiTempl.class).list());
			HibernateUtil.commitTransaction();
		}
		finally{
			HibernateUtil.closeSession();
		}

		if (LOGGER.isDebugEnabled()){
			LOGGER.debug(form);
			LOGGER.debug("exiting");
		}
		return mapping.findForward(FORWARD_SUCCESS);
	}
	
    public static void doImpuSelection(Session session, ImpuSubSelectForm form, Set givenImpus){
    	
        if ((form.getImpuSelect().equals("true")) && ((form.getImpuUrl() != null) && (form.getImpuUrl().length() > 0))){
            List results = null;

            if (form.getImsuId() != null){
                Query query = session.createQuery("select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ? and impu.impis.imsu.imsuId = ?");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                query.setInteger(1, form.getImsuId());
                query.setMaxResults(20);
                results = query.list();
                query = session.createQuery
                	("select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ? and impu.impis is empty");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                results.addAll(query.list());
            }
            else{
                // Prepare Querry
                Query query = session.createQuery("select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ?");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                query.setMaxResults(20);
                results = query.list();
            }

            results.removeAll(givenImpus);
            form.setImpusSelected(results);
            LOGGER.debug(form);
        }
    }

}
