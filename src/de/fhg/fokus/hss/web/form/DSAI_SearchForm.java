/*
  *  Copyright (C) 2004-2007 FhG Fokus
  *
  * Developed by Instrumentacion y Componentes S.A. (Inycom). Contact at: ims at inycom dot es
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

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.SP;
import de.fhg.fokus.hss.db.model.SP_IFC;
import de.fhg.fokus.hss.db.model.SP_Shared_IFC_Set;
import de.fhg.fokus.hss.db.op.SP_DAO;
import de.fhg.fokus.hss.db.op.SP_IFC_DAO;
import de.fhg.fokus.hss.db.op.SP_Shared_IFC_Set_DAO;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */


public class DSAI_SearchForm extends ActionForm implements Serializable{
	private static final long serialVersionUID=1L;

	private static Logger logger = Logger.getLogger(DSAI_SearchForm.class);

	private String id_dsai;
	private String dsai_tag;

	private String crtPage;		//Current page
	private String rowsPerPage;	//Rows to show in each page


	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

        boolean dbException = false;
    	try{

    		if (!isInteger(id_dsai)&& id_dsai!="" && id_dsai!=null ){ //"isInteger" is an auxiliar method that checks if the value introduced in ID field is an integer.
    			actionErrors.add("dsai.error.id_not_integer", new ActionMessage("dsai.error.id_not_integer"));
    		}

    		if(isNegative(id_dsai)){ //"isNegative" is an auxiliar method that checks if the value introduced in ID field is a negative integer.

    			actionErrors.add("dsai.error.id_negative", new ActionMessage("dsai.error.id_negative"));
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

	// Reset
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
  		this.dsai_tag = null;
  		this.id_dsai = null;
  		crtPage = "1";
  		rowsPerPage = "20";
  	}

	//Getters and setters
	public String getCrtPage() {
		return crtPage;
	}

	public void setCrtPage(String crtPage) {
		this.crtPage = crtPage;
	}

	public String getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public String getDsai_tag() {
		return dsai_tag;
	}

	public void setDsai_tag(String dsai_tag) {
		this.dsai_tag = dsai_tag;
	}

	public String getId_dsai() {
		return id_dsai;
	}

	public void setId_dsai(String id_dsai) {
		this.id_dsai = id_dsai;
	}

	//Auxiliar methods for Validate

	private boolean isInteger (String id){

		try {
			Integer.parseInt(id);
			return true;
		}
		catch (java.lang.Exception e){
			return false;
		}

	}

	private boolean isNegative (String dsai_id){

		boolean isNeg=false;
		int id=0;
		try {
			id=Integer.parseInt(dsai_id);
			if (id<0){isNeg=true;}
			else {
				isNeg=false;
			}
			return isNeg;
		}
		catch (java.lang.Exception e){
			isNeg=false;
			return isNeg;
		}

	}
}
