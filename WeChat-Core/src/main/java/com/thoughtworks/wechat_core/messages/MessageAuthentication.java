package com.thoughtworks.wechat_core.messages;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

public final class MessageAuthentication {
    private static String sha1Encrypt(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
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

    public static boolean validation(String signature, String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        checkNotBlank(signature);
        checkNotBlank(token);
        checkNotBlank(timestamp);
        checkNotBlank(nonce);

        String[] data = new String[]{token, timestamp, nonce};
        Arrays.sort(data);

        final StringBuilder builder = new StringBuilder();
        for (String item : data) {
            builder.append(item);
        }

        return signature.contentEquals(sha1Encrypt(builder.toString()));
    }
}
