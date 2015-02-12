package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.TextMessage;
import com.thoughtworks.wechat_io.jdbi.mapper.TextMessageMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;
import java.util.List;

@RegisterMapper(TextMessageMapper.class)
public interface TextMessageDAO extends DAO {
    @SqlUpdate("INSERT INTO TextMessage (Title, Content, CreatedTime, ModifiedTime) VALUES (:title, :content, :createdTime, :createdTime)")
    @GetGeneratedKeys
    public long createTextMessage(@Bind("title") String title,
                                  @Bind("content") String content,
                                  @Bind("createdTime") Timestamp createdTime);

    @SqlQuery("SELECT * FROM TextMessage")
    public List<TextMessage> getAllMessages();

    @SqlUpdate("DELETE FROM TextMessage WHERE Id = :id")
    public void deleteMessage(@Bind("id") long id);
}
