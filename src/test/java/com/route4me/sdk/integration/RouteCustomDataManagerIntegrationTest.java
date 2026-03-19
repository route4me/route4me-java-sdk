package com.route4me.sdk.integration;

import com.route4me.sdk.services.routecustomdata.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RouteCustomDataManagerIntegrationTest extends BaseIntegrationTest {
    private RouteCustomDataManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new RouteCustomDataManager(API_KEY);
    }

    @Test
    public void testGetCustomDataKeys() throws APIException {
        String[] keys = manager.getCustomDataKeys();
        assertNotNull(keys);
    }
}
