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
package de.fhg.fokus.hss.server.cx.util;

import java.net.Inet4Address;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.AuthenticationVector;
import de.fhg.fokus.milenage.Ak;
import de.fhg.fokus.milenage.Amf;
import de.fhg.fokus.milenage.AuthKey;
import de.fhg.fokus.milenage.Autn;
import de.fhg.fokus.milenage.Ck;
import de.fhg.fokus.milenage.Ik;
import de.fhg.fokus.milenage.Mac;
import de.fhg.fokus.milenage.Nonce;
import de.fhg.fokus.milenage.Opc;
import de.fhg.fokus.milenage.Rand;
import de.fhg.fokus.milenage.Res;
import de.fhg.fokus.milenage.Sqn;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.server.ServerDigestAKA;


/**
 * This class provides some methods to generate authentication vectors.
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class AKAUtil
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(AKAUtil.class);
    /**
     * It generates authentication vector with the help of provided arguments
     * @param random an object which can generate some random value
     * @param secretKey secret key object
     * @param amf authentication management field object
     * @param opc operator specific parameter object
     * @param sqn sequence number object
     * @param pos position
     * @param digestAKA digestAKA object
     * @param authScheme authentication scheme
     * @param ip ip address
     * @return the generated authentication vector
     * @throws CoDecException
     * @throws InvalidKeyException
     */    
    public static AuthenticationVector generateAuthenticationVector(
        SecureRandom random, AuthKey secretKey, Amf amf, Opc opc, Sqn sqn,
        long pos, ServerDigestAKA digestAKA, String authScheme, Inet4Address ip)
        throws CoDecException, InvalidKeyException
    {
        byte[] randBytes = new byte[Rand.BYTES_LENGTH];
        random.setSeed(System.currentTimeMillis());
        random.nextBytes(randBytes);

        Rand rand = new Rand(randBytes);

        return generateAuthenticationVector(
            rand, secretKey, amf, opc, sqn, pos, digestAKA, authScheme, ip);
    }

    /**
     * It generates authentication vector with the help of provided arguments
     * @param rand an object which can generate some random value
     * @param secretKey secret key object
     * @param amf authentication management field object
     * @param opc operator specific parameter object
     * @param sqn sequence number object
     * @param pos position
     * @param digestAKA digestAKA Object
     * @param authScheme authentication scheme
     * @param ip ip address
     * @return the generated authentication vector
     * @throws CoDecException
     * @throws InvalidKeyException
     */ 
    public static AuthenticationVector generateAuthenticationVector(
        Rand rand, AuthKey secretKey, Amf amf, Opc opc, Sqn sqn, long pos,
        ServerDigestAKA digestAKA, String authScheme, Inet4Address ip)
        throws CoDecException, InvalidKeyException
    {
        LOGGER.debug("vector number: " + pos);
        LOGGER.debug("amf: " + amf.getAsString());
        LOGGER.debug("opc: " + opc.getAsString());
        LOGGER.debug("rand: " + rand.getAsString());
        LOGGER.debug("sqn: " + sqn.getAsString());

        Mac mac = digestAKA.generateMACA(secretKey, rand, opc, sqn, amf);

        LOGGER.debug("mac: " + mac.getAsString());

        Res res = digestAKA.generateXRES(authScheme,secretKey, rand, opc);

        LOGGER.debug("res: " + res.getAsString());

        Ck ck = digestAKA.generateCipherKey(secretKey, rand, opc);

        LOGGER.debug("ck: " + ck.getAsString());

        Ik ik = digestAKA.generateIntegrityKey(secretKey, rand, opc);
        LOGGER.debug("ik: " + ik.getAsString());

        Ak ak = null;
        Autn autn = new Autn(sqn, amf, mac);

        if (Ak.USE_AK)
        {
            ak = digestAKA.generateAnonoymityKey(secretKey, rand, opc);
            LOGGER.debug("ak: " + ak.getAsString());
            autn.xorSqn(ak);
        }

        Nonce nonce = new Nonce(rand, autn);

        byte[] sipAuthenticate = nonce.getBytes();
        byte[] sipAuthorization = res.getBytes();

        return new AuthenticationVector(
            authScheme, ip, sipAuthenticate, sipAuthorization, ck.getBytes(),
            ik.getBytes());
    }
}
