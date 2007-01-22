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
package de.fhg.fokus.hss.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.main.HSSProperties;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * This class provides database specific functions for public service identity specific
 * transactions
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class PsiBO extends Psi
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(PsiBO.class);
    /** uri for default psi impi */
    public static URI DEFAULT_PSI_IMPI;
    /** uri for default primary collection charging function */
    public static URI DEFAULT_PRI_COLL_CHRG_FN;
    static {
    	try {
				DEFAULT_PSI_IMPI = new URI(HSSProperties.PSI_DEFAULT_IMPI);
				DEFAULT_PRI_COLL_CHRG_FN = new URI(HSSProperties.PSI_DEFAULT_PRI_COLL_CHRG_FN);
			} catch (URISyntaxException e) {
				LOGGER.error(PsiBO.class, e);
			}
    }
    
    /**
     * Do link or unlink impu to psi
     * @param psiPk primary key for primary service identity
     * @param impuPk primary key for public identity
     * @param isLink indicator for link or unlink
     */
    public static void linkImpu2Psi(
        Integer psiPk, Integer impuPk, boolean isLink)
    {
        Session session = null;
        session = HibernateUtil.currentSession();

        Transaction tx = session.beginTransaction();

        Psi psi = (Psi) session.load(Psi.class, psiPk, LockMode.UPGRADE);
        Impu impu = (Impu) session.load(Impu.class, impuPk);

        if (isLink)
        {
            LOGGER.debug("add impu to psi");
            psi.getImpus().add(impu);
        }
        else
        {
            LOGGER.debug("remove impu from psi");
            psi.getImpus().remove(impu);
        }

        session.saveOrUpdate(psi);
        tx.commit();
        session.flush();

        HibernateUtil.closeSession();
    }
}
