package com.thoughtworks.wechat_application.jdbi.mapper;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class OAuthClientMapper implements ResultSetMapper<OAuthClient> {
    @Override
    public OAuthClient map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new OAuthClient(
                resultSet.getLong("Id"),
                resultSet.getString("ClientId"),
                resultSet.getString("ClientSecret"),
                AuthenticateRole.valueOf(resultSet.getString("Role")),
                resultSet.getLong("WeChatMemberId") == 0 ? Optional.<Long>empty() : Optional.of(resultSet.getLong("WeChatMemberId"))
        );
    }
}
