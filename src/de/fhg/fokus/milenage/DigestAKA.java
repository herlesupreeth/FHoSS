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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;

import de.fhg.fokus.milenage.codec.CoDecException;
import de.fhg.fokus.milenage.kernel.Kernel;
import de.fhg.fokus.milenage.kernel.KernelNotFoundException;

/**
 * @author Sizhong Liu, sli -at- fokus dot fraunhofer dot de
 */
public class DigestAKA
{
    public static final String DEFAULT_KERNEL_CLASSNAME="de.fhg.fokus.milenage.kernel.Rijndael32Bit";
    public static final String PROPERTY_KERNEL_CLASSNAME="de.fhg.fokus.milenage.KERNELCLASSNAME";
    
    private Kernel kernel;
    public static void main(String[] args) throws KernelNotFoundException 
    {
        DigestAKA test = new DigestAKA();
        int j = 1;
        for (int i = 0; i < j; i++)
        {
            test.testAuth();
        }
    }
    
    public DigestAKA(String kernelClassName) throws KernelNotFoundException
    {
        Class kernelClass= null;
        try
        {
            kernelClass = Class.forName(kernelClassName);
            Constructor[] constructors= kernelClass.getConstructors();
            for(int i=0;i<constructors.length;i++)
            {
                Constructor constructor= constructors[i];
                if(constructor.getParameterTypes().length==0)
                {
                    kernel= (Kernel) constructor.newInstance((Object[])null);
                    break;
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
        catch (IllegalArgumentException e)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
        catch (InstantiationException e)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
        catch (IllegalAccessException e)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
        catch (InvocationTargetException e)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
        
        if(kernel==null)
        {
            throw new KernelNotFoundException("Can not initialise Kernel: "+kernelClassName);
        }
    }
    
    public DigestAKA() throws KernelNotFoundException
    {
        //kernel= new Rijndael32Bit();
        this(DEFAULT_KERNEL_CLASSNAME);
    }

    public void testAuth()
    {
        String secret = "465b5ce8b199b49faa5f0a2ee238a6bc";
        String rand = "23553cbe9637a89d218ae64dae47bf35";
        String sqn = "ff9bb4d0b607";
        String amf = "b9b9";
        String op = "cdc202d5123e20f62b6d676ac72cb318";
        String opc = "cd63cb71954a9f4e48a5994e37a02baf";

        String macOrig = "4a9ffac354dfafb3";
        String resOrig = "a54211d5e3ba50bf";
        String ckOrig = "b40ba9a3c58b2a05bbf0d987b21bf8cb";
        String ikOrig = "f769bcd751044604127672711c6d3441";
        String akOrig = "aa689c648370";
        String mac_sOrig = "01cfaf9ec4e871e9";
        String akResynchOrig = "451e8beca43b";
        
        String passwdString= "testuser1";

        try
        {
            AuthKey keyObj = new AuthKey(secret);
            Rand randObj = new Rand(rand);
            Sqn sqnObj = new SimpleSqn(sqn);
            Amf amfObj = new Amf(amf);
            Op opObj = new Op(op);
            Opc opcObj = new Opc(opc);
            Opc genOpcObj = generateOp_c(keyObj, opObj);
            System.out.println("OPC LENGTH: " + genOpcObj.toString().length()
                    * 4);
            System.out.println("OPCORIG: " + opc);
            System.out.println("OPC:     " + genOpcObj);

            Mac macObj = fOne(keyObj, randObj, opcObj, sqnObj, amfObj);
            System.out.println("MAC LENGTH: " + macObj.toString().length() * 4);
            System.out.println("MACORIG(f1): " + macOrig);
            System.out.println("MAC(f1):     " + macObj);

            Res xResObj = fTwo("Digest-AKAv1-MD5",keyObj, randObj, opcObj);
            System.out.println("XRES LENGTH: " + xResObj.toString().length()
                    * 4);
            System.out.println("XRESORIG(f2): " + resOrig);
            System.out.println("XRES(f2):     " + xResObj);

            Ck ckObj = fThree(keyObj, randObj, opcObj);
            System.out.println("CK LENGTH: " + ckObj.toString().length() * 4);
            System.out.println("CKORIG(f3): " + ckOrig);
            System.out.println("CK(f3):     " + ckObj);

            Ik ikObj = fFour(keyObj, randObj, opcObj);
            System.out.println("IK LENGTH: " + ckObj.toString().length() * 4);
            System.out.println("IKORIG(f4): " + ikOrig);
            System.out.println("IK(f4):     " + ikObj);

            Ak akObj = fFive(keyObj, randObj, opcObj);
            System.out.println("AK LENGTH: " + akObj.toString().length() * 4);
            System.out.println("AKORIG(f5): " + akOrig);
            System.out.println("AK(f5):     " + akObj);

            
            Mac mac_s = fOneStar(keyObj, randObj, opcObj, sqnObj);
            System.out.println("MACS LENGTH: " + mac_s.toString().length() * 4);
            System.out.println("MACSORIG(f1star): " + mac_sOrig);
            System.out.println("MACS(f1star):     " + mac_s);

            Ak akResynch = fFiveStar(keyObj, randObj, opcObj);
            System.out.println("AKResynch LENGTH: "
                    + akResynch.toString().length() * 4);
            System.out.println("AKResynchORIG(f5star): " + akResynchOrig);
            System.out.println("AKResynch(f5star):     " + akResynch);

            System.out.println("passwd:                 " + passwdString);
            System.out.println("secret key from passwd: " + AuthKey.getKeyFromPassword(passwdString));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Opc generateOp_c(AuthKey secretKey, Op opObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] op = opObj.getBytes();
        
        byte[] byteOp_c = kernel.encrypt(op);

        byte[] op_c = new byte[16];
        for (int i = 0; i < 16; i++)
            op_c[i] = (byte) (byteOp_c[i] ^ op[i]);

        return new Opc(op_c);
    }

    public Mac fOne(AuthKey secretKey, Rand randObj, Opc op_cObj,
            Sqn sqnObj, Amf amfObj) throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] in1 = new byte[16];
        byte[] out1 = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));

        temp = kernel.encrypt(rijndaelInput);

        // expand sqn and amf into 128 bit value
        for (int i = 0; i < 6; i++)
        {
            in1[i] = sqnObj.getByte(i);
            in1[i + 8] = sqnObj.getByte(i);
        }
        
        for (int i = 0; i < 2; i++)
        {
            in1[i + 6] = amfObj.getByte(i);
            in1[i + 14] = amfObj.getByte(i);
        }

        /*
         * XOR op_c and in1, rotate by r1=64, and XOR on the constant c1 (which
         * is all zeroes)
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 8) % 16] = (byte) (in1[i] ^ op_cObj.getByte(i));

        /* XOR on the value temp computed before */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] ^= temp[i];

