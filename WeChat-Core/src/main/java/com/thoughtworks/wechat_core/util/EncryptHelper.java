package com.thoughtworks.wechat_core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelper {
    public static String sha1Encrypt(String str) throws NoSuchAlgorithmException {
        return encrypt(str, "SHA1");
    }

    public static String md5Encrypt(String str) throws NoSuchAlgorithmException {
        return encrypt(str, "MD5");
    }

    private static String encrypt(String str, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(str.getBytes());
        return byteArrayToHex(messageDigest.digest());
    }

    private static String byteArrayToHex(byte[] byteArray) {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        return new String(resultCharArray);
    }
}
