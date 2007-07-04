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
public class ShNotification implements Serializable{
	private static final long serialVersionUID=1L;
	
	private int id;
	private int id_impu;
	private int id_application_server;
	private int data_ref;
	private String service_indication;
	private byte[] rep_data;
	private int sqn;
	private int id_ifc;
	private String server_name;
	private String scscf_name;
	private int reg_state;
	private int psi_activation;
	private String dsai_tag;
	private int dsai_value;
	private long hopbyhop;
	private long endtoend;
	private int grp;

	public ShNotification(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_application_server() {
		return id_application_server;
	}

	public void setId_application_server(int id_application_server) {
		this.id_application_server = id_application_server;
	}

	public int getId_ifc() {
		return id_ifc;
	}

	public void setId_ifc(int id_ifc) {
		this.id_ifc = id_ifc;
	}

	public int getId_impu() {
		return id_impu;
	}

	public void setId_impu(int id_impu) {
		this.id_impu = id_impu;
	}

	public int getReg_state() {
		return reg_state;
	}

	public void setReg_state(int reg_state) {
		this.reg_state = reg_state;
	}


	public byte[] getRep_data() {
		return rep_data;
	}

	public void setRep_data(byte[] rep_data) {
		this.rep_data = rep_data;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public int getData_ref() {
		return data_ref;
	}

	public void setData_ref(int data_ref) {
		this.data_ref = data_ref;
	}

	public long getEndtoend() {
		return endtoend;
	}

	public void setEndtoend(long endtoend) {
		this.endtoend = endtoend;
	}

	public long getHopbyhop() {
		return hopbyhop;
	}

	public void setHopbyhop(long hopbyhop) {
		this.hopbyhop = hopbyhop;
	}

	public int getDsai_value() {
		return dsai_value;
	}

	public void setDsai_value(int dsai_value) {
		this.dsai_value = dsai_value;
	}

	public int getGrp() {
		return grp;
	}

	public void setGrp(int grp) {
		this.grp = grp;
	}

	public int getPsi_activation() {
		return psi_activation;
	}

	public void setPsi_activation(int psi_activation) {
		this.psi_activation = psi_activation;
	}

	public String getDsai_tag() {
		return dsai_tag;
	}

	public void setDsai_tag(String dsai_tag) {
		this.dsai_tag = dsai_tag;
	}

	public String getService_indication() {
		return service_indication;
	}

	public void setService_indication(String service_indication) {
		this.service_indication = service_indication;
	}

	public int getSqn() {
		return sqn;
	}

	public void setSqn(int sqn) {
		this.sqn = sqn;
	}

	public String getScscf_name() {
		return scscf_name;
	}

	public void setScscf_name(String scscf_name) {
		this.scscf_name = scscf_name;
	}
	
	
}

