package com.thoughtworks.wechat_core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {
    public static String sha1Hash(final String str) throws NoSuchAlgorithmException {
        return hash(str, "SHA-1");
    }

    public static String hash(final String str, final String algorithm) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(str.getBytes());
        return byteArrayToHex(messageDigest.digest());
    }

    private static String byteArrayToHex(final byte[] byteArray) {
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
