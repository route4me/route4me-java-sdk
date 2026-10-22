package com.route4me.sdk.services.driverreview;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DriverReviewManager}: customer reviews of drivers on the V5 API.
 */
public class DriverReviewManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String REVIEWS_EP = "/modules/api/v5.0/driver-reviews";
    private static final String REVIEW = json("{'rating_id':'RATE-1','tracking_number':'TRK-1',"
            + "'review':'On time','rating':4.5,'added_at':'2026-10-21 10:00:00'}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private DriverReviewManager manager() {
        return http.install(new DriverReviewManager(MockHttp.API_KEY));
    }

    /** Builds a sample driver review with fake data. */
    private static DriverReview review() {
        DriverReview review = new DriverReview();
        review.setTrackingNumber("TRK-1");
        review.setReview("On time");
        review.setRating(4.5);
        return review;
    }

    @Test
    public void getDriverReviewsSendsOnlyTheGivenFilters() throws Exception {
        DriverReviewManager manager = manager();
        http.respondToAll(200, "{\"data\":[" + REVIEW + "],\"simple_pagination\":{\"per_page\":10,"
                + "\"current_page\":1,\"next\":\"page-2\"},\"total\":[{\"type\":5,\"quantity\":3}]}");

        DriverReviewsResponse reviews = manager.getDriverReviews(5, 1001L, "2026-10-01", "2026-10-31", 1, 10);
        manager.getDriverReviews(null, null, null, null, null, null);

        http.request(0).assertEndpoint("GET", HOST, REVIEWS_EP).assertApiKeyInQuery()
                .assertQuery("rating", "5")
                .assertQuery("member_id", "1001")
                .assertQuery("start", "2026-10-01")
                .assertQuery("end", "2026-10-31")
                .assertQuery("page", "1")
                .assertQuery("per_page", "10");
        http.request(1).assertNoQuery("rating").assertNoQuery("member_id").assertNoQuery("page");
        assertEquals("On time", reviews.getData().get(0).getReview());
        assertEquals("page-2", reviews.getSimplePagination().getNext());
        assertEquals(Integer.valueOf(3), reviews.getTotal().get(0).getQuantity());
    }

    @Test
    public void getCreateAndUpdateReview() throws Exception {
        DriverReviewManager manager = manager();
        http.respondToAll(200, "{\"data\":" + REVIEW + "}");

        DriverReviewResponse fetched = manager.getDriverReviewById("RATE-1");
        manager.createDriverReview(review());
        manager.updateDriverReview("RATE-1", review());

        http.request(0).assertEndpoint("GET", HOST, REVIEWS_EP + "/RATE-1");
        http.request(1).assertEndpoint("POST", HOST, REVIEWS_EP)
                .assertJson("tracking_number", "TRK-1")
                .assertJson("review", "On time")
                .assertJson("rating", 4.5);
        http.request(2).assertEndpoint("PUT", HOST, REVIEWS_EP + "/RATE-1").assertJson("rating", 4.5);
        assertEquals(4.5, fetched.getData().getRating(), 0.0);
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        DriverReviewManager manager = http.install(new DriverReviewManager(MockHttp.API_KEY, true));
        http.respond(200, "{\"data\":" + REVIEW + "}");

        manager.getDriverReviewById("RATE-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final DriverReviewManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getDriverReviewById("RATE-1"));
    }
}
