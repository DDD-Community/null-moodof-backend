package com.ddd.moodof.adapter.infrastructure.security.encrypt;

import org.springframework.stereotype.Component;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptUtil {

    public static String encryptAES256(String msg, String key) throws Exception {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();

        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));
        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
        return Base64.getEncoder().encodeToString(buffer);
    }
    public static String decryptAES256(String msg, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));
        byte[] saltBytes = new byte[20];
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];

        buffer.get(saltBytes, 0, saltBytes.length);
        buffer.get(ivBytes, 0, ivBytes.length);
        buffer.get(encryoptedTextBytes);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
        byte[] decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);
        return new String(decryptedTextBytes);
    }
}
