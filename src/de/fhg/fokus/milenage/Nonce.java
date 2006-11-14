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
public class Nonce extends AbstractAuth3gppObject
{
    private Autn autn;

    private Rand rand;

    public static final int BYTES_LENGTH = 32;

    /**
     * creates new nonce from string. For decoding/encoding Base64 codec will be
     * used
     * 
     * @param base64String
     *            Base64 encoded String
     * @throws CoDecException
     */
    public Nonce(String base64String) throws CoDecException
    {
        this(base64String, Base64CoDec.CODEC_NAME);
    }

    /**
     * creates new nonce from string
     * 
     * @param string
     * @param codecName
     *            codec name to use by decoding
     * @throws CoDecException
     */
    public Nonce(String string, String codecName) throws CoDecException
    {
        super(string, codecName);
        rand = new Rand(bytes, Rand.BYTES_LENGTH, 0);
        autn = new Autn(bytes, Autn.BYTES_LENGTH, Rand.BYTES_LENGTH);
    }

    public Nonce(Rand rand, Autn autn)
    {
        super(new byte[BYTES_LENGTH], BYTES_LENGTH, 0, Base64CoDec.CODEC_NAME);
        this.rand = new Rand(bytes, Rand.BYTES_LENGTH, 0);
        this.autn = new Autn(bytes, Autn.BYTES_LENGTH, Rand.BYTES_LENGTH);
        this.rand.copyFrom(rand);
        this.autn.copyFrom(autn);
    }

    public Nonce(byte[] nonceBytes, int startIndex)
    {
        super(nonceBytes, BYTES_LENGTH, startIndex, Base64CoDec.CODEC_NAME);
        this.rand = new Rand(bytes, Rand.BYTES_LENGTH, startIndex);
        this.autn = new Autn(bytes, Autn.BYTES_LENGTH, startIndex
                + Rand.BYTES_LENGTH);
    }

    /**
     * @param nonceBytes
     */
    public Nonce(byte[] nonceBytes)
    {
        this(nonceBytes, 0);
    }

    /**
     * @return Returns the autn.
     */
    public Autn getAutn()
    {
        return autn;
    }

    /**
     * @param autn
     *            The autn to set.
     */
    public void setAutn(Autn autn)
    {
        this.autn = autn;
    }

    /**
     * @return Returns the rand.
     */
    public Rand getRand()
    {
        return rand;
    }
}
