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
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.xml.sax.InputSource;

import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;
import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.AliasesRepositoryData;
import de.fhg.fokus.hss.db.model.ApplicationServer;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.DSAI;
import de.fhg.fokus.hss.db.model.DSAI_IMPU;
import de.fhg.fokus.hss.db.model.RepositoryData;
import de.fhg.fokus.hss.db.op.AliasesRepositoryData_DAO;
import de.fhg.fokus.hss.db.op.ApplicationServer_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.RepositoryData_DAO;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.db.op.ShSubscription_DAO;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.UtilAVP;
import de.fhg.fokus.hss.sh.ShConstants;
import de.fhg.fokus.hss.sh.ShExperimentalResultException;
import de.fhg.fokus.hss.sh.ShFinalResultException;
import de.fhg.fokus.hss.sh.data.AliasesRepositoryDataElement;
import de.fhg.fokus.hss.sh.data.DSAIElement;
import de.fhg.fokus.hss.sh.data.RepositoryDataElement;
import de.fhg.fokus.hss.sh.data.ShDataElement;
import de.fhg.fokus.hss.sh.data.ShDataExtensionElement;
import de.fhg.fokus.hss.sh.data.ShDataParser;
import de.fhg.fokus.hss.sh.data.ShIMSDataElement;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI concept according to Release 7
 *
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */
public class PUR {
	private static Logger logger = Logger.getLogger(PUR.class);

