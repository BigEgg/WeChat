package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.mapper.MemberMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.sql.Timestamp;
import java.util.List;

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

    @SqlUpdate("UPDATE Member SET Subscribed = :subscribed WHERE Id = :id")
    void updateSubscribed(@Bind("id") final long id,
                          @Bind("subscribed") final boolean subscribed,
                          @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlQuery("SELECT m.* FROM MemberLabelRelation AS r" +
            "  JOIN Member AS m" +
            "  ON m.Id = r.MemberId" +
            "  WHERE r.LabelId IN (<labelId>)")
    List<Member> getMembersByLabelIds(@BindIn("labelId") final List<Long> labelId);

    @SqlUpdate("INSERT INTO MemberLabelRelation (MemberId, LabelId, LinkedTime) VALUES (:memberId, :labelId, :linkedTime)")
    void linkMemberWithLabel(@Bind("memberId") final long memberId,
                             @Bind("labelId") final long labelId,
                             @Bind("linkedTime") final Timestamp linkedTime);

    @SqlUpdate("UPDATE MemberLabelRelation SET LabelId = :labelId, LinkedTime = :linkedTime WHERE MemberId = :memberId")
    void updateMemberLabel(@Bind("memberId") final long memberId,
                           @Bind("labelId") final long labelId,
                           @Bind("linkedTime") final Timestamp linkedTime);

    @SqlUpdate("DELETE FROM MemberLabelRelation WHERE MemberId = :memberId AND LabelId = :labelId")
    void delinkMemberWithLabel(@Bind("memberId") final long memberId,
                               @Bind("labelId") final long labelId);
}
