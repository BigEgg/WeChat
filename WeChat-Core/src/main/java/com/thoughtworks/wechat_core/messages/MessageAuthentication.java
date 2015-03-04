package com.thoughtworks.wechat_core.messages;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

public final class MessageAuthentication {
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

        return signature.toUpperCase().equals(sha1Hash(builder.toString()));
    }
}
