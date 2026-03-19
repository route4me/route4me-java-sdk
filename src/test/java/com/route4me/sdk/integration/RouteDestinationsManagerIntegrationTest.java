package com.route4me.sdk.integration;

import com.route4me.sdk.services.routedestinations.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RouteDestinationsManagerIntegrationTest extends BaseIntegrationTest {
    private RouteDestinationsManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new RouteDestinationsManager(API_KEY);
    }

    @Test
    public void testGetDestinationFields() throws APIException {
        RouteDestinationFieldsResponse fields = manager.getDestinationFields();
        assertNotNull(fields);
    }
}
