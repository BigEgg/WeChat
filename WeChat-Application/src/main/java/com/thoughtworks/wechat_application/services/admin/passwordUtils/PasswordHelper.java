package com.thoughtworks.wechat_application.services.admin.passwordUtils;

import com.google.inject.ImplementedBy;

@ImplementedBy(PasswordHelper_V1.class)
public interface PasswordHelper {
    String saltHash(final String password);

    String saltHash(final String password, final String salt);

    String getSaltFromHashedPassword(final String hashedPassword);
}
