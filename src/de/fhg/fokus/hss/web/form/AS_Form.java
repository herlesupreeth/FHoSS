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

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class AS_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(AS_Form.class);
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private String server_name;
	private int default_handling;
	private String service_info;
	private String diameter_address;
	private int rep_data_size_limit;
	private boolean udr;
	private boolean pur;
	private boolean snr;
	private boolean udr_rep_data;
	private boolean udr_impu;
	private boolean udr_ims_user_state;
	private boolean udr_scscf_name;
	private boolean udr_ifc;
	private boolean udr_location;
	private boolean udr_user_state;
	private boolean udr_charging_info;
	private boolean udr_msisdn;
	private boolean udr_psi_activation;
	private boolean udr_dsai;
	private boolean udr_aliases_rep_data;
	private boolean pur_rep_data;
	private boolean pur_psi_activation;
	private boolean pur_dsai;
	private boolean pur_aliases_rep_data;
	private boolean snr_rep_data;
	private boolean snr_impu;
	private boolean snr_ims_user_state;
	private boolean snr_scscf_name;
	private boolean snr_ifc;
	private boolean snr_psi_activation;
	private boolean snr_dsai;
	private boolean snr_aliases_rep_data;
	private boolean include_register_request;
	private boolean include_register_response;

	private String nextAction;
	private List select_default_handling;
	private List select_ifc;
	private int ifc_id;
	private int associated_ID;
	
	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.name = "";
    	this.server_name = "";
    	this.default_handling = CxConstants.Default_Handling_Session_Continued;
    	this.service_info = "";
    	this.diameter_address = "";
    	this.rep_data_size_limit = 1024;
    	this.udr = false;
    	this.pur = false;
    	this.snr = false;
    	this.udr_rep_data = false;
    	this.udr_impu = false;
    	this.udr_ims_user_state = false;
    	this.udr_scscf_name = false;
    	this.udr_ifc = false;
    	this.udr_location = false;
    	this.udr_user_state = false;
    	this.udr_charging_info = false;
    	this.udr_msisdn = false;
    	this.udr_psi_activation = false;
    	this.udr_dsai = false;
    	this.udr_aliases_rep_data = false;
    	this.pur_rep_data = false;
    	this.pur_psi_activation = false;
    	this.pur_dsai = false;
    	this.pur_aliases_rep_data = false;
    	this.snr_rep_data = false;
    	this.snr_impu = false;
    	this.snr_ims_user_state = false;
    	this.snr_scscf_name = false;
    	this.snr_ifc = false;
    	this.snr_psi_activation = false;
    	this.snr_dsai = false;
    	this.snr_aliases_rep_data = false;
    	this.include_register_request = false;
    	this.include_register_response = false;
    
    	this.nextAction = null;
    	this.select_default_handling = WebConstants.select_default_handling;
    	this.select_ifc = null;
    	this.ifc_id = -1;
    	this.associated_ID = -1;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();
        
        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	
        	if (nextAction.equals("save")){
        		
                if (name == null || name.equals("")){
                	actionErrors.add("as.error.name", new ActionMessage("as.error.name"));
                }
                if (server_name == null || server_name.equals("")){
                	actionErrors.add("as.error.server_name", new ActionMessage("as.error.server_name"));
                }
                if (server_name != null && (!server_name.toLowerCase().startsWith("sip:") || server_name.substring(4).equals(""))){
                	// check if the server_name is a "sip:" URI
                	actionErrors.add("as.error.incorrect_server_name", new ActionMessage("as.error.incorrect_server_name"));
                }
                if (diameter_address == null || diameter_address.equals("")){
                	actionErrors.add("as.error.diameter_address", new ActionMessage("as.error.diameter_address"));
                }

                // check if this AS Name was already used for other AS
        		ApplicationServer as = ApplicationServer_DAO.get_by_Name(session, name);
        		if (as != null && as.getId() != id){
        			actionErrors.add("as.error.duplicate_as_name", new ActionMessage("as.error.duplicate_as_name"));
        		}
        		
        		as = ApplicationServer_DAO.get_by_Diameter_Address(session, diameter_address);
        		if (as != null && as.getId() != id){
        			actionErrors.add("as.error.duplicate_diameter_name", new ActionMessage("as.error.duplicate_diameter_name"));
        		}
        	}
        	else if (nextAction.equals("attach_ifc")){
        		if (ifc_id == -1){
        			actionErrors.add("as.error.invalid_ifc_id", new ActionMessage("as.error.invalid_ifc_id"));
        		}
        		
        		IFC ifc = IFC_DAO.get_by_ID(session, ifc_id);
        		if (ifc != null && ifc.getId_application_server() > 1){
        			actionErrors.add("as.error.ifc_has_already_association", new ActionMessage("as.error.ifc_has_already_association"));	
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

	public int getDefault_handling() {
		return default_handling;
	}

	public void setDefault_handling(int default_handling) {
		this.default_handling = default_handling;
	}

	public String getDiameter_address() {
		return diameter_address;
	}

	public void setDiameter_address(String diameter_address) {
		this.diameter_address = diameter_address;
	}

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

	public boolean isPur() {
		return pur;
	}

	public void setPur(boolean pur) {
		this.pur = pur;
	}

	public boolean isPur_aliases_rep_data() {
		return pur_aliases_rep_data;
	}

	public void setPur_aliases_rep_data(boolean pur_aliases_rep_data) {
		this.pur_aliases_rep_data = pur_aliases_rep_data;
	}

	public boolean isPur_dsai() {
		return pur_dsai;
	}

	public void setPur_dsai(boolean pur_dsai) {
		this.pur_dsai = pur_dsai;
	}

	public boolean isPur_psi_activation() {
		return pur_psi_activation;
	}

	public void setPur_psi_activation(boolean pur_psi_activation) {
		this.pur_psi_activation = pur_psi_activation;
	}

	public boolean isPur_rep_data() {
		return pur_rep_data;
	}

	public void setPur_rep_data(boolean pur_rep_data) {
		this.pur_rep_data = pur_rep_data;
	}

	public int getRep_data_size_limit() {
		return rep_data_size_limit;
	}

	public void setRep_data_size_limit(int rep_data_size_limit) {
		this.rep_data_size_limit = rep_data_size_limit;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getService_info() {
		return service_info;
	}

	public void setService_info(String service_info) {
		this.service_info = service_info;
	}

	public boolean isSnr() {
		return snr;
	}

	public void setSnr(boolean snr) {
		this.snr = snr;
	}

	public boolean isSnr_aliases_rep_data() {
		return snr_aliases_rep_data;
	}

	public void setSnr_aliases_rep_data(boolean snr_aliases_rep_data) {
		this.snr_aliases_rep_data = snr_aliases_rep_data;
	}

	public boolean isSnr_dsai() {
		return snr_dsai;
	}

	public void setSnr_dsai(boolean snr_dsai) {
		this.snr_dsai = snr_dsai;
	}

	public boolean isSnr_ifc() {
		return snr_ifc;
	}

	public void setSnr_ifc(boolean snr_ifc) {
		this.snr_ifc = snr_ifc;
	}

	public boolean isSnr_impu() {
		return snr_impu;
	}

	public void setSnr_impu(boolean snr_impu) {
		this.snr_impu = snr_impu;
	}

	public boolean isSnr_ims_user_state() {
		return snr_ims_user_state;
	}

	public void setSnr_ims_user_state(boolean snr_ims_user_state) {
		this.snr_ims_user_state = snr_ims_user_state;
	}

	public boolean isSnr_psi_activation() {
		return snr_psi_activation;
	}

	public void setSnr_psi_activation(boolean snr_psi_activation) {
		this.snr_psi_activation = snr_psi_activation;
	}

	public boolean isSnr_rep_data() {
		return snr_rep_data;
	}

	public void setSnr_rep_data(boolean snr_rep_data) {
		this.snr_rep_data = snr_rep_data;
	}

	public boolean isSnr_scscf_name() {
		return snr_scscf_name;
	}

	public void setSnr_scscf_name(boolean snr_scscf_name) {
		this.snr_scscf_name = snr_scscf_name;
	}

	public boolean isUdr() {
		return udr;
	}

	public void setUdr(boolean udr) {
		this.udr = udr;
	}

	public boolean isUdr_aliases_rep_data() {
		return udr_aliases_rep_data;
	}

	public void setUdr_aliases_rep_data(boolean udr_aliases_rep_data) {
		this.udr_aliases_rep_data = udr_aliases_rep_data;
	}

	public boolean isUdr_charging_info() {
		return udr_charging_info;
	}

	public void setUdr_charging_info(boolean udr_charging_info) {
		this.udr_charging_info = udr_charging_info;
	}

	public boolean isUdr_dsai() {
		return udr_dsai;
	}

	public void setUdr_dsai(boolean udr_dsai) {
		this.udr_dsai = udr_dsai;
	}

	public boolean isUdr_ifc() {
		return udr_ifc;
	}

	public void setUdr_ifc(boolean udr_ifc) {
		this.udr_ifc = udr_ifc;
	}

	public boolean isUdr_impu() {
		return udr_impu;
	}

	public void setUdr_impu(boolean udr_impu) {
		this.udr_impu = udr_impu;
	}

	public boolean isUdr_ims_user_state() {
		return udr_ims_user_state;
	}

	public void setUdr_ims_user_state(boolean udr_ims_user_state) {
		this.udr_ims_user_state = udr_ims_user_state;
	}

	public boolean isUdr_location() {
		return udr_location;
	}

	public void setUdr_location(boolean udr_location) {
		this.udr_location = udr_location;
	}

	public boolean isUdr_msisdn() {
		return udr_msisdn;
	}

	public void setUdr_msisdn(boolean udr_msisdn) {
		this.udr_msisdn = udr_msisdn;
	}

	public boolean isUdr_psi_activation() {
		return udr_psi_activation;
	}

	public void setUdr_psi_activation(boolean udr_psi_activation) {
		this.udr_psi_activation = udr_psi_activation;
	}

	public boolean isUdr_rep_data() {
		return udr_rep_data;
	}

	public void setUdr_rep_data(boolean udr_rep_data) {
		this.udr_rep_data = udr_rep_data;
	}

	public boolean isUdr_scscf_name() {
		return udr_scscf_name;
	}

	public void setUdr_scscf_name(boolean udr_scscf_name) {
		this.udr_scscf_name = udr_scscf_name;
	}

	public boolean isUdr_user_state() {
		return udr_user_state;
	}

	public void setUdr_user_state(boolean udr_user_state) {
		this.udr_user_state = udr_user_state;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public List getSelect_default_handling() {
		return select_default_handling;
	}

	public void setSelect_default_handling(List select_default_handling) {
		this.select_default_handling = select_default_handling;
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

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}
    
	public boolean isInclude_register_request() {
	    return include_register_request;
	}

	public void setInclude_register_request(boolean include_register_request) {
	    this.include_register_request = include_register_request;
	}
    
	public boolean isInclude_register_response() {
		return include_register_response;
	}

	public void setInclude_register_response(boolean include_register_response) {
		this.include_register_response = include_register_response;
	}
}
