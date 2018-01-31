package de.artcom.http;


import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Post<T> extends Base {
    private final Object body;

    protected Post(String uri, Object body) {
        super(uri);
        this.body = body;
    }

    public T execute() throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), WRITE_MAPPER.writeValueAsBytes(this.body));
        Response response = CLIENT.newCall(new Request.Builder().url(uri).post(body).build()).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.toString());
        }

        // retrieve the runtime class of T
        Type arg = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return READ_MAPPER.readValue(response.body().string(), READ_MAPPER.getTypeFactory().constructType(arg));
    }
}
