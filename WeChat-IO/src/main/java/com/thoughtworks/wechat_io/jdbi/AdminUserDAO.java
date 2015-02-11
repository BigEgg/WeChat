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
    @SqlQuery("SELECT * FROM AdminUser WHERE Username = :username AND Password = :password")
    public AdminUser getAdminUserByUsernameAndPassword(@Bind("username") String username,
                                                       @Bind("password") String password);

    @SqlUpdate("INSERT INTO AdminUser (UserName, Password) VALUES (:username, :password)")
    @GetGeneratedKeys
    public long createAdminUser(@Bind("username") String username,
                                @Bind("password") String password);

    @SqlUpdate("UPDATE AdminUser SET MemberId = :memberId")
    public void setMember(@Bind("memberId") long memberId);
}
