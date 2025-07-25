package com.dbs.common.library.utils;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@SuppressWarnings({"java:S1118", "java:S1450"})
public class AesUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(AesUtil.class);

    private static SecretKeySpec secretKey;
    private static byte[] key;


    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"java:S3329", "java:S5542"})
    public static String encrypt(final String strToEncrypt, final String secret,final String initVector) {
        try {
            setKey(secret);
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("Error while encrypting : {}", e.getMessage());
        }
        return null;
    }

    @SuppressWarnings({"java:S3329", "java:S5542"})
    public static String decrypt(final String strToDecrypt, final String secret,final String initVector) {
        try {
            setKey(secret);
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey,iv);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            logger.error("Error while decrypting : {}", e.getMessage());
        }
        return null;
    }
    
    public static void main(String[] args) {
//        
        String y = encrypt("gwbn cjyq vntq kjjk", "testencryption16", "testinitvector16");
        String x = decrypt("Mz26g4vsEe4Am5lgG4knsZ3ucn/zFUBetqfX+ZdPeQU=", "testencryption16", "testinitvector16");
        String z = decrypt("Mz26g4vsEe4Am5lgG4knsZ3ucn/zFUBetqfX+ZdPeQU=", "testencryption16", "testinitvector16");
        logger.info("dec : {}", x);
        logger.info("enc : {}", y);
    }
    
    
}
