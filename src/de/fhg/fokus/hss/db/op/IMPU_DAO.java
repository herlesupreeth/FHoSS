/*
 *  Copyright (C) 2004-2007 FhG Fokus
 *
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
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.model.CxEvents;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.SP;
import de.fhg.fokus.hss.db.model.VisitedNetwork;
import de.fhg.fokus.hss.main.HSSProperties;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI concept according to Release 7 (methods get_all_IMPU_for_IFC_list,
 * get_all and get_all_by_DSAI_IFC_and_DSAI_value).
 * <p>
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */

public class IMPU_DAO {
	private static Logger logger = Logger.getLogger(IMPU_DAO.class);
	public static void insert(Session session, IMPU impu){
		session.save(impu);
	}
	
	public static void update(Session session, IMPU impu){
		if (impu.isPsi_dirtyFlag()){
			ShNotification_DAO.insert_notif_for_PSI_Activation(session, impu);
			impu.setPsi_dirtyFlag(false);
		}
		else if (impu.isSp_dirtyFlag()){
			if (HSSProperties.iFC_NOTIF_ENABLED) 
				ShNotification_DAO.insert_notif_for_iFC(session, impu);
			if (HSSProperties.AUTO_PPR_ENABLED) {
				
				if (impu.getUser_state() == CxConstants.IMPU_user_state_Registered || impu.getUser_state() == 
						CxConstants.IMPU_user_state_Unregistered){
											
					// we process the request only if the user is in Registered or Unregistered state
					int id_impi = IMPU_DAO.get_a_registered_IMPI_ID(session, impu.getId());
					if (id_impi != -1){
						int grp = CxEvents_DAO.get_max_grp(session);
						// we have only a PPR message for the implicit set!
						CxEvents rtr_ppr = new CxEvents();
						rtr_ppr.setId_impi(id_impi);
						rtr_ppr.setId_implicit_set(impu.getId_implicit_set());
						rtr_ppr.setId_impu(impu.getId());
						// type for PPR is 2
						rtr_ppr.setType(2);
						rtr_ppr.setSubtype(0);// user-data
						rtr_ppr.setGrp(grp);
						CxEvents_DAO.insert(session, rtr_ppr);
					}
				}
				else{
					logger.warn("IMPU: " + impu.getIdentity() + " is not registered! PPR Aborted!");						
				}
			}
			impu.setSp_dirtyFlag(false);
		}
		
		session.saveOrUpdate(impu);
	}	
	
	public static void update_others_from_implicit_set_ID(Session session, int id_impu, int old_implicit_set_id){
		List l = IMPU_DAO.get_others_from_set(session, id_impu, old_implicit_set_id);
		if (l != null && l.size() > 0){
			Iterator it = l.iterator();
			IMPU crt_impu;
			int id_set = -1;
			while (it.hasNext()){
				crt_impu = (IMPU) it.next();
				if (id_set == -1){
					id_set = crt_impu.getId();
				}
				if (crt_impu.getId() != id_impu){
					crt_impu.setId_implicit_set(id_set);
					IMPU_DAO.update(session, crt_impu);
				}
			}
		}
	}
	
	public static IMPU get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from impu where id=?")
			.addEntity(IMPU.class);
		query.setInteger(0, id);
		IMPU result = (IMPU) query.uniqueResult();
		
