package com.thoughtworks.wechat_io.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.TextMessage;
import com.thoughtworks.wechat_io.jdbi.TextMessageDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

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
    private TextMessageService textMessageService;

    @Before
    public void setUp() throws Exception {
        textMessageService = new TextMessageService(textMessageDAO, labelService);
    }

    @Test
    public void testInject() throws Exception {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(TextMessageDAO.class).toInstance(textMessageDAO);
            binder.bind(LabelService.class).toInstance(labelService);
        });

        TextMessageService service = injector.getInstance(TextMessageService.class);
        assertThat(service, notNullValue());
    }

    @Test
    public void testInject_Singleton() throws Exception {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(TextMessageDAO.class).toInstance(textMessageDAO);
            binder.bind(LabelService.class).toInstance(labelService);
        });

        TextMessageService service = injector.getInstance(TextMessageService.class);
        TextMessageService anotherService = injector.getInstance(TextMessageService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testNewTextMessage() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        boolean result = textMessageService.newTextMessage("Title", "Content");

        verify(textMessageDAO, times(1)).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, times(1)).createTextMessage(eq("Title"), eq("Content"), any(Timestamp.class));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNewTextMessage_Exist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage1());

        boolean result = textMessageService.newTextMessage("Title", "Content");

        verify(textMessageDAO, times(1)).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, never()).createTextMessage(anyString(), anyString(), any(Timestamp.class));
        assertThat(result, equalTo(false));
    }

    @Test
    public void testUpdateContent() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage1());

        boolean result = textMessageService.updateContent("Title", "Something different");

        verify(textMessageDAO, times(1)).getTextMessageByTitle("Title");
        verify(textMessageDAO, times(1)).updateContent(eq("Title"), eq("Something different"), any(Timestamp.class));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testUpdateContent_NotExist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        boolean result = textMessageService.updateContent("Title", "Something different");

        verify(textMessageDAO, times(1)).getTextMessageByTitle("Title");
        verify(textMessageDAO, never()).updateContent(anyString(), anyString(), any(Timestamp.class));
        assertThat(result, equalTo(false));
    }

    @Test
    public void testGetAllMessages() throws Exception {
        when(textMessageDAO.getAllMessages()).thenReturn(Arrays.asList());

        List<TextMessage> messages = textMessageService.getAllMessages();

        verify(textMessageDAO, times(1)).getAllMessages();
    }

    @Test
    public void testDeleteMessage() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(createTextMessage1());

        boolean result = textMessageService.deleteMessage("Title");

        verify(textMessageDAO, times(1)).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, times(1)).deleteMessage(eq(1L));
        assertThat(result, equalTo(true));
    }

    @Test
    public void testDeleteMessage_NotExist() throws Exception {
        when(textMessageDAO.getTextMessageByTitle("Title")).thenReturn(null);

        boolean result = textMessageService.deleteMessage("Title");

        verify(textMessageDAO, times(1)).getTextMessageByTitle(eq("Title"));
        verify(textMessageDAO, never()).deleteMessage(anyLong());
        assertThat(result, equalTo(false));
    }

    @Test
    public void testGetTextMessageByLabel() throws Exception {
        when(textMessageDAO.getTextMessageByLabelIds(Arrays.asList(1L))).thenReturn(Arrays.asList(createTextMessage1()));

        List<TextMessage> messages = textMessageService.getTextMessageByLabel(createLabel1());

        verify(textMessageDAO, times(1)).getTextMessageByLabelIds(eq(Arrays.asList(1L)));
        assertThat(messages.size(), equalTo(1));
    }


    @Test
    public void testGetTextMessageByLabels() throws Exception {
        when(textMessageDAO.getTextMessageByLabelIds(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(createTextMessage1()));

        List<TextMessage> messages = textMessageService.getTextMessageByLabels(Arrays.asList(createLabel1(), createLabel2()));

        verify(textMessageDAO, times(1)).getTextMessageByLabelIds(eq(Arrays.asList(1L, 2L)));
        assertThat(messages.size(), equalTo(1));
    }

    @Test
    public void testLinkTextMessageToLabel() throws Exception {
        TextMessage textMessage = createTextMessage1();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1()));

        textMessageService.linkTextMessageToLabel(textMessage, createLabel2());

        verify(labelService, times(1)).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, times(1)).linkTextMessageWithLabel(eq(textMessage.getId()), eq(2L));
    }

    @Test
    public void testLinkTextMessageToLabel_AlreadyLinked() throws Exception {
        TextMessage textMessage = createTextMessage1();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        textMessageService.linkTextMessageToLabel(textMessage, createLabel2());

        verify(labelService, times(1)).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, never()).linkTextMessageWithLabel(anyLong(), anyLong());
    }

    @Test
    public void testDelinkTextMessageWithLabel() throws Exception {
        TextMessage textMessage = createTextMessage1();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        textMessageService.delinkTextMessageWithLabel(textMessage, createLabel2());

        verify(labelService, times(1)).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, times(1)).delinkTextMessageWithLabel(eq(textMessage.getId()), eq(2L));
    }

    @Test
    public void testDelinkTextMessageWithLabel_NotLinked() throws Exception {
        TextMessage textMessage = createTextMessage1();
        when(labelService.getTextMessageLabels(textMessage)).thenReturn(Arrays.asList(createLabel1()));

        textMessageService.delinkTextMessageWithLabel(textMessage, createLabel2());

        verify(labelService, times(1)).getTextMessageLabels(eq(textMessage));
        verify(textMessageDAO, never()).delinkTextMessageWithLabel(anyLong(), anyLong());
    }

    private TextMessage createTextMessage1() {
        return new TextMessage(1L, "Title", "Content");
    }

    private TextMessage createTextMessage2() {
        return new TextMessage(1L, "Title", "Something different.");
    }

    private Label createLabel1() {
        return new Label(1, "Label1");
    }

    private Label createLabel2() {
        return new Label(2, "Label2");
    }
}