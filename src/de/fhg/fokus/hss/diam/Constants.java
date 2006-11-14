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
 *//*
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



/**
 * It contains constant values like AVPCodes-Codes, Command code values.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class Constants
{
    /** 
     *  an abstract inner class containing the constants for application
     */
    public abstract class Application {
			/** constant for cx application */
			public static final int CX = 16777216;
			/** constant for sh application */
			public static final int SH = 16777217;
			/** constant for zh application */
			public static final int ZH = 16777221;
    }
 
    /** 
     *  an abstract inner class containing the constants for vendor
     */    
    public abstract class Vendor
    {
        /** constant for diam */
        public static final int DIAM = 0;
        /** constant for v3gpp */
        public static final int V3GPP = 10415;
    }

    /** 
     *  an abstract inner class containing the constants for ims 
     *  specific requests and responses
     */  
    public abstract class COMMAND
    {
    	/** constant for Capablities Exchange Request */
        public static final int CER = 257;
        /** constant for Capablities Exchange Answer */
        public static final int CEA = 257;
        /** constant for Device Watchdog request */
        public static final int DWR = 280;
        /** constant for Device Watchdog Answer */
        public static final int DWA = 280;
        /** constant for User Authorization Request */
        public static final int UAR = 300;
        /** constant for User Authorization Answer */
        public static final int UAA = 300;
        /** constant for Server Assignment Request */
        public static final int SAR = 301;
        /** constant for Server Assignment Answer */
        public static final int SAA = 301;
        /** constant for Location Information Request */
        public static final int LIR = 302;
        /** constant for Location Information Answer */
        public static final int LIA = 302;
        /** constant for Multimedia Authentication Request */
        public static final int MAR = 303;
        /** constant for Multimedia Authentication Answer */
        public static final int MAA = 303;
        /** constant for Registration Termination Request */
        public static final int RTR = 304;
        /** constant for Registration Termination Answer */
        public static final int RTA = 304;
        /** constant for Push Profile Request */
        public static final int PPR = 305;
        /** constant for Push Profile Answer */
        public static final int PPA = 305;
        /** constant for User Data Request */
        public static final int UDR = 306;
        /** constant for User Data Answer */
        public static final int UDA = 306;
        /** constant for Profile Update Request */
        public static final int PUR = 307;
        /** constant for Profile Update Answer */
        public static final int PUA = 307;
        /** constant for Subscriber Notification Request */
        public static final int SNR = 308;
        /** constant for Subscriber Notification Answer */
        public static final int SNA = 308;
        /** constant for Push Notification Request */
        public static final int PNR = 309;
        /** constant for Push Notification Answer */
        public static final int PNA = 309;
        /** constant for Multimedia Authentication Request for Zh-Interface */
		public static final int MARzh = 903;
    }
}
