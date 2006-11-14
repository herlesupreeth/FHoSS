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

import de.fhg.fokus.milenage.codec.Base64CoDec;
import de.fhg.fokus.milenage.codec.CoDecException;

/**
 * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class Auts extends AbstractAuth3gppObject
{
    Sqn sqn;

    Mac mac;
    
    Ak ak;
    
    public static final int BYTES_LENGTH= 14;
    
    public Auts(Sqn sqn, Mac mac)
    {
        this(sqn, mac, 0, Sqn.BYTES_LENGTH, BYTES_LENGTH);
    }
    
    Auts(Sqn sqn, Mac mac, int sqnStartIndex, int macStartIndex, int length)
    {
        this(new byte[length]);
        this.sqn= new SimpleSqn(bytes, Sqn.BYTES_LENGTH, sqnStartIndex);
        this.mac= new Mac(bytes, Mac.BYTES_LENGTH, sqnStartIndex+macStartIndex);
        this.sqn.copyFrom(sqn);
        this.mac.copyFrom(mac);
    }
    
    public Auts(byte[] bytes, int startIndex)
    {
        this(bytes, BYTES_LENGTH, startIndex);
    }

    public Auts(byte[] autsBytes)
    {
        this(autsBytes, BYTES_LENGTH, 0);
    }

    Auts(byte[] bytes, int length, int startIndex)
    {
        super(bytes, length, startIndex, Base64CoDec.CODEC_NAME);
        this.sqn = new SimpleSqn(bytes, Sqn.BYTES_LENGTH, startIndex);
        this.mac = new Mac(bytes, Mac.BYTES_LENGTH, startIndex
                + Sqn.BYTES_LENGTH);
    }

    public Auts(String value) throws CoDecException
    {
        super(value, Base64CoDec.CODEC_NAME);
        this.sqn = new SimpleSqn(bytes, Sqn.BYTES_LENGTH, startIndex);
        this.mac = new Mac(bytes, Mac.BYTES_LENGTH, startIndex
                + Sqn.BYTES_LENGTH);
    }
    
    Auts(byte[] bytes, int length, int startIndex, int sqnStartIndex, int macStartIndex)
    {
        super(bytes, length, startIndex, Base64CoDec.CODEC_NAME);
        this.sqn = new SimpleSqn(bytes, Sqn.BYTES_LENGTH, sqnStartIndex);
        this.mac = new Mac(bytes, Mac.BYTES_LENGTH, macStartIndex);
    }
    
    Auts(String value, int sqnStartIndex, int macStartIndex) throws CoDecException
    {
        super(value, Base64CoDec.CODEC_NAME);
        this.sqn = new SimpleSqn(bytes, Sqn.BYTES_LENGTH, sqnStartIndex);
        this.mac = new Mac(bytes, Mac.BYTES_LENGTH, macStartIndex);
    }

    /**
     * @return Returns the mac.
     */
    public Mac getMac()
    {
        return mac;
    }

    /**
     * @return Returns the sqn.
     */
    public Sqn getSqn()
    {
        return sqn;
    }

    public void xorSqn(Ak ak)
    {
        for (int i = sqn.getStartIndex(); i < sqn.getStartIndex()
                + sqn.getLength(); i++)
        {
            bytes[i] = (byte) (bytes[i] ^ ak.getByte(i - sqn.getStartIndex()));
        }
    }

    public String toString()
    {
        try
        {
            return codec.encode(bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.toString();
        }
    }

    public String concatenate() throws CoDecException
    {
        if (ak != null)
        {
            xorSqn(ak);
        }
        return codec.encode(bytes);
    }

    public Sqn extractSqn(Ak ak) throws CoDecException
    {
        if (ak != null)
        {
            xorSqn(ak);
        }
        return getSqn();
    }

    /**
     * @return Returns the ak.
     */
    public Ak getAk()
    {
        return ak;
    }
    /**
     * @param ak The ak to set.
     */
    public void setAk(Ak ak)
    {
        this.ak = ak;
    }
}
