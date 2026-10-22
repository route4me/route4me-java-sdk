package com.route4me.sdk.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link Route4MeRedirectStrategy}: which redirect targets it follows.
 */
public class Route4MeRedirectStrategyTest {

    private final Route4MeRedirectStrategy strategy = new Route4MeRedirectStrategy();

    /** Builds a redirect response with a {@code Location} header. */
    private static HttpResponse redirect(int status, String location) {
        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, status, "Redirect");
        response.setHeader("Location", location);
        return response;
    }

    /** Builds a context whose target is the V4 API host. */
    private static HttpClientContext context() {
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute(HttpClientContext.HTTP_TARGET_HOST, new HttpHost("api.route4me.com", 443, "https"));
        return context;
    }

    /** Resolves a redirect target, as the client does before following it. */
    private URI follow(String location) throws ProtocolException {
        HttpGet request = new HttpGet("https://api.route4me.com/api.v4/route.php?api_key=k");
        return strategy.getLocationURI(request, redirect(302, location), context());
    }

    /** Asserts that following {@code location} is refused without naming its query. */
    private void assertRefused(String location) {
        try {
            follow(location);
            fail("expected the redirect to " + location + " to be refused");
        } catch (RedirectException expected) {
            assertTrue(expected.getMessage(), expected.getMessage().startsWith("Refused to follow a redirect to "));
            assertFalse("the message must not repeat the query: " + expected.getMessage(),
                    expected.getMessage().contains("secret"));
        } catch (ProtocolException other) {
            throw new AssertionError("expected a RedirectException for " + location, other);
        }
    }

    @Test
    public void followsHttpsRedirectsToRoute4MeHosts() throws Exception {
        for (String host : Route4MeRedirectStrategy.ALLOWED_HOSTS) {
            URI target = follow("https://" + host + "/x?y=1");
            assertEquals("https", target.getScheme());
            assertEquals(host, target.getHost());
            assertEquals("/x", target.getPath());
            assertEquals("y=1", target.getQuery());
        }
        URI upperCase = follow("https://WH.route4me.com:443/x");
        assertTrue(upperCase.getHost(), "wh.route4me.com".equalsIgnoreCase(upperCase.getHost()));
    }

    @Test
    public void followsRelativeRedirectsOnTheSameHost() throws Exception {
        URI target = follow("/api.v4/other.php");

        assertEquals("https", target.getScheme());
        assertEquals("api.route4me.com", target.getHost());
        assertEquals("/api.v4/other.php", target.getPath());
    }

    @Test
    public void refusesOtherHostsSchemesPortsAndUserInfo() {
        assertRefused("https://example.com/?api_key=secret");
        assertRefused("https://api.route4me.com.example.com/?api_key=secret");
        assertRefused("https://route4me.com/?utm_source=secret");
        assertRefused("http://api.route4me.com/api.v4/route.php?api_key=secret");
        assertRefused("ftp://api.route4me.com/secret");
        assertRefused("https://api.route4me.com:8443/?api_key=secret");
        assertRefused("https://user:secret@api.route4me.com/");
    }

    @Test
    public void isAllowedRejectsIncompleteTargets() {
        assertFalse(Route4MeRedirectStrategy.isAllowed(null));
        assertFalse(Route4MeRedirectStrategy.isAllowed(URI.create("/relative")));
        assertFalse(Route4MeRedirectStrategy.isAllowed(URI.create("https:opaque")));
        assertTrue(Route4MeRedirectStrategy.isAllowed(URI.create("https://wh.route4me.com/modules/api/v5.0/x")));
    }

    @Test
    public void refusesTheOtherRoute4MeHosts() {
        assertRefused("https://rapid.route4me.com/timezone?api_key=secret");
        assertRefused("https://validator.route4me.com/?api_key=secret");
        assertRefused("https://telematics.route4me.com/api/vendors.php?api_key=secret");
    }

    @Test
    public void stillFollowsOnlyTheRedirectsTheDefaultStrategyFollows() throws Exception {
        HttpGet get = new HttpGet("https://api.route4me.com/api.v4/route.php");
        HttpPost post = new HttpPost("https://api.route4me.com/api.v4/route.php");

        assertTrue(strategy.isRedirected(get, redirect(302, "https://wh.route4me.com/x"), context()));
        assertFalse(strategy.isRedirected(post, redirect(302, "https://wh.route4me.com/x"), context()));
        assertTrue(strategy.isRedirected(post, redirect(303, "https://wh.route4me.com/x"), context()));
    }

    @Test
    public void allowedHostsCannotBeChanged() {
        try {
            Route4MeRedirectStrategy.ALLOWED_HOSTS.add("example.com");
            fail("ALLOWED_HOSTS must be read-only");
        } catch (UnsupportedOperationException expected) {
            assertEquals("[api.route4me.com, wh.route4me.com]", Route4MeRedirectStrategy.ALLOWED_HOSTS.toString());
        }
    }
}
