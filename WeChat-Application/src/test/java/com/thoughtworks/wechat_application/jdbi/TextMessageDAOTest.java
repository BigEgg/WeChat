package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.core.TextMessage;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TextMessageDAOTest extends AbstractDAOTest {
    private TextMessageDAO textMessageDAO;
    private LabelDAO labelDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        textMessageDAO = getDAO(TextMessageDAO.class);
        labelDAO = getDAO(LabelDAO.class);
    }

    @Test
    public void testCreateTextMessage() throws Exception {
        long id = textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = textMessageDAO.createTextMessage("Welcome", "Welcome", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateTextMessage_TitleShouldUnique() throws Exception {
        textMessageDAO.createTextMessage("SubscribeMessage", "Welcome1", getHappenedTime());
        textMessageDAO.createTextMessage("SubscribeMessage", "Welcome2", getHappenedTime());
    }

    @Test
    public void testGetTextMessageByTitle() throws Exception {
        final long id = textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        final TextMessage subscribeMessage = textMessageDAO.getTextMessageByTitle("SubscribeMessage");

        assertThat(subscribeMessage, notNullValue());
        assertThat(subscribeMessage.getId(), equalTo(id));
        assertThat(subscribeMessage.getTitle(), equalTo("SubscribeMessage"));
        assertThat(subscribeMessage.getContent(), equalTo("Welcome"));
    }

    @Test
    public void testGetTextMessageByTitle_NotExist() throws Exception {
        final TextMessage subscribeMessage = textMessageDAO.getTextMessageByTitle("SubscribeMessage");

        assertThat(subscribeMessage, nullValue());
    }

    @Test
    public void testUpdateContent() throws Exception {
        final long id = textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        textMessageDAO.updateContent("SubscribeMessage", "Hey", getHappenedTime());

        final TextMessage subscribeMessage = textMessageDAO.getTextMessageByTitle("SubscribeMessage");
        assertThat(subscribeMessage, notNullValue());
        assertThat(subscribeMessage.getId(), equalTo(id));
        assertThat(subscribeMessage.getTitle(), equalTo("SubscribeMessage"));
        assertThat(subscribeMessage.getContent(), equalTo("Hey"));
    }

    @Test
    public void testUpdateContent_NotExist() throws Exception {
        textMessageDAO.updateContent("SubscribeMessage", "Hey", getHappenedTime());

        final TextMessage subscribeMessage = textMessageDAO.getTextMessageByTitle("SubscribeMessage");
        assertThat(subscribeMessage, nullValue());
    }

    @Test
    public void testGetAllMessages() throws Exception {
        textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        textMessageDAO.createTextMessage("Welcome", "Welcome", getHappenedTime());

        final List<TextMessage> messages = textMessageDAO.getAllMessages();
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(2));

        assertThat(messages.get(0).getId(), equalTo(1L));
        assertThat(messages.get(0).getTitle(), equalTo("SubscribeMessage"));
        assertThat(messages.get(0).getContent(), equalTo("Welcome"));

        assertThat(messages.get(1).getId(), equalTo(2L));
        assertThat(messages.get(1).getTitle(), equalTo("Welcome"));
        assertThat(messages.get(1).getContent(), equalTo("Welcome"));
    }

    @Test
    public void testGetAllMessages_Empty() throws Exception {
        final List<TextMessage> messages = textMessageDAO.getAllMessages();
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(0));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        final long messageId = textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        textMessageDAO.createTextMessage("Welcome", "Welcome", getHappenedTime());

        List<TextMessage> messages = textMessageDAO.getAllMessages();
        assertThat(messages.size(), equalTo(2));

        textMessageDAO.deleteMessage(messageId);
        messages = textMessageDAO.getAllMessages();
        assertThat(messages.size(), equalTo(1));

        assertThat(messages.get(0).getId(), equalTo(2L));
        assertThat(messages.get(0).getTitle(), equalTo("Welcome"));
        assertThat(messages.get(0).getContent(), equalTo("Welcome"));
    }

    @Test
    public void testDeleteMessage_NotExist() throws Exception {
        textMessageDAO.deleteMessage(1);
    }

    @Test
    public void testLinkTextMessageWithLabel() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId, getHappenedTime());
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkTextMessageWithLabel_NoMessage() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(1, labelId, getHappenedTime());
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkTextMessageWithLabel_NoLabel() throws Exception {
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, 1, getHappenedTime());
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testLinkTextMessageWithLabel_Existed() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId, getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId, getHappenedTime());
    }

    @Test
    public void testGetMembersByLabelIds() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId, getHappenedTime());

        final List<TextMessage> messages = textMessageDAO.getTextMessageByLabelIds(Arrays.asList(labelId));
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(1));
        assertThat(messages.get(0).getTitle(), equalTo("title"));
        assertThat(messages.get(0).getContent(), equalTo("content"));
        assertThat(messages.get(0).getId(), equalTo(messageId));
    }

    @Test
    public void testDelinkTextMessageWithLabel() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId, getHappenedTime());

        List<TextMessage> messages = textMessageDAO.getTextMessageByLabelIds(Arrays.asList(labelId));
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(1));

        textMessageDAO.delinkTextMessageWithLabel(messageId, labelId);
        messages = textMessageDAO.getTextMessageByLabelIds(Arrays.asList(labelId));
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(0));
    }

    @Test
    public void testDelinkMemberWithLabel_NotExist() throws Exception {
        textMessageDAO.delinkTextMessageWithLabel(1, 1);
    }
}