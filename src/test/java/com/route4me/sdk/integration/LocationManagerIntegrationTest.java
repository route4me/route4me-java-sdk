package com.route4me.sdk.integration;

import com.route4me.sdk.services.locations.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocationManagerIntegrationTest extends BaseIntegrationTest {
    private LocationManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new LocationManager(API_KEY);
    }

    @Test
    public void testGetLocationTypes() throws APIException {
        LocationTypeCollection types = manager.getLocationTypes(1, 10);
        assertNotNull(types);
    }

    @Test
    public void testGetLocationsList() throws APIException {
        LocationCombinedRequest request = new LocationCombinedRequest();
        request.setPage(1);
        request.setPerPage(10);
        LocationListResource list = manager.getLocationsList(request);
        assertNotNull(list);
    }
}
