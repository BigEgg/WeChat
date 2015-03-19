package com.thoughtworks.wechat_application.logic.workflow.exception;

public class NoSuchWorkflowException extends RuntimeException {
    public NoSuchWorkflowException() {
    }

    public NoSuchWorkflowException(String message) {
        super(message);
    }
}
