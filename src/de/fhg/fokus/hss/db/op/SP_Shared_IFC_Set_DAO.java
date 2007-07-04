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

import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.SP_Shared_IFC_Set;
import de.fhg.fokus.hss.db.model.Shared_IFC_Set;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class SP_Shared_IFC_Set_DAO {
	private static Logger logger = Logger.getLogger(SP_Shared_IFC_Set_DAO.class);
	
	public static void insert(Session session, SP_Shared_IFC_Set sp_shared_ifc){
		session.save(sp_shared_ifc);
	}
	
	public static void update(Session session, SP_Shared_IFC_Set sp_shared_ifc){
		session.saveOrUpdate(sp_shared_ifc);
	}	
	
	public static SP_Shared_IFC_Set get_by_SP_and_Shared_IFC_Set_ID(Session session, int id_sp, int id_shared_ifc_set){
		Query query;
		query = session.createSQLQuery("select * from sp_shared_ifc_set where id_sp=? and id_shared_ifc_set=?")
			.addEntity(SP_Shared_IFC_Set.class);
		query.setInteger(0, id_sp);
		query.setInteger(1, id_shared_ifc_set);
		
		SP_Shared_IFC_Set result = null;
		result = (SP_Shared_IFC_Set) query.uniqueResult();

		return result;
	}
	
	public static int get_SP_cnt_by_Shared_IFC_Set_ID(Session session, int set_id){
		Query query;
		query = session.createSQLQuery("select count(*) from sp_shared_ifc_set where sp_shared_ifc_set.id_shared_ifc_set=?");
		query.setInteger(0, set_id);
		BigInteger result = (BigInteger) query.uniqueResult();
		return result.intValue();
	}	
	
/*	public static List getJoinResult(Session session, int id_sp){
		Query query;
		query = session.createQuery(
				"from SP_Shared_IFC_Set as sp_shared_ifc_set" +
				"	inner join sp_shared_ifc_set.sp" +
				"	inner join sp_shared_ifc_set.shared_ifc_set" +
				"		where sp_shared_ifc_set.sp.id=?");
		query.setInteger(0, id_sp);
		return query.list();
	}
	*/
	
	public static List get_all_shared_IFC_set_IDs_by_SP_ID(Session session, int id_sp){
		Query query;
		query = session.createSQLQuery(
				"select * from shared_ifc_set" +
				"	inner join sp_shared_ifc_set on sp_shared_ifc_set.id_shared_ifc_set=shared_ifc_set.id" +
				"		where sp_shared_ifc_set.id_sp=?")
				.addScalar("id_set");
		query.setInteger(0, id_sp);
		return query.list();
	}
	
	public static List get_all_Shared_IFC_by_SP_ID(Session session, int id_sp){
		Query query;
		query = session.createSQLQuery("select distinct id_set, name from shared_ifc_set" +
				"	inner join sp_shared_ifc_set on shared_ifc_set.id_set=sp_shared_ifc_set.id_shared_ifc_set" +
				" where sp_shared_ifc_set.id_sp=?")
				.addScalar("id_set", Hibernate.INTEGER)
				.addScalar("name", Hibernate.STRING);
		query.setInteger(0, id_sp);
		
		List result = new ArrayList();
		if (query.list() != null){
			Iterator it = query.list().iterator();
			Object [] row = null;
			while (it.hasNext()){
				row = (Object[]) it.next();
				Shared_IFC_Set shared_IFC = null;
				if (row != null){
					shared_IFC = new Shared_IFC_Set();
					shared_IFC.setId_set((Integer)row[0]);
					shared_IFC.setName((String)row[1]);
				}
				result.add(shared_IFC);
			}
		} 

		return result;
	}
	
	public static int delete_by_SP_and_Shared_IFC_ID(Session session, int id_sp, int id_shared_ifc){
		Query query;
		query = session.createSQLQuery("delete from sp_shared_ifc_set where id_sp=? and id_shared_ifc_set=?");
		query.setInteger(0, id_sp);
		query.setInteger(1, id_shared_ifc);
		return query.executeUpdate();
	}
	
}
