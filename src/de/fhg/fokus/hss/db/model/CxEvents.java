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
public class CxEvents implements Serializable{
	private static final long serialVersionUID=1L;
	
	private int id;
	private long hopbyhop;
	private long endtoend;
	private int id_impu;
	private int id_impi;
	private int id_implicit_set;
	private int type;
	private int subtype;
	private int grp;
	private String reason_info;
	private int trials_cnt;
	private String diameter_name;
	
	public CxEvents(){}

	public long getEndtoend() {
		return endtoend;
	}

	public void setEndtoend(long endtoend) {
		this.endtoend = endtoend;
	}

	public int getGrp() {
		return grp;
	}

	public void setGrp(int grp) {
		this.grp = grp;
	}

	public long getHopbyhop() {
		return hopbyhop;
	}

	public void setHopbyhop(long hopbyhop) {
		this.hopbyhop = hopbyhop;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_impi() {
		return id_impi;
	}

	public void setId_impi(int id_impi) {
		this.id_impi = id_impi;
	}

	public int getId_impu() {
		return id_impu;
	}

	public void setId_impu(int id_impu) {
		this.id_impu = id_impu;
	}

	public String getReason_info() {
		return reason_info;
	}

	public void setReason_info(String reason_info) {
		this.reason_info = reason_info;
	}

	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	public int getTrials_cnt() {
		return trials_cnt;
	}

	public void setTrials_cnt(int trials_cnt) {
		this.trials_cnt = trials_cnt;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId_implicit_set() {
		return id_implicit_set;
	}

	public void setId_implicit_set(int id_implicit_set) {
		this.id_implicit_set = id_implicit_set;
	}

	public String getDiameter_name() {
		return diameter_name;
	}

	public void setDiameter_name(String diameter_name) {
		this.diameter_name = diameter_name;
	}

	
}

