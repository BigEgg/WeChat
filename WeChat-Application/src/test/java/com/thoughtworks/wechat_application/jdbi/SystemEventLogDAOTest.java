package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SystemEventLogDAOTest extends AbstractDAOTest {
    private OAuthClientDAO oAuthClientDAO;
    private SystemEventLogDAO systemEventLogDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        oAuthClientDAO = getDAO(OAuthClientDAO.class);
        systemEventLogDAO = getDAO(SystemEventLogDAO.class);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testInsertEventLog_NoRelatedMember() throws Exception {
        systemEventLogDAO.insertEventLog(1, "OAuth", "AccessToken", "some value", getHappenedTime());
    }

    @Test
    public void testInsertEventLog_WithEventValue() throws Exception {
        final long clientId = oAuthClientDAO.create("clientId", "clientSecret", AuthenticateRole.ADMIN, getHappenedTime());

        long id = systemEventLogDAO.insertEventLog(clientId, "OAuth", "AccessToken", "some value", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = systemEventLogDAO.insertEventLog(clientId, "OAuth", "AccessToken", "some value", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test
    public void testInsertEventLog_WithoutEventValue() throws Exception {
        final long clientId = oAuthClientDAO.create("clientId", "clientSecret", AuthenticateRole.ADMIN, getHappenedTime());

        long id = systemEventLogDAO.insertEventLog(clientId, "Update", "WelcomeMessage", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = systemEventLogDAO.insertEventLog(clientId, "Update", "WelcomeMessage", getHappenedTime());
        assertThat(id, equalTo(2L));
    }
}