package com.route4me.sdk.services.routing;

import com.route4me.sdk.testsupport.MockHttp;
import com.route4me.sdk.utils.Sanitizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Links}: the V4 links URLs may carry the {@code api_key}, which
 * {@code toString()} must hide.
 */
public class LinksTest {

    private static final String ROUTE_URL =
            "https://api.route4me.com/api.v4/route.php?route_id=ROUTE-A1&api_key=" + MockHttp.API_KEY + "&member_id=1";
    private static final String VIEW_URL =
            "https://api.route4me.com/route4me.php?api_key=" + MockHttp.API_KEY + "&route_id=ROUTE-A1";
    private static final String OPTIMIZATION_URL =
            "https://api.route4me.com/api.v4/optimization_problem.php?optimization_problem_id=OPT-1&api_key="
                    + MockHttp.API_KEY;

    /** Builds links whose three URLs carry the test API key. */
    private static Links linksWithKey() {
        Links links = new Links();
        links.setRoute(ROUTE_URL);
        links.setView(VIEW_URL);
        links.setOptimizationProblem(OPTIMIZATION_URL);
        return links;
    }

    @Test
    public void toStringHidesTheApiKeyInEveryUrl() {
        String text = linksWithKey().toString();

        assertFalse("Links.toString() must not contain the API key", text.contains(MockHttp.API_KEY));
        assertTrue(text, text.contains("route_id=ROUTE-A1&api_key=" + Sanitizer.REDACTED + "&member_id=1"));
        assertTrue(text, text.contains("route4me.php?api_key=" + Sanitizer.REDACTED + "&route_id=ROUTE-A1"));
        assertTrue(text, text.contains("optimization_problem_id=OPT-1&api_key=" + Sanitizer.REDACTED));
    }

    @Test
    public void gettersKeepTheUrlsUnchanged() {
        Links links = linksWithKey();

        assertEquals(ROUTE_URL, links.getRoute());
        assertEquals(VIEW_URL, links.getView());
        assertEquals(OPTIMIZATION_URL, links.getOptimizationProblem());
    }

    @Test
    public void toStringPrintsNullUrls() {
        assertEquals("Links(optimizationProblem=null, route=null, view=null)", new Links().toString());
    }

    @Test
    public void dataObjectToStringHidesTheApiKeyInItsLinks() {
        DataObject optimization = new DataObject();
        optimization.setLinks(linksWithKey());

        assertFalse("DataObject.toString() must not contain the API key",
                optimization.toString().contains(MockHttp.API_KEY));
    }
}
