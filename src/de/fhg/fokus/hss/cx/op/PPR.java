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
package de.fhg.fokus.hss.cx.op;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.ChargingInfo;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.op.ChargingInfo_DAO;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.DiameterStack;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class PPR {
	private static Logger logger = Logger.getLogger(PPR.class);
	
	public static void sendRequest(DiameterStack diameterStack, int id_impi, int id_implicit_set, int type, int grp){
		DiameterPeer diameterPeer = diameterStack.diameterPeer; 
		DiameterMessage request = diameterPeer.newRequest(DiameterConstants.Command.PPR, DiameterConstants.Application.Cx);
		request.flagProxiable = true;
		
		// add SessionId
		UtilAVP.addSessionID(request, diameterPeer.FQDN, diameterStack.getNextSessionID());
		
		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(request, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(request, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Cx);
		
		boolean dbException = false;
		try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	
        	// get an impu from the implicit set; this is used to obtain the charging-info functions
        	IMPU impu = IMPU_DAO.get_one_from_set(session, id_implicit_set);
        	if (impu.getUser_state() == CxConstants.IMPU_user_state_Not_Registered || impu.getUser_state() == CxConstants.IMPU_user_state_Auth_Pending){
        		logger.error("Implicit Registration Set: " + id_implicit_set + " is in Not-Registered state!");
        		logger.error("PPR aborted!");
   	        	CxEvents_DAO.delete(session, grp);
        	}
        	else{
        		IMPI impi = IMPI_DAO.get_by_ID(session, id_impi);
        		IMSU imsu = IMSU_DAO.get_by_ID(session, impi.getId_imsu());
     
        		String destHost = imsu.getDiameter_name(); 
        		UtilAVP.addDestinationHost(request, destHost);
        		UtilAVP.addDestinationRealm(request, destHost.substring(destHost.indexOf('.') + 1));
        		
        		String userData = SAR.downloadUserData(impi.getIdentity(), id_implicit_set);
        		ChargingInfo chgInfo = ChargingInfo_DAO.get_by_ID(session, impu.getId_charging_info());
			
        		UtilAVP.addUserName(request, impi.getIdentity());
        		if (type == WebConstants.PPR_Apply_for_User_Data){
        			// add only User Data
        			UtilAVP.addUserData(request, userData);
        		}
        		else if (type == WebConstants.PPR_Apply_for_Charging_Func){
        			if (chgInfo != null){
        				// 	add only Charging Info
        				UtilAVP.addChargingInformation(request, chgInfo);
        			}
        		}
        		else{
        			// 	add both
        			UtilAVP.addUserData(request, userData);
        			if (chgInfo != null){
        				UtilAVP.addChargingInformation(request, chgInfo);
        			}
        		}
        		
        		// add hopbyhop and endtoend ID into the rtr_ppr table
        		CxEvents_DAO.update_by_grp(session, grp, request.hopByHopID, request.endToEndID);
        		
        		if (chgInfo != null || type != WebConstants.PPR_Apply_for_Charging_Func){
            		// send the request
            		if (!diameterPeer.sendRequestTransactional(imsu.getDiameter_name(), request, diameterStack)){
            			// if the host is not connected or the request cannot be sent from other reasons, delete the Cx Event from database!
           	        	CxEvents_DAO.delete(session, request.hopByHopID, request.endToEndID);
            		}
        		}
        	}
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}		
	}
	
	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage response){
		String vendorSpecificAppID = UtilAVP.getVendorSpecificApplicationID(response);
		String authSessionState = UtilAVP.getAuthSessionState(response);
		String originHost = UtilAVP.getOriginatingHost(response);
		String originRealm = UtilAVP.getOriginatingRealm(response);
			
		if (vendorSpecificAppID == null || authSessionState == null || originHost == null || originRealm == null){
			logger.warn("Required Parameters are null in PPA!");
		}
		
		// get the response code...
		// to be implemented
		
		// delete the coresponding row from CxEvents table
		deleteCxEvent(response.hopByHopID, response.endToEndID);
	}
	
	public static void processTimeout(DiameterMessage request){
		// delete the coresponding row from CxEvents table
		deleteCxEvent(request.hopByHopID, request.endToEndID);
	}

	private static void deleteCxEvent(long hopByHopID, long endToEndID){
		boolean dbException = false;
		try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	CxEvents_DAO.delete(session, hopByHopID, endToEndID);
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}		        	
	}
}
