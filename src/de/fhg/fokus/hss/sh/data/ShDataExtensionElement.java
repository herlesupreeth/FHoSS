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

import java.util.Iterator;
import java.util.Vector;


/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class ShDataExtensionElement {
	private PublicIdentityElement registeredIdentities = null;
	private PublicIdentityElement implicitIdentities = null;
	private PublicIdentityElement allIdentities = null;
	private PublicIdentityElement aliasIdentities = null;
	private Vector<AliasesRepositoryDataElement> aliasesRepositoryDataList = null;
	
	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ShDataExtension_s);
		if (registeredIdentities != null){
			sBuffer.append(ShDataTags.RegisteredIdentities_s);
			sBuffer.append(registeredIdentities.toString());
			sBuffer.append(ShDataTags.RegisteredIdentities_e);
		}
		
		if (implicitIdentities != null){
			sBuffer.append(ShDataTags.ImplicitIdentities_s);
			sBuffer.append(implicitIdentities.toString());
			sBuffer.append(ShDataTags.ImplicitIdentities_e);
		}

		if (allIdentities != null){
			sBuffer.append(ShDataTags.AllIdentities_s);
			sBuffer.append(allIdentities.toString());
			sBuffer.append(ShDataTags.AllIdentities_e);
		}
		
		if (aliasIdentities != null){
			sBuffer.append(ShDataTags.AliasIdentities_s);
			sBuffer.append(aliasIdentities.toString());
			sBuffer.append(ShDataTags.AliasIdentities_e);
		}
		
		if (aliasesRepositoryDataList != null && aliasesRepositoryDataList.size() > 0){
			Iterator<AliasesRepositoryDataElement> it = aliasesRepositoryDataList.iterator();
			AliasesRepositoryDataElement transparentData;
			while (it.hasNext()){
				transparentData = it.next();
				sBuffer.append(transparentData.toString());
			}
		}
		
		sBuffer.append(ShDataTags.ShDataExtension_e);
		return sBuffer.toString();
	}

	public void addAliasesRepositoryData(AliasesRepositoryDataElement aliasesRepositoryData){
		if (aliasesRepositoryDataList == null){
			aliasesRepositoryDataList = new Vector<AliasesRepositoryDataElement>();
		}
		aliasesRepositoryDataList.add(aliasesRepositoryData);
	}
	
	public Vector<AliasesRepositoryDataElement> getAliasesRepositoryDataList() {
		return aliasesRepositoryDataList;
	}

	public void setAliasesRepositoryDataList(
			Vector<AliasesRepositoryDataElement> aliasesRepositoryDataList) {
		this.aliasesRepositoryDataList = aliasesRepositoryDataList;
	}

	public PublicIdentityElement getAliasIdentities() {
		return aliasIdentities;
	}

	public void setAliasIdentities(PublicIdentityElement aliasIdentities) {
		this.aliasIdentities = aliasIdentities;
	}

	public PublicIdentityElement getAllIdentities() {
		return allIdentities;
	}

	public void setAllIdentities(PublicIdentityElement allIdentities) {
		this.allIdentities = allIdentities;
	}

	public PublicIdentityElement getImplicitIdentities() {
		return implicitIdentities;
	}

	public void setImplicitIdentities(PublicIdentityElement implicitIdentities) {
		this.implicitIdentities = implicitIdentities;
	}

	public PublicIdentityElement getRegisteredIdentities() {
		return registeredIdentities;
	}

	public void setRegisteredIdentities(PublicIdentityElement registeredIdentities) {
		this.registeredIdentities = registeredIdentities;
	}

	
	
}
