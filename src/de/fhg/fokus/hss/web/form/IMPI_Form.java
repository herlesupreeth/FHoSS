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

package de.fhg.fokus.hss.web.form;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPI_IMPU;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPI_IMPU_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.main.HSSProperties;
import de.fhg.fokus.hss.web.util.WebConstants;
/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */


public class IMPI_Form extends ActionForm implements Serializable{
	private static Logger logger = Logger.getLogger(IMPI_Form.class);
	private static final long serialVersionUID=1L;
	
	public static final String DEFAULT_OP = HSSProperties.OPERATOR_ID;  
	public static final String DEFAULT_AMF = HSSProperties.AMF_ID;
	public static final String DEFAULT_SQN = "000000000000";
	private int id;
	private int id_imsu;
	private String identity;
	private String secretKey;
	private boolean aka1;
	private boolean aka2;
	private boolean md5;
	private boolean digest;
	private boolean sip_digest;
	private boolean http_digest;
	private boolean early;
	private boolean nass_bundle; 
	private boolean all;
	private int default_auth_scheme;
	private String amf;
	private String op;
	private String sqn;
	private String ip;
	private String line_identifier;
	
	// RTR & PPR variables
	private String[] rtr_identities;
	private List rtr_select_identities;
	private int rtr_reason;
	private List select_rtr_reason;
	private String reasonInfo;
	private List select_rtr_apply_for;
	private int rtr_apply_for;
	private int ppr_apply_for;
	private List select_ppr_apply_for;
	
	//private List select_imsu;
	private String nextAction;
	private String impu_identity;
	private String imsu_name;
	//private List associated_impu_set;
	private int associated_ID;
	private List select_auth_scheme;
	private int already_assigned_imsu_id;
	
    public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.id_imsu = -1;
    	this.identity = null;
    	this.secretKey = null;
    	this.ip = null;
    	this.op = IMPI_Form.DEFAULT_OP;
    	this.amf = IMPI_Form.DEFAULT_AMF;
    	this.sqn = IMPI_Form.DEFAULT_SQN;
    	this.aka1 = false;
    	this.aka2 = false;
    	this.md5 = false;
    	this.digest = false;
    	this.sip_digest = false;
    	this.http_digest = false;
    	this.early = false;
    	this.nass_bundle = false;
    	this.all = false;
    	this.default_auth_scheme = CxConstants.Auth_Scheme_AKAv1;
    	this.line_identifier = null;
    	
    	this.impu_identity = null;
    	this.imsu_name = null;
    	
    	//this.select_imsu = null;
    	//this.associated_impu_set = null;
    	this.associated_ID = -1;
    	this.select_auth_scheme = null;
    	this.already_assigned_imsu_id = -1;
    	
