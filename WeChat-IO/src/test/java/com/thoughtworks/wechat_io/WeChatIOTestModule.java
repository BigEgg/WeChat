package com.thoughtworks.wechat_io;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.wechat_io.configs.CacheConfiguration;
import com.thoughtworks.wechat_io.configs.WeChatConfiguration;
import com.thoughtworks.wechat_io.jdbi.LabelDAO;
import com.thoughtworks.wechat_io.jdbi.MemberDAO;

import static org.mockito.Mockito.mock;

public class WeChatIOTestModule implements Module {
    public void configure(Binder binder) {
        configureConfigs(binder);
        configureDAO(binder);
    }

    private void configureConfigs(Binder binder) {
        binder.bind(WeChatConfiguration.class).toInstance(mock(WeChatConfiguration.class));
        binder.bind(CacheConfiguration.class).toInstance(mock(CacheConfiguration.class));
    }

    private void configureDAO(Binder binder) {
        binder.bind(LabelDAO.class).toInstance(mock(LabelDAO.class));
        binder.bind(MemberDAO.class).toInstance(mock(MemberDAO.class));
    }
}
