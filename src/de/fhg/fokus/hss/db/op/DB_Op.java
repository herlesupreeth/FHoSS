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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class DB_Op {
	private static Logger logger = Logger.getLogger(DB_Op.class);
	
	public static void setUserState(Session session, int id_impi, int id_impu_implicitset, short user_state,
			boolean apply_on_IMPU){

		Query query = session.createSQLQuery(
				"select {IMPI_IMPU.*}, {IMPU.*} from impi_impu IMPI_IMPU" +
				"	inner join impu IMPU on IMPI_IMPU.id_impu=IMPU.id" +
				"		where IMPU.id_implicit_set=? and IMPI_IMPU.id_impi=?")
					.addEntity(IMPI_IMPU.class)
					.addEntity(IMPU.class);

		query.setInteger(0, id_impu_implicitset);
		query.setInteger(1, id_impi);
		
		List resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()){
			Object[] row = (Object[]) it.next(); 
			IMPI_IMPU impi_impu = (IMPI_IMPU)row[0];
			impi_impu.setUser_state(user_state);
			session.saveOrUpdate(impi_impu);
			if (apply_on_IMPU){
				IMPU impu = (IMPU)row[1];
				impu.setUser_state(user_state);
				session.saveOrUpdate(impu);
			}
		}
	}
	
	public static void resetUserState(Session session, int id_implicit_set){
		Query query = session.createSQLQuery(
				"select * from impi_impu " +
				"	inner join impu on impi_impu.id_impu=impu.id" +
				"		where impu.id_implicit_set=?")
					.addEntity(IMPI_IMPU.class);
		query.setInteger(0, id_implicit_set);

		List resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()){
			IMPI_IMPU impi_impu = (IMPI_IMPU) it.next();
			impi_impu.setUser_state(CxConstants.IMPU_user_state_Not_Registered);
			session.saveOrUpdate(impi_impu);
		}
	}
	
	public static void resetAuthPending(Session session, int id_impi, int id_impu_implicitset){
		
		Query query = session.createSQLQuery(
				"select * from impi_impu " +
				"	inner join impu on impi_impu.id_impu=impu.id" +
				"		where impu.id_implicit_set=? and impi_impu.id_impi=?")
					.addEntity(IMPI_IMPU.class);
		query.setInteger(0, id_impu_implicitset);
		query.setInteger(1, id_impi);
		
		// update the user state on impi_impu and on impu table
		List resultList = query.list();
		Iterator it = resultList.iterator();

		while (it.hasNext()){
			IMPI_IMPU impi_impu = (IMPI_IMPU) it.next();
			impi_impu.setUser_state(CxConstants.IMPU_user_state_Not_Registered);
			session.saveOrUpdate(impi_impu);
		}
	}
	
	
	
	public static void deregister_all_IMPUs_for_an_IMPI_ID(Session session, int id_impi){

		Query query = session.createSQLQuery(
				"update impi_impu, impu set impi_impu.user_state=0, impu.user_state=0 where " +
				"	impi_impu.id_impi=? and impu.id=impi_impu.id_impu");
		
		query.setInteger(0, id_impi);
		query.executeUpdate();
	}
	
	public static void deregister_IMPU_for_an_IMPI_ID(Session session, int id_impu, int id_impi){
		Query query = session.createSQLQuery(
				"update impi_impu, impu set impi_impu.user_state=0, impu.user_state=0" +
				"	where impu.id=? and impi_impu.id_impu=impu.id" +
				"		and impi_impu.id_impi=?");
		
		query.setInteger(0, id_impu);
		query.setInteger(1, id_impi);
		query.executeUpdate();
	}
	
}
