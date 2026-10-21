package com.route4me.sdk;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.testsupport.MockHttp;
import com.route4me.sdk.utils.Sanitizer;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.RedirectException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Manager}: how it builds requests, sends them through the HTTP
 * client, and turns responses and errors into results or {@link APIException}s.
 *
 * <p>The tests call the protected request methods directly; this class is in the same
 * package as {@code Manager}.
 */
public class ManagerTest {

    /** A registry endpoint that accepts any method. */
    private static final String PATH = "/api.v4/route.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** A concrete manager with no behaviour of its own. */
    static final class TestManager extends Manager {
        TestManager(String apiKey) {
            super(apiKey);
        }

        TestManager(String apiKey, boolean disableRedirects) {
            super(apiKey, disableRedirects);
        }

        TestManager(String apiKey, boolean disableRedirects, String callBackURL) {
            super(apiKey, disableRedirects, callBackURL);
        }

        TestManager(String apiKey, GsonBuilder builder) {
            super(apiKey, builder);
        }

        TestManager(String apiKey, String proxyHost, int proxyPort, String proxySchema) {
            super(apiKey, proxyHost, proxyPort, proxySchema);
        }

        TestManager(String apiKey, String proxyHost, int proxyPort, String proxySchema, boolean disableRedirects) {
            super(apiKey, proxyHost, proxyPort, proxySchema, disableRedirects);
        }
    }

    /** Query parameters, converted by {@code QueryConverter}. */
    static final class Params {
        @QueryParameter("limit")
        Integer limit = 5;
        @QueryParameter("active")
        Boolean active = Boolean.TRUE;
        @QueryParameter("skipped")
        String skipped;
    }

    /** A request body with one annotated and one plain field. */
    static final class Body {
        @SerializedName("route_name")
        String routeName = "Morning";
        String internalNote = "plain field";
    }

    /** A response type. */
    static final class Reply {
        @SerializedName("route_id")
        String routeId;
        @SerializedName("count")
        int count;
    }

    /** Returns a builder for {@link #PATH} on the V4 API host. */
    private static URIBuilder builder() {
        return Manager.defaultBuilder(PATH);
    }

    /** Returns a builder for {@code path} on the V5 host. */
    private static URIBuilder wh(String path) {
        return new URIBuilder().setScheme("https").setHost("wh.route4me.com").setPath(path);
    }

    /** Returns a manager that sends its requests to the mocked client. */
    private TestManager manager() {
        return http.install(new TestManager(MockHttp.API_KEY));
    }

    // ------------------------------------------------------------------ request building

    @Test
    public void defaultBuilderTargetsTheV4ApiHostOverHttps() {
        URIBuilder builder = Manager.defaultBuilder("/api.v4/order.php");
        assertEquals("https", builder.getScheme());
        assertEquals("api.route4me.com", builder.getHost());
        assertEquals("/api.v4/order.php", builder.getPath());
    }

    @Test
    public void getSendsApiKeyQueryAndUserAgentAndParsesTheJson() throws Exception {
        TestManager manager = manager();
        http.respond(200, json("{'route_id':'R1','count':3}"));
        URIBuilder builder = builder();
        builder.setParameter("route_id", "R1");

        Reply reply = manager.makeRequest(RequestMethod.GET, builder, "", Reply.class);

        assertEquals("R1", reply.routeId);
        assertEquals(3, reply.count);
        http.request()
                .assertEndpoint("GET", "api.route4me.com", PATH)
                .assertQuery("route_id", "R1")
                .assertApiKeyInQuery()
                .assertHeader("User-Agent", "Route4Me Java SDK")
                .assertNoBody();
        assertNull(http.request().header("Content-Type"));
        assertEquals("the client is kept for the next request", 0, http.closeCount());
    }

