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

import de.fhg.fokus.hss.db.model.ShSubscription;
import de.fhg.fokus.hss.sh.ShConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class ShSubscription_DAO {
	private static Logger logger = Logger.getLogger(ShSubscription_DAO.class);
	
	public static void insert(Session session, ShSubscription sh_subscription){
		session.save(sh_subscription);
	}

	public static void update(Session session, ShSubscription sh_subscription){
		session.saveOrUpdate(sh_subscription);
	}
	
	public static ShSubscription get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id);

		return (ShSubscription) query.uniqueResult();
	}	

	
	public static ShSubscription get_by_AS_IMPU_and_DataRef(Session session, int id_application_server, int id_impu, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_application_server=? and id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_application_server);
		query.setInteger(1, id_impu);
		query.setInteger(2, data_ref);
		return (ShSubscription) query.uniqueResult();
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
		return (ShSubscription) query.uniqueResult();
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

		return (ShSubscription) query.uniqueResult();
	}	

	public static List get_all_by_IMPU_and_DataRef(Session session, int id_impu, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_impu);
		query.setInteger(1, data_ref);
		return  query.list();
	}
	
	public static List get_all_by_setID_and_DataRef(Session session, int id_implicit_set, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_implicit_set);
		query.setInteger(1, data_ref);
		return  query.list();
	}		

	public static List get_all_by_ServerName_and_DataRef(Session session, String server_name, int data_ref){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where server_name=? and data_ref=?")
			.addEntity(ShSubscription.class);
		query.setString(0, server_name);
		query.setInteger(1, data_ref);
		return  query.list();
	}		
	
	
	
	
	public static List get_all_by_IMPU_DataRef_and_ServInd(Session session, int id_impu, int data_ref, String service_indication){
		Query query;
		query = session.createSQLQuery("select * from sh_subscription where id_impu=? and data_ref=? and service_indication=?")
			.addEntity(ShSubscription.class);
		query.setInteger(0, id_impu);
		query.setInteger(1, data_ref);
		query.setString(2, service_indication);
		
		return  query.list();
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
	
	
}
