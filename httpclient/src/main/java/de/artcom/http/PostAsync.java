package de.artcom.http;


import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class PostAsync<T> extends Base implements Callback {
    private final Object body;

    protected PostAsync(String uri, Object body) {
        super(uri);
        this.body = body;
    }

    public void execute() throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), WRITE_MAPPER.writeValueAsBytes(this.body));
        CLIENT.newCall(new Request.Builder().url(uri).post(body).build()).enqueue(this);
    }

    public void onResponse(Call call, Response response) throws IOException {
        // retrieve the runtime class of T
        Type arg = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T result = READ_MAPPER.readValue(response.body().string(), READ_MAPPER.getTypeFactory().constructType(arg));
        onResult(result);
    }

    public abstract void onResult(T result);

    public abstract void onFailure(Call call, IOException e);
}
