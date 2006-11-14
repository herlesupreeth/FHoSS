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

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/** 
 * This class represents the spt (service pont trigger)table in the database. Hibernate
 * uses it during transaction of service point trigger specific data.
 * @author Hibernate CodeGenerator 
 */
public class Spt extends NotifySupport implements Serializable
{
    /** identifier field */
    private Integer sptId;

    /** persistent field */
    private String sipMethod;

    /** persistent field */
    private String sipHeader;

    /** persistent field */
    private String sipHeaderContent;

    /** persistent field */
    private String reqUri;

    /** persistent field */
    private int sessionCase;

    /** persistent field */
    private String sessionDescContent;

    /** persistent field */
    private String sessionDescLine;

    /** persistent field */
    private boolean neg;

    /** persistent field */
    private int groupId;

    /** persistent field */
    private int type;

    /** persistent field */
    private de.fhg.fokus.hss.model.Trigpt trigpt;

    /** 
     * full constructor 
     * @param sipMethod sip method
     * @param sipHeader sip header
     * @param sipHeaderContent sip header content
     * @param reqUri request Uri
     * @param sessionCase session case: originating 0, terminating 1 or 
              terminating_unregistered 2
     * @param sessionDescContent content of sdp description
     * @param sessionDescLine session description line
     * @param neg flag if service point trigger is negotiated
     * @param group assigned spt-group
     * @param type the internal value for type of data set. 1 for request uri,
              2 for session desc etc...
     * @param trigpt trigger point
     */
    public Spt(
        String sipMethod, String sipHeader, String sipHeaderContent,
        String reqUri, int sessionCase, String sessionDescContent,
        String sessionDescLine, boolean neg, int group, int type,
        de.fhg.fokus.hss.model.Trigpt trigpt)
    {
        this.sipMethod = sipMethod;
        this.sipHeader = sipHeader;
        this.sipHeaderContent = sipHeaderContent;
        this.reqUri = reqUri;
        this.sessionCase = sessionCase;
        this.sessionDescContent = sessionDescContent;
        this.sessionDescLine = sessionDescLine;
        this.neg = neg;
        this.groupId = group;
        this.type = type;
        this.trigpt = trigpt;
    }

    /** default constructor */
    public Spt()
    {
    }

    /** 
     * minimal constructor 
     * @param neg flag if service point trigger is negotiated
     * @param group assigned spt-group
     * @param type the internal value for type of data set. 1 for request uri,
              2 for session desc etc...
     * @param trigpt trigger point
     */
    public Spt(
        boolean neg, int group, int type, de.fhg.fokus.hss.model.Trigpt trigpt)
    {
        this.neg = neg;
        this.groupId = group;
        this.type = type;
        this.trigpt = trigpt;
    }

   /**
    * Getter method for sptId
    * @return the internal id of service point trigger
    */
    public Integer getSptId()
    {
        return this.sptId;
    }

   /**
    * Setter method for sptId
    * @param sptId the internal id of service point trigger
    */
    public void setSptId(Integer sptId)
    {
        this.sptId = sptId;
    }

   /**
    * Getter method for sipMethod
    * @return the sip method
    */
    public String getSipMethod()
    {
        return this.sipMethod;
    }

   /**
    * Setter method for sipMethod
    * @param sipMethod the sip method
    */
    public void setSipMethod(String sipMethod)
    {
        fire("sipMethod", this.sipMethod, sipMethod);
        this.sipMethod = sipMethod;
    }

   /**
    * Getter method for sipHeader
    * @return the sip header
    */
    public String getSipHeader()
    {
        return this.sipHeader;
    }

   /**
    * Setter method for sipHeader
    * @param sipHeader the sip header
    */
    public void setSipHeader(String sipHeader)
    {
        fire("sipHeader", this.sipHeader, sipHeader);
        this.sipHeader = sipHeader;
    }

   /**
    * Getter method for sipHeaderContent
    * @return the content of sip Header
    */
    public String getSipHeaderContent()
    {
        return this.sipHeaderContent;
    }

   /**
    * Setter method for sipHeaderContent
    * @param sipHeaderContent the content of sip header
    */
    public void setSipHeaderContent(String sipHeaderContent)
    {
        fire("sipHeaderContent", this.sipHeaderContent, sipHeaderContent);
        this.sipHeaderContent = sipHeaderContent;
    }

   /**
    * Getter method for reqUri
    * @return the reqUri
    */
    public String getReqUri()
    {
        return this.reqUri;
    }

   /**
    * Setter method for reqUri
    * @param reqUri the the request uri
    */
    public void setReqUri(String reqUri)
    {
        fire("reqUri", this.reqUri, reqUri);
        this.reqUri = reqUri;
    }

   /**
    * Getter method for sessionCase
    * @return the session case
    */
    public int getSessionCase()
    {
        return this.sessionCase;
    }

   /**
    * Setter method for sessionCase
    * @param sessionCase the session case
    */
    public void setSessionCase(int sessionCase)
    {
        fire("sessionCase", this.sessionCase, sessionCase);
        this.sessionCase = sessionCase;
    }

   /**
    * Getter method for sessionDescContent
    * @return the session description content
    */
    public String getSessionDescContent()
    {
        return this.sessionDescContent;
    }

   /**
    * Setter method for sessionDescContent
    * @param sessionDescContent the session description content
    */
    public void setSessionDescContent(String sessionDescContent)
    {
        fire("sessionDescContent", this.sessionDescContent, sessionDescContent);
        this.sessionDescContent = sessionDescContent;
    }

   /**
    * Getter method for sessionDescLine
    * @return the session description line
    */
    public String getSessionDescLine()
    {
        return this.sessionDescLine;
    }

   /**
    * Setter method for sessionDescLine
    * @param sessionDescLine the session description line
    */
    public void setSessionDescLine(String sessionDescLine)
    {
        fire("sessionDescLine", this.sessionDescLine, sessionDescLine);
        this.sessionDescLine = sessionDescLine;
    }

   /**
    * Getter method for trigpt
    * @return the triger point
    */
    public de.fhg.fokus.hss.model.Trigpt getTrigpt()
    {
        return this.trigpt;
    }

   /**
    * Setter method for trigpt
    * @param trigpt the trigger point
    */
    public void setTrigpt(de.fhg.fokus.hss.model.Trigpt trigpt)
    {
        this.trigpt = trigpt;
    }

   /**
    * This method converts into string 
    * @return converted string 
    */
    public String toString()
    {
        return new ToStringBuilder(this).append("sptId", getSptId()).toString();
    }

   /**
    * Getter method for groupId
    * @return the group id
    */
    public int getGroupId()
    {
        return groupId;
    }

   /**
    * Setter method for group
    * @param group the group id
    */
    public void setGroupId(int group)
    {
        fire("groupId", this.groupId, group);
        this.groupId = group;
    }

   /**
    * Getter method for neg
    * @return the value of flag which indicates whether
    *         service point trigger is negotiated or not
    */
    public boolean isNeg()
    {
        return neg;
    }

   /**
    * Setter method for neg
    * @param neg the value of flag which indicate whether
    *        service point trigger is negotiated or not
    */
    public void setNeg(boolean neg)
    {
        fire("neg", this.neg, neg);
        this.neg = neg;
    }

   /**
    * Getter method for type
    * @return the internal value for the type of data set
    */
    public int getType()
    {
        return type;
    }

   /**
    * Setter method for type
    * @param type the internal value for the type of data set
    */
    public void setType(int type)
    {
        this.type = type;
    }
}
