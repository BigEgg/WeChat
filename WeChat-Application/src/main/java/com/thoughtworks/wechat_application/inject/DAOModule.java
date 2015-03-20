package com.thoughtworks.wechat_application.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.wechat_application.jdbi.*;

public class DAOModule implements Module {
    private AdminUserDAO adminUserDAO;
    private ConversationHistoryDAO conversationHistoryDAO;
    private EventLogDAO eventLogDAO;
    private ExpirableResourceDAO expirableResourceDAO;
    private LabelDAO labelDAO;
    private MemberDAO memberDAO;
    private TextMessageDAO textMessageDAO;

    @Override
    public void configure(Binder binder) {
        binder.bind(AdminUserDAO.class).toInstance(adminUserDAO);
        binder.bind(ConversationHistoryDAO.class).toInstance(conversationHistoryDAO);
        binder.bind(EventLogDAO.class).toInstance(eventLogDAO);
        binder.bind(ExpirableResourceDAO.class).toInstance(expirableResourceDAO);
        binder.bind(LabelDAO.class).toInstance(labelDAO);
        binder.bind(MemberDAO.class).toInstance(memberDAO);
        binder.bind(TextMessageDAO.class).toInstance(textMessageDAO);
    }

    public AdminUserDAO getAdminUserDAO() {
        return adminUserDAO;
    }

    public void setAdminUserDAO(AdminUserDAO adminUserDAO) {
        this.adminUserDAO = adminUserDAO;
    }

    public ConversationHistoryDAO getConversationHistoryDAO() {
        return conversationHistoryDAO;
    }

    public void setConversationHistoryDAO(ConversationHistoryDAO conversationHistoryDAO) {
        this.conversationHistoryDAO = conversationHistoryDAO;
    }

    public EventLogDAO getEventLogDAO() {
        return eventLogDAO;
    }

    public void setEventLogDAO(EventLogDAO eventLogDAO) {
        this.eventLogDAO = eventLogDAO;
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
