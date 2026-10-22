package com.route4me.sdk.integration;

import com.route4me.sdk.services.vehicles.VehiclesManager;
import com.route4me.sdk.services.vehicles.Vehicles;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class VehiclesManagerIntegrationTest extends BaseIntegrationTest {
    private VehiclesManager manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new VehiclesManager(API_KEY);
    }

    @Test
    public void testGetVehicles() throws APIException {
        List<Vehicles> vehicles = manager.getVehicles();
        assertNotNull(vehicles);
    }
}
