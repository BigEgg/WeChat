package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.jdbi.mapper.OAuthClientMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(OAuthClientMapper.class)
public interface OAuthClientDAO {
    @SqlQuery("SELECT * FROM OAuthClient WHERE ClientId = :clientId")
    OAuthClient getByClientId(@Bind("clientId") final String clientId);

    @SqlUpdate("INSERT INTO OAuthClient (ClientId, ClientSecret, Role) VALUES (:clientId, :clientSecret, :role)")
    @GetGeneratedKeys
    long create(@Bind("clientId") final String clientId,
                @Bind("clientSecret") final String hashedClientSecret,
                @Bind("role") AuthenticateRole authenticateRole);

    @SqlUpdate("UPDATE OAuthClient SET WeChatMemberId = :memberId WHERE clientId = :clientId")
    void setMember(@Bind("clientId") final String clientId, @Bind("memberId") final long memberId);
}
