package com.route4me.sdk.testsupport;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link EndpointRegistry}: loading {@code endpoints.json} and matching methods,
 * hosts and path patterns.
 */
public class EndpointRegistryTest {

    private final EndpointRegistry registry = EndpointRegistry.load();

    @Test
    public void loadsAllowedHostsFromTheClasspath() {
        assertTrue(registry.isAllowedHost("api.route4me.com"));
        assertTrue(registry.isAllowedHost("wh.route4me.com"));
        assertFalse(registry.isAllowedHost("telematics.route4me.com"));
        assertFalse(registry.isAllowedHost("example.com"));
        assertFalse(registry.isAllowedHost(null));
    }

    @Test
    public void placeholderSegmentMatchesExactlyOneSegment() {
        assertTrue(EndpointRegistry.pathMatches("/a/{}/c", "/a/b/c"));
        assertFalse(EndpointRegistry.pathMatches("/a/{}/c", "/a/c"));
        assertFalse(EndpointRegistry.pathMatches("/a/{}/c", "/a/b/x/c"));
        assertFalse(EndpointRegistry.pathMatches("/a/{}", "/a/"));
        assertFalse(EndpointRegistry.pathMatches("/a/b", "/a/b/"));
        assertTrue(EndpointRegistry.pathMatches("/a/b", "/a/b"));
    }

    @Test
    public void placeholderSegmentNeverMatchesADotSegment() {
        for (String dots : new String[] {".", "..", "%2e", "%2E%2e", ".%2E", "%2e."}) {
            assertFalse(dots, EndpointRegistry.pathMatches("/a/{}/c", "/a/" + dots + "/c"));
        }
        assertTrue(EndpointRegistry.pathMatches("/a/{}/c", "/a/.b/c"));
        assertTrue(EndpointRegistry.pathMatches("/a/{}/c", "/a/.../c"));
    }

    @Test
    public void placeholderSegmentNeverMatchesAnEncodedSlashOrBackslash() {
        // Some front ends decode %2F and %5C before they resolve "..": /a/..%2Fusers would become /users.
        for (String segment : new String[] {"%2e%2fx", "..%2Fusers", "a%2F..%2Fb", "%2f", "x%2F", "..%5Cusers",
            "a%5c..%5cb", "%5C", "%5c"}) {
            assertFalse(segment, EndpointRegistry.pathMatches("/a/{}/c", "/a/" + segment + "/c"));
        }
        assertTrue(EndpointRegistry.pathMatches("/a/{}/c", "/a/x%20y/c"));
        assertTrue(EndpointRegistry.pathMatches("/a/{}/c", "/a/x%252Fy/c"));
    }

    @Test
    public void placeholderSegmentNeverMatchesAPathParameter() {
        // Some servers drop ";params" before they resolve "..": /a/..;x would become /.
        for (String segment : new String[] {"..;x", ".;", "..;", ";", "b;c"}) {
            assertFalse(segment, EndpointRegistry.pathMatches("/a/{}/c", "/a/" + segment + "/c"));
        }
    }

    @Test
    public void theV5HostHasNoCatchAllEntries() {
        // The URL shortener's /{short_link} and the route export paths under the spec's bare
        // https://wh.route4me.com/modules/ server matched almost any path on the V5 host.
        assertNull(registry.findEndpoint("GET", "wh.route4me.com", "/anything"));
        assertNull(registry.findEndpoint("GET", "wh.route4me.com", "/info/anything"));
        assertNull(registry.findEndpoint("GET", "wh.route4me.com", "/modules/anything/export/1"));
        assertNull(registry.findEndpoint("POST", "wh.route4me.com", "/modules/anything/async-export"));
        assertNull(registry.findEndpoint("GET", "wh.route4me.com", "/modules/export"));
        assertNotNull(registry.findEndpoint("GET", "wh.route4me.com", "/modules/api/v5.0/route-export/R1/export/P1"));
        assertNotNull(registry.findEndpoint("POST", "wh.route4me.com",
                "/modules/api/v5.0/route-export/R1/async-export"));
    }

    @Test
    public void anyMethodEntryMatchesEveryMethod() {
        for (String method : new String[] {"GET", "POST", "PUT", "DELETE"}) {
            assertNotNull(method, registry.findEndpoint(method, "api.route4me.com", "/api.v4/route.php"));
        }
    }

    @Test
    public void methodSpecificEntryMatchesOnlyThatMethod() {
        String path = "/modules/api/v5.0/equipment-types/E1";
        assertNotNull(registry.findEndpoint("GET", "wh.route4me.com", path));
        assertNotNull(registry.findEndpoint("DELETE", "wh.route4me.com", path));
        assertNull(registry.findEndpoint("PUT", "wh.route4me.com", path));
        assertTrue(registry.knownMethods("wh.route4me.com", path).contains("GET"));
    }

    @Test
    public void hostIsPartOfTheMatch() {
        assertNull(registry.findEndpoint("GET", "wh.route4me.com", "/api.v4/route.php"));
    }

    @Test
    public void findsDeprecatedEntriesForAnyMethod() {
        EndpointRegistry.Entry entry = registry.findDeprecated("telematics.route4me.com", "/api/vendors.php");
        assertNotNull(entry);
        assertNotNull(entry.getReason());
        assertEquals("/api/vendors.php", entry.getPath());
        assertNull(registry.findDeprecated("api.route4me.com", "/api.v4/route.php"));
    }
}
