package com.thoughtworks.wechat_core.util.precondition;

public class ArgumentPrecondition {
    public static void checkNotBlank(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new PreconditionException();
        }
    }

    public static void checkNotBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new PreconditionException(message);
        }
    }
}
