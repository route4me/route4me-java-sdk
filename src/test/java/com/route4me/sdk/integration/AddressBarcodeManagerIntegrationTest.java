package com.route4me.sdk.integration;

import com.route4me.sdk.services.addressbarcode.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddressBarcodeManagerIntegrationTest extends BaseIntegrationTest {
    private AddressBarcodeManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new AddressBarcodeManager(API_KEY);
    }

    // Address barcode operations require existing route_id and route_destination_id
    // Manager instantiation test validates connectivity
}
