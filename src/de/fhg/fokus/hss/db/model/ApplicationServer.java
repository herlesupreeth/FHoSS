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

package de.fhg.fokus.hss.db.model;

import java.io.Serializable;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class ApplicationServer implements Serializable{
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private String server_name;
	private int default_handling;
	private String service_info;
	private String diameter_address;
	private int rep_data_size_limit;
	private int udr;
	private int pur;
	private int snr;
	private int udr_rep_data;
	private int udr_impu;
	private int udr_ims_user_state;
	private int udr_scscf_name;
	private int udr_ifc;
	private int udr_location;
	private int udr_user_state;
	private int udr_charging_info;
	private int udr_msisdn;
	private int udr_psi_activation;
	private int udr_dsai;
	private int udr_aliases_rep_data;
	private int pur_rep_data;
	private int pur_psi_activation;
	private int pur_dsai;
	private int pur_aliases_rep_data;
	private int snr_rep_data;
	private int snr_impu;
	private int snr_ims_user_state;
	private int snr_scscf_name;
	private int snr_ifc;
	private int snr_psi_activation;
	private int snr_dsai;
	private int snr_aliases_rep_data;
	private int include_register_request;
	private int include_register_response;
    
	private boolean dirtyFlag = false;
	
	public ApplicationServer(){
		// default values

		this.name = null;
		this.server_name = null;
		this.diameter_address = null;
		this.service_info = null;
		this.default_handling = -2;
		
		this.rep_data_size_limit = 100;
		this.udr = 0;
		this.pur = 0;
		this.snr = 0;
		this.udr_rep_data = 0;
		this.udr_impu = 0;
		this.udr_ims_user_state = 0;
		this.udr_scscf_name = 0;
		this.udr_ifc = 0;
		this.udr_location = 0;
		this.udr_user_state = 0;
		this.udr_charging_info = 0;
		this.udr_msisdn = 0;
		this.udr_psi_activation = 0;
		this.udr_dsai = 0;
		this.udr_aliases_rep_data = 0;
		this.pur_rep_data = 0;
		this.pur_psi_activation = 0;
		this.pur_dsai = 0;
		this.pur_aliases_rep_data = 0;
		this.snr_rep_data = 0;
		this.snr_impu = 0;
		this.snr_ims_user_state = 0;
		this.snr_scscf_name = 0;
		this.snr_ifc = 0;
		this.snr_psi_activation = 0;
		this.snr_dsai = 0;
		this.snr_aliases_rep_data = 0;
		this.include_register_request = 0;
		this.include_register_response = 0;
	}

	// getters & setters
	public int getDefault_handling() {
		return default_handling;
	}

	public void setDefault_handling(int default_handling) {
		if (this.default_handling != -2 && this.default_handling!= default_handling){
			dirtyFlag = true;
		}
		this.default_handling = default_handling;
	}

	public String getDiameter_address() {
		return diameter_address;
	}

	public void setDiameter_address(String diameter_address) {
		this.diameter_address = diameter_address;
	}

	void setId(int id){
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPur() {
		return pur;
	}

	public void setPur(int pur) {
		this.pur = pur;
	}

	public int getPur_aliases_rep_data() {
		return pur_aliases_rep_data;
	}

	public void setPur_aliases_rep_data(int pur_aliases_rep_data) {
		this.pur_aliases_rep_data = pur_aliases_rep_data;
	}

	public int getPur_dsai() {
		return pur_dsai;
	}

	public void setPur_dsai(int pur_dsai) {
		this.pur_dsai = pur_dsai;
	}

	public int getPur_psi_activation() {
		return pur_psi_activation;
	}

	public void setPur_psi_activation(int pur_psi_activation) {
		this.pur_psi_activation = pur_psi_activation;
	}

	public int getPur_rep_data() {
		return pur_rep_data;
	}

	public void setPur_rep_data(int pur_rep_data) {
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
		if (this.service_info != null && !this.service_info.equals(service_info)){
			dirtyFlag = true;
		}
		this.service_info = service_info;
	}

	public int getSnr() {
		return snr;
	}

	public void setSnr(int snr) {
		this.snr = snr;
	}

	public int getSnr_aliases_rep_data() {
		return snr_aliases_rep_data;
	}

	public void setSnr_aliases_rep_data(int snr_aliases_rep_data) {
		this.snr_aliases_rep_data = snr_aliases_rep_data;
	}

	public int getSnr_dsai() {
		return snr_dsai;
	}

	public void setSnr_dsai(int snr_dsai) {
		this.snr_dsai = snr_dsai;
	}

	public int getSnr_ifc() {
		return snr_ifc;
	}

	public void setSnr_ifc(int snr_ifc) {
		this.snr_ifc = snr_ifc;
	}

	public int getSnr_impu() {
		return snr_impu;
	}

	public void setSnr_impu(int snr_impu) {
		this.snr_impu = snr_impu;
	}

	public int getSnr_ims_user_state() {
		return snr_ims_user_state;
	}

	public void setSnr_ims_user_state(int snr_ims_user_state) {
		this.snr_ims_user_state = snr_ims_user_state;
	}

	public int getSnr_psi_activation() {
		return snr_psi_activation;
	}

	public void setSnr_psi_activation(int snr_psi_activation) {
		this.snr_psi_activation = snr_psi_activation;
	}

	public int getSnr_rep_data() {
		return snr_rep_data;
	}

	public void setSnr_rep_data(int snr_rep_data) {
		this.snr_rep_data = snr_rep_data;
	}

	public int getSnr_scscf_name() {
		return snr_scscf_name;
	}

	public void setSnr_scscf_name(int snr_scscf_name) {
		this.snr_scscf_name = snr_scscf_name;
	}

	public int getUdr() {
		return udr;
	}

	public void setUdr(int udr) {
		this.udr = udr;
	}

	public int getUdr_aliases_rep_data() {
		return udr_aliases_rep_data;
	}

	public void setUdr_aliases_rep_data(int udr_aliases_rep_data) {
		this.udr_aliases_rep_data = udr_aliases_rep_data;
	}

	public int getUdr_charging_info() {
		return udr_charging_info;
	}

	public void setUdr_charging_info(int udr_charging_info) {
		this.udr_charging_info = udr_charging_info;
	}

	public int getUdr_dsai() {
		return udr_dsai;
	}

	public void setUdr_dsai(int udr_dsai) {
		this.udr_dsai = udr_dsai;
	}

	public int getUdr_ifc() {
		return udr_ifc;
	}

	public void setUdr_ifc(int udr_ifc) {
		this.udr_ifc = udr_ifc;
	}

	public int getUdr_impu() {
		return udr_impu;
	}

	public void setUdr_impu(int udr_impu) {
		this.udr_impu = udr_impu;
	}

	public int getUdr_ims_user_state() {
		return udr_ims_user_state;
	}

	public void setUdr_ims_user_state(int udr_ims_user_state) {
		this.udr_ims_user_state = udr_ims_user_state;
	}

	public int getUdr_location() {
		return udr_location;
	}

	public void setUdr_location(int udr_location) {
		this.udr_location = udr_location;
	}

	public int getUdr_msisdn() {
		return udr_msisdn;
	}

	public void setUdr_msisdn(int udr_msisdn) {
		this.udr_msisdn = udr_msisdn;
	}

	public int getUdr_psi_activation() {
		return udr_psi_activation;
	}

	public void setUdr_psi_activation(int udr_psi_activation) {
		this.udr_psi_activation = udr_psi_activation;
	}

	public int getUdr_rep_data() {
		return udr_rep_data;
	}

	public void setUdr_rep_data(int udr_rep_data) {
		this.udr_rep_data = udr_rep_data;
	}

	public int getUdr_scscf_name() {
		return udr_scscf_name;
	}

	public void setUdr_scscf_name(int udr_scscf_name) {
		this.udr_scscf_name = udr_scscf_name;
	}

	public int getUdr_user_state() {
		return udr_user_state;
	}

	public void setUdr_user_state(int udr_user_state) {
		this.udr_user_state = udr_user_state;
	}
    
	public int getInclude_register_request() {
		return include_register_request;
	}

	public void setInclude_register_request(int include_register_request) {
		this.include_register_request = include_register_request;
	}
    
	public int getInclude_register_response() {
		return include_register_response;
	}

	public void setInclude_register_response(int include_register_response) {
		this.include_register_response = include_register_response;    
	}
    
	public boolean isDirtyFlag() {
		return dirtyFlag;
	}

	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	
}
