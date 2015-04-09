package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class OAuthClientDAOTest extends AbstractDAOTest {
    private MemberDAO memberDAO;
    private OAuthClientDAO OAuthClientDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
        OAuthClientDAO = getDAO(OAuthClientDAO.class);
    }

    @Test
    public void testCreateAdminUser() throws Exception {
        long id = OAuthClientDAO.create("username1", "encryptPassword", AuthenticateRole.ADMIN);
        assertThat(id, equalTo(1L));
        id = OAuthClientDAO.create("username2", "encryptPassword", AuthenticateRole.ADMIN);
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateAdminUser_WithSameUsername() throws Exception {
        OAuthClientDAO.create("username", "encryptPassword1", AuthenticateRole.ADMIN);
        OAuthClientDAO.create("username", "encryptPassword2", AuthenticateRole.ADMIN);
    }

    @Test
    public void testGet() throws Exception {
        final String clientId = "clientId";
        final String clientSecret = "clientSecret";
        OAuthClientDAO.create(clientId, clientSecret, AuthenticateRole.ADMIN);

        final OAuthClient oAuthClient = OAuthClientDAO.getByClientId(clientId);
        assertThat(oAuthClient, notNullValue());
        assertThat(oAuthClient.getId(), equalTo(1L));
        assertThat(oAuthClient.getClientId(), equalTo(clientId));
        assertThat(oAuthClient.getHashedClientSecret(), equalTo(clientSecret));
        assertThat(oAuthClient.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(oAuthClient.getWeChatMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testGet_NotExist() throws Exception {
        final OAuthClient oAuthClient = OAuthClientDAO.getByClientId("clientId");
        assertThat(oAuthClient, nullValue());
    }

    @Test
    public void testSetMember() throws Exception {
        final String clientId = "clientId";
        final String clientSecret = "clientSecret";
        OAuthClientDAO.create(clientId, clientSecret, AuthenticateRole.ADMIN);

        OAuthClient oAuthClient = OAuthClientDAO.getByClientId(clientId);
        assertThat(oAuthClient.getWeChatMemberId().isPresent(), equalTo(false));

        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        OAuthClientDAO.setMember(clientId, memberId);

        oAuthClient = OAuthClientDAO.getByClientId(clientId);
        assertThat(oAuthClient.getWeChatMemberId().isPresent(), equalTo(true));
        assertThat(oAuthClient.getWeChatMemberId().get(), equalTo(memberId));
    }
}