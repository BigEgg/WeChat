package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.core.ExpirableResource;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ExpirableResourceDAOTest extends AbstractDAOTest {
    private ExpirableResourceDAO expirableResourceDAO;

    @Before
    public void SetUp() throws Exception {
        expirableResourceDAO = getDAO(ExpirableResourceDAO.class);
    }

    @Test
    public void testCreateResource() throws Exception {
        long resourceId = expirableResourceDAO.createResource("key1", "type1", "value", 1, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(1L));

        resourceId = expirableResourceDAO.createResource("key2", "type2", "value", 1, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateResource_SameKeyAndValue() throws Exception {
        expirableResourceDAO.createResource("key", "type", "value", 1, getHappenedTime(), getHappenedTime());
        expirableResourceDAO.createResource("key", "type", "value", 1, getHappenedTime(), getHappenedTime());
    }

    @Test
    public void testUpdateResource() throws Exception {
        final long resourceId = expirableResourceDAO.createResource("key", "type", "value", 1, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(1L));

        expirableResourceDAO.updateResource("key", "type", "newValue", 60, getHappenedTime());

        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, notNullValue());
        assertThat(resource.getKey(), equalTo("key"));
        assertThat(resource.getType(), equalTo("type"));
        assertThat(resource.isExpired(), equalTo(false));
        assertThat(resource.getValue().isPresent(), equalTo(true));
        assertThat(resource.getValue().get(), equalTo("newValue"));
    }

    @Test
    public void testUpdateResource_NotExist() throws Exception {
        expirableResourceDAO.updateResource("key", "type", "newValue", 60, getHappenedTime());

        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, nullValue());
    }

    @Test
    public void testGetResource_Expire() throws Exception {
        final long resourceId = expirableResourceDAO.createResource("key", "type", "value", 1, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(1L));

        Thread.sleep(5000);
        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, notNullValue());
        assertThat(resource.getKey(), equalTo("key"));
        assertThat(resource.getType(), equalTo("type"));
        assertThat(resource.isExpired(), equalTo(true));
        assertThat(resource.getValue().isPresent(), equalTo(false));
    }

    @Test
    public void testGetResource_NeverExpire() throws Exception {
        final long resourceId = expirableResourceDAO.createResource("key", "type", "value", 0, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(1L));

        Thread.sleep(5000);
        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, notNullValue());
        assertThat(resource.getKey(), equalTo("key"));
        assertThat(resource.getType(), equalTo("type"));
        assertThat(resource.isExpired(), equalTo(false));
        assertThat(resource.getValue().isPresent(), equalTo(true));
        assertThat(resource.getValue().get(), equalTo("value"));
    }

    @Test
    public void testGetResource_NotExpire() throws Exception {
        final long resourceId = expirableResourceDAO.createResource("key", "type", "value", 60, getHappenedTime(), getHappenedTime());
        assertThat(resourceId, equalTo(1L));

        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, notNullValue());
        assertThat(resource.getKey(), equalTo("key"));
        assertThat(resource.getType(), equalTo("type"));
        assertThat(resource.isExpired(), equalTo(false));
        assertThat(resource.getValue().isPresent(), equalTo(true));
        assertThat(resource.getValue().get(), equalTo("value"));
    }

    @Test
    public void testGetResource_NotExist() throws Exception {
        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, nullValue());
    }

    @Test
    public void testDeleteResources() throws Exception {
        expirableResourceDAO.createResource("key", "type", "value", 60, getHappenedTime(), getHappenedTime());

        expirableResourceDAO.deleteResources("key", "type");
        final ExpirableResource resource = expirableResourceDAO.getResource("key", "type");
        assertThat(resource, nullValue());
    }

    @Test
    public void testDeleteResources_NotExist() throws Exception {
        expirableResourceDAO.deleteResources("key", "type");
    }
}