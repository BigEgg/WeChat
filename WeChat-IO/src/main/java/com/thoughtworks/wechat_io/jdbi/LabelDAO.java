package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.jdbi.mapper.LabelMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;
import java.util.List;

@RegisterMapper(LabelMapper.class)
public interface LabelDAO extends DAO {
    @SqlUpdate("INSERT INTO Label (Name, CreatedTime) VALUES (:name, :createdTime)")
    @GetGeneratedKeys
    long createLabel(@Bind("name") final String name,
                     @Bind("createdTime") final Timestamp createdTime);

    @SqlQuery("SELECT * FROM Label")
    List<Label> getAllLabel();

    @SqlUpdate("DELETE FROM Label WHERE Id = :id")
    void deleteLabel(@Bind("id") final long id);

    @SqlQuery("SELECT l.* FROM MemberLabelRelation AS r" +
            "  JOIN Label AS l" +
            "  ON l.Id = r.LabelId" +
            "  WHERE r.MemberId = :memberId")
    List<Label> getMemberLabels(@Bind("memberId") final long memberId);
}
