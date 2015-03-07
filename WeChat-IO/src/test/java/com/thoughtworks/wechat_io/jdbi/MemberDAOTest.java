package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.Member;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MemberDAOTest extends AbstractDAOTest {
    private MemberDAO memberDAO;
    private LabelDAO labelDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
        labelDAO = getDAO(LabelDAO.class);
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

        memberDAO.updateSubscribed(id, false, getHappenedTime());
        member = memberDAO.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(false));

        memberDAO.updateSubscribed(id, true, getHappenedTime());
        member = memberDAO.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testLinkMemberWithLabel() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkMemberWithLabel_NoMember() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        memberDAO.linkMemberWithLabel(1, labelId);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkMemberWithLabel_NoLabel() throws Exception {
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, 1);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkMemberWithLabel_Existed() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);
        memberDAO.linkMemberWithLabel(memberId, labelId);
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkMemberWithLabel_LinkTo2Label() throws Exception {
        final long labelId1 = labelDAO.createLabel("Label1", getHappenedTime());
        final long labelId2 = labelDAO.createLabel("Label2", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId1);
        memberDAO.linkMemberWithLabel(memberId, labelId2);
    }

    @Test
    public void testGetMembersByLabelIds() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);

        final List<Member> members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId));
        assertThat(members, notNullValue());
        assertThat(members.size(), equalTo(1));
        assertThat(members.get(0).getWeChatOpenId(), equalTo("OpenId"));
        assertThat(members.get(0).getId(), equalTo(memberId));
    }

    @Test
    public void testUpdateMemberLabel() throws Exception {
        final long labelId1 = labelDAO.createLabel("Label1", getHappenedTime());
        final long labelId2 = labelDAO.createLabel("Label2", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId1);
        memberDAO.updateMemberLabel(memberId, labelId2);

        List<Member> members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId1));
        assertThat(members, notNullValue());
        assertThat(members.size(), equalTo(0));
        members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId2));
        assertThat(members, notNullValue());
        assertThat(members.size(), equalTo(1));
    }

    @Test
    public void testUpdateMemberLabel_NoMember() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        memberDAO.updateMemberLabel(1, labelId);

        List<Member> members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId));
        assertThat(members.size(), equalTo(0));
    }

    @Test
    public void testUpdateMemberLabel_NoLabel() throws Exception {
        final long memberId = memberDAO.createMember("OpenId1", getHappenedTime());
        memberDAO.updateMemberLabel(memberId, 1);

        Label label = labelDAO.getMemberLabel(memberId);
        assertThat(label, nullValue());
    }

    @Test
    public void testDelinkMemberWithLabel() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);

        List<Member> members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId));
        assertThat(members, notNullValue());
        assertThat(members.size(), equalTo(1));

        memberDAO.delinkMemberWithLabel(memberId, labelId);
        members = memberDAO.getMembersByLabelIds(Arrays.asList(labelId));
        assertThat(members, notNullValue());
        assertThat(members.size(), equalTo(0));
    }

    @Test
    public void testDelinkMemberWithLabel_NotExist() throws Exception {
        memberDAO.delinkMemberWithLabel(1, 1);
    }
}