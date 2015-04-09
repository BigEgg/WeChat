package com.thoughtworks.wechat_application.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.sql.Timestamp;

public interface SystemEventLogDAO {
    @SqlUpdate("INSERT INTO SystemEventLog (ClientId, EventType, EventName, EventValue, HappenedTime)" +
            "   VALUES (:clientId, :eventType, :eventName, :eventValue, :happenedTime)")
    @GetGeneratedKeys
    long insertEventLog(@Bind("clientId") final long clientId,
                        @Bind("eventType") final String eventType,
                        @Bind("eventName") final String eventName,
                        @Bind("eventValue") final String eventValue,
                        @Bind("happenedTime") final Timestamp happenedTime);

    @SqlUpdate("INSERT INTO SystemEventLog (ClientId, EventType, EventName, HappenedTime)" +
            "   VALUES (:clientId, :eventType, :eventName, :happenedTime)")
    @GetGeneratedKeys
    long insertEventLog(@Bind("clientId") final long clientId,
                        @Bind("eventType") final String eventType,
                        @Bind("eventName") final String eventName,
                        @Bind("happenedTime") final Timestamp happenedTime);
}
