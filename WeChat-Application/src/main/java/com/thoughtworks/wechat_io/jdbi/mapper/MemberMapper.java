package com.thoughtworks.wechat_io.jdbi.mapper;

import com.thoughtworks.wechat_io.core.Member;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberMapper implements ResultSetMapper<Member> {
    @Override
    public Member map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Member(
                resultSet.getLong("Id"),
                resultSet.getString("WeChatOpenId"),
                resultSet.getBoolean("Subscribed")
        );
    }
}
