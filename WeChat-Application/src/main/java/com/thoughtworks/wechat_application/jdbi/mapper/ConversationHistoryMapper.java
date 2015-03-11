package com.thoughtworks.wechat_application.jdbi.mapper;

import com.thoughtworks.wechat_application.core.ConversationHistory;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUTCDateTime;

public class ConversationHistoryMapper implements ResultSetMapper<ConversationHistory> {
    @Override
    public ConversationHistory map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new ConversationHistory(
                resultSet.getLong("Id"),
                resultSet.getLong("MemberId"),
                resultSet.getString("WorkflowName"),
                toUTCDateTime(resultSet.getDate("StartTime").getTime()),
                resultSet.getDate("EndTime") == null ? Optional.<DateTime>empty() : Optional.of(toUTCDateTime(resultSet.getDate("EndTime").getTime())),
                Optional.ofNullable(resultSet.getString("Content"))
        );
    }
}
