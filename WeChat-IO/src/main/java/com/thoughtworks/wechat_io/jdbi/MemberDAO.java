package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Member;
import com.thoughtworks.wechat_io.jdbi.mapper.MemberMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;

@RegisterMapper(MemberMapper.class)
public interface MemberDAO extends DAO {
    @SqlQuery("SELECT * FROM Member WHERE WeChatOpenId = :openId")
    Member getMemberByOpenId(@Bind("openId") String openId);

    @SqlQuery("SELECT * FROM Member WHERE Id = :id")
    Member getMemberById(@Bind("id") long id);

    @SqlUpdate("INSERT INTO Member (WeChatOpenId, CreatedTime, ModifiedTime) VALUES (:openId, :createdTime, :createdTime)")
    @GetGeneratedKeys
    long createMember(@Bind("openId") String openId,
                      @Bind("createdTime") Timestamp createdTime);

    @SqlUpdate("UPDATE Member SET Subscribed = :subscribed WHERE Id = :id")
    void updateSubscribed(@Bind("id") long id,
                          @Bind("subscribed") boolean subscribed);
}
