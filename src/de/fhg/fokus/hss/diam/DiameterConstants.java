/*
 * $Id: Constants.java 119 2007-02-06 16:48:35 +0000 (Tue, 06 Feb 2007) adp $
 *
 * Copyright (C) 2004-2009 FhG Fokus
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
 *//*
 * $Id: Constants.java 119 2007-02-06 16:48:35 +0000 (Tue, 06 Feb 2007) adp $
 *
 * Copyright (C) 2004-2006 FhG Fokus
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
package de.fhg.fokus.hss.diam;

/**
 * It contains all the possible values for Application, Vendor, Command Code, AVP Code, Authentication Scheme
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 * 
 * @author andreea dot ancuta dot onofrei at fokus dot fraunhofer dot de 
 * Andreea Ancuta Onofrei / FOKUS Fraunhofer Institute
 *
 */

public class DiameterConstants
{
    /** 
     *  an inner class containing the constants for application
     */
    public class Application {
			/** constant for cx application */
			public static final int Cx = 16777216;
			/** constant for sh application */
			public static final int Sh = 16777217;
			/** constant for zh application */
			public static final int Zh = 16777221;
    }
 
    /** 
     *  an inner class containing the constants for vendor
     */    
    public class Vendor
    {
        /** constant for diam */
        public static final int DIAM = 0;
        /** constant for v3gpp */
        public static final int V3GPP = 10415;
        /** constant for ETSI */
        public static final int VETSI = 13019;		/**< Vendor Id for ETSI */
        /** constant for CableLabs */
        public static final int VCableLabs = 4491;	/**< Vendor Id for CableLabs */
     
    }

    /** 
     *  an inner class containing the constants for ims 
     *  specific requests and responses
     */  
    public class Command
    {
    	/** constant for Capablities Exchange Request */
        public static final int CER = 257;
        /** constant for Device Watchdog request */
        public static final int DWR = 280;
        /** constant for User Authorization Request */
        public static final int UAR = 300;
        /** constant for Server Assignment Request */
        public static final int SAR = 301;
        /** constant for Location Information Request */
        public static final int LIR = 302;
        /** constant for Multimedia Authentication Request */
        public static final int MAR = 303;
        /** constant for Registration Termination Request */
        public static final int RTR = 304;
        /** constant for Push Profile Request */
        public static final int PPR = 305;
        /** constant for User Data Request */
        public static final int UDR = 306;
        /** constant for Profile Update Request */
        public static final int PUR = 307;
        /** constant for Subscriber Notification Request */
        public static final int SNR = 308;
        /** constant for Push Notification Request */
        public static final int PNR = 309;
        /** constant for Multimedia Authentication Request for Zh-Interface */
		public static final int MARzh = 303;
    }
    
    public class AVPCode{
    	
    	// Diameter AVPs
        public static final int USER_NAME = 1;
        public static final int FRAMED_IP_ADDRESS = 8;
    	public static final int DIGEST_REALM = 104;
    	public static final int DIGEST_QOP = 110;
    	public static final int DIGEST_ALGORITHM = 111;
    	public static final int DIGEST_AUTH_PARAM = 117;
    	public static final int DIGEST_DOMAIN = 119;
    	public static final int DIGEST_HA1 = 121;
    	public static final int AUTH_APPLICATION_ID = 258;
        public static final int VENDOR_SPECIFIC_APPLICATION_ID = 260;
        public static final int SESSION_ID = 263;
        public static final int ORIGIN_HOST = 264;
        public static final int ORIGIN_REALM = 296;
        public static final int VENDOR_ID = 266;
        public static final int RESULT_CODE = 268;
    	public static final int AUTH_SESSION_STATE = 277;
        public static final int DESTINATION_HOST = 293;
        public static final int DESTINATION_REALM = 283;
        public static final int EXPERIMENTAL_RESULT = 297;
        public static final int EXPERIMENTAL_RESULT_CODE = 298;
        
