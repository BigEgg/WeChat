package com.thoughtworks.wechat_io.jdbi;

import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.h2.jdbcx.JdbcConnectionPool;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.skife.jdbi.v2.DBI;

import java.sql.Connection;
import java.sql.Timestamp;

public abstract class AbstractDAOTest {
    protected JdbcConnectionPool pool;
    private DBI jdbi;

    @Before
    public void setUp() throws Exception {
        pool = JdbcConnectionPool.create("jdbc:h2:mem:test", "username", "password");
        Connection connection = pool.getConnection();
        Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new HsqlConnection(connection));
        liquibase.update("");
        connection.close();

        jdbi = new DBI(pool);
    }

    @After
    public void tearDown() throws Exception {
        Connection connection = pool.getConnection();
        Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new HsqlConnection(connection));
        liquibase.dropAll();
        connection.close();

        pool.dispose();
    }

    protected <SqlObjectType> SqlObjectType getDAO(Class<SqlObjectType> daoClass) {
        return jdbi.onDemand(daoClass);
    }

    protected Timestamp getHappenedTime() {
        return new Timestamp(DateTime.now().getMillis());
    }
}
