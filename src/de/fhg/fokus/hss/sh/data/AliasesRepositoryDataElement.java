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
package de.fhg.fokus.hss.sh.data;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class AliasesRepositoryDataElement {
	private String serviceIndication = null;
	private int sqn = 1;
	/** can have any value; tag used is Service-Data */
	private String serviceData = null;
	
	public AliasesRepositoryDataElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.AliasesRepositoryData_s);
		if (serviceIndication != null){
			sBuffer.append(ShDataTags.ServiceIndication_s);
			sBuffer.append(serviceIndication);
			sBuffer.append(ShDataTags.ServiceIndication_e);
		}
		
		if (sqn > 0){
			sBuffer.append(ShDataTags.SequenceNumber_s);
			sBuffer.append(sqn);
			sBuffer.append(ShDataTags.SequenceNumber_e);
		}
		
		if (serviceData != null){
			sBuffer.append(ShDataTags.ServiceData_s);
			sBuffer.append(serviceData);
			sBuffer.append(ShDataTags.ServiceData_e);
		}
		
		sBuffer.append(ShDataTags.AliasesRepositoryData_e);
		return sBuffer.toString();
	}
	
	public String getServiceData() {
		return serviceData;
	}

	public void setServiceData(String serviceData) {
		this.serviceData = serviceData;
	}

	public String getServiceIndication() {
		return serviceIndication;
	}

	public void setServiceIndication(String serviceIndication) {
		this.serviceIndication = serviceIndication;
	}

	public int getSqn() {
		return sqn;
	}

	public void setSqn(int sqn) {
		this.sqn = sqn;
	}
	
	
}
