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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.CapabilitiesSet;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class CapabilitiesSet_DAO {
	private static Logger logger = Logger.getLogger(CapabilitiesSet_DAO.class);
	
	public static void insert(Session session, CapabilitiesSet cap_s){
		session.save(cap_s);
	}
	
	public static void update(Session session, CapabilitiesSet cap_s){
		session.saveOrUpdate(cap_s);
	}
	
	public static void update_all_from_set(Session session, int id_set, String name){
		Query query = session.createSQLQuery("update capabilities_set set name=? where capabilities_set.id_set=?");
		query.setString(0, name);
		query.setInteger(1, id_set);
		query.executeUpdate();
	}

	public static List get_all_from_set(Session session, int id_set){
		Query query = session.createSQLQuery("select * from capabilities_set cs where cs.id_set=?")
			.addEntity(CapabilitiesSet.class);
		query.setInteger(0, id_set);
		return query.list();
	}
	
	public static List get_all_from_set(Session session, int id_set, int is_mandatory){
		Query query = session.createSQLQuery("select * from capabilities_set cs where cs.id_set=? and is_mandatory=?")
			.addEntity(CapabilitiesSet.class);
		query.setInteger(0, id_set);
		query.setInteger(1, is_mandatory);
		return query.list();
	}
	

	public static List get_all_sets(Session session){
		Query query = session.createSQLQuery("select distinct id_set, name from capabilities_set order by(id_set)")
			.addScalar("id_set", Hibernate.INTEGER)
			.addScalar("name", Hibernate.STRING);

		List result = new ArrayList();
		CapabilitiesSet cap_set = null;
	
		if (query.list() != null && query.list().size() > 0){
			Iterator it = query.list().iterator();
			
			while (it.hasNext()){
				Object[] row = (Object[]) it.next();
				cap_set = new CapabilitiesSet();
				cap_set.setId_set((Integer)row[0]);
				cap_set.setName((String)row[1]);
				result.add(cap_set);
			}	
		}
		return result;
	}

	public static Object[] get_all(Session session, int firstResult, int maxResults){
		Query query = session.createSQLQuery("select distinct id_set, name from capabilities_set order by(id_set) ")
			.addScalar("id_set", Hibernate.INTEGER)
			.addScalar("name", Hibernate.STRING);

		int size = get_cnt(session);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		
		Object[] result = new Object[2];
		result[0] = new Integer(size);
		
		List list_result = new ArrayList();
		CapabilitiesSet cap_set = null;
	
		if (query.list() != null && query.list().size() > 0){
			Iterator it = query.list().iterator();
			
			while (it.hasNext()){
				Object[] row = (Object[]) it.next();
				cap_set = new CapabilitiesSet();
				cap_set.setId_set((Integer)row[0]);
				cap_set.setName((String)row[1]);
				list_result.add(cap_set);
			}	
		}
		result[1] = list_result;
		return result;
	}
	
	public static Object[] get_by_Wildcarded_Name(Session session, String name, 
			int firstResult, int maxResults){
		
		Query query;
		query = session.createSQLQuery("select distinct id_set, name from capabilities_set where name like ? order by(id_set)")
			.addScalar("id_set", Hibernate.INTEGER)
			.addScalar("name", Hibernate.STRING);

		query.setString(0, "%" + name + "%");
		int size = get_cnt(session);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		
		Object[] result = new Object[2];
		result[0] = new Integer(size);
		
		List list_result = new ArrayList();
		CapabilitiesSet cap_set = null;
	
		if (query.list() != null && query.list().size() > 0){
			Iterator it = query.list().iterator();
			
			while (it.hasNext()){
				Object[] row = (Object[]) it.next();
				cap_set = new CapabilitiesSet();
				cap_set.setId_set((Integer)row[0]);
				cap_set.setName((String)row[1]);
				list_result.add(cap_set);
			}	
		}
		result[1] = list_result;
		return result;
	}
	
	public static int get_max_id_set(Session session){
		Query query = session.createSQLQuery("select max(id_set) from capabilities_set");
		Integer result = null;
		result = (Integer) query.uniqueResult();
				
		if (result == null)
			return 0;
		else
			return result.intValue();
	}
	
	public static CapabilitiesSet get_by_set_ID(Session session, int id_set){
		Query query = session.createSQLQuery("select distinct id_set, name from capabilities_set where id_set=?")
			.addScalar("id_set", Hibernate.INTEGER)
			.addScalar("name", Hibernate.STRING);
		query.setInteger(0, id_set);
		
		CapabilitiesSet result = null;
		if (query.list() != null && query.list().size() > 0){
			Iterator it = query.list().iterator();
			Object[] row = (Object[]) it.next();
			
			result = new CapabilitiesSet();
			result.setId_set((Integer)row[0]);
			result.setName((String) row[1]);
		}
		return result;
	}
	
	public static int delete_set_by_ID(Session session, int ID){
		Query query = session.createSQLQuery("delete from capabilities_set where id_set=?");
		query.setInteger(0, ID);
		return query.executeUpdate();
	}
	
	public static int delete_capability_from_set(Session session, int id_set, int id_capability){
		Query query = session.createSQLQuery("delete from capabilities_set where id_set=? and id_capability=?");
		query.setInteger(0, id_set);
		query.setInteger(1, id_capability);
		return query.executeUpdate();
	}

	public static int get_cap_cnt(Session session, int id_cap){
		Query query;
		query = session.createSQLQuery("select count(*) from capabilities_set where id_capability=?");
		query.setInteger(0, id_cap);
		BigInteger result = (BigInteger) query.uniqueResult();
		return result.intValue();
	}

	public static int get_cnt_for_set(Session session, int id_set){
		Query query;
		query = session.createSQLQuery("select count(*) from capabilities_set where id_set=?");
		query.setInteger(0, id_set);
		BigInteger result = (BigInteger) query.uniqueResult();
		return result.intValue();
	}

	public static int get_cnt(Session session){
		Query query;
		query = session.createSQLQuery("select count(distinct id_set) from capabilities_set");
		BigInteger result = (BigInteger) query.uniqueResult();
		return result.intValue();
	}
	
	public static boolean test_unused_name(Session session, String name, int id_set){
		Query query = session.createSQLQuery("select * from capabilities_set where name=? and id_set !=?")
			.addEntity(CapabilitiesSet.class);
		query.setString(0, name);
		query.setInteger(1, id_set);
		
		List result = query.list();
		if (result != null && result.size() > 0){
			return false;
		}
		return true;
	}
	
	public static CapabilitiesSet get_by_Capability_and_Set_ID(Session session, int id_cap, int id_set){
		Query query = session.createSQLQuery("select * from capabilities_set where id_capability=? and id_set=?")
			.addEntity(CapabilitiesSet.class);
		query.setInteger(0, id_cap);
		query.setInteger(1, id_set);
	
		return (CapabilitiesSet) query.uniqueResult();
	}
	
}
