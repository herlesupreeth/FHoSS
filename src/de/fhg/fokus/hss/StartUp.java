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
package de.fhg.fokus.hss;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.hss.model.Imsu;
import de.fhg.fokus.hss.util.HibernateUtil;


/**
 * This is a test class for hibernate operations.
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class StartUp {
  private static final Logger LOGGER = Logger.getLogger(StartUp.class);

  public static void main(String[] args) {
  	
  	Session session = HibernateUtil.currentSession();
  	//createDataSets(session);
  	
  	checkForUser(session);
  	
  	HibernateUtil.closeSession();
  }

   /**
    * This method checks for User
    * @param session The hibernate Session
    **/

	private static void checkForUser(Session session) {
		long startTime = System.currentTimeMillis();
  	
  	

  	
//  	// get user bean
//  	Iterator it = session.createCriteria(Imsu.class).list().iterator();
//  	
//  	
//  	Imsu user = null;
//  	while(it.hasNext()){
//  		user = (Imsu) it.next();
//  		LOGGER.info(user);
//  		if(user.getImpis() != null){
//  			
//  			Iterator it2 = user.getImpis().iterator();
//  			while(it2.hasNext()){
//  				Impi privateId = (Impi)it2.next();
//  				LOGGER.info(privateId);
//  			}
//  		}
//  		//LOGGER.info(ix++);
//  	}
		Transaction tx = session.beginTransaction();
		
  	Imsu myImsu = (Imsu)session.load(Imsu.class, new Integer(1001));
  	LOGGER.info(myImsu);
  	myImsu.setName("MAXIMUS");
  	LOGGER.info(myImsu);
  	session.saveOrUpdate(myImsu);
  	session.flush();
  	
  	tx.commit();
  	HibernateUtil.closeSession();
  	
  	LOGGER.info("test ends ater " + (System.currentTimeMillis() - startTime) + " ms");
	}

   /**
    * This method creates data sets
    * @param session the hibernate session
    **/

  private static void createDataSets(Session session){
//  	Transaction tx = session.beginTransaction();
//  	for (int ix = 1000; ix < 4000; ix++){
//  		Imsu user = new Imsu();
//  		user.setImsuId(new Integer(ix));
//  		user.setFirstname("MAX" + ix);
//  		user.setLastname("USERMANN");
//  		session.save(user);
//  		Impi privateId = new Impi(new Integer(ix), "BLA", "", "22", null);
//  		session.save(privateId);
//  	}
//  	tx.commit();
  }
  
}
