package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.Label;
import com.thoughtworks.wechat_application.core.TextMessage;
import com.thoughtworks.wechat_application.jdbi.TextMessageDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class TextMessageService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TextMessageService.class);
    private final TextMessageDAO textMessageDAO;
    private final LabelService labelService;

    @Inject
    public TextMessageService(final TextMessageDAO textMessageDAO, final LabelService labelService) {
        this.textMessageDAO = textMessageDAO;
        this.labelService = labelService;
    }

    public boolean newTextMessage(final String title, final String content) {
        checkNotBlank(title);
        checkNotBlank(content);

        LOGGER.info("[NewTextMessage] Try create text message with title: {}, content: {}.", title, content);
        final TextMessage textMessage = textMessageDAO.getTextMessageByTitle(title);
        if (textMessage == null) {
            long id = textMessageDAO.createTextMessage(title, content, toUnixTimestamp(DateTime.now()));
            LOGGER.info("[NewTextMessage] Create text message '{}' success, id: {}.", title, id);
            return true;
        } else {
            LOGGER.info("[NewTextMessage] Already have another text message with same title '{}'.", textMessage);
            return false;
        }
    }

    public boolean updateContent(final String title, final String content) {
        checkNotBlank(title);
        checkNotBlank(content);

        LOGGER.info("[UpdateContent] Try update text message(title: {})'s content to {}.", title, content);
        final TextMessage textMessage = textMessageDAO.getTextMessageByTitle(title);
        if (textMessage != null) {
            textMessageDAO.updateContent(title, content, toUnixTimestamp(DateTime.now()));
            LOGGER.info("[UpdateContent] Update text message(id: {}, title: {}) success. Original value is: {}.", textMessage.getId(), title, textMessage.getContent());
            return true;
        } else {
            LOGGER.info("[UpdateContent] Cannot update text message(title '{}'), there have no such message.", title);
            return false;
        }
    }

    public boolean deleteMessage(final String title) {
        checkNotBlank(title);

        LOGGER.info("[DeleteMessage] Try delete text message(title: {}).", title);
        final TextMessage textMessage = textMessageDAO.getTextMessageByTitle(title);
        if (textMessage != null) {
            textMessageDAO.deleteMessage(textMessage.getId());
            LOGGER.info("[DeleteMessage] Delete text message(id: {}, title: {}) success.", textMessage.getId(), title);
            return true;
        } else {
            LOGGER.info("[DeleteMessage] Cannot delete text message(title '{}'), there have no such message..", title);
            return false;
        }
    }

    public List<TextMessage> getAllMessages() {
        List<TextMessage> messages = textMessageDAO.getAllMessages();
        LOGGER.info("[GetAllMessage] Get {} messages.", messages.size());
        return messages;
    }

    public Optional<TextMessage> getTextMessageByTitle(final String title) {
        checkNotBlank(title);

        LOGGER.info("[GetTextMessageByTitle] Try get text message with title: {}.", title);
        final Optional<TextMessage> textMessage = Optional.ofNullable(textMessageDAO.getTextMessageByTitle(title));
        LOGGER.info("[GetTextMessageByTitle] Get text message with title: {}. Status {}.", title, textMessage.isPresent());
        return textMessage;
    }

    public List<TextMessage> getTextMessageByLabel(final Label label) {
        checkNotNull(label);

        return getTextMessageByLabels(Arrays.asList(label));
    }

    public List<TextMessage> getTextMessageByLabels(final List<Label> labels) {
        checkNotNull(labels);

        final List<Long> labelIds = labels.stream().map(Label::getId).collect(Collectors.toList());
        final List<TextMessage> messages = textMessageDAO.getTextMessageByLabelIds(labelIds);
        LOGGER.info("[GetTextMessageByLabel] Get text messages with label's id [{}], size: {}.", labelIds.stream().map(Object::toString).collect(Collectors.joining(", ")), messages.size());
        return messages;
    }

    public void linkTextMessageToLabel(final TextMessage textMessage, final Label label) {
        checkNotNull(textMessage);
        checkNotNull(label);

        LOGGER.info("[LinkTextMessageToLabel] Try link text message(id: {}, title: {}) to label(id: {}).", textMessage.getId(), textMessage.getTitle(), label.getId());
        List<Label> textMessageLabels = labelService.getTextMessageLabels(textMessage);
        if (textMessageLabels.stream().anyMatch(l -> l.getId() == label.getId())) {
            LOGGER.info("[LinkTextMessageToLabel] Text message(id: {}) already linked to label(id: {}). Skip it.", textMessage.getId(), label.getId());
        } else {
            textMessageDAO.linkTextMessageWithLabel(textMessage.getId(), label.getId(), toUnixTimestamp(DateTime.now()));
            LOGGER.info("[LinkTextMessageToLabel] Text message(id: {}) link to label(id: {}) success.", textMessage.getId(), label.getId());
        }
    }

    public void delinkTextMessageWithLabel(final TextMessage textMessage, final Label label) {
        checkNotNull(textMessage);
        checkNotNull(label);

        LOGGER.info("[DelinkTextMessageWithLabel] Try delink text message(id: {}, title: {}) with label(id: {}).", textMessage.getId(), textMessage.getTitle(), label.getId());
        List<Label> textMessageLabels = labelService.getTextMessageLabels(textMessage);
        if (textMessageLabels.stream().anyMatch(l -> l.getId() == label.getId())) {
            textMessageDAO.delinkTextMessageWithLabel(textMessage.getId(), label.getId());
            LOGGER.info("[DelinkTextMessageWithLabel] Delink text message(id: {}) with label(id: {}) success.", textMessage.getId(), label.getId());
        } else {
            LOGGER.info("[DelinkTextMessageWithLabel] Text message(id: {}) not link to label(id: {}) before, skip it.", textMessage.getId(), label.getId());
        }
    }
}
