/*
 * $Id$
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

import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.hss.diam.Constants.Vendor;


/**
 * This class contains integer values for AVP codes which are required
 * while building diameter requests and responses. Please refer to 3GPP TS 29.229
 * for further details.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class AVPCodes
{
    public static final int _PRIVATE_USER_IDENTITY = 1;
    public static final int _FRAMED_IP_ADDRESS = 8;
    public static final int _ORIGIN_HOST = 264;
    public static final int _VENDOR_ID = 266;
    public static final int _RESULT_CODE = 268;
    public static final int _DESTINATION_HOST = 293;
    public static final int _EXPERIMENTAL_RESULT_CODE_AVP = 297;
    public static final int _EXPERIMENTAL_RESULT_CODE = 298;
    
    
    public static final int _VISITED_NETWORK_IDENTIFIER = 600;
    public static final int _CX_PUBLIC_IDENTITY = 601;
    public static final int _CX_SERVER_NAME = 602;
    public static final int _CX_SERVER_CAPABILITIES = 603;
    public static final int _MANDATORY_CAPABILITY = 604;
    public static final int _OPTIONAL_CAPABILITY = 605;
    public static final int _CX_USER_DATA = 606;
    public static final int _SIP_NUMBER_AUTH_ITEMS = 607;
    public static final int _SIP_AUTHENTICATION_SCHEME = 608;
    public static final int _SIP_AUTHENTICATE = 609;
    public static final int _SIP_AUTHORIZATION = 610;
    public static final int _SIP_AUTHENTICATION_CONTEXT = 611;
    public static final int _SIP_AUTH_DATA_ITEM = 612;
    public static final int _SIP_ITEM_NUMBER = 613;
    public static final int _SERVER_ASSIGNMENT_TYPE = 614;
    public static final int _DEREGISTRATION_REASON = 615;
    public static final int _REASON_CODE = 616;
    public static final int _REASON_INFO = 617;
    public static final int _CHARGING_INFO = 618;
    public static final int _PRI_EVENT_CHARGING_FN_NAME = 619;
    public static final int _SEC_EVENT_CHARGING_FN_NAME = 620;
    public static final int _PRI_CHRG_COLL_FN_NAME = 621;
    public static final int _SEC_CHRG_COLL_FN_NAME = 622;
    public static final int _USER_AUTHORIZATION_TYPE = 623;
    public static final int _USER_DATA_ALREADY_AVAILABLE = 624;
    public static final int _CONFIDENTIALITY_KEY = 625;
    public static final int _INTEGRITY_KEY = 626;
    
    public static final int _SH_USER_IDENTITY = 700;
    public static final int _SH_PUBLIC_IDENTITY = 601;
    public static final int _SH_SERVER_NAME = 602;
    public static final int _SH_USER_DATA = 702;
    public static final int _SH_DATA_REFERENCE = 703;
    public static final int _SH_SERVICE_INDICATION = 704;
    public static final int _SH_SUBSCRIBTION_REQ_TYPE = 705;
    
	public static final int _ZH_GUSS = 400;

    /**
     * The AVPCodes Class generator
     * @param code the code
     * @return the attribute value pair
     */
    public static AVP getAVP(int code)
    {
        switch (code)
        {
        case _PRIVATE_USER_IDENTITY:
            return new AVP(_PRIVATE_USER_IDENTITY, true, Vendor.DIAM);

        case _RESULT_CODE:
            return new AVP(_RESULT_CODE, true, Vendor.DIAM);

        case _EXPERIMENTAL_RESULT_CODE_AVP:
            return new AVP(_EXPERIMENTAL_RESULT_CODE_AVP, true, Vendor.DIAM);

        case _EXPERIMENTAL_RESULT_CODE:
            return new AVP(_EXPERIMENTAL_RESULT_CODE, true, Vendor.DIAM);

        case _VENDOR_ID:
            return new AVP(_VENDOR_ID, true, Vendor.DIAM);
        
        case _DESTINATION_HOST:
          return new AVP(_VENDOR_ID, true, Vendor.DIAM);
          
        default:
        	return new AVP(code, true, Vendor.V3GPP);
        }
    }
}
