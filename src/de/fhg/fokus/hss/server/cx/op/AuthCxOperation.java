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
import de.fhg.fokus.hss.diam.Constants;
import de.fhg.fokus.hss.diam.ResultCode;
import de.fhg.fokus.hss.form.ImpiForm;
import de.fhg.fokus.hss.main.HSSProperties;
import de.fhg.fokus.hss.model.Impi;

import java.net.Inet4Address;
import java.net.URI;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.ArrayList;
import de.fhg.fokus.hss.util.*;
import de.fhg.fokus.security.auth.DigestAKA;
import de.fhg.fokus.security.auth.HexCoDec;
import de.fhg.fokus.security.auth.Milenage;

/**
 * This class represents cx specific authentication operations. It implements the 
 * processing of MAR.
 * @author Andre Charton  (dev -at- open-ims dot org)
 * 
 */

public class AuthCxOperation extends CxOperation{
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(AuthCxOperation.class);
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
    public AuthCxOperation( PublicIdentity _publicIdentity, URI _privateUserIdentity,
        Long _numberOfAuthVectors, AuthenticationVector _authenticationVector, String _scscfName){
    	
        this.privateUserIdentity = _privateUserIdentity;
        this.publicIdentity = _publicIdentity;
        this.authenticationVector = _authenticationVector;
        this.numberOfAuthVectors = _numberOfAuthVectors;
        this.scscfName = _scscfName;
        this.addPropertyChangeListener(this);
    }

    /**
     * It loads or generates the required authentication data and 
     * returns authentication response
     * @throws DiameterException
     * @return an authentication data response object containing authentication data
     * @see de.fhg.fokus.hss.server.cx.CxOperation#execute()
     */
    
