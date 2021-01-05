package com.shopify.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class MessageDigestUtils {
    public static String getMessageDigest(String input, String algorithm, String charsetName)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(input.getBytes(charsetName));
        byte byteData[] = messageDigest.digest();

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < byteData.length; i++) {
            stringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return Base64.encodeBase64(binaryData);
    }

    public static byte[] decodeBase64(byte[] binaryData) {
        return Base64.decodeBase64(binaryData);
    }
}