package com.route4me.sdk.integration;

import com.route4me.sdk.services.geocoding.GeocodingManager;
import com.route4me.sdk.services.routing.Geocodings;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeocodingManagerIntegrationTest extends BaseIntegrationTest {
    private GeocodingManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new GeocodingManager(API_KEY);
    }

    @Test
    public void testGeocodeAddress() throws APIException {
        Geocodings[] result = manager.geocode("1600 Amphitheatre Pkwy, Mountain View, CA");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
