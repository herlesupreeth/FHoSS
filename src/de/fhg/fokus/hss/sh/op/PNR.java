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

package de.fhg.fokus.hss.sh.op;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.SPT;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.ShNotification;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.TP;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.SPT_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.TP_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.DiameterStack;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.sh.ShConstants;
import de.fhg.fokus.hss.sh.data.AliasesRepositoryDataElement;
import de.fhg.fokus.hss.sh.data.ApplicationServerElement;
import de.fhg.fokus.hss.sh.data.DSAIElement;
import de.fhg.fokus.hss.sh.data.InitialFilterCriteriaElement;
import de.fhg.fokus.hss.sh.data.PublicIdentityElement;
import de.fhg.fokus.hss.sh.data.RepositoryDataElement;
import de.fhg.fokus.hss.sh.data.SPTElement;
import de.fhg.fokus.hss.sh.data.ShDataElement;
import de.fhg.fokus.hss.sh.data.ShDataExtensionElement;
import de.fhg.fokus.hss.sh.data.ShIMSDataElement;
import de.fhg.fokus.hss.sh.data.TriggerPointElement;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI concept according to Release 7
 *
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */

public class PNR {
	private static Logger logger = Logger.getLogger(PNR.class);

	public static void sendRequest(DiameterStack diameterStack, int id_as, int id_impu, int group){
		DiameterPeer diameterPeer = diameterStack.diameterPeer;
		DiameterMessage request = diameterPeer.newRequest(DiameterConstants.Command.PNR, DiameterConstants.Application.Sh);
		request.flagProxiable = true;

		// add SessionId
		UtilAVP.addSessionID(request, diameterPeer.FQDN, diameterStack.getNextSessionID());

		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(request, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(request, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Sh);

		boolean dbException = false;
		try{
			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			ApplicationServer applicationServer = ApplicationServer_DAO.get_by_ID(session, id_as);
			if (applicationServer == null){
				logger.error("Application Server was not found in the DB! Aborting...");
				return;
			}
			IMPU impu = IMPU_DAO.get_by_ID(session, id_impu);

			// add Destination-Host and Destination-Realm
			String destHost = applicationServer.getDiameter_address();
			UtilAVP.addDestinationHost(request, destHost);
			UtilAVP.addDestinationRealm(request, destHost.substring(destHost.indexOf('.') + 1));


			// prepare the ShData to be sent to the AS
			List<ShNotification> list = ShNotification_DAO.get_all_from_grp(session, group);
			if (list == null){
				logger.error("Sh-Notification-List coresponding to the AS:" + applicationServer.getName() + " and IMPU: " + impu.getIdentity() +
						" is NULL! Aborting...");
				return;
			}

			ShDataElement shData = new ShDataElement();
			for (int i = 0; i < list.size(); i++){
				ShNotification sh_notif = list.get(i);
				PNR.getShNotifData(shData, sh_notif);
			}

			// add the ShData to the request
			UtilAVP.addShData(request, shData.toString());
			// add the User-Identity
			UtilAVP.addShUserIdentity(request, impu.getIdentity());

			// update all the rows from the same group
			ShNotification_DAO.update_by_grp(session, group, request.hopByHopID, request.endToEndID);

			// send the request
			if (!diameterPeer.sendRequestTransactional(applicationServer.getDiameter_address(), request, diameterStack)){
				ShNotification_DAO.delete(session, request.hopByHopID, request.endToEndID);
			}
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}

	}

	public static void getShNotifData(ShDataElement shData, ShNotification shNotification){
		Session session = HibernateUtil.getCurrentSession();
		String crt_service_indication = shNotification.getService_indication();

		int crt_data_ref = shNotification.getData_ref();
		ShIMSDataElement shIMSData = shData.getShIMSData();
		if (crt_data_ref == ShConstants.Data_Ref_Charging_Info || crt_data_ref == ShConstants.Data_Ref_DSAI || crt_data_ref == ShConstants.Data_Ref_iFC
				|| crt_data_ref == ShConstants.Data_Ref_IMS_User_State || crt_data_ref == ShConstants.Data_Ref_PSI_Activation ||
				crt_data_ref == ShConstants.Data_Ref_SCSCF_Name){
			if (shIMSData == null){
				shIMSData = new ShIMSDataElement();
				shData.setShIMSData(shIMSData);
			}
		}
		ShDataExtensionElement shDataExtension = shData.getShDataExtension();
		if (crt_data_ref == ShConstants.Data_Ref_Aliases_Repository_Data){

			if (shDataExtension == null){
				shDataExtension = new ShDataExtensionElement();
				shData.setShDataExtension(shDataExtension);
			}
		}

		switch (crt_data_ref){
				case  ShConstants.Data_Ref_Repository_Data:
					RepositoryDataElement repDataElement = new RepositoryDataElement();
					if (shNotification.getRep_data() != null){
						repDataElement.setServiceData(new String(shNotification.getRep_data()));
					}
					repDataElement.setSqn(shNotification.getSqn());
					repDataElement.setServiceIndication(crt_service_indication);
					shData.addRepositoryData(repDataElement);
					break;

				case  ShConstants.Data_Ref_Aliases_Repository_Data:
					AliasesRepositoryDataElement aliasesRepDataElement = new AliasesRepositoryDataElement();
					if (shNotification.getRep_data() != null){
						aliasesRepDataElement.setServiceData(new String(shNotification.getRep_data()));
					}
					aliasesRepDataElement.setSqn(shNotification.getSqn());
					aliasesRepDataElement.setServiceIndication(crt_service_indication);
					shDataExtension.addAliasesRepositoryData(aliasesRepDataElement);
					break;

				case  ShConstants.Data_Ref_IMS_Public_Identity:
					// for the moment Aliases-Identities is interpreted the same as Implicit-Set Identities!
					IMPU impu = IMPU_DAO.get_by_ID(session, shNotification.getId_impu());
					List impuList = IMPU_DAO.get_all_from_set(session, impu.getId_implicit_set());
					if (impuList == null){
						logger.error("IMPU List is NULL. The list should contain at least one element!");
						return;
					}
					PublicIdentityElement pIdentityElement = shData.getPublicIdentifiers();
					if (pIdentityElement == null){
						pIdentityElement = new PublicIdentityElement();
						shData.setPublicIdentifiers(pIdentityElement);
					}

					for (int i = 0; i < impuList.size(); i++){
						IMPU crtIMPU = (IMPU)impuList.get(i);
						if (i == 0){
							// add the identity type for all the IMPUs
							pIdentityElement.setIdentityType(crtIMPU.getType());
						}
						pIdentityElement.addPublicIdentity(crtIMPU.getIdentity());
					}
					break;

				case  ShConstants.Data_Ref_IMS_User_State:

					shIMSData.setImsUserState(shNotification.getReg_state());
					break;

				case  ShConstants.Data_Ref_SCSCF_Name:
					shIMSData.setScscfName(shNotification.getScscf_name());
					if (shNotification.getScscf_name() == null){
						shIMSData.setAddEmptySCSCFName(true);
					}
					break;

				case  ShConstants.Data_Ref_iFC:
					ApplicationServer serviceAS = ApplicationServer_DAO.get_by_Server_Name(session, shNotification.getServer_name());
					if (serviceAS == null){
						logger.error("Server-Name AS was not found! Aborting...");
						return;
					}

					impu = IMPU_DAO.get_by_ID(session, shNotification.getId_impu());
					if (impu == null){
						logger.error("IMPU was not found! Aborting...");
						return;
					}

					// initialize ApplicationServer
					ApplicationServerElement asElement = new ApplicationServerElement();
					asElement.setDefaultHandling(serviceAS.getDefault_handling());
					asElement.setServerName(serviceAS.getServer_name());
					asElement.setServiceInfo(serviceAS.getService_info());

					// Set of IFCs active for the user (not de-activated by any DSAI)
					List ifcList = IFC_DAO.get_all_IFCs_by_IMPU_ID_and_DSAI_Value_Active(session, shNotification.getId_impu());
					if (ifcList != null){

						Iterator it = ifcList.iterator();
						while (it.hasNext()){
							IFC crt_ifc = (IFC) it.next();
							if (crt_ifc.getId_application_server() == serviceAS.getId()){
								InitialFilterCriteriaElement ifcElement = new InitialFilterCriteriaElement();

								ifcElement.setApplicationServer(asElement);
								SP_IFC sp_ifc = SP_IFC_DAO.get_by_SP_and_IFC_ID(session, impu.getId_sp(), crt_ifc.getId());
								ifcElement.setPriority(sp_ifc.getPriority());
								ifcElement.setProfilePartIndicator(crt_ifc.getProfile_part_ind());

								// set the trigger point
								TP tp = TP_DAO.get_by_ID(session, crt_ifc.getId_tp());
								if (tp != null){
									TriggerPointElement tpElement = new TriggerPointElement();
									tpElement.setConditionTypeCNF(tp.getCondition_type_cnf());

									List sptList = SPT_DAO.get_all_by_TP_ID(session, tp.getId());
									if (sptList != null){
										Iterator it2 = sptList.iterator();
										SPT crt_spt;
										SPTElement sptElement;
										while (it2.hasNext()){
											crt_spt = (SPT) it2.next();
											sptElement = new SPTElement();
											sptElement.setConditionNegated(crt_spt.getCondition_negated());
											sptElement.setGroupID(crt_spt.getGrp());

											sptElement.setMethod(crt_spt.getMethod());
											sptElement.setRequestURI(crt_spt.getRequesturi());
											if (crt_spt.getSession_case() != null){
												sptElement.setSessionCase(crt_spt.getSession_case());
											}
											sptElement.setSessionDescLine(crt_spt.getSdp_line());
											sptElement.setSessionDescContent(crt_spt.getSdp_line_content());
											sptElement.setSipHeader(crt_spt.getHeader());
											sptElement.setSipHeaderContent(crt_spt.getHeader_content());

											// extension
											sptElement.addRegistrationType(crt_spt.getRegistration_type());
											tpElement.addSPT(sptElement);
										}
									}
									ifcElement.setTriggerPoint(tpElement);
								}

								shIMSData.addInitialFilterCriteria(ifcElement);
							}
						}
						if (shIMSData.getIfcList() == null){
							shIMSData.setAddEmptyIFCs(true);
						}
					}
					break;

				case  ShConstants.Data_Ref_PSI_Activation:
					shIMSData.setPsiActivation(shNotification.getPsi_activation());
					break;

				case  ShConstants.Data_Ref_DSAI:
					DSAIElement dsai = new DSAIElement();
					dsai.setTag(shNotification.getDsai_tag());
					dsai.setValue(shNotification.getDsai_value());
					shIMSData.addDSAI(dsai);
					break;
			}

	}

	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage response){
		deleteShNotification(response.hopByHopID, response.endToEndID);
	}

	public static void processTimeout(DiameterMessage request){
		deleteShNotification(request.hopByHopID, request.endToEndID);
	}

	private static void deleteShNotification(long hopByHopID, long endToEndID){
		boolean dbException = false;
		try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();
        	ShNotification_DAO.delete(session, hopByHopID, endToEndID);
		}
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}
	}
}
