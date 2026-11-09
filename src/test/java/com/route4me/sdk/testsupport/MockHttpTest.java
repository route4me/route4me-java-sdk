package com.route4me.sdk.testsupport;

import com.google.gson.JsonObject;
import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.Manager;
import com.route4me.sdk.ManagerTestAccess;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
    private static class ProbeManager extends Manager {
        ProbeManager() {
            this(MockHttp.API_KEY);
        }

        ProbeManager(String apiKey) {
            super(apiKey);
        }

        ProbeManager(String proxyHost, int proxyPort) {
            super(MockHttp.API_KEY, proxyHost, proxyPort, "http");
        }

        String get(String path) throws APIException {
            URIBuilder builder = defaultBuilder(path);
            builder.setParameter("route_id", "R1");
            return makeRequest(RequestMethod.GET, builder, "", String.class);
        }

        JsonObject postJson(String path, Object body) throws APIException {
            return makeJSONRequest(RequestMethod.POST, defaultBuilder(path), body, JsonObject.class);
        }

        AsyncJob startJob(String path) throws APIException {
            return makeJobRequest(RequestMethod.POST, defaultBuilder(path), "{}");
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
    public void answersWithResponseHeaders() throws Exception {
        ProbeManager manager = http.install(new ProbeManager());
        http.respondWithHeaders(202, null, "X-Job-Id", "JOB-1",
                "Location", "https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/status/JOB-1")
                .respondWithHeaders(200, MockHttp.json("{'status':true}"));

        AsyncJob queued = manager.startJob("/api.v4/route.php");
        AsyncJob done = manager.startJob("/api.v4/route.php");

        assertEquals(202, queued.getStatusCode());
        assertEquals("JOB-1", queued.getJobId());
        assertEquals("https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/status/JOB-1",
                queued.getLocation());
        assertEquals("", queued.getBody());
        assertEquals(200, done.getStatusCode());
        assertNull(done.getJobId());
        assertNull(done.getLocation());
        assertEquals("{\"status\":true}", done.getBody());
    }

    @Test
    public void responseHeadersComeInPairs() {
        http.expectThrows(IllegalArgumentException.class, () -> http.respondWithHeaders(202, null, "X-Job-Id"));
    }

    @Test
    public void everyUriRequestOverloadGoesThroughTheGuard() throws Exception {
        CloseableHttpClient client = ManagerTestAccess.httpClient(http.install(new ProbeManager()));
        String url = "https://api.route4me.com/api.v4/route.php?route_id=R";
        http.respond(200, "one", "text/plain").respond(200, "two", "text/plain")
                .respond(200, "three", "text/plain").respond(200, "four", "text/plain");

        try (CloseableHttpResponse plain = client.execute(new HttpGet(url + 1))) {
            assertEquals("one", EntityUtils.toString(plain.getEntity()));
        }
        try (CloseableHttpResponse withContext = client.execute(new HttpGet(url + 2), new BasicHttpContext())) {
            assertEquals("two", EntityUtils.toString(withContext.getEntity()));
        }
        ResponseHandler<String> text = response -> EntityUtils.toString(response.getEntity());
        assertEquals("three", client.execute(new HttpGet(url + 3), text));
        assertEquals("four", client.execute(new HttpGet(url + 4), text, null));

        assertEquals(4, http.requestCount());
        for (int i = 0; i < 4; i++) {
            http.request(i).assertQuery("route_id", "R" + (i + 1));
        }
    }

    @Test
    public void theGuardChecksTheContextAndHandlerOverloadsToo() {
        AssertionError error = runInRule(inner -> {
            CloseableHttpClient client = ManagerTestAccess.httpClient(inner.install(new ProbeManager()));
            try {
                client.execute(new HttpGet("https://example.com/collect?api_key=" + MockHttp.API_KEY),
                        response -> "read");
            } catch (AssertionError swallowed) {
                // the code under test swallows the guard failure
            }
        });

        assertTrue(error.getMessage(), error.getMessage().contains("host 'example.com' is not in allowed_hosts"));
        assertFalse(error.getMessage(), error.getMessage().contains(MockHttp.API_KEY));
    }

    @Test
    public void anExecuteOverloadThatBypassesTheGuardFailsTheTestEvenWhenCaught() {
        AssertionError error = runInRule(inner -> {
            CloseableHttpClient client = ManagerTestAccess.httpClient(inner.install(new ProbeManager()));
            try {
                client.execute(new HttpHost("api.route4me.com", 443, "https"), new HttpGet("/api.v4/route.php"));
                fail("expected the mocked client to refuse an unrouted overload");
            } catch (AssertionError swallowed) {
                assertTrue(swallowed.getMessage(), swallowed.getMessage().contains("does not route"));
            }
        });

        assertTrue(error.getMessage(),
                error.getMessage().contains("CloseableHttpClient.execute(org.apache.http.HttpHost"));
        assertTrue(error.getMessage(), error.getMessage().contains("does not route through the request guard"));
    }

    @Test
    public void answersTheNextRequestWithCustomCode() throws Exception {
        final ProbeManager manager = http.install(new ProbeManager());
        final CapturedRequest[] seen = new CapturedRequest[1];
        http.answerNextRequest(request -> {
            seen[0] = request;
            throw new IllegalStateException("custom failure");
        }).respond(200, "after", "text/plain");

        APIException error = http.expectApiException(() -> manager.get("/api.v4/route.php"));

        assertTrue(error.getMessage(), error.getMessage().endsWith(" failed: custom failure"));
        assertSame("the answer gets the captured request", http.request(0), seen[0]);
        assertEquals("the queue goes on after the answer", "after", manager.get("/api.v4/route.php"));
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
    public void aManagerThatWasNotInstalledCannotReachTheNetwork() throws Exception {
        assertTrue("the surefire listener or the rule installs the block", NetworkBlock.isInstalled());
        // A proxy given as an address literal (TEST-NET-1, RFC 5737): no DNS lookup comes first,
        // so the request reaches the socket connect, which the block refuses.
        try (ProbeManager manager = new ProbeManager("192.0.2.1", 3128)) {
            manager.get("/api.v4/route.php");
            fail("expected the network block to refuse the connection");
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage(), expected.getMessage().startsWith(NetworkBlock.MESSAGE));
            assertFalse(expected.getMessage(), expected.getMessage().contains(MockHttp.API_KEY));
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(InetAddress.getByAddress(new byte[] {(byte) 192, 0, 2, 1}), 443), 100);
            fail("expected the network block to refuse the connection");
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage(), expected.getMessage().contains("socket://192.0.2.1:443"));
        }
        assertEquals(Collections.singletonList(Proxy.NO_PROXY),
                ProxySelector.getDefault().select(URI.create("socket://127.0.0.1:8080")));
    }

    @Test
    public void onlyLoopbackLiteralsPassTheNetworkBlock() {
        for (String host : Arrays.asList("127.0.0.1", "127.1.2.3", "localhost", "[::1]", "0:0:0:0:0:0:0:1")) {
            assertTrue(host, NetworkBlock.isLoopback(host));
        }
        for (String host : Arrays.asList("127.0.0.1.example.net", "127.example.com", "127.0.0.256", "127.0.0",
                "127.0.0.1.", "127.0.0.x", "128.0.0.1", "0127.0.0.1", "", null)) {
            assertFalse(String.valueOf(host), NetworkBlock.isLoopback(host));
        }
        try {
            ProxySelector.getDefault().select(URI.create("socket://127.0.0.1.example.net:443"));
            fail("expected the network block to refuse a name that only starts like a loopback address");
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage(), expected.getMessage().startsWith(NetworkBlock.MESSAGE));
        }
    }

    @Test
    public void installRefusesAManagerWithAnotherKey() {
        String otherKey = "22222222222222222222222222222222";
        IllegalArgumentException error = http.expectThrows(IllegalArgumentException.class,
                () -> http.install(new ProbeManager(otherKey)));

        assertFalse(error.getMessage(), error.getMessage().contains(otherKey));
        http.install(new ProbeManager((String) null));
    }

    @Test
    public void failsATestThatLogsTheKeyThroughLog4jAtAnyLevel() {
        assertTrue("the test Log4j provider is registered", Log4jCapture.isInstalled());
        final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MockHttpTest.class);

        AssertionError trace = runInRule(() -> logger.trace("request sent with {}", MockHttp.API_KEY));
        AssertionError thrown = runInRule(() -> logger.warn("failed", new IllegalStateException(MockHttp.API_KEY)));

        assertTrue(trace.getMessage(),
                trace.getMessage().contains("Log4j TRACE message from " + MockHttpTest.class.getName()));
        assertTrue(thrown.getMessage(), thrown.getMessage().contains(IllegalStateException.class.getName()));
        assertFalse(trace.getMessage().contains(MockHttp.API_KEY));
    }

    @Test
    public void failsATestThatLogsTheKeyThroughJavaUtilLoggingAtFine() {
        final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.route4me.sdk.probe");

        AssertionError error = runInRule(() -> logger.log(Level.FINE, "sent {0}", MockHttp.API_KEY));

        assertTrue(error.getMessage(), error.getMessage().contains("log message from com.route4me.sdk.probe"));
    }

    @Test
    public void reportsAGuardFailureEvenWhenTheTestIsSkipped() {
        AssertionError error = runInRule(() -> {
            throw new AssumptionViolatedException("skipped after the request");
        }, true);

        assertTrue(error.getMessage(), error.getMessage().contains("is not a known endpoint"));
        assertTrue(error.getSuppressed()[0] instanceof AssumptionViolatedException);
    }

    @Test
    public void toStringMasksAnyApiKeyValue() throws Exception {
        CapturedRequest request = CapturedRequest.from(
                new HttpGet("https://api.route4me.com/api.v4/route.php?api_key=OTHER-KEY&route_id=R1"));

        assertEquals("GET https://api.route4me.com/api.v4/route.php?api_key=<api_key>&route_id=R1", request.toString());
    }

    /** Runs code inside a fresh {@link MockHttp} rule and returns the guard failure it causes. */
    private static AssertionError runInRule(MockHttp.Call call) {
        return runInRule(call, false);
    }

    /** Code that runs inside a fresh {@link MockHttp} rule and uses it. */
    private interface RuleCall {
        void run(MockHttp inner) throws Exception;
    }

    /** Runs code inside a fresh {@link MockHttp} rule, which it gets, and returns the guard failure it causes. */
    private static AssertionError runInRule(final RuleCall call) {
        final MockHttp inner = new MockHttp();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                call.run(inner);
            }
        };
        return evaluate(inner, statement);
    }

    /**
     * Runs code inside a fresh {@link MockHttp} rule, with a manager installed on it when
     * {@code installProbe} is set, and returns the guard failure it causes.
     */
    private static AssertionError runInRule(final MockHttp.Call call, final boolean installProbe) {
        final MockHttp inner = new MockHttp();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (installProbe) {
                    final ProbeManager probe = inner.install(new ProbeManager());
                    try {
                        probe.get("/api.v4/no-such-endpoint.php");
                    } catch (AssertionError swallowed) {
                        // the code under test swallows the guard failure
                    }
                }
                call.run();
            }
        };
        return evaluate(inner, statement);
    }

    /** Runs a statement inside a rule and returns the guard failure it causes. */
    private static AssertionError evaluate(MockHttp inner, Statement statement) {
        try {
            inner.apply(statement, Description.createTestDescription(MockHttpTest.class, "inner")).evaluate();
        } catch (AssertionError expected) {
            return expected;
        } catch (Throwable other) {
            throw new AssertionError("expected a guard failure, got " + other, other);
        }
        throw new AssertionError("expected a guard failure, but the inner test passed");
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
