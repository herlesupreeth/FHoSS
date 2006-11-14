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
package de.fhg.fokus.milenage.codec;

import com.mindprod.base64.Base64;

/**
 * Codec implementation for base64.
 * 
 * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class Base64CoDec extends CoDecImpl
{
    /**
     * Comment for <code>base64</code>
     */
    private static Base64 base64 = new Base64();
    
    public Base64CoDec()
    {
        base64.setLineLength(200);
    }
    
    /**
     * Comment for <code>CODEC_BASE64</code>
     */
    public static final String CODEC_NAME= "base64";

    /* (non-Javadoc)
     * @see de.fhg.fokus.milenage.codec.CoDec#encode(byte[], int)
     */
    public String encode(byte[] bytes, int requestedLength) throws CoDecException
    {
        if (bytes.length != requestedLength)
        {
            throw new CoDecException("Requested length is: " + requestedLength
                    + "! Found: " + bytes.length);
        }

        return base64.encode(bytes);
    }

    /* (non-Javadoc)
     * @see de.fhg.fokus.milenage.codec.CoDec#decode(java.lang.String, int)
     */
    public byte[] decode(String string, int requestedLength) throws CoDecException
    {
        if (string.length() != requestedLength)
        {
            throw new CoDecException("Requested length is: " + requestedLength
                    + "! Found: " + string.length());
        }
        return base64.decode(string);
    }

    /* (non-Javadoc)
     * @see de.fhg.fokus.milenage.codec.CoDec#encode(byte[], int, int)
     */
    public String encode(byte[] bytes, int requestedLength, int startIndex) throws CoDecException
    {
        try
        {
            byte[] temp = new byte[requestedLength];
            System.arraycopy(bytes, startIndex, temp, 0, requestedLength);
            return base64.encode(temp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new CoDecException(e.getMessage());
        }
    }

}
