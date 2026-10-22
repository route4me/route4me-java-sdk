package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link VehiclesManagerV5}: vehicles, bulk operations, the job tracker, vehicle profiles and
 * capacity profiles on the V5 API.
 */
public class VehiclesManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String VEHICLES_EP = "/modules/api/v5.0/vehicles";
    private static final String PROFILES_EP = "/modules/api/v5.0/vehicle-profiles";
    private static final String CAPACITY_EP = "/modules/api/v5.0/vehicle-capacity-profiles";
    private static final String VEHICLE_ID = "C0FFEE0123456789ABCDEF0123456789";
    private static final String OTHER_VEHICLE_ID = "C0FFEE9876543210FEDCBA9876543210";
    private static final String VEHICLE = json("{'vehicle_id':'" + VEHICLE_ID + "','member_id':1001,"
            + "'vehicle_alias':'Van 1','vehicle_make':'Ford','vehicle_model':'Transit','vehicle_model_year':2023,"
            + "'vehicle_license_plate':'EX-123','vehicle_capacity_profile_id':4,"
            + "'fuel_type':'diesel','mpg_city':18.5}");
    private static final String PROFILE = json("{'vehicle_profile_id':3,'name':'Box truck','height':3.5,"
            + "'width':2.4,'length':7.0,'weight':7500,'is_default':false}");
    private static final String CAPACITY = json("{'data':{'vehicle_capacity_profile_id':4,'name':'Small van',"
            + "'max_volume':10.5,'max_weight':1200,'max_items_number':80}}");
    private static final String JOB = json("{'job_id':'JOB-1','status':'processing'}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private VehiclesManagerV5 manager() {
        return http.install(new VehiclesManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample vehicle with fake data. */
    private static VehicleV5 vehicle() {
        VehicleV5 vehicle = new VehicleV5();
        vehicle.setVehicleAlias("Van 1");
        vehicle.setVehicleMake("Ford");
        vehicle.setVehicleModel("Transit");
        vehicle.setVehicleModelYear(2023);
        vehicle.setVehicleLicensePlate("EX-123");
        return vehicle;
    }

    /** Builds a sample vehicle profile with fake data. */
    private static VehicleProfile profile() {
        VehicleProfile profile = new VehicleProfile();
        profile.setName("Box truck");
        profile.setHeight(3.5);
        profile.setWeight(7500.0);
        return profile;
    }

    /** Builds a sample vehicle capacity profile with fake data. */
    private static VehicleCapacityProfile capacity() {
        VehicleCapacityProfile capacity = new VehicleCapacityProfile();
        capacity.setName("Small van");
        capacity.setMaxWeight(1200.0);
        capacity.setMaxItemsNumber(80);
        return capacity;
    }

    // ------------------------------------------------------------------ vehicles

    @Test
    public void getVehiclesSendsTheFilters() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, "[" + VEHICLE + "]");
        VehicleListRequest filters = new VehicleListRequest().setShow("deleted").setSearchQuery("Van")
                .setQuery("make:Ford").setIsOperational(true).setOnlyWithProfile(false).setWithMultiUnitFields(true)
                .setTelematicsConnectionIds(Arrays.asList(7L, 8L))
                .setFacilityIds(Collections.singletonList("C0FFEE00000000000000000000000001"));

        VehicleV5[] vehicles = manager.getVehicles(filters);
        manager.getVehicles(null);

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP).assertApiKeyAsBearer().assertNoBody()
                .assertQuery("show", "deleted").assertQuery("search_query", "Van").assertQuery("query", "make:Ford")
                .assertQuery("is_operational", "1").assertQuery("only_with_profile", "0")
                .assertQuery("with_multi_unit_fields", "1")
                .assertQueryValues("telematics_connection_ids[]", "7", "8")
                .assertQueryValues("facility_ids[]", "C0FFEE00000000000000000000000001")
                .assertNoQuery("page").assertNoQuery("per_page");
        CapturedRequest unfiltered = http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP).assertNoBody();
        assertTrue(unfiltered.getQueryParameters().isEmpty());
        assertEquals("Van 1", vehicles[0].getVehicleAlias());
        assertEquals(18.5, vehicles[0].getMpgCity(), 0.0);
    }

    @Test
    public void getVehiclesByStateSendsShowOnly() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "[]");

        VehicleV5[] vehicles = manager.getVehicles(new VehicleListRequest().setShow("pending"));

        CapturedRequest request = http.request().assertEndpoint("GET", HOST, VEHICLES_EP)
                .assertQuery("show", "pending");
        assertEquals(1, request.getQueryParameters().size());
        assertEquals(0, vehicles.length);
    }

    @Test
    public void createGetAndDeleteVehicle() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, VEHICLE);

        VehicleV5 created = manager.createVehicle(vehicle());
        manager.getVehicleById(VEHICLE_ID);
        manager.deleteVehicle(VEHICLE_ID);

        http.request(0).assertEndpoint("POST", HOST, VEHICLES_EP).assertNoQuery("vehicle_alias")
                .assertJson("vehicle_alias", "Van 1")
                .assertJson("vehicle_make", "Ford")
                .assertJson("vehicle_model_year", 2023)
                .assertJson("vehicle_license_plate", "EX-123")
                .assertJsonLacks("vehicle_id");
        http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP + "/" + VEHICLE_ID).assertNoBody();
        http.request(2).assertEndpoint("DELETE", HOST, VEHICLES_EP + "/" + VEHICLE_ID).assertNoBody();
        assertEquals(VEHICLE_ID, created.getVehicleId());
        assertEquals(Long.valueOf(1001), created.getMemberId());
    }

    @Test
    public void createVehicleSendsJsonContentType() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, VEHICLE);

        manager.createVehicle(vehicle());

        http.request().assertContentType("application/json; charset=UTF-8");
    }

    @Test
    public void createVehicleRequiresTheAlias() {
        final VehiclesManagerV5 manager = manager();
        final VehicleV5 noAlias = new VehicleV5();
        noAlias.setVehicleMake("Ford");
        final VehicleV5 emptyAlias = new VehicleV5();
        emptyAlias.setVehicleAlias("");

        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicle(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicle(noAlias));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicle(emptyAlias));
        http.assertNoRequests();
    }

    @Test
    public void updateVehiclePatchesTheVehicle() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, VEHICLE);
        VehicleV5 change = new VehicleV5();
        change.setVehicleAlias("Van 2");

        VehicleV5 updated = manager.updateVehicle(VEHICLE_ID, change);

        http.request().assertEndpoint("PATCH", HOST, VEHICLES_EP + "/" + VEHICLE_ID)
                .assertContentType("application/json; charset=UTF-8")
                .assertJson("vehicle_alias", "Van 2").assertJsonLacks("vehicle_make", "vehicle_id");
        assertEquals(VEHICLE_ID, updated.getVehicleId());
    }

    @Test
    public void searchVehiclesPostsTheIdsAndTheLocation() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "[{\"vehicle\":" + VEHICLE + json(",'vehicleLocation':{'lat':40.5,'lng':-74.5},"
                + "'distance':12.5}]"));
        VehicleSearchRequest request = new VehicleSearchRequest()
                .setVehicleIds(Arrays.asList(VEHICLE_ID, OTHER_VEHICLE_ID)).setLat(40.75).setLng(-73.5);

        VehicleSearchResult[] found = manager.searchVehicles(request);

        http.request().assertEndpoint("POST", HOST, VEHICLES_EP + "/search")
                .assertNoQuery("vehicle_ids[]").assertNoQuery("lat")
                .assertJson("vehicle_ids[0]", VEHICLE_ID).assertJson("vehicle_ids[1]", OTHER_VEHICLE_ID)
                .assertJson("lat", 40.75).assertJson("lng", -73.5);
        assertEquals(1, found.length);
        assertEquals("Transit", found[0].getVehicle().getVehicleModel());
        assertEquals(40.5, found[0].getVehicleLocation().getLat(), 0.0);
        assertEquals(12.5, found[0].getDistance(), 0.0);
    }

    @Test
    public void searchVehiclesRequiresEveryField() {
        final VehiclesManagerV5 manager = manager();
        final List<String> ids = Collections.singletonList(VEHICLE_ID);

        http.expectThrows(IllegalArgumentException.class, () -> manager.searchVehicles(null));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.searchVehicles(new VehicleSearchRequest().setLat(1.0).setLng(2.0)));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.searchVehicles(new VehicleSearchRequest().setVehicleIds(ids).setLng(2.0)));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.searchVehicles(new VehicleSearchRequest().setVehicleIds(ids).setLat(1.0)));
        http.assertNoRequests();
    }

    @Test
    public void getVehicleByLicensePlateReadsDataVehicle() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "{\"data\":{\"vehicle\":" + VEHICLE + "}}");

        VehicleResponse byPlate = manager.getVehicleByLicensePlate("EX-123");

        http.request().assertEndpoint("GET", HOST, VEHICLES_EP + "/license").assertNoBody()
                .assertQuery("vehicle_license_plate", "EX-123");
        assertEquals("Transit", byPlate.getData().getVehicle().getVehicleModel());
    }

    @Test
    public void getVehicleByLicensePlateRequiresThePlate() {
        final VehiclesManagerV5 manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.getVehicleByLicensePlate(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getVehicleByLicensePlate(""));
        http.assertNoRequests();
    }

    @Test
    public void getVehicleLocationsRepeatsTheIdsParameter() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(200, json("{'data':[{'vehicle_id':'VEH-1','lat':40.5,'lng':-74.5,"
                + "'last_position_received':'2026-10-21 10:00:00'}]}"));

        VehicleLocationResponse locations = manager.getVehicleLocations(new String[] {"VEH-1", "VEH-2"});
        manager.getVehicleLocations(null);
        manager.getVehicleLocations(new String[] {"VEH-3"});

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/location")
                .assertQueryValues("ids[]", "VEH-1", "VEH-2");
        http.request(1).assertNoQuery("ids[]");
        http.request(2).assertQueryValues("ids[]", "VEH-3").assertNoQuery("ids");
        assertEquals(40.5, locations.getData().get(0).getLat(), 0.0);
    }

    // ------------------------------------------------------------------ bulk operations and jobs

    @Test
    public void bulkOperationsPostTheVehicleIdsAndAcceptAnEmptyReply() throws Exception {
        VehiclesManagerV5 manager = manager();
        String[] ids = {VEHICLE_ID, OTHER_VEHICLE_ID};
        http.respondWithoutBody(202).respondWithoutBody(202).respondWithoutBody(202).respondWithoutBody(202);

        assertNull(manager.activateVehicles(ids));
        assertNull(manager.deactivateVehicles(ids));
        assertNull(manager.deleteVehicles(ids));
        assertNull(manager.restoreVehicles(ids));

        String[] paths = {"/bulk/activate", "/bulk/deactivate", "/bulk/delete", "/bulk/restore"};
        for (int i = 0; i < paths.length; i++) {
            http.request(i).assertEndpoint("POST", HOST, VEHICLES_EP + paths[i]).assertNoQuery("vehicle_ids[]")
                    .assertJson("vehicle_ids[0]", VEHICLE_ID)
                    .assertJson("vehicle_ids[1]", OTHER_VEHICLE_ID);
        }
    }

    @Test
    public void bulkOperationsReadAJobInTheBody() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respondToAll(202, JOB);
        String[] ids = {VEHICLE_ID};

        VehicleJobResponse[] jobs = {manager.activateVehicles(ids), manager.deactivateVehicles(ids),
            manager.deleteVehicles(ids), manager.restoreVehicles(ids)};

        for (VehicleJobResponse job : jobs) {
            assertEquals("JOB-1", job.getJobId());
            assertEquals("processing", job.getStatus());
        }
    }

    @Test
    public void bulkOperationsRequireTheIds() {
        final VehiclesManagerV5 manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.activateVehicles(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.deactivateVehicles(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.deleteVehicles(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.restoreVehicles(null));
        http.assertNoRequests();
    }

    @Test
    public void jobTrackerReadsTheStatusAndTheResult() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, json("{'status':'processing'}"))
                .respond(200, json("{'status':true,'result':{'updated':2,'ids':['A','B']}}"));

        VehicleJobResponse status = manager.getVehicleJobStatus("JOB-1");
        VehicleJobResponse result = manager.getVehicleJobResult("JOB-1");

        http.request(0).assertEndpoint("GET", HOST, VEHICLES_EP + "/job-tracker/status/JOB-1").assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, VEHICLES_EP + "/job-tracker/result/JOB-1").assertNoBody();
        assertEquals("processing", status.getStatus());
        assertNull(status.getResult());
        assertEquals("true", result.getStatus());
        Map<?, ?> payload = (Map<?, ?>) result.getResult();
        assertEquals(2.0, payload.get("updated"));
        assertEquals(Arrays.asList("A", "B"), payload.get("ids"));
    }

    @Test
    public void jobStatusWithoutRedirectsReadsTheFinalStatus() throws Exception {
        VehiclesManagerV5 manager = http.install(new VehiclesManagerV5(MockHttp.API_KEY, true));
        http.respond(303, json("{'status':'processed'}"));

        VehicleJobResponse status = manager.getVehicleJobStatus("JOB-1");

        http.request().assertEndpoint("GET", HOST, VEHICLES_EP + "/job-tracker/status/JOB-1")
                .assertQuery("redirect", "0");
        assertEquals("processed", status.getStatus());
    }

    // ------------------------------------------------------------------ vehicle profiles

    @Test
    public void vehicleProfilesListGetCreateAndDelete() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, "{\"data\":[" + PROFILE + json("],'current_page':1,'last_page':3,'per_page':10,"
                + "'from':1,'to':10,'total':25,'next_page_url':'https://example.com/vehicle-profiles?page=2'}"))
                .respond(200, json("{'data':[]}"))
                .respond(200, PROFILE).respond(200, PROFILE).respond(200, PROFILE);

        VehicleProfilesResponse profiles = manager.getVehicleProfiles(1, 10);
        manager.getVehicleProfiles(null, null);
        VehicleProfile fetched = manager.getVehicleProfileById("3");
        manager.createVehicleProfile(profile());
        manager.deleteVehicleProfile("3");

        http.request(0).assertEndpoint("GET", HOST, PROFILES_EP).assertNoBody().assertQuery("with_pagination", "1")
                .assertQuery("page", "1").assertQuery("per_page", "10");
        http.request(1).assertQuery("with_pagination", "1").assertNoQuery("page").assertNoQuery("per_page");
        http.request(2).assertEndpoint("GET", HOST, PROFILES_EP + "/3").assertNoBody();
        http.request(3).assertEndpoint("POST", HOST, PROFILES_EP)
                .assertJson("name", "Box truck").assertJson("height", 3.5).assertJson("weight", 7500.0);
        http.request(4).assertEndpoint("DELETE", HOST, PROFILES_EP + "/3").assertNoBody();
        assertEquals("Box truck", profiles.getData().get(0).getName());
        assertEquals(Integer.valueOf(25), profiles.getTotal());
        assertEquals(Integer.valueOf(3), profiles.getLastPage());
        assertEquals("https://example.com/vehicle-profiles?page=2", profiles.getNextPageUrl());
        assertEquals(Integer.valueOf(3), fetched.getVehicleProfileId());
    }

    @Test
    public void createVehicleProfileRequiresTheName() {
        final VehiclesManagerV5 manager = manager();
        final VehicleProfile noName = new VehicleProfile();
        noName.setHeight(3.5);

        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicleProfile(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicleProfile(noName));
        http.assertNoRequests();
    }

    @Test
    public void updateVehicleProfilePatchesTheProfile() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, PROFILE);

        VehicleProfile updated = manager.updateVehicleProfile("3", profile());

        http.request().assertEndpoint("PATCH", HOST, PROFILES_EP + "/3").assertJson("name", "Box truck")
                .assertJsonLacks("vehicle_profile_id");
        assertEquals("Box truck", updated.getName());
    }

    // ------------------------------------------------------------------ capacity profiles

    @Test
    public void capacityProfilesListGetCreateAndDelete() throws Exception {
        VehiclesManagerV5 manager = manager();
        String list = json("{'data':[{'vehicle_capacity_profile_id':4,'name':'Small van'}],"
                + "'meta':{'current_page':1,'last_page':1,'per_page':10,'total':1}}");
        http.respond(200, list).respond(200, list).respond(200, CAPACITY).respond(200, CAPACITY).respond(200, CAPACITY);

        VehicleCapacityProfilesResponse profiles = manager.getVehicleCapacityProfiles(1, 10);
        manager.getVehicleCapacityProfiles(null, null);
        VehicleCapacityProfileResponse fetched = manager.getVehicleCapacityProfileById("4");
        manager.createVehicleCapacityProfile(capacity());
        manager.deleteVehicleCapacityProfile("4");

        http.request(0).assertEndpoint("GET", HOST, CAPACITY_EP).assertNoBody().assertQuery("page", "1")
                .assertQuery("per_page", "10");
        http.request(1).assertNoQuery("page").assertNoQuery("per_page");
        http.request(2).assertEndpoint("GET", HOST, CAPACITY_EP + "/4").assertNoBody();
        http.request(3).assertEndpoint("POST", HOST, CAPACITY_EP)
                .assertJson("name", "Small van").assertJson("max_weight", 1200.0).assertJson("max_items_number", 80);
        http.request(4).assertEndpoint("DELETE", HOST, CAPACITY_EP + "/4").assertNoBody();
        assertEquals("Small van", profiles.getData().get(0).getName());
        assertEquals(Integer.valueOf(1), profiles.getMeta().getTotal());
        assertEquals(10.5, fetched.getData().getMaxVolume(), 0.0);
    }

    @Test
    public void createVehicleCapacityProfileRequiresTheName() {
        final VehiclesManagerV5 manager = manager();
        final VehicleCapacityProfile noName = new VehicleCapacityProfile();
        noName.setMaxWeight(10.0);

        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicleCapacityProfile(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createVehicleCapacityProfile(noName));
        http.assertNoRequests();
    }

    @Test
    public void updateVehicleCapacityProfilePatchesTheProfile() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, CAPACITY);

        VehicleCapacityProfileResponse updated = manager.updateVehicleCapacityProfile("4", capacity());

        http.request().assertEndpoint("PATCH", HOST, CAPACITY_EP + "/4").assertJson("name", "Small van")
                .assertJson("max_items_number", 80);
        assertEquals(Integer.valueOf(4), updated.getData().getVehicleCapacityProfileId());
    }

    // ------------------------------------------------------------------ path parameters

    @Test
    public void emptyPathIdsAreRejectedBeforeSending() {
        final VehiclesManagerV5 manager = manager();
        final VehicleV5 change = vehicle();

        for (final String id : Arrays.asList(null, "")) {
            http.expectThrows(IllegalArgumentException.class, () -> manager.updateVehicle(id, change));
            http.expectThrows(IllegalArgumentException.class, () -> manager.updateVehicleProfile(id, profile()));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.updateVehicleCapacityProfile(id, capacity()));
            http.expectThrows(IllegalArgumentException.class, () -> manager.getVehicleJobResult(id));
            http.expectThrows(IllegalArgumentException.class, () -> manager.getVehicleJobStatus(id));
        }
        http.assertNoRequests();
    }

    @Test
    public void slashInAnIdStaysInItsSegment() throws Exception {
        VehiclesManagerV5 manager = manager();
        http.respond(200, VEHICLE).respond(200, PROFILE).respond(200, CAPACITY).respond(200, json("{'status':true}"));

        manager.updateVehicle("../secret", vehicle());
        manager.updateVehicleProfile("../secret", profile());
        manager.updateVehicleCapacityProfile("../secret", capacity());
        manager.getVehicleJobResult("../secret");

        assertEquals(VEHICLES_EP + "/..%2Fsecret", http.request(0).getUri().getRawPath());
        assertEquals(PROFILES_EP + "/..%2Fsecret", http.request(1).getUri().getRawPath());
        assertEquals(CAPACITY_EP + "/..%2Fsecret", http.request(2).getUri().getRawPath());
        assertEquals(VEHICLES_EP + "/job-tracker/result/..%2Fsecret", http.request(3).getUri().getRawPath());
    }

    // ------------------------------------------------------------------ manager behaviour

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        VehiclesManagerV5 manager = http.install(new VehiclesManagerV5(MockHttp.API_KEY, true));
        http.respond(200, VEHICLE);

        manager.getVehicleById(VEHICLE_ID);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final VehiclesManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getVehicleById(VEHICLE_ID));
    }
}
