package com.route4me.sdk.integration;

import com.route4me.sdk.services.driverreview.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DriverReviewManagerIntegrationTest extends BaseIntegrationTest {
    private DriverReviewManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new DriverReviewManager(API_KEY);
    }

    @Test
    public void testGetDriverReviews() throws APIException {
        DriverReviewsResponse reviews = manager.getDriverReviews(null, null, null, null, 1, 10);
        assertNotNull(reviews);
    }
}
