package com.route4me.sdk.integration;

import com.route4me.sdk.services.vehicles.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class VehiclesManagerV5IntegrationTest extends BaseIntegrationTest {
    private VehiclesManagerV5 manager;
    private List<String> createdIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new VehiclesManagerV5(API_KEY);
    }

    @After
    public void tearDown() {
        for (String id : createdIds) {
            try { manager.deleteVehicle(id); } catch (Exception e) { }
        }
    }

    private VehicleV5 buildVehicle() {
        VehicleV5 vehicle = new VehicleV5();
        vehicle.setVehicleAlias("SDK Test Vehicle " + System.currentTimeMillis());
        vehicle.setVehicleMake("Ford");
        vehicle.setVehicleModel("Transit");
        vehicle.setVehicleModelYear(2023);
        vehicle.setVehicleLicensePlate("SDK" + System.currentTimeMillis() % 10000);
        return vehicle;
    }

    @Test
    public void testCreateAndGetVehicle() throws APIException {
        VehicleV5 created = manager.createVehicle(buildVehicle());
        assertNotNull(created);
        assertNotNull(created.getVehicleId());
        createdIds.add(created.getVehicleId());

        VehicleV5 fetched = manager.getVehicleById(created.getVehicleId());
        assertNotNull(fetched);
        assertEquals(created.getVehicleId(), fetched.getVehicleId());
    }

    @Test
    public void testGetVehicles() throws APIException {
        VehicleV5 created = manager.createVehicle(buildVehicle());
        createdIds.add(created.getVehicleId());

        VehicleV5[] vehicles = manager.getVehicles(1, 10);
        assertNotNull(vehicles);
        assertTrue(vehicles.length > 0);
    }

    @Test
    public void testUpdateVehicle() throws APIException {
        VehicleV5 created = manager.createVehicle(buildVehicle());
        createdIds.add(created.getVehicleId());

        VehicleV5 update = new VehicleV5();
        update.setVehicleAlias("SDK Updated Vehicle");
        VehicleV5 updated = manager.updateVehicle(created.getVehicleId(), update);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteVehicle() throws APIException {
        VehicleV5 created = manager.createVehicle(buildVehicle());
        assertNotNull(created.getVehicleId());
        VehicleV5 deleted = manager.deleteVehicle(created.getVehicleId());
        assertNotNull(deleted);
    }

    @Test
    public void testSearchVehicles() throws APIException {
        VehicleV5 created = manager.createVehicle(buildVehicle());
        createdIds.add(created.getVehicleId());

        VehicleV5[] results = manager.searchVehicles("SDK");
        assertNotNull(results);
    }

    @Test
    public void testGetVehicleProfiles() throws APIException {
        VehicleProfilesResponse profiles = manager.getVehicleProfiles(1, 10);
        assertNotNull(profiles);
    }

    @Test
    public void testGetVehicleCapacityProfiles() throws APIException {
        VehicleCapacityProfilesResponse profiles = manager.getVehicleCapacityProfiles(1, 10);
        assertNotNull(profiles);
    }
}
