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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.model.AliasesRepositoryData;
import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.DSAI;
import de.fhg.fokus.hss.db.model.DSAI_IMPU;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.ShNotification;
import de.fhg.fokus.hss.db.model.ShSubscription;
import de.fhg.fokus.hss.sh.ShConstants;
import de.fhg.fokus.hss.sh.data.AliasesRepositoryDataElement;
import de.fhg.fokus.hss.sh.data.RepositoryDataElement;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI Information Element according to Release 7.
 * <p>
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */

public class ShNotification_DAO {
	private static Logger logger = Logger.getLogger(ShNotification_DAO.class);

	public static void insert(Session session, ShNotification sh_notification){
		session.save(sh_notification);
	}

	public static void update(Session session, ShNotification sh_notification){
		session.save(sh_notification);
	}

	public static ShNotification get_by_ID(Session session, int id){
		Query query;
		query = session.createSQLQuery("select * from sh_notification where id=?")
			.addEntity(ShNotification.class);
		query.setInteger(0, id);

		return (ShNotification) query.uniqueResult();
	}

	public static ShNotification get_next_available(Session session){
		Query query;
		query = session.createSQLQuery("select * from sh_notification where hopbyhop=0 limit 1")
				.addEntity(ShNotification.class);
		return (ShNotification) query.uniqueResult();
	}

