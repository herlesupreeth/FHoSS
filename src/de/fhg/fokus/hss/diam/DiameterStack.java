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

package de.fhg.fokus.hss.diam;

import org.apache.log4j.Logger;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.EventListener;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.diameter.DiameterPeer.transaction.TransactionListener;
import de.fhg.fokus.hss.main.HSSContainer;
import de.fhg.fokus.hss.main.Task;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class DiameterStack implements EventListener, TransactionListener {
	private static Logger logger = Logger.getLogger(DiameterStack.class);
	
	public DiameterPeer diameterPeer = null;
	public HSSContainer hssContainer = null;
	
	public DiameterStack(HSSContainer hssContainer){
		this.hssContainer = hssContainer;
		
		diameterPeer = new DiameterPeer();
        diameterPeer.configure("DiameterPeerHSS.xml", true);
		diameterPeer.enableTransactions(10, 1);
		diameterPeer.addEventListener(this);
		
	}
	private int sessionID = 0; 

	/** Gets the next value for the Session ID Counter. It is used in the second part of the Session-Id 
	 * field together with the time stamp */
	public int getNextSessionID(){
		synchronized (this){
			return sessionID++;
		}
	}
	
	public void recvMessage(String FQDN, DiameterMessage request) {
		
		
		if (request.commandCode == DiameterConstants.Command.PPR || request.commandCode == DiameterConstants.Command.RTR){
			// these commands are received also by the receiveAnswer method
			return;
		}
		
		Task task = new Task(hssContainer.diamStack, 2, FQDN, request.commandCode, request.applicationID, request);
		try{
			hssContainer.tasksQueue.put(task);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void receiveAnswer(String FQDN, DiameterMessage request, DiameterMessage response) {
		Task task = new Task(hssContainer.diamStack, 2, FQDN, request.commandCode, request.applicationID, response);
		try{
			hssContainer.tasksQueue.put(task);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void timeout(DiameterMessage request) {
		Task task = new Task(hssContainer.diamStack, 3, request.commandCode, request.applicationID);
		task.message = request;
		try{
			hssContainer.tasksQueue.put(task);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
}

