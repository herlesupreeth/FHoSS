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
package de.fhg.fokus.milenage.server;

import java.security.InvalidKeyException;

import de.fhg.fokus.milenage.Ak;
import de.fhg.fokus.milenage.Amf;
import de.fhg.fokus.milenage.AuthKey;
import de.fhg.fokus.milenage.Ck;
import de.fhg.fokus.milenage.DigestAKA;
import de.fhg.fokus.milenage.Ik;
import de.fhg.fokus.milenage.Mac;
import de.fhg.fokus.milenage.Op;
import de.fhg.fokus.milenage.Opc;
import de.fhg.fokus.milenage.Rand;
import de.fhg.fokus.milenage.Res;
import de.fhg.fokus.milenage.Sqn;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.kernel.KernelNotFoundException;

/**
  * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class ServerDigestAKA
{
    DigestAKA digestAKA;

    public ServerDigestAKA() throws KernelNotFoundException
    {
        digestAKA = new DigestAKA();
    }

    public ServerDigestAKA(String kernelClassName)
            throws KernelNotFoundException
    {
        digestAKA = new DigestAKA(kernelClassName);
    }

    public Opc generateOp_c(AuthKey secretKey, Op opObj)
            throws InvalidKeyException
    {
        return digestAKA.generateOp_c(secretKey, opObj);
    }

    public Mac generateMACA(AuthKey secretKey, Rand randObj, Opc op_cObj,
            Sqn sqnObj, Amf amfObj) throws InvalidKeyException
    {
        return digestAKA.fOne(secretKey, randObj, op_cObj, sqnObj, amfObj);
    }
    
    public Mac generateXMACS(AuthKey secretKey, Rand randObj, Opc op_cObj,
            Sqn sqnObj) throws InvalidKeyException, CoDecException
    {
        return digestAKA.fOneStar(secretKey, randObj, op_cObj, sqnObj);
    }
    
    public Res generateXRES(String authScheme,AuthKey secretKey, Rand randObj, Opc op_cObj) throws InvalidKeyException
    {
        return digestAKA.fTwo(authScheme,secretKey, randObj, op_cObj);
    }
    
    public Ck generateCipherKey(AuthKey secretKey, Rand randObj, Opc op_cObj) throws InvalidKeyException
    {
        return digestAKA.fThree(secretKey, randObj, op_cObj);
    }
    
    public Ik generateIntegrityKey(AuthKey secretKey, Rand randObj, Opc op_cObj) throws InvalidKeyException
    {
        return digestAKA.fFour(secretKey, randObj, op_cObj);
    }
    
    public Ak generateAnonoymityKey(AuthKey secretKey, Rand randObj, Opc op_cObj) throws InvalidKeyException
    {
        return digestAKA.fFive(secretKey, randObj, op_cObj);
    }
    
    public Ak generateSynchronizationAnonoymityKey(AuthKey secretKey, Rand randObj, Opc op_cObj) throws InvalidKeyException
    {
        return digestAKA.fFiveStar(secretKey, randObj, op_cObj);
    }
    
}
