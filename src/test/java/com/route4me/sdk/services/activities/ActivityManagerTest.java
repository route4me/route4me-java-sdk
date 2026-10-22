package com.route4me.sdk.services.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ActivityManager}: reading the activity feed and logging a user message.
 */
public class ActivityManagerTest {

    private static final String HOST = "api.route4me.com";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private ActivityManager manager() {
        return http.install(new ActivityManager(MockHttp.API_KEY));
    }

    @Test
    public void getActivitiesSendsTheFiltersAsQuery() throws Exception {
        ActivityManager manager = manager();
        http.respondWithFixture(200, "activities.json");
        ActivityRequest request = new ActivityRequest().setRouteId("ROUTE-A1").setType(ActivityType.USER_MESSAGE)
                .setLimit(10).setOffset(0).setTeam(true).setMemberId(1001).setDeviceId("DEV-1").setStart(1).setEnd(2);

        Activities activities = manager.getActivities(request);

        http.request().assertEndpoint("GET", HOST, "/api/get_activities.php").assertApiKeyInQuery()
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("type", "user_message")
                .assertQuery("limit", "10")
                .assertQuery("offset", "0")
                .assertQuery("team", "1")
                .assertQuery("member_id", "1001")
                .assertQuery("device_id", "DEV-1")
                .assertQuery("start", "1")
                .assertQuery("end", "2")
                .assertNoBody();
        assertEquals(2, activities.getResults().size());
        assertEquals(ActivityType.USER_MESSAGE, activities.getResults().get(0).getType());
        assertEquals(ActivityType.ROUTE_OPTIMIZED, activities.getResults().get(1).getType());
        assertEquals("Driver is running late", activities.getResults().get(0).getMessage());
        assertEquals(2, activities.getTotal().intValue());
    }

    @Test
    public void logActivityPostsAUserMessage() throws Exception {
        ActivityManager manager = manager();
        http.respond(200, json("{'status':true}"));

        manager.logActivity("Driver is running late", "ROUTE-A1");

        http.request().assertEndpoint("POST", HOST, "/api.v4/activity_feed.php")
                .assertJson("activity_message", "Driver is running late")
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("activity_type", "user_message");
    }

    @Test
    public void logActivityFailsWhenTheStatusIsFalse() {
        final ActivityManager manager = manager();
        http.respond(200, json("{'status':false}"));

        APIException error = http.expectApiException(() -> manager.logActivity("hello", "ROUTE-A1"));

        assertTrue(error.getMessage().contains("has not succeeded"));
    }

    @Test
    public void errorStatusBecomesApiException() {
        final ActivityManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getActivities(new ActivityRequest()));
    }

    @Test
    public void logActivityTreatsAMissingStatusAsFailure() {
        final ActivityManager manager = manager();
        http.respond(200, "{}");

        http.expectApiException(() -> manager.logActivity("hello", "ROUTE-A1"));
    }

    @Test
    public void logActivityTreatsAnEmptyResponseAsFailure() {
        final ActivityManager manager = manager();
        http.respond(200, "");

        http.expectApiException(() -> manager.logActivity("hello", "ROUTE-A1"));
    }
}
