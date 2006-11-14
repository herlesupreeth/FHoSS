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
import de.fhg.fokus.milenage.codec.CoDec;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.codec.HexCoDec;

/**
 * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class AbstractAuth3gppObject extends Abstract3gppObject
{
    byte[] bytes;

    int length;

    int startIndex;

    CoDec codec;

    private AbstractAuth3gppObject(String codecName)
    {
        if (codecName == Base64CoDec.CODEC_NAME)
        {
            codec = new Base64CoDec();
        }
        else if (codecName == HexCoDec.CODEC_NAME)
        {
            codec = new HexCoDec();
        }
        else
        {
            //TODO throw any Exception
        }
    }

    AbstractAuth3gppObject(String value, String codecName)
            throws CoDecException
    {
        this(codecName);
        bytes = codec.decode(value);
        this.length = bytes.length;
        this.startIndex = 0;
    }

    AbstractAuth3gppObject(byte bytes[], int requestedLength, int startIndex,
            String codecName)
    {
        this(codecName);
        this.length = requestedLength;
        this.startIndex = startIndex;
        this.bytes = bytes;

    }

    AbstractAuth3gppObject(String string, int requestedLength, String codecName)
            throws CoDecException
    {
        this(codecName);
        this.bytes = codec.decode(string, requestedLength);
        this.length = bytes.length;
        this.startIndex = 0;
    }

    /**
     * @return Returns the bytes.
     */
    public byte[] getBytes()
    {
        return bytes;
    }

    public String getAsString() throws CoDecException
    {
        return codec.encode(bytes, length, startIndex);
    }

    public boolean equals(Object o)
    {
        try
        {
            AbstractAuth3gppObject other = (AbstractAuth3gppObject) o;
            for(int i=this.startIndex;i<this.startIndex+this.length;i++)
            {
                if(this.bytes[i]!=other.getByte(i-this.startIndex))
                {
                    return false;
                }
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String toString()
    {
        try
        {
            return getAsString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.toString();
        }
    }

    /**
     * @return Returns the codec.
     */
    public CoDec getCodec()
    {
        return codec;
    }

    /**
     * @return Returns the length.
     */
    public int getLength()
    {
        return length;
    }

    /**
     * @param length
     *            The length to set.
     */
    public void setLength(int length)
    {
        this.length = length;
    }

    /**
     * @return Returns the startIndex.
     */
    public int getStartIndex()
    {
        return startIndex;
    }

    /**
     * @param codec
     *            The codec to set.
     */
    public void setCodec(CoDec codec)
    {
        this.codec = codec;
    }

    /**
     * @param bytes
     *            The bytes to set.
     */
    public void setBytes(byte[] bytes)
    {
        this.bytes = bytes;
    }

    /**
     * @param requestedLength
     *            The requestedLength to set.
     */
    public void setRequestedLength(int requestedLength)
    {
        this.length = requestedLength;
    }

    /**
     * @param startIndex
     *            The startIndex to set.
     */
    public void setStartIndex(int startIndex)
    {
        this.startIndex = startIndex;
    }

    public void copyFrom(Object o)
    {
        System.arraycopy(((AbstractAuth3gppObject) o).getBytes(),
                ((AbstractAuth3gppObject) o).getStartIndex(), bytes,
                startIndex, length);
    }
    
    public byte getByte(int index)
    {
        return bytes[index+this.startIndex];
    }
}
