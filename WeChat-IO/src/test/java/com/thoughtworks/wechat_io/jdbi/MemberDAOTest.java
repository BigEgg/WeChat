package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Member;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MemberDAOTest extends AbstractDAOTest {
    private MemberDAO memberDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
    }

    @Test
    public void testCreateMember() throws Exception {
        long id = memberDAO.createMember("OpenId1", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = memberDAO.createMember("OpenId2", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateMember_FailedWithSameOpenId() throws Exception {
        memberDAO.createMember("OpenId", getHappenedTime());
        memberDAO.createMember("OpenId", getHappenedTime());
    }

    @Test
    public void testGetMemberById() throws Exception {
        final long id = memberDAO.createMember("OpenId1", getHappenedTime());

        final Member member = memberDAO.getMemberById(id);
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(id));
        assertThat(member.getWeChatOpenId(), equalTo("OpenId1"));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testGetMemberById_NotExist() throws Exception {
        final Member member = memberDAO.getMemberById(0);
        assertThat(member, nullValue());
    }

    @Test
    public void testGetMemberByOpenId() throws Exception {
        final String openId = "OpenId1";
        final long id = memberDAO.createMember(openId, getHappenedTime());

        final Member member = memberDAO.getMemberByOpenId(openId);
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(id));
        assertThat(member.getWeChatOpenId(), equalTo(openId));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testGetMemberByOpenId_NotExist() throws Exception {
        final Member member = memberDAO.getMemberByOpenId("OpenId");
        assertThat(member, nullValue());
    }

    @Test
    public void testUpdateSubscribed() throws Exception {
        final long id = memberDAO.createMember("OpenId1", getHappenedTime());
        Member member = memberDAO.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(true));

        memberDAO.updateSubscribed(id, false);
        member = memberDAO.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(false));

        memberDAO.updateSubscribed(id, true);
        member = memberDAO.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(true));
    }
}