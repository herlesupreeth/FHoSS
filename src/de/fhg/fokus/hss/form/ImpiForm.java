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
package de.fhg.fokus.hss.form;

import de.fhg.fokus.hss.model.AuthSchemeBO;
import de.fhg.fokus.hss.server.HSSProperties;
import de.fhg.fokus.hss.util.Converter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class ImpiForm extends ImpuSubSelectForm
{
    private static final Logger LOGGER = Logger.getLogger(ImpiForm.class);
    private static final ArrayList authSchemes;
    private static final ArrayList authAlgos;

    static
    {
        authSchemes = new ArrayList();
        authSchemes.add(new Tupel(AuthSchemeBO.AUS_MD5, AuthSchemeBO.AUS_MD5));

        //authSchemes.add(
        //  new Tupel(AuthSchemeBO.AUS_EARLY, AuthSchemeBO.AUS_EARLY));
    }

    static
    {
        authAlgos = new ArrayList();
        authAlgos.add(
            new Tupel(AuthSchemeBO.AUTH_ALGO_MD5, AuthSchemeBO.AUTH_ALGO_MD5));
    }

    public static final String SQD_UPDATE_TRUE = "1";
    public static final String SQD_UPDATE_FALSE = "0";
    private String impiId;
    private String impiString;
    private String imsi;
    private String scscfName;
    private String skey;
    private String authScheme;
    private String amf;
    private String algorithm;
    private String operatorId;
    private String sqn;
    private String sqnUpdate;
    private Set roamNetworkIdentifiers;
    private String asBytes;
    private String chrgInfoId;
    private List chrgInfos;

    public Set getRoamNetworkIdentifiers()
    {
        return roamNetworkIdentifiers;
    }

    public void setRoamNetworkIdentifiers(Set roamNetworkIdentifiers)
    {
        this.roamNetworkIdentifiers = roamNetworkIdentifiers;
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");

        ActionErrors actionErrors = new ActionErrors();

        if ((getImpiString() == null) || (getImpiString().length() < 1))
        {
            actionErrors.add(
                "impiString", new ActionMessage(
                    "impi.error.missing.impiString"));
        }

        if (
            (getAuthScheme() != null)
                && (getAuthScheme().equals(AuthSchemeBO.AUS_MD5)))
        {
            if ((getAmf() == null) || (getAmf().length() != 4))
            {
                actionErrors.add(
                    "amf", new ActionMessage("impi.error.size.amf"));
            }

            if ((getOperatorId() == null) || (getOperatorId().length() != 32))
            {
                actionErrors.add(
                    "operatorId",
                    new ActionMessage("impi.error.size.operatorId"));
            }

            if ((getSkey() == null) || (getSkey().length() != 32))
            {
                actionErrors.add(
                    "skey", new ActionMessage("impi.error.size.skey"));
            }

            if ((getSqn() == null) || (getSqn().length() != 12))
            {
                actionErrors.add(
                    "sqn", new ActionMessage("impi.error.size.sqn"));
            }
        }

        LOGGER.debug("exiting");

        return actionErrors;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        LOGGER.debug("entering");
        impiId = null;
        impiString = null;
        imsi = null;
        roamNetworkIdentifiers = new TreeSet();
        scscfName = null;
        skey = "00000000000000000000000000000000";
        authScheme = AuthSchemeBO.AUS_MD5;
        amf = HSSProperties.AMF_ID;
        algorithm = AuthSchemeBO.AUTH_ALGO_MD5;
        operatorId = HSSProperties.OPERATOR_ID;
        sqn = "000000000000";
        sqnUpdate = SQD_UPDATE_FALSE;
        asBytes = "true";
        chrgInfoId = null;
        chrgInfos = new ArrayList();
        super.reset(arg0, arg1);

        LOGGER.debug("exiting");
    }

    public Integer getPrimaryKey()
    {
        return getPrimaryKey(getImpiId());
    }

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }

    public String getImpiId()
    {
        return impiId;
    }

    public void setImpiId(String impiId)
    {
        this.impiId = impiId;
    }

    public String getImpiString()
    {
        return impiString;
    }

    public void setImpiString(String impiString)
    {
        this.impiString = impiString;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(
            this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getScscfName()
    {
        return scscfName;
    }

    public void setScscfName(String cscfName)
    {
        scscfName = cscfName;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    public String getAmf()
    {
        return amf;
    }

    public void setAmf(String amf)
    {
        this.amf = amf;
    }

    public String getAuthScheme()
    {
        return authScheme;
    }

    public void setAuthScheme(String authScheme)
    {
        this.authScheme = authScheme;
    }

    public String getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

    public String getSkey()
    {
        return skey;
    }

    public void setSkey(String key)
    {
        skey = key;
    }

    public String getSkeyChars()
    {
        return Converter.convertByteStringToString(skey);
    }

    public void setSkeyChars(String key)
    {
        skey = Converter.convertStringToByteString(key, 32);
    }

    public ArrayList getAuthSchemes()
    {
        return authSchemes;
    }

    public static void setAuthSchemes(ArrayList authSchemes)
    {
        // do Nothing;
    }

    public String getSqn()
    {
        return sqn;
    }

    public void setSqn(String sqn)
    {
        this.sqn = sqn;
    }

    public String getAsBytes()
    {
        return asBytes;
    }

    public void setAsBytes(String asBytes)
    {
        this.asBytes = asBytes;
    }

    public ArrayList getAuthAlgos()
    {
        return authAlgos;
    }

    public String getChrgInfoId()
    {
        return chrgInfoId;
    }

    public void setChrgInfoId(String chrgInfoId)
    {
        this.chrgInfoId = chrgInfoId;
    }

    public List getChrgInfos()
    {
        return chrgInfos;
    }

    public void setChrgInfos(List chrgInfos)
    {
        this.chrgInfos = chrgInfos;
    }

    public String getSqnUpdate()
    {
        return sqnUpdate;
    }

    public void setSqnUpdate(String sqnUpdate)
    {
        this.sqnUpdate = sqnUpdate;
    }
}
