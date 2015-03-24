package com.thoughtworks.wechat_application.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public abstract class ResourceTestBase {
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
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(getResponseJson(response), clazz);
    }
}
