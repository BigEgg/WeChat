package com.thoughtworks.wechat_io.services.passwordUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.thoughtworks.wechat_core.util.HashHelper.hash;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

public class PasswordHelper_V1 implements PasswordHelper {
    public final static String DEFAULT_ALGORITHM = "SHA-256";
    private final static Logger LOGGER = LoggerFactory.getLogger(PasswordHelper_V1.class);

    @Override
    public String saltHash(final String password) {
        checkNotBlank(password);

        return saltHash(password, generateSalt());
    }

    @Override
    public String saltHash(final String password, final String salt) {
        checkNotBlank(password);
        checkNotBlank(salt);

        try {
            String hashedPassword = password;
            for (int i = 0; i < 5; i++) {
                hashedPassword = addSalt(hashedPassword, salt);
            }
            return hashedPassword;
        } catch (final NoSuchAlgorithmException ex) {
            LOGGER.error("Cannot add salt for password. Must fix this.", ex);
            return password;
        }
    }

    @Override
    public String getSaltFromHashedPassword(final String hashedPassword) {
        checkNotBlank(hashedPassword);
        checkArgument(hashedPassword.length() == 80);

        return hashedPassword.substring(64, 80);
    }

    private String addSalt(final String word, final String salt) throws NoSuchAlgorithmException {
        return hash(word, DEFAULT_ALGORITHM) + salt;
    }

    private String generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return new String(salt).substring(0, 16);
    }

}
