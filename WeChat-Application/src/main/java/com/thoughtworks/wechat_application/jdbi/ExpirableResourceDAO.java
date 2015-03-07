package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.mapper.ExpirableResourceMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.sql.Timestamp;
import java.util.List;

@RegisterMapper(ExpirableResourceMapper.class)
@UseStringTemplate3StatementLocator
public interface ExpirableResourceDAO extends DAO {
    @SqlUpdate("INSERT INTO ExpirableResources (Key, Type, Value, ExpiresInSecond, CreatedTime, ModifiedTime)" +
            "   VALUES (:key, :type, :value, :expiresInSecond, :createTime, :modifiedTime)")
    @GetGeneratedKeys
    long createResource(@Bind("key") final String key,
                        @Bind("type") final String type,
                        @Bind("value") final String value,
                        @Bind("expiresInSecond") final int expiresInSecond,
                        @Bind("createTime") final Timestamp createdTime,
                        @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlUpdate("UPDATE ExpirableResources " +
            "   SET Value = :value, ExpiresInSecond = :expiresInSecond, CreatedTime = :createTime, ModifiedTime = :modifiedTime" +
            "   WHERE Key = :key AND Type = :type")
    void updateResource(@Bind("key") final String key,
                        @Bind("type") final String type,
                        @Bind("value") final String value,
                        @Bind("expiresInSecond") final int expiresInSecond,
                        @Bind("createTime") final Timestamp createdTime,
                        @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlQuery("SELECT * FROM ExpirableResources WHERE Key = :key AND Type = :type")
    ExpirableResource getResource(@Bind("key") final String key,
                                  @Bind("type") final String type);

    @SqlUpdate("DELETE FROM ExpirableResources WHERE Id IN (<ids>)")
    void deleteResources(@BindIn("ids") final List<Long> ids);
}
