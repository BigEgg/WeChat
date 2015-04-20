package com.thoughtworks.wechat_application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public abstract class APIModelTestBase {
    protected static ObjectMapper MAPPER = Jackson.newObjectMapper();

    protected static <T> T deserializeFixture(final String fileName, final Class<T> clazz) throws Exception {
        return MAPPER.readValue(fixture(fileName), clazz);
    }

    protected static <T> String serializeObject(final T object) throws Exception {
        return jsonIgnoreSpace(MAPPER.writeValueAsString(object));
    }

    protected static String getResource(final String fileName) throws Exception {
        return jsonIgnoreSpace(fixture(fileName));
    }

    private static String jsonIgnoreSpace(final String original) {
        return original.replace(" ", "").replace("\n", "");
    }
}
