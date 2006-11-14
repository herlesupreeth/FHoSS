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
 * Classes which have a defined set of possible constant values may use this
 * class as a base class.
 * 
 * @author Elmar Fasel, fasel -at- fokus dot fraunhofer dot de
 * @version $Revision$ $Date$
 */
public abstract class CheckedConstantsValue {

    /**
     * the set of possible values
     */
    private Set values = new HashSet();

    /**
     * the set of already used values
     */
    private Set usedValues = new HashSet();

    /**
     * the value of this class
     */
    int value = -1;

    /**
     * Get value of the instance.
     * 
     * @return one of the defined constants
     */
    public int getValue() {
        return value;
    }

    /**
     * Use prohibited.
     */
    private CheckedConstantsValue() {
        // use prohibited
    }

    /**
     * Create a CheckedConstantsValue instance.
     * 
     * @param values
     *            the possible values
     * @param value
     *            the value of this instance
     */
    protected CheckedConstantsValue(Set values, int value) {
        this.values = values;
        this.value = value;
        checkValue(value);
    }

    /**
     * Check if the specified value is one of the possible values.
     * @param value2
     * 
     * @throws IllegalArgumentException
     */
    private void checkValue(int value) throws IllegalArgumentException {
        if (usedValues.contains(new Integer(value))) {
            throw new RuntimeException("Constant \"" + value + "\" already used.");
        }
        if (values.size() == 0) {
            throw new RuntimeException("Please initialise the values Set.");
        }
        if (!values.contains(new Integer(value))) {
            throw new IllegalArgumentException(value + " is not one of the defined constants for this class");
        }
    }

}