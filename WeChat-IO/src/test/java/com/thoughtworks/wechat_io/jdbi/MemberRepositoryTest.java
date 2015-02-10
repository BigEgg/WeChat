package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Member;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MemberRepositoryTest extends AbstractRepositoryTest {
    private MemberRepository memberRepository;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        memberRepository = getRepository(MemberRepository.class);
    }

    @Test
    public void testCreateMember() throws Exception {
        long id = memberRepository.createMember("OpenId1", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = memberRepository.createMember("OpenId2", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateMember_FailedWithSameOpenId() throws Exception {
        memberRepository.createMember("OpenId", getHappenedTime());
        memberRepository.createMember("OpenId", getHappenedTime());
    }

    @Test
    public void testGetMemberById() throws Exception {
        long id = memberRepository.createMember("OpenId1", getHappenedTime());

        Member member = memberRepository.getMemberById(id);
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(id));
        assertThat(member.getWeChatOpenId(), equalTo("OpenId1"));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testGetMemberById_NotExist() throws Exception {
        Member member = memberRepository.getMemberById(0);
        assertThat(member, nullValue());
    }

    @Test
    public void testGetMemberByOpenId() throws Exception {
        String openId = "OpenId1";
        long id = memberRepository.createMember(openId, getHappenedTime());

        Member member = memberRepository.getMemberByOpenId(openId);
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(id));
        assertThat(member.getWeChatOpenId(), equalTo(openId));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testGetMemberByOpenId_NotExist() throws Exception {
        Member member = memberRepository.getMemberByOpenId("OpenId");
        assertThat(member, nullValue());
    }

    @Test
    public void testUpdateSubscribed() throws Exception {
        long id = memberRepository.createMember("OpenId1", getHappenedTime());
        Member member = memberRepository.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(true));

        memberRepository.updateSubscribed(id, false);
        member = memberRepository.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(false));

        memberRepository.updateSubscribed(id, true);
        member = memberRepository.getMemberById(id);
        assertThat(member.isSubscribed(), equalTo(true));
    }
}