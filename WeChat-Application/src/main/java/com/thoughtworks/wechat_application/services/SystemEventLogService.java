package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.SystemEventLogDAO;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import org.joda.time.DateTime;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;

@Singleton
public class SystemEventLogService {
    private final SystemEventLogDAO systemEventLogDAO;
    private final OAuthSystemEventLogService oAuthSystemEventLogService;

    @Inject
    public SystemEventLogService(final SystemEventLogDAO systemEventLogDAO) {
        this.systemEventLogDAO = systemEventLogDAO;

        oAuthSystemEventLogService = new OAuthSystemEventLogService();
    }

    public OAuthSystemEventLogService oAuth() {
        return oAuthSystemEventLogService;
    }


    public class OAuthSystemEventLogService {
        private final String EVENT_NAME = "OAuth";
        private final String GET_ACCESS_TOKEN_EVENT = "GetAccessToken";
        private final String REFRESH_ACCESS_TOKEN_EVENT = "RefreshAccessToken";

        public void accessToken(final OAuthClient client, final DateTime happenedTime) {
            systemEventLogDAO.insertEventLog(client.getId(), EVENT_NAME, GET_ACCESS_TOKEN_EVENT, toUnixTimestamp(happenedTime));
        }

        public void refresh(final OAuthClient client, final DateTime happenedTime) {
            systemEventLogDAO.insertEventLog(client.getId(), EVENT_NAME, REFRESH_ACCESS_TOKEN_EVENT, toUnixTimestamp(happenedTime));
        }
    }
}
