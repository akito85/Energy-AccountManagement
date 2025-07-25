/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.security;

import com.dbs.common.base.utils.Constant;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author RachmatY
 */
@SuppressWarnings("java:S1118")
public class CryptoSecurity {
    private static final Logger logger = LoggerFactory.getLogger(CryptoSecurity.class);

    public static final String KEY = "12345678901234567890123456789012";
    public static final byte[] KEY_ARRAY = Base64.decodeBase64(KEY);
    
    public static final String KEY_1= "8Ubo89Bhyiq09mLp064Db1f4Ju8Zq79X";
    public static final String KEY_2= "7Hn7kn87HnbGyt87Qp0o8Nhd78B6jn9Z";
    public static final String ENCINSTANCE = "AES/CBC/PKCS5PADDING";

    /**
     * 
     * @param key
     * @return 
     */
    public static byte[] loadKey(int key) {
        String tmp;
        if (key == 2) {
            tmp = KEY_2;
        } else {
            tmp = KEY_1;
        }
        return Base64.decodeBase64(tmp);
    }

    /**
     * 
     * @param strToEncrypt
     * @param key
     * @return 
     */
    @SuppressWarnings("java:S3329")
    public static String encrypt(String strToEncrypt, int key) {
        try {
            Cipher cipher = Cipher.getInstance(ENCINSTANCE);

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = {1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key secretKey = new SecretKeySpec(loadKey(key), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * @param strToEncrypt
     * @return 
     */
    @SuppressWarnings("java:S3329")
    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ENCINSTANCE);

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = {1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key secretKey = new SecretKeySpec(loadKey(1), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * @param EncryptedMessage
     * @param key
     * @return 
     */
    @SuppressWarnings("java:S3329")
    public static String decrypt(String encryptedMessage, int key) {
        try {
            Cipher cipher = Cipher.getInstance(ENCINSTANCE);

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = {1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key secretKey = new SecretKeySpec(loadKey(key), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            byte[] decodedMessage = Base64.decodeBase64(encryptedMessage);
            return new String(cipher.doFinal(decodedMessage));

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * @param EncryptedMessage
     * @return 
     */
    @SuppressWarnings("java:S3329")
    public static String decrypt(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance(ENCINSTANCE);

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = {1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key secretKey = new SecretKeySpec(loadKey(1), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            byte[] decodedMessage = Base64.decodeBase64(encryptedMessage);
            return new String(cipher.doFinal(decodedMessage));

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
        }
        return null;
    }
}
