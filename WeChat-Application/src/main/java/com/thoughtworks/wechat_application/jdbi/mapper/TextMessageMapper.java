package com.thoughtworks.wechat_application.jdbi.mapper;

import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TextMessageMapper implements ResultSetMapper<TextMessage> {
    @Override
    public TextMessage map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new TextMessage(
                resultSet.getLong("Id"),
                resultSet.getString("Title"),
                resultSet.getString("Content")
        );
    }
}
