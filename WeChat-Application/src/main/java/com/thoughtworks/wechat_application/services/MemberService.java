package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.MemberDAO;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class MemberService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    private MemberDAO memberDAO;

    @Inject
    public MemberService(final MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public Optional<Member> getMemberByOpenId(final String openId) {
        checkNotBlank(openId);

        Optional<Member> member = Optional.ofNullable(memberDAO.getMemberByOpenId(openId));
        LOGGER.info("[GetMemberByOpenId] Try get member by open id: {}. Status: {}.", openId, member.isPresent());
        return member;
    }

    public Member subscribeMember(final String openId) {
        checkNotBlank(openId);

        LOGGER.info("[SubscribeMember] Try subscribe member: {}", openId);
        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null) {
            if (!member.isSubscribed()) {
                memberDAO.updateSubscribed(member.getId(), true, toUnixTimestamp(DateTime.now()));
                LOGGER.info("[SubscribeMember] An unsubscribe member(id: {}, OpenId: {}) existed, mark as subscribe.", member.getId(), openId);
            } else {
                LOGGER.info("[SubscribeMember] An subscribed member(id: {}, OpenId: {}) existed, skip.", member.getId(), openId);
            }
        } else {
            long memberId = memberDAO.createMember(openId, toUnixTimestamp(DateTime.now()));
            LOGGER.info("[SubscribeMember] Create new member, id: {}, OpenId: {}.", memberId, openId);
        }
        return memberDAO.getMemberByOpenId(openId);
    }

    public void unsubscribeMember(final String openId) {
        checkNotBlank(openId);

        LOGGER.info("[UnsubscribeMember] Try unsubscribe member, open id: {}.", openId);
        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null && member.isSubscribed()) {
            memberDAO.updateSubscribed(member.getId(), false, toUnixTimestamp(DateTime.now()));
            LOGGER.info("[UnsubscribeMember] Mark member(id: {}, OpenId: {}) to unsubscribe.", member.getId(), openId);
        }
    }
}
