package com.thoughtworks.wechat_application.services.passwordUtils;

public interface PasswordHelper {
    String saltHash(final String password);

    String saltHash(final String password, final String salt);

    String getSaltFromHashedPassword(final String hashedPassword);
}
