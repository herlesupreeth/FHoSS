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

import de.fhg.fokus.milenage.codec.CoDec;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.codec.HexCoDec;

/**
  * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class SimpleSqn extends Sqn
{

    public static final long MIN_VALUE = Long.parseLong("000000000000", 16);

    public static final long MAX_VALUE = Long.parseLong("ffffffffffff", 16);

    /**
     * @param bytes
     */
    SimpleSqn(byte[] bytes)
    {
        super(bytes);
    }

    SimpleSqn(byte[] bytes, int length, int startIndex)
    {
        super(bytes, length, startIndex);
    }

    /**
     * @param hex
     * @throws CoDecException
     */
    public SimpleSqn(String hex) throws CoDecException
    {
        super(hex);
    }

    public Sqn calculateNextSqn() throws CoDecException
    {
        CoDec codec = new HexCoDec();
        CoDec tempCodec = this.codec;
        this.codec = codec;
        try
        {
            String tempHex = getAsString();
            long tempLong = Long.parseLong(tempHex, 16);
            if (tempLong + 1 == MAX_VALUE)
            {
                tempLong = MIN_VALUE;
            }
            else
            {
                tempLong++;
            }
            SimpleSqn nextSqn = (SimpleSqn) createFromLong(tempLong);
            this.copyFrom(nextSqn);
            return this;
        }
        catch (Exception e)
        {
            this.codec= tempCodec;
            e.printStackTrace();
            throw new CoDecException(e.getMessage());
        }
    }

    public static SimpleSqn getNewInstance() throws CoDecException
    {
        return (SimpleSqn) createFromLong(MIN_VALUE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see de.fhg.fokus.milenage.Sqn#isInRange(de.fhg.fokus.milenage.Sqn)
     */
    public boolean isInRange(Sqn sqn)
    {
        CoDec codec = new HexCoDec();
        try
        {
            CoDec myCodec = this.codec;
            CoDec otherCodec = sqn.getCodec();
            sqn.setCodec(codec);
            this.codec = codec;
            String mySqnString = getAsString();
            String otherSqnString = sqn.getAsString();
            long myLong = Long.parseLong(mySqnString, 16);
            long otherLong = Long.parseLong(otherSqnString, 16);
            this.codec = myCodec;
            sqn.setCodec(otherCodec);
            if (Math.abs(myLong - otherLong) <= SQN_RANGE)
            {
                return true;
            }
        }
        catch (CoDecException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Sqn createFromLong(long value) throws CoDecException
    {
        String tempResHex = Long.toHexString(value);
        String temp="";
        for (int i=tempResHex.length();i<Sqn.STRING_LENGTH;i++)
        {
            temp+="0";
        }
        return new SimpleSqn(temp+tempResHex);
    }
    
    public static long getAsLong(Sqn sqn) throws NumberFormatException, CoDecException
    {
        return Long.parseLong(sqn.getAsString(), 16);
    }
}
