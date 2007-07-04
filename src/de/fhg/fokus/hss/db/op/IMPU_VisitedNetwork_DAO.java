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

package de.fhg.fokus.hss.db.op;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMPU_VisitedNetwork;
import de.fhg.fokus.hss.db.model.VisitedNetwork;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class IMPU_VisitedNetwork_DAO {
	private static Logger logger = Logger.getLogger(IMPU_VisitedNetwork_DAO.class);
	
	public static void insert(Session session, IMPU_VisitedNetwork impu_vn){
		session.save(impu_vn);
	}
	
	public static void update(Session session, IMPU_VisitedNetwork impu_vn){
		session.saveOrUpdate(impu_vn);
	}	
	
	public static IMPU_VisitedNetwork get_by_IMPU_and_VisitedNetwork_ID(Session session, int id_impu, int id_visited_network){
		Query query;
		query = session.createSQLQuery("select * from impu_visited_network where id_impu=? and id_visited_network=?")
			.addEntity(IMPU_VisitedNetwork.class);
		query.setInteger(0, id_impu);
		query.setInteger(1, id_visited_network);

		IMPU_VisitedNetwork result = null;
		try{
			result = (IMPU_VisitedNetwork) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static List get_all_VN_by_IMPU_ID(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery(
				"select * from visited_network" +
				"	inner join impu_visited_network on visited_network.id=impu_visited_network.id_visited_network" +
				"		where impu_visited_network.id_impu=?")
					.addEntity(VisitedNetwork.class);
		query.setInteger(0, id_impu);
		return query.list();
	}
	
	public static List getJoinResult(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery(
				"select * from impu_visited_network" +
				"	inner join impu on impu_visited_network.id_impu=impu.id" +
				"	inner join visited_network on impu_visited_network.id_visited_networ=visited_network.id" +
				"		where impu_visited_network.id_impu=?")
					.addEntity(IMPU_VisitedNetwork.class)
					.addEntity(IMPU.class)
					.addEntity(VisitedNetwork.class);
					
		query.setInteger(0, id_impu);
		return query.list();
	}
		
}
