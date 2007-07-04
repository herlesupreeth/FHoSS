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

package de.fhg.fokus.hss.zh;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class ZhConstants{
	
	// GAA Service Types
	public static int GAA_Service_Type_Unspecific_Service = 0;
	public static int GAA_Service_Type_PKI_Portal = 1;
	public static int GAA_Service_Type_Authentication_Proxy = 2;
	public static int GAA_Service_Type_Presence = 3;
	public static int GAA_Service_Type_MBMS = 4;
	public static int GAA_Service_Type_Liberty_Alliance_Project = 5;
	public static String GAA_Service_Type_Unspecific_Service_Name = "Unspecific Service";
	public static String GAA_Service_Type_PKI_Portal_Name = "PKI-Portal";
	public static String GAA_Service_Type_Authentication_Proxy_Name = "Authentication Proxy";
	public static String GAA_Service_Type_Presence_Name = "Presence";
	public static String GAA_Service_Type_MBMS_Name = "MBMS";
	public static String GAA_Service_Type_Liberty_Alliance_Project_Name = "Liberty Alliance Project";
 
	// Flags for PKI Portal Service Type
	public static int GAA_Authorization_Authentication_Allowed = 1;
	public static int GAA_Authorization_Non_Repudiation_Allowed = 2;
	public static String GAA_Authorization_Authentication_Allowed_Name = "Authentication Allowed";
	public static String GAA_Authorization_Non_Repudiation_Allowed_Name = "Non-Repudiation Allowed";
 
	// UICC Types
	public static int UICC_Type_Basic_GBA = 0;
	public static int UICC_Type_GBA_U = 1;
	public static String UICC_Type_Basic_GBA_Name = "GBA";
	public static String UICC_Type_GBA_U_Name = "GBA_U";
 
	// default Key Life Time
	public static int Default_Key_Life_Time = 3600;

}
