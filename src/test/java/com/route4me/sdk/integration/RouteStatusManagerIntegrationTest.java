package com.route4me.sdk.integration;

import com.route4me.sdk.services.routestatus.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RouteStatusManagerIntegrationTest extends BaseIntegrationTest {
    private RouteStatusManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new RouteStatusManager(API_KEY);
    }

    // Route status operations require an existing route_id
    // These tests validate manager instantiation
    // Full CRUD tests would need a route created via RoutingManager first
}
