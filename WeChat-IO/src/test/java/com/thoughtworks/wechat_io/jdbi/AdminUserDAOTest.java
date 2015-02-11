package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.AdminUser;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AdminUserDAOTest extends AbstractDAOTest {
    private MemberDAO memberDAO;
    private AdminUserDAO adminUserDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
        adminUserDAO = getDAO(AdminUserDAO.class);
    }

    @Test
    public void testCreateAdminUser() throws Exception {
        long id = adminUserDAO.createAdminUser("username1", "encryptPassword");
        assertThat(id, equalTo(1L));
        id = adminUserDAO.createAdminUser("username2", "encryptPassword");
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateAdminUser_WithSameUsernameAndPassword() throws Exception {
        adminUserDAO.createAdminUser("username", "encryptPassword");
        adminUserDAO.createAdminUser("username", "encryptPassword");
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateAdminUser_WithSameUsername() throws Exception {
        adminUserDAO.createAdminUser("username1", "encryptPassword1");
        adminUserDAO.createAdminUser("username1", "encryptPassword2");
    }

    @Test
    public void testGetAdminUserByUsernameAndPassword() throws Exception {
        final String username = "username";
        final String encryptPassword = "encryptPassword";
        long id = adminUserDAO.createAdminUser(username, encryptPassword);
        final AdminUser user = adminUserDAO.getAdminUserByUsernameAndPassword(username, encryptPassword);

        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(id));
        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getEncryptedPassword(), equalTo(encryptPassword));
        assertThat(user.getMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testGetAdminUserByUsernameAndPassword_NoMatchAdminUser() throws Exception {
        final AdminUser user = adminUserDAO.getAdminUserByUsernameAndPassword("username", "encryptPassword");
        assertThat(user, nullValue());
    }

    @Test
    public void testSetMember() throws Exception {
        final String username = "username";
        final String encryptPassword = "encryptPassword";
        adminUserDAO.createAdminUser(username, encryptPassword);
        AdminUser user = adminUserDAO.getAdminUserByUsernameAndPassword(username, encryptPassword);

        assertThat(user.getMemberId().isPresent(), equalTo(false));
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        adminUserDAO.setMember(memberId);

        user = adminUserDAO.getAdminUserByUsernameAndPassword(username, encryptPassword);
        assertThat(user.getMemberId().isPresent(), equalTo(true));
        assertThat(user.getMemberId().get(), equalTo(memberId));
    }
}