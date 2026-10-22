package com.route4me.sdk.testsupport;

import com.google.gson.JsonObject;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link MockHttp} rule itself: request capture, canned responses and the
 * assertion helpers of {@link CapturedRequest}.
 */
public class MockHttpTest {

    @Rule
    public final MockHttp http = new MockHttp();

    /** A minimal manager that exposes the protected request methods of {@link Manager}. */
    private static final class ProbeManager extends Manager {
        ProbeManager() {
            super(MockHttp.API_KEY);
        }

        String get(String path) throws APIException {
            URIBuilder builder = defaultBuilder(path);
            builder.setParameter("route_id", "R1");
            return makeRequest(RequestMethod.GET, builder, "", String.class);
        }

        JsonObject postJson(String path, Object body) throws APIException {
            return makeJSONRequest(RequestMethod.POST, defaultBuilder(path), body, JsonObject.class);
        }
    }

    @Test
    public void capturesMethodUriHeadersAndQuery() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        http.respond(200, "plain text", "text/plain");

        assertEquals("plain text", manager.get("/api.v4/route.php"));

        http.request()
                .assertEndpoint("GET", "api.route4me.com", "/api.v4/route.php")
                .assertScheme("https")
                .assertQuery("route_id", "R1")
                .assertApiKeyInQuery()
                .assertHeader("User-Agent", "Route4Me Java SDK")
                .assertNoBody();
        assertEquals("the manager keeps its client between requests", 0, http.closeCount());
    }

    @Test
    public void capturesJsonBodyAndContentType() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        http.respond(200, MockHttp.json("{'ok':true}"));

        JsonObject result = manager.postJson("/api.v4/route.php",
                Collections.singletonMap("addresses", Collections.singletonList(Collections.singletonMap("lat", 1.5))));

        assertTrue(result.get("ok").getAsBoolean());
        http.request()
                .assertContentType("application/json")
                .assertJsonHas("addresses", "addresses[0].lat")
                .assertJson("addresses[0].lat", 1.5)
                .assertJsonLacks("missing", "addresses[1]");
        assertEquals("[addresses]", http.request().jsonKeys().toString());
    }

    @Test
    public void answersQueuedResponsesInOrderThenTheDefault() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        http.respond(200, "first", "text/plain").respond(200, "second", "text/plain").respondToAll(200, "\"any\"");

        assertEquals("first", manager.get("/api.v4/route.php"));
        assertEquals("second", manager.get("/api.v4/route.php"));
        assertEquals("\"any\"", manager.get("/api.v4/route.php"));
        assertEquals(3, http.requestCount());
        assertNull(http.request(1).getBody());
    }

    @Test
    public void readsFixtures() {
        assertTrue(Fixtures.read("error_400.json").contains("errors"));
    }

    @Test
    public void failsClearlyWhenNoResponseIsQueued() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        try {
            manager.get("/api.v4/route.php");
            fail("expected an AssertionError");
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains("No canned response"));
        }
    }

    @Test
    public void networkFailureBecomesApiException() throws Exception {
        final ProbeManager manager = http.install(new ProbeManager());
        http.failNextRequest(new IOException("connection reset"));

        APIException error = http.expectApiException(() -> manager.get("/api.v4/route.php"));

        assertTrue(error.getMessage().contains("connection reset"));
    }

    @Test
    public void toStringMasksTheApiKey() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        http.respond(200, "ok", "text/plain");
        manager.get("/api.v4/route.php");

        String text = http.request().toString();
        assertTrue(text, text.contains("api_key=<api_key>"));
        assertTrue(text, !text.contains(MockHttp.API_KEY));
    }
}
