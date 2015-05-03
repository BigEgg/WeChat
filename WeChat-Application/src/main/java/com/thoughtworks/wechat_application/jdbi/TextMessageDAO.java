package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.jdbi.mapper.TextMessageMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.sql.Timestamp;
import java.util.List;

@RegisterMapper(TextMessageMapper.class)
@UseStringTemplate3StatementLocator
public interface TextMessageDAO {
    @SqlUpdate("INSERT INTO TextMessage (Title, Content, CreatedTime, ModifiedTime) VALUES (:title, :content, :createdTime, :createdTime)")
    @GetGeneratedKeys
    public long createTextMessage(@Bind("title") final String title,
                                  @Bind("content") final String content,
                                  @Bind("createdTime") final Timestamp createdTime);

    @SqlQuery("SELECT * FROM TextMessage WHERE Title = :title")
    public TextMessage getTextMessageByTitle(@Bind("title") final String title);

    @SqlUpdate("UPDATE TextMessage SET Content = :content, ModifiedTime = :modifiedTime WHERE Title = :title")
    public void updateContent(@Bind("title") final String title,
                              @Bind("content") final String content,
                              @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlQuery("SELECT * FROM TextMessage WHERE Title NOT LIKE CONCAT(:admin_prefix, '%')")
    public List<TextMessage> getAllMessages(@Bind("admin_prefix") final String adminPrefix);

    @SqlUpdate("DELETE FROM TextMessage WHERE Id = :id")
    public void deleteMessage(@Bind("id") final long id);

    @SqlQuery("SELECT m.* FROM TextMessageLabelRelation AS r" +
            "  JOIN TextMessage AS m" +
            "  ON m.Id = r.TextMessageId" +
            "  WHERE r.LabelId IN (<labelId>)")
    List<TextMessage> getTextMessageByLabelIds(@BindIn("labelId") final List<Long> labelId);

    @SqlUpdate("INSERT INTO TextMessageLabelRelation (TextMessageId, LabelId, LinkedTime) VALUES (:textMessageId, :labelId, :linkedTime)")
    void linkTextMessageWithLabel(@Bind("textMessageId") final long textMessageId,
                                  @Bind("labelId") final long labelId,
                                  @Bind("linkedTime") final Timestamp linkedTime);

    @SqlUpdate("DELETE FROM TextMessageLabelRelation WHERE TextMessageId = :textMessageId AND LabelId = :labelId")
    void delinkTextMessageWithLabel(@Bind("textMessageId") final long textMessageId,
                                    @Bind("labelId") final long labelId);
}
