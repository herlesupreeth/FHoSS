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
public class IFC implements Serializable {
	private static final long serialVersionUID=1L;
	
	private int id;
	private String name;
	private Integer profile_part_ind = -2;
	private Integer id_application_server = -2;
	private Integer id_tp = -2;
	
	private boolean dirtyFlag = false;
	// old_id_application_server is needed in Sh-Notification
	private int old_id_application_server = -2;
	public IFC(){}

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

	public int getProfile_part_ind() {
		return profile_part_ind;
	}

	public void setProfile_part_ind(Integer profile_part_ind) {
		if (profile_part_ind == null){
			// profile part indicator 
			this.profile_part_ind = -1;
		}
		else{
			if (this.profile_part_ind != -2 && this.profile_part_ind != profile_part_ind.intValue()){
				dirtyFlag = true;
			}
			this.profile_part_ind = profile_part_ind;
		}
	}

	public int getId_tp() {
		return id_tp;
	}

	public void setId_tp(Integer id_tp) {
		if (id_tp == null){
			this.id_tp = -1;
		}
		else {
			if (this.id_tp != -2 && this.id_tp != id_tp.intValue()){
				dirtyFlag = true;
			}
			this.id_tp = id_tp;
		}
	}

	public int getId_application_server() {
		return id_application_server;
	}

	public void setId_application_server(Integer id_application_server) {
		if (id_application_server == null){
			this.id_application_server = -1;
		}
		else{
			if (this.id_application_server != -2 && this.id_application_server != id_application_server.intValue()){
				this.old_id_application_server = this.id_application_server;
				dirtyFlag = true;
			}
			this.id_application_server = id_application_server;
		}
	}

	public boolean isDirtyFlag() {
		return dirtyFlag;
	}

	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}

	public int getOld_id_application_server() {
		return old_id_application_server;
	}

	public void setOld_id_application_server(int old_id_application_server) {
		this.old_id_application_server = old_id_application_server;
	}
	
	
	

}

