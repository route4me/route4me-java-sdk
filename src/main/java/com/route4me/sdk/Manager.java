package com.route4me.sdk;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.http.Route4MeRedirectStrategy;
import com.route4me.sdk.queryconverter.QueryConverter;
import com.route4me.sdk.utils.Sanitizer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;

/**
 * Base class of the SDK managers: it builds the requests, sends them and parses the responses.
 *
 * <p>Each manager owns one HTTP client with a pool of 10 connections, which
 * {@link com.route4me.sdk.services.geocoding.GeocodingManager#bulkGeocoder} grows to its
 * thread count. The client is created on the first request and shared by every later
 * request of the same manager, also when several threads use the manager at once. Reuse
 * a manager instead of creating one per call, and close it when you no longer need it, to
 * release its connections:
 * <pre>{@code
 * try (RoutingManager manager = new RoutingManager(apiKey)) {
 *     DataObject optimization = manager.runOptimization(parameters);
 * }
 * }</pre>
 *
 * <p>The API key goes in an {@code Authorization: Bearer} header on requests to the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/} and {@code v5.1/}), so it stays out of
 * URLs and server logs. Every other request sends it as the {@code api_key} query
 * parameter, which the V4 API requires.
 *
 * <p>Requests to the V5 API also send {@code Accept: application/json, *}{@code /*;q=0.8}, so
 * that its errors come back as JSON rather than as an HTML page. Other requests send no
 * {@code Accept} header.
 *
 * <p>Redirects are followed only over https to the Route4Me API hosts; see
 * {@link Route4MeRedirectStrategy}.
 *
 * <p>Every request carries the connect, socket (read) and connection-request timeouts of
 * the manager; see {@link #setConnectTimeout(int)}, {@link #setSocketTimeout(int)} and
 * {@link #setConnectionRequestTimeout(int)}.
 */
public abstract class Manager implements Closeable {

    /** Default time to open a connection, in milliseconds. */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 6050;
    /** Default time to wait for data on an open connection, in milliseconds. */
    public static final int DEFAULT_SOCKET_TIMEOUT_MS = 30000;
    /** Default time to wait for a free connection from the pool, in milliseconds. */
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS = 6050;

    /** The default number of pooled connections of a manager, per route and in total. */
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    /** The host of the V5 API, which takes the API key as a bearer token. */
    private static final String V5_HOST = "wh.route4me.com";
    /** The path prefixes of the V5 API on {@link #V5_HOST}. */
    private static final String[] V5_PATH_PREFIXES = {"/modules/api/v5.0", "/modules/api/v5.1"};
    private static final String USER_AGENT = "Route4Me Java SDK";
    /**
     * The {@code Accept} header of V5 requests. The V5 API (Laravel) renders an error as an
     * HTML page unless the client asks for JSON first; {@code *}{@code /*} keeps CSV, XML
     * and PDF exports acceptable.
     */
    private static final String V5_ACCEPT = "application/json, */*;q=0.8";

