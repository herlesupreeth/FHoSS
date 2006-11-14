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
package de.fhg.fokus.hss.server.cx.op;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.fhg.fokus.cx.AuthenticationVector;
import de.fhg.fokus.cx.CxAuthDataResponse;
import de.fhg.fokus.cx.datatypes.PublicIdentity;
import de.fhg.fokus.cx.exceptions.DiameterException;
import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.server.cx.util.AKAUtil;
import de.fhg.fokus.milenage.Ak;
import de.fhg.fokus.milenage.Amf;
import de.fhg.fokus.milenage.AuthKey;
import de.fhg.fokus.milenage.Auts;
import de.fhg.fokus.milenage.Mac;
import de.fhg.fokus.milenage.Nonce;
import de.fhg.fokus.milenage.Op;
import de.fhg.fokus.milenage.Opc;
import de.fhg.fokus.milenage.Rand;
import de.fhg.fokus.milenage.SIPAuthorization;
import de.fhg.fokus.milenage.SimpleSqn;
import de.fhg.fokus.milenage.Sqn;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.kernel.KernelNotFoundException;
import de.fhg.fokus.milenage.server.ServerDigestAKA;


import java.net.Inet4Address;
import java.net.URI;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.ArrayList;


/**
 * This class represents cx specific authentication operations. It implements the 
 * processing of MAR.
 * @author Andre Charton  (dev -at- open-ims dot org)
 */
public class AuthCxOperation extends CxOperation
{
    /** Logger */
    private static final Logger LOGGER =
        Logger.getLogger(AuthCxOperation.class);
    /** name of serving call session control function */    
    private String scscfName;
    /** authentication vector */
    private AuthenticationVector authenticationVector;
    /** number of authentication vectors */
    private Long numberOfAuthVectors;

    /**
     * constructor
     * @param _publicIdentity public identity
     * @param _privateUserIdentity private user identity
     * @param _numberOfAuthVectors number of authentication vectors
     * @param _authenticationVector authentication vector 
     * @param _scscfName serving call session control functions name
     */     
    public AuthCxOperation(
        PublicIdentity _publicIdentity, URI _privateUserIdentity,
        Long _numberOfAuthVectors, AuthenticationVector _authenticationVector,
        String _scscfName)
    {
        LOGGER.debug("entering");
        this.privateUserIdentity = _privateUserIdentity;
        this.publicIdentity = _publicIdentity;
        this.authenticationVector = _authenticationVector;
        this.numberOfAuthVectors = _numberOfAuthVectors;
        this.scscfName = _scscfName;
        this.addPropertyChangeListener(this);
        LOGGER.debug("exiting");
    }

