/*
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

package de.fhg.fokus.hss.auth;

import java.net.Inet4Address;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

import de.fhg.fokus.hss.cx.CxConstants;
import de.fhg.fokus.hss.main.HSSProperties;

/**
 * @author Dragos Vingarzan vingarzan -at- fokus dot fraunhofer dot de 
 * contributions Adrian Popescu adp -at- fokus dot fraunhofer dot de
 */

public class DigestAKA
{
	public static final String AKAv1 = "Digest-AKAv1-MD5";
	public static final String AKAv2 = "Digest-AKAv2-MD5";
    
    private static final Logger logger = Logger.getLogger(DigestAKA.class);

	private static int getPower(int a, int b){
		int res = 1;
		for ( int i = 0; i < b; i++){
			res *= a; 
		}
		return res;
	}
	
	private static long bin2long(byte[] b){
    	long result = 0;
    	for (int i = 0; i < b.length; i++){
    		result <<= 8;
    		result |= (b[i] & 255);
    	}
    	return result;
    }
    
	private static byte[] incIND(byte[] ind, int octetLenInd, int amod8){
		int i = octetLenInd - 1;
		
		while (i >= 0){
			if (i == 0 && (amod8 != 0 && ind[i] == (byte)(getPower(2, amod8) - 1))){
				ind[i]=(byte)0;
				break;
			}
			else if (ind[i] == (byte)255){
				ind[i] = (byte)0;
				i--;
			}
			else{
				ind[i]++;
				break;
			}
		}
		return ind;
	}

	private static byte[] incSEQ(byte[] seq, int octetLenSeq, int amod8){
		
		int i = octetLenSeq - 1;
		int increment = 1;
		
		while (i >= 0){
			if (i == octetLenSeq - 1 && (amod8 != 0)){
				increment = getPower(2, amod8);
			}
			else{
				increment = 1;
			}
			if (seq[i] == (byte)(256 - increment)){
				seq[i] = (byte)0;
				i--;
			}
			else{
				seq[i] += increment;
				break;
			}
		}
		return seq;
		
	}
	
	private static byte[] reGenerateSQN(byte[] seq, byte[] ind, int octetLenSeq, int octetLenInd, int amod8){
		byte[] x = new byte[6];
		
		int k = x.length - 1;
		for (int i = octetLenInd - 1; i >= 0; i--){
			x[k] = ind[i];
			k--;
		}
		
		for (int i = octetLenSeq - 1; i >= 0; i--){
			if (i == octetLenSeq - 1 && amod8 != 0){
				x[k+1] = (byte) (x[k+1] + seq[i]);
			}
			else{
				x[k] = seq[i];
				k--;
			}
		}

		return x;
	}
	
	
	public static byte[] getNextSQN(byte[] sqn, int a){
    	
    	boolean startFlag = true;
    	
    	for (int i = 0; i < 6; i++){
    		if (sqn[i] != (byte)0){
    			startFlag = false;
    			break;
    		}
    	}
    		
    	byte[] ind = new byte[6];
    	byte[] seq = new byte[6];
    	
    	int octetLenInd = 0;
    	int octetLenSeq = 0;
    	
    	byte amod8 = (byte)(a % 8);
    	
    	octetLenInd = a / 8;
    	if (amod8 != 0){
    		octetLenInd++;
    	}
    	
    	octetLenSeq = (48 - a) / 8;
    	if (amod8 != 0){
    		octetLenSeq++;
    	}
    	
    	int k = octetLenInd - 1;
    	int i, j;
    	for (i = sqn.length - 1; i > sqn.length - 1 - octetLenInd; i--){
    		ind[k] = sqn[i];
    		k--;
    	}
    	
    	if (amod8 != 0){
    		ind[k+1] = (byte) (ind[k+1] & (getPower(2, amod8) - 1));
    		sqn[i+1] = (byte)(sqn[i+1] - ind[k+1]);
    		i++;
    	}
    		
    	k = octetLenSeq - 1;
    	for (j = i; j >= 0; j--){
    		seq[k] = sqn[j];
    		k--;
    	}
    	
    	if (startFlag){
    		incSEQ(seq, octetLenSeq, amod8);	
    	}
    	else{
    		incIND(ind, octetLenInd, amod8);
    		incSEQ(seq, octetLenSeq, amod8);
    	}
      	
    	return reGenerateSQN( seq, ind, octetLenSeq, octetLenInd, amod8);
    	
	}


