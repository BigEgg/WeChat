package com.thoughtworks.wechat_application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.inject.DAOModule;
import com.thoughtworks.wechat_application.inject.WeChatApplicationModule;
import com.thoughtworks.wechat_application.inject.WorkflowModule;
import com.thoughtworks.wechat_application.jdbi.*;
import com.thoughtworks.wechat_application.resources.WeChatEntryPointResource;
import com.thoughtworks.wechat_application.resources.WeChatInboundMessageReader;
import com.thoughtworks.wechat_application.resources.WeChatOutboundMessageWriter;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

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
        bootstrap.addBundle(new MigrationsBundle<WeChatApplicationConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(WeChatApplicationConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(WeChatApplicationConfiguration weChatApplicationConfiguration, Environment environment) throws Exception {
        registDAO(environment, weChatApplicationConfiguration);

        configInjector(weChatApplicationConfiguration, daoModule);

        registResource(environment);
    }

    private void configInjector(WeChatApplicationConfiguration weChatApplicationConfiguration, DAOModule daoModule) {
        injector = Guice.createInjector(
                new WeChatApplicationModule(weChatApplicationConfiguration.getCacheConfiguration()),
                daoModule,
                new WorkflowModule());
    }

    private void registResource(final Environment environment) {
        environment.jersey().register(WeChatInboundMessageReader.class);
        environment.jersey().register(WeChatOutboundMessageWriter.class);

        environment.jersey().register(injector.getInstance(WeChatEntryPointResource.class));
    }

    private DAOModule registDAO(final Environment environment, WeChatApplicationConfiguration configuration) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        final DAOModule daoModule = new DAOModule();
        daoModule.setAdminUserDAO(jdbi.onDemand(AdminUserDAO.class));
        daoModule.setConversationHistoryDAO(jdbi.onDemand(ConversationHistoryDAO.class));
        daoModule.setEventLogDAO(jdbi.onDemand(EventLogDAO.class));
        daoModule.setExpirableResourceDAO(jdbi.onDemand(ExpirableResourceDAO.class));
        daoModule.setLabelDAO(jdbi.onDemand(LabelDAO.class));
        daoModule.setMemberDAO(jdbi.onDemand(MemberDAO.class));
        daoModule.setTextMessageDAO(jdbi.onDemand(TextMessageDAO.class));

        return daoModule;
    }
}