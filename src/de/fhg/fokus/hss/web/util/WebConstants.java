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

package de.fhg.fokus.hss.web.util;

import java.util.ArrayList;
import java.util.List;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.zh.ZhConstants;
import de.fhg.fokus.hss.sh.ShConstants;

/**
 * This class has been modified by Instrumentacion y Componentes S.A (ims at inycom dot es)
 * to support the DSAI Information Element according to Release 7 (ArrayList select_dsai_value).
 *
 * @author adp dot fokus dot fraunhofer dot de
 * Adrian Popescu / FOKUS Fraunhofer Institute
 * @author Instrumentacion y Componentes S.A (Inycom)
 * for modifications (ims at inycom dot es)
 */

public class WebConstants {
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_FAILURE = "failure";
	public static final String FORWARD_DELETE = "delete";

	public static final List<Tuple> select_identity_type;
	static{
		select_identity_type = new ArrayList<Tuple>();
		select_identity_type.add(new Tuple(CxConstants.Identity_Type_Public_User_Identity_Name, CxConstants.Identity_Type_Public_User_Identity));
		select_identity_type.add(new Tuple(CxConstants.Identity_Type_Distinct_PSI_Name, CxConstants.Identity_Type_Distinct_PSI));
		select_identity_type.add(new Tuple(CxConstants.Identity_Type_Wildcarded_PSI_Name, CxConstants.Identity_Type_Wildcarded_PSI));

	}

