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
package de.fhg.fokus.sh;

import java.util.HashSet;
import java.util.Set;

/**
 * Indicate which data is requested. Reference to the data that an AS is
 * requesting from the HSS. Reference to the data which an AS wants to be
 * notified of when changed. Reference to data for which subscription to
 * notification of change is rejected. See 3GPP TS 29.329, section 7.6
 * 
 * @author Elmar Fasel, fasel -at- fokus dot fraunhofer dot de
 * @version $Revision$ $Date$
 */
public class RequestedData extends CheckedConstantsValue {

    /**
     * A set with all possible values.
     */
    private static Set values = new HashSet();

    /**
     * Transparent data.
     */
    public final static int _REPOSITORYDATA = 0;

    /**
     * List of public identities of the user.
     */
    public final static int _PUBLICIDENTIFIERS = 10;

    /**
     * IMS user state
     */
    public final static int _IMSUSERSTATE = 11;

    /**
     * Name of the S-CSCF where a multimedia public identity is registered.
     */
    public final static int _SCSCFNAME = 12;

    /**
     * Triggering information for a service.
     */
    public final static int _INITIALFILTERCRITERIA = 13;

    /**
     * Location of the served subscriber in the MSC/VLR if the requested domain
     * is CS, or the location of the served subscriber in the SGSN if the
     * requested domain is PS.
     */
    public final static int _LOCATIONINFORMATION = 14;

    /**
     * State of the user in the domain indicated by the RequestedDomain.
     */
    public final static int _USERSTATE = 15;

    /**
     * The addresses of the charging functions.
     */
    public final static int _CHARGINGINFORMATION = 16;

    /**
     * initialise the set with all possible values
     */
    static {
        values.add(new Integer(_REPOSITORYDATA));
        values.add(new Integer(_PUBLICIDENTIFIERS));
        values.add(new Integer(_IMSUSERSTATE));
        values.add(new Integer(_SCSCFNAME));
        values.add(new Integer(_INITIALFILTERCRITERIA));
        values.add(new Integer(_LOCATIONINFORMATION));
        values.add(new Integer(_USERSTATE));
        values.add(new Integer(_CHARGINGINFORMATION));
    }

    public static final RequestedData REPOSITORYDATA = new RequestedData(_REPOSITORYDATA);

    public static final RequestedData PUBLICIDENTIFIERS = new RequestedData(_PUBLICIDENTIFIERS);

    public static final RequestedData IMSUSERSTATE = new RequestedData(_IMSUSERSTATE);

    public static final RequestedData SCSCFNAME = new RequestedData(_SCSCFNAME);

    public static final RequestedData INITIALFILTERCRITERIA = new RequestedData(_INITIALFILTERCRITERIA);
    
    public static final RequestedData LOCATIONINFORMATION = new RequestedData(_LOCATIONINFORMATION);

    public static final RequestedData USERSTATE = new RequestedData(_USERSTATE);
    
    public static final RequestedData CHARGINGINFORMATION = new RequestedData(_CHARGINGINFORMATION);
    
    /**
     * Create an instance with one value of the defined constants.
     * 
     * @param value
     *            one of the defined constants
     */
    private RequestedData(int value) {
        super(values, value);
    }

}