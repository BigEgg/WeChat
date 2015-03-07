package com.thoughtworks.wechat_io.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.sql.Timestamp;

public interface EventLogDAO extends DAO {
    @SqlUpdate("INSERT INTO EventLog (MemberId, EventType, EventName, EventValue, HappenedTime)" +
            "   VALUES (:memberId, :eventType, :eventName, :eventValue, :happenedTime)")
    @GetGeneratedKeys
    long insertEventLog(@Bind("memberId") final long memberId,
                        @Bind("eventType") final String eventType,
                        @Bind("eventName") final String eventName,
                        @Bind("eventValue") final String eventValue,
                        @Bind("happenedTime") final Timestamp happenedTime);

    @SqlUpdate("INSERT INTO EventLog (MemberId, EventType, EventName, HappenedTime)" +
            "   VALUES (:memberId, :eventType, :eventName, :happenedTime)")
    @GetGeneratedKeys
    long insertEventLog(@Bind("memberId") final long memberId,
                        @Bind("eventType") final String eventType,
                        @Bind("eventName") final String eventName,
                        @Bind("happenedTime") final Timestamp happenedTime);
}
