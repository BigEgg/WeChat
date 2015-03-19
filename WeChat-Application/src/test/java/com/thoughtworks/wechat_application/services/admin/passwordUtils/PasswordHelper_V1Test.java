package com.thoughtworks.wechat_application.services.admin.passwordUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.thoughtworks.wechat_core.util.HashHelper.hash;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PasswordHelper_V1Test {
    private PasswordHelper passwordHelper;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
        });

        passwordHelper = injector.getInstance(PasswordHelper.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final PasswordHelper anotherHelper = injector.getInstance(PasswordHelper.class);
        assertThat(passwordHelper, equalTo(anotherHelper));
    }

    @Test
    public void make_sure_sha256_hash_algorithm_exist() throws Exception {
        final String hashMessage = hash("Some word.", PasswordHelper_V1.DEFAULT_ALGORITHM);

        assertThat(hashMessage.length(), equalTo(64));
    }

    @Test
    public void testSaltHash_SaltLength() throws Exception {
        final String hashMessage = passwordHelper.saltHash("some password.");

        assertThat(hashMessage.length(), equalTo(80));
    }

    @Test
    public void testSaltHash_RandomSalt() throws Exception {
        final String hashMessage1 = passwordHelper.saltHash("some password");
        final String hashMessage2 = passwordHelper.saltHash("some password");

        assertThat(hashMessage1.length(), equalTo(80));
        assertThat(hashMessage2.length(), equalTo(80));
        assertThat(hashMessage1, not(equalTo(hashMessage2)));
    }

    @Test
    public void testGetSaltFromHashedPassword() throws Exception {
        final String hashMessage = passwordHelper.saltHash("some password");
        final String salt = passwordHelper.getSaltFromHashedPassword(hashMessage);

        assertThat(salt.length(), equalTo(16));
    }

    @Test
    public void testSaltHash_SameSalt() throws Exception {
        final String hashMessage = passwordHelper.saltHash("some password");
        final String salt = passwordHelper.getSaltFromHashedPassword(hashMessage);
        final String newHashMessage = passwordHelper.saltHash("some password", salt);

        assertThat(newHashMessage.length(), equalTo(80));
        assertThat(newHashMessage, equalTo(hashMessage));
    }
}