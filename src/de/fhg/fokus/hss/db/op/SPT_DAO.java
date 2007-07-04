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

import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.SPT;
import de.fhg.fokus.hss.main.HSSProperties;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class SPT_DAO {
	private static Logger logger = Logger.getLogger(SPT_DAO.class);
	
	private static void prepareNotifications(Session session, SPT spt){
		// get all the IFCs corresponding to the TP of the current SPT
		List ifcList = IFC_DAO.get_all_by_TP_ID(session, spt.getId_tp());
		if (ifcList != null){
			for (int i = 0; i < ifcList.size(); i++){
				IFC crtIFC = (IFC) ifcList.get(i);
				// insert notifications for all the corresponding IFC (if necessary)
				ShNotification_DAO.insert_notif_for_iFC(session, crtIFC);
			}
		}
	}
	
	public static void insert(Session session, SPT spt){
		if (HSSProperties.iFC_NOTIF_ENABLED){
			prepareNotifications(session, spt);
		}
		session.save(spt);
	}
	
	public static void update(Session session, SPT spt){
		if (HSSProperties.iFC_NOTIF_ENABLED && spt.isDirtyFlag()){
			prepareNotifications(session, spt);
			spt.setDirtyFlag(false);
		}
		session.saveOrUpdate(spt);
	}
	
	public static SPT get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from spt where id=?")
			.addEntity(SPT.class);
		query.setInteger(0, id);

		return (SPT) query.uniqueResult();
	}	
	
	public static List get_all_by_TP_ID(Session session, int id_tp){
		Query query = session.createSQLQuery("select * from spt where id_tp=? order by (grp)")
			.addEntity(SPT.class);
		query.setInteger(0, id_tp);
		return query.list();
	}
	public static int get_max_grp(Session session, int id_tp){
		Query query = session.createSQLQuery("select max(grp) from spt where id_tp=?");
			
		query.setInteger(0, id_tp);
		List res = query.list();
		if (res != null && res.size() > 0){
			Integer result = (Integer)res.get(0);
			if (result != null){
				return result;
			}
		}
		
		return 0;
	}	
	
	public static int delete_by_ID(Session session, int id){
		if (HSSProperties.iFC_NOTIF_ENABLED){
			SPT spt = SPT_DAO.get_by_ID(session, id);
			prepareNotifications(session, spt);
		}
		
		Query query = session.createSQLQuery("delete from spt where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}
}
