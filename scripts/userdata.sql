use hssdb;
--add a roaming network
insert into networks(network_string) values ('open-ims.test');

--add charging information
insert into chrginfo(
	name, 
	pri_chrg_coll_fn_name,
 	sec_chrg_coll_fn_name,
 	pri_event_chrg_fn_name,
 	sec_event_chrg_fn_name)
values (
	'default_chrg',
	'prim_chrg',
	'sec_chrg',
	'pri_event',
	'sec_event');

--add a service profile
insert into svp(name) values ('default_sp');

--add an application server
insert into apsvr(name, address, default_handling) values('default_as', 'sip:localhost:5065', 0);	

--add permitions to the application server
insert into as_perm_list(apsvr_id, pull, pull_rep_data, pull_impu, pull_impu_user_state, pull_scscf_name, pull_ifc, pull_loc_info,
	pull_user_state, pull_charging, pull_psi, pull_msisdn, upd_rep_data, upd_psi, sub_rep_data, sub_impu_user_state,
	sub_scscfname, sub_ifc, sub_psi)
values((select apsvr_id from apsvr where apsvr.name='default_as'),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);	

--add a triggering point
insert into trigpt(name, cnf) values ('default_tp', 1);
insert into spt(trigpt_id, sip_method, session_case, neg, groupId, type) values ((select trigpt_id from trigpt where trigpt.name='default_tp'), 'SUBSCRIBE', 0, 0, 0, 2);
insert into spt(trigpt_id, sip_method, session_case, neg, groupId, type) values ((select trigpt_id from trigpt where trigpt.name='default_tp'), 'PUBLISH', 0, 0, 0, 2);
insert into spt(trigpt_id, sip_header, session_case, sip_header_content, neg, groupId, type) values ((select trigpt_id from trigpt where trigpt.name='default_tp'), 'Event', 0, '.*presence.*', 0, 1, 5);

--add an initial filter criteria
insert into ifc(trigpt_id, apsvr_id, ifc_name) values ((select trigpt_id from trigpt where trigpt.name='default_tp'), (select apsvr_id from apsvr where apsvr.name='default_as'), 'default_ifc');

--add initial filter criteria to the service profile
insert into ifc2svp(ifc_id, svp_id, priority) values ((select ifc_id from ifc where ifc.ifc_name='default_ifc'), (select svp_id from svp where svp.name='default_sp'), 0); 

--add IMS Users
insert into imsu(name) values ('alice_imsu');
insert into imsu(name) values ('bob_imsu');

--add Private Identity

--alice@open-ims.test
insert into impi(
	impi_string, 
	imsu_id, 
	imsi, 
	scscf_name, 
	s_key, 
	chrg_id, 
	sqn) 
values(
	'alice@open-ims.test', 
	(select imsu_id from imsu where imsu.name='alice_imsu'), 
	'alice_ISDN_User_part_ID', 
	'sip:scscf.open-ims.test:6060', 
	'616c6963650000000000000000000000', 
	(select chrg_id from chrginfo where chrginfo.name='default_chrg'), 
	'000000000000');

--bob@open-ims.test
insert into impi(
	impi_string, 
	imsu_id, 
	imsi, 
	scscf_name, 
	s_key, 
	chrg_id, 
	sqn) 
values(
	'bob@open-ims.test', 
	(select imsu_id from imsu where imsu.name='bob_imsu'), 
	'bob_ISDN_User_part_ID', 
	'sip:scscf.open-ims.test:6060', 
	'626f6200000000000000000000000000', 
	(select chrg_id from chrginfo where chrginfo.name='default_chrg'), 
	'000000000000');

--add Public Identity
insert into impu(sip_url, tel_url, svp_id) values ('sip:alice@open-ims.test','tel:004912345678', (select svp_id from svp where svp.name='default_sp'));	
insert into impu(sip_url, tel_url, svp_id) values ('sip:bob@open-ims.test','tel:004987654321', (select svp_id from svp where svp.name='default_sp'));	

--add Public Identity to Private Identity
insert into impu2impi(impi_id, impu_id) values ((select impi_id from impi where impi.impi_string='alice@open-ims.test'), (select impu_id from impu where impu.sip_url='sip:alice@open-ims.test'));
insert into impu2impi(impi_id, impu_id) values ((select impi_id from impi where impi.impi_string='bob@open-ims.test'), (select impu_id from impu where impu.sip_url='sip:bob@open-ims.test'));
 
--add roaming network
insert into roam(impi_id, nw_id) values((select impi_id from impi where impi.impi_string='alice@open-ims.test'), (select nw_id from networks where networks.network_string='open-ims.test'));
insert into roam(impi_id, nw_id) values((select impi_id from impi where impi.impi_string='bob@open-ims.test'), (select nw_id from networks where networks.network_string='open-ims.test'));

