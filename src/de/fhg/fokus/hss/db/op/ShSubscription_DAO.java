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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.ShSubscription;
import de.fhg.fokus.hss.sh.ShConstants;

/**
 * This class has been modified to delete expired subscriptions from Sh-subscriptions table
 * and support DSAI concepts according to release 7
 *
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom) for modifications (ims at inycom dot es)
 */

public class ShSubscription_DAO {
	private static Logger logger = Logger.getLogger(ShSubscription_DAO.class);

	public static void insert(Session session, ShSubscription sh_subscription){
		session.save(sh_subscription);
	}

	public static void update(Session session, ShSubscription sh_subscription){
		session.saveOrUpdate(sh_subscription);
	}

	/**
	 * Each time the HSS tries to get a list of subscriptions from the Sh-Subscriptions table,
	 * it checks if the expiration-date of each subscription is over. If it is,
	 * HSS deletes that Subscription from the table.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to delete expired subscriptions from Sh-subscriptions table
	 *
	 * @param session	Hibernate session
	 * @param subscriptions_list	List of subscriptions got from Sh-Subscriptions table
	 * @return updated list of subscriptions, without the expired ones
	 */

	public static List update_list_of_subscriptions(Session session, List subscriptions_list){
		if (subscriptions_list.isEmpty()) {
			return subscriptions_list;
		}
		List<ShSubscription> updated_list = new ArrayList<ShSubscription>();
		Iterator it = subscriptions_list.iterator();
		while (it.hasNext()) {
			ShSubscription subs = (ShSubscription) it.next();
			long expiration_date = subs.getExpires();
			if (expiration_date != -1) {
				if (expiration_date <= System.currentTimeMillis()) {
					Query query = session.createSQLQuery("delete from sh_subscription where id=?");
					query.setInteger(0, subs.getId());
					query.executeUpdate();
				}
				else updated_list.add(subs);
			}
			else updated_list.add(subs);
		}
		return updated_list;
	}

	/**
	 * Each time the HSS tries to get a subscription from the Sh-Subscriptions table,
	 * it checks if the expiration-date of the subscription is over. If it is,
	 * HSS deletes that Subscription from the table.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom)(ims at inycom dot es)
	 * to delete expired subscriptions from Sh-subscriptions table
	 *
	 * @param session	Hibernate session
	 * @param unique_subscription	Subscription got from Sh-Subscriptions table
	 * @return null or the subscription, if the subscription has expired or not
	 */

	public static ShSubscription update_unique_subscription(Session session, ShSubscription unique_subscription) {
		if (unique_subscription == null){
			return null;
		}
		long expiration_date = unique_subscription.getExpires();
		if (expiration_date != -1) {
			if (expiration_date <= System.currentTimeMillis()) {
				Query query = session.createSQLQuery("delete from sh_subscription where id=?");
				query.setInteger(0, unique_subscription.getId());
				query.executeUpdate();
				return null;
			}
		}
		return unique_subscription;
	}


