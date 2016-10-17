import java.io.*;
import java.security.SecureRandom;

/**
 * Created by SPRS1994 on 27/10/15.
 * Implementation of One Time Pad
 * Student No: 130391986
 */
public class OneTimePad {

    public static void main(String[] args) throws IOException {

        //Given OTP key
        String key = "6dc72fc595e35dcd38c05dca2a0d2dbd8e2df20b129b2cfa29ad17972922a2";
        //Key for plainText3
        String key2 = "657f314c6139322d3a76051d03442a831743137c2334046b63133350392f030c1b37";
        /*
        * Plain Texts match cipher texts by number i.e. plainText2 uses cipherText2
        */
        String plainText = "Every cloud has a silver lining";
        String plainText2 = "Happy canny and a little tanned";
        String plainText3 = "Supercalifragilisticexpialidocious";

        String cipherText = "28b14ab7ecc33ea157b539ea426c5e9def0d81627eed498809c17ef9404cc5";
        String cipherText2 = "25a65fb5ecc33eac56ae24ea4b63499def0d9e6266ef409f09df76f94746db";
        String cipherText3 = "360a4129135a53415310777c642d46ea64377a1f464c7402027f5a34564c6a636e44";

        OneTimePad otp = new OneTimePad();

        String s = new String(otp.hexStringToByteArray(key));

        System.out.println("XORing spaces tests: ");
        otp.xorSpaceTest("a");
        otp.xorSpaceTest("A");
        otp.xorSpaceTest("b");
        otp.xorSpaceTest("B");
        System.out.println("\n");


        System.out.println("Encryption of plainText: " + otp.encrypt(plainText, hexStringToByteArray(key)) + "\n");
        System.out.println("Encryption of plainText3: " + otp.encrypt(plainText3, hexStringToByteArray(key2)) + "\n");
        System.out.println("Decryption of cipherText3: " + otp.decrypt(cipherText3, hexStringToByteArray(key2)) + "\n");
        System.out.println("Decryption of cipherText: " + otp.decrypt(cipherText, hexStringToByteArray(key)) + "\n");


        //otp.writeGenerator();
        otp.writeToFile(otp.encrypt(otp.readIn("500000latin.txt"), hexStringToByteArray(otp.readIn("generated.txt"))), "encryptedLatin.txt");

        otp.writeToFile(otp.decrypt(otp.readIn("encryptedLatin.txt"), hexStringToByteArray(otp.readIn("generated.txt"))), "decryptedLatin.txt");
        //otp.xorSpaceTest(" ");

    }

    public String readIn(String PATH_NAME) throws IOException {
        //Read in text file and return it as a String
        String textString = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME));

            String line;

            //While there is text in the file
            while ((line = reader.readLine()) != null) {

                textString += line;

            }


        } catch (Exception e) {
            //if file is not found produce error
            System.err.println("File not Found!");
        }

        return textString;


    }

    public static String bytesToHex(byte[] bytes) {
        /*
        *
        *   Taken from OTPAttack.java
        *   http://homepages.cs.ncl.ac.uk/feng.hao/teaching/OTPAttack.java
        *   @Author: Feng Hao
        *   Date accessed: 26/10/2015
        *
        */
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        /*
        *
        *   Taken from OTPAttack.java
        *   http://homepages.cs.ncl.ac.uk/feng.hao/teaching/OTPAttack.java
        *   @Author: Feng Hao
        *   Date accessed: 26/10/2015
        *
        */
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public String encrypt(String pText, byte[] key) {
        /*
        *Converts plain text to bytes using getBytes() and takes key as string and converts to byte array
        *(using hexStringToByteArray) function and Xor's the bytes, then rebuilds the string using
        *bytesToHex function.
        */
        final byte[] plain = pText.getBytes();
        final byte[] keyByte = key;

        if (plain.length > keyByte.length) {

            System.err.println("Message length and key length not equal");
            System.exit(0);

        }

        final byte[] encrypted = new byte[plain.length];

        for (int i = 0; i < plain.length; i++) {
            encrypted[i] = (byte) ((plain[i] ^ keyByte[i]));
        }
        String e = bytesToHex(encrypted);

        return e;
    }


    public String decrypt(String cText, byte[] key) {
        /*
        *Converts cipher text to bytes and takes in key bytes (using hexStringToByteArray) function
        *and Xor's the bytes, then rebuilds string of decrypted text
        *
        */

        final byte[] cipher = hexStringToByteArray(cText);
        final byte[] keyByte = key;
        final byte[] decrypted = new byte[cipher.length];
        if (cipher.length > keyByte.length) {

            System.err.println("Message length and key length not equal");
            System.exit(0);

        }

        for (int i = 0; i < cipher.length; i++) {
            decrypted[i] = (byte) ((cipher[i] ^ keyByte[i]));
        }

        String e = new String(decrypted);

        return e;
    }

    public static String genKey(int length) {
        /*
        *Generates key for given length and converts to hex
        */
        SecureRandom rand = new SecureRandom();
        byte[] key = new byte[length];
        rand.nextBytes(key);


        return bytesToHex(key);
    }

    public void writeGenerator() throws IOException {
        /*
        *Generates 1mb one time pad using genKey function
        */
        try {
            File file = new File("generated.txt");

            BufferedWriter bW = new BufferedWriter(new FileWriter(file, false));

            bW.write(genKey(500000));
            bW.flush();
            bW.close();
        } catch (Exception e) {
            System.err.println("File not written!");
        }
    }

    public void writeToFile(String text, String filename) throws IOException {
        /*
        *writes text to file
        */
        try {
            File file = new File(filename);

            BufferedWriter bW = new BufferedWriter(new FileWriter(file, false));

            bW.write(text);
            bW.flush();
            bW.close();
            System.out.println(filename + " " + "File Written!");
        } catch (Exception e) {
            System.err.println(filename + " " + "File not written!");
        }
    }


    public void xorSpaceTest(String text) {
        /*
        *
        * Tests the XOR of character [a-zA-Z] and space
        *
        */
        String a = text;
        String sp = " ";

        byte[] by = a.getBytes();
        byte[] spa = sp.getBytes();
        byte[] t = new byte[by.length];

        for (int i = 0; i < by.length; i++) {
            t[i] = (byte) (by[i] ^ spa[i]);
        }

        System.out.println(new String(t));
    }


}
