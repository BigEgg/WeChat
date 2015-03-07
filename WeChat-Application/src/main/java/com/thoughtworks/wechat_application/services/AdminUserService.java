package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.AdminUser;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.jdbi.AdminUserDAO;
import com.thoughtworks.wechat_application.services.passwordUtils.PasswordHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class AdminUserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminUserService.class);
    private AdminUserDAO adminUserDAO;
    private PasswordHelper passwordHelper;

    @Inject
    public AdminUserService(final AdminUserDAO adminUserDAO, final PasswordHelper passwordHelper) {
        this.adminUserDAO = adminUserDAO;
        this.passwordHelper = passwordHelper;
    }

    public Optional<AdminUser> logIn(final String username, final String password) {
        checkNotBlank(username);
        checkNotBlank(password);

        AdminUser currentAdminUser = adminUserDAO.getAdminUserByUsername(username);
        if (currentAdminUser != null) {
            String salt = passwordHelper.getSaltFromHashedPassword(currentAdminUser.getHashedPassword());
            String hashedPassword = passwordHelper.saltHash(password, salt);
            if (currentAdminUser.getHashedPassword().equals(hashedPassword)) {
                LOGGER.info("[LogIn] Log in admin with username: {}, and password: {}, success.", username, hashedPassword);
                return Optional.of(currentAdminUser);
            } else {
                LOGGER.info("[LogIn] Log in admin with username: {}, and password: {}, failed. Password not right.", username, hashedPassword);
                return Optional.empty();
            }
        } else {
            LOGGER.info("[LogIn] Log in admin with username: {}, failed. No such user.", username);
            return Optional.empty();
        }
    }

    public Optional<AdminUser> createAdmin(final String username, final String password) {
        checkNotBlank(username);
        checkNotBlank(password);

        AdminUser currentAdminUser = adminUserDAO.getAdminUserByUsername(username);
        if (currentAdminUser == null) {
            String hashedPassword = passwordHelper.saltHash(password);
            long adminId = adminUserDAO.createAdminUser(username, hashedPassword);
            LOGGER.info("[CreateAdmin] Create a new admin user(id: {}) with username: {}, password: {}.", adminId, username, hashedPassword);
            return Optional.of(adminUserDAO.getAdminUserByUsername(username));
        } else {
            LOGGER.info("[CreateAdmin] Create a new admin user with username: {} failed. Already have an admin user(id: {}) with same user name", username, currentAdminUser.getId());
            return Optional.empty();
        }
    }

    public boolean setMember(final AdminUser adminUser, final Member member) {
        checkNotNull(adminUser);
        checkNotNull(member);

        final AdminUser currentAdminUser = adminUserDAO.getAdminUserByMemberId(member.getId());
        if (currentAdminUser != null) {
            LOGGER.info("[SetMember] Cannot set the member(id: {}) to admin user(id: {}), it already bound to another admin user(id: {}).", member.getId(), adminUser.getId(), currentAdminUser.getId());
            return false;
        } else {
            adminUserDAO.setMember(adminUser.getId(), member.getId());
            LOGGER.info("[SetMember] Set member(id: {}) to admin user(id: {}).", member.getId(), adminUser.getId());
            return true;
        }
    }
}