		return result;
	}

	
	public static List get_all_VisitedNetworks_by_IMPU_ID(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery("select * from visited_network" +
				"	inner join impu_visited_network on impu_visited_network.id_visited_network=visited_network.id" +
				" where impu_visited_network.id_impu=?")
				.addEntity("visited_network", VisitedNetwork.class);
		query.setInteger(0, id_impu);
		return query.list();
	}
	
	public static List get_all_IMPU_for_VN_ID(Session session, int id_vn){
		Query query;
		query = session.createSQLQuery("select * from impu" +
				"	inner join impu_visited_network on impu_visited_network.id_impu=impu.id" +
				" where impu_visited_network.id_visited_network=? limit 0,1")
				.addEntity("impu", IMPU.class);
		query.setInteger(0, id_vn);
		return query.list();
	}

	public static List get_all_for_IMPI_ID(Session session, int id_impi){
		Query query;
		query = session.createSQLQuery("select * from impu" +
				"	inner join impi_impu on impi_impu.id_impu=impu.id" +
				" where impi_impu.id_impi=?")
				.addEntity("impu", IMPU.class);
		query.setInteger(0, id_impi);
		return query.list();
	}
	
		
	public static int get_a_registered_IMPI_ID(Session session, int id_impu){
		Query query;
		query = session.createSQLQuery("select id_impi from impi_impu" +
				"	inner join impu on impu.id=impi_impu.id_impu" +
				" where (impi_impu.user_state=1 or impi_impu.user_state=2) and impi_impu.id_impu=? limit 1")
				.addScalar("id_impi", Hibernate.INTEGER);
		query.setInteger(0, id_impu);
		Integer result = (Integer) query.uniqueResult();
		if (result == null)
			return -1;
		return result;
	}
		

	public static IMPU get_one_from_set(Session session, int id_implicit_set){
		Query query;
		query = session.createSQLQuery("select * from impu where id_implicit_set=? limit 1")
			.addEntity(IMPU.class);
		query.setInteger(0, id_implicit_set);
		IMPU result = (IMPU) query.uniqueResult();
				
		return result;
	}
	
	public static List get_aliases_IMPUs(Session session, int id_implicit_set, int id_sp){
		Query query;
		query = session.createSQLQuery("select * from impu where id_implicit_set=? and id_sp=?")
			.addEntity(IMPU.class);
		query.setInteger(0, id_implicit_set);
		query.setInteger(1, id_sp);

		return query.list();
	}
	
	public static List get_all_within_same_IMPI_Associations(Session session, int id_impu){
		List<IMPU> resultList = new ArrayList<IMPU>();
		
		List referenceIMPIList = IMPI_DAO.get_all_IMPI_for_IMPU_ID(session, id_impu);
		if (referenceIMPIList.size() == 0){
			return resultList;
		}
		
		IMPI refIMPI = (IMPI) referenceIMPIList.get(0);
		List allIMPU_ID_List = IMPU_DAO.get_all_IMPU_ID_for_IMSU(session, refIMPI.getId_imsu());
		if (allIMPU_ID_List == null || allIMPU_ID_List.size() == 0){
			return resultList;
		}
		
		
		for (int i = 0; i < allIMPU_ID_List.size(); i++){
			int crtIMPU_ID = (Integer) allIMPU_ID_List.get(i);
			List crtIMPIList = IMPI_DAO.get_all_IMPI_for_IMPU_ID(session, crtIMPU_ID);
			if (crtIMPIList.size() != referenceIMPIList.size()){
				continue;
			}
			for (int j = 0; j < referenceIMPIList.size(); j++){
				// notice: the two list are ordered by ID!
				IMPI impi1 = (IMPI) referenceIMPIList.get(j);
				IMPI impi2 = (IMPI) crtIMPIList.get(j);
				if (impi1.getId() != impi2.getId())
					break;
				if (j == referenceIMPIList.size() - 1){
					// test was finished successfully!
					IMPU crtIMPU = IMPU_DAO.get_by_ID(session, crtIMPU_ID);
					resultList.add(crtIMPU);
				}
			}
		}
		
		return resultList;
	}

	public static List get_all_Registered_within_same_IMPI_Associations(Session session, int id_impu){
		List<IMPU> resultList = new ArrayList<IMPU>();
		
		List referenceIMPIList = IMPI_DAO.get_all_IMPI_for_IMPU_ID(session, id_impu);
		if (referenceIMPIList.size() == 0){
			return resultList;
		}
		IMPI refIMPI = (IMPI) referenceIMPIList.get(0);
		List allIMPU_ID_List = IMPU_DAO.get_all_Registered_IMPU_ID_for_IMSU(session, refIMPI.getId_imsu());
		
		if (allIMPU_ID_List == null || allIMPU_ID_List.size() == 0){
			return resultList;
		}
		
		
		for (int i = 0; i < allIMPU_ID_List.size(); i++){
			int crtIMPU_ID = (Integer) allIMPU_ID_List.get(i);
			List crtIMPIList = IMPI_DAO.get_all_IMPI_for_IMPU_ID(session, crtIMPU_ID);
			if (crtIMPIList.size() != referenceIMPIList.size()){
				continue;
			}
			for (int j = 0; j < referenceIMPIList.size(); j++){
				// notice: the two list are ordered by ID!
				IMPI impi1 = (IMPI) referenceIMPIList.get(j);
				IMPI impi2 = (IMPI) crtIMPIList.get(j);
				if (impi1.getId() != impi2.getId())
					break;
				if (j == referenceIMPIList.size() - 1){
					// test was finished successfully!
					IMPU impu = IMPU_DAO.get_by_ID(session, crtIMPU_ID);
					resultList.add(impu);
				}
			}
		}
		
		return resultList;
	}	
	
	
	public static IMPU get_by_Identity(Session session, String identity){
		Query query;
		query = session.createSQLQuery("select * from impu where identity=?")
			.addEntity(IMPU.class);
		query.setString(0, identity);
		
		IMPU result = null;
		try{
			result = (IMPU) query.uniqueResult();
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}

		return result;
	}
	
	public static Object[] get_by_Incomplete_Identity(Session session, String identity, int firstResult, int maxResults) {
		Query query;
		query = session.createSQLQuery("select * from impu where identity like ?")
			.addEntity(IMPU.class);
		query.setString(0, "%" + identity + "%");
		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;		
	}
	
	public static IMPU get_by_Wildcarded_Identity(Session session, String identity, int firstResult, int maxResults){
		Query query;
		
		query = session.createSQLQuery("select * from impu where ? like wildcard_psi limit 1")
			.addEntity(IMPU.class);
		query.setString(0, identity);

		IMPU result = null;
		try{
			result = (IMPU) query.uniqueResult();
		
		}
		catch(org.hibernate.NonUniqueResultException e){
			logger.error("Query did not returned an unique result! You have a duplicate in the database!");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object[] get_all(Session session, int firstResult, int maxResults){
		Query query;
		query = session.createSQLQuery("select * from impu")
			.addEntity(IMPU.class);
		
		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;
	}
	
	
	public static List get_all_from_set(Session session, int id_set){
		Query query;
		query = session.createSQLQuery("select * from impu where id_implicit_set=?")
			.addEntity(IMPU.class);
		query.setInteger(0, id_set);
		return query.list();
	}
	
	public static Object[] get_all_from_set(Session session, int id_set, int firstResult, int maxResults){
		Query query;
		query = session.createSQLQuery("select * from impu where id_implicit_set=?")
		.addEntity(IMPU.class);
		query.setInteger(0, id_set);

		Object[] result = new Object[2];
		result[0] = new Integer(query.list().size());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		result[1] = query.list();
		return result;
	}
	
	public static List get_others_from_set(Session session, int id, int id_set){
		Query query;
		query = session.createSQLQuery("select * from impu where id != ? and id_implicit_set=?")
			.addEntity(IMPU.class);
		query.setInteger(0, id);
		query.setInteger(1, id_set);
		return query.list();
	}
	
	public static List get_all_sp_for_set(Session session, int id_implicit_set){
		Query query;
		query = session.createSQLQuery(
				"select {SP.*}, {IMPU.*} from sp SP" +
				"	inner join impu IMPU on IMPU.id_sp=SP.id" +
				"		where IMPU.id_implicit_set=? order by IMPU.id_sp")
					.addEntity(SP.class)
					.addEntity(IMPU.class);
		query.setInteger(0, id_implicit_set);
		return query.list();
	}
	
	
	public static List get_by_Charging_Info_ID(Session session, int id_charging_info){
		Query query;
		query = session.createSQLQuery("select * from impu where id_charging_info = ?")
			.addEntity(IMPU.class);
		query.setInteger(0, id_charging_info);
		return query.list();
	}

	public static List get_all_IMPU_ID_for_IMSU(Session session, int id_imsu){
		Query query;
		query = session.createSQLQuery("select distinct impu.id from impu" +
				"	inner join impi_impu on impu.id=impi_impu.id_impu" +
				"	inner join impi on impi.id=impi_impu.id_impi" +
				" where impi.id_imsu=?")
				.addScalar("id", Hibernate.INTEGER);
		query.setInteger(0, id_imsu);
		return query.list();
	}
	
	public static List get_all_Registered_IMPU_ID_for_IMSU(Session session, int id_imsu){
		Query query;
		query = session.createSQLQuery("select distinct impu.id from impu" +
				"	inner join impi_impu on impu.id=impi_impu.id_impu" +
				"	inner join impi on impi.id=impi_impu.id_impi" +
				" where impi.id_imsu=? and impu.user_state=?")
				.addScalar("id", Hibernate.INTEGER);
		query.setInteger(0, id_imsu);
		query.setInteger(1, CxConstants.IMPU_user_state_Registered);
		return query.list();
	}

	public static int delete_by_ID(Session session, int id){
		Query query = session.createSQLQuery("delete from impu where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}

	public static int delete_VisitedNetwork_for_IMPU(Session session, int id_impu, int id_vn){
		Query query = session.createSQLQuery("delete from impu_visited_network where id_impu=? and id_visited_network=?");
		query.setInteger(0, id_impu);
		query.setInteger(1, id_vn);
		return query.executeUpdate();
	}

	/**
	 * This method returns all IMPUs attached to the iFCs given.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es) to support the DSAI Information Element
	 *
	 * @param session Hibernate session
	 * @param ifc_list List of IFC
	 * @return List impu
	 */
	public static List get_all_IMPU_for_IFC_list(Session session, List ifc_list){

		List ifc_lista= new ArrayList();
		for(int i=0; i<ifc_list.size();i++){
			IFC ifc = (IFC) ifc_list.get(i);
			ifc_lista.add(ifc.getId());
		}

		if(ifc_lista.isEmpty()){
			return null;
		}

		Query query = null;
		query = session.createSQLQuery("select distinct IMPU.* from impu IMPU, sp_ifc SP_IFC, ifc IFC where SP_IFC.id_sp=IMPU.id_sp and SP_IFC.id_ifc=IFC.id and IFC.id in (:ifc_lista)").addEntity(IMPU.class);
		query.setParameterList("ifc_lista", ifc_lista);

		return query.list();
	}

	/**
	 * This method returns all IMPU saved in the database.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es) to support the DSAI Information Element
	 *
	 * @param session Hibernate session
	 * @return List of IMPU
	 */
	public static List get_all(Session session){
		Query query = session.createSQLQuery("select * from impu")
		.addEntity(IMPU.class);
		return query.list();
	}

	/**
	 * This method returns all impu associated to the DSAI and iFC given and with the DSAI-value given.
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es) to support the DSAI Information Element
	 *
	 * @param session Hibernate session
	 * @param id_dsai DSAI identifier
	 * @param id_ifc IFC identifier
	 * @param dsai_value DSAI value
	 * @return List of IMPU
	 */
	public static List get_all_by_DSAI_IFC_and_DSAI_value(Session session, int id_dsai, int id_ifc, int dsai_value){

		Query query;
		query = session.createSQLQuery("select impu.* from dsai_impu dsai_impu, dsai_ifc dsai_ifc, impu impu"+
				" where dsai_impu.dsai_value=? and dsai_impu.id_dsai=?"+
				" and dsai_impu.id_dsai=dsai_ifc.id_dsai and dsai_ifc.id_ifc=?"+
		" and dsai_impu.id_impu=impu.id").addEntity(IMPU.class);
		query.setInteger(0, dsai_value);
		query.setInteger(1, id_dsai);
		query.setInteger(2, id_ifc);
		return query.list();
	}

}
