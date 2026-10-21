package com.route4me.sdk.testsupport;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Collects the requests that tests sent to deprecated endpoints, and prints one summary
 * when the test run finishes.
 *
 * <p>{@link RequestGuard} allows requests to the registry's {@code deprecated} entries
 * (for example the retired {@code telematics.route4me.com} host), but every hit is
 * recorded here. Surefire registers this class as a JUnit {@link RunListener} (see the
 * {@code listener} property in {@code pom.xml}), so the summary appears once at the end of
 * {@code mvn test}. When tests run without that listener, {@link #summary()} still
 * returns the collected hits.
 */
public class DeprecatedEndpointReport extends RunListener {

    /** Key: "METHOD https://host/path"; value: the tests that sent it. */
    private static final ConcurrentMap<String, Set<String>> HITS = new ConcurrentSkipListMap<>();
    private static final ConcurrentMap<String, String> REASONS = new ConcurrentSkipListMap<>();

    /**
     * Records one request to a deprecated endpoint.
     *
     * @param method   the HTTP method
     * @param host     the host
     * @param path     the path
     * @param reason   why the registry lists it as deprecated
     * @param testName the test that sent it
     */
    static void record(String method, String host, String path, String reason, String testName) {
        String key = method + " https://" + host + path;
        HITS.computeIfAbsent(key, k -> new ConcurrentSkipListSet<>()).add(testName == null ? "(unknown test)" : testName);
        if (reason != null) {
            REASONS.put(key, reason);
        }
    }

    /**
     * Builds the summary text.
     *
     * @return the summary lines; a single line saying "none" when nothing was hit
     */
    public static List<String> summary() {
        List<String> lines = new ArrayList<>();
        if (HITS.isEmpty()) {
            lines.add("Deprecated endpoints hit by unit tests: none");
            return lines;
        }
        lines.add("Deprecated endpoints hit by unit tests (" + HITS.size() + "):");
        for (Map.Entry<String, Set<String>> hit : HITS.entrySet()) {
            String reason = REASONS.get(hit.getKey());
            lines.add("  " + hit.getKey() + (reason == null ? "" : "  [" + reason + "]"));
            for (String test : hit.getValue()) {
                lines.add("      " + test);
            }
        }
        return lines;
    }

    /** Prints the summary once, after the last test of the run. */
    @Override
    public void testRunFinished(Result result) {
        StringBuilder text = new StringBuilder();
        text.append(System.lineSeparator());
        for (String line : summary()) {
            text.append(line).append(System.lineSeparator());
        }
        System.out.print(text);
        System.out.flush();
    }
}
