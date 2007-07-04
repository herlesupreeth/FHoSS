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

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.TP;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.TP_DAO;
import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class TP_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(TP_Form.class);
	private static final long serialVersionUID=1L;
	
	// TP properties
	private int id;
	private String name;
	private int condition_type_cnf;
	
	// SPT properties	
	private transient List spts;
	private int type;
	private int group;
	
	private int ifc_id;
	private int associated_ID;
	private List select_ifc;
	private List select_spt;
	private String nextAction;
	
	private static List select_condition_type;
	static {
		select_condition_type = WebConstants.select_condition_type_cnf;
	}
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = null;
    	this.condition_type_cnf = CxConstants.ConditionType_CNF;

    	Factory factory = new Factory() {
                public Object create() {
                    return new SPT_Form();
                }
        };

        this.spts = LazyList.decorate(new ArrayList(), factory);
    	this.type = 0;
    	this.group = 1;
    	this.nextAction = null;
    	this.ifc_id = -1;
    	this.associated_ID = -1;
    	this.select_ifc = null;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	
        		if (nextAction.equals("save")){
        			if (name == null || name.equals("")){
        				actionErrors.add("tp.error.name", new ActionMessage("tp.error.name"));
        			}
        			TP tp = TP_DAO.get_by_Name(session, name);
        			if (tp != null && tp.getId() != id){
        				actionErrors.add("tp.error.duplicate_name", new ActionMessage("tp.error.duplicate_name"));
        			}
        		}
        		else if (nextAction.equals("attach_ifc")){
        			if (ifc_id == -1){
        				actionErrors.add("tp.error.invalid_ifc", new ActionMessage("tp.error.invalid_ifc"));
        			}
        			IFC ifc = IFC_DAO.get_by_ID(session, ifc_id);
        			if (ifc != null && ifc.getId_tp() > 0){
        				actionErrors.add("tp.error.duplicate_tp_association", new ActionMessage("tp.error.duplicate_tp_association"));
        			}
        		}

        		if (spts != null){
        			// validate SPTs
        			Iterator it = spts.iterator();
        			while (it.hasNext()){
        				SPT_Form sptFrom = (SPT_Form) it.next();
        				ActionErrors sptActionErrors = sptFrom.validate(actionMapping, request);

        				if (sptActionErrors != null && sptActionErrors.size() > 0){
        					actionErrors.add(sptActionErrors);
        				}
        			}
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

    // getters & setters
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCondition_type_cnf() {
		return condition_type_cnf;
	}

	public void setCondition_type_cnf(int condition_type_cnf) {
		this.condition_type_cnf = condition_type_cnf;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}

	public int getIfc_id() {
		return ifc_id;
	}

	public void setIfc_id(int ifc_id) {
		this.ifc_id = ifc_id;
	}

	public List getSelect_ifc() {
		return select_ifc;
	}

	public void setSelect_ifc(List select_ifc) {
		this.select_ifc = select_ifc;
	}

	public List getSelect_condition_type() {
		return select_condition_type;
	}

	public List getSelect_spt() {
		return select_spt;
	}

	public void setSelect_spt(List select_spt) {
		this.select_spt = select_spt;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public List getSpts() {
		return spts;
	}

	public void setSpts(List spts) {
		this.spts = spts;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
