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
package de.fhg.fokus.hss.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * This class represents the as_perm_list table in the database. Hibernate
 * uses it during transaction of permission list.
 * @author Hibernate CodeGenerator 
 **/
public class AsPermList implements Serializable {

    /** identifier field */
    private Integer apsvrId;

    /** persistent field */
    private boolean pull;

    /** persistent field */
    private boolean pullRepData;

    /** persistent field */
    private boolean pullImpu;

    /** persistent field */
    private boolean pullImpuUserState;

    /** persistent field */
    private boolean pullScscfName;

    /** persistent field */
    private boolean pullIfc;

    /** persistent field */
    private boolean pullLocInfo;

    /** persistent field */
    private boolean pullUserState;

    /** persistent field */
    private boolean pullCharging;

    /** persistent field */
    private boolean pullPsi;

    /** persistent field */
    private boolean pullMsisdn;

    /** persistent field */
    private boolean updRepData;

    /** persistent field */
    private boolean updPsi;

    /** persistent field */
    private boolean subRepData;

    /** persistent field */
    private boolean subImpuUserState;

    /** persistent field */
    private boolean subScscfname;

    /** persistent field */
    private boolean subIfc;

    /** persistent field */
    private boolean subPsi;

    /** 
     * full constructor 
     * @param apsvrId Id of application Server
     * @param pull 
     * @param pullRepData
     * @param pullImpu
     * @param pullImpuUserState
     * @param pullScscfName
     * @param pullIfc
     * @param pullLocInfo
     * @param pullUserState
     * @param pullCharging
     * @param pullPsi
     * @param pullMsisdn
     * @param updRepData
     * @param updPsi
     * @param subRepData
     * @param subImpuUserState
     * @param subScscfname
     * @param subIfc
     * @param subPsi
     **/
    public AsPermList(Integer apsvrId, boolean pull, boolean pullRepData, boolean pullImpu, boolean pullImpuUserState, boolean pullScscfName, boolean pullIfc, boolean pullLocInfo, boolean pullUserState, boolean pullCharging, boolean pullPsi, boolean pullMsisdn, boolean updRepData, boolean updPsi, boolean subRepData, boolean subImpuUserState, boolean subScscfname, boolean subIfc, boolean subPsi) {
        this.apsvrId = apsvrId;
        this.pull = pull;
        this.pullRepData = pullRepData;
        this.pullImpu = pullImpu;
        this.pullImpuUserState = pullImpuUserState;
        this.pullScscfName = pullScscfName;
        this.pullIfc = pullIfc;
        this.pullLocInfo = pullLocInfo;
        this.pullUserState = pullUserState;
        this.pullCharging = pullCharging;
        this.pullPsi = pullPsi;
        this.pullMsisdn = pullMsisdn;
        this.updRepData = updRepData;
        this.updPsi = updPsi;
        this.subRepData = subRepData;
        this.subImpuUserState = subImpuUserState;
        this.subScscfname = subScscfname;
        this.subIfc = subIfc;
        this.subPsi = subPsi;
    }

    /** default constructor */
    public AsPermList() {
    }
   
   /**
    * Get method for id of application server 
    * @return id of application server
    */
    public Integer getApsvrId() {
        return this.apsvrId;
    }

   /**
    * Set method for id of application server
    * @param apsvrId id of application server
    */
    public void setApsvrId(Integer apsvrId) {
        this.apsvrId = apsvrId;
    }

   /**
    * Get method for pull 
    * @return boolean value of pull
    */
    public boolean isPull() {
        return this.pull;
    }
   
   /**
    * Set method for pull
    * @param pull boolean value for pull
    */
    public void setPull(boolean pull) {
        this.pull = pull;
    }

   /**
    * Get method for pullRepData 
    * @return boolean value of pullRepData
    */
    public boolean isPullRepData() {
        return this.pullRepData;
    }

   /**
    * Set method for pullRepData
    * @param pullRepData boolean value of pullRepData
    */
    public void setPullRepData(boolean pullRepData) {
        this.pullRepData = pullRepData;
    }

   /**
    * Get method for pullImpu 
    * @return boolean value of pullImpu
    */
    public boolean isPullImpu() {
        return this.pullImpu;
    }

   /**
    * Set method for pullImpu
    * @param pullImpu boolean value of pullImpu
    */
    public void setPullImpu(boolean pullImpu) {
        this.pullImpu = pullImpu;
    }

   /**
    * Get method for pullImpuUserState 
    * @return boolean value of pullImpuUserState
    */
    public boolean isPullImpuUserState() {
        return this.pullImpuUserState;
    }

   /**
    * Set method for pullImpuUserState
    * @param pullImpuUserState boolean value of pullImpuUserState
    */
    public void setPullImpuUserState(boolean pullImpuUserState) {
        this.pullImpuUserState = pullImpuUserState;
    }

   /**
    * Get method for pullScscfName 
    * @return boolean value of pullScscfName
    */
    public boolean isPullScscfName() {
        return this.pullScscfName;
    }

