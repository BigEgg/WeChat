package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.mapper.ExpirableResourceMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;

@RegisterMapper(ExpirableResourceMapper.class)
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
            "   SET Value = :value, ExpiresInSecond = :expiresInSecond, ModifiedTime = :modifiedTime" +
            "   WHERE Key = :key AND Type = :type")
    void updateResource(@Bind("key") final String key,
                        @Bind("type") final String type,
                        @Bind("value") final String value,
                        @Bind("expiresInSecond") final int expiresInSecond,
                        @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlQuery("SELECT * FROM ExpirableResources WHERE Key = :key AND Type = :type")
    ExpirableResource getResource(@Bind("key") final String key,
                                  @Bind("type") final String type);

    @SqlUpdate("DELETE FROM ExpirableResources WHERE Key = :key AND Type = :type")
    void deleteResources(@Bind("key") final String key,
                         @Bind("type") final String type);
}
