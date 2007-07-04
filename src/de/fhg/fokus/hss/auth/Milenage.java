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

import java.security.InvalidKeyException;

/**
 * @author Sizhong Liu, sli -at- fokus dot fraunhofer dot de
 * contributions: Dragos Vingarzan vingarzan -at- fokus dot fraunhofer dot de
 * 				  Adrian Popescu adp -at- fokus dot fraunhofer dot de	
 */

public class Milenage {
    private static Rijndael32Bit kernel = new Rijndael32Bit();;

	public static byte[] f1(byte[] secretKey, byte[] rand, byte[] op_c,
            byte[] sqn, byte[] amf) throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] in1 = new byte[16];
        byte[] out1 = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < rand.length && i<op_c.length; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);

        temp = kernel.encrypt(rijndaelInput);

        // expand sqn and amf into 128 bit value
        for (int i = 0; i < sqn.length; i++)
        {
            in1[i] = sqn[i];
            in1[i + 8] = sqn[i];
        }
        
        for (int i = 0; i < amf.length; i++)
        {
            in1[i + 6] = amf[i];
            in1[i + 14] = amf[i];
        }

        /*
         * XOR op_c and in1, rotate by r1=64, and XOR on the constant c1 (which
         * is all zeroes)
         */

        for (int i = 0; i < in1.length; i++)
            rijndaelInput[(i + 8) % 16] = (byte) (in1[i] ^ op_c[i]);

        /* XOR on the value temp computed before */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] ^= temp[i];

        out1 = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out1[i] = (byte) (out1[i] ^op_c[i]);

        byte[] mac = new byte[8];
        for (int i = 0; i < 8; i++)
            mac[i] = (byte) out1[i];

        return mac;
    }

    public static byte[] f1star(byte[] secretKey, byte[] rand, byte[] op_c, byte[] sqn,byte[] amfStar)
            throws InvalidKeyException
    {
        //Amf amfObj= new Amf("b9b9");
        //return f1(secretKey, rand, op_c, sqn, AMF);
        //return fOne(secretKey, randObj, op_cObj, sqnObj, amfObj);        
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] in1 = new byte[16];
        byte[] out1 = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);

        temp = kernel.encrypt(rijndaelInput);

        // expand sqn and amf into 128 bit value
        for (int i = 0; i < 6; i++)
        {
            in1[i] = sqn[i];
            in1[i + 8] = sqn[i];
        }
        
        for (int i = 0; i < 2; i++)
        {
            in1[i + 6] = amfStar[i];
            in1[i + 14] = amfStar[i];
        }

        
         /* XOR op_c and in1, rotate by r1=64, and XOR on the constant c1 (which
         * is all zeroes)*/
         

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 8) % 16] = (byte) (in1[i] ^ op_c[i]);

         //XOR on the value temp computed before 

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] ^= temp[i];

        out1 = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out1[i] ^= op_c[i];

        byte[] mac = new byte[8];
        for (int i = 0; i < 8; i++)
            mac[i] = (byte) out1[i+8];

        return mac;
    }

    public static byte[] f2(byte[] secretKey, byte[] rand, byte[] op_c)
            throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT2: XOR OPc and TEMP, rotate by r2=0, and
         * XOR on the constant c2 is all zeroes except that the last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (temp[i] ^ op_c[i]);

        rijndaelInput[15] ^= 1;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] = (byte) (out[i] ^op_c[i]);

        byte[] res = new byte[8];
        for (int i = 0; i < 8; i++)
            res[i] = (byte) out[i + 8];

        return res;
    }

    public static byte[] f3(byte[] secretKey, byte[] rand, byte[] op_c)
            throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT3: XOR OPc and TEMP, rotate by r3=32, and
         * XOR on the constant c3 (which * is all zeroes except that the next to
         * last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 12) % 16] = (byte) (temp[i] ^ op_c[i]);
        rijndaelInput[15] ^= 2;

        out = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out[i] = (byte) (out[i] ^op_c[i]);

        byte[] ck = new byte[16];
        for (int i = 0; i < 16; i++)
            ck[i] = (byte) out[i];
        return ck;
    }

    public static byte[] f4(byte[] secretKey, byte[] rand, byte[] op_c)
            throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT4: XOR OPc and TEMP, rotate by r4=64, and
         * XOR on the constant c4 (which * is all zeroes except that the 2nd
         * from last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 8) % 16] = (byte) (temp[i] ^ op_c[i]);
        rijndaelInput[15] ^= 4;

        out = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out[i] = (byte) (out[i] ^op_c[i]);

        byte[] ik = new byte[16];
        for (int i = 0; i < 16; i++)
            ik[i] = (byte) out[i];
        return ik;
    }

    public static byte[] f5(byte[] secretKey, byte[] rand, byte[] op_c)
            throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);
        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT2: XOR OPc and TEMP, rotate by r2=0, and
         * XOR on the constant c2 is all zeroes except that the last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (temp[i] ^ op_c[i]);

        rijndaelInput[15] ^= 1;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] = (byte) (out[i] ^op_c[i]);

        byte[] ak = new byte[6];
        for (int i = 0; i < 6; i++)
            ak[i] = (byte) out[i];

        return ak;
    }

    public static byte[] f5star(byte[] secretKey, byte[] rand, byte[] op_c)
            throws InvalidKeyException
    {
        kernel.init(secretKey);
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (rand[i] ^ op_c[i]);
        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT5: XOR OPc and TEMP, rotate by r5=96, and
         * XOR on the constant c5 (which * is all zeroes except that the 3rd
         * from last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 4) % 16] = (byte) (temp[i] ^ op_c[i]);

        rijndaelInput[15] ^= 8;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] = (byte) (out[i] ^op_c[i]);

        byte[] ak = new byte[6];
        for (int i = 0; i < 6; i++)
            ak[i] = (byte) out[i];

        return ak;
    }

    public static byte[] generateOpC(byte[] secretKey, byte[] op) throws InvalidKeyException            
    {
        kernel.init(secretKey);        
        
        byte[] byteOp_c = kernel.encrypt(op);

        byte[] op_c = new byte[byteOp_c.length];
        for (int i = 0; i < byteOp_c.length; i++)
            op_c[i] ^= (byte) (byteOp_c[i] ^ op[i]);

        return op_c;
    }
}
