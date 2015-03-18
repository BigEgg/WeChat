package com.thoughtworks.wechat_application.logic.workflow.exception;

public class WorkflowNotSupportMessageException extends RuntimeException {
    public WorkflowNotSupportMessageException() {
    }

    public WorkflowNotSupportMessageException(String message) {
        super(message);
    }
}
