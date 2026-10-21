package com.route4me.sdk.testsupport;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checks every request a manager sends during a unit test, and every exception it
 * produces, against the endpoint registry and the API-key rules.
 *
 * <p>A request passes when:
 * <ul>
 *   <li>its scheme is {@code https};</li>
 *   <li>its (host, path) is on the registry's {@code deprecated} list, which is allowed but
 *       recorded for the {@link DeprecatedEndpointReport}; or its host is in
 *       {@code allowed_hosts} and its (method, host, path) matches an {@code endpoints}
 *       entry;</li>
 *   <li>the API key appears nowhere except the {@code api_key} query parameter, which is
 *       where the SDK puts it today: not in a header, the path, another query parameter,
 *       or the body.</li>
 * </ul>
 * Messages never print the key itself.
 */
public final class RequestGuard {

    private static final String API_KEY_PARAMETER = "api_key";

    private final EndpointRegistry registry;
    private final String apiKey;
    private final DeprecatedEndpointReport.Hits deprecatedHits;

    /**
     * Creates a guard that records deprecated-endpoint hits in the run-wide report.
     *
     * @param registry the endpoint registry
     * @param apiKey   the key the managers under test were created with, or {@code null}
     */
    public RequestGuard(EndpointRegistry registry, String apiKey) {
        this(registry, apiKey, DeprecatedEndpointReport.global());
    }

    /**
     * @param registry       the endpoint registry
     * @param apiKey         the key the managers under test were created with, or {@code null}
     * @param deprecatedHits where to record requests to deprecated endpoints
     */
    public RequestGuard(EndpointRegistry registry, String apiKey, DeprecatedEndpointReport.Hits deprecatedHits) {
        this.registry = registry;
        this.apiKey = apiKey;
        this.deprecatedHits = deprecatedHits;
    }

    /**
     * Checks one request.
     *
     * @param request  the captured request
     * @param testName the running test, for the deprecated-endpoint report
     * @return the problems found; empty when the request is fine
     */
    public List<String> check(CapturedRequest request, String testName) {
        List<String> problems = new ArrayList<>();
        String host = request.getHost();
        String path = request.getPath();

        if (!"https".equals(request.getScheme())) {
            problems.add(request + ": scheme must be https, not " + request.getScheme());
        }
        EndpointRegistry.Entry deprecated = registry.findDeprecated(host, path);
        if (deprecated != null) {
            deprecatedHits.record(request.getMethod(), host, path, deprecated.getReason(), testName);
        } else if (!registry.isAllowedHost(host)) {
            problems.add(request + ": host '" + host + "' is not in allowed_hosts " + registry.getAllowedHosts()
                    + " of endpoints.json");
        } else if (registry.findEndpoint(request.getMethod(), host, path) == null) {
            List<String> known = registry.knownMethods(host, path);
            problems.add(request.getMethod() + " https://" + host + path + " is not a known endpoint in endpoints.json"
                    + (known.isEmpty() ? " (no entry for this path)" : " (known methods: " + known + ")"));
        }
        problems.addAll(keyLeaks(request));
        return problems;
    }

    /**
     * Finds every place where the API key appears in a request, other than the
     * {@code api_key} query parameter.
     *
     * @param request the captured request
     * @return one message per leak; empty when there is none
     */
    public List<String> keyLeaks(CapturedRequest request) {
        if (!hasKey()) {
            return Collections.emptyList();
        }
        List<String> leaks = new ArrayList<>();
        for (Map.Entry<String, List<String>> header : request.getHeaders().entrySet()) {
            for (String value : header.getValue()) {
                if (contains(header.getKey()) || contains(value)) {
                    leaks.add(request + ": the API key appears in header '" + header.getKey() + "'");
                }
            }
        }
        if (contains(request.getPath())) {
            leaks.add(request + ": the API key appears in the URL path");
        }
        for (NameValuePair pair : request.getQueryParameters()) {
            boolean expectedPlace = API_KEY_PARAMETER.equals(pair.getName());
            if (contains(pair.getName()) || (!expectedPlace && contains(pair.getValue()))) {
                leaks.add(request + ": the API key appears in query parameter '" + pair.getName() + "'");
            }
        }
        if (contains(request.getUri().getRawFragment())) {
            leaks.add(request + ": the API key appears in the URL fragment");
        }
        if (contains(request.getBody())) {
            leaks.add(request + ": the API key appears in the request body");
        }
        return leaks;
    }

    /**
     * Finds the API key in the message or {@code toString()} of a throwable, its causes
     * and its suppressed exceptions.
     *
     * @param throwable the throwable to inspect, or {@code null}
     * @return one message per throwable that contains the key; empty when there is none
     */
    public List<String> keyLeaks(Throwable throwable) {
        if (!hasKey() || throwable == null) {
            return Collections.emptyList();
        }
        List<String> leaks = new ArrayList<>();
        collectLeaks(throwable, leaks, Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>()));
        return leaks;
    }

    private void collectLeaks(Throwable throwable, List<String> leaks, Set<Throwable> seen) {
        if (throwable == null || !seen.add(throwable)) {
            return;
        }
        if (contains(throwable.getMessage()) || contains(throwable.toString())) {
            leaks.add("the API key appears in the message of " + throwable.getClass().getName());
        }
        collectLeaks(throwable.getCause(), leaks, seen);
        for (Throwable suppressed : throwable.getSuppressed()) {
            collectLeaks(suppressed, leaks, seen);
        }
    }

    /**
     * @param text text produced during a test, such as a log message
     * @return whether the API key appears in it
     */
    public boolean containsKey(String text) {
        return hasKey() && contains(text);
    }

    private boolean hasKey() {
        return apiKey != null && !apiKey.isEmpty();
    }

    private boolean contains(String text) {
        return text != null && text.contains(apiKey);
    }
}
