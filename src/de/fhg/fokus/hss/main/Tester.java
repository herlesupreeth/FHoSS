/**
 * 
 */
package de.fhg.fokus.hss.main;


import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.xml.sax.InputSource;

import de.fhg.fokus.hss.auth.HexCodec;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMSU;
import de.fhg.fokus.hss.db.model.RTR_PPR;
import de.fhg.fokus.hss.db.model.RepositoryData;
import de.fhg.fokus.hss.db.op.CapabilitiesSet_DAO;
import de.fhg.fokus.hss.db.op.DB_Op;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMSU_DAO;
import de.fhg.fokus.hss.db.op.RTR_PPR_DAO;
import de.fhg.fokus.hss.db.op.RepositoryData_DAO;
import de.fhg.fokus.hss.db.hibernate.*;
import de.fhg.fokus.hss.diam.DiameterStack;
import de.fhg.fokus.hss.sh.data.ShDataElement;
import de.fhg.fokus.hss.sh.data.ShDataParser;
import de.fhg.fokus.hss.zh.op.MAR;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class Tester extends Thread{
	public void run() {
		/*
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		IMPI impi = IMPI_DAO.get_by_ID(session, 1);
		System.out.println("\nGUSS:\n" + MAR.getGUSS(session, impi));
		HibernateUtil.commitTransaction();
		
		RepositoryData repData = new RepositoryData();
		repData.setId_impu(1);
		repData.setRep_data("asasa".getBytes());
		repData.setService_indication("asasa");
		repData.setSqn(1);
		
		RepositoryData_DAO.insert(session, repData);
		HibernateUtil.commitTransaction();
		int id = repData.getId();
		
		session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		
		repData = RepositoryData_DAO.get_by_ID(session, id);
		repData.setRep_data(null);
		//RepositoryData_DAO.delete_by_ID(session, 11);
		HibernateUtil.commitTransaction();
		*/
		
/*		InputSource input;
		String inputString = 
		
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Sh-Data> " +
		"<Sh-Data-Extension> " +
		"<AliasesRepositoryData> " +
		"<ServiceIndication>service_adi</ServiceIndication> " +
		"<SequenceNumber>1</SequenceNumber>" +
		"<ServiceData><data>gicu merge la plimbare</data></ServiceData>" +
		"</AliasesRepositoryData>" +
		"</Sh-Data-Extension> " +
		"</Sh-Data>";
		input = new InputSource(new ByteArrayInputStream(inputString.getBytes()));
		ShDataParser parser = new ShDataParser(input);

		ShDataElement shData = parser.getShData();
		if (shData != null)
			System.out.println(shData.toString());
		else
			System.out.println("ShData is NULL!");
		
		List l = shData.getShDataExtension().getAliasesRepositoryDataList();
		for (int i=0; i < l.size(); i++){
			System.out.println("Rep:" + l.get(i).toString());
		}*/
		/*
		InputSource input;// = new InputSource(new FileReader("files//online1.xml"));
		String inputString = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<Sh-Data xmlns=\"urn:ietf:params:xml:ns:pidf\" >" +
			"<PublicIdentifiers>" +
				"<IMSPublicIdentity>" +
					"<PublicIdentity>" +
						"sip:alice@open-ims.test" + 
					"</PublicIdentity>" +
				"</IMSPublicIdentity>" +
				"<IMSPublicIdentity>" +
					"<PublicIdentity>" +
						"sip:alice2@open-ims.test" + 
					"</PublicIdentity>" +
				"</IMSPublicIdentity>" +
				"<MSISDN>" +
					"msisdn_id" +
				"</MSISDN>" +
				"<MSISDN>" +
					"msisdn_id2" +
				"</MSISDN>" +
				"<IdentityType>" +
					"1" + 
				"</IdentityType>" +
				"<WildcardedPSI>" +
					"wildcarded-psi" + 
				"</WildcardedPSI>" +
			"</PublicIdentifiers>" +
			
			"<RepositoryData>" +
				"<ServiceIndication>" + "service_indication" + "</ServiceIndication>" +
				"<SequenceNumber>" + "6" + "</SequenceNumber>" +
				"<ServiceData>" + "service_data" + "</ServiceData>" +
			"</RepositoryData>" +
			
			"<Sh-IMS-Data>" +
				"<SCSCFName>" + "scscf_name" + "</SCSCFName>" +
				"<IMSUSerState>" + "1" + "</IMSUSerState>" +
				"<IFCs>" +
					"<InitialFilterCriteria>" +
						"<Priority>" + "1" + "</Priority>" +
						"<TriggerPoint>" +
							"<ConditionTypeCNF>" + "1" + "</ConditionTypeCNF>" +
							"<SPT>" +
								"<ConditionNegated>" + "1" + "</ConditionNegated>" +
								"<Group>" + "1" + "</Group>" +
								"<RequestURI>" + "publish" + "</RequestURI>" +  
							"</SPT>" +

							"<SPT>" +
								"<ConditionNegated>" + "1" + "</ConditionNegated>" +
								"<Group>" + "1" + "</Group>" +
								"<SIPHeader>" +
									"<Header>" +
										"Event" + 
									"</Header>" +
									"<Content>" +
										"presence" + 
									"</Content>" +
								"</SIPHeader>" +
							"</SPT>" +
							"<SPT>" +
								"<ConditionNegated>" + "1" + "</ConditionNegated>" +
								"<Group>" + "1" + "</Group>" +
								"<SessionDescription>" +
									"<Line>" +
										"sdp_line" + 
									"</Line>" +
									"<Content>" +
										"sdp_content" + 
									"</Content>" +
								"</SessionDescription>" +
							"</SPT>" +

						"</TriggerPoint>" +
						"<ApplicationServer>" +
							"<ServerName>" + "AS" + "</ServerName>" +
							"<DefaultHandling>" + "2" + "</DefaultHandling>" +
							"<ServiceInfo>" + "serviceinfo" + "</ServiceInfo>" +
						"</ApplicationServer>" +
						"<ProfilePartIndicator>" + "1" + "</ProfilePartIndicator>" +
					"</InitialFilterCriteria>" + 
				"</IFCs>" +
				"<ChargingInformation>" +
					"<PrimaryEventChargingFunctionName>" + "p_ecf" + "</PrimaryEventChargingFunctionName>" +
					"<SecondaryEventChargingFunctionName>" + "s_ecf" + "</SecondaryEventChargingFunctionName>" +
					"<PrimaryChargingCollectionFunctionName>" + "p_ccf" + "</PrimaryChargingCollectionFunctionName>" +
					"<SecondaryChargingCollectionFunctionName>" + "s_ccf" + "</SecondaryChargingCollectionFunctionName>" +
				"</ChargingInformation>" +
				"<Extension>" +
					"<PSIActivation>" + "1" + "</PSIActivation>" + 
					"<Extension>" +
						"<DSAI>" +
							"<DSAI-Tag>" +
								"dsai-tag" + 
							"</DSAI-Tag>" +
							"<DSAI-Value>" +
								"5" + 
							"</DSAI-Value>" +
						"</DSAI>" +
						"<DSAI>" +
							"<DSAI-Tag>" +
								"dsai-tag2" + 
							"</DSAI-Tag>" +
							"<DSAI-Value>" +
								"8" + 
							"</DSAI-Value>" +
						"</DSAI>" +
					"</Extension>" +	
				"</Extension>" +
			"</Sh-IMS-Data>" +
			
			"<CSLocationInformation>" +
				"<LocationNumber>" + "1" + "</LocationNumber>" +
				"<CellGlobalId>" + "1" + "</CellGlobalId>" +
				"<ServiceAreaId>" + "1" + "</ServiceAreaId>" +
				"<LocationAreaId>" + "1" + "</LocationAreaId>" +
				"<GeographicalInformation>" + "1" + "</GeographicalInformation>" +
				"<GeodeticInformation>" + "1" + "</GeodeticInformation>" +
				"<VLRNumber>" + "1" + "</VLRNumber>" +
				"<MSCNumber>" + "1" + "</MSCNumber>" +
				"<CurrentLocationRetrieved>" + "1" + "</CurrentLocationRetrieved>" +
				"<AgeOfLocationInformation>" + "1" + "</AgeOfLocationInformation>" +
			"</CSLocationInformation>" +

			"<PSLocationInformation>" +
				"<CellGlobalId>" + "1" + "</CellGlobalId>" +
				"<ServiceAreaId>" + "1" + "</ServiceAreaId>" +
				"<LocationAreaId>" + "1" + "</LocationAreaId>" +
				"<RoutingAreaId>" + "1" + "</RoutingAreaId>" +
				"<GeographicalInformation>" + "1" + "</GeographicalInformation>" +
				"<GeodeticInformation>" + "1" + "</GeodeticInformation>" +
				"<SGSNNumber>" + "1" + "</SGSNNumber>" +
				"<CurrentLocationRetrieved>" + "1" + "</CurrentLocationRetrieved>" +
				"<AgeOfLocationInformation>" + "1" + "</AgeOfLocationInformation>" +
			"</PSLocationInformation>" +
			"<CSUserState>" +
				"1" + 
			"</CSUserState>" +
			"<PSUserState>" +
				"2" +
			"</PSUserState>" +
			"<Sh-Data-Extension>" +
				"<RegisteredIdentities>" +
					"<PublicIdentifiers>" +
						"<IMSPublicIdentity>" +
							"<PublicIdentity>" +
								"sip:bob@open-ims.test" + 
							"</PublicIdentity>" +
						"</IMSPublicIdentity>" +
						"<IMSPublicIdentity>" +
							"<PublicIdentity>" +
								"sip:bob2@open-ims.test" + 
							"</PublicIdentity>" +
						"</IMSPublicIdentity>" +
					"</PublicIdentifiers>" +
				"</RegisteredIdentities>" +
				
				"<ImplicitIdentities>" +
				"<PublicIdentifiers>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:implicit@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:implicit2@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
				"</PublicIdentifiers>" +
				"</ImplicitIdentities>" +

				"<AllIdentities>" +
				"<PublicIdentifiers>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:all@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:all2@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
				"</PublicIdentifiers>" +
				"</AllIdentities>" +

				"<AliasIdentities>" +
				"<PublicIdentifiers>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:all@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
					"<IMSPublicIdentity>" +
						"<PublicIdentity>" +
							"sip:all2@open-ims.test" + 
						"</PublicIdentity>" +
					"</IMSPublicIdentity>" +
				"</PublicIdentifiers>" +
				"</AliasIdentities>" +
				
				"<AliasesRepositoryData>" +
					"<ServiceIndication>" + "aliases_service_indication" + "</ServiceIndication>" +
					"<SequenceNumber>" + "7" + "</SequenceNumber>" +
					"<ServiceData>" + "aliases_service_data" + "</ServiceData>" +
				"</AliasesRepositoryData>" +

				"<AliasesRepositoryData>" +
					"<ServiceIndication>" + "aliases2_service_indication" + "</ServiceIndication>" +
					"<SequenceNumber>" + "27" + "</SequenceNumber>" +
					"<ServiceData>" + "aliases2_service_data" + "</ServiceData>" +
				"</AliasesRepositoryData>" +
			"</Sh-Data-Extension>" +
			"</Sh-Data>";
		
		input = new InputSource(new ByteArrayInputStream(inputString.getBytes()));
		ShDataParser parser = new ShDataParser(input);
		
*/		
/*		  		
		ShData shData = parser.getShData();
		if (shData != null)
			System.out.println(shData.toString());		
		else
			System.out.println("The output was null!");
*/			
		/*
		long time1 = System.currentTimeMillis();
		Session session = HibernateUtil.getCurrentSession();
		HibernateUtil.getCurrentSession().beginTransaction();
		
		long t01= System.currentTimeMillis();
		//IMPI_IMPU_DAO.get_IMPI(session, 2);
		long t02= System.currentTimeMillis();
		System.out.println("Delta select t0: " + (t02 - t01));
		
		IMPI impi = new IMPI();
		impi.setIdentity("sasas");
		impi.setK("saasa");
		impi.setAuth_scheme(1);
		impi.setIp("");
		impi.setAmf(HexCodec.decode("0000"));
		impi.setOp(HexCodec.decode("0000000000"));
		impi.setSqn(new String(HexCodec.decode("1111")));
		impi.setId_imsu(-1);
		//impi.setDefault_auth_scheme()
		impi.setLine_identifier("erere");
		
		IMPI_DAO.insert(session, impi);
		impi.setIdentity("id2");
		IMPI_DAO.update(session, impi);
		
		IMPI impi2 = IMPI_DAO.get_by_ID(session, impi.getId());
		System.out.println("\n\nIMPI Identitu:" + impi2.getIdentity());
		
		int xmed = 0;
		int cnt = 0;
		for (int i = 0; i < 1000; i++){		
			long t1;
			t1= System.currentTimeMillis();
			//IMPI impi = IMPI_DAO.getByID(session, 2);
			
			// List list = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI(session, 2);
			//DB_Op.setUserState(session, 2, 1, (short)0, true);
			//IMPI_IMPU_DAO.get_all_Default_IMPU(session, 2);
			//IMPI_IMPU_DAO.get_all_Default_IMPU_of_Set_by_IMPI(session, 2);
			long t2;
			t2 = System.currentTimeMillis();
			System.out.println("Delta select one: " + (t2 - t1));
			xmed += t2 - t1;
		}
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		
		System.out.println("XMED:" + xmed / 1000);
		
		long time3 = System.currentTimeMillis();
		System.out.println("Delta last after first selection process: " + (time3 - time1));
		System.out.println("Sleep 15 secs");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Last measurement:");
		long t4, t5;
		t4 = System.currentTimeMillis();
		session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();
		//List list = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI(session, 2);
		HibernateUtil.commitTransaction();
		session.close();
		t5 = System.currentTimeMillis();
		System.out.println("Time difference is:" + (t5 - t4));
		
		xmed = 0;
		session = HibernateUtil.getCurrentSession();
		HibernateUtil.beginTransaction();		
		for (int i = 0; i < 1000; i++){		
			long t1;
			t1= System.currentTimeMillis();
			//IMPI impi = IMPI_DAO.getByID(session, 2);
			//list = IMPI_IMPU_DAO.get_all_IMPU_by_IMPI(session, 2);
			//IMSU imsu = IMSU_DAO.getByID(session, 1);
			//DB_Op.setUserState(session, 2, 1, (short)0, true);
			//IMPI_IMPU_DAO.get_all_Default_IMPU(session, 2);
			//IMPI_IMPU_DAO.get_all_Default_IMPU_of_Set_by_IMPI(session, 2);
			long t2;
			t2 = System.currentTimeMillis();
			System.out.println("Delta select two: " + (t2 - t1));
			xmed += t2 - t1;
		}
		System.out.println("XMED:" + xmed / 1000);
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		
/*		
		ApplicationServer as = new ApplicationServer();
		as.setName("adrian");
		as.setDiameter_address("diameter");
		as.setDefault_handling(1);
		as.setServer_name("server-name");
		as.setService_info("info");
		as.setRep_data_size_limit(100);
		HibernateUtil.getCurrentSession().save(as);
		HibernateUtil.commitTransaction();
		long time2 = System.currentTimeMillis();
		long delta = time2 - time1;
		System.out.println("Time difference is:" + delta);
		time2 = System.currentTimeMillis();
		HibernateUtil.getCurrentSession().beginTransaction();
		HibernateUtil.getCurrentSession().delete(as);
		HibernateUtil.commitTransaction();
		long time3 = System.currentTimeMillis();
		System.out.println("Time difference2 is:" + (time3 - time2));
		time3 = System.currentTimeMillis();
		
		HibernateUtil.getCurrentSession().beginTransaction();
		as = new ApplicationServer();
		as.setName("adrian2");
		as.setDiameter_address("diameter");
		as.setDefault_handling(1);
		as.setServer_name("server-name");
		as.setService_info("info");
		as.setRep_data_size_limit(100);
		HibernateUtil.getCurrentSession().save(as);
		HibernateUtil.commitTransaction();
		long time4 = System.currentTimeMillis();
		System.out.println("Time difference is:" + (time4 - time3));
		
		SP sp = new SP();
		sp.setName("default_sp");
		
		ChargingInfo ci = new ChargingInfo();
		ci.setName("default_chinfo");
		ci.setPri_ccf("a");
		ci.setSec_ccf("b");
		ci.setPri_ecf("c");
		ci.setSec_ecf("d");*/
		
/*		HibernateUtil.beginTransaction();
		//HibernateUtil.getCurrentSession().saveOrUpdate(sp);
		//HibernateUtil.getCurrentSession().saveOrUpdate(ci);
/*		IMSU imsu = new IMSU();
		imsu.setDiameter_name("diameter_name");
		imsu.setName("name");
		HibernateUtil.getCurrentSession().save(imsu);
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		ApplicationServerDAO.joinTest();*/

/*		HibernateUtil.beginTransaction();
		List result = IMPI_IMPU_DAO.getJoinResult(HibernateUtil.getCurrentSession(), 1);
		System.out.println("Result size is:" + result.size());
		
		Object[] all = (Object[])result.get(0);
		IMPI_IMPU row = (IMPI_IMPU)all[0];
		System.out.println("IMPI:" + row.getImpi().getIdentity() + " IMPU:" + row.getImpu().getIdentity() + " user state:" + row.getUser_state());
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();*/
	}
}