    @Test
    public void theCallersBuilderIsNotChanged() throws Exception {
        TestManager manager = http.install(new TestManager(MockHttp.API_KEY, true, "https://example.com/done"));
        http.respond(200, "{}").respond(200, "{}");
        URIBuilder builder = builder();
        builder.setParameter("route_id", "R1");

        manager.makeRequest(RequestMethod.GET, builder, "", Reply.class);
        manager.makeRequest(RequestMethod.GET, builder, "", Reply.class);

        assertEquals(1, builder.getQueryParams().size());
        for (int i = 0; i < 2; i++) {
            http.request(i).assertApiKeyInQuery().assertQuery("redirect", "0")
                    .assertQuery("optimized_callback_url", "https://example.com/done").assertQuery("route_id", "R1");
        }
    }

    @Test
    public void v5RequestsSendTheKeyAsABearerToken() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}").respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, wh("/modules/api/v5.0/vehicles"), "", Reply.class);
        manager.makeRequest(RequestMethod.POST, wh("/modules/api/v5.1/routes"), "{}", Reply.class);

        http.request(0).assertApiKeyAsBearer().assertHeader("Authorization", "Bearer " + MockHttp.API_KEY);
        http.request(1).assertApiKeyAsBearer();
    }

    @Test
    public void otherRequestsKeepTheKeyInTheQuery() throws Exception {
        TestManager manager = manager();
        http.respondToAll(200, "{}");
        URIBuilder rapid = new URIBuilder().setScheme("https").setHost("rapid.route4me.com").setPath("/timezone");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);
        manager.makeRequest(RequestMethod.GET, wh("/modules/api/vehicles"), "", Reply.class);
        manager.makeRequest(RequestMethod.GET, wh("/api.v4/note_custom_types.php"), "", Reply.class);
        manager.makeRequest(RequestMethod.GET, rapid, "", Reply.class);

        for (int i = 0; i < 4; i++) {
            http.request(i).assertApiKeyInQuery();
        }
    }

    @Test
    public void managerWithoutKeySendsNoAuthorizationHeader() throws Exception {
        TestManager manager = http.install(new TestManager(null));
        http.respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, wh("/modules/api/v5.0/vehicles"), "", Reply.class);

        assertNull(http.request().header("Authorization"));
        http.request().assertNoQuery("api_key");
    }

    @Test
    public void onlyV5PathsOnTheV5HostUseTheBearerToken() {
        assertTrue(Manager.sendsKeyAsBearerToken("wh.route4me.com", "/modules/api/v5.0/vehicles"));
        assertTrue(Manager.sendsKeyAsBearerToken("WH.ROUTE4ME.COM", "/modules/api/v5.1/routes"));
        assertTrue(Manager.sendsKeyAsBearerToken("wh.route4me.com", "/modules/api/v5.0"));
        assertFalse(Manager.sendsKeyAsBearerToken("wh.route4me.com", "/modules/api/v5.00/vehicles"));
        assertFalse(Manager.sendsKeyAsBearerToken("wh.route4me.com", "/modules/api/vehicles"));
        assertFalse(Manager.sendsKeyAsBearerToken("wh.route4me.com", "/api/v5.0/geotab-integration/x"));
        assertFalse(Manager.sendsKeyAsBearerToken("api.route4me.com", "/modules/api/v5.0/vehicles"));
        assertFalse(Manager.sendsKeyAsBearerToken(null, "/modules/api/v5.0/vehicles"));
        assertFalse(Manager.sendsKeyAsBearerToken("wh.route4me.com", null));
    }

    @Test
    public void managerWithoutKeySendsNoApiKey() throws Exception {
        TestManager manager = http.install(new TestManager(null));
        http.respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        http.request().assertNoQuery("api_key");
    }

    @Test
    public void disabledRedirectsAddRedirectZeroAndStillUseTheInstalledClient() throws Exception {
        TestManager manager = http.install(new TestManager(MockHttp.API_KEY, true));
        http.respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        http.request().assertQuery("redirect", "0").assertApiKeyInQuery();
    }

    @Test
    public void callbackUrlIsSentAsQueryParameter() throws Exception {
        TestManager manager = http.install(new TestManager(MockHttp.API_KEY, false, "https://example.com/done"));
        http.respond(200, "{}").respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);
        manager.setCallBackURL("https://example.com/other");
        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        http.request(0).assertQuery("optimized_callback_url", "https://example.com/done").assertNoQuery("redirect");
        http.request(1).assertQuery("optimized_callback_url", "https://example.com/other");
        assertEquals("https://example.com/other", manager.getCallBackURL());
    }

    @Test
    public void proxySettingsAreAppliedToEveryRequest() throws Exception {
        TestManager plain = http.install(new TestManager(MockHttp.API_KEY, "proxy.example.com", 3128, "http"));
        TestManager noRedirects = http.install(
                new TestManager(MockHttp.API_KEY, "proxy.example.com", 3128, "https", true));
        http.respond(200, "{}").respond(200, "{}");

        plain.makeRequest(RequestMethod.GET, builder(), "", Reply.class);
        noRedirects.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        assertEquals("http://proxy.example.com:3128", http.request(0).getProxy());
        http.request(0).assertNoQuery("redirect");
        assertEquals("https://proxy.example.com:3128", http.request(1).getProxy());
        http.request(1).assertQuery("redirect", "0");
    }

    @Test
    public void defaultGsonSerializesEveryField() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");

        manager.makeJSONRequest(RequestMethod.POST, builder(), new Body(), Reply.class);

        http.request().assertJson("route_name", "Morning").assertJson("internalNote", "plain field");
    }

    @Test
    public void gsonBuilderConstructorSkipsFieldsWithoutSerializedName() throws Exception {
        TestManager manager = http.install(new TestManager(MockHttp.API_KEY, new GsonBuilder()));
        http.respond(200, "{}");

        manager.makeJSONRequest(RequestMethod.POST, builder(), new Body(), Reply.class);

        http.request().assertJson("route_name", "Morning").assertJsonLacks("internalNote");
    }

    // ------------------------------------------------------------------ request bodies

    @Test
    public void formBodyIsUrlEncoded() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");
        List<NameValuePair> form = Arrays.<NameValuePair>asList(
                new BasicNameValuePair("route_name", "Morning run"), new BasicNameValuePair("to", "none"));

        manager.makeRequest(RequestMethod.POST, builder(), form, Reply.class);

        http.request()
                .assertMethod("POST")
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("route_name", "Morning run")
                .assertForm("to", "none");
    }

    @Test
    public void stringBodyIsSentUnchangedAndEmptyStringMeansNoBody() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}").respond(200, "{}");

        manager.makeRequest(RequestMethod.PUT, builder(), "{\"a\":1}", Reply.class);
        manager.makeRequest(RequestMethod.DELETE, builder(), "", Reply.class);

        assertEquals("{\"a\":1}", http.request(0).getBody());
        http.request(0).assertMethod("PUT");
        http.request(1).assertMethod("DELETE").assertNoBody();
    }

    @Test
    public void typeOverloadsParseGenericResponses() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, json("[{'route_id':'R1'},{'route_id':'R2'}]"))
                .respond(200, json("[{'route_id':'R3'}]"))
                .respond(200, "raw text", "text/plain");

        List<Reply> fromString = manager.makeRequest(RequestMethod.GET, builder(), "", listOfReplies);
        List<Reply> fromEntity = manager.makeRequest(RequestMethod.GET, builder(), (HttpEntity) null, listOfReplies);
        String raw = manager.makeRequest(RequestMethod.GET, builder(), (HttpEntity) null);

        assertEquals(2, fromString.size());
        assertEquals("R2", fromString.get(1).routeId);
        assertEquals("R3", fromEntity.get(0).routeId);
        assertEquals("raw text", raw);
    }

    @Test
    public void jsonRequestWithBodyAndParamsSendsBoth() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, json("{'route_id':'R1'}")).respond(200, json("[{'route_id':'R2'}]"));

        Reply reply = manager.makeJSONRequest(RequestMethod.POST, builder(), new Body(), new Params(), Reply.class);
        List<Reply> replies = manager.makeJSONRequest(RequestMethod.PUT, builder(), new Body(), new Params(), listOfReplies);

        assertEquals("R1", reply.routeId);
        assertEquals("R2", replies.get(0).routeId);
        for (int i = 0; i < 2; i++) {
            http.request(i)
                    .assertQuery("limit", "5")
                    .assertQuery("active", "1")
                    .assertNoQuery("skipped")
                    .assertHeader("Content-type", "application/json")
                    .assertJson("route_name", "Morning");
        }
    }

    @Test
    public void jsonRequestWithoutParamsSendsTheBodyOnly() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");

        manager.makeJSONRequest(RequestMethod.POST, builder(), new Body(), null, Reply.class);

        http.request().assertNoQuery("limit").assertJson("route_name", "Morning");
    }

    @Test
    public void jsonGetPutsParamsInTheQueryAndSendsNoBody() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, "{}").respond(200, "[]");

        manager.makeJSONRequest(RequestMethod.GET, builder(), new Params(), Reply.class);
        manager.makeJSONRequest(RequestMethod.GET, builder(), new Params(), listOfReplies);

        for (int i = 0; i < 2; i++) {
            http.request(i).assertMethod("GET").assertQuery("limit", "5").assertQuery("active", "1").assertNoBody();
        }
    }

    @Test
    public void jsonPostPutsParamsInTheBodyAndNotInTheQuery() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, "{}").respond(200, "[]");

        manager.makeJSONRequest(RequestMethod.POST, builder(), new Params(), Reply.class);
        manager.makeJSONRequest(RequestMethod.PUT, builder(), new Params(), listOfReplies);

        for (int i = 0; i < 2; i++) {
            http.request(i).assertNoQuery("limit").assertJson("limit", 5).assertJson("active", true)
                    .assertJsonLacks("skipped");
        }
    }

    @Test
    public void jsonRequestWithNullParamsSendsNoBody() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, "{}").respond(200, "[]");

        manager.makeJSONRequest(RequestMethod.POST, builder(), (Object) null, Reply.class);
        manager.makeJSONRequest(RequestMethod.POST, builder(), (Object) null, listOfReplies);

        http.request(0).assertNoBody().assertHeader("Content-type", "application/json");
        http.request(1).assertNoBody();
    }

    @Test
    public void getWithBodyIsRejectedBeforeSending() {
        final TestManager manager = manager();

        RuntimeException error = http.expectThrows(RuntimeException.class,
                () -> manager.makeRequest(RequestMethod.GET, builder(), "body", Reply.class));

        assertEquals("Method does not support body!", error.getMessage());
        http.assertNoRequests();
        assertEquals(0, http.closeCount());
    }

    @Test
    public void missingResultTypeReturnsNull() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");

        assertNull(manager.makeRequest(RequestMethod.GET, builder(), "", (Class<Object>) null));
    }

    // ------------------------------------------------------------------ errors

    @Test
    public void errorStatusBecomesApiExceptionWithTheStatusCode() {
        final TestManager manager = manager();
        http.respondWithFixture(400, "error_400.json");
        final URIBuilder builder = builder().setParameter("route_id", "R1");

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder, "", Reply.class));

        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 400 for GET "
                + "https://api.route4me.com" + PATH));
        assertEquals(400, error.getStatusCode());
        assertEquals("GET", error.getMethod());
        assertFalse("the message has no query string: " + error.getMessage(), error.getMessage().contains("route_id"));
        assertTrue(error.getUrl(), error.getUrl().contains("route_id=R1"));
        assertTrue(error.getUrl(), error.getUrl().contains("api_key=" + Sanitizer.REDACTED));
        assertEquals(0, http.closeCount());
    }

    @Test
    public void errorStatusExposesTheV4ErrorsAndTheFirstOneInTheMessage() {
        final TestManager manager = manager();
        http.respond(400, json("{'errors':['Route not found','Second problem'],'timestamp':1}"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertEquals(Arrays.asList("Route not found", "Second problem"), error.getErrors());
        assertTrue(error.getMessage(), error.getMessage().endsWith(": Route not found"));
        assertFalse(error.getMessage(), error.getMessage().contains("Second problem"));
        assertEquals(json("{'errors':['Route not found','Second problem'],'timestamp':1}"), error.getResponseBody());
        assertNull(error.getCode());
        assertTrue(error.getMessages().isEmpty());
    }

    @Test
    public void errorStatusExposesTheV5CodeAndMessages() {
        final TestManager manager = manager();
        http.respond(422, json("{'status':false,'code':422,'exit_code':0,"
                + "'messages':{'vehicle_alias':['The alias is required.'],'vehicle_make':['Unknown make.']}}"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.POST, builder(), "{}", Reply.class));

        assertEquals(422, error.getStatusCode());
        assertEquals(Integer.valueOf(422), error.getCode());
        assertEquals(Collections.singletonList("The alias is required."), error.getMessages().get("vehicle_alias"));
        assertEquals(Collections.singletonList("Unknown make."), error.getMessages().get("vehicle_make"));
        assertTrue(error.getErrors().isEmpty());
        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 422 for POST "));
        assertTrue(error.getMessage(), error.getMessage().endsWith(": The alias is required."));
    }

    @Test
    public void serverErrorBecomesApiException() {
        final TestManager manager = manager();
        http.respond(503, json("{'errors':['unavailable']}"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.POST, builder(), "{}", Reply.class));

        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 503"));
        assertEquals(503, error.getStatusCode());
    }

    @Test
    public void errorMessageNeverShowsTheRawBody() {
        final TestManager manager = manager();
        StringBuilder body = new StringBuilder("<html><body>");
        for (int i = 0; i < 500; i++) {
            body.append("Bad gateway ");
        }
        body.append("</body></html>");
        http.respond(502, body.toString(), "text/html");

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertEquals("Invalid status code 502 for GET https://api.route4me.com" + PATH, error.getMessage());
        assertEquals(APIException.MAX_BODY_LENGTH, error.getResponseBody().length());
        assertTrue(error.getResponseBody().startsWith("<html><body>Bad gateway"));
        assertTrue(error.getResponseBody().endsWith("..."));
    }

    @Test
    public void statusesUpTo303AreTreatedAsSuccess() throws Exception {
        final TestManager manager = manager();
        http.respond(303, json("{'route_id':'R1'}")).respond(304, "{}");

        assertEquals("R1", manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class).routeId);
        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));
        assertTrue(error.getMessage().startsWith("Invalid status code 304"));
    }

    @Test
    public void malformedJsonBecomesApiException() {
        final TestManager manager = manager();
        final Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respond(200, "not json at all {").respond(200, "{\"not\": \"a list\"}");

        APIException forClass = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));
        APIException forType = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", listOfReplies));

        assertEquals("Unexpected server response for GET https://api.route4me.com" + PATH
                + " (status 200): the body is not valid JSON for Reply", forClass.getMessage());
        assertTrue(forClass.getCause() instanceof JsonParseException);
        assertEquals("not json at all {", forClass.getResponseBody());
        assertEquals(200, forClass.getStatusCode());
        assertTrue(forType.getMessage(), forType.getMessage().startsWith("Unexpected server response for GET "));
        assertTrue(forType.getMessage(), forType.getMessage().endsWith("java.util.ArrayList<"
                + Reply.class.getName() + ">"));
        assertTrue(forType.getCause() instanceof JsonParseException);
    }

    @Test
    public void networkFailureBecomesApiException() {
        final TestManager manager = manager();
        http.failNextRequest(new IOException("connection timed out"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertEquals("GET https://api.route4me.com" + PATH + " failed: connection timed out", error.getMessage());
        assertEquals(0, error.getStatusCode());
        assertNull(error.getResponseBody());
    }

    @Test
    public void networkFailureKeepsTheCause() {
        final TestManager manager = manager();
        IOException cause = new IOException("connection timed out");
        http.failNextRequest(cause);

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertSame(cause, error.getCause());
    }

    @Test
    public void networkFailureMessageHasNoLineBreaks() {
        final TestManager manager = manager();
        http.failNextRequest(new IOException("first line\r\nINFO forged log line"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertFalse(error.getMessage(), error.getMessage().contains("\n") || error.getMessage().contains("\r"));
    }

    @Test
    public void closeFailureDoesNotSwallowTheApiException() {
        final TestManager manager = manager();
        http.failOnClose(new IOException("close failed")).respond(400, json("{'errors':['bad request']}"));

        http.expectApiException(() -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));
    }

    @Test
    public void closeFailureDoesNotDiscardTheResult() throws Exception {
        TestManager manager = manager();
        http.failOnClose(new IOException("close failed")).respond(200, json("{'route_id':'R1'}"));

        Reply reply = manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        assertEquals("R1", reply == null ? null : reply.routeId);
    }

    @Test
    public void responseWithoutBodyIsNotAnError() throws Exception {
        TestManager manager = manager();
        http.respondWithoutBody(204);

        String result = manager.makeRequest(RequestMethod.DELETE, builder(), "", String.class);

        assertTrue("expected no content, got: " + result, result == null || result.isEmpty());
    }

    @Test
    public void textResponseKeepsLineBreaks() throws Exception {
        TestManager manager = manager();
        http.respond(200, "id,name\n1,Depot\n", "text/csv");

        assertEquals("id,name\n1,Depot\n", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
    }

    @Test
    public void textResponseKeepsWindowsLineBreaks() throws Exception {
        TestManager manager = manager();
        http.respond(200, "id,name\r\n1,Depot\r\n", "text/csv");

        assertEquals("id,name\r\n1,Depot\r\n", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
    }

    @Test
    public void responseWithoutBodyParsesToNull() throws Exception {
        TestManager manager = manager();
        Type listOfReplies = new TypeToken<ArrayList<Reply>>() {
        }.getType();
        http.respondWithoutBody(204).respondWithoutBody(204);

        assertNull(manager.makeRequest(RequestMethod.DELETE, builder(), "", Reply.class));
        assertNull(manager.makeRequest(RequestMethod.DELETE, builder(), "", listOfReplies));
    }

    @Test
    public void errorWithoutBodyBecomesApiException() {
        final TestManager manager = manager();
        http.respondWithoutBody(404);

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 404"));
    }

    @Test
    public void bodyWithoutCharsetIsReadAsUtf8() throws Exception {
        TestManager manager = manager();
        http.respondWithBytes(200, "Caf\u00e9 \u00fc \u2713".getBytes(StandardCharsets.UTF_8), "text/plain");

        assertEquals("Caf\u00e9 \u00fc \u2713", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
    }

    @Test
    public void declaredCharsetIsHonoured() throws Exception {
        TestManager manager = manager();
        http.respondWithBytes(200, "Caf\u00e9".getBytes(StandardCharsets.ISO_8859_1), "text/plain; charset=ISO-8859-1")
                .respondWithBytes(200, "Caf\u00e9".getBytes(StandardCharsets.UTF_8), "text/plain; charset=no-such-charset");

        assertEquals("Caf\u00e9", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
        assertEquals("an unknown charset falls back to UTF-8",
                "Caf\u00e9", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
    }

    // ------------------------------------------------------------------ client life cycle

    @Test
    public void everyRequestCarriesTheDefaultTimeouts() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        RequestConfig config = http.request().getRequestConfig();
        assertEquals(Manager.DEFAULT_CONNECT_TIMEOUT_MS, config.getConnectTimeout());
        assertEquals(Manager.DEFAULT_SOCKET_TIMEOUT_MS, config.getSocketTimeout());
        assertEquals(Manager.DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS, config.getConnectionRequestTimeout());
        assertEquals(6050, Manager.DEFAULT_CONNECT_TIMEOUT_MS);
        assertEquals(30000, Manager.DEFAULT_SOCKET_TIMEOUT_MS);
        assertEquals(6050, Manager.DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS);
        assertTrue("redirects are followed unless disabled", config.isRedirectsEnabled());
        assertNull(config.getProxy());
    }

    @Test
    public void changedTimeoutsApplyToLaterRequests() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}");
        manager.setConnectTimeout(1000);
        manager.setSocketTimeout(2000);
        manager.setConnectionRequestTimeout(0);

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        RequestConfig config = http.request().getRequestConfig();
        assertEquals(1000, config.getConnectTimeout());
        assertEquals(2000, config.getSocketTimeout());
        assertEquals(0, config.getConnectionRequestTimeout());
        assertEquals(1000, manager.getConnectTimeout());
        assertEquals(2000, manager.getSocketTimeout());
        assertEquals(0, manager.getConnectionRequestTimeout());
    }

    @Test
    public void negativeTimeoutsAreRejected() {
        final TestManager manager = new TestManager(MockHttp.API_KEY);

        http.expectThrows(IllegalArgumentException.class, () -> manager.setConnectTimeout(-1));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setSocketTimeout(-1));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setConnectionRequestTimeout(-1));
        assertEquals(Manager.DEFAULT_CONNECT_TIMEOUT_MS, manager.getConnectTimeout());
    }

    @Test
    public void disabledRedirectsAreAlsoOffInTheRequestConfig() throws Exception {
        TestManager manager = http.install(new TestManager(MockHttp.API_KEY, true));
        http.respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        assertFalse(http.request().getRequestConfig().isRedirectsEnabled());
    }

    @Test
    public void requestsShareOneClientThatCloseReleases() throws Exception {
        TestManager manager = manager();
        http.respond(200, "{}").respond(200, "{}");

        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);
        manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);
        assertEquals(0, http.closeCount());

        manager.close();
        manager.close();
        assertEquals("close() closes the client once", 1, http.closeCount());
    }

    @Test
    public void closePropagatesTheCloseFailure() throws Exception {
        final TestManager manager = manager();
        http.failOnClose(new IOException("close failed"));

        IOException error = http.expectThrows(IOException.class, manager::close);

        assertEquals("close failed", error.getMessage());
    }

    @Test
    public void defaultClientIsCreatedOnceAndAgainAfterClose() throws Exception {
        TestManager manager = new TestManager(MockHttp.API_KEY);
        try {
            CloseableHttpClient first = manager.httpClient();
            assertSame(first, manager.httpClient());
            manager.close();
            CloseableHttpClient second = manager.httpClient();
            assertNotSame(first, second);
        } finally {
            manager.close();
        }
        manager.close();
    }

    @Test
    public void concurrentFirstRequestsGetTheSameClient() throws Exception {
        final TestManager manager = new TestManager(MockHttp.API_KEY);
        final int threads = 8;
        final CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<CloseableHttpClient>> results = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                results.add(pool.submit(() -> {
                    start.await();
                    return manager.httpClient();
                }));
            }
            start.countDown();
            Set<CloseableHttpClient> clients = Collections.newSetFromMap(new IdentityHashMap<CloseableHttpClient, Boolean>());
            for (Future<CloseableHttpClient> result : results) {
                clients.add(result.get(10, TimeUnit.SECONDS));
            }
            assertEquals(1, clients.size());
        } finally {
            pool.shutdownNow();
            manager.close();
        }
    }

    @Test
    public void theDefaultClientRefusesRedirectsAwayFromRoute4Me() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/", exchange -> {
            String target = exchange.getRequestURI().getPath().endsWith("/plain")
                    ? "http://api.route4me.com/api.v4/route.php" : "https://example.com/collect";
            exchange.getResponseHeaders().add("Location", target);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });
        server.start();
        final TestManager manager = new TestManager(MockHttp.API_KEY);
        try {
            for (String path : Arrays.asList("/foreign", "/plain")) {
                final URIBuilder local = new URIBuilder().setScheme("http")
                        .setHost(server.getAddress().getAddress().getHostAddress())
                        .setPort(server.getAddress().getPort()).setPath(path);

                APIException error = http.expectApiException(
                        () -> manager.makeRequest(RequestMethod.GET, local, "", Reply.class));

                Throwable cause = error.getCause();
                while (cause != null && !(cause instanceof RedirectException)) {
                    cause = cause.getCause();
                }
                assertTrue("expected a RedirectException in " + error, cause != null);
                assertTrue(cause.getMessage(), cause.getMessage().startsWith("Refused to follow a redirect to "));
            }
        } finally {
            server.stop(0);
            manager.close();
        }
    }
}