    /**
     * It loads or generates the required authentication data and 
     * returns authentication response
     * @throws DiameterException
     * @return an authentication data response object containing authentication data
     * @see de.fhg.fokus.hss.server.cx.CxOperation#execute()
     */
    public Object execute() throws DiameterException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("entering");
            LOGGER.debug(this);
        }

        CxAuthDataResponse authDataResponse = null;

        try
        {
            loadUserProfile();

           
            // Check the Server name
            if (userProfil.getImpi().getScscfName().equals(scscfName) == false)
            {
                userProfil.getImpi().setScscfName(scscfName);
                markUpdateUserProfile();
            }

            ArrayList authVector = null;

            // Generate the Authentiaction Vectors ...
            if (authenticationVector != null)
            {
                // ... for synchronistation
                authVector = handleSynchFailure();
            }
            else
            {
                // ... for authentication
                authVector = generateVector();
            }

            authDataResponse =
                new CxAuthDataResponse(ResultCode._DIAMETER_SUCCESS, true);
            authDataResponse.setAuthenticationVectors(authVector);
            updateUserProfile();
            LOGGER.debug("exiting");
        }
        finally
        {
        	if(getUserProfil() != null){
            getUserProfil().closeSession();
        	}
        }

        return authDataResponse;
    }

    /**
     * This method generates a list of authentication vectors
     * @return list of authentication vectors
     */
    public ArrayList generateVector()
    {
        LOGGER.debug("entering");

        ArrayList vector = null;
        Impi impi = null;

        try
        {
            impi = userProfil.getImpi();

            // create per init answer vector
            vector = new ArrayList(numberOfAuthVectors.intValue());

            ServerDigestAKA digestAKA = new ServerDigestAKA();

            // Collection values
            SecureRandom randomAccess = SecureRandom.getInstance("SHA1PRNG");
            AuthKey secretKey = new AuthKey(impi.getSkey());
            Amf amf = new Amf(impi.getAmf());
            Op operator = new Op(impi.getOperatorId());
            Opc opc = digestAKA.generateOp_c(secretKey, operator);
            String authScheme = impi.getAuthScheme();
            //For the sake of Early IMS
            Inet4Address ip = impi.getIP();

            String sqnString = new String(impi.getSqn());
            Sqn sqn;

            if (sqnString == null)
            {
                sqn = SimpleSqn.getNewInstance();
            }
            else
            {
                sqn = new SimpleSqn(sqnString);
            }

            for (long ix = 0; ix < numberOfAuthVectors.intValue(); ix++)
            {
                sqn = sqn.calculateNextSqn();
                vector.add(
                    AKAUtil.generateAuthenticationVector(
                        randomAccess, secretKey, amf, opc, sqn, (long) ix,
                        digestAKA, authScheme,ip));
            }

            impi.setSqn(sqn.getAsString());
            markUpdateUserProfile();
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error(this, e);
        }
        catch (CoDecException e)
        {
            LOGGER.error(this, e);
        }
        catch (KernelNotFoundException e)
        {
            LOGGER.error(this, e);
        }
        catch (InvalidKeyException e)
        {
            LOGGER.error(this, e);
        }
        catch (Exception e)
        {
            // Check impi
            if (impi.getAmf() == null)
            {
                throw new NullPointerException("Missing AMF value.");
            }

            if (impi.getSkey() == null)
            {
                throw new NullPointerException("Missing Secret Key value.");
            }

            if (impi.getAuthScheme() == null)
            {
                throw new NullPointerException(
                    "Missing Authentication Scheme.");
            }

            if (impi.getOperatorId() == null)
            {
                throw new NullPointerException("Missing Operator ID.");
            }
        }

        LOGGER.debug("exiting");

        return vector;
    }

    /**
     * It handles synchronization failures
     * @throws DiameterException
     * @return a list of authentication vector
     */
    public ArrayList handleSynchFailure() throws DiameterException
    {
        LOGGER.debug("entering");

        try
        {
            ServerDigestAKA digestAKA = new ServerDigestAKA();
            SIPAuthorization sipAuthorization =
                new SIPAuthorization(authenticationVector.sipAuthorization);

            Nonce nonce = sipAuthorization.getNonce();
            Auts auts = sipAuthorization.getAuts();
            Ak ak = null;
            Rand rand = nonce.getRand();
            AuthKey key = new AuthKey(userProfil.getImpi().getSkey());
            Opc opc =
                digestAKA.generateOp_c(
                    key, new Op(userProfil.getImpi().getOperatorId()));
            String sqnHeString = userProfil.getImpi().getSqn();
            String amf = userProfil.getImpi().getAmf();
            String authScheme = userProfil.getImpi().getAuthScheme();
            //For the sake of Early IMS
            Inet4Address ip = userProfil.getImpi().getIP();
            Sqn sqnHe = null;

            if (sqnHeString == null)
            {
                sqnHe = SimpleSqn.getNewInstance();
            }
            else
            {
                sqnHe = new SimpleSqn(sqnHeString);
            }

            sqnHe = sqnHe.calculateNextSqn();

            if (Ak.USE_AK)
            {
                ak = digestAKA.generateSynchronizationAnonoymityKey(
                        key, rand, opc);
            }

            Sqn sqnMs = auts.extractSqn(ak);

            if (!sqnMs.isInRange(sqnHe))
            {
                Mac xMacS = digestAKA.generateXMACS(key, rand, opc, sqnMs);
                Mac macS = auts.getMac();
                LOGGER.debug("X-MACS: " + xMacS);
                LOGGER.debug("MACS:   " + macS);

                if (!xMacS.equals(macS))
                {
                    throw new UnableToComply();
                }

                sqnHe = sqnMs;
                sqnHe = sqnHe.calculateNextSqn();
            }

            AuthenticationVector newAv =
                AKAUtil.generateAuthenticationVector(
                    rand, key, new Amf(amf), opc, sqnHe, 0, digestAKA,
                    authScheme, ip);

            ArrayList avs = new ArrayList();
            avs.add(newAv);

            // update Cxdata
            userProfil.getImpi().setSqn(sqnHe.getAsString());
            markUpdateUserProfile();
            LOGGER.debug("exiting");

            return avs;
        }
        catch (CoDecException e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }
        catch (InvalidKeyException e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }
        catch (KernelNotFoundException e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
            throw new UnableToComply();
        }
    }
}
