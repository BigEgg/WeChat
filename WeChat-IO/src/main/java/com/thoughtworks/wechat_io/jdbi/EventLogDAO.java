package com.thoughtworks.wechat_io.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.sql.Timestamp;

public interface EventLogDAO extends DAO {
    @SqlUpdate("INSERT INTO EventLog (MemberId, EventType, EventName, EventValue, HappenedTime)" +
            "   VALUES (:memberId, :eventType, :eventName, :eventValue, :happenedTime)")
    @GetGeneratedKeys
    public long insertEventLog(@Bind("memberId") long memberId,
                               @Bind("eventType") String eventType,
                               @Bind("eventName") String eventName,
                               @Bind("eventValue") String eventValue,
                               @Bind("happenedTime") Timestamp happenedTime);

    @SqlUpdate("INSERT INTO EventLog (MemberId, EventType, EventName, HappenedTime)" +
            "   VALUES (:memberId, :eventType, :eventName, :happenedTime)")
    @GetGeneratedKeys
    public long insertEventLog(@Bind("memberId") long memberId,
                               @Bind("eventType") String eventType,
                               @Bind("eventName") String eventName,
                               @Bind("happenedTime") Timestamp happenedTime);
}
