package com.thoughtworks.wechat_io;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestBase {
    private final Injector injector;

    public TestBase() {
        this.injector = Guice.createInjector(new WeChatIOTestModule());
    }

    protected <T> T getInstance(Class<T> aClass) {
        return injector.getInstance(aClass);
    }
}
