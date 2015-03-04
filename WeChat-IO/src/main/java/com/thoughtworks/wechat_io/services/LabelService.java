package com.thoughtworks.wechat_io.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_io.configs.CacheConfiguration;
import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.Member;
import com.thoughtworks.wechat_io.core.TextMessage;
import com.thoughtworks.wechat_io.jdbi.LabelDAO;
import com.thoughtworks.wechat_io.utils.CacheManager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
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

    public Optional<Label> createLabel(final String name) {
        checkNotBlank(name);

        LOGGER.info("[CreateLabel] Try create label with name {}.", name);
        final Collection<Label> oldLabels = getLabels().values();
        if (oldLabels.parallelStream().anyMatch((l) -> l.getName().equalsIgnoreCase(name))) {
            LOGGER.warn("[CreateLabel] Already have a label exist with same name.");
            return Optional.empty();
        }

        cacheManager.expire(LABEL_CACHE_KEY);
        final long id = labelDAO.createLabel(name, toUnixTimestamp(DateTime.now()));
        LOGGER.info("[CreateLabel] Create a label with id: {}.", id);
        return Optional.of(getLabels().get(id));
    }

    public List<Label> getAllLabels() {
        ArrayList<Label> labels = new ArrayList<>(getLabels().values());
        LOGGER.info("[GetAllLabel] Get {} label(s).", labels.size());
        return labels;
    }

    public Optional<Label> get(final long id) {
        Optional<Label> label = Optional.ofNullable(getLabels().get(id));
        LOGGER.info("[Get] Try get label with id: {}. Status: {}.", id, label.isPresent());
        return label;
    }

    void deleteLabel(final long id) {
        checkArgument(id > 0);

        LOGGER.info("[DeleteLabel] Try delete label with id: {}.", id);
        final Collection<Label> oldLabels = getLabels().values();
        if (oldLabels.parallelStream().anyMatch((l) -> l.getId() == id)) {
            cacheManager.expire(LABEL_CACHE_KEY);
            LOGGER.info("[DeleteLabel] Find the label, delete it.");
            labelDAO.deleteLabel(id);
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

    private Map<Long, Label> getLabels() {
        Optional<Map> cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        if (!cache.isPresent()) {
            LOGGER.info("[LabelCache] Cache expired, read from database.");
            List<Label> labels = labelDAO.getAllLabel();
            Map<Long, Label> labelCache = new HashMap<>();
            labels.forEach(l -> labelCache.put(l.getId(), l));

            cacheManager.put(LABEL_CACHE_KEY, labelCache, labelCacheSeconds);
        }

        cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        return (Map<Long, Label>) cache.get();
    }
}