	public static final List<Tuple> select_auth_scheme;
	static{
		select_auth_scheme = new ArrayList<Tuple>();
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_AKAv1_Name, CxConstants.Auth_Scheme_AKAv1));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_AKAv2_Name, CxConstants.Auth_Scheme_AKAv2));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_MD5_Name, CxConstants.Auth_Scheme_MD5));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_Digest_Name, CxConstants.Auth_Scheme_Digest));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_HTTP_Digest_MD5_Name, CxConstants.Auth_Scheme_HTTP_Digest_MD5));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_Early_Name, CxConstants.Auth_Scheme_Early));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_NASS_Bundled_Name, CxConstants.Auth_Scheme_NASS_Bundled));
		select_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_SIP_Digest_Name, CxConstants.Auth_Scheme_SIP_Digest));
	}

	public static final List<Tuple> select_default_handling;
	static{
		select_default_handling = new ArrayList<Tuple>();
		select_default_handling.add(new Tuple("Session - Continued", CxConstants.Default_Handling_Session_Continued));
		select_default_handling.add(new Tuple("Session - Terminated", CxConstants.Default_Handling_Session_Terminated));
	}

	public static final List<Tuple> select_condition_type_cnf;
	static{
		select_condition_type_cnf = new ArrayList<Tuple>();
		select_condition_type_cnf.add(new Tuple(CxConstants.ConditionType_DNF_Name, CxConstants.ConditionType_DNF));
		select_condition_type_cnf.add(new Tuple(CxConstants.ConditionType_CNF_Name, CxConstants.ConditionType_CNF));
	}

	public static final List<Tuple> select_spt_type;
	static{
		select_spt_type = new ArrayList<Tuple>();
		select_spt_type.add(new Tuple("Request-URI", CxConstants.SPT_Type_RequestURI));
		select_spt_type.add(new Tuple("Method", CxConstants.SPT_Type_Method));
		select_spt_type.add(new Tuple("SIP-Header", CxConstants.SPT_Type_SIPHeader));
		select_spt_type.add(new Tuple("Session-Case", CxConstants.SPT_Type_SessionCase));
		select_spt_type.add(new Tuple("SDP-Line", CxConstants.SPT_Type_SessionDescription));
	}

	public static final ArrayList<Tuple> select_spt_method_type;
	static{
		select_spt_method_type = new ArrayList<Tuple>();
		select_spt_method_type.add(new Tuple("INVITE", "INVITE"));
		select_spt_method_type.add(new Tuple("REGISTER", "REGISTER"));
		select_spt_method_type.add(new Tuple("CANCEL", "CANCEL"));
		select_spt_method_type.add(new Tuple("OPTION", "OPTION"));
		select_spt_method_type.add(new Tuple("PUBLISH", "PUBLISH"));
		select_spt_method_type.add(new Tuple("SUBSCRIBE", "SUBSCRIBE"));
		select_spt_method_type.add(new Tuple("MESSAGE", "MESSAGE"));
		select_spt_method_type.add(new Tuple("INFO", "INFO"));
		select_spt_method_type.add(new Tuple("REFER (outside dialog only)", "REFER"));
		//etc
	}
	public static final ArrayList<Tuple> select_profile_part_indicator;
	static{
		select_profile_part_indicator = new ArrayList<Tuple>();
		select_profile_part_indicator.add(new Tuple(CxConstants.Profile_Part_Indicator_Any_Name,
				CxConstants.Profile_Part_Indicator_Any));
		select_profile_part_indicator.add(new Tuple(CxConstants.Profile_Part_Indicator_Registered_Name,
				CxConstants.Profile_Part_Indicator_Registered));
		select_profile_part_indicator.add(new Tuple(CxConstants.Profile_Part_Indicator_UnRegistered_Name,
				CxConstants.Profile_Part_Indicator_UnRegistered));
	}

	public static final ArrayList<Tuple> select_cap_type;
	static{
		select_cap_type = new ArrayList<Tuple>();
		select_cap_type.add(new Tuple("Optional", 0));
		select_cap_type.add(new Tuple("Mandatory", 1));
	}
	public static final ArrayList<Tuple> select_direction_of_request;
	static{
		select_direction_of_request = new ArrayList<Tuple>();
		select_direction_of_request.add(new Tuple(CxConstants.Direction_of_Request_Originating_Session_Name,
				CxConstants.Direction_of_Request_Originating_Session));
		select_direction_of_request.add(new Tuple(CxConstants.Direction_of_Request_Terminating_Registered_Name,
				CxConstants.Direction_of_Request_Terminating_Registered));
		select_direction_of_request.add(new Tuple(CxConstants.Direction_of_Request_Terminating_Unregistered_Name,
				CxConstants.Direction_of_Request_Terminating_Unregistered));
		select_direction_of_request.add(new Tuple(CxConstants.Direction_of_Request_Originating_Unregistered_Name,
				CxConstants.Direction_of_Request_Originating_Unregistered));
		select_direction_of_request.add(new Tuple(CxConstants.Direction_of_Request_Originating_Cdiv_Name,
                CxConstants.Direction_of_Request_Originating_Cdiv));
	}

	public static final int PPR_Apply_for_User_Data = 0;
	public static final int PPR_Apply_for_Charging_Func = 1;
	public static final int PPR_Apply_for_Both = 2;
	public static final ArrayList<Tuple> select_ppr_apply_for;
	static{
		select_ppr_apply_for = new ArrayList<Tuple>();
		select_ppr_apply_for.add(new Tuple("User-Data", PPR_Apply_for_User_Data));
		select_ppr_apply_for.add(new Tuple("Charging-Func", PPR_Apply_for_Charging_Func));
		select_ppr_apply_for.add(new Tuple("Both", PPR_Apply_for_Both));
	}

	public static final int RTR_Apply_for_IMPUs = 0;
	public static final int RTR_Apply_for_IMPIs = 1;
	public static final ArrayList<Tuple> select_rtr_apply_for;
	static{
		select_rtr_apply_for = new ArrayList<Tuple>();
		select_rtr_apply_for.add(new Tuple("IMPU(s) of crt IMPI", RTR_Apply_for_IMPUs));
		select_rtr_apply_for.add(new Tuple("IMPI(s) of crt IMSU", RTR_Apply_for_IMPIs));
	}

	public static final ArrayList<Tuple> select_rtr_reason;
	static{
		select_rtr_reason = new ArrayList<Tuple>();
		select_rtr_reason.add(new Tuple(CxConstants.RTR_Permanent_Termination_Name, CxConstants.RTR_Permanent_Termination));
		select_rtr_reason.add(new Tuple(CxConstants.RTR_New_Server_Assigned_Name, CxConstants.RTR_New_Server_Assigned));
		select_rtr_reason.add(new Tuple(CxConstants.RTR_Server_Change_Name, CxConstants.RTR_Server_Change));
		select_rtr_reason.add(new Tuple(CxConstants.RTR__Remove_S_CSCF_Name, CxConstants.RTR_Remove_S_CSCF));
	}

	// Zh Select Lists
	public static final ArrayList<Tuple> select_uicc_type;
	static{
		select_uicc_type = new ArrayList<Tuple>();
		select_uicc_type.add(new Tuple(ZhConstants.UICC_Type_Basic_GBA_Name, ZhConstants.UICC_Type_Basic_GBA));
		select_uicc_type.add(new Tuple(ZhConstants.UICC_Type_GBA_U_Name, ZhConstants.UICC_Type_GBA_U));
	}


	// we have for Zh only AKA and MD5
	public static final List<Tuple> select_zh_auth_scheme;
	static{
		select_zh_auth_scheme = new ArrayList<Tuple>();
		select_zh_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_AKAv1_Name, CxConstants.Auth_Scheme_AKAv1));
		select_zh_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_AKAv2_Name, CxConstants.Auth_Scheme_AKAv2));
		select_zh_auth_scheme.add(new Tuple(CxConstants.Auth_Scheme_MD5_Name, CxConstants.Auth_Scheme_MD5));
	}

	public static final List<Tuple> select_uss_type;
	static{
		select_uss_type = new ArrayList<Tuple>();
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_Unspecific_Service_Name,
				ZhConstants.GAA_Service_Type_Unspecific_Service));
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_PKI_Portal_Name,
				ZhConstants.GAA_Service_Type_PKI_Portal));
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_Authentication_Proxy_Name,
				ZhConstants.GAA_Service_Type_Authentication_Proxy));
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_Presence_Name,
				ZhConstants.GAA_Service_Type_Presence));
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_MBMS_Name,
				ZhConstants.GAA_Service_Type_MBMS));
		select_uss_type.add(new Tuple(ZhConstants.GAA_Service_Type_Liberty_Alliance_Project_Name,
				ZhConstants.GAA_Service_Type_Liberty_Alliance_Project));

	}

	public static final ArrayList<Tuple> select_dsai_value;
	static{
		select_dsai_value= new ArrayList<Tuple>();
		select_dsai_value.add(new Tuple(ShConstants.DSAI_value_Active_Name, ShConstants.DSAI_value_Active));
		select_dsai_value.add(new Tuple(ShConstants.DSAI_value_Inactive_Name, ShConstants.DSAI_value_Inactive));
	}
	public static final String DSAI_ACTIVE = "Active";
	public static final String DSAI_INACTIVE = "Inactive";


	// Tomcat Security Permissions
	public static final String Security_Permission_ADMIN = "admin";
	public static final String Security_Permission_USER = "user";



}
