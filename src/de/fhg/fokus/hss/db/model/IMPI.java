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

package de.fhg.fokus.hss.db.model;

import java.io.Serializable;

import de.fhg.fokus.hss.cx.CxConstants;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class IMPI implements Serializable{
	private static final long serialVersionUID=1L;
	// table fields
	private int id;
	private String identity;
	private byte[] k;
	private int auth_scheme;
	private int default_auth_scheme;
	
	private byte[] amf;
	private byte[] op;
	private String sqn;
	private String ip;
	private Integer id_imsu;
	private String line_identifier;
	private int zh_uicc_type;
	private int zh_key_life_time;
	private int zh_default_auth_scheme;
	
	public IMPI(){}

	public static int generateAuthScheme(boolean akav1, boolean akav2, boolean md5, boolean digest, 
			boolean sip_digest, boolean http_digest, boolean early, boolean nass_bundle, boolean all){
		
		if (all){
			return 255;
		}
		else{
			int result = 0;

			if (akav1){
				result |= CxConstants.Auth_Scheme_AKAv1; 
			}
			if (akav2){
				result |= CxConstants.Auth_Scheme_AKAv2; 
			}
			if (md5){
				result |= CxConstants.Auth_Scheme_MD5; 
			}
			if (digest){
				result |= CxConstants.Auth_Scheme_Digest; 
			}
			if (sip_digest){
				result |= CxConstants.Auth_Scheme_SIP_Digest; 
			}
			if (http_digest){
				result |= CxConstants.Auth_Scheme_HTTP_Digest_MD5; 
			}
			if (early){
				result |= CxConstants.Auth_Scheme_Early; 
			}
			if (nass_bundle){
				result |= CxConstants.Auth_Scheme_NASS_Bundled; 
			}
			
			return result;
		}
	}
	
	
	// getters and setters
	public int getAuth_scheme() {
		return auth_scheme;
	}


	public void setAuth_scheme(int auth_scheme) {
		this.auth_scheme = auth_scheme;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getK() {
		return k;
	}

	public void setK(byte[] k) {
		this.k = k;
	}
	public byte[] getAmf() {
		return amf;
	}

	public void setAmf(byte[] amf) {
		this.amf = amf;
	}

	public byte[] getOp() {
		return op;
	}

	public void setOp(byte[] op) {
		this.op = op;
	}

	public String getSqn() {
		return sqn;
	}

	public void setSqn(String sqn) {
		this.sqn = sqn;
	}

	public int getId_imsu() {
		return id_imsu;
	}

	public void setId_imsu(Integer id_imsu) {
		if (id_imsu != null)
			this.id_imsu = id_imsu;
		else 
			this.id_imsu = -1;
	}
	
	public int getDefault_auth_scheme() {
		return default_auth_scheme;
	}

	public void setDefault_auth_scheme(int default_auth_scheme) {
		this.default_auth_scheme = default_auth_scheme;
	}

	public String getLine_identifier() {
		return line_identifier;
	}

	public void setLine_identifier(String line_identifier) {
		this.line_identifier = line_identifier;
	}


	public int getZh_key_life_time() {
		return zh_key_life_time;
	}

	public void setZh_key_life_time(int zh_key_life_time) {
		this.zh_key_life_time = zh_key_life_time;
	}

	public int getZh_uicc_type() {
		return zh_uicc_type;
	}

	public void setZh_uicc_type(int zh_uicc_type) {
		this.zh_uicc_type = zh_uicc_type;
	}

	public int getZh_default_auth_scheme() {
		return zh_default_auth_scheme;
	}

	public void setZh_default_auth_scheme(int zh_default_auth_scheme) {
		this.zh_default_auth_scheme = zh_default_auth_scheme;
	}
	
	
}
