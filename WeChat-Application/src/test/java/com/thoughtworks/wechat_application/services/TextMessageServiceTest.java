package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.core.Label;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.jdbi.TextMessageDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TextMessageServiceTest {
    @Mock
    private TextMessageDAO textMessageDAO;
    @Mock
    private LabelService labelService;
    private TextMessageService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(TextMessageDAO.class).toInstance(textMessageDAO);
            binder.bind(LabelService.class).toInstance(labelService);
        });

        service = injector.getInstance(TextMessageService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final TextMessageService anotherService = injector.getInstance(TextMessageService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testNewTextMessage() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        final boolean result = service.newTextMessage("Title", "Content");

        verify(textMessageDAO).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO).createTextMessage(eq("Title"), eq("Content"), any(Timestamp.class));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNewTextMessage_Exist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage());

        final boolean result = service.newTextMessage("Title", "Content");

        verify(textMessageDAO).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, never()).createTextMessage(anyString(), anyString(), any(Timestamp.class));
        assertThat(result, equalTo(false));
    }

    @Test
    public void testUpdateContent() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage());

        final boolean result = service.updateContent("Title", "Something different");

        verify(textMessageDAO).getTextMessageByTitle("Title");
        verify(textMessageDAO).updateContent(eq("Title"), eq("Something different"), any(Timestamp.class));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testUpdateContent_NotExist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        final boolean result = service.updateContent("Title", "Something different");

        verify(textMessageDAO).getTextMessageByTitle("Title");
        verify(textMessageDAO, never()).updateContent(anyString(), anyString(), any(Timestamp.class));
        assertThat(result, equalTo(false));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage());

        final boolean result = service.deleteMessage("Title");

        verify(textMessageDAO).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO).deleteMessage(eq(1L));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testDeleteMessage_NotExist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        final boolean result = service.deleteMessage("Title");

        verify(textMessageDAO).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, never()).deleteMessage(anyLong());
        assertThat(result, equalTo(false));
    }

    @Test
    public void testGetAllMessages() throws Exception {
        when(textMessageDAO.getAllMessages()).thenReturn(Arrays.asList());

        final List<TextMessage> messages = service.getAllMessages();

        verify(textMessageDAO).getAllMessages();
        assertThat(messages, notNullValue());
    }

    @Test
    public void testGetTextMessageByTitle_NotExist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("title")).thenReturn(null);

        final Optional<TextMessage> textMessage = service.getTextMessageByTitle("title");

        verify(textMessageDAO).getTextMessageByTitle(eq("title"));
        assertThat(textMessage.isPresent(), equalTo(false));
    }

    @Test
    public void testGetTextMessageByTitle_Exist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("title")).thenReturn(createTextMessage());

        final Optional<TextMessage> textMessage = service.getTextMessageByTitle("title");

        verify(textMessageDAO).getTextMessageByTitle(eq("title"));
        assertThat(textMessage.isPresent(), equalTo(true));
    }

    @Test
    public void testGetTextMessageByLabel() throws Exception {
        when(textMessageDAO.getTextMessageByLabelIds(Arrays.asList(1L))).thenReturn(Arrays.asList(createTextMessage()));

        final List<TextMessage> messages = service.getTextMessageByLabel(createLabel1());

        verify(textMessageDAO).getTextMessageByLabelIds(eq(Arrays.asList(1L)));
        assertThat(messages.size(), equalTo(1));
    }

    @Test
    public void testGetTextMessageByLabels() throws Exception {
        when(textMessageDAO.getTextMessageByLabelIds(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(createTextMessage()));

        final List<TextMessage> messages = service.getTextMessageByLabels(Arrays.asList(createLabel1(), createLabel2()));

        verify(textMessageDAO).getTextMessageByLabelIds(eq(Arrays.asList(1L, 2L)));
        assertThat(messages.size(), equalTo(1));
    }

    @Test
    public void testLinkTextMessageToLabel() throws Exception {
        final TextMessage textMessage = createTextMessage();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1()));

        service.linkTextMessageToLabel(textMessage, createLabel2());

        verify(labelService).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO).linkTextMessageWithLabel(eq(textMessage.getId()), eq(2L), any(Timestamp.class));
    }

    @Test
    public void testLinkTextMessageToLabel_AlreadyLinked() throws Exception {
        final TextMessage textMessage = createTextMessage();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        service.linkTextMessageToLabel(textMessage, createLabel2());

        verify(labelService).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, never()).linkTextMessageWithLabel(anyLong(), anyLong(), any(Timestamp.class));
    }

    @Test
    public void testDelinkTextMessageWithLabel() throws Exception {
        final TextMessage textMessage = createTextMessage();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        service.delinkTextMessageWithLabel(textMessage, createLabel2());

        verify(labelService).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO).delinkTextMessageWithLabel(eq(textMessage.getId()), eq(2L));
    }

    @Test
    public void testDelinkTextMessageWithLabel_NotLinked() throws Exception {
        final TextMessage textMessage = createTextMessage();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1()));

        service.delinkTextMessageWithLabel(textMessage, createLabel2());

        verify(labelService).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, never()).delinkTextMessageWithLabel(anyLong(), anyLong());
    }

    private TextMessage createTextMessage() {
        return new TextMessage(1L, "Title", "Content");
    }

    private Label createLabel1() {
        return new Label(1, "Label1");
    }

    private Label createLabel2() {
        return new Label(2, "Label2");
    }
}