        out1 = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out1[i] ^= op_cObj.getByte(i);

        byte[] mac = new byte[8];
        for (int i = 0; i < 8; i++)
            mac[i] = (byte) out1[i];

        return new Mac(mac);

    }

    public Mac fOneStar(AuthKey secretKey, Rand randObj, Opc op_cObj, Sqn sqnObj)
            throws InvalidKeyException, CoDecException
    {
        return fOne(secretKey, randObj, op_cObj, sqnObj, new Amf(new byte[2]));
    }

    public Res fTwo(String authScheme, AuthKey secretKey, Rand randObj, Opc op_cObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT2: XOR OPc and TEMP, rotate by r2=0, and
         * XOR on the constant c2 is all zeroes except that the last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (temp[i] ^ op_cObj.getByte(i));

        rijndaelInput[15] ^= 1;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] ^= op_cObj.getByte(i);

        byte[] res = new byte[8];
        for (int i = 0; i < 8; i++)
            res[i] = (byte) out[i + 8];

        if (authScheme.equalsIgnoreCase("Digest-AKAv2-MD5")){
            Ik ik = this.fFour(secretKey, randObj, op_cObj);
            Ck ck = this.fThree(secretKey, randObj, op_cObj);
            byte[] resv2=new byte[res.length+ik.bytes.length+ck.bytes.length];
            System.arraycopy(res,0,resv2,0,res.length);
            System.arraycopy(ik,0,resv2,res.length,ik.bytes.length);
            System.arraycopy(ck,0,resv2,res.length+ik.bytes.length,ck.bytes.length);
            return new Res(resv2);
        }
        return new Res(res);
    }

    public Ck fThree(AuthKey secretKey, Rand randObj, Opc op_cObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT3: XOR OPc and TEMP, rotate by r3=32, and
         * XOR on the constant c3 (which * is all zeroes except that the next to
         * last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 12) % 16] = (byte) (temp[i] ^ op_cObj.getByte(i));
        rijndaelInput[15] ^= 2;

        out = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out[i] ^= op_cObj.getByte(i);

        byte[] ck = new byte[16];
        for (int i = 0; i < 16; i++)
            ck[i] = (byte) out[i];
        return new Ck(ck);
    }

    public Ik fFour(AuthKey secretKey, Rand randObj, Opc op_cObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));

        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT4: XOR OPc and TEMP, rotate by r4=64, and
         * XOR on the constant c4 (which * is all zeroes except that the 2nd
         * from last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 8) % 16] = (byte) (temp[i] ^ op_cObj.getByte(i));
        rijndaelInput[15] ^= 4;

        out = kernel.encrypt(rijndaelInput);
        for (int i = 0; i < 16; i++)
            out[i] ^= op_cObj.getByte(i);

        byte[] ik = new byte[16];
        for (int i = 0; i < 16; i++)
            ik[i] = (byte) out[i];
        return new Ik(ik);
    }

    public Ak fFive(AuthKey secretKey, Rand randObj, Opc op_cObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));
        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT2: XOR OPc and TEMP, rotate by r2=0, and
         * XOR on the constant c2 is all zeroes except that the last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (temp[i] ^ op_cObj.getByte(i));

        rijndaelInput[15] ^= 1;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] ^= op_cObj.getByte(i);

        byte[] ak = new byte[6];
        for (int i = 0; i < 6; i++)
            ak[i] = (byte) out[i];

        return new Ak(ak);
    }

    /**
     * Creates a Ak object
     * @param secretKey
     * @param randObj
     * @param op_cObj
     * @return new Ak object
     * @throws InvalidKeyException
     */
    public Ak fFiveStar(AuthKey secretKey, Rand randObj, Opc op_cObj)
            throws InvalidKeyException
    {
        kernel.init(secretKey.getBytes());
        byte[] temp = new byte[16];
        byte[] out = new byte[16];
        byte[] rijndaelInput = new byte[16];

        for (int i = 0; i < 16; i++)
            rijndaelInput[i] = (byte) (randObj.getByte(i) ^ op_cObj.getByte(i));
        temp = kernel.encrypt(rijndaelInput);

        /*
         * To obtain output block OUT5: XOR OPc and TEMP, rotate by r5=96, and
         * XOR on the constant c5 (which * is all zeroes except that the 3rd
         * from last bit is 1).
         */

        for (int i = 0; i < 16; i++)
            rijndaelInput[(i + 4) % 16] = (byte) (temp[i] ^ op_cObj.getByte(i));

        rijndaelInput[15] ^= 8;

        out = kernel.encrypt(rijndaelInput);

        for (int i = 0; i < 16; i++)
            out[i] ^= op_cObj.getByte(i);

        byte[] ak = new byte[6];
        for (int i = 0; i < 6; i++)
            ak[i] = (byte) out[i];

        return new Ak(ak);
    }
}
