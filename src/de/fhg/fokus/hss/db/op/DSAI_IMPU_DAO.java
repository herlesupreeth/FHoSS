/*
  *  Copyright (C) 2004-2007 FhG Fokus
  *
  * Developed by Instrumentacion y Componentes S.A. (Inycom). Contact at: ims at inycom dot es
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

import de.fhg.fokus.hss.db.model.DSAI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.sh.ShConstants;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */

public class DSAI_IMPU_DAO {
	private static Logger logger = Logger.getLogger(DSAI_IMPU_DAO.class);

	public static void insert(Session session, DSAI_IMPU dsai_impu){
		session.save(dsai_impu);
	}

	public static void update(Session session, DSAI_IMPU dsai_impu){
		session.saveOrUpdate(dsai_impu);
	}

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to get an unique DSAI from a dsai identifier and an impu identifier
	 *
	 * @param session	Hibernate session
	 * @param id_dsai	DSAI identifier
	 * @param id_impu	IMPU identifier
	 * @return
	 */

	public static DSAI_IMPU get_by_DSAI_and_IMPU_ID(Session session, int id_dsai, int id_impu){
		Query query;
		query = session.createSQLQuery("select * from dsai_impu where id_dsai=? and id_impu=?")
			.addEntity(DSAI_IMPU.class);
		query.setInteger(0, id_dsai);
		query.setInteger(1, id_impu);

		DSAI_IMPU result = null;
		try{
			result = (DSAI_IMPU) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to get all IMPUs associated to a DSAI
	 *
	 * @param session	Hibernate session
	 * @param id_dsai	DSAI identifier
	 * @return
	 */

	public static List get_all_IMPU_by_DSAI_ID(Session session, int id_dsai){

		Query query;
		query = session.createSQLQuery(
					"select * from impu " +
					"	inner join dsai_impu on impu.id = dsai_impu.id_impu" +
					"		where dsai_impu.id_dsai=?")
						.addEntity(IMPU.class);
		query.setInteger(0, id_dsai);
		return query.list();

	}

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to delete form the two identifiers given
	 *
	 * @param session	Hibernate session
	 * @param id_dsai	DSAI identifier
	 * @param id_impu	IMPU identifier
	 * @return
	 */

	public static int delete_by_DSAI_and_IMPU_ID(Session session, int id_dsai, int id_impu){

		Query query;
		query = session.createSQLQuery("delete from dsai_impu where id_dsai=? and id_impu=?");
		query.setInteger(0, id_dsai);
		query.setInteger(1, id_impu);
		return query.executeUpdate();
	}



	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to return the value for an IMPU and DSAI given.
	 *
	 * @param session	Hibernate session
	 * @param id_dsai	DSAI identifier
	 * @param id_impu	IMPU identifier
	 * @return
	 */

	public static DSAI_IMPU get_DSAI_value_by_DSAI_and_IMPU_id(Session session, int id_dsai, int id_impu){

		Query query;
		query = session.createSQLQuery("select * from dsai_impu where id_impu=? and id_dsai=?");
		query.setInteger(0, id_impu);
		query.setInteger(1, id_dsai);

		return (DSAI_IMPU)query;
	}



	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to return IMPUs attached to the DSAI that, as well, have in their SPs at least one of the IFCs from ifc_list
	 *
	 * @param session	Hibernate session
	 * @param id_dsai	DSAI identifier
	 * @param ifc_list	list of IFCs
	 * @return
	 */

	public static List get_IMPU_by_DSAI_for_IFC_list(Session session, int id_dsai, List ifc_list){

		Query query;
		query = session.createSQLQuery("Select distinct i.* FROM sp_ifc s, impu i, dsai_impu di"+
				" where di.id_dsai=? and i.id=di.id_impu"+
				" and i.id_sp=s.id_sp and s.id_ifc in (:ifc_list)").addEntity(IMPU.class);
		query.setInteger(0, id_dsai);
		query.setParameterList("ifc_list", ifc_list);

		return query.list();
	}


	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to return IMPU attached to the same DSAI as the IFC given
	 *
	 * @param session	Hibernate session
	 * @param id_ifc	IFC identifier
	 * @return
	 */

	public static List get_IMPU_attached_to_same_DSAI_as_IFC(Session session, int id_ifc){


		Query query;
		query=session.createSQLQuery("select distinct impu.* from impu, dsai_impu, dsai_ifc " +
									"where dsai_ifc.id_ifc=? " +
									"and dsai_ifc.id_dsai=dsai_impu.id_dsai and impu.id=dsai_impu.id_impu").addEntity(IMPU.class);
		query.setInteger(0, id_ifc);
		return query.list();

	}

	/**
	 *
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to test if there is any Dsai inactived for the IMPU given.
	 *
	 * @param session	Hibernate session
	 * @param id_impu	IMPU identifier
	 * @return Number   Integer with muber of DSAI inactived
	 */

	public static boolean test_DSAI_Inactived_by_IMPU_id(Session session, int id_impu){

		Integer Dsai_value= (Integer) ShConstants.DSAI_value_Inactive;
		Query query;
		query = session.createSQLQuery("select * from dsai_impu where id_impu=? and dsai_value=?");
		query.setInteger(0, id_impu);
		query.setInteger(1, Dsai_value);

		List result = query.list();
		if (result != null && result.size() > 0){
			return true;
		}
		return false;
	}
}
