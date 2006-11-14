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
package de.fhg.fokus.hss.server.zh;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.fhg.fokus.cx.exceptions.base.UnableToComply;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.model.GussBO;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.Impu;
import de.fhg.fokus.hss.model.UserSecSettings;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.milenage.Ak;
import de.fhg.fokus.milenage.Amf;
import de.fhg.fokus.milenage.AuthKey;
import de.fhg.fokus.milenage.Autn;
import de.fhg.fokus.milenage.Auts;
import de.fhg.fokus.milenage.Ck;
import de.fhg.fokus.milenage.Ik;
import de.fhg.fokus.milenage.Mac;
import de.fhg.fokus.milenage.Nonce;
import de.fhg.fokus.milenage.Op;
import de.fhg.fokus.milenage.Opc;
import de.fhg.fokus.milenage.Rand;
import de.fhg.fokus.milenage.Res;
import de.fhg.fokus.milenage.SIPAuthorization;
import de.fhg.fokus.milenage.SimpleSqn;
import de.fhg.fokus.milenage.Sqn;
import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.kernel.KernelNotFoundException;
import de.fhg.fokus.milenage.server.ServerDigestAKA;
import de.fhg.fokus.zh.AuthenticationVector;
import de.fhg.fokus.zh.ZhAuthDataResponse;
import de.fhg.fokus.zh.ZhOperations;
import de.fhg.fokus.zh.data.BsfInfo;
import de.fhg.fokus.zh.data.Flags;
import de.fhg.fokus.zh.data.Guss;
import de.fhg.fokus.zh.data.TflagsItem;
import de.fhg.fokus.zh.data.TuidsItem;
import de.fhg.fokus.zh.data.TussListItem;
import de.fhg.fokus.zh.data.Uids;
import de.fhg.fokus.zh.data.Uss;
import de.fhg.fokus.zh.data.UssList;
import de.fhg.fokus.zh.exceptions.DiameterBaseException;
import de.fhg.fokus.zh.exceptions.DiameterException;
import de.fhg.fokus.zh.exceptions.UnknownImpi;


