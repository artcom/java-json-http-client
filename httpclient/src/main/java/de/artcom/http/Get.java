package de.artcom.http;


import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Get<T> extends Base {
    protected Get(String uri) {
        super(uri);
    }

    public T execute() throws IOException {
        Response response = CLIENT.newCall(new Request.Builder().url(uri).build()).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.toString());
        }

        // retrieve the runtime class of T
        Type arg = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return READ_MAPPER.readValue(response.body().string(), READ_MAPPER.getTypeFactory().constructType(arg));
    }
}
