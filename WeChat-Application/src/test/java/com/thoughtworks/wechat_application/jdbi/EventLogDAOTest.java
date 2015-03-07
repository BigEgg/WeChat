package com.thoughtworks.wechat_application.jdbi;

import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class EventLogDAOTest extends AbstractDAOTest {
    private MemberDAO memberDAO;
    private EventLogDAO eventLogDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
        eventLogDAO = getDAO(EventLogDAO.class);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testInsertEventLog_NoRelatedMember() throws Exception {
        eventLogDAO.insertEventLog(1, "Redirect", "SomePage", "Parameters", getHappenedTime());
    }

    @Test
    public void testInsertEventLog_WithEventValue() throws Exception {
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());

        long id = eventLogDAO.insertEventLog(memberId, "Redirect", "SomePage", "Parameters", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = eventLogDAO.insertEventLog(memberId, "Redirect", "SomePage", "Parameters", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test
    public void testInsertEventLog_WithoutEventValue() throws Exception {
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());

        long id = eventLogDAO.insertEventLog(memberId, "ClickMenu", "ViewLatestNews", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = eventLogDAO.insertEventLog(memberId, "ClickMenu", "ViewLatestNews", getHappenedTime());
        assertThat(id, equalTo(2L));
    }
}