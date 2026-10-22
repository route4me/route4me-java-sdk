package com.route4me.sdk.testsupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * An immutable copy of one HTTP request that a manager sent to the mocked client, with
 * assertion helpers for tests.
 *
 * <p>The assertion methods return {@code this}, so they can be chained:
 * <pre>{@code
 * http.request()
 *     .assertEndpoint("GET", "wh.route4me.com", "/modules/api/v5.0/vehicles")
 *     .assertQuery("page", "2")
 *     .assertNoBody();
 * }</pre>
 *
 * <p>{@link #toString()} masks the value of every {@code api_key} query parameter, whatever
 * the key, and {@link MockHttp#API_KEY} anywhere else, so failure messages never print a key.
 */
public final class CapturedRequest {

    /** An {@code api_key} query parameter and its value, which {@link #toString()} masks. */
    private static final Pattern API_KEY_VALUE = Pattern.compile("([?&]api_key=)[^&#]*");

    private final String method;
    private final URI uri;
    private final Map<String, List<String>> headers;
    private final List<NameValuePair> query;
    private final String body;
    private final String entityContentType;
    private final String proxy;
    private final RequestConfig config;

    private CapturedRequest(String method, URI uri, Map<String, List<String>> headers,
                            String body, String entityContentType, RequestConfig config) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.query = Collections.unmodifiableList(URLEncodedUtils.parse(uri, StandardCharsets.UTF_8));
        this.body = body;
        this.entityContentType = entityContentType;
        this.config = config;
        this.proxy = config != null && config.getProxy() != null ? config.getProxy().toURI() : null;
    }

    /**
     * Copies an outgoing request. The entity, if any, is read as text; the SDK only sends
     * repeatable string and form entities, so reading does not affect the request.
     *
     * @param request the request passed to {@code HttpClient.execute}
     * @return the captured copy
     * @throws IOException if the entity cannot be read
     */
    static CapturedRequest from(HttpUriRequest request) throws IOException {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        for (Header header : request.getAllHeaders()) {
            List<String> values = headers.get(header.getName());
            if (values == null) {
                values = new ArrayList<>();
                headers.put(header.getName(), values);
            }
            values.add(header.getValue());
        }
        String body = null;
        String contentType = null;
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                contentType = entity.getContentType() == null ? null : entity.getContentType().getValue();
            }
        }
        RequestConfig config = request instanceof HttpRequestBase ? ((HttpRequestBase) request).getConfig() : null;
        return new CapturedRequest(request.getMethod(), request.getURI(), headers, body, contentType, config);
    }

    // ------------------------------------------------------------------ accessors

    /** @return the HTTP method, such as {@code GET} */
    public String getMethod() {
        return method;
    }

    /** @return the full request URI, including the query string */
    public URI getUri() {
        return uri;
    }

    /** @return the URI scheme, such as {@code https} */
    public String getScheme() {
        return uri.getScheme();
    }

    /** @return the host name */
    public String getHost() {
        return uri.getHost();
    }

    /** @return the decoded path, without the query string */
    public String getPath() {
        return uri.getPath();
    }

    /** @return the decoded query parameters, in order; repeated names appear repeatedly */
    public List<NameValuePair> getQueryParameters() {
        return query;
    }

    /** @return the request headers, by name, in the order they were set */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /** @return the proxy set in the request config, as {@code scheme://host:port}, or {@code null} */
    public String getProxy() {
        return proxy;
    }

    /**
     * @return the per-request configuration (timeouts, proxy, redirects) the SDK set, or
     * {@code null} when it set none
     */
    public RequestConfig getRequestConfig() {
        return config;
    }

    /** @return the body text, or {@code null} when the request has no entity */
    public String getBody() {
        return body;
    }

    /**
     * Returns the first value of a header, ignoring the case of its name.
     *
     * @param name the header name
     * @return the value, or {@code null} when the header is not set
     */
    public String header(String name) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name) && !entry.getValue().isEmpty()) {
                return entry.getValue().get(0);
            }
        }
        return null;
    }

    /**
     * Returns the content type the server would see: the {@code Content-Type} header when the
     * SDK sets one, otherwise the entity's own content type (Apache HttpClient sends that one
     * when no header is set).
     *
     * @return the effective content type, or {@code null} when there is no body
     */
    public String effectiveContentType() {
        String header = header("Content-Type");
        return header != null ? header : entityContentType;
    }

    /**
     * Returns every value of a query parameter.
     *
     * @param name the parameter name
     * @return the values, possibly empty
     */
    public List<String> queryValues(String name) {
        List<String> values = new ArrayList<>();
        for (NameValuePair pair : query) {
            if (pair.getName().equals(name)) {
                values.add(pair.getValue());
            }
        }
        return values;
    }

    /**
     * Returns the first value of a query parameter.
     *
     * @param name the parameter name
     * @return the value, or {@code null} when the parameter is absent
     */
    public String query(String name) {
        List<String> values = queryValues(name);
        return values.isEmpty() ? null : values.get(0);
    }

    /**
     * Parses a form-encoded ({@code application/x-www-form-urlencoded}) body.
     *
     * @return the form fields, in order
     */
    public List<NameValuePair> formFields() {
        assertNotNull("Expected a form body on " + this, body);
        return URLEncodedUtils.parse(body, StandardCharsets.UTF_8);
    }

    /**
     * Returns the first value of a form field.
     *
     * @param name the field name
     * @return the value, or {@code null} when the field is absent
     */
    public String form(String name) {
        for (NameValuePair pair : formFields()) {
            if (pair.getName().equals(name)) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * Parses the body as JSON with Gson.
     *
     * @return the parsed body
     */
    public JsonElement jsonBody() {
        assertNotNull("Expected a JSON body on " + this, body);
        try {
            return JsonParser.parseString(body);
        } catch (JsonParseException e) {
            throw new AssertionError("Body of " + this + " is not valid JSON: " + body, e);
        }
    }

    /**
     * Parses the body as a JSON object.
     *
     * @return the parsed object
     */
    public JsonObject json() {
        JsonElement element = jsonBody();
        assertTrue("Expected a JSON object body on " + this + " but got: " + body, element.isJsonObject());
        return element.getAsJsonObject();
    }

    /** @return the top-level field names of the JSON object body, in order */
    public Set<String> jsonKeys() {
        return new LinkedHashSet<>(json().keySet());
    }

    /**
     * Looks up a value in the JSON body by path. Path segments are separated by dots, and
     * a segment can end in {@code [n]} to index an array, for example
     * {@code addresses[0].address} or {@code [1]} for a top-level array.
     *
     * @param path the path to look up
     * @return the value, or {@code null} when the path does not exist (JSON {@code null}
     * is returned as {@code JsonNull})
     */
    public JsonElement jsonAt(String path) {
        JsonElement current = jsonBody();
        for (String segment : path.split("\\.")) {
            String name = segment;
            List<Integer> indexes = new ArrayList<>();
            int bracket = segment.indexOf('[');
            if (bracket >= 0) {
                name = segment.substring(0, bracket);
                for (String index : segment.substring(bracket + 1).split("\\[")) {
                    indexes.add(Integer.parseInt(index.replace("]", "")));
                }
            }
            if (!name.isEmpty()) {
                if (current == null || !current.isJsonObject() || !current.getAsJsonObject().has(name)) {
                    return null;
                }
                current = current.getAsJsonObject().get(name);
            }
            for (int index : indexes) {
                if (current == null || !current.isJsonArray()) {
                    return null;
                }
                JsonArray array = current.getAsJsonArray();
                if (index >= array.size()) {
                    return null;
                }
                current = array.get(index);
            }
        }
        return current;
    }

    // ------------------------------------------------------------------ assertions

    /**
     * Asserts the method, host and path in one call. The guard has already checked that
     * the scheme is {@code https}.
     *
     * @param expectedMethod the HTTP method
     * @param expectedHost   the host name
     * @param expectedPath   the exact decoded path
     * @return this request
     */
    public CapturedRequest assertEndpoint(String expectedMethod, String expectedHost, String expectedPath) {
        return assertMethod(expectedMethod).assertHost(expectedHost).assertPath(expectedPath);
    }

    /**
     * @param expected the HTTP method
     * @return this request
     */
    public CapturedRequest assertMethod(String expected) {
        assertEquals("HTTP method of " + this, expected, method);
        return this;
    }

    /**
     * @param expected the URI scheme
     * @return this request
     */
    public CapturedRequest assertScheme(String expected) {
        assertEquals("Scheme of " + this, expected, getScheme());
        return this;
    }

    /**
     * @param expected the host name
     * @return this request
     */
    public CapturedRequest assertHost(String expected) {
        assertEquals("Host of " + this, expected, getHost());
        return this;
    }

    /**
     * @param expected the exact decoded path
     * @return this request
     */
    public CapturedRequest assertPath(String expected) {
        assertEquals("Path of " + this, expected, getPath());
        return this;
    }

    /**
     * Asserts that a query parameter is present exactly once with the given value.
     *
     * @param name     the parameter name
     * @param expected the value
     * @return this request
     */
    public CapturedRequest assertQuery(String name, String expected) {
        assertEquals("Values of query parameter '" + name + "' in " + this,
                Collections.singletonList(expected), queryValues(name));
        return this;
    }

    /**
     * Asserts every value of a repeated query parameter, in order.
     *
     * @param name     the parameter name
     * @param expected the values
     * @return this request
     */
    public CapturedRequest assertQueryValues(String name, String... expected) {
        List<String> values = new ArrayList<>();
        Collections.addAll(values, expected);
        assertEquals("Values of query parameter '" + name + "' in " + this, values, queryValues(name));
        return this;
    }

    /**
     * @param name a query parameter that must not be sent
     * @return this request
     */
    public CapturedRequest assertNoQuery(String name) {
        assertTrue("Query parameter '" + name + "' should be absent from " + this, queryValues(name).isEmpty());
        return this;
    }

    /**
     * Asserts that the SDK put the test API key in the {@code api_key} query parameter
     * exactly once, which is where it sends the key outside the V5 API.
     *
     * @return this request
     */
    public CapturedRequest assertApiKeyInQuery() {
        List<String> values = queryValues("api_key");
        assertEquals("Number of api_key query parameters in " + this, 1, values.size());
        assertNull("No Authorization header is expected on " + this, header("Authorization"));
        assertTrue("The api_key query parameter in " + this + " is not the test key",
                MockHttp.API_KEY.equals(values.get(0)));
        return this;
    }

    /**
     * Asserts that the SDK sent the test API key as a bearer token, which is how it sends
     * the key to the V5 API, and not in the {@code api_key} query parameter.
     *
     * @return this request
     */
    public CapturedRequest assertApiKeyAsBearer() {
        assertTrue("The Authorization header of " + this + " is not the bearer test key",
                ("Bearer " + MockHttp.API_KEY).equals(header("Authorization")));
        assertTrue("The API key must not be in the query of " + this, queryValues("api_key").isEmpty());
        return this;
    }

    /**
     * Asserts that the API key is in its expected place for this request: the bearer
     * header on the V5 API, the {@code api_key} query parameter elsewhere.
     *
     * @return this request
     */
    public CapturedRequest assertApiKeySent() {
        return RequestGuard.isV5(this) ? assertApiKeyAsBearer() : assertApiKeyInQuery();
    }

    /**
     * @param name     a header name (case-insensitive)
     * @param expected its value
     * @return this request
     */
    public CapturedRequest assertHeader(String name, String expected) {
        assertEquals("Header '" + name + "' of " + this, expected, header(name));
        return this;
    }

    /**
     * Asserts the content type the server would receive (see {@link #effectiveContentType()}).
     *
     * @param expectedPrefix the expected media type, such as {@code application/json}
     * @return this request
     */
    public CapturedRequest assertContentType(String expectedPrefix) {
        String actual = effectiveContentType();
        assertTrue("Content type of " + this + " should start with " + expectedPrefix + " but was " + actual,
                actual != null && actual.toLowerCase(Locale.ROOT).startsWith(expectedPrefix.toLowerCase(Locale.ROOT)));
        return this;
    }

    /** @return this request, after asserting that it has no body */
    public CapturedRequest assertNoBody() {
        assertNull("Expected no body on " + this + " but got: " + body, body);
        return this;
    }

    /**
     * Asserts that each path exists in the JSON body (see {@link #jsonAt(String)}).
     *
     * @param paths the paths that must exist
     * @return this request
     */
    public CapturedRequest assertJsonHas(String... paths) {
        for (String path : paths) {
            assertNotNull("JSON body of " + this + " has no '" + path + "': " + body, jsonAt(path));
        }
        return this;
    }

    /**
     * Asserts that none of the paths exist in the JSON body.
     *
     * @param paths the paths that must be absent
     * @return this request
     */
    public CapturedRequest assertJsonLacks(String... paths) {
        for (String path : paths) {
            assertNull("JSON body of " + this + " should not have '" + path + "': " + body, jsonAt(path));
        }
        return this;
    }

    /**
     * Asserts a primitive JSON value. Strings, numbers and booleans are compared by their
     * text, so {@code assertJson("limit", 10)} matches {@code "limit": 10}.
     *
     * @param path     the path of the value
     * @param expected the expected value
     * @return this request
     */
    public CapturedRequest assertJson(String path, Object expected) {
        JsonElement element = jsonAt(path);
        assertNotNull("JSON body of " + this + " has no '" + path + "': " + body, element);
        if (expected == null) {
            assertTrue("'" + path + "' should be null in " + body, element.isJsonNull());
            return this;
        }
        assertTrue("'" + path + "' should be a primitive in " + body, element.isJsonPrimitive());
        assertEquals("'" + path + "' in JSON body of " + this, String.valueOf(expected), element.getAsString());
        return this;
    }

    /**
     * Asserts that a form field is present with the given value.
     *
     * @param name     the field name
     * @param expected its value
     * @return this request
     */
    public CapturedRequest assertForm(String name, String expected) {
        assertEquals("Form field '" + name + "' of " + this, expected, form(name));
        return this;
    }

    /**
     * Asserts that the body does not contain a piece of text.
     *
     * @param text the text that must be absent
     * @return this request
     */
    public CapturedRequest assertBodyLacks(String text) {
        assertFalse("Body of " + this + " should not contain '" + text + "': " + body,
                body != null && body.contains(text));
        return this;
    }

    /**
     * Fails unless the condition holds; a small helper for checks the methods above do not cover.
     *
     * @param message   what is being checked
     * @param condition the condition
     * @return this request
     */
    public CapturedRequest check(String message, boolean condition) {
        if (!condition) {
            fail(message + " (" + this + ")");
        }
        return this;
    }

    /** @return {@code METHOD url}, with the {@code api_key} value masked */
    @Override
    public String toString() {
        return API_KEY_VALUE.matcher(method + " " + uri).replaceAll("$1<api_key>")
                .replace(MockHttp.API_KEY, "<api_key>");
    }
}
