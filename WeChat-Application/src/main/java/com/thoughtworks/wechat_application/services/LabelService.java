package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.configs.CacheConfiguration;
import com.thoughtworks.wechat_application.jdbi.core.Label;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.jdbi.LabelDAO;
import com.thoughtworks.wechat_application.utils.CacheManager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class LabelService {
    private final static String LABEL_CACHE_KEY = "Label_Cache_Key";
    private final static Logger LOGGER = LoggerFactory.getLogger(LabelService.class);
    private final LabelDAO labelDAO;
    private final CacheManager cacheManager;
    private final int labelCacheSeconds;

    @Inject
    public LabelService(final LabelDAO labelDAO, final CacheConfiguration cacheConfiguration, final CacheManager cacheManager) {
        this.labelDAO = labelDAO;
        this.cacheManager = cacheManager;
        this.labelCacheSeconds = cacheConfiguration.getLabelCacheSeconds();
    }

    public Optional<Label> createLabel(final String title) {
        checkNotBlank(title);

        LOGGER.info("[CreateLabel] Try create label with title {}.", title);
        final Collection<Label> oldLabels = getLabels().values();
        if (oldLabels.parallelStream().anyMatch((l) -> l.getTitle().equalsIgnoreCase(title))) {
            LOGGER.warn("[CreateLabel] Already have a label exist with same title.");
            return Optional.empty();
        }

        cacheManager.expire(LABEL_CACHE_KEY);
        final long id = labelDAO.createLabel(title, toUnixTimestamp(DateTime.now()));
        LOGGER.info("[CreateLabel] Create a label with id: {}.", id);
        return Optional.of(getLabels().get(title));
    }

    public List<Label> getAllLabels() {
        ArrayList<Label> labels = new ArrayList<>(getLabels().values());
        LOGGER.info("[GetAllLabel] Get {} label(s).", labels.size());
        return labels;
    }

    public Optional<Label> get(final String title) {
        checkNotBlank(title);

        Optional<Label> label = Optional.ofNullable(getLabels().get(title));
        LOGGER.info("[Get] Try get label with title: {}. Status: {}.", title, label.isPresent());
        return label;
    }

    void deleteLabel(final String title) {
        checkNotBlank(title);

        LOGGER.info("[DeleteLabel] Try delete label with title: {}.", title);
        Label label = getLabels().get(title);

        if (label != null) {
            cacheManager.expire(LABEL_CACHE_KEY);
            labelDAO.deleteLabel(label.getId());
            LOGGER.info("[DeleteLabel] Find the label(title: {}), delete it.", title);
        } else {
            LOGGER.info("[DeleteLabel] Not find the label(title: {}). Skip it.", title);
        }
    }

    public Optional<Label> getMemberLabels(final Member member) {
        checkNotNull(member);

        Optional<Label> label = Optional.ofNullable(labelDAO.getMemberLabel(member.getId()));
        LOGGER.info("[GetMemberLabels] Try get member(id: {})'s label. Status: {}.", member.getId(), label.isPresent());
        return label;
    }

    public List<Label> getTextMessageLabels(final TextMessage textMessage) {
        checkNotNull(textMessage);

        List<Label> labels = labelDAO.getTextMessageLabels(textMessage.getId());
        LOGGER.info("[GetTextMessageLabels] Try get text message(id: {})'s labels, size: {}.", textMessage.getId(), labels.size());
        return labels;
    }

    private Map<String, Label> getLabels() {
        Optional<Map> cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        if (!cache.isPresent()) {
            LOGGER.info("[LabelCache] Cache expired, read from database.");
            List<Label> labels = labelDAO.getAllLabel();
            Map<String, Label> labelCache = new HashMap<>();
            labels.forEach(l -> labelCache.put(l.getTitle(), l));

            cacheManager.put(LABEL_CACHE_KEY, labelCache, labelCacheSeconds);
        }

        cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        return (Map<String, Label>) cache.get();
    }
}
