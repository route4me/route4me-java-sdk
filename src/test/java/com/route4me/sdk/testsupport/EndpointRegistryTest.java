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
    public void anyMethodEntryMatchesEveryMethod() {
        for (String method : new String[] {"GET", "POST", "PUT", "DELETE"}) {
            assertNotNull(method, registry.findEndpoint(method, "api.route4me.com", "/api.v4/route.php"));
        }
    }

    @Test
    public void methodSpecificEntryMatchesOnlyThatMethod() {
        String path = "/modules/api/v5.0/vehicles/V1";
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
