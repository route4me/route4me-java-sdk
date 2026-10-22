package com.route4me.sdk.integration;

import com.route4me.sdk.services.activities.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ActivityManagerIntegrationTest extends BaseIntegrationTest {
    private ActivityManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new ActivityManager(API_KEY);
    }

    @Test
    public void testGetActivities() throws APIException {
        ActivityRequest request = new ActivityRequest();
        request.setLimit(10);
        request.setOffset(0);
        Activities activities = manager.getActivities(request);
        assertNotNull(activities);
    }
}
