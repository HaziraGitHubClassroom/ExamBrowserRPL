package com.hazira.exambrowser;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY = "0123456789abcdef"; // Replace with your secure key
    private static final String IV = "0123456789abcdef";  // Replace with your secure IV

    public static String decrypt(String encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        byte[] decodedValue = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] original = cipher.doFinal(decodedValue);

        return new String(original, "UTF-8");
    }
}
