package com.thoughtworks.wechat_application.jdbi.mapper;

import com.thoughtworks.wechat_application.core.Label;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelMapper implements ResultSetMapper<Label> {
    @Override
    public Label map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Label(
                resultSet.getLong("Id"),
                resultSet.getString("Title")
        );
    }
}
