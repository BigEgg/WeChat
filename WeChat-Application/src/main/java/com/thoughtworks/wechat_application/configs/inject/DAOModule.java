package com.thoughtworks.wechat_application.configs.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.wechat_application.jdbi.*;

public class DAOModule implements Module {
    private OAuthClientDAO OAuthClientDAO;
    private ConversationHistoryDAO conversationHistoryDAO;
    private WeChatEventLogDAO weChatEventLogDAO;
    private ExpirableResourceDAO expirableResourceDAO;
    private LabelDAO labelDAO;
    private MemberDAO memberDAO;
    private TextMessageDAO textMessageDAO;

    @Override
    public void configure(Binder binder) {
        binder.bind(OAuthClientDAO.class).toInstance(OAuthClientDAO);
        binder.bind(ConversationHistoryDAO.class).toInstance(conversationHistoryDAO);
        binder.bind(WeChatEventLogDAO.class).toInstance(weChatEventLogDAO);
        binder.bind(ExpirableResourceDAO.class).toInstance(expirableResourceDAO);
        binder.bind(LabelDAO.class).toInstance(labelDAO);
        binder.bind(MemberDAO.class).toInstance(memberDAO);
        binder.bind(TextMessageDAO.class).toInstance(textMessageDAO);
    }

    public OAuthClientDAO getOAuthClientDAO() {
        return OAuthClientDAO;
    }

    public void setOAuthClientDAO(OAuthClientDAO OAuthClientDAO) {
        this.OAuthClientDAO = OAuthClientDAO;
    }

    public ConversationHistoryDAO getConversationHistoryDAO() {
        return conversationHistoryDAO;
    }

    public void setConversationHistoryDAO(ConversationHistoryDAO conversationHistoryDAO) {
        this.conversationHistoryDAO = conversationHistoryDAO;
    }

    public WeChatEventLogDAO getWeChatEventLogDAO() {
        return weChatEventLogDAO;
    }

    public void setWeChatEventLogDAO(WeChatEventLogDAO weChatEventLogDAO) {
        this.weChatEventLogDAO = weChatEventLogDAO;
    }

    public ExpirableResourceDAO getExpirableResourceDAO() {
        return expirableResourceDAO;
    }

    public void setExpirableResourceDAO(ExpirableResourceDAO expirableResourceDAO) {
        this.expirableResourceDAO = expirableResourceDAO;
    }

    public LabelDAO getLabelDAO() {
        return labelDAO;
    }

    public void setLabelDAO(LabelDAO labelDAO) {
        this.labelDAO = labelDAO;
    }

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }

    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public TextMessageDAO getTextMessageDAO() {
        return textMessageDAO;
    }

    public void setTextMessageDAO(TextMessageDAO textMessageDAO) {
        this.textMessageDAO = textMessageDAO;
    }
}