/**
 * Implementation of the HSS Zh Operations.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HSSzhOperationsImpl implements ZhOperations
{
    /** logger */
    private static final Logger LOGGER =
        Logger.getLogger(HSSzhOperationsImpl.class);
    /** private user identity uri*/    
    private URI privateUserIdentity;
    /** number of authentication items */
    private Long numberAuthItems;
    /** authentication vector */
    private AuthenticationVector sipAuthDataItem;
    /** private user identity object used for hibernate transaction*/
    private Impi impi;
    /** hibernate session */
    private Session session;

    /**
     * Implementation of zh-MAR-Auth Method.
     * @return an object containing the zh specific authentication data
     * @throws DiameterException
     */
    public ZhAuthDataResponse zhAuthData(
        URI _privateUserIdentity, Long _numberAuthItems,
        AuthenticationVector _sipAuthDataItem, String _serverName)
        throws DiameterException
    {
        LOGGER.debug("entering");
        this.privateUserIdentity = _privateUserIdentity;
        this.numberAuthItems = _numberAuthItems;
        this.sipAuthDataItem = _sipAuthDataItem;

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(this);
        }

        ZhAuthDataResponse authDataResponse =
            new ZhAuthDataResponse(ResultCode._DIAMETER_SUCCESS);

        try
        {
            this.session = HibernateUtil.currentSession();
            this.impi =
                (Impi) session.createQuery(
                    "select impi from de.fhg.fokus.hss.model.Impi as impi where impi.impiString = ?")
                              .setString(0, privateUserIdentity.getPath())
                              .uniqueResult();

            if (impi == null)
            {
                throw new UnknownImpi();
            }

            ArrayList authVector = null;

            // Generate the Authentiaction Vectors ...
            if (sipAuthDataItem != null)
            {
                // ... for synchronistation
                authVector = handleSynchFailure();
            }
            else
            {
                // ... for authentication
                authVector = generateVector();
            }

            authDataResponse.setAuthenticationVectors(authVector);

            Guss guss = generateGuss();

            authDataResponse.setGuss(guss);
        }
        finally
        {
            HibernateUtil.closeSession();
        }

        LOGGER.debug("exiting");

        return authDataResponse;
    }

    /**
     * It generates the GUSS
     * @return the generated GUSS
     */
    private Guss generateGuss()
    {
        LOGGER.debug("entering");

        Guss guss = new Guss();
        guss.setId(impi.getImpiString());

        boolean isGba =
            ((int) impi.getUiccType().byteValue() == 0) ? false : true;
        Integer keylifeTime = impi.getKeyLifeTime();
        BsfInfo bsfInfo = new BsfInfo();

        if (!isGba)
        {
            bsfInfo.setUiccType("1");
        }

        if (keylifeTime != null)
        {
            bsfInfo.setLifeTime(keylifeTime.intValue());
        }

        guss.setBsfInfo(bsfInfo);

        // Get Uids
        Uids uids = new Uids();
        Iterator it = impi.getImpus().iterator();

        while (it.hasNext())
        {
            Impu impu = (Impu) it.next();
            TuidsItem item = new TuidsItem();
            item.setUid(impu.getSipUrl());
            uids.addTuidsItem(item);
        }

        // Iterate about the uss entrys
        UssList ussList = new UssList();

        UserSecSettings impiUss = null;
        Iterator itUss =
            session.createQuery(
                "from de.fhg.fokus.hss.model.UserSecSettings as uss where uss.impiId = ?")
                   .setInteger(0, impi.getImpiId()).list().iterator();

        while (itUss.hasNext())
        {
            impiUss = (UserSecSettings) itUss.next();

            TussListItem ussItem = new TussListItem();
            Uss uss = new Uss();
            uss.setId(GussBO.getIdByType(impiUss.getUssType()));
            uss.setType(impiUss.getUssType());
            uss.setNafGroup(impiUss.getNafGroup());

            Integer flags = impiUss.getFlag();
            Flags ussFlags = new Flags();
            uss.setFlags(ussFlags);

            if ((flags != null) && (flags.intValue() > 0))
            {
                buildFlags(flags.intValue(), ussFlags);
            }

            uss.setUids(uids);
            ussItem.setUss(uss);
            ussList.addTussListItem(ussItem);
        }

        guss.setUssList(ussList);

        LOGGER.debug("exiting");

        return guss;
    }

    /**
     * it builds flags and saves them in the flaglist
     * @param value an integer value
     * @param flagList a flag list in which the generated flags are saved
     *
     */
    private void buildFlags(int value, Flags flagList)
    {
        int i = value;
        int j = 0;

        while (i > 0)
        {
            if ((i % 2) == 1)
            {
                TflagsItem flag = new TflagsItem();
                flag.setFlag((int) Math.pow(2, j));
                flagList.addTflagsItem(flag);
            }

            i /= 2;
            j++;
        }
    }

    /**
     * This method generates the authentication vectors sized by given paramter.
     * @return a list of Authentication vectors
     */
    public ArrayList generateVector()
    {
        LOGGER.debug("entering");

        ArrayList vector = null;

        try
        {
            // create per init answer vector
            vector = new ArrayList(numberAuthItems.intValue());

            ServerDigestAKA digestAKA = new ServerDigestAKA();

            // Collection values
            SecureRandom randomAccess = SecureRandom.getInstance("SHA1PRNG");
            AuthKey secretKey = new AuthKey(impi.getSkey());
            Amf amf = new Amf(impi.getAmf());
            Op operator = new Op(impi.getOperatorId());
            Opc opc = digestAKA.generateOp_c(secretKey, operator);
            String authScheme = impi.getAuthScheme();

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

            for (long ix = 0; ix < numberAuthItems; ix++)
            {
                sqn = sqn.calculateNextSqn();
                vector.add(
                    generateAuthenticationVector(
                        randomAccess, secretKey, amf, opc, sqn, (long) ix,
                        digestAKA, authScheme));
            }

            impi.setSqn(sqn.getAsString());

            Transaction tx = session.beginTransaction();
            session.update(impi);
            tx.commit();
            session.flush();
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

    /*
     * It handles synchronization failure
     * @return a list of authentication vectors
     * @throws DiameterException
     */
    public ArrayList handleSynchFailure() throws DiameterException
    {
        LOGGER.debug("entering");

        try
        {
            ServerDigestAKA digestAKA = new ServerDigestAKA();
            SIPAuthorization sipAuthorization =
                new SIPAuthorization(sipAuthDataItem.sipAuthorization);

            Nonce nonce = sipAuthorization.getNonce();
            Auts auts = sipAuthorization.getAuts();
            Ak ak = null;
            Rand rand = nonce.getRand();
            AuthKey key = new AuthKey(impi.getSkey());
            Opc opc = digestAKA.generateOp_c(key, new Op(impi.getOperatorId()));
            String sqnHeString = impi.getSqn();
            String amf = impi.getAmf();
            String authScheme = impi.getAuthScheme();
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
                generateAuthenticationVector(
                    rand, key, new Amf(amf), opc, sqnHe, 0, digestAKA,
                    authScheme);

            ArrayList avs = new ArrayList();
            avs.add(newAv);

            // update Cxdata
            impi.setSqn(sqnHe.getAsString());

            Transaction tx = session.beginTransaction();
            session.update(impi);
            tx.commit();
            session.flush();
            LOGGER.debug("exiting");

            return avs;
        }
        catch (CoDecException e)
        {
            LOGGER.error(this, e);
            throw new DiameterBaseException(UnableToComply.ERRORCODE);
        }
        catch (InvalidKeyException e)
        {
            LOGGER.error(this, e);
            throw new DiameterBaseException(UnableToComply.ERRORCODE);
        }
        catch (KernelNotFoundException e)
        {
            LOGGER.error(this, e);
            throw new DiameterBaseException(UnableToComply.ERRORCODE);
        }
        catch (Exception e)
        {
            LOGGER.error(this, e);
            throw new DiameterBaseException(UnableToComply.ERRORCODE);
        }
    }

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
     * @return the generated authentication vector
     * @throws CoDecException
     * @throws InvalidKeyException
     */ 
    private static AuthenticationVector generateAuthenticationVector(
        SecureRandom random, AuthKey secretKey, Amf amf, Opc opc, Sqn sqn,
        long pos, ServerDigestAKA digestAKA, String authScheme)
        throws CoDecException, InvalidKeyException
    {
        byte[] randBytes = new byte[Rand.BYTES_LENGTH];
        random.setSeed(System.currentTimeMillis());
        random.nextBytes(randBytes);

        Rand rand = new Rand(randBytes);

        return generateAuthenticationVector(
            rand, secretKey, amf, opc, sqn, pos, digestAKA, authScheme);
    }

    /**
     * It generates authentication vector with the help of provided arguments
     * @param rand an object which can generate some random value
     * @param secretKey secret key object
     * @param amf authentication management field object
     * @param opc operator specific parameter object
     * @param sqn sequence number object
     * @param pos position
     * @param digestAKA digestAKA object
     * @param authScheme authentication scheme
     * @return the generated authentication vector
     * @throws CoDecException
     * @throws InvalidKeyException
     */ 
    private static AuthenticationVector generateAuthenticationVector(
        Rand rand, AuthKey secretKey, Amf amf, Opc opc, Sqn sqn, long pos,
        ServerDigestAKA digestAKA, String authScheme)
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
            authScheme, sipAuthenticate, sipAuthorization, ck.getBytes(),
            ik.getBytes());
    }

    /** 
     *  a string converter
     *  @return the string
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(
            this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
