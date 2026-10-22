package com.route4me.sdk.testsupport;

import com.route4me.sdk.exception.APIException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RequestGuard}: what it accepts, what it rejects, and that its messages
 * never print the API key.
 */
public class RequestGuardTest {

    private static final String KEY = MockHttp.API_KEY;

    /** Kept apart from the run-wide report, so the self-tests do not show up in its summary. */
    private final DeprecatedEndpointReport.Hits hits = new DeprecatedEndpointReport.Hits();
    private final RequestGuard guard = new RequestGuard(EndpointRegistry.load(), KEY, hits);

    /** Copies a request the way {@link MockHttp} does. */
    private static CapturedRequest capture(HttpUriRequest request) throws IOException {
        return CapturedRequest.from(request);
    }

    /** Runs the guard on a request and checks that no message prints the key. */
    private List<String> check(HttpUriRequest request) throws IOException {
        List<String> problems = guard.check(capture(request), "RequestGuardTest");
        for (String problem : problems) {
            assertFalse("Guard messages must not print the key: " + problem, problem.contains(KEY));
        }
        return problems;
    }

    @Test
    public void acceptsKnownEndpointWithKeyInApiKeyParameter() throws IOException {
        List<String> problems = check(new HttpGet("https://api.route4me.com/api.v4/route.php?route_id=R1&api_key=" + KEY));
        assertEquals("[]", problems.toString());
    }

    @Test
    public void rejectsPlainHttp() throws IOException {
        List<String> problems = check(new HttpGet("http://api.route4me.com/api.v4/route.php"));
        assertEquals(1, problems.size());
        assertTrue(problems.get(0).contains("scheme must be https"));
    }

    @Test
    public void rejectsHostOutsideTheRegistry() throws IOException {
        List<String> problems = check(new HttpGet("https://example.com/api.v4/route.php"));
        assertEquals(1, problems.size());
        assertTrue(problems.get(0).contains("not in allowed_hosts"));
    }

    @Test
    public void rejectsUnknownPathAndListsKnownMethods() throws IOException {
        List<String> unknown = check(new HttpGet("https://api.route4me.com/api.v4/no-such-endpoint.php"));
        assertEquals(1, unknown.size());
        assertTrue(unknown.get(0).contains("is not a known endpoint"));

        List<String> wrongMethod = check(new HttpPut("https://wh.route4me.com/modules/api/v5.0/equipment-types/E1"));
        assertEquals(1, wrongMethod.size());
        assertTrue(wrongMethod.get(0), wrongMethod.get(0).contains("known methods"));
    }

    @Test
    public void allowsDeprecatedEndpointButRecordsIt() throws IOException {
        List<String> problems = guard.check(
                capture(new HttpGet("https://telematics.route4me.com/api/vendors.php?api_key=" + KEY)),
                "RequestGuardTest.allowsDeprecatedEndpointButRecordsIt");
        assertEquals("[]", problems.toString());
        assertTrue(hits.summary().toString().contains("GET https://telematics.route4me.com/api/vendors.php"));
        assertTrue(hits.summary().toString().contains("RequestGuardTest.allowsDeprecatedEndpointButRecordsIt"));
        assertEquals("[Deprecated endpoints hit by unit tests: none]",
                new DeprecatedEndpointReport.Hits().summary().toString());
    }

    @Test
    public void deprecatedEndpointMustStillUseHttps() throws IOException {
        List<String> problems = check(new HttpGet("http://telematics.route4me.com/api/vendors.php"));
        assertEquals(1, problems.size());
    }

    @Test
    public void rejectsKeyInHeader() throws IOException {
        HttpGet request = new HttpGet("https://api.route4me.com/api.v4/route.php");
        request.setHeader("Authorization", "Bearer " + KEY);
        List<String> problems = check(request);
        assertEquals(1, problems.size());
        assertTrue(problems.get(0).contains("header 'Authorization'"));
    }

    @Test
    public void rejectsKeyInOtherQueryParameterOrPath() throws IOException {
        assertEquals(1, check(new HttpGet("https://api.route4me.com/api.v4/route.php?route_id=" + KEY)).size());
        assertTrue(check(new HttpGet("https://wh.route4me.com/modules/api/v5.0/vehicles/" + KEY)).toString()
                .contains("URL path"));
    }

    @Test
    public void rejectsKeyInBody() throws IOException {
        HttpPost request = new HttpPost("https://api.route4me.com/api.v4/route.php");
        request.setEntity(new StringEntity("{\"key\":\"" + KEY + "\"}", "UTF-8"));
        List<String> problems = check(request);
        assertEquals(1, problems.size());
        assertTrue(problems.get(0).contains("request body"));
    }

    @Test
    public void findsKeyInExceptionCauseChain() {
        Exception cause = new IllegalStateException("bad key " + KEY);
        APIException exception = new APIException("wrapper", cause);
        List<String> leaks = guard.keyLeaks(exception);
        assertEquals(1, leaks.size());
        assertTrue(leaks.get(0).contains(IllegalStateException.class.getName()));
        assertTrue(guard.keyLeaks(new APIException("no key here")).isEmpty());
    }

    @Test
    public void managerWithoutKeyHasNothingToLeak() throws IOException {
        RequestGuard noKey = new RequestGuard(EndpointRegistry.load(), null, hits);
        assertTrue(noKey.keyLeaks(capture(new HttpGet("https://api.route4me.com/api.v4/route.php"))).isEmpty());
        assertTrue(noKey.keyLeaks(new APIException("anything")).isEmpty());
    }
}
