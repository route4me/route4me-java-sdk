package com.route4me.sdk;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;

/**
 * Base class of the SDK managers: it builds the requests, sends them and parses the responses.
 *
 * <p>Each manager owns one HTTP client with a small connection pool. The client is created
 * on the first request and shared by every later request of the same manager, also when
 * several threads use the manager at once. Reuse a manager instead of creating one per
 * call, and close it when you no longer need it, to release its connections:
 * <pre>{@code
 * try (RoutingManager manager = new RoutingManager(apiKey)) {
 *     DataObject optimization = manager.runOptimization(parameters);
 * }
 * }</pre>
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

    private static final int MAX_CONNECTIONS = 10;
    private static final String USER_AGENT = "Route4Me Java SDK";

    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Manager.class);
    private final String apiKey;
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

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, List<NameValuePair> body, Class<T> clazz) throws APIException {
        try {
            return this.makeRequest(method, builder, new UrlEncodedFormEntity(body), clazz, null);
        } catch (UnsupportedEncodingException ex) {
            throw new APIException(ex);
        }
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Class<T> clazz) throws APIException {
        return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body, "UTF-8") : (HttpEntity) null, clazz, null);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Type type) throws APIException {
        return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body, "UTF-8") : (HttpEntity) null, type);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz) throws APIException {
        return this.makeRequest(method, builder, body, clazz, null);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Type type) throws APIException {
        return this.makeRequest(method, builder, body, null, type);
    }

    protected String makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body) throws APIException {
        return this.makeRequest(method, builder, body, String.class, null);
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = QueryConverter.convertObjectToParameters(requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                logger.error(ex);
            }
        }
        return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(body), "UTF-8"), clazz, null, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams, Type type) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = QueryConverter.convertObjectToParameters(requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                logger.error(ex);
            }
        }
        return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(body), "UTF-8"), null, type, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = QueryConverter.convertObjectToParameters(requestParams);
                if (method == RequestMethod.GET) {
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.toString() + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(requestParams), "UTF-8"), clazz, null, "application/json");
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, clazz, null, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Type type) throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = QueryConverter.convertObjectToParameters(requestParams);
                if (method == RequestMethod.GET) {
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.toString() + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(requestParams), "UTF-8"), null, type, "application/json");
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, null, type, "application/json");
    }

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type) throws APIException {
        return this.makeRequest(method, builder, body, clazz, type, null);
    }

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type, String requestContentType) throws APIException {
        HttpRequestBase hrb;
        try {
            // Redirects Management
            if (this.disableRedirects) {
                builder.addParameter("redirect", "0");
            }
            if (this.callBackURL != null) {
                builder.addParameter("optimized_callback_url", this.callBackURL);
            }
            if (this.apiKey != null) {
                builder.addParameter("api_key", this.apiKey);
            }
            hrb = method.create(builder.build());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        // Adding User-Agent
        hrb.setHeader("User-Agent", USER_AGENT);

        if (requestContentType != null) {
            hrb.setHeader("Content-type", requestContentType);
        }
        // Timeouts, proxy and redirect handling of this manager.
        hrb.setConfig(this.requestConfig());
        //if there's any entity to be attached to the body and request supports it, do it.
        if (body != null && hrb instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) hrb).setEntity(body);
        } else if (body != null) {
            throw new RuntimeException("Method does not support body!");
        }

        //try with resources to close streams
        try (CloseableHttpResponse resp = this.httpClient().execute(hrb); InputStream is = resp.getEntity().getContent()) {
            //response should always be present
            if (is == null) {
                throw new APIException("Response body is null.");
            }
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
                if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() > 303) {
                    try {
                        StringBuilder respString = responseContentParser(br);
                        throw new APIException(String.format("Invalid status code %d, errors: %s", resp.getStatusLine().getStatusCode(), respString.toString()));
                    } catch (IOException e) {
                        throw new APIException(String.format("Invalid status code %d and no error present.", resp.getStatusLine().getStatusCode()));
                    }
                }
                //deserialize json into an object
                if (clazz != null && clazz != String.class) {
                    StringBuilder respString = responseContentParser(br);
                    try {
                        return this.gson.fromJson(respString.toString(), clazz);
                    } catch (JsonSyntaxException ex) {
                        throw new APIException(ex.toString());
                    }
                } else if (type != null) {
                    StringBuilder respString = responseContentParser(br);
                    try {
                        return this.gson.fromJson(respString.toString(), type);
                    } catch (IllegalStateException ex) {
                        logger.error(ex);
                        throw new APIException(String.format("Error parsing JSON response from server. Illegal state error: %s", respString.toString()));
                    } catch (JsonSyntaxException ex) {
                        logger.error(String.format("Error parsing JSON response from server. Unexpected syntax: %s", respString.toString()));
                        throw new APIException(String.format("Unexpected Server response: %s", respString.toString()));
                    }
                }
                if (clazz == String.class) {
                    StringBuilder respString = responseContentParser(br);
                    return (T) respString.toString();
                }
                return null;
            }
        } catch (IOException ex) {
            throw new APIException(ex);
        }
    }

    private StringBuilder responseContentParser(BufferedReader br) throws IOException {
        StringBuilder respString = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            respString.append(line);
        }
        return respString;
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

    /** Creates the pooled client that this manager uses for all its requests. */
    private CloseableHttpClient buildHttpClient() {
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(MAX_CONNECTIONS);
        pool.setDefaultMaxPerRoute(MAX_CONNECTIONS);
        return HttpClients.custom()
                .setConnectionManager(pool)
                .setDefaultRequestConfig(this.requestConfig())
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