   public static boolean SQNinRange(byte[] sqnMS, byte[] sqnHE, int indLen, int delta, int L){

    	// get the long values for sqnMS and sqnHE, these are needed for comparations operations 

	    long sqnMSLong = DigestAKA.bin2long(sqnMS);
    	long sqnHELong = DigestAKA.bin2long(sqnHE);
	    	
    	long seqMS = sqnMSLong >> indLen;
    	long seqHE = sqnHELong >> indLen;
	    	
    	// get the index
    	long mask = 0;
    	for (int i=0; i<indLen; i++){
    		mask <<= 1;
    		mask |= 1;
    	}
    	long index = mask & sqnHELong;
    	//System.out.println("index:" + index);
	    	
    	// C.2.1 from TS 33.102
    	if (seqHE - seqMS > delta)
    		return false;
	    	
    	// C.2.2 from TS 33.102
    	if (seqMS - seqHE > L)
    		return false;
	    	
    	if (seqHE <= seqMS)
    		return false;
	    	
    	return true;
    }
    
    
    public static AuthenticationVector getAuthenticationVector(int auth_scheme, byte[] secretKey, byte [] opC, 
    		byte [] amf, byte [] sqn) {
    
        // random bytes
        SecureRandom randomAccess;
		try {
			randomAccess = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
        byte[] randBytes = new byte[16];
    	randomAccess.setSeed(System.currentTimeMillis());
        randomAccess.nextBytes(randBytes);
    	
    	byte[] mac, xres, ck, ik, autn;
    	
		try {
			mac = Milenage.f1(secretKey,randBytes, opC, sqn, amf);
		
	        xres = Milenage.f2(secretKey, randBytes, opC);
	        ck = Milenage.f3(secretKey, randBytes, opC);
	        ik = Milenage.f4(secretKey, randBytes, opC); 
	        
	        autn = new byte[16];
	        if (HSSProperties.USE_AK){
	        	byte[] ak = Milenage.f5(secretKey, randBytes, opC);
	        	for (int i = 0; i < 6; i++){
	            	sqn[i] = (byte) (sqn[i] ^ ak[i]);
	            }	
	        }
		
		} 
		catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
		
        // compute autn
        int k = 0;
        for (int i = 0; i < 6; i++, k++)
        	autn[k] = sqn[i];
        for (int i = 0; i < 2; i++, k++)
          	autn[k] = amf[i];
        for (int i = 0; i < 8; i++, k++)
          	autn[k] = mac[i];

        // compute nonce
        byte [] nonce = new byte[32];
        k = 0;
        for (int i = 0; i < 16; i++, k++)
         	nonce[k] = randBytes[i];
        for (int i = 0; i < 16; i++, k++)
          	nonce[k] = autn[i];

        AuthenticationVector authenticationVector = null;
        if ((auth_scheme & CxConstants.Auth_Scheme_AKAv1) != 0){
           	//AKAv1
           	logger.debug("Authentication-Scheme: AKAv1!");
          	authenticationVector = new AuthenticationVector(auth_scheme, nonce, xres, ck, ik);
        }
        else if ((auth_scheme & CxConstants.Auth_Scheme_AKAv2) != 0){
           	// AKAv2
           	logger.debug("Authentication-Scheme: AKAv2!");
           	byte xresikck[] =  new byte[xres.length + ck.length + ik.length];
           	k = 0;
           	for (int i = 0; i < xres.length; i++, k++)
          		xresikck[k] = xres[i];
           	for (int i = 0; i < ik.length; i++, k++)
           		xresikck[k] = ik[i];
           	for (int i = 0; i < ck.length; i++, k++)
           		xresikck[k] = ck[i];
            
			byte xresv2[] = bin2base64(getHMacMD5(xresikck,"http-digest-akav2-password".getBytes()));
			byte ikv2[]	= getHMacMD5(ik,"http-digest-akav2-integritykey".getBytes());
			byte ckv2[]	= getHMacMD5(ck,"http-digest-akav2-cipherkey".getBytes());
           	authenticationVector = new AuthenticationVector(auth_scheme, nonce, xresv2, ckv2, ikv2);
        }
    	return authenticationVector;
    }

    private static byte[] base64=String.valueOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getBytes();
    private static byte[] bin2base64( byte[] from)
    {
    	if (from==null||from.length==0) return new byte[0];
    	int i,k;
    	int triplets,rest;
    	byte[] out;
    	int ptr=0;
    	int len = from.length;
    	triplets = len/3;
    	rest = len%3;
    	out = new byte[ triplets * 4 + 8 ];
    	
    	ptr = 0;
    	for(i=0;i<triplets*3;i+=3){
    		k = (from[i]&0xFC) / 4;
    		out[ptr++]=base64[k];

    		k = (from[i]&0x03) * 16;
    		k += (from[i+1]&0xF0)/16;
    		out[ptr++]=base64[k];

    		k = (from[i+1]&0x0F)*4;
    		k += (from[i+2]&0xC0)/64;
    		out[ptr++]=base64[k];

    		k = (from[i+2]&0x3F);
    		out[ptr++]=base64[k];
    	}
    	i=triplets*3;
    	switch(rest){
    		case 0:
    			break;
    		case 1:
    			k = (from[i]&0xFC)/4;
    			out[ptr++]=base64[k];

    			k = (from[i]&0x03)*16;
    			out[ptr++]=base64[k];

    			out[ptr++]='=';

    			out[ptr++]='=';
    			break;
    		case 2:
    			k = (from[i]&0xFC)/4;
    			out[ptr++]=base64[k];

    			k = (from[i]&0x03)*16;
    			k +=(from[i+1]&0xF0)/16;
    			out[ptr++]=base64[k];

    			k = (from[i+1]&0x0F)*4;
    			out[ptr++]=base64[k];

    			out[ptr++]='=';
    			break;
    	}    	
        byte[] x = new byte[ptr];
        System.arraycopy(out,0,x,0,ptr);
    	return x;
    }    

	private static byte[] getHMacMD5(byte[] secret, byte[] data)
    {
		Key key = new SecretKeySpec(secret, 0, secret.length, "HMacMD5");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacMD5");
			mac.init(key);
			mac.update(data);
			return mac.doFinal();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }    
    
}