        // Cx AVPs
        public static final int IMS_VISITED_NETWORK_IDENTIFIER = 600;
        public static final int IMS_PUBLIC_IDENTITY = 601;
        public static final int IMS_SERVER_NAME = 602;
        public static final int IMS_SERVER_CAPABILITIES = 603;
        public static final int IMS_MANDATORY_CAPABILITY = 604;
        public static final int IMS_OPTIONAL_CAPABILITY = 605;
        public static final int IMS_USER_DATA = 606;
        public static final int IMS_SIP_NUMBER_AUTH_ITEMS = 607;
        public static final int IMS_SIP_AUTHENTICATION_SCHEME = 608;
        public static final int IMS_SIP_AUTHENTICATE = 609;
        public static final int IMS_SIP_AUTHORIZATION = 610;
        public static final int IMS_SIP_AUTHENTICATION_CONTEXT = 611;
        public static final int IMS_SIP_AUTH_DATA_ITEM = 612;
        public static final int IMS_SIP_ITEM_NUMBER = 613;
        public static final int IMS_SERVER_ASSIGNMENT_TYPE = 614;
        public static final int IMS_DEREGISTRATION_REASON = 615;
        public static final int IMS_REASON_CODE = 616;
        public static final int IMS_REASON_INFO = 617;
        public static final int IMS_CHARGING_INFORMATION = 618;
        public static final int IMS_PRI_EVENT_CHARGING_FN_NAME = 619;
        public static final int IMS_SEC_EVENT_CHARGING_FN_NAME = 620;
        public static final int IMS_PRI_CHRG_COLL_FN_NAME = 621;
        public static final int IMS_SEC_CHRG_COLL_FN_NAME = 622;
        public static final int IMS_USER_AUTHORIZATION_TYPE = 623;
        public static final int IMS_USER_DATA_ALREADY_AVAILABLE = 624;
        public static final int IMS_CONFIDENTIALITY_KEY = 625;
        public static final int IMS_INTEGRITY_KEY = 626;
        public static final int IMS_SUPPORTED_FEATURES = 628;
        public static final int IMS_FEATURE_LIST_ID = 629;
        public static final int IMS_FEATURE_LIST = 630;
        public static final int IMS_SUPPORTED_APPLICATIONS = 631;
        public static final int IMS_ASSOCIATED_IDENTITIES = 632;
        public static final int IMS_ORIGINATING_REQUEST = 633;
    	public static final int IMS_SIP_DIGEST_AUTHENTICATE = 635;
        public static final int IMS_UAR_FLAGS                  = 637;
        
        // Sh AVPs
        public static final int IMS_PUBLIC_IDENTITY_SH = 601;
        public static final int IMS_SERVER_NAME_SH = 602;
        public static final int IMS_USER_IDENTITY = 700;
        public static final int IMS_MSISDN = 701;
        public static final int IMS_USER_DATA_SH = 702;
        public static final int IMS_DATA_REFERENCE = 703;
        public static final int IMS_SERVICE_INDICATION = 704;
        public static final int IMS_SUBSCRIBTION_REQ_TYPE = 705;
        public static final int IMS_REQUESTED_DOMAIN = 706;
        public static final int IMS_CURRENT_LOCATION = 707;
        public static final int IMS_IDENTITY_SET = 708;
        public static final int IMS_EXPIRY_TIME = 709;
        public static final int IMS_SEND_DATA_INDICATION = 710;
        public static final int IMS_DSAI_TAG = 711;
        
        // Zh AVPs
    	public static final int IMS_GBA_USER_SEC_SETTINGS = 400;
    	
    	public static final int AVP_Line_Identifier										= 500;
    	public static final int AVP_ETSI_SIP_Authenticate 							= 501; 
    	public static final int AVP_ETSI_SIP_Authorization 							= 502; 
    	public static final int AVP_ETSI_SIP_Authentication_Info 					= 503; 
    	public static final int AVP_ETSI_Digest_Realm 								= 504;  
    	public static final int AVP_ETSI_Digest_Nonce 								= 505;  
    	public static final int AVP_ETSI_Digest_Domain								= 506;  
    	public static final int AVP_ETSI_Digest_Opaque 							= 507;  
    	public static final int AVP_ETSI_Digest_Stale 								= 508;  
    	public static final int AVP_ETSI_Digest_Algorithm 							= 509;  
    	public static final int AVP_ETSI_Digest_QoP 									= 510;  
    	public static final int AVP_ETSI_Digest_HA1 									= 511;  
    	public static final int AVP_ETSI_Digest_Auth_Param 						= 512;  
    	public static final int AVP_ETSI_Digest_Username 							= 513;  
    	public static final int AVP_ETSI_Digest_URI 									= 514;  
    	public static final int AVP_ETSI_Digest_Response 							= 515;  
    	public static final int AVP_ETSI_Digest_CNonce 								= 516;  
    	public static final int AVP_ETSI_Digest_Nonce_Count 						= 517;  
    	public static final int AVP_ETSI_Digest_Method 								= 518;  
    	public static final int AVP_ETSI_Digest_Entity_Body_Hash 				= 519;  
    	public static final int AVP_ETSI_Digest_Nextnonce 						= 520;  
    	public static final int AVP_ETSI_Digest_Response_Auth					= 521;
    	
