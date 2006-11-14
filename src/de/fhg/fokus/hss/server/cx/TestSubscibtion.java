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
package de.fhg.fokus.hss.server.cx;

import java.io.OutputStreamWriter;
import java.io.Writer;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import de.fhg.fokus.cx.datatypes.IMSSubscription;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.datatypes.ServiceProfile;

/**
 * This is a test class
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class TestSubscibtion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IMSSubscription subscription = new IMSSubscription();
		
		subscription.setPrivateID("My_PrivateID");
		
		ServiceProfile serviceProfile = new ServiceProfile();
		
		PublicIdentity publicIdentity = new PublicIdentity();
		publicIdentity.setIdentity("My_PublicID@home.org");
		serviceProfile.addPublicIdentity(publicIdentity);
//		
//		InitialFilterCriteria initialFilterCriteria = new InitialFilterCriteria();
//		initialFilterCriteria.setPriority(0);
//		ApplicationServer applicationServer = new ApplicationServer();
//		applicationServer.setServerName("as.home.org");
//		
//		initialFilterCriteria.setApplicationServer(applicationServer);
//		
//		TriggerPoint triggerPoint = new TriggerPoint();
//		
//		SPT spt = new SPT();
//		spt.addGroup(1);
//		
//		TSePoTriChoice choice = new TSePoTriChoice();
//		choice.setMethod("INVITE");
//		spt.setTSePoTriChoice(choice);
//		choice = new TSePoTriChoice();
//		choice.setRequestURI("My_URI@home.org");
//		//spt.setTSePoTriChoice(choice);
//		
//		triggerPoint.setConditionTypeCNF(true);
//		triggerPoint.addSPT(spt);
//		
//		initialFilterCriteria.setTriggerPoint(triggerPoint);
//		
//		
//		serviceProfile.addInitialFilterCriteria(initialFilterCriteria);
//		serviceProfile.addInitialFilterCriteria(initialFilterCriteria);
		subscription.addServiceProfile(serviceProfile);
		
		Writer pw = new OutputStreamWriter(System.out);
		
		try {
			subscription.marshal(pw);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

}
