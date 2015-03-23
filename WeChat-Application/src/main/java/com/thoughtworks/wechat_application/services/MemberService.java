package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.core.Label;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.MemberDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class MemberService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    private MemberDAO memberDAO;
    private LabelService labelService;

    @Inject
    public MemberService(final MemberDAO memberDAO, final LabelService labelService) {
        this.memberDAO = memberDAO;
        this.labelService = labelService;
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

    public void linkMemberToLabel(final Member member, final Label label) {
        checkNotNull(member);
        checkNotNull(label);

        LOGGER.info("[LinkMemberToLabel] Try link member(id: {}) to label(id: {}).", member.getId(), label.getId());
        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.updateMemberLabel(member.getId(), label.getId(), toUnixTimestamp(DateTime.now()));
            LOGGER.info("[LinkMemberToLabel] Member(id: {}) already have a label(id: {}), update it.", member.getId(), currentLabel.get().getId());
        } else {
            memberDAO.linkMemberWithLabel(member.getId(), label.getId(), toUnixTimestamp(DateTime.now()));
            LOGGER.info("[LinkMemberToLabel] Link member(id: {}) to label success.", member.getId());
        }
    }

    public void delinkMemberLabel(final Member member) {
        checkNotNull(member);

        LOGGER.info("[DelinkMemberLabel] Try delink member(id: {})'s label.", member.getId());
        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.delinkMemberWithLabel(member.getId(), currentLabel.get().getId());
            LOGGER.info("[DelinkMemberLabel] Delink member(id: {}) from label(id: {}).", member.getId(), currentLabel.get().getId());
        } else {
            LOGGER.info("[DelinkMemberLabel] Member(id: {}) don't have label. Skip.", member.getId());
        }
    }
}
