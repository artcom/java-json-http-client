package de.artcom.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.awaitility.Awaitility;
import okhttp3.Call;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpQueryTest {
    private static final Logger LOG = Logger.getLogger(HttpQueryTest.class.getSimpleName());

    private static MockWebServer webServer;

    @Before
    public void setup() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @After
    public void teardown() throws IOException {
        webServer.shutdown();
    }

    static class ResponseClass {
        @JsonCreator
        ResponseClass(@JsonProperty("foo") String foo, @JsonProperty("bar") int bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public final String foo;
        public final int bar;
    }

    @Test
    public void get() throws IOException {
        final ResponseClass expected = new ResponseClass("foo", 1234);
        webServer.enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expected)));

        String uri = webServer.url("").toString();
        ResponseClass actual = new Get<ResponseClass>(uri) {}.execute();

        assertNotNull(actual);
        assertEquals(expected.foo, actual.foo);
        assertEquals(expected.bar, actual.bar);
    }

    @Test
    public void getAsync() throws JsonProcessingException {
        final ResponseClass expected = new ResponseClass("foo", 1234);
        webServer.enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expected)));

        final ResponseClass[] actual = {null};
        String uri = webServer.url("").toString();
        new GetAsync<ResponseClass>(uri) {
            @Override
            public void onResult(ResponseClass result) {
                actual[0] = result;
            }

            @Override
            public void onFailure(Call call, IOException e) {}
        }.execute();


        Awaitility.await().atMost(1, TimeUnit.SECONDS).until(new Runnable() {
            @Override
            public void run() {
                assertNotNull(actual[0]);
                assertEquals(expected.foo, actual[0].foo);
                assertEquals(expected.bar, actual[0].bar);
            }
        });
    }

    @Test
    public void post() throws IOException {
        final ResponseClass expected = new ResponseClass("foo", 1234);
        webServer.enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expected)));

        Object body = new Object() {
            String topic = "foo";
        };

        String uri = webServer.url("").toString();
        ResponseClass actual = new Post<ResponseClass>(uri, body) {}.execute();

        assertNotNull(actual);
        assertEquals(expected.foo, actual.foo);
        assertEquals(expected.bar, actual.bar);
    }

    @Test
    public void postAsync() throws IOException {
        final ResponseClass expected = new ResponseClass("foo", 1234);
        webServer.enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expected)));

        Object body = new Object() {
            String topic = "foo";
        };

        final ResponseClass[] actual = {null};
        String uri = webServer.url("").toString();
        new PostAsync<ResponseClass>(uri, body) {
            @Override
            public void onResult(ResponseClass result) {
                actual[0] = result;
            }

            @Override
            public void onFailure(Call call, IOException e) {}
        }.execute();


        Awaitility.await().atMost(1, TimeUnit.SECONDS).until(new Runnable() {
            @Override
            public void run() {
                assertNotNull(actual[0]);
                assertEquals(expected.foo, actual[0].foo);
                assertEquals(expected.bar, actual[0].bar);
            }
        });
    }
}
