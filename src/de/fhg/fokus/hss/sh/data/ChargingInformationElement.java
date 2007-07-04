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
public class ChargingInformationElement {
	private String priECFName = null;
	private String secECFName = null;
	private String priCCFName = null;
	private String secCCFName = null;
	
	public ChargingInformationElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ChargingInformation_s);
		
		if (priECFName != null){
			sBuffer.append(ShDataTags.PrimaryEventChargingFunctionName_s);
			sBuffer.append(priECFName);
			sBuffer.append(ShDataTags.PrimaryEventChargingFunctionName_e);
		}
		
		if (secECFName != null){
			sBuffer.append(ShDataTags.SecondaryEventChargingFunctionName_s);
			sBuffer.append(secECFName);
			sBuffer.append(ShDataTags.SecondaryEventChargingFunctionName_e);
		}

		if (priCCFName != null){
			sBuffer.append(ShDataTags.PrimaryChargingCollectionFunctionName_s);
			sBuffer.append(priCCFName);
			sBuffer.append(ShDataTags.PrimaryChargingCollectionFunctionName_e);
		}
		
		if (secCCFName != null){
			sBuffer.append(ShDataTags.SecondaryChargingCollectionFunctionName_s);
			sBuffer.append(secCCFName);
			sBuffer.append(ShDataTags.SecondaryChargingCollectionFunctionName_e);
		}
		
		sBuffer.append(ShDataTags.ChargingInformation_e);
		return sBuffer.toString();
	}

	public String getPriCCFName() {
		return priCCFName;
	}

	public void setPriCCFName(String priCCFName) {
		this.priCCFName = priCCFName;
	}

	public String getPriECFName() {
		return priECFName;
	}

	public void setPriECFName(String priECFName) {
		this.priECFName = priECFName;
	}

	public String getSecCCFName() {
		return secCCFName;
	}

	public void setSecCCFName(String secCCFName) {
		this.secCCFName = secCCFName;
	}

	public String getSecECFName() {
		return secECFName;
	}

	public void setSecECFName(String secECFName) {
		this.secECFName = secECFName;
	}
	
	
}
