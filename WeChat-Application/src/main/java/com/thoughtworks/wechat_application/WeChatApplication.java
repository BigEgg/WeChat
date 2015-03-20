package com.thoughtworks.wechat_application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.resources.WeChatEntryPointResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WeChatApplication extends Application<WeChatApplicationConfiguration> {
    private Injector injector;

    public static void main(final String[] args) throws Exception {
        new WeChatApplication().run(args);
    }

    @Override
    public String getName() {
        return "ThoughtWorks";
    }

    @Override
    public void initialize(Bootstrap<WeChatApplicationConfiguration> bootstrap) {

    }

    @Override
    public void run(WeChatApplicationConfiguration weChatApplicationConfiguration, Environment environment) throws Exception {
        configInjector(weChatApplicationConfiguration);

        registResource(environment);
    }

    private void configInjector(WeChatApplicationConfiguration weChatApplicationConfiguration) {
        injector = Guice.createInjector(new WeChatApplicationModule(weChatApplicationConfiguration.getCacheConfiguration()));
    }

    private void registResource(Environment environment) {
        environment.jersey().register(injector.getInstance(WeChatEntryPointResource.class));
    }
}
