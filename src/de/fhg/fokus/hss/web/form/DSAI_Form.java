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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.hibernate.DatabaseException;
import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.DSAI;
import de.fhg.fokus.hss.db.model.DSAI_IFC;
import de.fhg.fokus.hss.db.model.DSAI_IMPU;
import de.fhg.fokus.hss.db.model.IFC;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.op.DSAI_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IFC_DAO;
import de.fhg.fokus.hss.db.op.DSAI_IMPU_DAO;
import de.fhg.fokus.hss.sh.ShConstants;
import de.fhg.fokus.hss.web.util.WebConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

/**
 * @author Instrumentacion y Componentes S.A (Inycom).
 * Contact at: ims at inycom dot es
 *
 */


public class DSAI_Form extends ActionForm implements Serializable{


	private static Logger logger = Logger.getLogger(DSAI_Form.class);
	private static final long serialVersionUID=1L;



	private int id;
	private String dsai_tag;
	private int dsai_value;


	private static List select_dsai_value;
	static {
		select_dsai_value = WebConstants.select_dsai_value;
	}


	private int ifc_id;
	private int impu_id;
	private List select_ifc;
	private List select_impu;
	private String nextAction;
	private int associated_ID;


	public void reset(ActionMapping actionMapping, HttpServletRequest request){
    	this.id = -1;
    	this.dsai_tag = null;
    	this.dsai_value=ShConstants.DSAI_value_Active;

    	this.ifc_id = -1;
    	this.impu_id = -1;
    	this.select_ifc = null;
    	this.select_impu = null;
    	this.nextAction = null;
    	this.associated_ID = -1;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request){
        ActionErrors actionErrors = new ActionErrors();

       	boolean dbException = false;
    	try{
    		Session session = HibernateUtil.getCurrentSession();
    		HibernateUtil.beginTransaction();

    		if (nextAction.equals("save")){
        	        if (dsai_tag == null || dsai_tag.equals("")){
        	        	actionErrors.add("dsai.error.dsai_tag", new ActionMessage("dsai.error.dsai_tag"));
        	      }

        			DSAI dsai = DSAI_DAO.get_by_Dsai_tag(session,dsai_tag);
        			if (dsai != null && dsai.getId() != id){
        				actionErrors.add("dsai.error.duplicate_dsai_tag", new ActionMessage("dsai.error.duplicate_dsai_tag"));
        			}

        	}
        	else if (nextAction.equals("attach_ifc")){
        			if (ifc_id == -1){
        				actionErrors.add("dsai.error.invalid_ifc_selection", new ActionMessage("dsai.error.invalid_ifc_selection"));
        			}
        			// check if this association already exists
        			DSAI_IFC dsai_ifc = DSAI_IFC_DAO.get_by_DSAI_and_IFC_ID(session, id, ifc_id);

        			if (dsai_ifc != null){
        				actionErrors.add("dsai.error.duplicate_ifc_association", new ActionMessage("dsai.error.duplicate_ifc_association"));
        			}
        			//Before attaching a new ifc to the dsai, the system needs to check if there is already a dsai with the same pair of
        			//ifc-impu attached. This can not be allowed because it would result in duplicate (and maybe different) dsai-value
        			//for a pair ifc-impu.

        			else{
        				List attached_impu=new ArrayList();
        				List impu_attached_same_ifc=new ArrayList();

        				attached_impu=DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id); //List of IMPUs attached to the DSAI
        				impu_attached_same_ifc=DSAI_IMPU_DAO.get_IMPU_attached_to_same_DSAI_as_IFC(session, ifc_id);//List of IMPUs
        				Iterator attached_impu_it=attached_impu.iterator();

        				if(!impu_attached_same_ifc.isEmpty()){
        					while(attached_impu_it.hasNext()){
        						if(impu_attached_same_ifc.contains((IMPU)attached_impu_it.next())){
        							actionErrors.add("dsai.error.duplicate_ifc_impu_association", new ActionMessage("dsai.error.duplicate_ifc_impu_association"));
        							break;
        						}
        					}
        				}
        			}
        	}

        	else if (nextAction.equals("attach_impu")){

        		if (impu_id == -1){

        				actionErrors.add("dsai.error.invalid_impu_id", new ActionMessage("dsai.error.invalid_impu_id"));
        			}

        			DSAI_IMPU dsai_impu = null;
        			try{
        				dsai_impu  =
        					DSAI_IMPU_DAO.get_by_DSAI_and_IMPU_ID(session, id, impu_id);
        			}
        			catch(org.hibernate.NonUniqueResultException e){
        				logger.error("Query did not returned an unique result! You have a duplicate in the database!");
        				e.printStackTrace();
        			}

        			if (dsai_impu  != null){
        				actionErrors.add("dsai.error.duplicate_dsai_impu_association", new ActionMessage("dsai.error.duplicate_dsai_impu_association"));
        			}

        			//Before attaching a new impu to the dsai, the system needs to check if there is already a dsai with the same pair of
        			//ifc-impu attached. This can not be allowed because it would result in duplicate (and maybe different) dsai-value
        			//for a pair ifc-impu.

        			else{
        				List attached_ifc=new ArrayList();
        				List ifc_attached_same_dsai_as_impu=new ArrayList();

        				attached_ifc=DSAI_IFC_DAO.get_all_IFC_by_DSAI_ID(session, id);
        				ifc_attached_same_dsai_as_impu=DSAI_IFC_DAO.get_IFC_attached_to_same_DSAI_as_IMPU(session, impu_id);
        				Iterator attached_ifc_it=attached_ifc.iterator();

        				if(!ifc_attached_same_dsai_as_impu.isEmpty()){
        					while(attached_ifc_it.hasNext()){
        						if(ifc_attached_same_dsai_as_impu.contains((IFC)attached_ifc_it.next())){
        							actionErrors.add("dsai.error.duplicate_impu_ifc_association", new ActionMessage("dsai.error.duplicate_impu_ifc_association"));
        							break;
        						}
        					}
        				}
        			}
        	}
        	else if (nextAction.equals("detach_ifc")){

        		//Before detaching a new ifc the system needs to check if the impus attached to the same dsai have in their Service
        		//Profiles any of the ifcs that are going to be left after detaching. If not, there is no point in keeping that impu
        		//attached to the dsai. In that case, the system will ask the user to detach that impu first.

        		List dsai_ifc_list=new ArrayList();  //List of IFCs associated to the DSAI except the one the user wants to detach
				List impu_ifc_list=new ArrayList();	 //List of IMPUs that have in their service profile at least one of the IFCs from dsai_ifc_list
				List attached_impu= new ArrayList(); //List of IMPUs attached to the DSAI

				dsai_ifc_list=DSAI_IFC_DAO.get_all_IFC_associated_except_IFC_given(session, associated_ID, id);
				attached_impu=DSAI_IMPU_DAO.get_all_IMPU_by_DSAI_ID(session, id);

				if(dsai_ifc_list.isEmpty()){
					if(!attached_impu.isEmpty()){ //The IFC the user wants to detach is the only one attached to the dsai and there are
												  //IMPUs attached as well. The IFC cannot be detached.
						actionErrors.add("dsai.error.desassociate_impu", new ActionMessage("dsai.error.desassociate_impu"));
					}
				}
				else { //More than one IFC attached
						impu_ifc_list=DSAI_IMPU_DAO.get_IMPU_by_DSAI_for_IFC_list(session, id, dsai_ifc_list);

						boolean same_list=true;   	//same_list=true if IMPUs attached to DSAI have in their Service Profile at
													//least one of the IFCs in dsai_ifc_list.
						Iterator attached_impu_it=attached_impu.iterator();

						while (attached_impu_it.hasNext()){
							if(!impu_ifc_list.contains((IMPU)attached_impu_it.next())){
								same_list=false;
								break;
							}
						}

						if(!same_list){ //There is at least one IMPU that is ONLY associated to the IFC the user wants
										//to detach.
							actionErrors.add("dsai.error.desassociate_impu", new ActionMessage("dsai.error.desassociate_impu"));
						}
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

    // getters & setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDsai_value(){
		return dsai_value;
	}

	public void setDsai_value(int dsai_value){
		this.dsai_value=dsai_value;
	}

	public List getSelect_dsai_value() {
		return select_dsai_value;
	}

	public String getDsai_tag() {
		return dsai_tag;
	}

	public void setDsai_tag(String dsai_tag) {
		this.dsai_tag = dsai_tag;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public int getIfc_id() {
		return ifc_id;
	}

	public void setIfc_id(int ifc_id) {
		this.ifc_id = ifc_id;
	}

	public List getSelect_ifc() {
		return select_ifc;
	}

	public void setSelect_ifc(List select_ifc) {
		this.select_ifc = select_ifc;
	}

	public List getSelect_impu() {
		return select_impu;
	}

	public void setSelect_impu(List select_impu) {
		this.select_impu = select_impu;
	}

	public int getImpu_id() {
		return impu_id;
	}

	public void setImpu_id(int impu_id) {
		this.impu_id = impu_id;
	}

	public int getAssociated_ID() {
		return associated_ID;
	}

	public void setAssociated_ID(int associated_ID) {
		this.associated_ID = associated_ID;
	}


}
