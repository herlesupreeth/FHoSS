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
package de.fhg.fokus.milenage;

import de.fhg.fokus.milenage.codec.CoDecException;

/**
 * @author Sizhong Liu, sli -at- fokus dot fraunhofer dot de
 */
public class Autn extends Auts
{

    Amf amf;
    
    public static final int BYTES_LENGTH= 16;
    
    public Autn(Sqn sqn, Amf amf, Mac mac)
    {
        super(sqn, mac, 0, Sqn.BYTES_LENGTH+Amf.BYTES_LENGTH, BYTES_LENGTH);
        this.amf= new Amf(bytes, Amf.BYTES_LENGTH, Sqn.BYTES_LENGTH);
        this.amf.copyFrom(amf);
    }

    /**
     * Creates a new Autn object
     * @param bytes
     * @param length
     * @param startIndex
     * @param ak
     */
    public Autn(byte[] bytes, int length, int startIndex, Ak ak)
    {
        super(bytes, length, startIndex);
        this.ak = ak;
    }

    public Autn(byte[] bytes, int length, int startIndex)
    {
        super(bytes, length, startIndex, startIndex, startIndex
                + Sqn.BYTES_LENGTH + Amf.BYTES_LENGTH);
        this.amf= new Amf(bytes, Amf.BYTES_LENGTH, startIndex+Sqn.BYTES_LENGTH);
    }

    public Autn(String value) throws CoDecException
    {
        super(value, 0, Sqn.BYTES_LENGTH + Amf.BYTES_LENGTH);
        this.amf= new Amf(bytes, Amf.BYTES_LENGTH, startIndex+Sqn.BYTES_LENGTH);
    }

    public Autn(byte[] bytes)
    {
        this(bytes, bytes.length, 0);
    }

    /**
     * @return Returns the amf.
     */
    public Amf getAmf()
    {
        return amf;
    }

    /**
     * @param amf
     *            The amf to set.
     */
    public void copyAmf(Amf amf)
    {
        this.amf.copyFrom(amf);
    }

}
