package com.thoughtworks.wechat_core.util.precondition;

public class PreconditionException extends RuntimeException {
    public PreconditionException(String message) {
        super(message);
    }

    public PreconditionException() {
    }
}
