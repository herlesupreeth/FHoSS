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


package de.fhg.fokus.cx;

import java.net.URI;


/**
 * This class represents the set of charging information used in IMS
 * for the purpose of accounting. 
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ChargingInfoSet
{
    /** 
     * primary event charging function name
     */
	private URI pri_event_chrg_fn_name;
    /** 
     * secondary event charging function name
     */	
    private URI sec_event_chrg_fn_name;
    /** 
     * primary charging collection function name
     */    
    private URI pri_chrg_coll_fn_name;
    /** 
     * secondary charging collection function name
     */    
    private URI sec_chrg_coll_fn_name;
    /**
     * minimal constructor
     * @param pri_chrg_coll_fn_name primary charging collection function name
     */
    public ChargingInfoSet(URI pri_chrg_coll_fn_name){
    	this.pri_chrg_coll_fn_name = pri_chrg_coll_fn_name;
    }
    /**
     * Getter method for primary charging collection function name
     * @return name primary charging collection function name
     */    
    public URI getPri_chrg_coll_fn_name()
    {
        return pri_chrg_coll_fn_name;
    }

    /**
     * Setter method for primary charging collection function name
     * @param pri_chrg_coll_fn_name primary charging collection function name
     */     
    public void setPri_chrg_coll_fn_name(URI pri_chrg_coll_fn_name)
    {
        this.pri_chrg_coll_fn_name = pri_chrg_coll_fn_name;
    }

    /**
     * Getter method for primary event charging function name
     * @return primary event charging function name
     */     
    public URI getPri_event_chrg_fn_name()
    {
        return pri_event_chrg_fn_name;
    }

    /**
     * Setter method for primary event charging function name
     * @param pri_event_chrg_fn_name primary event charging function name
     */  
    public void setPri_event_chrg_fn_name(URI pri_event_chrg_fn_name)
    {
        this.pri_event_chrg_fn_name = pri_event_chrg_fn_name;
    }

    /**
     * Getter method for secondary charging collection function name
     * @return secondary charging collection function name
     */      
    public URI getSec_chrg_coll_fn_name()
    {
        return sec_chrg_coll_fn_name;
    }

    /**
     * Setter method for secondary charging collection function name
     * @param sec_chrg_coll_fn_name secondary charging collection function name
     */     
    public void setSec_chrg_coll_fn_name(URI sec_chrg_coll_fn_name)
    {
        this.sec_chrg_coll_fn_name = sec_chrg_coll_fn_name;
    }

    /**
     * Getter method for secondary event charging function name
     * @return secondary event charging function name
     */         
    public URI getSec_event_chrg_fn_name()
    {
        return sec_event_chrg_fn_name;
    }

    /**
     * Setter method for secondary event charging function name
     * @param sec_event_chrg_fn_name secondary event charging function name
     */     
    public void setSec_event_chrg_fn_name(URI sec_event_chrg_fn_name)
    {
        this.sec_event_chrg_fn_name = sec_event_chrg_fn_name;
    }
}