    public Object execute() throws DiameterException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("AuthCXOperation, execute method!");
            LOGGER.debug(this);
        }

        CxAuthDataResponse authDataResponse = null;

        try{
        	HibernateUtil.beginTransaction();
            loadUserProfile();
            if (userProfil.getImpi().getScscfName().equals(scscfName) == false){
                userProfil.getImpi().setScscfName(scscfName);
                markUpdateUserProfile();
            }

            ArrayList authenticationVectors = null;

            // Generate the Authentiaction Vectors
            if (authenticationVector != null && authenticationVector.sipAuthorization != null){
                // perform synchronization for the SQN (SQN-hn = SQN-ms)
                authenticationVectors = performSynchronization();
            }
            else{
                // generate the authentication vectors
                authenticationVectors = generateAuthenticationVectors(authenticationVector);
            }
            if (authenticationVectors != null){
            	authDataResponse = new CxAuthDataResponse(ResultCode._DIAMETER_SUCCESS, true);
            	authDataResponse.setAuthenticationVectors(authenticationVectors);
            	updateUserProfile();
            }
            
        }
        finally{
        	HibernateUtil.commitTransaction();
        	HibernateUtil.closeSession();
        }

        return authDataResponse;
    }

    /**
     * This method generates the list of the authentication vectors
     * @return list of authentication vectors
     */
    public ArrayList generateAuthenticationVectors(AuthenticationVector receivedAuthVector){
        LOGGER.debug("Generation of Authentication Vectors");
        ArrayList vectorList = null;
        Impi impi = null;

        try{
            impi = userProfil.getImpi();
            vectorList = new ArrayList(numberOfAuthVectors.intValue());

            HexCoDec codec;
            codec = new HexCoDec();
            byte [] secretKey = codec.decode(impi.getSkey());
            byte [] amf = codec.decode(impi.getAmf());

            // op and generate opC	
            byte [] op = codec.decode(impi.getOperatorId());
            byte [] opC = Milenage.generateOpC(secretKey, op);
            
            String supportedAuthScheme = impi.getAuthScheme();
            Inet4Address ip = impi.getIP();
            byte [] sqn = codec.decode(impi.getSqn());
            
            for (int i=0; i < 6; i++)
            	System.out.println("SQNNNNNNNNNNNN:" + sqn[i]);
            
            LOGGER.debug("Auth-Scheme Supported by the HSS: " + supportedAuthScheme);
            String usedAuthScheme;
            if (receivedAuthVector != null && receivedAuthVector.authenticationScheme != null){
            	LOGGER.debug("Auth-Scheme asked by S-CSCF: " + receivedAuthVector.authenticationScheme);
            	usedAuthScheme = receivedAuthVector.authenticationScheme;
            	if (supportedAuthScheme.equalsIgnoreCase("any") == false && supportedAuthScheme.equalsIgnoreCase(usedAuthScheme) == false){
            		throw new NoSuchAlgorithmException();
            	}
            }
            else{
            	usedAuthScheme = supportedAuthScheme;
            	if (usedAuthScheme.equalsIgnoreCase("any")){
            		// the default one
            		usedAuthScheme = Constants.AuthScheme.AUTH_SCHEME_AKAv1;
            	}
            }
            
            // MD5 Authentication Scheme
            if (usedAuthScheme.equalsIgnoreCase("Digest-MD5")){
            	// Authentication Scheme is Digest-MD5
            	LOGGER.debug("Auth-Scheme is Digest-MD5");
                SecureRandom randomAccess = SecureRandom.getInstance("SHA1PRNG");

                for (long ix = 0; ix < numberOfAuthVectors.intValue(); ix++){
                    byte[] randBytes = new byte[16];
                	randomAccess.setSeed(System.currentTimeMillis());
                    randomAccess.nextBytes(randBytes);
                    
                	secretKey = codec.decodePassword(impi.getSkey()).getBytes();
                	
                	AuthenticationVector aVector = new AuthenticationVector(usedAuthScheme, randBytes, secretKey);
                	vectorList.add(aVector);
                }
            	markUpdateUserProfile();
            	return vectorList;
            }
            else if (usedAuthScheme.equalsIgnoreCase(Constants.AuthScheme.AUTH_SCHEME_AKAv1) || 
            		usedAuthScheme.equalsIgnoreCase(Constants.AuthScheme.AUTH_SCHEME_AKAv2)){
            	// We have AKAv1 or AKAv2
            	LOGGER.debug("Auth-Scheme is Digest-AKA");
            	
                for (long ix = 0; ix < numberOfAuthVectors.intValue(); ix++){
                	sqn = DigestAKA.getNextSQN(sqn, HSSProperties.IND_LEN);
        	        byte[] copySqnHe = new byte[6];
        	        int k = 0;
        	        for (int i = 0; i < 6; i++, k++){
                		copySqnHe[k] = sqn[i]; 
        	        }

                	AuthenticationVector authenticationVector = DigestAKA.getAuthenticationVector(usedAuthScheme, ip,
                			secretKey, opC, amf, copySqnHe);
                    vectorList.add(authenticationVector);
                }
                impi.setSqn(codec.encode(sqn));
                markUpdateUserProfile();
            }
            else throw new NoSuchAlgorithmException("The " + usedAuthScheme + " scheme is not supported on the HSS!");
            
            /* EarlyIMS to be implemented ...*/
        }
        catch (NoSuchAlgorithmException e){
            LOGGER.error(this, e);
        }
        catch (InvalidKeyException e){
            LOGGER.error(this, e);
        }
        catch (Exception e){
        	e.printStackTrace();
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
        byte[] sipAuthorization = authenticationVector.sipAuthorization;

        HexCoDec codec;
        codec = new HexCoDec();
        Impi impi = userProfil.getImpi();

        byte [] secretKey = codec.decode(impi.getSkey());
        byte [] amf = codec.decode(impi.getAmf());

        try {
            // get op and generate opC	
            byte [] op = codec.decode(impi.getOperatorId());
			byte [] opC = Milenage.generateOpC(secretKey, op);
			//System.out.println("opC is: " + codec.encode(opC));
	        String authScheme = impi.getAuthScheme();
	        Inet4Address ip = impi.getIP();

	        // sqnHE - represent the SQN from the HSS
	        // sqnMS - represent the SQN from the client side
	        byte[] sqnHe = codec.decode(impi.getSqn());
	        //System.out.println("SQN_He is before increment: " + codec.encode(sqnHe));
	        sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);
	        //System.out.println("SQN_He is after increment: " + codec.encode(sqnHe));

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
	        //System.out.println("AUTS is: " + codec.encode(auts));
	        
	        byte [] rand = new byte[16];
	        k = 0;
	        for (int i = 0; i < 16; i++, k++){
        		rand[k] = nonce[i]; 
	        }
	        //System.out.println("Rand is: " + codec.encode(rand));

	        byte[] ak = null;
	        if (HSSProperties.USE_AK){
                ak = Milenage.f5star(secretKey, rand, opC);
            }
	        //System.out.println("AK is: " + codec.encode(ak));
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
	        //System.out.println("SQN_MS is: " + codec.encode(sqnMs));
	        //System.out.println("SQN_HE is: " + codec.encode(sqnHe));
	        
	        if (DigestAKA.SQNinRange(sqnMs, sqnHe, HSSProperties.IND_LEN, HSSProperties.delta, HSSProperties.L)){
	        	LOGGER.info("The new generated SQN value shall be accepted on the client, abort synchronization!");
	        	k = 0;
	        	byte[] copySqnHe = new byte[6];
		        for (int i = 0; i < 6; i++, k++){
	        		copySqnHe[k] = sqnHe[i]; 
		        }
	        	
	            AuthenticationVector aVector = DigestAKA.getAuthenticationVector(authScheme, ip, secretKey, opC, amf, copySqnHe);
	            ArrayList vectorsList = new ArrayList();
	            vectorsList.add(aVector);

	            // update Cxdata
	            userProfil.getImpi().setSqn(codec.encode(sqnHe));
	            markUpdateUserProfile();
	            return vectorsList;	        	
	        }
	        	
        	byte xmac_s[] = Milenage.f1star(secretKey, rand, opC, sqnMs, amf);
        	byte mac_s[] = new byte[8];
        	k = 0;
        	for (int i = 6; i < 14; i++, k++){
        		mac_s[k] = auts[i];
        	}
        	//System.out.println("xmac_s is: " + codec.encode(xmac_s));
        	//System.out.println("mac_s is: " + codec.encode(mac_s));
	        	
        	for (int i = 0; i < 8; i ++)
        		if (xmac_s[i] != mac_s[i]){
        			LOGGER.error("XMAC and MAC are different! User not authorized in performing synchronization!");
        			throw new UnableToComply();
               }

            sqnHe = sqnMs;
            sqnHe = DigestAKA.getNextSQN(sqnHe, HSSProperties.IND_LEN);
     		LOGGER.info("Synchronization of SQN_HE with SQN_MS was completed successfully!");
	        
	        byte[] copySqnHe = new byte[6];
	        k = 0;
	        for (int i = 0; i < 6; i++, k++){
        		copySqnHe[k] = sqnHe[i]; 
	        }
            AuthenticationVector aVector = DigestAKA.getAuthenticationVector(authScheme, ip, secretKey, opC, amf, copySqnHe);
            ArrayList vectorsList = new ArrayList();
            vectorsList.add(aVector);

            // update Cxdata
            userProfil.getImpi().setSqn(codec.encode(sqnHe));
            markUpdateUserProfile();
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
}