    	public static final int AVP_CableLabs_SIP_Digest_Authenticate 		= 228;
    	public static final int AVP_CableLabs_Digest_Realm 						= 209;
    	public static final int AVP_CableLabs_Digest_Domain 					= 206;
    	public static final int AVP_CableLabs_Digest_Algorithm 					= 204;
    	public static final int AVP_CableLabs_Digest_QoP 							= 208;
    	public static final int AVP_CableLabs_Digest_HA1 							= 207;
    	public static final int AVP_CableLabs_Digest_Auth_Param 				= 205;
    	
    }
    public class AVPValue{
    	// User-Authorization-Type
    	public static final int UAT_Registration = 0;
    	public static final int UAT_De_Registration = 1;
    	public static final int UAT_Registration_and_Capabilities = 2;
    	
    	// Authentication-Session-State
    	public static final int ASS_No_State_Maintained = 1;
    	
    	// UAR Flags
    	public static final int UAR_Flag_Emergency = 1;
    }
    
    public enum ExperimentalResultCode {
		/** 1001 to 1999	Informational			*/
		/** 2001 to 2999	Success					*/
		/**	2001 to 2020 Reserved for TS29.229		*/
    	RC_IMS_DIAMETER_FIRST_REGISTRATION("IMS_Diameter_First_Registration", 2001, Vendor.V3GPP),
    	RC_IMS_DIAMETER_SUBSEQUENT_REGISTRATION("IMS_Diameter_Subsequent_Registration", 2002, Vendor.V3GPP),
    	RC_IMS_DIAMETER_UNREGISTERED_SERVICE("IMS_Diameter_Unregistered_Service", 2003, Vendor.V3GPP),
    	RC_IMS_DIAMETER_SUCCESS_SERVER_NAME_NOT_STORED("IMS_Diameter_Success_Server_name_Not_Stored", 2004, Vendor.V3GPP),
    	RC_IMS_DIAMETER_SERVER_SELECTION("IMS_Diameter_Server_Selection", 2005, Vendor.V3GPP),
		
    	/**	2401 to 2420 Reserved for TS29.109		*/
		/** 4001 to 4999	Transient Failures		*/
		/**	4100 to 4120 Reserved for TS29.329		*/
    	RC_IMS_DIAMETER_USER_DATA_NOT_AVAILABLE("IMS_Diameter_user_Data_Not_Available", 4100, Vendor.V3GPP),
    	RC_IMS_DIAMETER_PRIOR_UPDATE_IN_PROGRESS("IMS_Diameter_Prior_Update_In_Progress", 4101, Vendor.V3GPP),
		