    /** The logger of the managers; protected, and named in lower case, since subclasses use it. */
    @SuppressWarnings("checkstyle:ConstantName") // public API of subclasses: renaming it would break them
    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Manager.class);
    private final String apiKey;
    /** The JSON mapper of the requests and responses; subclasses use it to build bodies. */
    @SuppressWarnings("checkstyle:VisibilityModifier") // protected API of subclasses
    protected Gson gson;
    private HttpHost proxy;
    private boolean disableRedirects;
    private String callBackURL;
    private volatile int connectTimeout = DEFAULT_CONNECT_TIMEOUT_MS;
    private volatile int socketTimeout = DEFAULT_SOCKET_TIMEOUT_MS;
    private volatile int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS;
    /** Guards the creation, replacement and closing of {@link #httpClient}. */
    private final Object clientLock = new Object();
    /** The client of this manager; {@code null} until the first request or after {@link #close()}. */
    private volatile CloseableHttpClient httpClient;
    /**
     * The connection pool of {@link #httpClient} when this class created the client;
     * {@code null} otherwise. Guarded by {@link #clientLock}.
     */
    private PoolingHttpClientConnectionManager connectionPool;
    /** How many connections the pool may hold, per route and in total. Guarded by {@link #clientLock}. */
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    public Manager(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public Manager(String apiKey, boolean disableRedirects) {
        this(apiKey);
        this.disableRedirects = disableRedirects;
    }

    public Manager(String apiKey, boolean disableRedirects, String callBackURL) {
        this(apiKey);
        this.disableRedirects = disableRedirects;
        this.callBackURL = callBackURL;
    }

    public Manager(String apiKey, GsonBuilder builder) {
        this.apiKey = apiKey;
        this.gson = builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                //skip fields without SerializedName
                return fieldAttributes.getAnnotation(SerializedName.class) == null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }

    public Manager(String apiKey, String proxyHost, int proxyPort, String proxySchema) {
        this(apiKey);
        this.proxy = new HttpHost(proxyHost, proxyPort, proxySchema);
    }

    public Manager(String apiKey, String proxyHost, int proxyPort, String proxySchema, boolean disableRedirects) {
        this(apiKey, proxyHost, proxyPort, proxySchema);
        this.disableRedirects = disableRedirects;
    }

    protected static URIBuilder defaultBuilder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("api.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    /**
     * Sets the path of a builder to an endpoint followed by path segments, such as IDs.
     *
     * <p>Each segment is percent-encoded as one segment: a {@code /} in it becomes
     * {@code %2F}, so a value such as {@code ../secret} cannot climb out of the endpoint and
     * send the request, with the API key, to another one.
     *
     * @param builder  the builder to change
     * @param endpoint the endpoint path, such as {@code /modules/api/v5.0/vehicles}
     * @param segments the segments to append after it, converted with
     *                 {@link String#valueOf(Object)}; none leaves the endpoint as it is
     * @return {@code builder}
     * @throws IllegalArgumentException if a segment is {@code null}, empty, {@code .} or
     *                                  {@code ..}
     */
    protected static URIBuilder withPathSegments(URIBuilder builder, String endpoint, Object... segments) {
        if (segments == null || segments.length == 0) {
            return builder.setPath(endpoint);
        }
        List<String> parts = new ArrayList<>();
        for (String part : endpoint.split("/")) {
            if (!part.isEmpty()) {
                parts.add(part);
            }
        }
        for (Object segment : segments) {
            String value = segment == null ? null : String.valueOf(segment);
            if (value == null || value.isEmpty() || ".".equals(value) || "..".equals(value)) {
                throw new IllegalArgumentException("Invalid value for a URL path segment: "
                        + (value == null ? "null" : "'" + Sanitizer.forLog(value, 100) + "'"));
            }
            parts.add(value);
        }
        return builder.setPathSegments(parts);
    }

    /**
     * Sends a request with a form body ({@code application/x-www-form-urlencoded}, UTF-8)
     * and parses the response.
     *
     * @param method  the HTTP method
     * @param builder the request URL; it is not changed
     * @param body    the form fields
     * @param clazz   the response type
     * @param <T>     the response type
     * @return the parsed response, or {@code null} when the response has no content
     * @throws APIException if the request fails, the server answers with an error status or
     *                      the response cannot be parsed
     */
    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, List<NameValuePair> body, Class<T> clazz)
            throws APIException {
        return this.makeRequest(method, builder, new UrlEncodedFormEntity(body, StandardCharsets.UTF_8), clazz, null);
    }

    /**
     * Sends a request with a JSON text body and parses the response.
     *
     * @param method  the HTTP method
     * @param builder the request URL; it is not changed
     * @param body    the JSON text, sent as {@code application/json; charset=UTF-8}; an
     *                empty string sends no body
     * @param clazz   the response type; {@code String.class} returns the body as text
     * @param <T>     the response type
     * @return the parsed response, or {@code null} when the response has no content
     * @throws APIException if the request fails, the server answers with an error status or
     *                      the response cannot be parsed
     */
    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Class<T> clazz)
            throws APIException {
        return this.makeRequest(method, builder, !body.isEmpty() ? jsonEntity(body) : (HttpEntity) null, clazz, null);
    }

    /**
     * Sends a request with a JSON text body and parses the response into a generic type.
     *
     * @param method  the HTTP method
     * @param builder the request URL; it is not changed
     * @param body    the JSON text, sent as {@code application/json; charset=UTF-8}; an
     *                empty string sends no body
     * @param type    the response type, such as a {@code TypeToken} type
     * @param <T>     the response type
     * @return the parsed response, or {@code null} when the response has no content
     * @throws APIException if the request fails, the server answers with an error status or
     *                      the response cannot be parsed
     */
    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Type type) throws APIException {
        return this.makeRequest(method, builder, !body.isEmpty() ? jsonEntity(body) : (HttpEntity) null, type);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz)
            throws APIException {
        return this.makeRequest(method, builder, body, clazz, null);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Type type)
            throws APIException {
        return this.makeRequest(method, builder, body, null, type);
    }

    protected String makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body) throws APIException {
        return this.makeRequest(method, builder, body, String.class, null);
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams,
            Class<T> clazz) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = queryParameters(builder, requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                logger.error("Could not convert {} to query parameters: {}",
                        requestParams.getClass().getSimpleName(), Sanitizer.forLog(ex.getMessage()));
            }
        }
        return this.makeRequest(method, builder, jsonEntity(this.gson.toJson(body)), clazz, null);
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams,
            Type type) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = queryParameters(builder, requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                logger.error("Could not convert {} to query parameters: {}",
                        requestParams.getClass().getSimpleName(), Sanitizer.forLog(ex.getMessage()));
            }
        }
        return this.makeRequest(method, builder, jsonEntity(this.gson.toJson(body)), null, type);
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Class<T> clazz)
            throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = queryParameters(builder, requestParams);
                if (method == RequestMethod.GET) {
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.getClass().getSimpleName()
                        + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, jsonEntity(this.gson.toJson(requestParams)), clazz, null);
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, clazz, null);
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Type type)
            throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = queryParameters(builder, requestParams);
                if (method == RequestMethod.GET) {
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.getClass().getSimpleName()
                        + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, jsonEntity(this.gson.toJson(requestParams)), null, type);
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, null, type);
    }

    /**
     * Converts the {@link com.route4me.sdk.queryconverter.QueryParameter} fields of a request
     * object to query parameters for {@code builder}. The V5 API (PHP) reads lists and maps
     * only from bracketed keys, so V5 requests get
     * {@link QueryConverter#convertObjectToBracketedParameters(Object)} ({@code ids[]=1},
     * {@code filters[status]=1}); every other request keeps
     * {@link QueryConverter#convertObjectToParameters(Object)}.
     *
     * @param builder       the request URL
     * @param requestParams the object with the query fields
     * @return the parameters
     * @throws IllegalAccessException if a field cannot be read
     */
    private static List<NameValuePair> queryParameters(URIBuilder builder, Object requestParams)
            throws IllegalAccessException {
        if (sendsKeyAsBearerToken(builder.getHost(), builder.getPath())) {
            return QueryConverter.convertObjectToBracketedParameters(requestParams);
        }
        return QueryConverter.convertObjectToParameters(requestParams);
    }

    /**
     * Wraps a JSON text as a request body. Its {@code Content-Type} is
     * {@code application/json; charset=UTF-8}.
     *
     * @param json the JSON text
     * @return the entity
     */
    private static HttpEntity jsonEntity(String json) {
        return new StringEntity(json, ContentType.APPLICATION_JSON);
    }

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type)
            throws APIException {
        HttpRequestBase hrb;
        boolean bearer;
        try {
            // Work on a copy: the caller's builder stays unchanged, so it can be sent again
            // (for example on a retry) without repeating these parameters.
            URIBuilder target = new URIBuilder(builder.build());
            // Redirects Management
            if (this.disableRedirects) {
                target.addParameter("redirect", "0");
            }
            if (this.callBackURL != null) {
                target.addParameter("optimized_callback_url", this.callBackURL);
            }
            bearer = sendsKeyAsBearerToken(target.getHost(), target.getPath());
            if (this.apiKey != null && !bearer) {
                target.addParameter("api_key", this.apiKey);
            }
            hrb = method.create(target.build());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        if (this.apiKey != null && bearer) {
            hrb.setHeader("Authorization", "Bearer " + this.apiKey);
        }
        if (bearer) {
            hrb.setHeader("Accept", V5_ACCEPT);
        }
        // Adding User-Agent
        hrb.setHeader("User-Agent", USER_AGENT);

        // Timeouts, proxy and redirect handling of this manager.
        hrb.setConfig(this.requestConfig());
        //if there's any entity to be attached to the body and request supports it, do it.
        if (body != null && hrb instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) hrb).setEntity(body);
        } else if (body != null) {
            throw new RuntimeException("Method does not support body!");
        }

        String safeUrl = this.redact(Sanitizer.redactUrl(hrb.getURI().toString()));
        try (CloseableHttpResponse resp = this.httpClient().execute(hrb)) {
            int status = resp.getStatusLine().getStatusCode();
            String respString = readBody(resp.getEntity());
            if (status < 200 || status > 303) {
                throw APIException.forErrorStatus(method.name(), safeUrl, status,
                        respString.isEmpty() ? null : this.redact(respString));
            }
            //deserialize json into an object
            try {
                if (clazz != null && clazz != String.class) {
                    return this.gson.fromJson(respString, clazz);
                } else if (type != null) {
                    return this.gson.fromJson(respString, type);
                }
            } catch (JsonParseException | IllegalStateException ex) {
                String expected = clazz != null ? clazz.getSimpleName() : type.getTypeName();
                throw APIException.forUnparsableResponse(method.name(), safeUrl, status, this.redact(respString),
                        expected, ex);
            }
            if (clazz == String.class) {
                return (T) respString;
            }
            return null;
        } catch (IOException ex) {
            throw APIException.forIOException(method.name(), safeUrl, ex);
        }
    }

    /**
     * Tells whether a request sends the API key as a bearer token: requests to the V5 API
     * ({@code https://wh.route4me.com/modules/api/v5.0/...} and {@code .../v5.1/...}) send
     * it in an {@code Authorization: Bearer} header; all other requests send it as the
     * {@code api_key} query parameter, which the V4 API requires.
     *
     * @param host the request host
     * @param path the decoded request path
     * @return {@code true} for a V5 request
     */
    static boolean sendsKeyAsBearerToken(String host, String path) {
        if (host == null || path == null || !V5_HOST.equalsIgnoreCase(host)) {
            return false;
        }
        for (String prefix : V5_PATH_PREFIXES) {
            if (path.startsWith(prefix + "/") || path.equals(prefix)) {
                return true;
            }
        }
        return false;
    }

    /** Removes the API key of this manager from a text, for exceptions and logs. */
    private String redact(String text) {
        return Sanitizer.redact(text, this.apiKey);
    }

    /**
     * Reads a whole response body as text, line breaks included.
     *
     * <p>The body is decoded with the charset its {@code Content-Type} declares, and as
     * UTF-8 when it declares none (never with the platform default charset).
     *
     * @param entity the response entity, or {@code null} when the response has none (such
     *               as a {@code 204 No Content})
     * @return the body, or an empty string when there is no entity or no content
     * @throws IOException if the body cannot be read
     */
    private static String readBody(HttpEntity entity) throws IOException {
        if (entity == null) {
            return "";
        }
        Charset charset = StandardCharsets.UTF_8;
        try {
            ContentType contentType = ContentType.get(entity);
            if (contentType != null && contentType.getCharset() != null) {
                charset = contentType.getCharset();
            }
        } catch (ParseException | UnsupportedCharsetException ex) {
            // An unreadable or unknown charset: keep UTF-8.
        }
        try (InputStream in = entity.getContent()) {
            if (in == null) {
                return "";
            }
            Reader reader = new InputStreamReader(in, charset);
            StringBuilder text = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                text.append(buffer, 0, read);
            }
            return text.toString();
        }
    }

    /**
     * Builds the request configuration of this manager: its timeouts, its proxy, and
     * whether redirects are followed.
     *
     * @return a new configuration with the current settings
     */
    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .setConnectionRequestTimeout(this.connectionRequestTimeout)
                .setProxy(this.proxy)
                .setRedirectsEnabled(!this.disableRedirects)
                .build();
    }

    /**
     * Returns the HTTP client of this manager, creating it on first use. Concurrent callers
     * get the same client.
     *
     * <p>Package-private so that the unit tests can check the client's life cycle.
     *
     * @return the client
     */
    CloseableHttpClient httpClient() {
        CloseableHttpClient client = this.httpClient;
        if (client == null) {
            synchronized (this.clientLock) {
                client = this.httpClient;
                if (client == null) {
                    client = this.buildHttpClient();
                    this.httpClient = client;
                }
            }
        }
        return client;
    }

    /** Creates the pooled client that this manager uses for all its requests; called under {@link #clientLock}. */
    private CloseableHttpClient buildHttpClient() {
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(this.maxConnections);
        pool.setDefaultMaxPerRoute(this.maxConnections);
        this.connectionPool = pool;
        return HttpClients.custom()
                .setConnectionManager(pool)
                .setDefaultRequestConfig(this.requestConfig())
                .setRedirectStrategy(new Route4MeRedirectStrategy())
                .setUserAgent(USER_AGENT)
                .build();
    }

    /**
     * Closes the HTTP client of this manager and releases its pooled connections.
     *
     * <p>Closing twice is harmless. A manager that sends another request after
     * {@code close()} creates a new client, which must be closed again.
     *
     * @throws IOException if the client fails to close
     */
    @Override
    public void close() throws IOException {
        CloseableHttpClient client;
        synchronized (this.clientLock) {
            client = this.httpClient;
            this.httpClient = null;
            this.connectionPool = null;
        }
        if (client != null) {
            client.close();
        }
    }

    /**
     * Test-only hook: makes every request of this manager go through {@code client}
     * instead of the client this class creates.
     *
     * <p>This method is package-private and is not part of the public API. It exists so
     * that offline unit tests can capture the outgoing requests and return canned
     * responses. Production code never calls it. The client is used like the default one:
     * it is kept for all later requests and closed by {@link #close()}. A client this
     * manager created earlier is not closed.
     *
     * @param client the client for all later requests, or {@code null} to go back to a
     *               client created by this manager
     */
    void setHttpClient(CloseableHttpClient client) {
        synchronized (this.clientLock) {
            this.httpClient = client;
            this.connectionPool = null;
        }
    }

    /**
     * Makes the connection pool of this manager hold at least {@code connections}
     * connections, per route and in total, so that as many threads can send requests at
     * once without waiting for a free connection (see
     * {@link #setConnectionRequestTimeout(int)}). The pool never shrinks: a smaller number
     * changes nothing. The new size applies to the current pool and to any pool created
     * after {@link #close()}.
     *
     * <p>{@link com.route4me.sdk.services.geocoding.GeocodingManager#bulkGeocoder} calls it
     * with its thread count.
     *
     * @param connections the number of requests that may run at once
     */
    protected void ensureMaxConnections(int connections) {
        synchronized (this.clientLock) {
            if (connections <= this.maxConnections) {
                return;
            }
            this.maxConnections = connections;
            if (this.connectionPool != null) {
                this.connectionPool.setMaxTotal(connections);
                this.connectionPool.setDefaultMaxPerRoute(connections);
            }
        }
    }

    /**
     * Returns how many connections the pool of this manager may hold, per route and in
     * total. Package-private, for the unit tests.
     *
     * @return the pool size; {@code 10} unless {@link #ensureMaxConnections(int)} raised it
     */
    int maxConnections() {
        synchronized (this.clientLock) {
            return this.maxConnections;
        }
    }

    /**
     * Returns the connection pool of the client this class created. Package-private, for
     * the unit tests.
     *
     * @return the pool, or {@code null} before the first request, after {@link #close()} or
     *         when a test installed its own client
     */
    PoolingHttpClientConnectionManager connectionPool() {
        synchronized (this.clientLock) {
            return this.connectionPool;
        }
    }

    /**
     * @return the connect timeout in milliseconds; {@link #DEFAULT_CONNECT_TIMEOUT_MS} unless changed
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Sets how long a request may take to open a connection. It applies to the requests
     * sent after the call.
     *
     * @param millis the timeout in milliseconds; {@code 0} waits forever
     * @throws IllegalArgumentException if {@code millis} is negative
     */
    public void setConnectTimeout(int millis) {
        this.connectTimeout = checkTimeout(millis);
    }

    /**
     * @return the socket (read) timeout in milliseconds; {@link #DEFAULT_SOCKET_TIMEOUT_MS} unless changed
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Sets how long a request may wait for data on an open connection. It applies to the
     * requests sent after the call.
     *
     * @param millis the timeout in milliseconds; {@code 0} waits forever
     * @throws IllegalArgumentException if {@code millis} is negative
     */
    public void setSocketTimeout(int millis) {
        this.socketTimeout = checkTimeout(millis);
    }

    /**
     * @return the connection-request timeout in milliseconds;
     * {@link #DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS} unless changed
     */
    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    /**
     * Sets how long a request may wait for a free connection from the pool of this
     * manager. It applies to the requests sent after the call.
     *
     * @param millis the timeout in milliseconds; {@code 0} waits forever
     * @throws IllegalArgumentException if {@code millis} is negative
     */
    public void setConnectionRequestTimeout(int millis) {
        this.connectionRequestTimeout = checkTimeout(millis);
    }

    /** Rejects negative timeouts, which Apache HttpClient would read as "system default". */
    private static int checkTimeout(int millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("A timeout cannot be negative: " + millis);
        }
        return millis;
    }

    /**
     * @return the callBackURL
     */
    public String getCallBackURL() {
        return callBackURL;
    }

    /**
     * @param callBackURL the callBackURL to set
     */
    public void setCallBackURL(String callBackURL) {
        this.callBackURL = callBackURL;
    }

}
