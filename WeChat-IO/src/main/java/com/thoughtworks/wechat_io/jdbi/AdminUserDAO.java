package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.AdminUser;
import com.thoughtworks.wechat_io.jdbi.mapper.AdminUserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(AdminUserMapper.class)
public interface AdminUserDAO extends DAO {
    @SqlQuery("SELECT * FROM AdminUser WHERE Username = :username")
    AdminUser getAdminUserByUsername(@Bind("username") final String username);

    @SqlUpdate("INSERT INTO AdminUser (UserName, Password) VALUES (:username, :password)")
    @GetGeneratedKeys
    long createAdminUser(@Bind("username") final String username,
                         @Bind("password") final String password);

    @SqlUpdate("UPDATE AdminUser SET MemberId = :memberId WHERE Id = :adminUserId")
    void setMember(@Bind("adminUserId") final long adminUserId, @Bind("memberId") final long memberId);

    @SqlQuery("SELECT * FORM AdminUser WHERE MemberId = :memberId")
    AdminUser getAdminUserByMemberId(@Bind("memberId") final long memberId);
}
