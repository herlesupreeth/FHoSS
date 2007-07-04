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
public class TriggerPointElement {
	private int conditionTypeCNF = 1;
	private Vector<SPTElement> sptList = null;
	
	public TriggerPointElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.TriggerPoint_s);

		sBuffer.append(ShDataTags.ConditionTypeCNF_s);
		sBuffer.append(conditionTypeCNF);
		sBuffer.append(ShDataTags.ConditionTypeCNF_e);
		
		if (sptList != null && sptList.size() > 0){
			Iterator<SPTElement> it = sptList.iterator();
			while (it.hasNext()){
				SPTElement spt = it.next();
				sBuffer.append(spt.toString());
			}
		}
		sBuffer.append(ShDataTags.TriggerPoint_e);		
		return sBuffer.toString();
	}
	
	public void addSPT(SPTElement spt){
		if (sptList == null){
			sptList = new Vector<SPTElement>();
		}
		sptList.add(spt);
		
	}
	public int getConditionTypeCNF() {
		return conditionTypeCNF;
	}

	public void setConditionTypeCNF(int conditionTypeCNF) {
		this.conditionTypeCNF = conditionTypeCNF;
	}

	public Vector<SPTElement> getSptList() {
		return sptList;
	}

	public void setSptList(Vector<SPTElement> sptList) {
		this.sptList = sptList;
	}
}
