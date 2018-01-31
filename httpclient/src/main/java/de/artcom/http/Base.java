package de.artcom.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

class Base {
    static final OkHttpClient CLIENT = new OkHttpClient();
    static final ObjectMapper READ_MAPPER = new ObjectMapper();
    static final ObjectMapper WRITE_MAPPER = new ObjectMapper();

    static {
        READ_MAPPER.setVisibility(READ_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY));

        WRITE_MAPPER.setVisibility(WRITE_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    final String uri;

    Base(String uri) {
        this.uri = uri;
    }
}
