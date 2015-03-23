package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.ConversationHistory;
import com.thoughtworks.wechat_application.jdbi.mapper.ConversationHistoryMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;

@RegisterMapper(ConversationHistoryMapper.class)
public interface ConversationHistoryDAO {
    @SqlUpdate("INSERT INTO ConversationHistory (MemberId, WorkflowName, StartTime) VALUES (:memberId, :workflowName, :startTime)")
    @GetGeneratedKeys
    long createConversationHistory(@Bind("memberId") final long memberId,
                                   @Bind("workflowName") final String workflowName,
                                   @Bind("startTime") final Timestamp startTime);

    @SqlQuery("SELECT * FROM ConversationHistory WHERE MemberId = :memberId AND EndTime IS NULL")
    ConversationHistory getNotCompleteConversationHistoryByMemberId(@Bind("memberId") final long memberId);

    @SqlUpdate("UPDATE ConversationHistory SET Content = :content WHERE Id = :id")
    void updateContentById(@Bind("id") final long id,
                           @Bind("content") final String content);

    @SqlUpdate("UPDATE ConversationHistory SET EndTime = :endTime WHERE Id = :id")
    void updateEndTimeById(@Bind("id") final long id,
                           @Bind("endTime") final Timestamp endTime);
}
