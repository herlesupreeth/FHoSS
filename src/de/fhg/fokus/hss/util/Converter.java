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
package de.fhg.fokus.hss.util;


/**
 * Converter converts HexByte to String, like T = 54.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class Converter
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String wert = "AAN";

        System.out.println(convertStringToByteString(wert, 32));
        System.out.println(
            convertByteStringToString(convertStringToByteString(wert, 32)));
    }

    public static String convertStringToByteString(String wert, int size)
    {
        StringBuffer bytes = new StringBuffer();
        char[] wertAsChars = wert.toCharArray();

        for (int ix = 0; ix < wertAsChars.length; ix++)
        {
            bytes.append(convertDecToHex(wertAsChars[ix]));
        }

        // while bytes are to small ad 0-bytes
        while (bytes.length() < size)
        {
            bytes = bytes.append("0");
        }

        return bytes.toString();
    }

    private static String convertDecToHex(char next)
    {
        return String.valueOf(
            Integer.toHexString(Integer.parseInt(String.valueOf((byte) next))));
    }

    public static String convertByteStringToString(String string)
    {
        if ((string.length() % 2) != 0)
        {
            throw new ArrayIndexOutOfBoundsException(
                "Can't convert string div. to mod 2.");
        }

        StringBuffer toString = new StringBuffer();
        String next = null;

        for (int ix = 0; ix < string.length();)
        {
            next =
                String.valueOf(string.charAt(ix++))
                + String.valueOf(string.charAt(ix++));

            if (next.equals("00") == false)
            {
                toString.append((char) (Integer.parseInt(next, 16)));
            }
        }

        return toString.toString();
    }
}
