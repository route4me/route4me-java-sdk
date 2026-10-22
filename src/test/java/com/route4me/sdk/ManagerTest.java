package com.route4me.sdk;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.testsupport.MockHttp;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
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
        assertEquals("the client is closed after each request", 1, http.closeCount());
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
        assertEquals(1, http.closeCount());
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

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 400"));
        assertEquals(1, http.closeCount());
    }

    @Test
    public void serverErrorBecomesApiException() {
        final TestManager manager = manager();
        http.respond(503, json("{'errors':['unavailable']}"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.POST, builder(), "{}", Reply.class));

        assertTrue(error.getMessage(), error.getMessage().startsWith("Invalid status code 503"));
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

        assertTrue(forClass.getMessage(), forClass.getMessage().contains("JsonSyntaxException"));
        assertTrue(forType.getMessage(), forType.getMessage().startsWith("Unexpected Server response"));
    }

    @Test
    public void networkFailureBecomesApiException() {
        final TestManager manager = manager();
        http.failNextRequest(new IOException("connection timed out"));

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertEquals("connection timed out", error.getMessage());
    }

    @Ignore("bug (unfiled): APIException(Exception) keeps the message but drops the cause")
    @Test
    public void networkFailureKeepsTheCause() {
        final TestManager manager = manager();
        IOException cause = new IOException("connection timed out");
        http.failNextRequest(cause);

        APIException error = http.expectApiException(
                () -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));

        assertSame(cause, error.getCause());
    }

    @Ignore("bug #37: return inside finally swallows the APIException when close() fails")
    @Test
    public void closeFailureDoesNotSwallowTheApiException() {
        final TestManager manager = manager();
        http.failOnClose(new IOException("close failed")).respond(400, json("{'errors':['bad request']}"));

        http.expectApiException(() -> manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class));
    }

    @Ignore("bug #37: return inside finally discards the parsed result when close() fails")
    @Test
    public void closeFailureDoesNotDiscardTheResult() throws Exception {
        TestManager manager = manager();
        http.failOnClose(new IOException("close failed")).respond(200, json("{'route_id':'R1'}"));

        Reply reply = manager.makeRequest(RequestMethod.GET, builder(), "", Reply.class);

        assertEquals("R1", reply == null ? null : reply.routeId);
    }

    @Ignore("bug (unfiled): a response without a body (such as 204) throws NullPointerException")
    @Test
    public void responseWithoutBodyIsNotAnError() throws Exception {
        TestManager manager = manager();
        http.respondWithoutBody(204);

        String result = manager.makeRequest(RequestMethod.DELETE, builder(), "", String.class);

        assertTrue("expected no content, got: " + result, result == null || result.isEmpty());
    }

    @Ignore("bug (unfiled): response bodies are read line by line and the line breaks are dropped")
    @Test
    public void textResponseKeepsLineBreaks() throws Exception {
        TestManager manager = manager();
        http.respond(200, "id,name\n1,Depot\n", "text/csv");

        assertEquals("id,name\n1,Depot\n", manager.makeRequest(RequestMethod.GET, builder(), "", String.class));
    }
}
