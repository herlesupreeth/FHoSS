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

package de.fhg.fokus.hss.sh.data;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class ShDataTags {
	// constants related to the xml document format; more details are provided in 3GPP TS 29.328 
	
	public static final String ShData = "Sh-Data";
	public static final String ShData_s = "<Sh-Data>";
	public static final String ShData_e = "</Sh-Data>";

	public static final String PublicIdentifiers = "PublicIdentifiers";
	public static final String PublicIdentifiers_s = "<PublicIdentifiers>";
	public static final String PublicIdentifiers_e = "</PublicIdentifiers>";

	public static final String RepositoryData = "RepositoryData";
	public static final String RepositoryData_s = "<RepositoryData>";
	public static final String RepositoryData_e = "</RepositoryData>";
	public static final String Sh_IMS_Data = "Sh-IMS-Data";
	public static final String Sh_IMS_Data_s = "<Sh-IMS-Data>";
	public static final String Sh_IMS_Data_e = "</Sh-IMS-Data>";
	public static final String CSLocationInformation = "CSLocationInformation";
	public static final String CSLocationInformation_s = "<CSLocationInformation>";
	public static final String CSLocationInformation_e = "</CSLocationInformation>";
	public static final String PSLocationInformation = "PSLocationInformation";
	public static final String PSLocationInformation_s = "<PSLocationInformation>";
	public static final String PSLocationInformation_e = "</PSLocationInformation>";
	public static final String CSUserState = "CSUserState";
	public static final String CSUserState_s = "<CSUserState>";
	public static final String CSUserState_e = "</CSUserState>";
	public static final String PSUserState = "PSUserState";
	public static final String PSUserState_s = "<PSUserState>";
	public static final String PSUserState_e = "</PSUserState>";
	public static final String ShDataExtension = "Sh-Data-Extension";
	public static final String ShDataExtension_s = "<Sh-Data-Extension>";
	public static final String ShDataExtension_e = "</Sh-Data-Extension>";

	// ApplicationServer
	public static final String ApplicationServer = "ApplicationServer"; 
	public static final String ApplicationServer_s= "<ApplicationServer>";
	public static final String ApplicationServer_e = "</ApplicationServer>";
	public static final String ServerName = "ServerName";
	public static final String ServerName_s = "<ServerName>";
	public static final String ServerName_e = "</ServerName>";
	public static final String DefaultHandling = "DefaultHandling";
	public static final String DefaultHandling_s = "<DefaultHandling>";
	public static final String Defaulthandling_e = "</DefaultHandling>";
	public static final String ServiceInfo = "ServiceInfo";
	public static final String ServiceInfo_s = "<ServiceInfo>";
	public static final String ServiceInfo_e = "</ServiceInfo>";
	
	// ChargingInformation
	public static final String ChargingInformation = "ChargingInformation";
	public static final String ChargingInformation_s = "<ChargingInformation>";
	public static final String ChargingInformation_e = "</ChargingInformation>";
	public static final String PrimaryEventChargingFunctionName = "PrimaryEventChargingFunctionName";
	public static final String PrimaryEventChargingFunctionName_s = "<PrimaryEventChargingFunctionName>";
	public static final String PrimaryEventChargingFunctionName_e = "</PrimaryEventChargingFunctionName>";
	public static final String SecondaryEventChargingFunctionName = "SecondaryEventChargingFunctionName";
	public static final String SecondaryEventChargingFunctionName_s = "<SecondaryEventChargingFunctionName>";
	public static final String SecondaryEventChargingFunctionName_e = "</SecondaryEventChargingFunctionName>";
	public static final String PrimaryChargingCollectionFunctionName = "PrimaryChargingCollectionFunctionName";
	public static final String PrimaryChargingCollectionFunctionName_s = "<PrimaryChargingCollectionFunctionName>";
	public static final String PrimaryChargingCollectionFunctionName_e = "</PrimaryChargingCollectionFunctionName>";
	public static final String SecondaryChargingCollectionFunctionName = "SecondaryChargingCollectionFunctionName";
	public static final String SecondaryChargingCollectionFunctionName_s = "<SecondaryChargingCollectionFunctionName>";
	public static final String SecondaryChargingCollectionFunctionName_e = "</SecondaryChargingCollectionFunctionName>";
	
	// SPT
	public static final String SPT = "SPT";
	public static final String SPT_s = "<SPT>";
	public static final String SPT_e = "</SPT>";
	public static final String ConditionNegated = "ConditionNegated";
	public static final String ConditionNegated_s = "<ConditionNegated>";
	public static final String ConditionNegated_e = "</ConditionNegated>";
	public static final String Group = "Group";
	public static final String Group_s = "<Group>";
	public static final String Group_e = "</Group>";
	public static final String RequestURI = "RequestURI";
	public static final String RequestURI_s = "<RequestURI>";
	public static final String RequestURI_e = "</RequestURI>";
	public static final String Method = "Method";
	public static final String Method_s = "<Method>";
	public static final String Method_e = "</Method>";
	public static final String SIPHeader = "SIPHeader";
	public static final String SIPHeader_s = "<SIPHeader>";
	public static final String SIPHeader_e = "</SIPHeader>";
	public static final String Header = "Header";
	public static final String Header_s = "<Header>";
	public static final String Header_e = "</Header>";
	public static final String Content = "Content";
	public static final String Content_s = "<Content>";
	public static final String Content_e = "</Content>";
	public static final String SessionCase = "SessionCase";
	public static final String SessionCase_s = "<SessionCase>";
	public static final String SessionCase_e = "</SessionCase>";
	public static final String SessionDescription = "SessionDescription";
	public static final String SessionDescription_s = "<SessionDescription>";
	public static final String SessionDescription_e = "</SessionDescription>";
	public static final String SDPLine = "Line";
	public static final String SDPLine_s = "<Line>";
	public static final String SDPLine_e = "</Line>";
	public static final String RegistrationType = "RegistrationType";
	public static final String RegistrationType_s = "<RegistrationType>";
	public static final String RegistrationType_e = "</RegistrationType>";
	
	// TriggerPoint
	public static final String TriggerPoint = "TriggerPoint";
	public static final String TriggerPoint_s = "<TriggerPoint>";
	public static final String TriggerPoint_e = "</TriggerPoint>";
	public static final String ConditionTypeCNF = "ConditionTypeCNF";
	public static final String ConditionTypeCNF_s = "<ConditionTypeCNF>";
	public static final String ConditionTypeCNF_e = "</ConditionTypeCNF>";
	
	// InitialFilterCriteria
	public static final String InitialFilterCriteria = "InitialFilterCriteria";
	public static final String InitialFilterCriteria_s = "<InitialFilterCriteria>";
	public static final String InitialFilterCriteria_e = "</InitialFilterCriteria>";
	public static final String Priority = "Priority";
	public static final String Priority_s = "<Priority>";
	public static final String Priority_e = "</Priority>";
	public static final String ProfilePartIndicator = "ProfilePartIndicator";
	public static final String ProfilePartIndicator_s = "<ProfilePartIndicator>";
	public static final String ProfilePartIndicator_e = "</ProfilePartIndicator>";
	
	// Sh-Data-Extension
	public static final String Sh_Data_Extension = "Sh-Data-Extension";
	public static final String Sh_Data_Extension_s = "<Sh-Data-Extension>";
	public static final String Sh_Data_Extension_e = "</Sh-Data-Extension>";
	public static final String RegisteredIdentities = "RegisteredIdentities";
	public static final String RegisteredIdentities_s = "<RegisteredIdentities>";
	public static final String RegisteredIdentities_e = "</RegisteredIdentities>";
	public static final String ImplicitIdentities = "ImplicitIdentities";
	public static final String ImplicitIdentities_s = "<ImplicitIdentities>";
	public static final String ImplicitIdentities_e = "</ImplicitIdentities>";
	public static final String AllIdentities = "AllIdentities";
	public static final String AllIdentities_s = "<AllIdentities>";
	public static final String AllIdentities_e = "</AllIdentities>";
	public static final String AliasIdentities = "AliasIdentities";
	public static final String AliasIdentities_s = "<AliasIdentities>";
	public static final String AliasIdentities_e = "</AliasIdentities>";
	public static final String AliasesRepositoryData = "AliasesRepositoryData";
	public static final String AliasesRepositoryData_s = "<AliasesRepositoryData>";
	public static final String AliasesRepositoryData_e = "</AliasesRepositoryData>";
	
	// RepositoryData
	public static final String ServiceIndication = "ServiceIndication";
	public static final String ServiceIndication_s = "<ServiceIndication>";
	public static final String ServiceIndication_e = "</ServiceIndication>";
	public static final String SequenceNumber = "SequenceNumber";
	public static final String SequenceNumber_s = "<SequenceNumber>";
	public static final String SequenceNumber_e = "</SequenceNumber>";
	public static final String ServiceData = "ServiceData";
	public static final String ServiceData_s = "<ServiceData>";
	public static final String ServiceData_e = "</ServiceData>";
	
	// CSLocationInformation, additional fields
	public static final String LocationNumber = "LocationNumber";
	public static final String LocationNumber_s = "<LocationNumber>";
	public static final String LocationNumber_e = "</LocationNumber>";
	public static final String CellGlobalId = "CellGlobalId";
	public static final String CellGlobalId_s = "<CellGlobalId>";
	public static final String CellGlobalId_e = "</CellGlobalId>";
	public static final String ServiceAreaId = "ServiceAreaId";
	public static final String ServiceAreaId_s = "<ServiceAreaId>";
	public static final String ServiceAreaId_e = "</ServiceAreaId>";
	public static final String LocationAreaId = "LocationAreaId";
	public static final String LocationAreaId_s = "<LocationAreaId>";
	public static final String LocationAreaId_e = "</LocationAreaId>";
	public static final String GeographicalInformation = "GeographicalInformation";
	public static final String GeographicalInformation_s = "<GeographicalInformation>";
	public static final String GeographicalInformation_e = "</GeographicalInformation>";
	public static final String GeodeticInformation = "GeodeticInformation";
	public static final String GeodeticInformation_s = "<GeodeticInformation>";
	public static final String GeodeticInformation_e = "</GeodeticInformation>";
	public static final String VLRNumber = "VLRNumber";
	public static final String VLRNumber_s = "<VLRNumber>";
	public static final String VLRNumber_e = "</VLRNumber>";
	public static final String MSCNumber = "MSCNumber";
	public static final String MSCNumber_s = "<MSCNumber>";
	public static final String MSCNumber_e = "</MSCNumber>";
	public static final String CurrentLocationRetrieved = "CurrentLocationRetrieved";
	public static final String CurrentLocationRetrieved_s= "<CurrentLocationRetrieved>";
	public static final String CurrentLocationRetrieved_e = "</CurrentLocationRetrieved>";
	public static final String AgeOfLocationInformation = "AgeOfLocationInformation";
	public static final String AgeOfLocationInformation_s = "<AgeOfLocationInformation>";
	public static final String AgeOfLocationInformation_e= "</AgeOfLocationInformation>";
	
	// PSLocationInformation, additional fields
	public static final String RoutingAreaId = "RoutingAreaId";
	public static final String RoutingAreaId_s = "<RoutingAreaId>";
	public static final String RoutingAreaId_e = "</RoutingAreaId>";
	public static final String SGSNNumber = "SGSNNumber";
	public static final String SGSNNumber_s = "<SGSNNumber>";
	public static final String SGSNNumber_e= "</SGSNNumber>";
	
	// ShIMSData
	public static final String SCSCFName = "SCSCFName";
	public static final String SCSCFName_s = "<SCSCFName>";
	public static final String SCSCFName_e = "</SCSCFName>";
	public static final String IFCs = "IFCs";
	public static final String IFCs_s = "<IFCs>";
	public static final String IFCs_e = "</IFCs>";
	public static final String IMSUserState = "IMSUserState";
	public static final String IMSUserState_s = "<IMSUserState>";
	public static final String IMSUserState_e = "</IMSUserState>";

	// DSAI

	public static final String Extension = "Extension";
	public static final String Extension_s = "<Extension>";
	public static final String Extension_e = "</Extension>";

	public static final String DSAI = "DSAI";
	public static final String DSAI_s = "<DSAI>";
	public static final String DSAI_e = "</DSAI>";

	public static final String DSAI_Tag = "DSAI-Tag";
	public static final String DSAI_Tag_s = "<DSAI-Tag>";
	public static final String DSAI_Tag_e = "</DSAI-Tag>";

	public static final String DSAI_Value = "DSAI-Value";
	public static final String DSAI_Value_s = "<DSAI-Value>";
	public static final String DSAI_Value_e = "</DSAI-Value>";

	// PSIActivation
	public static final String PSIActivation = "PSIActivation";
	public static final String PSIActivation_s = "<PSIActivation>";
	public static final String PSIActivation_e = "</PSIActivation>";
	
	// other tags
	public static final String IMSPublicIdentity = "IMSPublicIdentity";
	public static final String IMSPublicIdentity_s = "<IMSPublicIdentity>";
	public static final String IMSPublicIdentity_e = "</IMSPublicIdentity>";
	
	public static final String MSISDN = "MSISDN";
	public static final String MSISDN_s = "<MSISDN>";
	public static final String MSISDN_e = "</MSISDN>";
	
	public static final String IdentityType = "IdentityType";
	public static final String IdentityType_s = "<IdentityType>";
	public static final String IdentityType_e = "</IdentityType>";
	
	public static final String WildcardedPSI = "WildcardedPSI";
	public static final String WildcardedPSI_s = "<WildcardedPSI>";
	public static final String WildcardedPSI_e = "</WildcardedPSI>";
}
