/*
  *  Copyright (C) 2004-2007 FhG Fokus
  *  Parts by Instrumentacion y Componentes S.A. (Inycom). Contact at: ims at inycom dot es
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

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.CxEvents;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.ShNotification;
import de.fhg.fokus.hss.db.model.ShSubscription;
import de.fhg.fokus.hss.sh.ShConstants;
import de.fhg.fokus.hss.web.util.WebConstants;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the propagation of users Service Profile in Cx Interface due to changes in DSAI,
 * according to Release 7.
 *
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */
public class CxEvents_DAO {

	public static void insert(Session session, CxEvents cx_events){
		session.save(cx_events);
	}

	public static void update(Session session, CxEvents cx_events){
		session.saveOrUpdate(cx_events);
	}

	public static void mark_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("update cx_events set hopbyhop=1 where grp=?");
		query.setInteger(0, grp);
		query.executeUpdate();
	}

	public static List get_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("select * from cx_events where grp=?")
				.addEntity(CxEvents.class);
		query.setInteger(0, grp);
		return query.list();
	}

	public static void update_by_grp(Session session, int grp, long hopByHopID, long endToEndID){
		Query query;
		query = session.createSQLQuery("update cx_events set hopbyhop=?, endtoend=? where grp=?")
				.setLong(0, hopByHopID)
				.setLong(1, endToEndID)
				.setInteger(2, grp);
		query.executeUpdate();
	}

	public static void delete(Session session, int grp){
		Query query = session.createSQLQuery("delete from cx_events where grp=?");
		query.setLong(0, grp);
		query.executeUpdate();
	}

	public static void delete(Session session, long hopbyhop, long endtoend){
		Query query = session.createSQLQuery("delete from cx_events where hopbyhop=? and endtoend=?");
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		query.executeUpdate();
	}

	public static CxEvents get_next_available(Session session){
		Query query;
		query = session.createSQLQuery("select * from cx_events where hopbyhop=0 limit 1")
				.addEntity(CxEvents.class);
		return (CxEvents) query.uniqueResult();
	}

	public static List get_all_IMPI_IDs_by_HopByHop_and_EndToEnd_ID(Session session, long hopbyhop, long endtoend){
		Query query;
		query = session.createSQLQuery("select distinct id_impi from cx_events where hopbyhop=? and endtoend=?")
				.addScalar("id_impi", Hibernate.INTEGER);
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		return query.list();
	}


	public static int get_max_grp(Session session){
		Query query = session.createSQLQuery("select max(grp) from cx_events");
		Integer result = (Integer) query.uniqueResult();
		if (result == null)
			return 0;
		return result.intValue();
	}

	public static CxEvents get_one_from_grp (Session session, long hopbyhop, long endtoend){
		Query query;
		query = session.createSQLQuery("select * from cx_events where hopbyhop=? and endtoend=? limit 1")
			.addEntity(CxEvents.class);
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		return (CxEvents) query.uniqueResult();
	}


	/**
     * This method inserts a new Cx_Event in the table Cx_Events.
     * <p>
     * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es) to support the DSAI Information Element
     *
     * @params session Hibernate Session
     * @params impu_id IMPU Identifier
     * @return void
     */
	public static void insert_CxEvent (Session session, int impu_id){

		int id_impi = IMPU_DAO.get_a_registered_IMPI_ID(session, impu_id);
		if (id_impi != -1) {
			IMPI impi = IMPI_DAO.get_by_ID(session, id_impi);
			IMPU impu = IMPU_DAO.get_by_ID(session, impu_id);
	//		IMSU imsu = IMSU_DAO.get_by_IMPI_ID(session, impi.getId());
			CxEvents rtr_ppr = new CxEvents();
			rtr_ppr.setGrp(CxEvents_DAO.get_max_grp(session) + 1);
			//	Type for PPR is 2
			rtr_ppr.setType(2);
			//	Subtype	userdata is 0
			rtr_ppr.setSubtype(0);
			rtr_ppr.setId_implicit_set(impu.getId_implicit_set());
	//		rtr_ppr.setDiameter_name(imsu.getDiameter_name());
			rtr_ppr.setId_impi(impi.getId());
			rtr_ppr.setId_impu(impu_id);
			CxEvents_DAO.insert(session, rtr_ppr);
		}

	}
}