	public static void mark_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("update sh_notification set hopbyhop=1 where grp=?");
		query.setInteger(0, grp);
		query.executeUpdate();
	}


	public static int get_max_grp(Session session){
		Query query = session.createSQLQuery("select max(grp) from sh_notification");
		Integer result = (Integer) query.uniqueResult();
		if (result == null)
			return 0;
		return result.intValue();
	}

	public static ShNotification get_one_from_grp (Session session, int grp){
		Query query;
		query = session.createSQLQuery("select * from sh_notification where grp=? limit 1")
			.addEntity(ShNotification.class);
		query.setLong(0, grp);
		return (ShNotification) query.uniqueResult();
	}

	public static List get_all_from_grp(Session session, int grp){
		Query query;
		query = session.createSQLQuery("select * from sh_notification where grp=?")
				.addEntity(ShNotification.class);
		query.setInteger(0, grp);
		return query.list();
	}


	public static void update_by_grp(Session session, int grp, long hopByHopID, long endToEndID){
		Query query;
		query = session.createSQLQuery("update sh_notification set hopbyhop=?, endtoend=? where grp=?")
				.setLong(0, hopByHopID)
				.setLong(1, endToEndID)
				.setInteger(2, grp);
		query.executeUpdate();
	}

	public static int delete_by_ID(Session session, int id){
		Query query = session.createSQLQuery("delete from sh_notification where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}

	public static void delete(Session session, long hopbyhop, long endtoend){
		Query query = session.createSQLQuery("delete from sh_notification where hopbyhop=? and endtoend=?");
		query.setLong(0, hopbyhop);
		query.setLong(1, endtoend);
		query.executeUpdate();
	}


	public static void insert_notif_for_IMS_User_State(Session session, int id_set, int imsUserState){
		// send Sh notification to all subscribers
		int data_reference = ShConstants.Data_Ref_IMS_User_State;
		List impuList = IMPU_DAO.get_all_from_set(session, id_set);

		if (impuList != null){
			for (int i = 0; i < impuList.size(); i++){
				IMPU crtIMPU = (IMPU) impuList.get(i);
				List shSubscriptionList = ShSubscription_DAO.get_all_by_IMPU_and_DataRef(session, crtIMPU.getId(),
						data_reference);
				if (shSubscriptionList != null){
					for (int j = 0; j < shSubscriptionList.size(); j++){
						ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
						ShNotification shNotification = new ShNotification();
						shNotification.setData_ref(data_reference);
						shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
						shNotification.setId_application_server(shSubscription.getId_application_server());
						shNotification.setId_impu(crtIMPU.getId());
						shNotification.setReg_state(imsUserState);
						ShNotification_DAO.insert(session, shNotification);
					}
				}

			}
		}
	}

	public static void insert_notif_for_SCSCFName(Session session, int id_imsu, String scscfName){
		int data_reference = ShConstants.Data_Ref_SCSCF_Name;
		List impuIDList = IMPU_DAO.get_all_IMPU_ID_for_IMSU(session, id_imsu);

		if (impuIDList != null){
			for (int i = 0; i < impuIDList.size(); i++){
				Integer id_impu = (Integer) impuIDList.get(i);

				// send Sh notification to all subscribers
				List shSubscriptionList = ShSubscription_DAO.get_all_by_IMPU_and_DataRef(session, id_impu,
						data_reference);
				if (shSubscriptionList != null){
					for (int j = 0; j < shSubscriptionList.size(); j++){
						ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
						ShNotification shNotification = new ShNotification();
						shNotification.setData_ref(data_reference);
						shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
						shNotification.setId_application_server(shSubscription.getId_application_server());
						shNotification.setId_impu(id_impu);
						shNotification.setScscf_name(scscfName);
						ShNotification_DAO.insert(session, shNotification);
					}
				}
			}
		}
	}

	public static void insert_notif_for_RepData(Session session, int id_impu, RepositoryDataElement repDataElement){
		int data_reference = ShConstants.Data_Ref_Repository_Data;
		// send Sh notification to all subscribers
		List shSubscriptionList = ShSubscription_DAO.get_all_by_IMPU_DataRef_and_ServInd(session, id_impu,
				data_reference, repDataElement.getServiceIndication());

		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
				ShNotification shNotification = new ShNotification();
				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(id_impu);
				if (repDataElement.getServiceData() != null){
					shNotification.setRep_data(repDataElement.getServiceData().getBytes());
				}
				shNotification.setSqn(repDataElement.getSqn());
				shNotification.setService_indication(repDataElement.getServiceIndication());

				ShNotification_DAO.insert(session, shNotification);
			}
		}
	}

	public static void insert_notif_for_AliasesRepData(Session session, int id_impu,
			AliasesRepositoryDataElement aliasesRepDataElement){

		int data_reference = ShConstants.Data_Ref_Aliases_Repository_Data;

		IMPU impu = IMPU_DAO.get_by_ID(session, id_impu);
		if (impu == null){
			logger.error("The provided IMPU ID is not valid! Notification for Aliases Repository Data aborted...");
			return;
		}

		// send Sh notification to all subscribers
		List shSubscriptionList = ShSubscription_DAO.get_all_by_setID_and_DataRef(session, impu.getId_implicit_set(),
				data_reference);
		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
				ShNotification shNotification = new ShNotification();
				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(id_impu);
				if (aliasesRepDataElement.getServiceData() != null){
					shNotification.setRep_data(aliasesRepDataElement.getServiceData().getBytes());
				}
				shNotification.setSqn(aliasesRepDataElement.getSqn());
				shNotification.setService_indication(aliasesRepDataElement.getServiceIndication());

				ShNotification_DAO.insert(session, shNotification);
			}
		}
	}

	public static void insert_notif_for_PSI_Activation(Session session, IMPU impu){
		// send Sh notification to all subscribers
		int data_reference = ShConstants.Data_Ref_PSI_Activation;
		List shSubscriptionList = ShSubscription_DAO.get_all_by_IMPU_and_DataRef(session, impu.getId(),
			data_reference);
		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
				ShNotification shNotification = new ShNotification();
				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(impu.getId());
				shNotification.setPsi_activation(impu.getPsi_activation());
				ShNotification_DAO.insert(session, shNotification);
			}
		}
	}

	private static void insert_notif_for_iFC(Session session, IFC ifc, ApplicationServer appServ){
		int data_reference = ShConstants.Data_Ref_iFC;
		List shSubscriptionList = ShSubscription_DAO.get_all_by_ServerName_and_DataRef(session, appServ.getServer_name(), data_reference);
		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
				// get current IMPU from subscription
				IMPU impu = IMPU_DAO.get_by_ID(session, shSubscription.getId_impu());

				if (SP_IFC_DAO.get_by_SP_and_IFC_ID(session, impu.getId_sp(), ifc.getId()) != null){
					// only if the modification of the IFC affected the user's SP, we are sending the notification
					ShNotification shNotification = new ShNotification();
					shNotification.setData_ref(data_reference);
					shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
					shNotification.setId_application_server(shSubscription.getId_application_server());
					shNotification.setId_impu(shSubscription.getId_impu());
					shNotification.setServer_name(shSubscription.getServer_name());
					ShNotification_DAO.insert(session, shNotification);
				}
			}
		}
	}
	public static void insert_notif_for_iFC(Session session, IFC ifc){
		// send Sh notification to all subscribers
		ApplicationServer appServ = null;
		if (ifc.getOld_id_application_server() != -2 && ifc.getOld_id_application_server() != ifc.getId_application_server()){
			// we have to prepare notification for the old AS too
			appServ = ApplicationServer_DAO.get_by_ID(session, ifc.getOld_id_application_server());
			insert_notif_for_iFC(session, ifc, appServ);
		}
		appServ = ApplicationServer_DAO.get_by_ID(session, ifc.getId_application_server());
		insert_notif_for_iFC(session, ifc, appServ);
	}

	public static void insert_notif_for_iFC(Session session, IFC ifc, int id_sp){
		// send Sh notification to all subscribers
		int data_reference = ShConstants.Data_Ref_iFC;
		ApplicationServer appServ = ApplicationServer_DAO.get_by_ID(session, ifc.getId_application_server());

		List shSubscriptionList = ShSubscription_DAO.get_all_by_ServerName_and_DataRef(session, appServ.getServer_name(), data_reference);

		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);
				// get current IMPU from subscription
				IMPU impu = IMPU_DAO.get_by_ID(session, shSubscription.getId_impu());

				if (impu.getId_sp() == id_sp){
					// the modification of the IFC affected the SP of the user
					ShNotification shNotification = new ShNotification();
					shNotification.setData_ref(data_reference);
					shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
					shNotification.setId_application_server(shSubscription.getId_application_server());
					shNotification.setId_impu(shSubscription.getId_impu());
					shNotification.setServer_name(shSubscription.getServer_name());
					ShNotification_DAO.insert(session, shNotification);
				}
			}
		}
	}

	public static void insert_notif_for_iFC(Session session, ApplicationServer appServer){
		// send Sh notification to all subscribers
		int data_reference = ShConstants.Data_Ref_iFC;
		List shSubscriptionList = ShSubscription_DAO.get_all_by_ServerName_and_DataRef(session, appServer.getServer_name(), data_reference);

		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);

				// send notifications to all the subscribers
				ShNotification shNotification = new ShNotification();
				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(shSubscription.getId_impu());
				shNotification.setServer_name(shSubscription.getServer_name());
				ShNotification_DAO.insert(session, shNotification);
			}
		}
	}

	public static void insert_notif_for_iFC(Session session, IMPU impu){
		int data_reference = ShConstants.Data_Ref_iFC;

		List shSubscriptionList = ShSubscription_DAO.get_all_by_IMPU_and_DataRef(session, impu.getId(), data_reference);
		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);

				ShNotification shNotification = new ShNotification();
				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(shSubscription.getId_impu());
				shNotification.setServer_name(shSubscription.getServer_name());
				ShNotification_DAO.insert(session, shNotification);

			}
		}
	}

	/**
	 * This method inserts a new register in Sh_notifications for the DSAI and IMPU given
	 * <p>
	 * Method developed by Instrumentacion y Componentes S.A (Inycom) (ims at inycom dot es) to support the DSAI Information Element
	 *
	 * @param session Hibernate session
	 * @param dsai_impu Object of type DSAI_IMPU
	 * @return void
	 */
	public static void insert_notif_for_DSAI(Session session, DSAI_IMPU dsai_impu){
		int data_reference = ShConstants.Data_Ref_DSAI;
		DSAI dsai= DSAI_DAO.get_by_ID(session, dsai_impu.getId_dsai());
		IMPU impu= IMPU_DAO.get_by_ID(session, dsai_impu.getId_impu());
		List shSubscriptionList = ShSubscription_DAO.get_all_by_DSAI_Tag_and_IMPU_ID_and_DataRef(session, dsai.getDsai_tag(), impu.getId(), data_reference);
		if (shSubscriptionList != null){
			for (int j = 0; j < shSubscriptionList.size(); j++){
				ShSubscription shSubscription = (ShSubscription) shSubscriptionList.get(j);

				ShNotification shNotification = new ShNotification();

				shNotification.setData_ref(data_reference);
				shNotification.setGrp(ShNotification_DAO.get_max_grp(session) + 1);
				shNotification.setId_application_server(shSubscription.getId_application_server());
				shNotification.setId_impu(shSubscription.getId_impu());
				shNotification.setServer_name(shSubscription.getServer_name());
				shNotification.setDsai_tag(shSubscription.getDsai_tag());
				shNotification.setDsai_value(dsai_impu.getDsai_value());
				ShNotification_DAO.insert(session, shNotification);

			}
		}
	}
}
