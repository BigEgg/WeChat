package com.thoughtworks.wechat_io.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.Member;
import com.thoughtworks.wechat_io.jdbi.MemberDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;
import static org.apache.commons.lang.Validate.notNull;

@Singleton
public class MemberService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    private MemberDAO memberDAO;
    private LabelService labelService;

    @Inject
    public MemberService(MemberDAO memberDAO, LabelService labelService) {
        this.memberDAO = memberDAO;
        this.labelService = labelService;
    }

    public Optional<Member> getMemberByOpenId(String openId) {
        checkNotBlank(openId);

        Optional<Member> member = Optional.ofNullable(memberDAO.getMemberByOpenId(openId));
        LOGGER.info("[GetMemberByOpenId] Try get member by open id: {}. Status: {}.", openId, member.isPresent());
        return member;
    }

    public Member subscribeMember(String openId) {
        checkNotBlank(openId);

        LOGGER.info("[SubscribeMember] Try subscribe member: {}", openId);
        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null) {
            if (!member.isSubscribed()) {
                memberDAO.updateSubscribed(member.getId(), true);
                LOGGER.info("[SubscribeMember] An unsubscribe member existed, mark as subscribe.");
            } else {
                LOGGER.info("[SubscribeMember] An subscribed member existed, skip.");
            }
        } else {
            long memberId = memberDAO.createMember(openId, toUnixTimestamp(DateTime.now()));
            LOGGER.info("[SubscribeMember] Create new member, id: {}.", memberId);
        }
        return memberDAO.getMemberByOpenId(openId);
    }

    public void unsubscribeMember(String openId) {
        checkNotBlank(openId);

        LOGGER.info("[UnsubscribeMember] Try unsubscribe member: {}.", openId);
        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null && member.isSubscribed()) {
            memberDAO.updateSubscribed(member.getId(), false);
            LOGGER.info("[UnsubscribeMember] Mark member(id: {}) to unsubscribe.", openId);
        }
    }

    public void linkMemberToLabel(Member member, Label label) {
        notNull(member);
        notNull(label);

        LOGGER.info("[LinkMemberToLabel] Try link member(id: {}) to label(id: {}).", member.getId(), label.getId());
        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.updateMemberLabel(member.getId(), label.getId());
            LOGGER.info("[LinkMemberToLabel] Member already have a label(id: {}), update it.", currentLabel.get().getId());
        } else {
            memberDAO.linkMemberWithLabel(member.getId(), label.getId());
            LOGGER.info("[LinkMemberToLabel] Link member to label success.");
        }
    }

    public void delinkMemberLabel(Member member) {
        notNull(member);

        LOGGER.info("[DelinkMemberLabel] Try delink member(id: {})'s label.", member.getId());
        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.delinkMemberWithLabel(member.getId(), currentLabel.get().getId());
            LOGGER.info("[DelinkMemberLabel] Delink member from label(id: {}).", currentLabel.get().getId());
        }else {
            LOGGER.info("[DelinkMemberLabel] Member don't have label. Skip.");
        }
    }
}
