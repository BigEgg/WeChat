package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.AdminUser;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.jdbi.AdminUserDAO;
import com.thoughtworks.wechat_application.services.admin.passwordUtils.PasswordHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminUserServiceTest {
    @Mock
    private AdminUserDAO adminUserDAO;
    @Mock
    private PasswordHelper passwordHelper;
    private AdminUserService service;
    private Injector injector;

    @Before
    public void setUp() {
        injector = Guice.createInjector(binder -> {
            binder.bind(AdminUserDAO.class).toInstance(adminUserDAO);
            binder.bind(PasswordHelper.class).toInstance(passwordHelper);
        });

        service = injector.getInstance(AdminUserService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final AdminUserService anotherService = injector.getInstance(AdminUserService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testLogIn() throws Exception {
        when(adminUserDAO.getAdminUserByUsername("username")).thenReturn(createAdmin());
        when(passwordHelper.getSaltFromHashedPassword("hashedPassword")).thenReturn("salt");
        when(passwordHelper.saltHash("password", "salt")).thenReturn("hashedPassword");

        final Optional<AdminUser> adminUser = service.logIn("username", "password");

        verify(adminUserDAO).getAdminUserByUsername(eq("username"));
        verify(passwordHelper).getSaltFromHashedPassword(eq("hashedPassword"));
        verify(passwordHelper).saltHash(eq("password"), eq("salt"));
        assertThat(adminUser.isPresent(), equalTo(true));
        assertThat(adminUser.get().getId(), equalTo(1L));
        assertThat(adminUser.get().getUsername(), equalTo("username"));
        assertThat(adminUser.get().getHashedPassword(), equalTo("hashedPassword"));
        assertThat(adminUser.get().getMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testLogIn_UserNotExist() throws Exception {
        when(adminUserDAO.getAdminUserByUsername("username")).thenReturn(null);

        final Optional<AdminUser> adminUser = service.logIn("username", "password");

        verify(adminUserDAO).getAdminUserByUsername(eq("username"));
        verify(passwordHelper, never()).getSaltFromHashedPassword(anyString());
        verify(passwordHelper, never()).saltHash(anyString(), anyString());
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testLogIn_PasswordNotRight() throws Exception {
        when(adminUserDAO.getAdminUserByUsername("username")).thenReturn(createAdmin());
        when(passwordHelper.getSaltFromHashedPassword("hashedPassword")).thenReturn("salt");
        when(passwordHelper.saltHash("password", "salt")).thenReturn("hashedPassword123");

        final Optional<AdminUser> adminUser = service.logIn("username", "password");

        verify(adminUserDAO).getAdminUserByUsername(eq("username"));
        verify(passwordHelper).getSaltFromHashedPassword(eq("hashedPassword"));
        verify(passwordHelper).saltHash(eq("password"), eq("salt"));
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testCreateAdmin() throws Exception {
        when(adminUserDAO.getAdminUserByUsername("username")).thenReturn(null, createAdmin());
        when(passwordHelper.saltHash("password")).thenReturn("hashedPassword");

        final Optional<AdminUser> adminUser = service.createAdmin("username", "password");

        verify(adminUserDAO).createAdminUser(eq("username"), eq("hashedPassword"));
        verify(adminUserDAO, times(2)).getAdminUserByUsername(eq("username"));
        assertThat(adminUser.isPresent(), equalTo(true));
        assertThat(adminUser.get().getId(), equalTo(1L));
        assertThat(adminUser.get().getUsername(), equalTo("username"));
        assertThat(adminUser.get().getHashedPassword(), equalTo("hashedPassword"));
        assertThat(adminUser.get().getMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testCreateAdmin_SameUsername() throws Exception {
        when(adminUserDAO.getAdminUserByUsername("username")).thenReturn(createAdmin());
        when(passwordHelper.saltHash("password")).thenReturn("hashedPassword");

        final Optional<AdminUser> adminUser = service.createAdmin("username", "password1");

        verify(adminUserDAO, never()).createAdminUser(anyString(), anyString());
        verify(adminUserDAO).getAdminUserByUsername(eq("username"));
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testSetMember() throws Exception {
        final AdminUser adminUser = createAdmin();
        when(adminUserDAO.getAdminUserByMemberId(1L)).thenReturn(null);

        final boolean isSuccess = service.setMember(adminUser, createSubscribeMember());

        verify(adminUserDAO).getAdminUserByMemberId(eq(1L));
        verify(adminUserDAO).setMember(eq(1L), eq(1L));
        assertThat(isSuccess, equalTo(true));
    }

    @Test
    public void testSetMember_AlreadyExist() throws Exception {
        final AdminUser adminUser = createAdmin();
        when(adminUserDAO.getAdminUserByMemberId(1L)).thenReturn(adminUser);

        final boolean isSuccess = service.setMember(adminUser, createSubscribeMember());

        verify(adminUserDAO).getAdminUserByMemberId(eq(1L));
        verify(adminUserDAO, never()).setMember(anyLong(), anyLong());
        assertThat(isSuccess, equalTo(false));
    }

    private AdminUser createAdmin() {
        return new AdminUser(1L, "username", "hashedPassword", Optional.<Long>empty());
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }
}