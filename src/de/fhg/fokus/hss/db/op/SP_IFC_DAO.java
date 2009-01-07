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
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.SP;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.main.HSSProperties;

/**
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class SP_IFC_DAO {
	private static Logger logger = Logger.getLogger(SP_IFC_DAO.class);

	public static void insert(Session session, SP_IFC sp_ifc){
		if (HSSProperties.iFC_NOTIF_ENABLED){
			IFC ifc = IFC_DAO.get_by_ID(session, sp_ifc.getId_ifc());
			ShNotification_DAO.insert_notif_for_iFC(session, ifc, sp_ifc.getId_sp());
		}
		session.save(sp_ifc);
	}

	public static SP_IFC get_by_SP_and_IFC_ID(Session session, int id_sp, int id_ifc){
		Query query;
		query = session.createSQLQuery("select * from sp_ifc where id_sp=? and id_ifc=?")
			.addEntity(SP_IFC.class);
		query.setInteger(0, id_sp);
		query.setInteger(1, id_ifc);

		SP_IFC result = null;
		try{
			result = (SP_IFC) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}

		return result;
	}

	public static List get_IFC_by_SP_ID(Session session, int id_sp){
		Query query;
		query = session.createSQLQuery(
				"select * from ifc " +
				"	inner join sp_ifc on ifc.id=sp_ifc.id_ifc" +
				"	inner join sp on sp_ifc.id_sp=sp.id" +
				"		where sp.id=?")
				.addEntity(IFC.class);
		query.setInteger(0, id_sp);
		return query.list();
	}

	public static List get_all_SP_by_IFC_ID(Session session, int id_ifc){
		Query query;
		query = session.createSQLQuery(
				"select * from sp " +
				"	inner join sp_ifc on sp.id=sp_ifc.id_sp" +
				"		where sp_ifc.id_ifc=? order by (priority)")
				.addEntity(SP.class);
		query.setInteger(0, id_ifc);
		return query.list();
	}

	public static SP_IFC get_by_SP_ID_and_Priority(Session session, int id_sp, int priority){
		Query query;
		query = session.createSQLQuery(
				"select * from sp_ifc " +
				"		where sp_ifc.id_sp = ? and sp_ifc.priority = ?")
				.addEntity(SP_IFC.class);

		query.setInteger(0, id_sp);
		query.setInteger(1, priority);

		SP_IFC result = null;
		try{
			result = (SP_IFC) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}
		return result;
	}


	public static int get_Unreg_Serv_Count(Session session, int id_sp){

		Query query;
		query = session.createSQLQuery(
				"select count(*) from sp_ifc" +
				"	inner join sp on sp.id=sp_ifc.id_sp" +
				"	inner join ifc on ifc.id=sp_ifc.id_ifc" +
				"		where sp.id=? and (ifc.profile_part_ind=? or ifc.profile_part_ind=?)");
		query.setInteger(0, id_sp);
		query.setInteger(1, CxConstants.Profile_Part_Indicator_UnRegistered);
		query.setInteger(2, CxConstants.Profile_Part_Indicator_Any);

		BigInteger result = (BigInteger)query.uniqueResult();
		if (result == null)
			return 0;
		return result.intValue();
	}

	public static List get_all_IFC_by_SP_ID(Session session, int id_sp){
		Query query;
		query = session.createSQLQuery(
					"select * from ifc " +
					"	inner join sp_ifc on ifc.id = sp_ifc.id_ifc" +
					"		where sp_ifc.id_sp=? order by (sp_ifc.priority)")
						.addEntity(IFC.class);
		query.setInteger(0, id_sp);
		return query.list();
	}

	public static List get_all_SP_IFC_by_SP_ID(Session session, int id_sp){
		Query query;
		query = session.createSQLQuery(
					"select * from sp_ifc " +
					"	inner join ifc on ifc.id = sp_ifc.id_ifc" +
					"		where sp_ifc.id_sp=?")
						.addEntity(SP_IFC.class)
						.addEntity(IFC.class);
		query.setInteger(0, id_sp);
		return query.list();
	}

	public static int delete_by_SP_and_IFC_ID(Session session, int id_sp, int id_ifc){
		if (HSSProperties.iFC_NOTIF_ENABLED){
			IFC ifc = IFC_DAO.get_by_ID(session, id_ifc);
			ShNotification_DAO.insert_notif_for_iFC(session, ifc, id_sp);
		}

		Query query;
		query = session.createSQLQuery("delete from sp_ifc where id_sp=? and id_ifc=?");
		query.setInteger(0, id_sp);
		query.setInteger(1, id_ifc);
		return query.executeUpdate();
	}

}
