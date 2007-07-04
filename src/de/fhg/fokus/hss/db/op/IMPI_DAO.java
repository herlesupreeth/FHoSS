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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class IMPI_DAO {
	private static Logger logger = Logger.getLogger(IMPI_DAO.class);
	
	public static void insert(Session session, IMPI impi){
		session.save(impi);
	}
	
	public static void update(Session session, IMPI impi){
		session.saveOrUpdate(impi);
	}	

	public static IMPI update(Session session, int id, String sqn){
		
		IMPI impi = (IMPI) session.load(IMPI.class, id);
		impi.setSqn(sqn);
		session.saveOrUpdate(impi);
		return impi;
	}
	
	
	public static IMPI get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from impi where id=?")
			.addEntity(IMPI.class);
		query.setInteger(0, id);

		return (IMPI) query.uniqueResult();
	}
	
	public static List get_all_IMPI_for_IMPU_ID(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery("select * from impi" +
				"	inner join impi_impu on impi.id=impi_impu.id_impi" +
				" where impi_impu.id_impu=? order by (impi.id)")
				.addEntity("impi", IMPI.class);
		query.setInteger(0, id_impu);
		return query.list();
	}
	
	public static IMPI get_by_Identity(Session session, String identity){
		Query query;
		query = session.createSQLQuery("select * from impi where identity like ?")
			.addEntity(IMPI.class);
		query.setString(0, identity);
		IMPI result = null;
		
		try{
			result = (IMPI) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object[] get_by_Wildcarded_Identity(Session session, String identity, 
			int firstResult, int maxResults){
		
		Query query;
		query = session.createSQLQuery("select * from impi where identity like ?")
			.addEntity(IMPI.class);
		query.setString(0, "%" + identity + "%");

		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;
	}
	
	public static Object[] get_all(Session session, int firstResult, int maxResults){
		Query query;
		query = session.createSQLQuery("select * from impi")
			.addEntity(IMPI.class);
		
		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;
	}
	
	public static List get_all_by_IMSU_ID(Session session, int id_imsu){
		Query query;
		query = session.createSQLQuery("select * from impi where id_imsu=? order by id")
			.addEntity(IMPI.class);
		query.setInteger(0, id_imsu);
		
		return query.list();
	}
	
	public static List get_all_registered_implicit_sets(Session session, int id_impi){
		Query query;
		query = session.createSQLQuery("select distinct id_implicit_set from impu" +
				"	inner join impi_impu on impu.id=impi_impu.id_impu" +
				" where impi_impu.id_impi=? and (impi_impu.user_state=1 or impi_impu.user_state=2)")
				.addScalar("id_implicit_set", Hibernate.INTEGER);
		query.setInteger(0, id_impi);
		return query.list();
	}	
	
	public static List get_all_Registered_IMPIs_by_IMSU_ID(Session session, int id_imsu){
		Query query;
		query = session.createSQLQuery("select distinct impi.id, impi.identity from impi" +
				"	inner join impi_impu on impi.id=impi_impu.id_impi" +
				" where impi.id_imsu=? and (impi_impu.user_state=1 or impi_impu.user_state=2)")
				.addScalar("id", Hibernate.INTEGER)
				.addScalar("identity", Hibernate.STRING);
		query.setInteger(0, id_imsu);

		List list_result = new ArrayList();
		IMPI impi = null;
		if (query.list() != null && query.list().size() > 0){
			Iterator it = query.list().iterator();
			while (it.hasNext()){
				Object[] row = (Object[]) it.next();
				impi = new IMPI();
				impi.setId((Integer)row[0]);
				impi.setIdentity((String)row[1]);
				list_result.add(impi);
			}	
		}
		return list_result;
	}	

	public static IMPI get_an_IMPI_for_IMPU(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery("select * from impi" +
				"	inner join impi_impu on impi.id=impi_impu.id_impi" +
				" where impi_impu.id_impu=? limit 1")
				.addEntity(IMPI.class);
		query.setInteger(0, id_impu);
		return (IMPI) query.uniqueResult();
	}
	
	
	public static int delete_by_ID(Session session, int id){
		Query query = session.createSQLQuery("delete from impi where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}
}
