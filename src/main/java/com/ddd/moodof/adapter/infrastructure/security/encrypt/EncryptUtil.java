package com.ddd.moodof.adapter.infrastructure.security.encrypt;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class EncryptUtil {

    public static String encryptSHA256(String msg){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(msg.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
