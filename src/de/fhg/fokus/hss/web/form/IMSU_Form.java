/*
  *  Copyright (C) 2004-2007 FhG Fokus
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

package de.fhg.fokus.hss.web.form;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.op.IMSU_DAO;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IMSU_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(IMSU_Form.class);
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private String scscf_name;
	private String diameter_name;
	private int id_capabilities_set;
	private int id_preferred_scscf;

	private List select_capabilities_set;
	private List select_preferred_scscf;
	private String impi_identity;
	private String nextAction;
	private int associated_ID;
	
    public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = null;
    	this.scscf_name = null;
    	this.diameter_name = null;
    	this.id_capabilities_set = -1;
    	this.id_preferred_scscf = -1;
    	this.select_capabilities_set = null;
    	this.select_preferred_scscf = null;
    	this.impi_identity = null;
    	this.associated_ID = -1;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        if (name == null || name.equals("")){
        	actionErrors.add("name", new ActionMessage("imsu_form.error.name"));
        }
        
        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	
        	IMSU imsu = IMSU_DAO.get_by_Name(session, name);
        	if (imsu != null && imsu.getId() != id){
        		actionErrors.add("imsu_form.error.duplicate_identity", new ActionMessage("imsu_form.error.duplicate_identity"));	
        	}
        }
		catch(DatabaseException e){
			logger.error("Database Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}        
        return actionErrors;
    }

	public String getDiameter_name() {
		return diameter_name;
	}

	public void setDiameter_name(String diameter_name) {
		this.diameter_name = diameter_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_capabilities_set() {
		return id_capabilities_set;
	}

	public void setId_capabilities_set(int id_capabilities_set) {
		this.id_capabilities_set = id_capabilities_set;
	}

	public int getId_preferred_scscf() {
		return id_preferred_scscf;
	}

	public void setId_preferred_scscf(int id_preferred_scscf) {
		this.id_preferred_scscf = id_preferred_scscf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getScscf_name() {
		return scscf_name;
	}

	public void setScscf_name(String scscf_name) {
		this.scscf_name = scscf_name;
	}

	public List getSelect_capabilities_set() {
		return select_capabilities_set;
	}

	public void setSelect_capabilities_set(List select_capabilities_set) {
		this.select_capabilities_set = select_capabilities_set;
	}

	public List getSelect_preferred_scscf() {
		return select_preferred_scscf;
	}

	public void setSelect_preferred_scscf(List select_preferred_scscf) {
		this.select_preferred_scscf = select_preferred_scscf;
	}

	public String getImpi_identity() {
		return impi_identity;
	}

	public void setImpi_identity(String impi_identity) {
		this.impi_identity = impi_identity;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}
	
}
