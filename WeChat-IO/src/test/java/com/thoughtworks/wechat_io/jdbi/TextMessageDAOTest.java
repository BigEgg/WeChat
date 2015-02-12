package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.TextMessage;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TextMessageDAOTest extends AbstractDAOTest {
    private TextMessageDAO textMessageDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        textMessageDAO = getDAO(TextMessageDAO.class);
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
    public void testGetAllMessages() throws Exception {
        textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
        textMessageDAO.createTextMessage("Welcome", "Welcome", getHappenedTime());

        List<TextMessage> messages = textMessageDAO.getAllMessages();
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
        List<TextMessage> messages = textMessageDAO.getAllMessages();
        assertThat(messages, notNullValue());
        assertThat(messages.size(), equalTo(0));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        long messageId = textMessageDAO.createTextMessage("SubscribeMessage", "Welcome", getHappenedTime());
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
}