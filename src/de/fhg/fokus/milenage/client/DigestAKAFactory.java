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
package de.fhg.fokus.milenage.client;

import java.security.InvalidKeyException;
import java.util.Properties;

import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.kernel.KernelNotFoundException;

/**
 * Utility class for creation and update of client processing
 * 
 * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class DigestAKAFactory
{
    private static DigestAKAFactory instance;

    private DigestAKAFactory()
    {
    }

    /**
     * Method for singelton usage
     * 
     * @return a DigestAKAFactory
     */
    public static DigestAKAFactory getInstance()
    {
        if (instance == null)
        {
            instance = new DigestAKAFactory();
        }
        return instance;
    }

    /**
     * Creates new client processing from properties
     * 
     * @param props
     *            de.fhg.fokus.milenage.AMF: the authentication management field
     *            in hex format(optional)
     *             
     *            de.fhg.fokus.milenage.OP: the Operator
     *            Variant Algorithm Configuration Field (conditional)
     *            
     *            de.fhg.fokus.milenage.OPC: the encrypted OP value in hex
     *            format(conditional) 
     *            
     *            de.fhg.fokus.milenage.SQN: the Sequence
     *            Number as long (required) de.fhg.fokus.milenage.SECRET_KEY:
     *            the password based secret key (required)
     *            
     *            de.fhg.fokus.milenage.USE_AK: flag for indication of usage of
     *            Anonymity Key as boolean(optional)
     *            
     *            de.fhg.fokus.milenage.SQN_RANGE: offset for synchronization of
     *            SQN's between client and server as long
     *            
     *            de.fhg.fokus.ims.PRIVATEID: private Id of a user in NAI format
     *            (required) 
     *            
     *            de.fhg.fokus.ims.PUBLICID: public Id of a user (SIP
     *            URI or TEL URL) (required)
     *            
     *            de.fhg.fokus.milenage.KERNELCLASSNAME: a class name of a
     *            Kernel interface implementation to be used by the algorithm
     *            (optional)
     * @return new client processing
     * @throws CoDecException
     * @throws InvalidKeyException
     * @throws KernelNotFoundException
     */
    public ClientProcessing createClientProcessing(Properties props)
            throws CoDecException, InvalidKeyException, KernelNotFoundException
    {
        return ClientProcessing.getInstance(props);
    }
}