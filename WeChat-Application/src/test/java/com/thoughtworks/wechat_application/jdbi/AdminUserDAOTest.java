package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.AdminUser;
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
    public void testCreateAdminUser_WithSameUsername() throws Exception {
        adminUserDAO.createAdminUser("username", "encryptPassword1");
        adminUserDAO.createAdminUser("username", "encryptPassword2");
    }

    @Test
    public void testGetAdminUserByUsername() throws Exception {
        final String username = "username";
        final String encryptPassword = "encryptPassword";
        final long id = adminUserDAO.createAdminUser(username, encryptPassword);
        final AdminUser user = adminUserDAO.getAdminUserByUsername(username);

        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(id));
        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getHashedPassword(), equalTo(encryptPassword));
        assertThat(user.getMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testGetAdminUserByUsername_NoMatchAdminUser() throws Exception {
        final AdminUser user = adminUserDAO.getAdminUserByUsername("username");
        assertThat(user, nullValue());
    }

    @Test
    public void testSetMember() throws Exception {
        final String username = "username";
        final long adminUserId = adminUserDAO.createAdminUser(username, "encryptPassword");
        final AdminUser user = adminUserDAO.getAdminUserByUsername(username);
        assertThat(user.getMemberId().isPresent(), equalTo(false));

        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        adminUserDAO.setMember(adminUserId, memberId);

        final AdminUser currentUser = adminUserDAO.getAdminUserByUsername(username);
        assertThat(currentUser.getMemberId().isPresent(), equalTo(true));
        assertThat(currentUser.getMemberId().get(), equalTo(memberId));
    }

    @Test
    public void testSetMember_SameMemberId() throws Exception {
        final long adminUser1Id = adminUserDAO.createAdminUser("username1", "encryptPassword");
        final long adminUser2Id = adminUserDAO.createAdminUser("username2", "encryptPassword");

        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        adminUserDAO.setMember(adminUser1Id, memberId);
        adminUserDAO.setMember(adminUser2Id, memberId);
    }

    @Test
    public void testGetAdminUserByMemberId() throws Exception {
        final long adminUserId = adminUserDAO.createAdminUser("username", "encryptPassword");
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        adminUserDAO.setMember(adminUserId, memberId);

        final AdminUser adminUser = adminUserDAO.getAdminUserByMemberId(memberId);
        assertThat(adminUser, notNullValue());
        assertThat(adminUser.getId(), equalTo(1L));
        assertThat(adminUser.getMemberId().isPresent(), equalTo(true));
        assertThat(adminUser.getMemberId().get(), equalTo(1L));
        assertThat(adminUser.getUsername(), equalTo("username"));
    }

    @Test
    public void testGetAdminUserByMemberId_NotExist() throws Exception {
        final AdminUser adminUser = adminUserDAO.getAdminUserByMemberId(1L);
        assertThat(adminUser, nullValue());
    }
}