package com.route4me.sdk.integration;

import com.route4me.sdk.services.telematics.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TelematicsManagerIntegrationTest extends BaseIntegrationTest {
    private TelematicsManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new TelematicsManager(API_KEY);
    }

    @Test
    public void testGetTelematicsVendorsInfo() throws APIException {
        TelematicsVendorsInfo vendors = manager.getTelematicsVendorsInfo();
        assertNotNull(vendors);
    }

    @Test
    public void testSearchVendorByKeyWord() throws APIException {
        TelematicsVendorsInfo result = manager.searchVendorByKeyWord("GPS");
        assertNotNull(result);
    }
}
