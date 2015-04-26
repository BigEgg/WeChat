package com.thoughtworks.wechat_application.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public abstract class ResourceTestBase {
    protected static ObjectMapper MAPPER = Jackson.newObjectMapper();

    protected static String getResponseJson(final Response response) {
        final ByteArrayInputStream entity = (ByteArrayInputStream) response.getEntity();
        Scanner scanner = new Scanner(entity);
        scanner.useDelimiter("\\Z");//To read all scanner content in one String
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();

        return data;
    }

    protected static <T> T getResponseEntity(final Response response, final Class<T> clazz) throws Exception {
        return MAPPER.readValue(getResponseJson(response), clazz);
    }

    protected static <T> T deserializeFixture(final String fileName, final Class<T> clazz) throws Exception {
        return MAPPER.readValue(fixture(fileName), clazz);
    }

}
