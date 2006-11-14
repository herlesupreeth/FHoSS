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
package de.fhg.fokus.hss.form;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionMapping;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class ImpuSubSelectForm extends HssForm
{
    private static final Logger LOGGER =
        Logger.getLogger(ImpuSubSelectForm.class);

    /**
     * impu lookup selector
     */
    private String impuSelect;
    private Integer imsuId;

    /**
     * impu url for lookup
     */
    private String impuUrl;
    private List impusSelected;

    /**
     * impu selected id
     */
    private String impuSelectId;
    private Set impus;

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        imsuId = null;
        impus = new TreeSet();
        impusSelected = new ArrayList();
        impuSelect = "false";
        impuUrl = null;
        impuSelectId = null;
        LOGGER.debug("exiting");
    }

    public Set getImpus()
    {
        return impus;
    }

    public void setImpus(Set impus)
    {
        this.impus = impus;
    }

    public String getImpuSelect()
    {
        return impuSelect;
    }

    public void setImpuSelect(String impuSelect)
    {
        this.impuSelect = impuSelect;
    }

    public String getImpuSelectId()
    {
        return impuSelectId;
    }

    public void setImpuSelectId(String impuSelectId)
    {
        this.impuSelectId = impuSelectId;
    }

    public List getImpusSelected()
    {
        return impusSelected;
    }

    public void setImpusSelected(List impusSelected)
    {
        this.impusSelected = impusSelected;
    }

    public String getImpuUrl()
    {
        return impuUrl;
    }

    public void setImpuUrl(String impuUrl)
    {
        this.impuUrl = impuUrl;
    }

    public Integer getPrimaryKey() throws NullPointerException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public static void doImpuSelection(
        Session session, ImpuSubSelectForm form, Set givenImpus)
    {
        if (
            (form.getImpuSelect().equals("true"))
                && (
                    (form.getImpuUrl() != null)
                    && (form.getImpuUrl().length() > 0)
                ))
        {
            List results = null;

            if (form.getImsuId() != null)
            {
                // Prepare Querry
                Query query =
                    session.createQuery(
                        "select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ? and impu.impis.imsu.imsuId = ?");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                query.setInteger(1, form.getImsuId());
                query.setMaxResults(20);
                results = query.list();
                query =
                    session.createQuery(
                        "select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ? and impu.impis is empty");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                results.addAll(query.list());
            }
            else
            {
                // Prepare Querry
                Query query =
                    session.createQuery(
                        "select impu from de.fhg.fokus.hss.model.Impu as impu where impu.psi = 0 and impu.sipUrl like ?");
                query.setString(0, "%" + form.getImpuUrl() + "%");
                query.setMaxResults(20);
                results = query.list();
            }

            results.removeAll(givenImpus);
            form.setImpusSelected(results);

            LOGGER.debug(form);
        }
    }

    public Integer getImsuId()
    {
        return imsuId;
    }

    public void setImsuId(Integer imsuId)
    {
        this.imsuId = imsuId;
    }
}
