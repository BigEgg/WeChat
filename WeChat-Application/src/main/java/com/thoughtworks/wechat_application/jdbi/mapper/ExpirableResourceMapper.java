package com.thoughtworks.wechat_application.jdbi.mapper;

import com.thoughtworks.wechat_application.jdbi.core.ExpirableResource;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUTCDateTime;

public class ExpirableResourceMapper implements ResultSetMapper<ExpirableResource> {

    @Override
    public ExpirableResource map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new ExpirableResource(
                resultSet.getString("Key"),
                resultSet.getString("Type"),
                resultSet.getString("Value"),
                resultSet.getInt("ExpiresInSecond"),
                toUTCDateTime(resultSet.getTimestamp("CreatedTime").getTime())
        );
    }
}
