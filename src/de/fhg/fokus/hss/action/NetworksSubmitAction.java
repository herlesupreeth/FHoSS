/*
 * $Id$
 *
 * Copyright (C) 2004-2006 FhG Fokus
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
package de.fhg.fokus.hss.action;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.fhg.fokus.hss.form.NetworkForm;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.Network;
import de.fhg.fokus.hss.util.HibernateUtil;

import org.apache.log4j.Logger;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Submit action of a network object. It supports three types of action: delete,
 * rename and create a network entry.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class NetworksSubmitAction extends HssAction
{
	private static final Logger LOGGER = Logger.getLogger(NetworksShowAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		LOGGER.debug("entering");

		ActionMessages actionMessages = null;
		ActionForward forward = null;
		Network network = null;

		NetworkForm networkForm = (NetworkForm) form;
		LOGGER.debug(networkForm);

		try{
			HibernateUtil.beginTransaction();
			if (networkForm.getActionString().equals(NetworkForm.ACTION_CREATE)){
				LOGGER.debug("Action type is: create");

				if (checkExist(networkForm.getNetworkString())){
					actionMessages = new ActionMessages();
					actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage("network.error.duplicated"));
					saveMessages(request, actionMessages);
				} 
				else{
					network = new Network();
					network.setNetworkString(networkForm.getNetworkString());
					HibernateUtil.getCurrentSession().save(network);
				}
			}	 
			else if (networkForm.getActionString().equals(NetworkForm.ACTION_RENAME)){
				LOGGER.debug("Action type is: rename");

				if (checkExist(networkForm.getNetworkString())){
					actionMessages = new ActionMessages();
					actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage("network.error.duplicated"));
					saveMessages(request, actionMessages);
				}
				else{
					network = (Network) HibernateUtil.getCurrentSession().load(Network.class, networkForm.getPrimaryKey());
					network.setNetworkString(networkForm.getNetworkString());
					HibernateUtil.getCurrentSession().update(network);
				}
			}
			else if (networkForm.getActionString().equals(NetworkForm.ACTION_DELETE)){
				
	            if(checkAssignedRoams(networkForm.getId()) == true){
	              	actionMessages = new ActionMessages();
	                actionMessages.add(Globals.MESSAGE_KEY,new ActionMessage("network.error.roam.assigned"));
	                saveMessages(request, actionMessages);	
	            }
	            else{
	            	Integer primaryKey = networkForm.getPrimaryKey();
	            	network = (Network) HibernateUtil.getCurrentSession().load(Network.class, primaryKey);
	            	HibernateUtil.getCurrentSession().delete(network);
				}
			}
			HibernateUtil.commitTransaction();
		}
		finally{
			HibernateUtil.closeSession();
		}
		
		LOGGER.debug("exiting");

		if (actionMessages == null){
			forward = mapping.findForward(FORWARD_SUCCESS);
		} 
		else{
			forward = mapping.findForward(FORWARD_FAILURE);
		}

		return forward;
	}

	
	  private boolean checkAssignedRoams(String net_id){
		  Network roam = null;
		  Impi impi = null;
		  
		  List impiList = HibernateUtil.getCurrentSession().createQuery("from de.fhg.fokus.hss.model.Impi").list();
		  Iterator iterator = impiList.iterator();
		  while(iterator.hasNext()){
			  impi = (Impi) iterator.next();
			  Iterator roamIt;

			  if(impi.getRoams() != null){
				  roamIt = impi.getRoams().iterator();
				  while(roamIt.hasNext()){
					  roam = (Network)roamIt.next();
					  if((roam.getNwId().toString()).equals(net_id)){	
						  return true;
					  }
	        		}
	        	}                             
	        } 
	        return false;
	  }   	
	
	private boolean checkExist(String name){
		return ((Integer) HibernateUtil.getCurrentSession()
				.createQuery("select count(network) from de.fhg.fokus.hss.model.Network as network where network.networkString = ?")
				.setString(0, name).uniqueResult()).intValue() != 0;
	}
}