    	// RTR & PPR variables
    	this.rtr_reason = -1;
    	this.select_rtr_reason = WebConstants.select_rtr_reason;
    	this.reasonInfo = null;
    	this.rtr_apply_for = 0;
    	this.select_rtr_apply_for = WebConstants.select_rtr_apply_for;
    	this.ppr_apply_for = 0;
    	this.select_ppr_apply_for = WebConstants.select_ppr_apply_for;
    	this.rtr_select_identities = null;
    }
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        boolean dbException = false;
        try{
        	Session session = HibernateUtil.getCurrentSession();
        	HibernateUtil.beginTransaction();

            if (nextAction.equals("save")){
            	if (identity == null || identity.equals("")){
            		actionErrors.add("identity", new ActionMessage("impi_form.error.identity"));
            	}
            
            	int auth_scheme = IMPI.generateAuthScheme(aka1, aka2, md5, digest, sip_digest, http_digest, early, nass_bundle, all);	
            	if ((auth_scheme & default_auth_scheme) == 0){
            		actionErrors.add("", new ActionMessage(""));
            	}
            
            	if (!(this.aka1 || this.aka2 || this.md5 || this.digest || this.sip_digest || this.http_digest || this.early || this.nass_bundle || this.all )){
            		actionErrors.add("auth_scheme", new ActionMessage("impi_form.error.auth_scheme"));
            	}
            	if (secretKey == null || secretKey.equals("")){
            		actionErrors.add("secret_key", new ActionMessage("impi_form.error.secret_key"));
            	}
            	if (amf == null || amf.equals("") || amf.length() != 4){
            		actionErrors.add("secret_key", new ActionMessage("impi_form.error.amf"));
            	}
            	if (op == null || op.equals("") || op.length() != 32){
            		actionErrors.add("secret_key", new ActionMessage("impi_form.error.op"));
            	}
            	if (sqn == null || sqn.equals("") || sqn.length() != 12){
            		actionErrors.add("secret_key", new ActionMessage("impi_form.error.sqn"));
            	}
        	
            	IMPI impi = IMPI_DAO.get_by_Identity(session, identity);
            	if (impi != null && impi.getId() != id){
            		actionErrors.add("impi.error.duplicate_identity", new ActionMessage("impi.error.duplicate_identity"));	
            	}
            }
            else if (nextAction.equals("add_impu")){
            	IMPU impu = IMPU_DAO.get_by_Identity(session, impu_identity);
            	if (impu != null){
            		IMPI_IMPU impi_impu = IMPI_IMPU_DAO.get_by_IMPI_and_IMPU_ID(session, id, impu.getId());
            		if (impi_impu != null){
            			actionErrors.add("impi.error.association_exists", new ActionMessage("impi.error.association_exists"));	
            		}
            	}
            }
            
            else if (nextAction.equals("rtr_all") || nextAction.equals("rtr_selected")){
            	if (rtr_reason == -1){
            		actionErrors.add("impi.error.rtr_reason_missing", new ActionMessage("impi.error.rtr_reason_missing"));
            	}
            }
        	
        }
		catch(DatabaseException e){
			logger.error("Database Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		
		catch (HibernateException e){
			logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
			e.printStackTrace();
			dbException = true;
		}
		finally{
			if (!dbException){
				HibernateUtil.commitTransaction();
			}
			HibernateUtil.closeSession();
		}        	
        return actionErrors;
    }

	public boolean isAka1() {
		return aka1;
	}


	public void setAka1(boolean aka1) {
		this.aka1 = aka1;
	}


	public boolean isAka2() {
		return aka2;
	}


	public void setAka2(boolean aka2) {
		this.aka2 = aka2;
	}


	public boolean isAll() {
		return all;
	}


	public void setAll(boolean all) {
		this.all = all;
	}


	public String getAmf() {
		return amf;
	}


	public void setAmf(String amf) {
		this.amf = amf;
	}


	public boolean isEarly() {
		return early;
	}


	public void setEarly(boolean early) {
		this.early = early;
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


	public boolean isMd5() {
		return md5;
	}


	public void setMd5(boolean md5) {
		this.md5 = md5;
	}


	public String getNextAction() {
		return nextAction;
	}


	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}


	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


	public String getSecretKey() {
		return secretKey;
	}


	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
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

	public void setId_imsu(int id_imsu) {
		this.id_imsu = id_imsu;
	}

	public String getImpu_identity() {
		return impu_identity;
	}

	public void setImpu_identity(String impu_identity) {
		this.impu_identity = impu_identity;
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

	public List getSelect_auth_scheme() {
		return select_auth_scheme;
	}

	public void setSelect_auth_scheme(List select_auth_scheme) {
		this.select_auth_scheme = select_auth_scheme;
	}

	public boolean isDigest() {
		return digest;
	}

	public void setDigest(boolean digest) {
		this.digest = digest;
	}

	public boolean isSip_digest() {
		return sip_digest;
	}

	public void setSip_digest(boolean sip_digest) {
		this.sip_digest = sip_digest;
	}

	public boolean isHttp_digest() {
		return http_digest;
	}

	public void setHttp_digest(boolean http_digest) {
		this.http_digest = http_digest;
	}

	public boolean isNass_bundle() {
		return nass_bundle;
	}

	public void setNass_bundle(boolean nass_bundle) {
		this.nass_bundle = nass_bundle;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}

	public String getImsu_name() {
		return imsu_name;
	}

	public void setImsu_name(String imsu_name) {
		this.imsu_name = imsu_name;
	}

	public int getAlready_assigned_imsu_id() {
		return already_assigned_imsu_id;
	}

	public void setAlready_assigned_imsu_id(int already_assigned_imsu_id) {
		this.already_assigned_imsu_id = already_assigned_imsu_id;
	}

	public String[] getRtr_identities() {
		return rtr_identities;
	}

	public void setRtr_identities(String[] rtr_identities) {
		this.rtr_identities = rtr_identities;
	}


	public List getRtr_select_identities() {
		return rtr_select_identities;
	}

	public void setRtr_select_identities(List rtr_select_identities) {
		this.rtr_select_identities = rtr_select_identities;
	}

	public int getRtr_reason() {
		return rtr_reason;
	}

	public void setRtr_reason(int rtr_reason) {
		this.rtr_reason = rtr_reason;
	}

	public List getSelect_rtr_reason() {
		return select_rtr_reason;
	}

	public void setSelect_rtr_reason(List select_rtr_reason) {
		this.select_rtr_reason = select_rtr_reason;
	}

	public String getReasonInfo() {
		return reasonInfo;
	}

	public void setReasonInfo(String reasonInfo) {
		this.reasonInfo = reasonInfo;
	}

	public int getRtr_apply_for() {
		return rtr_apply_for;
	}

	public void setRtr_apply_for(int rtr_apply_for) {
		this.rtr_apply_for = rtr_apply_for;
	}

	public List getSelect_rtr_apply_for() {
		return select_rtr_apply_for;
	}

	public void setSelect_rtr_apply_for(List select_rtr_apply_for) {
		this.select_rtr_apply_for = select_rtr_apply_for;
	}

	public int getPpr_apply_for() {
		return ppr_apply_for;
	}

	public void setPpr_apply_for(int ppr_apply_for) {
		this.ppr_apply_for = ppr_apply_for;
	}

	public List getSelect_ppr_apply_for() {
		return select_ppr_apply_for;
	}

	public void setSelect_ppr_apply_for(List select_ppr_apply_for) {
		this.select_ppr_apply_for = select_ppr_apply_for;
	}

}
