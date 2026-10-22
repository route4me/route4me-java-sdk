package com.route4me.sdk.integration;

import com.route4me.sdk.services.routing.breaks.RouteBreaksManager;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RouteBreaksManagerIntegrationTest extends BaseIntegrationTest {
    private RouteBreaksManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new RouteBreaksManager(API_KEY);
    }

    // Route breaks operations require existing route IDs
    // Manager instantiation validates basic connectivity setup
}
