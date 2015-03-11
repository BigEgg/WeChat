package com.thoughtworks.wechat_application.core;

import org.joda.time.DateTime;

import java.util.Optional;

public class ConversationHistory {
    private long id;
    private long memberId;
    private String workflowName;
    private DateTime startTime;
    private Optional<DateTime> endTime;
    private Optional<String> content;

    public ConversationHistory(long id, long memberId, String workflowName, DateTime startTime, Optional<DateTime> endTime, Optional<String> content) {
        this.id = id;
        this.memberId = memberId;
        this.workflowName = workflowName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public Optional<DateTime> getEndTime() {
        return endTime;
    }

    public Optional<String> getContent() {
        return content;
    }
}
