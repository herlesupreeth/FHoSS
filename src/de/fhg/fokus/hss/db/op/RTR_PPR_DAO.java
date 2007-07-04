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

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.RTR_PPR;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class RTR_PPR_DAO {
	
	public static void insert(Session session, RTR_PPR rtr_ppr){
		session.save(rtr_ppr);
	}
	
	public static void update(Session session, RTR_PPR rtr_ppr){
		session.saveOrUpdate(rtr_ppr);
	}
	
	public static void mark_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("update rtr_ppr set hopbyhop=1 where grp=?");
		query.setInteger(0, grp);
		query.executeUpdate();
	}
	
	public static List get_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("select * from rtr_ppr where grp=?")
				.addEntity(RTR_PPR.class);
		query.setInteger(0, grp);
		return query.list();
	}
	
	public static void update_by_grp(Session session, int grp, long hopByHopID, long endToEndID){
		Query query;
		query = session.createSQLQuery("update rtr_ppr set hopbyhop=?, endtoend=? where grp=?")
				.setLong(0, hopByHopID)
				.setLong(1, endToEndID)
				.setInteger(2, grp);
		query.executeUpdate();
	}
	
	public static void delete(Session session, long hopbyhop, long endtoend){
		Query query = session.createSQLQuery("delete from rtr_ppr where hopbyhop=? and endtoend=?");
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		query.executeUpdate();
	}

	public static RTR_PPR get_next_available(Session session){
		Query query;
		query = session.createSQLQuery("select * from rtr_ppr where hopbyhop=0 limit 1")
				.addEntity(RTR_PPR.class);
		return (RTR_PPR) query.uniqueResult();
	}

	public static List get_all_IMPI_IDs_by_HopByHop_and_EndToEnd_ID(Session session, long hopbyhop, long endtoend){
		Query query;
		query = session.createSQLQuery("select distinct id_impi from rtr_ppr where hopbyhop=? and endtoend=?")
				.addScalar("id_impi", Hibernate.INTEGER);
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		return query.list();
	}
	
	
	public static int get_max_grp(Session session){
		Query query = session.createSQLQuery("select max(grp) from rtr_ppr");
		Integer result = (Integer) query.uniqueResult();
		if (result == null)
			return 0;
		return result.intValue();
	}
	
	public static RTR_PPR get_one_from_grp (Session session, long hopbyhop, long endtoend){
		Query query;
		query = session.createSQLQuery("select * from rtr_ppr where hopbyhop=? and endtoend=? limit 1")
			.addEntity(RTR_PPR.class);
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		return (RTR_PPR) query.uniqueResult();
	}
}