   /**
    * Set method for pullScscfName
    * @param pullScscfName boolean value of pullScscfName
    */
    public void setPullScscfName(boolean pullScscfName) {
        this.pullScscfName = pullScscfName;
    }

   /**
    * Get method for pullIfc
    * @return boolean value of pullIfc
    */
    public boolean isPullIfc() {
        return this.pullIfc;
    }

   /**
    * Set method for pullIfc
    * @param pullIfc boolean value of pullIfc
    */
    public void setPullIfc(boolean pullIfc) {
        this.pullIfc = pullIfc;
    }

   /**
    * Get method for pullLocInfo 
    * @return boolean value of pullLocInfo
    */
    public boolean isPullLocInfo() {
        return this.pullLocInfo;
    }

   /**
    * Set method for pullLocInfo
    * @param pullLocInfo boolean value of pullLocInfo
    */
    public void setPullLocInfo(boolean pullLocInfo) {
        this.pullLocInfo = pullLocInfo;
    }

   /**
    * Get method for pullUserState 
    * @return boolean value of pullUserState
    */
    public boolean isPullUserState() {
        return this.pullUserState;
    }

   /**
    * Set method for pullUserState
    * @param pullUserState boolean value of pullUserState
    */
    public void setPullUserState(boolean pullUserState) {
        this.pullUserState = pullUserState;
    }

   /**
    * Get method for pullCharging 
    * @return boolean value of pullCharging
    */
    public boolean isPullCharging() {
        return this.pullCharging;
    }

   /**
    * Set method for pullCharging
    * @param pullCharging boolean value of pullCharging
    */
    public void setPullCharging(boolean pullCharging) {
        this.pullCharging = pullCharging;
    }

   /**
    * Get method for pullPsi 
    * @return boolean value of pullPsi
    */
    public boolean isPullPsi() {
        return this.pullPsi;
    }

   /**
    * Set method for pullPsi
    * @param pullPsi boolean value of pullPsi
    */
    public void setPullPsi(boolean pullPsi) {
        this.pullPsi = pullPsi;
    }

   /**
    * Get method for pullMsisdn 
    * @return boolean value of pullMsisdn
    */
    public boolean isPullMsisdn() {
        return this.pullMsisdn;
    }

   /**
    * Set method for pullMsisdn
    * @param pullMsisdn boolean value of pullMsisdn
    */
    public void setPullMsisdn(boolean pullMsisdn) {
        this.pullMsisdn = pullMsisdn;
    }

   /**
    * Get method for updRepData 
    * @return boolean value of updRepData
    */
    public boolean isUpdRepData() {
        return this.updRepData;
    }

   /**
    * Set method for updRepData
    * @param updRepData boolean value of updRepData
    */
    public void setUpdRepData(boolean updRepData) {
        this.updRepData = updRepData;
    }

   /**
    * Get method for updPsi 
    * @return boolean value of updPsi
    */
    public boolean isUpdPsi() {
        return this.updPsi;
    }

   /**
    * Set method for updPsi
    * @param updPsi boolean value of updPsi
    */
    public void setUpdPsi(boolean updPsi) {
        this.updPsi = updPsi;
    }

   /**
    * Get method for subRepData 
    * @return boolean value of subRepData
    */
    public boolean isSubRepData() {
        return this.subRepData;
    }

   /**
    * Set method for subRepData
    * @param subRepData boolean value of subRepData
    */
    public void setSubRepData(boolean subRepData) {
        this.subRepData = subRepData;
    }

   /**
    * Get method for SubImpuUserState 
    * @return boolean value of SubImpuUserState
    */
    public boolean isSubImpuUserState() {
        return this.subImpuUserState;
    }

   /**
    * Set method for subImpuUserState
    * @param subImpuUserState boolean value of subImpuUserState
    */
    public void setSubImpuUserState(boolean subImpuUserState) {
        this.subImpuUserState = subImpuUserState;
    }

   /**
    * Get method for SubScscfname
    * @return boolean value of isSubScscfname
    */
    public boolean isSubScscfname() {
        return this.subScscfname;
    }

   /**
    * Set method for subScscfname
    * @param subScscfname boolean value of subScscfname
    */
    public void setSubScscfname(boolean subScscfname) {
        this.subScscfname = subScscfname;
    }

   /**
    * Get method for subIfc 
    * @return boolean value of subIfc
    */
    public boolean isSubIfc() {
        return this.subIfc;
    }

   /**
    * Set method for subIfc
    * @param subIfc boolean value of subIfc
    */
    public void setSubIfc(boolean subIfc) {
        this.subIfc = subIfc;
    }

   /**
    * Get method for subPsi
    * @return boolean value of subPsi
    */
    public boolean isSubPsi() {
        return this.subPsi;
    }

   /**
    * Set method for subPsi
    * @param subPsi boolean value of subPsi
    */
    public void setSubPsi(boolean subPsi) {
        this.subPsi = subPsi;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString() {
        return new ToStringBuilder(this)
            .append("apsvrId", getApsvrId())
            .toString();
    }

}