	public static ShSubscription get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id);
		ShSubscription updated_subscription = update_unique_subscription(session, (ShSubscription) query.uniqueResult());
		return updated_subscription;
	}


	public static ShSubscription get_by_AS_IMPU_and_DataRef(Session session, int id_application_server, int id_impu, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_application_server=? and id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		ShSubscription updated_subscription = update_unique_subscription(session, (ShSubscription) query.uniqueResult());
		return updated_subscription;
	}

	public static ShSubscription get_by_AS_IMPU_DataRef_and_ServerName(Session session, int id_application_server, int id_impu, int data_ref,
			String server_name){

		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_application_server=? and id_impu=? and data_ref=? and server_name=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, server_name);
		ShSubscription updated_subscription = update_unique_subscription(session, (ShSubscription) query.uniqueResult());
		return updated_subscription;
	}

	public static ShSubscription get_by_AS_IMPU_DataRef_and_ServInd(Session session, int id_application_server, int id_impu,
			int data_ref, String service_ind){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_application_server=? and id_impu=? and " +
				"data_ref=? and service_indication=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, service_ind);
		ShSubscription updated_subscription = update_unique_subscription(session, (ShSubscription) query.uniqueResult());
		return updated_subscription;
	}

	public static List get_all_by_IMPU_and_DataRef(Session session, int id_impu, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_impu);
		query.setInteger(1, data_ref);
		List updated_list = update_list_of_subscriptions(session,query.list());
		return  updated_list;
	}

	public static List get_all_by_setID_and_DataRef(Session session, int id_implicit_set, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_implicit_set);
		query.setInteger(1, data_ref);
		List updated_list = update_list_of_subscriptions(session,query.list());
		return  updated_list;
	}

	public static List get_all_by_ServerName_and_DataRef(Session session, String server_name, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where server_name=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setString(0, server_name);
		query.setInteger(1, data_ref);
		List updated_list = update_list_of_subscriptions(session,query.list());
		return  updated_list;
	}




	public static List get_all_by_IMPU_DataRef_and_ServInd(Session session, int id_impu, int data_ref, String service_indication){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=? and service_indication=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_impu);
		query.setInteger(1, data_ref);
		query.setString(2, service_indication);
		List updated_list = update_list_of_subscriptions(session,query.list());
		return  updated_list;
	}

	public static int delete_by_ID(Session session, int id){
		Query query = session.createSQLQuery("delete from sh_subscription where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}

	public static int delete_by_AS_IMPU_and_DataRef(Session session, int id_application_server, int id_impu, int data_ref){
		Query query = session.createSQLQuery("delete from sh_subscription where id_application_server=? and id_impu=? and data_ref=?");
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		return query.executeUpdate();
	}

	public static int delete_by_AS_IMPU_DataRef_and_DSAITag(Session session, int id_application_server, int id_impu,
			int data_ref, String dsai_tag){
		Query query = session.createSQLQuery("delete from sh_subscription where id_application_server=? and id_impu=? and " +
				"data_ref=? and dsai_tag=?");
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, dsai_tag);
		return query.executeUpdate();
	}

	public static int delete_by_AS_IMPU_DataRef_and_ServInd(Session session, int id_application_server, int id_impu, int data_ref,
			String service_indication){
		Query query = session.createSQLQuery("delete from sh_subscription where id_application_server=? and id_impu=? " +
				"and data_ref=? and service_indication=?");
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, service_indication);
		return query.executeUpdate();
	}

	public static int delete_by_AS_IMPU_DataRef_and_ServerName(Session session, int id_application_server, int id_impu,
			int data_ref, String server_name){
		Query query = session.createSQLQuery("delete from sh_subscription where id_application_server=? and id_impu=? " +
				"and data_ref=? and server_name=?");
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, server_name);
		return query.executeUpdate();
	}

	public static void delete_subs_for_RepData(Session session, int id_impu, String service_indication){

		Query query = session.createSQLQuery("delete from sh_subscription where id_impu=? and service_indication=? " +
			"and data_ref=?");
		query.setInteger(0, id_impu);
		query.setString(1, service_indication);
		query.setInteger(2, ShConstants.Data_Ref_Repository_Data);
		query.executeUpdate();
	}

	public static void delete_subs_for_AliasesRepData(Session session, int id_implicit_set, String service_indication){

		// !!!! for subscriptions to AliasesRepData the "id_impu" from ShSubscription takes the value of the "id_implicit_set" !!!
		Query query = session.createSQLQuery("delete from sh_subscription where id_impu=? and service_indication=? " +
			"and data_ref=?");
		query.setInteger(0, id_implicit_set);
		query.setString(1, service_indication);
		query.setInteger(2, ShConstants.Data_Ref_Aliases_Repository_Data);
		query.executeUpdate();
	}

	/**
	 * Get a subscription with especific Application Server, IMPU, Data Reference, DSAITag and Server Name
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support DSAI concepts according to release 7
	 *
	 * @param session	Hibernate session
	 * @param id_application_server	application_server identifier
	 * @param id_impu	impu identifier
	 * @param data_ref	data reference identifier type
	 * @param dsai_tag	dsai name
	 * @param server_name Names of the AS to suscribe
	 * @return	subscription
	 */

	public static ShSubscription get_by_AS_IMPU_DataRef_DSAITag_and_Server_name(Session session, int id_application_server, int id_impu,
			int data_ref, String dsai_tag, String server_name){
		Query query = session.createSQLQuery("select * from sh_subscription where id_application_server=? and id_impu=? and " +
				"data_ref=? and dsai_tag=? and server_name=?")
		.addEntity(ShSubscription.class);
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		query.setString(3, dsai_tag);
		query.setString(4, server_name);

		ShSubscription updated_subscription = update_unique_subscription(session, (ShSubscription) query.uniqueResult());
		return updated_subscription;
	}

	/**
	 * Get a list of subscriptions with especific IMPU, Data Reference and DSAITag
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es)
	 * to support DSAI concepts according to release 7
	 *
	 * @param session	Hibernate session
	 * @param dsai_tag	dsai name
	 * @param id_impu	impu identifier
	 * @param	data_ref	data reference identifier type
	 * @return	subscription list
	 */

	public static List get_all_by_DSAI_Tag_and_IMPU_ID_and_DataRef(Session session, String dsai_tag, int id_impu, int data_ref){

		Query query;
		query = session.createSQLQuery("select * from sh_subscription where dsai_tag=? and id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setString(0, dsai_tag);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		List updated_list = update_list_of_subscriptions(session,query.list());
		return updated_list;
	}
}
