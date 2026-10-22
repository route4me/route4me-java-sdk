package com.route4me.sdk.testsupport;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The registry of Route4Me endpoints that SDK requests may go to, loaded from the
 * {@code endpoints.json} test resource.
 *
 * <p>The file is generated outside the repo from the OpenAPI specs and a liveness probe.
 * It has three parts:
 * <ul>
 *   <li>{@code allowed_hosts}: the only hosts a request may go to;</li>
 *   <li>{@code endpoints}: known (method, host, path) entries. The method {@code "*"}
 *       matches any method, and the path segment {@code {}} matches exactly one segment;</li>
 *   <li>{@code deprecated}: (host, path) entries the SDK still calls although they are
 *       dead. Requests to them are allowed but reported.</li>
 * </ul>
 */
public final class EndpointRegistry {

    private static final String RESOURCE = "/endpoints.json";
    private static final String ANY_METHOD = "*";
    private static final String ANY_SEGMENT = "{}";

    private static EndpointRegistry instance;

    @SerializedName("allowed_hosts")
    private List<String> allowedHosts = new ArrayList<>();
    @SerializedName("endpoints")
    private List<Entry> endpoints = new ArrayList<>();
    @SerializedName("deprecated")
    private List<Entry> deprecated = new ArrayList<>();

    /** One registry entry; {@code method} is absent on deprecated entries. */
    public static final class Entry {
        @SerializedName("method")
        private String method;
        @SerializedName("host")
        private String host;
        @SerializedName("path")
        private String path;
        @SerializedName("reason")
        private String reason;

        /** @return the HTTP method, {@code "*"} for any, or {@code null} on deprecated entries */
        public String getMethod() {
            return method;
        }

        /** @return the host name */
        public String getHost() {
            return host;
        }

        /** @return the path pattern, where {@code {}} stands for one segment */
        public String getPath() {
            return path;
        }

        /** @return why a deprecated entry is deprecated, or {@code null} */
        public String getReason() {
            return reason;
        }

        @Override
        public String toString() {
            return (method == null ? "" : method + " ") + host + path;
        }
    }

    /** Created by Gson from {@code endpoints.json}. */
    private EndpointRegistry() {
    }

    /**
     * Returns the registry loaded from the classpath. It is loaded once and then cached.
     *
     * @return the shared registry
     */
    public static synchronized EndpointRegistry load() {
        if (instance == null) {
            instance = read(RESOURCE);
        }
        return instance;
    }

    /** Parses the registry from a classpath resource. */
    private static EndpointRegistry read(String resource) {
        try (InputStream in = EndpointRegistry.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Test resource " + resource + " is missing");
            }
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                EndpointRegistry registry = new Gson().fromJson(reader, EndpointRegistry.class);
                if (registry == null || registry.allowedHosts.isEmpty() || registry.endpoints.isEmpty()) {
                    throw new IllegalStateException("Test resource " + resource + " has no endpoints");
                }
                return registry;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read test resource " + resource, e);
        }
    }

    /** @return the hosts requests may go to */
    public List<String> getAllowedHosts() {
        return Collections.unmodifiableList(allowedHosts);
    }

    /**
     * @param host a host name
     * @return whether {@code host} is in {@code allowed_hosts}
     */
    public boolean isAllowedHost(String host) {
        return host != null && allowedHosts.contains(host.toLowerCase(Locale.ROOT));
    }

    /**
     * Finds the endpoint entry that matches a request.
     *
     * @param method the HTTP method, such as {@code GET}
     * @param host   the host name
     * @param path   the decoded request path, without the query string
     * @return the matching entry, or {@code null} when the request is not a known endpoint
     */
    public Entry findEndpoint(String method, String host, String path) {
        for (Entry entry : endpoints) {
            if (entry.host.equalsIgnoreCase(host)
                    && (ANY_METHOD.equals(entry.method) || entry.method.equalsIgnoreCase(method))
                    && pathMatches(entry.path, path)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Finds the deprecated entry that matches a request. Deprecated entries match any method.
     *
     * @param host the host name
     * @param path the decoded request path, without the query string
     * @return the matching entry, or {@code null} when the request is not deprecated
     */
    public Entry findDeprecated(String host, String path) {
        for (Entry entry : deprecated) {
            if (entry.host.equalsIgnoreCase(host) && pathMatches(entry.path, path)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Lists the methods the registry knows for a host and path, to make guard messages
     * more helpful (for example "known methods: GET, PATCH").
     *
     * @param host the host name
     * @param path the decoded request path
     * @return the known methods, possibly empty
     */
    public List<String> knownMethods(String host, String path) {
        List<String> methods = new ArrayList<>();
        for (Entry entry : endpoints) {
            if (entry.host.equalsIgnoreCase(host) && pathMatches(entry.path, path) && !methods.contains(entry.method)) {
                methods.add(entry.method);
            }
        }
        return methods;
    }

    /**
     * Matches a path against a registry pattern. Segments must be equal, except that a
     * {@code {}} segment in the pattern matches exactly one non-empty segment.
     *
     * @param pattern the registry path, such as {@code /modules/api/v5.0/vehicles/{}}
     * @param path    the request path
     * @return whether the path matches
     */
    static boolean pathMatches(String pattern, String path) {
        if (pattern == null || path == null) {
            return false;
        }
        String[] expected = pattern.split("/", -1);
        String[] actual = path.split("/", -1);
        if (expected.length != actual.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (ANY_SEGMENT.equals(expected[i])) {
                if (actual[i].isEmpty()) {
                    return false;
                }
            } else if (!expected[i].equals(actual[i])) {
                return false;
            }
        }
        return true;
    }
}
