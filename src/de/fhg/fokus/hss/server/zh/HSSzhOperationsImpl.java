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

import java.net.Inet4Address;
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
import de.fhg.fokus.hss.main.HSSProperties;
import de.fhg.fokus.hss.model.GussBO;
import de.fhg.fokus.hss.model.Impi;
import de.fhg.fokus.hss.model.Impu;
import de.fhg.fokus.hss.model.UserSecSettings;
import de.fhg.fokus.hss.util.HibernateUtil;
import de.fhg.fokus.security.auth.DigestAKA;
import de.fhg.fokus.security.auth.HexCoDec;
import de.fhg.fokus.security.auth.Milenage;
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

import de.fhg.fokus.security.auth.*;

/**
 * Implementation of the HSS Zh Operations.
 *
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class HSSzhOperationsImpl implements ZhOperations{
	
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(HSSzhOperationsImpl.class);
    /** private user identity uri*/    
    private URI privateUserIdentity;
    /** number of authentication items */
    private Long numberAuthItems;
    /** authentication vector */
    private AuthenticationVector sipAuthDataItem;
    /** private user identity object used for hibernate transaction*/
    private Impi impi;

    /**
     * Implementation of zh-MAR-Auth Method.
     * @return an object containing the zh specific authentication data
     * @throws DiameterException
     */
    public ZhAuthDataResponse zhAuthData(URI _privateUserIdentity, Long _numberAuthItems,
    		AuthenticationVector _sipAuthDataItem, String _serverName) throws DiameterException{
    	
        LOGGER.debug("entering");
        this.privateUserIdentity = _privateUserIdentity;
        this.numberAuthItems = _numberAuthItems;
        this.sipAuthDataItem = _sipAuthDataItem;

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(this);
        }

        ZhAuthDataResponse authDataResponse = new ZhAuthDataResponse(ResultCode._DIAMETER_SUCCESS);

        try
        {
            HibernateUtil.beginTransaction();
            this.impi = (Impi) HibernateUtil.getCurrentSession()
            	.createQuery("select impi from de.fhg.fokus.hss.model.Impi as impi where impi.impiString = ?")
                .setString(0, privateUserIdentity.getPath())
                .uniqueResult();

            if (impi == null){
                throw new UnknownImpi();
            }

            ArrayList authVector = null;

            // Generate the Authentiaction Vectors ...
            if (sipAuthDataItem != null){
                // ... for synchronistation
                authVector = performSynchronization();
            }
            else{
                // ... for authentication
                authVector = generateAuthenticationVectors();
            }
            authDataResponse.setAuthenticationVectors(authVector);

            Guss guss = generateGuss();
            authDataResponse.setGuss(guss);
            
        }
        finally{
        	HibernateUtil.commitTransaction();
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

        boolean isGba = ((int) impi.getUiccType().byteValue() == 0) ? false : true;
        Integer keylifeTime = impi.getKeyLifeTime();
        BsfInfo bsfInfo = new BsfInfo();

        if (!isGba){
            bsfInfo.setUiccType("1");
        }

        if (keylifeTime != null){
            bsfInfo.setLifeTime(keylifeTime.intValue());
        }
        guss.setBsfInfo(bsfInfo);

        // Get Uids
        Uids uids = new Uids();
        Iterator it = impi.getImpus().iterator();

        while (it.hasNext()){
            Impu impu = (Impu) it.next();
            TuidsItem item = new TuidsItem();
            item.setUid(impu.getSipUrl());
            uids.addTuidsItem(item);
        }

        // Iterate about the uss entrys
        UssList ussList = new UssList();
        UserSecSettings impiUss = null;
        Iterator itUss = HibernateUtil.getCurrentSession().createQuery("from de.fhg.fokus.hss.model.UserSecSettings as uss where uss.impiId = ?")
        	.setInteger(0, impi.getImpiId()).list().iterator();

        while (itUss.hasNext()){
            impiUss = (UserSecSettings) itUss.next();
            TussListItem ussItem = new TussListItem();
            Uss uss = new Uss();
            uss.setId(GussBO.getIdByType(impiUss.getUssType()));
            uss.setType(impiUss.getUssType());
            uss.setNafGroup(impiUss.getNafGroup());

            Integer flags = impiUss.getFlag();
            Flags ussFlags = new Flags();
            uss.setFlags(ussFlags);

            if ((flags != null) && (flags.intValue() > 0)){
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
    private void buildFlags(int value, Flags flagList){
        int i = value;
        int j = 0;

        while (i > 0){
            if ((i % 2) == 1){
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
    public ArrayList generateAuthenticationVectors(){
        LOGGER.debug("entering");

        ArrayList vectorList = null;
        try{
        	
            vectorList = new ArrayList(numberAuthItems.intValue());

            HexCoDec codec;
            codec = new HexCoDec();
            byte [] secretKey = codec.decode(impi.getSkey());
            byte [] amf = codec.decode(impi.getAmf());

            // op and generate opC	
            byte [] op = codec.decode(impi.getOperatorId());
            byte [] opC = Milenage.generateOpC(secretKey, op);
            
            String authScheme = impi.getAuthScheme();
            Inet4Address ip = impi.getIP();
            byte [] sqn = codec.decode(impi.getSqn());
        	
            if (authScheme.equalsIgnoreCase("Digest-MD5")){
            	// Authentication Scheme is Digest-MD5
            	LOGGER.debug("Auth-Scheme is Digest-MD5");
                SecureRandom randomAccess = SecureRandom.getInstance("SHA1PRNG");

                for (long ix = 0; ix <numberAuthItems; ix++){
                    byte[] randBytes = new byte[16];
                	randomAccess.setSeed(System.currentTimeMillis());
                    randomAccess.nextBytes(randBytes);
                    
                	secretKey = codec.decodePassword(impi.getSkey()).getBytes();
                	
                	AuthenticationVector aVector = new AuthenticationVector(authScheme, randBytes, secretKey);
                	vectorList.add(aVector);
                }
                impi.setSqn(codec.encode(sqn));
                HibernateUtil.getCurrentSession().update(impi);
            }
            else if (authScheme.equalsIgnoreCase("Digest-AKAv1-MD5") || authScheme.equalsIgnoreCase("Digest-AKAv2-MD5")){
            	// We have AKAv1 or AKAv2
            	LOGGER.debug("Auth-Scheme is Digest-AKA");
            	
                for (long ix = 0; ix < numberAuthItems; ix++)
                {
                	sqn = DigestAKA.getNextSQN(sqn, HSSProperties.IND_LEN);
        	        byte[] copySqnHe = new byte[6];
        	        int k = 0;
        	        for (int i = 0; i < 6; i++, k++){
                		copySqnHe[k] = sqn[i]; 
        	        }

                    vectorList.add(DigestAKA.getAuthenticationVector(authScheme, secretKey, opC, amf, sqn));
                }
                impi.setSqn(codec.encode(sqn));
                HibernateUtil.getCurrentSession().update(impi);            
            }
            
        }
        catch (NoSuchAlgorithmException e){
            LOGGER.error(this, e);
        }
        catch (InvalidKeyException e){
            LOGGER.error(this, e);
        }
        catch (Exception e){
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
        return vectorList;
    }

    /**
     * It handles the synchronization of the SQN-MS with the SQN-HE
     * @throws DiameterException
     * @return a list of authentication vectorList
     */
    public ArrayList performSynchronization() throws DiameterException
    {
    	LOGGER.info("Handling Synchronization between Mobile Station and Home Environment!");
        byte[] sipAuthorization = sipAuthDataItem.sipAuthorization;

        HexCoDec codec;
        codec = new HexCoDec();

        byte [] secretKey = codec.decode(impi.getSkey());
        byte [] amf = codec.decode(impi.getAmf());

        try {
            // get op and generate opC	
            byte [] op = codec.decode(impi.getOperatorId());
			byte [] opC = Milenage.generateOpC(secretKey, op);

			String authScheme = impi.getAuthScheme();
	        Inet4Address ip = impi.getIP();

	        // sqnHE - represent the SQN from the HSS
	        // sqnMS - represent the SQN from the client side
	        byte[] sqnHe = codec.decode(impi.getSqn());
	        sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);

	        byte [] nonce = new byte[32];
	        byte [] auts = new byte[14];
	        int k = 0;
	        for (int i = 0; i < 32; i++, k++){
        		nonce[k] = sipAuthorization[i]; 
	        }
	        k = 0;
	        for (int i = 32; i < 46; i++, k++){
        		auts[k] = sipAuthorization[i]; 
	        }
	        
	        byte [] rand = new byte[16];
	        k = 0;
	        for (int i = 0; i < 16; i++, k++){
        		rand[k] = nonce[i]; 
	        }

	        byte[] ak = null;
	        if (HSSProperties.USE_AK){
                ak = Milenage.f5star(secretKey, rand, opC);
            }

	        byte [] sqnMs = new byte[6];
	        k = 0;
	        if (HSSProperties.USE_AK){
		        for (int i = 0; i < 6; i++, k++){
	        		sqnMs[k] = (byte) (auts[i] ^ ak[i]); 
		        }
		        LOGGER.warn("USE_AK is enabled and will be used in Milenage algorithm!");
	        }
	        else{
		        for (int i = 0; i < 6; i++, k++){
	        		sqnMs[k] = auts[i]; 
		        }
		        LOGGER.warn("USE_AK is NOT enabled and will NOT be used in Milenage algorithm!");
	        }
	        
	        if (DigestAKA.SQNinRange(sqnMs, sqnHe, HSSProperties.IND_LEN, HSSProperties.delta, HSSProperties.L)){
	        	LOGGER.info("The new generated SQN value shall be accepted on the client, abort synchronization!");
	        	k = 0;
	        	byte[] copySqnHe = new byte[6];
		        for (int i = 0; i < 6; i++, k++){
	        		copySqnHe[k] = sqnHe[i]; 
		        }
	        	
	            AuthenticationVector aVector = DigestAKA.getAuthenticationVector(authScheme, secretKey, opC, amf, copySqnHe);
	            ArrayList vectorsList = new ArrayList();
	            vectorsList.add(aVector);

	            // update Cxdata
	            impi.setSqn(codec.encode(sqnHe));
	            HibernateUtil.getCurrentSession().update(impi);
	            return vectorsList;	        	
	        }
	        	
        	byte xmac_s[] = Milenage.f1star(secretKey, rand, opC, sqnMs, amf);
        	byte mac_s[] = new byte[8];
        	k = 0;
        	for (int i = 6; i < 14; i++, k++){
        		mac_s[k] = auts[i];
        	}
	        	
        	for (int i = 0; i < 8; i ++)
        		if (xmac_s[i] != mac_s[i]){
        			LOGGER.error("XMAC and MAC are different! User not authorized in performing synchronization!");
        			throw new DiameterBaseException(5012);
               }

            sqnHe = sqnMs;
            sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);
     		LOGGER.info("Synchronization of SQN_HE with SQN_MS was completed successfully!");
	        
	        byte[] copySqnHe = new byte[6];
	        k = 0;
	        for (int i = 0; i < 6; i++, k++){
        		copySqnHe[k] = sqnHe[i]; 
	        }
            AuthenticationVector aVector = DigestAKA.getAuthenticationVector(authScheme, secretKey, opC, amf, copySqnHe);
            ArrayList vectorsList = new ArrayList();
            vectorsList.add(aVector);

            // update Cxdata
            impi.setSqn(codec.encode(sqnHe));
            HibernateUtil.getCurrentSession().update(impi);
            return vectorsList;
            
		} 
        catch (InvalidKeyException e) {
			e.printStackTrace();
		}
        catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        return new ArrayList();
    }

    /** 
     *  a string converter
     *  @return the string
     */
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
