package com.thoughtworks.wechat_io.jdbi.mapper;

import com.thoughtworks.wechat_io.core.AdminUser;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminUserMapper implements ResultSetMapper<AdminUser> {
    @Override
    public AdminUser map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new AdminUser(
                resultSet.getLong("Id"),
                resultSet.getString("Username"),
                resultSet.getString("Password"),
                resultSet.getLong("MemberId") == 0 ? Optional.<Long>empty() : Optional.of(resultSet.getLong("MemberId"))
        );
    }
}
