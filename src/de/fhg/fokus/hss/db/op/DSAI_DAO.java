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

import de.fhg.fokus.hss.db.model.DSAI;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */

public class DSAI_DAO {
	private static Logger logger = Logger.getLogger(DSAI_DAO.class);

	/**
	 * This method saves a new DSAI in the database
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element.
	 *
	 * @param session	Hibernate session
	 * @param dsai	object of type DSAI
	 */

	public static void insert(Session session, DSAI dsai){
		session.save(dsai);
	}

	/**
	 * This method updates any of the fields of a DSAI already saved in the database
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element.
	 *
	 * @param session	Hibernate session
	 * @param dsai	object of type DSAI
	 */
	public static void update(Session session, DSAI dsai){
		session.save(dsai);
	}

	/**
	 * This method retrieves a DSAI from the database whose identifier is the given one.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element.
	 *
	 * @param session	Hibernate session
	 * @param id	identifier
	 * @return object of type DSAI
	 */

	public static DSAI get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from dsai where id=?")
			.addEntity(DSAI.class);
		query.setInteger(0, id);

		return (DSAI) query.uniqueResult();
	}

	/**
	 * This method retrieves a DSAI from the database whose dsai_tag is the given one.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element
	 *
	 * @param session	Hibernate session
	 * @param dsai_tag Tag that identifies the DSAI
	 * @return object of type DSAI
	 */

	public static DSAI get_by_Dsai_tag(Session session, String dsai_tag){
		Query query;
		query = session.createSQLQuery("select * from dsai where dsai_tag like ?")
			.addEntity(DSAI.class);
		query.setString(0, dsai_tag);
		DSAI result = null;

		try{
			result = (DSAI) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * This method retrieves from the database a number of DSAIs equal to maxResult, beginning in the row firstResult.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element.
	 *
	 * @param session	Hibernate session
	 * @param firstResult row of the first DSAI to retrieve
	 * @param maxResults number of DSAIs to retrieve
	 * @return Array composed of 2 objects, the second one is a List of DSAIs and the first one, an
	 * Integer that expreses the number of elements of the list of DSAIs.
	 */

	public static Object[] get_all(Session session, int firstResult, int maxResults){
		Query query;
		query = session.createSQLQuery("select * from dsai")
			.addEntity(DSAI.class);

		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;
	}

	/**
	 * This method retrieves all DSAIs saved in the database.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element
	 *
	 * @param session	Hibernate session
	 * @return List of DSAI objects
	 */

	public static List get_all(Session session){
		Query query;
		query = session.createSQLQuery("select * from dsai")
			.addEntity(DSAI.class);

		return query.list();
	}

	/**
	 * This method deletes the DSAI with the identifier given from the database.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element
	 *
	 * @param session	Hibernate session
	 * @param id	DSAI identifier
	 * @return result of the update
	 */

	public static int delete_by_ID(Session session, int id){
		Query query = session.createSQLQuery("delete from dsai where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}

	/**
	 * This method retrieves the DSAIs with the Tag given that are between the row first
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support the DSAI Information Element.
	 *
	 * @param session	Hibernate session
	 * @param dsai_tag Tag that identifies the DSAI
	 * @param firstResult first row to look for
	 * @param maxResults number of rows to look for
	 * @return Array of objects that contains the DSAIs retrieved
	 */

	public static Object[] get_by_Wildcarded_Tag(Session session, String dsai_tag,
			int firstResult, int maxResults){

		Query query;
		query = session.createSQLQuery("select * from dsai where dsai_tag like ?")
			.addEntity(DSAI.class);
		query.setString(0, "%" + dsai_tag + "%");

		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;


	}
}
