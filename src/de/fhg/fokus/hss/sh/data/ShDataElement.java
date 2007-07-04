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

import java.util.Vector;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class ShDataElement {

	private PublicIdentityElement publicIdentifiers = null;
	private Vector<RepositoryDataElement> repositoryDataList = null;
	private ShIMSDataElement shIMSData = null;
	private CSLocationInformationElement csLocationInformation = null;
	private PSLocationInformationElement psLocationInformation = null;
	private int csUserState = -1;
	private int psUserState = -1;
	private ShDataExtensionElement shDataExtension;
	
	public ShDataElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ShData_s);
		
		// append all the child elements
		if (publicIdentifiers != null){
			sBuffer.append(publicIdentifiers.toString());
		}
		if (repositoryDataList != null && repositoryDataList.size() > 0){
			for (int i = 0; i < repositoryDataList.size(); i++){
				sBuffer.append(repositoryDataList.get(i).toString());
			}
		}
		if (shIMSData != null){
			sBuffer.append(shIMSData.toString());
		}
		if (csLocationInformation != null){
			sBuffer.append(csLocationInformation.toString());
		}
		if (psLocationInformation != null){
			sBuffer.append(psLocationInformation.toString());
		}
		if (csUserState != -1){
			sBuffer.append(ShDataTags.CSUserState_s);
			sBuffer.append(csUserState);
			sBuffer.append(ShDataTags.CSUserState_e);
		}
		if (psUserState != -1){
			sBuffer.append(ShDataTags.PSUserState_s);
			sBuffer.append(psUserState);
			sBuffer.append(ShDataTags.CSUserState_e);
		}
		if (shDataExtension != null){
			sBuffer.append(shDataExtension.toString());
		}
		sBuffer.append(ShDataTags.ShData_e);
		return sBuffer.toString();
	}
	
	public void addRepositoryData(RepositoryDataElement data){
		if (repositoryDataList == null){
			repositoryDataList = new Vector<RepositoryDataElement>();
		}
		repositoryDataList.add(data);
	}
	
	// getters & setters
	public CSLocationInformationElement getCsLocationInformation() {
		return csLocationInformation;
	}

	public void setCsLocationInformation(CSLocationInformationElement csLocationInformation) {
		this.csLocationInformation = csLocationInformation;
	}

	public PSLocationInformationElement getPsLocationInformation() {
		return psLocationInformation;
	}

	public void setPsLocationInformation(PSLocationInformationElement psLocationInformation) {
		this.psLocationInformation = psLocationInformation;
	}

	public int getCsUserState() {
		return csUserState;
	}

	public void setCsUserState(int csUserState) {
		this.csUserState = csUserState;
	}

	public int getPsUserState() {
		return psUserState;
	}

	public void setPsUserState(int psUserState) {
		this.psUserState = psUserState;
	}

	public Vector<RepositoryDataElement> getRepositoryDataList() {
		return repositoryDataList;
	}
	public void setRepositoryDataList(Vector<RepositoryDataElement> repositoryDataList) {
		this.repositoryDataList = repositoryDataList;
	}

	public PublicIdentityElement getPublicIdentifiers() {
		return publicIdentifiers;
	}

	public void setPublicIdentifiers(PublicIdentityElement publicIdentifiers) {
		this.publicIdentifiers = publicIdentifiers;
	}


	public ShDataExtensionElement getShDataExtension() {
		return shDataExtension;
	}

	public void setShDataExtension(ShDataExtensionElement shDataExtension) {
		this.shDataExtension = shDataExtension;
	}

	public ShIMSDataElement getShIMSData() {
		return shIMSData;
	}

	public void setShIMSData(ShIMSDataElement shIMSData) {
		this.shIMSData = shIMSData;
	}
	
}
