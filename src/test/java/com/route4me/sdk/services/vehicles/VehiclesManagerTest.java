package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

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