	public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request){
		DiameterMessage response = diameterPeer.newResponse(request);
		// set the proxiable flag for the response
		response.flagProxiable = true;

		// add Auth-Session-State and Vendor-Specific-Application-ID to Response
		UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP, DiameterConstants.Application.Sh);

		boolean dbException = false;
		try{
			if (request.flagProxiable == false){
				logger.warn("You should notice that the Proxiable flag for PUR request was not set!");
			}

			// -0- check for mandatory fields in the message
			String vendor_specific_ID = UtilAVP.getVendorSpecificApplicationID(request);
			String auth_session_state = UtilAVP.getAuthSessionState(request);
			String origin_host = UtilAVP.getOriginatingHost(request);
			String origin_realm = UtilAVP.getOriginatingRealm(request);
			String dest_realm = UtilAVP.getDestinationRealm(request);
			String user_identity = UtilAVP.getShUserIdentity(request);
			int data_reference = UtilAVP.getDataReference(request);
			String user_data = UtilAVP.getShUserData(request);


			if (vendor_specific_ID == null || auth_session_state == null || origin_host == null || origin_realm == null ||
					dest_realm == null || user_identity == null || data_reference == -1 || user_data == null){
				throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}

			Session session = HibernateUtil.getCurrentSession();
			HibernateUtil.beginTransaction();

			// - 1 - check if the AS is allowed to update information, by checking the combination of AS identity and Data-Reference

			// in the AS table, the server address is always saved with the sip scheme included!
			ApplicationServer as = ApplicationServer_DAO.get_by_Diameter_Address(session, origin_host);

			if (as == null){
				throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_MODIFIED);
			}

			if (as.getPur() == 0){
				throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_MODIFIED);
			}

			if ((data_reference == ShConstants.Data_Ref_Repository_Data && as.getPur_rep_data() == 0) ||
					(data_reference == ShConstants.Data_Ref_PSI_Activation && as.getPur_psi_activation() == 0) ||
					(data_reference == ShConstants.Data_Ref_DSAI && as.getPur_dsai() == 0) ||
					(data_reference == ShConstants.Data_Ref_Aliases_Repository_Data && as.getPur_aliases_rep_data() == 0)){
				throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_MODIFIED);
			}

			// - 2 - check for user identity existence

			IMPU impu = IMPU_DAO.get_by_Identity(session, user_identity);
			if (impu == null){
				throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_USER_UNKNOWN);
			}

			// - 3 - check if the user identity apply to the Data-Reference, as specified in table 7.6.1 (TS 29.328)
			if (data_reference == ShConstants.Data_Ref_PSI_Activation && impu.getType() != CxConstants.Identity_Type_Distinct_PSI){
				throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_OPERATION_NOT_ALLOWED);
			}

			// - 3a -
			if (data_reference == ShConstants.Data_Ref_Aliases_Repository_Data && impu.getType() != CxConstants.Identity_Type_Public_User_Identity){
				throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_OPERATION_NOT_ALLOWED);
			}

			InputSource input = new InputSource(new ByteArrayInputStream(user_data.getBytes()));
			ShDataParser parser = new ShDataParser(input);
			ShDataElement shData = parser.getShData();

			if (shData == null){
				// we have some sort of error caused by an invalid input document
				throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
			}

			// - 4 - check Data-Reference for PSIActivation
			if (data_reference == ShConstants.Data_Ref_PSI_Activation){
				ShIMSDataElement shIMSData = shData.getShIMSData();
				if (shIMSData == null){
					throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}
				// save the new PSIActivation value
				impu.setPsi_activation(shIMSData.getPsiActivation());
				IMPU_DAO.update(session, impu);
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
			}

			else if (data_reference == ShConstants.Data_Ref_DSAI){
				// - 4a -
				//...
				//Check conditions
				ShIMSDataElement shIMSData = shData.getShIMSData();
				if (shIMSData == null){
					throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}

				List list_dsai_tag = shIMSData.getDsaiList();
				//Only need the first (maybe the only one)
				Iterator it= list_dsai_tag.iterator();
				//while (it.hasNext(){
				DSAIElement dsai_shdata= (DSAIElement) it.next();
				String dsai_tag = dsai_shdata.getTag();
				DSAI dsai = DSAI_DAO.get_by_Dsai_tag(session, dsai_tag);
				if (dsai==null){
					throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_DSAI_NOT_AVAILABLE);
				}
				else {
					int id_impu= impu.getId();
					int id_dsai= dsai.getId();
					DSAI_IMPU dsai_impu= DSAI_IMPU_DAO.get_by_DSAI_and_IMPU_ID(session, id_dsai, id_impu);
					if (dsai_impu ==null){
						throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_DSAI_NOT_AVAILABLE);
					}
					else {
						ApplicationServer as_by_diameter_address = ApplicationServer_DAO.get_by_Diameter_Address(session, origin_host);
						List ifc_list = IFC_DAO.get_all_by_AS_ID_and_IMPU_ID_and_DSAI_ID(session, as_by_diameter_address.getId(), id_impu, id_dsai);
						if (ifc_list.isEmpty()){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_DSAI_NOT_AVAILABLE);
						}
						else{
							//Only save this change if dsai_value is changed
							if (dsai_impu.getDsai_value()!= dsai_shdata.getValue()){
								dsai_impu.setDsai_value(dsai_shdata.getValue());
								DSAI_IMPU_DAO.update(session, dsai_impu);
								ShNotification_DAO.insert_notif_for_DSAI(session, dsai_impu);
								//Insert also notification for the ASs related to that active IMPU and iFC.
								ShNotification_DAO.insert_notif_for_iFC(session, impu);
								//Insert also notification for the SCSCF
								CxEvents_DAO.insert_CxEvent(session, id_impu);
							}
							UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
						}
					}
				}
				//} //end while
			}


			else if (data_reference == ShConstants.Data_Ref_Repository_Data){
				// - 5 - check for update in progress
				// -skipped for the moment

				// - 6 -
				if (shData.getRepositoryDataList() == null ){
					throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}
				Vector<RepositoryDataElement> repositoryDataList = shData.getRepositoryDataList();
				for (int i = 0; i < repositoryDataList.size(); i++){
					RepositoryDataElement repDataElement = shData.getRepositoryDataList().get(i);
					RepositoryData repData = RepositoryData_DAO.get_by_IMPU_and_ServiceIndication(session,
							impu.getId(), repDataElement.getServiceIndication());

					if (repData != null){
						if (repDataElement.getSqn() == 0 || (repDataElement.getSqn() - 1) != (repData.getSqn() % 65535)){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TRANSPARENT_DATA_OUT_OF_SYNC);
						}

						// check for Service-Data
						if (repDataElement.getServiceData() != null){
							if (repDataElement.getServiceData().length() > as.getRep_data_size_limit()){
								// repository data received is more than the HSS can offer
								throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TOO_MUCH_DATA);
							}

							// perform the update in the database
							repData.setRep_data(repDataElement.getServiceData().getBytes());
							repData.setSqn(repDataElement.getSqn());
							RepositoryData_DAO.update(session, repData);

							// this will trigger the notification for the corresponding subscriptions
							ShNotification_DAO.insert_notif_for_RepData(session, impu.getId(), repDataElement);
						}
						else{
							// Service-Data was not received , the data stored in the HSS repository will be removed!
							// delete the repository data from the HSS
							RepositoryData_DAO.delete_by_ID(session, repData.getId());

							// this will trigger the notification for the corresponding subscriptions
							ShNotification_DAO.insert_notif_for_RepData(session, impu.getId(), repDataElement);

							// this will determine the deletion of the corresnponding subscriptions
							ShSubscription_DAO.delete_subs_for_RepData(session, impu.getId(), repDataElement.getServiceIndication());
						}
					}
					else{
						// repository data is not yet stored in the HSS
						if (repDataElement.getSqn() != 0){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TRANSPARENT_DATA_OUT_OF_SYNC);
						}

						if (repDataElement.getServiceData() == null){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_OPERATION_NOT_ALLOWED);
						}

						if (repDataElement.getServiceData().length() > as.getRep_data_size_limit()){
							// repository data received is more than the HSS can offer
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TOO_MUCH_DATA);
						}

						// store the data into the HSS
						repData = new RepositoryData();
						repData.setId_impu(impu.getId());
						repData.setRep_data(repDataElement.getServiceData().getBytes());
						repData.setService_indication(repDataElement.getServiceIndication());
						repData.setSqn(repDataElement.getSqn());
						RepositoryData_DAO.insert(session, repData);
					}
				}
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
			}
			else if (data_reference == ShConstants.Data_Ref_Aliases_Repository_Data){

				if (shData.getShDataExtension() == null || shData.getShDataExtension().getAliasesRepositoryDataList() == null){
					throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY);
				}
				ShDataExtensionElement shDataExtension = shData.getShDataExtension();
				Vector<AliasesRepositoryDataElement> aliasesRepDataList = shDataExtension.getAliasesRepositoryDataList();
				for (int i = 0; i < aliasesRepDataList.size(); i++){
					AliasesRepositoryDataElement aliasesRepDataElement = shDataExtension.getAliasesRepositoryDataList().get(i);
					AliasesRepositoryData aliasesRepData = AliasesRepositoryData_DAO.get_by_setID_and_ServiceIndication(session,
							impu.getId_implicit_set(), aliasesRepDataElement.getServiceIndication());

					if (aliasesRepData != null){
						if (aliasesRepDataElement.getSqn() == 0 || (aliasesRepDataElement.getSqn() - 1) != (aliasesRepData.getSqn() % 65535)){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TRANSPARENT_DATA_OUT_OF_SYNC);
						}

						// check for Service-Data
						if (aliasesRepDataElement.getServiceData() != null){
							if (aliasesRepDataElement.getServiceData().length() > as.getRep_data_size_limit()){
								// repository data received is more than the HSS can offer
								throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TOO_MUCH_DATA);
							}

							// perform the update in the database
							aliasesRepData.setRep_data(aliasesRepDataElement.getServiceData().getBytes());
							aliasesRepData.setSqn(aliasesRepDataElement.getSqn());
							AliasesRepositoryData_DAO.update(session, aliasesRepData);

							// this will trigger the notification for the corresponding subscriptions
							ShNotification_DAO.insert_notif_for_AliasesRepData(session, impu.getId(), aliasesRepDataElement);
						}
						else{
							// Service-Data was not received , the data stored in the HSS repository will be removed!
							AliasesRepositoryData_DAO.delete_by_ID(session, aliasesRepData.getId());

							// this will trigger the notification for the corresponding subscriptions
							ShNotification_DAO.insert_notif_for_AliasesRepData(session, impu.getId(), aliasesRepDataElement);

							// this will determine the deletion of the corresnponding subscriptions
							ShSubscription_DAO.delete_subs_for_AliasesRepData(session, impu.getId_implicit_set(),
									aliasesRepDataElement.getServiceIndication());
						}
					}
					else{
						// repository data is not yet stored in the HSS
						if (aliasesRepDataElement.getSqn() != 0){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TRANSPARENT_DATA_OUT_OF_SYNC);
						}

						if (aliasesRepDataElement.getServiceData() == null){
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_OPERATION_NOT_ALLOWED);
						}

						if (aliasesRepDataElement.getServiceData().length() > as.getRep_data_size_limit()){
							// repository data received is more than the HSS can offer
							throw new ShExperimentalResultException(DiameterConstants.ExperimentalResultCode.RC_IMS_DIAMETER_ERROR_TOO_MUCH_DATA);
						}

						// store the data in the HSS
						aliasesRepData = new AliasesRepositoryData();
						aliasesRepData.setId_implicit_set(impu.getId_implicit_set());
						aliasesRepData.setRep_data(aliasesRepDataElement.getServiceData().getBytes());
						aliasesRepData.setService_indication(aliasesRepDataElement.getServiceIndication());
						aliasesRepData.setSqn(aliasesRepDataElement.getSqn());
						AliasesRepositoryData_DAO.insert(session, aliasesRepData);
					}

				}
				UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
			}
		}
		catch (HibernateException e){
			dbException = true;
			UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
			e.printStackTrace();
		}
		catch(ShExperimentalResultException e){
			UtilAVP.addExperimentalResultCode(response, e.getErrorCode(), e.getVendor());
			e.printStackTrace();
		}
		catch(ShFinalResultException e){
			UtilAVP.addResultCode(response, e.getErrorCode());
			e.printStackTrace();
		}
		finally{
			// commit transaction only when a Database doesn't occured
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}
		return response;
	}

}