    	/**	41xx to 41yy Reserved for TS32.299		*/
		/** 5001 to 5999	Permanent Failures		*/
		/**	5001 to 5020 Reserved for TS29.229		*/
    	RC_IMS_DIAMETER_ERROR_USER_UNKNOWN("IMS_Diameter_Error_User_Unknown", 5001, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_IDENTITIES_DONT_MATCH("IMS_Diameter_Error_Identities_Dont_Match", 5002, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_IDENTITY_NOT_REGISTERED("IMS_Diameter_Error_Identity_Not_Registered", 5003, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_ROAMING_NOT_ALLOWED("IMS_Diameter_Error_Roaming_Not_Allowed", 5004, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_IDENTITY_ALREADY_REGISTERED("IMS_Diameter_Error_Identity_Already_Registered", 5005, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_AUTH_SCHEME_NOT_SUPPORTED("IMS_Diameter_Error_Auth_Scheme_Not_Supported", 5006, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_IN_ASSIGNMENT_TYPE("IMS_Diameter_Error_In_Assignment", 5007, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_TOO_MUCH_DATA("IMS_Diameter_Error_Too_Much_Data", 5008, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_NOT_SUPPORTED_USER_DATA("IMS_Diameter_Error_Not_Supported_User_Data", 5009, Vendor.V3GPP),
    	RC_IMS_DIAMETER_MISSING_USER_ID("IMS_Diameter_Missing_User_ID", 5010, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_FEATURE_UNSUPPORTED("IMS_Diameter_Error_Feature_Unsupported", 5011, Vendor.V3GPP),
    	
		/**	5021 to 5040 Reserved for TS32.299		*/
		/**	5041 to 5060 Reserved for TS29.234		*/
		/**	5061 to 5080 Reserved for TS29.209		*/
    	
    	RC_IMS_DIAMETER_ERROR_INVALID_SERVICE_INFORMATION("IMS_Diameter_Error_Invalid_Service_Information", 5061, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_FILTER_RESTRICTIONS("IMS_Diameter_Error_Filter_Restrictions",5062, Vendor.V3GPP),
		
    	/**	5100 to 5119 Reserved for TS29.329		*/
    	RC_IMS_DIAMETER_ERROR_USER_DATA_NOT_RECOGNIZED("IMS_Diameter_Error_User_Data_Not_Recognized",5100, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_OPERATION_NOT_ALLOWED("IMS_Diameter_Error_Operation_Not_Allowed",5101, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_READ("IMS_Diameter_Error_User_Data_Cannot_Be_Read",5102, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_MODIFIED("IMS_Diameter_Error_User_Data_Cannot_Be_Modified",5103, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_USER_DATA_CANNOT_BE_NOTIFIED("IMS_Diameter_Error_User_Data_Cannot_Be_Notified",5104, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_TRANSPARENT_DATA_OUT_OF_SYNC("IMS_Diameter_Error_Transparent_Data_Out_Of_Sync",5105, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_SUBS_DATA_ABSENT("IMS_Diameter_Error_Subs_Data_Absent",5106, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_NO_SUBSCRIPTION_TO_DATA("IMS_Diameter_Error_No_Subscription_To_Data",5107, Vendor.V3GPP),
    	RC_IMS_DIAMETER_ERROR_DSAI_NOT_AVAILABLE("IMS_Diameter_Error_DSAI_Not_Available",5108, Vendor.V3GPP);
		
    	/** 5400 to 5419 Reserved for TS29.109	*/
    	
    	ExperimentalResultCode(String name, int code, int vendor){
    		this.name = name;
    		this.code = code;
    		this.vendor = vendor;
    	}

    	private String name;
    	private int code;
    	private int vendor;
    	
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public int getVendor() {
			return vendor;
		}
    	    	
    }
    
    public enum ResultCode{
    	DIAMETER_SUCCESS ("Diameter_Success", 2001), 							
    	DIAMETER_REALM_NOT_SERVED("Diameter_Realm_Not_Served", 3003), 			
    	DIAMETER_AUTHENTICATION_REJECTED("Diameter_Authentication_Rejected", 4001),
    	DIAMETER_AUTHORIZATION_REJECTED("Diameter_Authorization_Rejected", 5003),
    	DIAMETER_MISSING_AVP("Diameter_Missing_AVP", 5005),
    	DIAMETER_AVP_NOT_ALLOWED("Diameter_AVP_Not_Allowed", 5008),
    	DIAMETER_AVP_OCCURS_TOO_MANY_TIMES("Diameter_AVP_Occurs_Too_Many_Times", 5009),
    	DIAMETER_NO_COMMON_APPLICATION("Diameter_No_Common_Application", 5010),
    	DIAMETER_UNABLE_TO_COMPLY("Diameter_Unable_To_Comply", 5012),
    	DIAMETER_NO_COMMON_SECURITY("Diameter_No_Common_Security", 5017),
    	DIAMETER_INVALID_AVP_VALUE("Diameter_invalid_AVP_Value", 5040),

    	/** RFC 4006 */
    	DIAMETER_USER_UNKNOWN("Diameter_User_Unknown", 5030);
    	
    	ResultCode(String name, int code){
    		this.name = name;
    		this.code = code;
    	}

    	private String name;
    	private int code;
    	
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}

    }
   
}
