package de.artcom.http;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GetAsync<T> extends Base implements Callback {
    protected GetAsync(String uri) {
        super(uri);
    }

    public void execute() {
        CLIENT.newCall(new Request.Builder().url(uri).build()).enqueue(this);
    }

    public void onResponse(Call call, Response response) throws IOException {
        // retrieve the runtime class of T
        Type arg = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T result = READ_MAPPER.readValue(response.body().string(), READ_MAPPER.getTypeFactory().constructType(arg));
        onResult(result);
    }

    public abstract void onResult(T result);

    public abstract void onFailure(Call Call, IOException e);
}
