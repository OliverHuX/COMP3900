package com.yyds.recipe.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import java.security.SecureRandom;
import java.util.Arrays;

public class SaltGenerator {
    public static String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        int length = 0;
        do {
            length = secureRandom.nextInt(20);
        } while (length < 10);
        System.out.println(length);
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }

    public static String base64UrlEncode(String str) throws Exception {
        return Base64.encodeBase64String(str.getBytes("UTF-8"));
    }

    public static String HMACSHA256(String s, String secret) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        return s + Base64.encodeBase64String(sha256.doFinal(secret.getBytes("UTF-8")));
    }
}
