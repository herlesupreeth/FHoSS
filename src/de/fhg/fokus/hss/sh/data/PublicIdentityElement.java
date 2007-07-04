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
public class PublicIdentityElement {
	private Vector<String> publicIdentityList = null;
	private Vector<String> msisdnList = null;

	// Extension
	private int identityType = -1;
	private String wildcardedPSI = null;
	
	public PublicIdentityElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.PublicIdentifiers_s);
		if (publicIdentityList != null && publicIdentityList.size() > 0){
			for (int i = 0; i < publicIdentityList.size(); i++){
				sBuffer.append(ShDataTags.IMSPublicIdentity_s);
				sBuffer.append(publicIdentityList.get(i));
				sBuffer.append(ShDataTags.IMSPublicIdentity_e);
			}
		}
		
		if (msisdnList != null && msisdnList.size() > 0){
			for (int i = 0; i < msisdnList.size(); i++){
				sBuffer.append(ShDataTags.MSISDN_s);
				sBuffer.append(msisdnList.get(i));
				sBuffer.append(ShDataTags.MSISDN_e);
			}
		}
		
		if (identityType != -1 || wildcardedPSI != null){
			sBuffer.append(ShDataTags.Extension_s);
			if (identityType != -1){
				sBuffer.append(ShDataTags.IdentityType_s);
				sBuffer.append(identityType);
				sBuffer.append(ShDataTags.IdentityType_e);
			}

			if (wildcardedPSI != null){
				sBuffer.append(ShDataTags.WildcardedPSI_s);
				sBuffer.append(wildcardedPSI);
				sBuffer.append(ShDataTags.WildcardedPSI_e);
			}
			sBuffer.append(ShDataTags.Extension_e);
		}
		
		sBuffer.append(ShDataTags.PublicIdentifiers_e);
		return sBuffer.toString();
	}

	public void addPublicIdentity(String publicIdentity){
		if (publicIdentityList == null){
			publicIdentityList = new Vector<String>();
		}
		publicIdentityList.add(publicIdentity);
	}

	public void addMSISDN(String msisdn){
		if (msisdnList == null){
			msisdnList = new Vector<String>();
		}
		msisdnList.add(msisdn);
	}
	
	public int getIdentityType() {
		return identityType;
	}

	public void setIdentityType(int identityType) {
		this.identityType = identityType;
	}

	public Vector<String> getMsisdnList() {
		return msisdnList;
	}

	public void setMsisdnList(Vector<String> msisdnList) {
		this.msisdnList = msisdnList;
	}

	public Vector<String> getPublicIdentityList() {
		return publicIdentityList;
	}

	public void setPublicIdentityList(Vector<String> publicIdentityList) {
		this.publicIdentityList = publicIdentityList;
	}

	public String getWildcardedPSI() {
		return wildcardedPSI;
	}

	public void setWildcardedPSI(String wildcardedPSI) {
		this.wildcardedPSI = wildcardedPSI;
	}
	
}
