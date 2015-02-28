package com.thoughtworks.wechat_io.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.Member;
import com.thoughtworks.wechat_io.jdbi.MemberDAO;
import org.joda.time.DateTime;

import java.util.Optional;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;
import static org.apache.commons.lang.Validate.notNull;

@Singleton
public class MemberService {
    private MemberDAO memberDAO;
    private LabelService labelService;

    @Inject
    public MemberService(MemberDAO memberDAO, LabelService labelService) {
        this.memberDAO = memberDAO;
        this.labelService = labelService;
    }

    public Optional<Member> getMemberByOpenId(String openId) {
        checkNotBlank(openId);

        Member member = memberDAO.getMemberByOpenId(openId);
        return Optional.ofNullable(member);
    }

    public Member subscribeMember(String openId) {
        checkNotBlank(openId);

        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null) {
            if (!member.isSubscribed()) {
                memberDAO.updateSubscribed(member.getId(), true);
            }
        } else {
            memberDAO.createMember(openId, toUnixTimestamp(DateTime.now()));
        }
        return memberDAO.getMemberByOpenId(openId);
    }

    public void unsubscribeMember(String openId) {
        checkNotBlank(openId);

        Member member = memberDAO.getMemberByOpenId(openId);
        if (member != null && member.isSubscribed()) {
            memberDAO.updateSubscribed(member.getId(), false);
        }
    }

    public void linkMemberToLabel(Member member, Label label) {
        notNull(member);
        notNull(label);

        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.updateMemberLabel(member.getId(), label.getId());
        } else {
            memberDAO.linkMemberWithLabel(member.getId(), label.getId());
        }
    }

    public void delinkMemberLabel(Member member) {
        notNull(member);

        Optional<Label> currentLabel = labelService.getMemberLabels(member);
        if (currentLabel.isPresent()) {
            memberDAO.delinkMemberWithLabel(member.getId(), currentLabel.get().getId());
        }
    }
}
