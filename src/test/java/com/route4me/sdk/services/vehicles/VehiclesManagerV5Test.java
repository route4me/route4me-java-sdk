package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link VehiclesManagerV5}: vehicles, bulk operations, vehicle profiles and
 * capacity profiles on the V5 API.
 *
 * <p>The update methods send {@code PUT}, but the registry knows only {@code GET},
 * {@code DELETE} and (for the profiles) {@code PATCH} on those paths, and the job-tracker
 * paths are not in the registry. Those tests assert the request the SDK means to send and
 * are ignored with a {@code registry:} reason.
 */
public class VehiclesManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String VEHICLES_EP = "/modules/api/v5.0/vehicles";
    private static final String PROFILES_EP = "/modules/api/v5.0/vehicle-profiles";
    private static final String CAPACITY_EP = "/modules/api/v5.0/vehicle-capacity-profiles";
    private static final String VEHICLE = json("{'vehicle_id':'VEH-1','member_id':1001,'vehicle_alias':'Van 1',"
            + "'vehicle_make':'Ford','vehicle_model':'Transit','vehicle_model_year':2023,'vehicle_license_plate':'EX-123',"
            + "'fuel_type':'diesel','mpg_city':18.5}");
    private static final String PROFILE = json("{'vehicle_profile_id':3,'name':'Box truck','height':3.5,"
            + "'width':2.4,'length':7.0,'weight':7500,'is_default':false}");
    private static final String CAPACITY = json("{'data':{'vehicle_capacity_profile_id':4,'name':'Small van',"
            + "'max_volume':10.5,'max_weight':1200,'max_items_number':80}}");
    private static final String STATUS_TRUE = json("{'status':true}");

    @Rule
    public final MockHttp http = new MockHttp();

    private VehiclesManagerV5 manager() {
        return http.install(new VehiclesManagerV5(MockHttp.API_KEY));
    }

    private static VehicleV5 vehicle() {
        VehicleV5 vehicle = new VehicleV5();
        vehicle.setVehicleAlias("Van 1");
        vehicle.setVehicleMake("Ford");
        vehicle.setVehicleModel("Transit");
        vehicle.setVehicleModelYear(2023);
        vehicle.setVehicleLicensePlate("EX-123");
        return vehicle;
    }

    private static VehicleProfile profile() {
        VehicleProfile profile = new VehicleProfile();
        profile.setName("Box truck");
        profile.setHeight(3.5);
        profile.setWeight(7500.0);
        return profile;
    }

    private static VehicleCapacityProfile capacity() {
        VehicleCapacityProfile capacity = new VehicleCapacityProfile();
        capacity.setName("Small van");
        capacity.setMaxWeight(1200.0);
        capacity.setMaxItemsNumber(80);
        return capacity;
    }

    // ------------------------------------------------------------------ vehicles

    @Test
    public void getVehiclesSendsPaging() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, "[" + VEHICLE + "]");

        VehicleV5[] vehicles = manager.getVehicles(2, 25);
        manager.getVehicles(null, null);

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP).assertApiKeyInQuery()
                .assertQuery("page", "2").assertQuery("per_page", "25").assertNoBody();
        http.request(1).assertNoQuery("page").assertNoQuery("per_page");
        assertEquals("Van 1", vehicles[0].getVehicleAlias());
        assertEquals(18.5, vehicles[0].getMpgCity(), 0.0);
    }

    @Test
    public void createGetAndDeleteVehicle() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, VEHICLE);

        VehicleV5 created = manager.createVehicle(vehicle());
        manager.getVehicleById("VEH-1");
        manager.deleteVehicle("VEH-1");

        http.request(0).assertEndpoint("POST", HOST, VEHICLES_EP)
                .assertJson("vehicle_alias", "Van 1")
                .assertJson("vehicle_make", "Ford")
                .assertJson("vehicle_model_year", 2023)
                .assertJson("vehicle_license_plate", "EX-123");
        http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP + "/VEH-1");
        http.request(2).assertEndpoint("DELETE", HOST, VEHICLES_EP + "/VEH-1").assertNoBody();
        assertEquals("VEH-1", created.getVehicleId());
        assertEquals(Long.valueOf(1001), created.getMemberId());
    }

    @Ignore("bug (unfiled): V5 JSON bodies are sent as text/plain, without Content-Type application/json")
    @Test
    public void createVehicleSendsJsonContentType() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, VEHICLE);

        manager.createVehicle(vehicle());

        http.request().assertContentType("application/json");
    }

    @Ignore("registry: PUT /modules/api/v5.0/vehicles/{} is not in endpoints.json (only GET and DELETE are)")
    @Test
    public void updateVehiclePutsTheVehicle() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, VEHICLE);

        manager.updateVehicle("VEH-1", vehicle());

        http.request().assertEndpoint("PUT", HOST, VEHICLES_EP + "/VEH-1").assertJson("vehicle_alias", "Van 1");
    }

    @Test
    public void searchAndLicensePlateLookups() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "[" + VEHICLE + "]").respond(200, "{\"data\":" + VEHICLE + "}");

        VehicleV5[] found = manager.searchVehicles("Van");
        VehicleResponse byPlate = manager.getVehicleByLicensePlate("EX-123");

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/search").assertQuery("search", "Van");
        http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP + "/license")
                .assertQuery("vehicle_license_plate", "EX-123");
        assertEquals(1, found.length);
        assertEquals("Transit", byPlate.getData().getVehicleModel());
    }

    @Test
    public void getVehicleLocationsRepeatsTheIdsParameter() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, json("{'data':[{'vehicle_id':'VEH-1','lat':40.5,'lng':-74.5,"
                + "'last_position_received':'2026-10-21 10:00:00'}]}"));

        VehicleLocationResponse locations = manager.getVehicleLocations(new String[] {"VEH-1", "VEH-2"});
        manager.getVehicleLocations(null);

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/location")
                .assertQueryValues("ids[]", "VEH-1", "VEH-2");
        http.request(1).assertNoQuery("ids[]");
        assertEquals(40.5, locations.getData().get(0).getLat(), 0.0);
    }

    // ------------------------------------------------------------------ bulk operations

    @Test
    public void bulkOperationsPostTheVehicleIds() throws Exception {
        VehiclesManagerV5 manager = manager();
        String[] ids = {"VEH-1", "VEH-2"};
        http.respond(200, "{\"data\":[" + VEHICLE + "]}").respond(200, "[" + VEHICLE + "]")
                .respond(200, STATUS_TRUE).respond(200, STATUS_TRUE);

        VehiclesResults activated = manager.activateVehicles(ids);
        VehicleV5[] deactivated = manager.deactivateVehicles(ids);
        StatusResponse deleted = manager.deleteVehicles(ids);
        StatusResponse restored = manager.restoreVehicles(ids);

        String[] paths = {"/bulk/activate", "/bulk/deactivate", "/bulk/delete", "/bulk/restore"};
        for (int i = 0; i < paths.length; i++) {
            http.request(i).assertEndpoint("POST", HOST, VEHICLES_EP + paths[i])
                    .assertJson("vehicle_ids[0]", "VEH-1")
                    .assertJson("vehicle_ids[1]", "VEH-2");
        }
        assertEquals("VEH-1", activated.getData()[0].getVehicleId());
        assertEquals(1, deactivated.length);
        assertTrue(deleted.getStatus());
        assertTrue(restored.getStatus());
    }

    @Ignore("registry: /modules/api/v5.0/vehicles/job-tracker/{result,status}/{} are not in endpoints.json "
            + "(the probe found them alive)")
    @Test
    public void jobTrackerLookupsPutTheJobIdInThePath() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, STATUS_TRUE);

        manager.getVehicleJobResult("JOB-1");
        manager.getVehicleJobStatus("JOB-1");

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/job-tracker/result/JOB-1");
        http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP + "/job-tracker/status/JOB-1");
    }

    // ------------------------------------------------------------------ vehicle profiles

    @Test
    public void vehicleProfilesListGetCreateAndDelete() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "{\"data\":[" + PROFILE + "]}").respond(200, json("{'data':[]}"))
                .respond(200, PROFILE).respond(200, PROFILE).respond(200, PROFILE);

        VehicleProfilesResponse profiles = manager.getVehicleProfiles(1, 10);
        manager.getVehicleProfiles(null, null);
        VehicleProfile fetched = manager.getVehicleProfileById("3");
        manager.createVehicleProfile(profile());
        manager.deleteVehicleProfile("3");

        http.request(0).assertEndpoint("GET", HOST, PROFILES_EP).assertQuery("page", "1").assertQuery("per_page", "10");
        http.request(1).assertNoQuery("page");
        http.request(2).assertEndpoint("GET", HOST, PROFILES_EP + "/3");
        http.request(3).assertEndpoint("POST", HOST, PROFILES_EP)
                .assertJson("name", "Box truck").assertJson("height", 3.5).assertJson("weight", 7500.0);
        http.request(4).assertEndpoint("DELETE", HOST, PROFILES_EP + "/3");
        assertEquals("Box truck", profiles.getData().get(0).getName());
        assertEquals(Integer.valueOf(3), fetched.getVehicleProfileId());
    }

    @Ignore("registry: PUT /modules/api/v5.0/vehicle-profiles/{} is not in endpoints.json (the registry has PATCH)")
    @Test
    public void updateVehicleProfileSendsTheProfile() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, PROFILE);

        manager.updateVehicleProfile("3", profile());

        http.request().assertEndpoint("PUT", HOST, PROFILES_EP + "/3").assertJson("name", "Box truck");
    }

    // ------------------------------------------------------------------ capacity profiles

    @Test
    public void capacityProfilesListGetCreateAndDelete() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, json("{'data':[{'vehicle_capacity_profile_id':4,'name':'Small van'}]}"))
                .respond(200, CAPACITY).respond(200, CAPACITY).respond(200, CAPACITY);

        VehicleCapacityProfilesResponse profiles = manager.getVehicleCapacityProfiles(1, 10);
        VehicleCapacityProfileResponse fetched = manager.getVehicleCapacityProfileById("4");
        manager.createVehicleCapacityProfile(capacity());
        manager.deleteVehicleCapacityProfile("4");

        http.request(0).assertEndpoint("GET", HOST, CAPACITY_EP).assertQuery("page", "1");
        http.request(1).assertEndpoint("GET", HOST, CAPACITY_EP + "/4");
        http.request(2).assertEndpoint("POST", HOST, CAPACITY_EP)
                .assertJson("name", "Small van").assertJson("max_weight", 1200.0).assertJson("max_items_number", 80);
        http.request(3).assertEndpoint("DELETE", HOST, CAPACITY_EP + "/4");
        assertEquals("Small van", profiles.getData().get(0).getName());
        assertEquals(10.5, fetched.getData().getMaxVolume(), 0.0);
    }

    @Ignore("registry: PUT /modules/api/v5.0/vehicle-capacity-profiles/{} is not in endpoints.json "
            + "(the registry has PATCH)")
    @Test
    public void updateVehicleCapacityProfileSendsTheProfile() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, CAPACITY);

        manager.updateVehicleCapacityProfile("4", capacity());

        http.request().assertEndpoint("PUT", HOST, CAPACITY_EP + "/4").assertJson("name", "Small van");
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        VehiclesManagerV5 manager = http.install(new VehiclesManagerV5(MockHttp.API_KEY, true));
        http.respond(200, VEHICLE);

        manager.getVehicleById("VEH-1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final VehiclesManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getVehicleById("VEH-1"));
    }
}
