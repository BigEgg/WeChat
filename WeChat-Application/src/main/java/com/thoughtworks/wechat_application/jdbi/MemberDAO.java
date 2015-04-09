package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.mapper.MemberMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.sql.Timestamp;

@RegisterMapper(MemberMapper.class)
@UseStringTemplate3StatementLocator
public interface MemberDAO {
    @SqlQuery("SELECT * FROM Member WHERE WeChatOpenId = :openId")
    Member getMemberByOpenId(@Bind("openId") final String openId);

    @SqlQuery("SELECT * FROM Member WHERE Id = :id")
    Member getMemberById(@Bind("id") final long id);

    @SqlUpdate("INSERT INTO Member (WeChatOpenId, CreatedTime, ModifiedTime) VALUES (:openId, :createdTime, :createdTime)")
    @GetGeneratedKeys
    long createMember(@Bind("openId") final String openId,
                      @Bind("createdTime") final Timestamp createdTime);

    @SqlUpdate("UPDATE Member SET Subscribed = :subscribed, ModifiedTime = :modifiedTime WHERE Id = :id")
    void updateSubscribed(@Bind("id") final long id,
                          @Bind("subscribed") final boolean subscribed,
                          @Bind("modifiedTime") final Timestamp modifiedTime);
}
