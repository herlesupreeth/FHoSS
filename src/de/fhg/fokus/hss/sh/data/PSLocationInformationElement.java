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
public class PSLocationInformationElement {

	private String cellGlobalID = null;
	private String serviceAreaID = null;
	private String locationAreaID = null;
	private String routingAreaID = null;
	private String geographicalInformation = null;
	private String geodeticInformation = null;
	private String sgsnNumber = null;
	private int currentLocationRetrieved = -1;
	private int ageOfLocationInformation = -1;
	
	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.PSLocationInformation_s);
		
		if (cellGlobalID != null){
			sBuffer.append(ShDataTags.CellGlobalId_s);
			sBuffer.append(cellGlobalID);
			sBuffer.append(ShDataTags.CellGlobalId_e);
		}
		if (serviceAreaID != null){
			sBuffer.append(ShDataTags.ServiceAreaId_s);
			sBuffer.append(serviceAreaID);
			sBuffer.append(ShDataTags.ServiceAreaId_e);
		}
		if (locationAreaID != null){
			sBuffer.append(ShDataTags.LocationAreaId_s);
			sBuffer.append(locationAreaID);
			sBuffer.append(ShDataTags.LocationAreaId_e);
		}
		if (routingAreaID != null){
			sBuffer.append(ShDataTags.RoutingAreaId_s);
			sBuffer.append(routingAreaID);
			sBuffer.append(ShDataTags.RoutingAreaId_e);
		}
		
		if (geographicalInformation != null){
			sBuffer.append(ShDataTags.GeographicalInformation_s);
			sBuffer.append(geographicalInformation);
			sBuffer.append(ShDataTags.GeographicalInformation_e);			
		}
		
		if (geodeticInformation != null){
			sBuffer.append(ShDataTags.GeodeticInformation_s);
			sBuffer.append(geodeticInformation);
			sBuffer.append(ShDataTags.GeodeticInformation_e);
		}
		
		if (sgsnNumber != null){
			sBuffer.append(ShDataTags.SGSNNumber_s);
			sBuffer.append(sgsnNumber);
			sBuffer.append(ShDataTags.SGSNNumber_e);
		}
		
		if (currentLocationRetrieved != -1){
			sBuffer.append(ShDataTags.CurrentLocationRetrieved_s);
			sBuffer.append(currentLocationRetrieved);
			sBuffer.append(ShDataTags.CurrentLocationRetrieved_e);
		}
		
		if (ageOfLocationInformation != -1){
			sBuffer.append(ShDataTags.AgeOfLocationInformation_s);
			sBuffer.append(ageOfLocationInformation);
			sBuffer.append(ShDataTags.AgeOfLocationInformation_e);
		}
		sBuffer.append(ShDataTags.PSLocationInformation_e);
		return sBuffer.toString();
	}

	public int getAgeOfLocationInformation() {
		return ageOfLocationInformation;
	}

	public void setAgeOfLocationInformation(int ageOfLocationInformation) {
		this.ageOfLocationInformation = ageOfLocationInformation;
	}

	public String getCellGlobalID() {
		return cellGlobalID;
	}

	public void setCellGlobalID(String cellGlobalID) {
		this.cellGlobalID = cellGlobalID;
	}

	public int getCurrentLocationRetrieved() {
		return currentLocationRetrieved;
	}

	public void setCurrentLocationRetrieved(int currentLocationRetrieved) {
		this.currentLocationRetrieved = currentLocationRetrieved;
	}

	public String getGeodeticInformation() {
		return geodeticInformation;
	}

	public void setGeodeticInformation(String geodeticInformation) {
		this.geodeticInformation = geodeticInformation;
	}

	public String getGeographicalInformation() {
		return geographicalInformation;
	}

	public void setGeographicalInformation(String geographicalInformation) {
		this.geographicalInformation = geographicalInformation;
	}

	public String getLocationAreaID() {
		return locationAreaID;
	}

	public void setLocationAreaID(String locationAreaID) {
		this.locationAreaID = locationAreaID;
	}

	public String getRoutingAreaID() {
		return routingAreaID;
	}

	public void setRoutingAreaID(String routingAreaID) {
		this.routingAreaID = routingAreaID;
	}

	public String getServiceAreaID() {
		return serviceAreaID;
	}

	public void setServiceAreaID(String serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}

	public String getSgsnNumber() {
		return sgsnNumber;
	}

	public void setSgsnNumber(String sgsnNumber) {
		this.sgsnNumber = sgsnNumber;
	}

	
}
