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

package de.fhg.fokus.hss.sh;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class ShConstants {
	
	// Data-Reference Constants
	public static final int Data_Ref_Repository_Data = 0;
	public static final int Data_Ref_IMS_Public_Identity = 10;
	public static final int Data_Ref_IMS_User_State = 11;
	public static final int Data_Ref_SCSCF_Name = 12;
	public static final int Data_Ref_iFC = 13;
	public static final int Data_Ref_Location_Info = 14;
	public static final int Data_Ref_User_State = 15;
	public static final int Data_Ref_Charging_Info = 16;
	public static final int Data_Ref_MSISDN = 17;
	public static final int Data_Ref_PSI_Activation = 18;
	public static final int Data_Ref_DSAI = 19;
	public static final int Data_Ref_Aliases_Repository_Data = 20;
	
	
	// Send-Data-Indication
	public static final int User_Data_Not_Requested = 0;
	public static final int User_Data_Requested = 1;
	
	// Subs-Req-Type
	public static final int Subs_Req_Type_Subscribe = 0;
	public static final int Subs_Req_Type_UnSubscribe = 1;
	
	// Identity-Set
	public static final int Identity_Set_All_Identities = 0;
	public static final int Identity_Set_Registered_Identities = 1;
	public static final int Identity_Set_Implicit_Identities = 2;
	public static final int Identity_Set_Alias_Identities = 3;
	
	
	
}
