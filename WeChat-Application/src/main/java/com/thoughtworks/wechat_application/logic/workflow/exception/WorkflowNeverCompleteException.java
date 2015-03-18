package com.thoughtworks.wechat_application.logic.workflow.exception;

public class WorkflowNeverCompleteException extends RuntimeException {
    public WorkflowNeverCompleteException(String message) {
        super(message);
    }

    public WorkflowNeverCompleteException() {
    }
}
