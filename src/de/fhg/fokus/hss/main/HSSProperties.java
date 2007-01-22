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
package de.fhg.fokus.hss.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * It contains some variables which describe the HSS properties.Further it reads
 * the values for those variables from a property file.
 * @author Andre Charton (dev -at- open-ims dot org)
 */

public class HSSProperties {
	
	private static final Logger LOGGER = Logger.getLogger(HSSProperties.class);
	public static String TOMCAT_HOST;
	public static String TOMCAT_PORT;
	public static String OPERATOR_ID;
	public static String AMF_ID;
	public static String PSI_DEFAULT_IMPI;
	public static String PSI_DEFAULT_PRI_COLL_CHRG_FN;

	public static boolean USE_AK = false;
	public static int IND_LEN = 5;
	public static int delta = 268435456;
	public static int L = 32;
	
	private static String fileName = "hss.properties";
	
	static {
		Properties props;
		
		try{
			props = new Properties();
			props.load(new FileInputStream(fileName));
			
			TOMCAT_HOST = props.getProperty("host");
			TOMCAT_PORT = props.getProperty("port");
			OPERATOR_ID = props.getProperty("operatorId");
			AMF_ID = props.getProperty("amfId");
			PSI_DEFAULT_IMPI = props.getProperty("psi.defaultImpi");
			PSI_DEFAULT_PRI_COLL_CHRG_FN = props.getProperty("psi.defaultPriChrgCollFN");
			USE_AK = Boolean.valueOf(props.getProperty("USE_AK")).booleanValue();
			IND_LEN = Integer.parseInt(props.getProperty("IND_LEN"));
			delta = Integer.parseInt(props.getProperty("delta"));
			L = Integer.parseInt(props.getProperty("L"));
		}
		catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException !", e);
			LOGGER.error("Failed to load configuration from \"" + fileName + "\" file!");
			System.exit(0);
		}
		catch (IOException e) {
			LOGGER.error("IOException !", e);
			LOGGER.error("Failed to load configuration from \"" + fileName + "\" file!");
			System.exit(0);
		}
	}
}
