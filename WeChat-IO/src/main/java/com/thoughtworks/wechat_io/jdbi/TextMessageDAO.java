package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.TextMessage;
import com.thoughtworks.wechat_io.jdbi.mapper.TextMessageMapper;
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
public interface TextMessageDAO extends DAO {
    @SqlUpdate("INSERT INTO TextMessage (Title, Content, CreatedTime, ModifiedTime) VALUES (:title, :content, :createdTime, :createdTime)")
    @GetGeneratedKeys
    public long createTextMessage(@Bind("title") final String title,
                                  @Bind("content") final String content,
                                  @Bind("createdTime") final Timestamp createdTime);

    @SqlQuery("SELECT * FROM TextMessage WHERE Title = :title")
    public TextMessage getTextMessageByTitle(@Bind("title") final String title);

    @SqlUpdate("UPDATE TextMessage SET Content = :content, ModifiedTime = :modifiedTime WHERE :title = title")
    public void updateContent(@Bind("title") final String title,
                              @Bind("content") final String content,
                              @Bind("modifiedTime") final Timestamp modifiedTime);

    @SqlQuery("SELECT * FROM TextMessage")
    public List<TextMessage> getAllMessages();

    @SqlUpdate("DELETE FROM TextMessage WHERE Id = :id")
    public void deleteMessage(@Bind("id") final long id);

    @SqlQuery("SELECT m.* FROM TextMessageLabelRelation AS r" +
            "  JOIN TextMessage AS m" +
            "  ON m.Id = r.TextMessageId" +
            "  WHERE r.LabelId IN (<labelId>)")
    List<TextMessage> getTextMessageByLabelIds(@BindIn("labelId") final List<Long> labelId);

    @SqlUpdate("INSERT INTO TextMessageLabelRelation (TextMessageId, LabelId) VALUES (:textMessageId, :labelId)")
    void linkTextMessageWithLabel(@Bind("textMessageId") final long textMessageId,
                                  @Bind("labelId") final long labelId);

    @SqlUpdate("DELETE FROM TextMessageLabelRelation WHERE TextMessageId = :textMessageId AND LabelId = :labelId")
    void delinkTextMessageWithLabel(@Bind("textMessageId") final long textMessageId,
                                    @Bind("labelId") final long labelId);
}
