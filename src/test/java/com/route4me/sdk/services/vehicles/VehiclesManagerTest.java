package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link VehiclesManager}, the V4-era vehicles API on
 * {@code wh.route4me.com/modules/api/vehicles}.
 */
@SuppressWarnings("deprecation") // VehiclesManager is deprecated (obsolete in C#), but its requests stay tested
public class VehiclesManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String VEHICLES_EP = "/modules/api/vehicles";
    private static final String VEHICLE_JSON =
            json("{'vehicle_id':'VEH-1','vehicle_alias':'Van 1','vehicle_make':'Ford','vehicle_model_year':2023}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private VehiclesManager manager() {
        return http.install(new VehiclesManager(MockHttp.API_KEY));
    }

    /** Builds a sample vehicle with fake data. */
    private static Vehicles vehicle() {
        Vehicles vehicle = new Vehicles();
        vehicle.setVehicleId("VEH-1");
        vehicle.setVehicleAlias("Van 1");
        vehicle.setVehicleMake(Vehicles.VehicleMake.FORD.getValue());
        return vehicle;
    }

    @Test
    public void getVehiclesListsTheVehicles() throws Exception {
        VehiclesManager manager = manager();
        http.respond(200, "[" + VEHICLE_JSON + "]");

        List<Vehicles> vehicles = manager.getVehicles();

        http.request().assertEndpoint("GET", HOST, VEHICLES_EP).assertApiKeyInQuery();
        assertEquals("Van 1", vehicles.get(0).getVehicleAlias());
    }

    @Test
    public void getVehicleAndDeleteVehiclePutTheIdInThePath() throws Exception {
        VehiclesManager manager = manager();
        http.respondToAll(200, VEHICLE_JSON);

        Vehicles vehicle = manager.getVehicle("VEH-1");
        manager.deleteVehicle("VEH-1");

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/VEH-1");
        http.request(1).assertEndpoint("DELETE", HOST, VEHICLES_EP + "/VEH-1").assertNoBody();
        assertEquals("Ford", vehicle.getVehicleMake());
    }

    @Test
    public void newVehiclePostsAndUpdateVehiclePutsTheVehicle() throws Exception {
        VehiclesManager manager = manager();
        http.respondToAll(200, VEHICLE_JSON);

        manager.newVehicle(vehicle());
        manager.updateVehicle(vehicle());

        http.request(0).assertEndpoint("POST", HOST, VEHICLES_EP).assertJson("vehicle_alias", "Van 1")
                .assertJson("vehicle_make", "Ford");
        http.request(1).assertEndpoint("PUT", HOST, VEHICLES_EP + "/VEH-1").assertJson("vehicle_id", "VEH-1");
    }

    @Test
    public void unsetFlagsAreLeftOutOfTheBody() throws Exception {
        VehiclesManager manager = manager();
        http.respondToAll(200, VEHICLE_JSON);

        manager.newVehicle(vehicle());
        manager.updateVehicle(vehicle());

        http.request(0).assertJsonLacks("is_operational", "is_deleted");
        http.request(1).assertJsonLacks("is_operational", "is_deleted");
    }

    @Test
    public void flagsAreSentWhenSet() throws Exception {
        VehiclesManager manager = manager();
        http.respondToAll(200, VEHICLE_JSON);
        Vehicles operational = vehicle();
        operational.setOperational(true);
        operational.setDeleted(false);
        Vehicles cleared = vehicle();
        cleared.setOperational(Boolean.FALSE);
        cleared.setDeleted((Boolean) null);

        manager.updateVehicle(operational);
        manager.updateVehicle(cleared);

        http.request(0).assertJson("is_operational", true).assertJson("is_deleted", false);
        http.request(1).assertJson("is_operational", false).assertJsonLacks("is_deleted");
        assertTrue(operational.isOperational());
        assertEquals(Boolean.TRUE, operational.getOperational());
        assertFalse(operational.isDeleted());
        assertEquals(Boolean.FALSE, operational.getDeleted());
        assertFalse(cleared.isOperational());
        assertNull(cleared.getDeleted());
    }

    @Test
    public void readFlagsKeepTheirValues() throws Exception {
        VehiclesManager manager = manager();
        http.respond(200, json("{'vehicle_id':'VEH-1','is_operational':1,'is_deleted':false}"))
                .respond(200, json("{'vehicle_id':'VEH-1'}"));

        Vehicles read = manager.getVehicle("VEH-1");
        Vehicles bare = manager.getVehicle("VEH-1");

        assertTrue(read.isOperational());
        assertEquals(Boolean.FALSE, read.getDeleted());
        assertFalse(bare.isOperational());
        assertFalse(bare.isDeleted());
        assertNull(bare.getOperational());
    }

    @Test
    public void updateVehicleRequiresTheVehicleAndItsId() {
        final VehiclesManager manager = manager();
        final Vehicles noId = vehicle();
        noId.setVehicleId(null);
        final Vehicles emptyId = vehicle();
        emptyId.setVehicleId("");

        http.expectThrows(IllegalArgumentException.class, () -> manager.updateVehicle(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateVehicle(noId));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateVehicle(emptyId));
        http.assertNoRequests();
    }

    @Test
    public void errorBecomesApiException() {
        final VehiclesManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(manager::getVehicles);
    }

    @Test
    public void vehicleMakeHasDisplayNames() {
        assertEquals("Mercedes-Benz", Vehicles.VehicleMake.MERCEDES_BENZ.toString());
        assertEquals("Western Star", Vehicles.VehicleMake.WESTERN_STAR.getValue());
    }
}
