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

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class LabelService {
    private final static String LABEL_CACHE_KEY = "Label_Cache_Key";
    private final LabelDAO labelDAO;
    private final CacheManager cacheManager;
    private final int labelCacheSeconds;

    @Inject
    public LabelService(LabelDAO labelDAO, CacheConfiguration cacheConfiguration, CacheManager cacheManager) {
        this.labelDAO = labelDAO;
        this.cacheManager = cacheManager;
        this.labelCacheSeconds = cacheConfiguration.getLabelCacheSeconds();
    }

    public Optional<Label> createLabel(final String name) {
        checkNotBlank(name);

        final Collection<Label> oldLabels = getLabels().values();
        if (oldLabels.parallelStream().anyMatch((l) -> l.getName().equalsIgnoreCase(name))) {
            return Optional.empty();
        }

        cacheManager.expire(LABEL_CACHE_KEY);
        final long id = labelDAO.createLabel(name, toUnixTimestamp(DateTime.now()));
        return Optional.of(getLabels().get(id));
    }

    public List<Label> getAllLabel() {
        return new ArrayList<>(getLabels().values());
    }

    void deleteLabel(final long id) {
        checkArgument(id > 0);

        final Collection<Label> oldLabels = getLabels().values();
        if (oldLabels.parallelStream().anyMatch((l) -> l.getId() == id)) {
            cacheManager.expire(LABEL_CACHE_KEY);
            labelDAO.deleteLabel(id);
        }
    }

    public Optional<Label> getMemberLabels(final Member member) {
        checkNotNull(member);

        return Optional.ofNullable(labelDAO.getMemberLabels(member.getId()));
    }

    public List<Label> getTextMessageLabels(final TextMessage textMessage) {
        checkNotNull(textMessage);

        return labelDAO.getTextMessageLables(textMessage.getId());
    }

    private Map<Long, Label> getLabels() {
        Optional<Map> cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        if (!cache.isPresent()) {
            List<Label> labels = labelDAO.getAllLabel();
            Map<Long, Label> labelCache = new HashMap<>();
            labels.forEach(l -> labelCache.put(l.getId(), l));

            cacheManager.put(LABEL_CACHE_KEY, labelCache, labelCacheSeconds);
        }

        cache = cacheManager.get(LABEL_CACHE_KEY, Map.class);
        return (Map<Long, Label>) cache.get();
    }
}
