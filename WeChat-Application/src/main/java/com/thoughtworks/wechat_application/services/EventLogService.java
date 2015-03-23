package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.EventLogDAO;
import org.joda.time.DateTime;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;

@Singleton
public class EventLogService {
    private final EventLogDAO eventLogDAO;
    private final MemberEventLogService memberEventLogService;

    @Inject
    public EventLogService(final EventLogDAO eventLogDAO) {
        this.eventLogDAO = eventLogDAO;

        memberEventLogService = new MemberEventLogService();
    }

    public MemberEventLogService member() {
        return memberEventLogService;
    }


    public class MemberEventLogService {
        private final String EVENT_NAME = "Member";
        private final String SUBSCRIBE_EVENT = "Subscribe";
        private final String UNSUBSCRIBE_EVENT = "Unsubscribe";

        public void subscribe(final Member member, final DateTime happenedTime) {
            eventLogDAO.insertEventLog(member.getId(), EVENT_NAME, SUBSCRIBE_EVENT, toUnixTimestamp(happenedTime));
        }

        public void unsubscribe(final Member member, final DateTime happenedTime) {
            eventLogDAO.insertEventLog(member.getId(), EVENT_NAME, UNSUBSCRIBE_EVENT, toUnixTimestamp(happenedTime));
        }
    }
}
