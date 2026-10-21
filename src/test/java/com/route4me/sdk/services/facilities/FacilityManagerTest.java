package com.route4me.sdk.services.facilities;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FacilityManager}: facilities and facility types on the V5 API.
 *
 * <p>The registry has {@code /facilities} and {@code /facilities/types} but not the
 * {@code /{id}} forms, although the unauthenticated probe found them alive (401). The
 * tests for those methods are ignored with a {@code registry:} reason until the registry
 * is updated.
 */
public class FacilityManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String FACILITIES_EP = "/modules/api/v5.0/facilities";
    private static final String FACILITY = json("{'facility_id':'FAC-1','root_member_id':1001,"
            + "'facility_alias':'North hub','address':'100 Example St','coordinates':{'lat':40.5,'lng':-74.5},"
            + "'status':1,'facility_types':[{'facility_type_id':1,'facility_type_alias':'Hub','is_default':true}]}");
    private static final String REGISTRY_ID =
            "registry: /modules/api/v5.0/facilities/{} is not in endpoints.json (the probe found it alive)";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private FacilityManager manager() {
        return http.install(new FacilityManager(MockHttp.API_KEY));
    }

    /** Builds a sample facility create request with fake data. */
    private static FacilityCreateRequest createRequest() {
        FacilityCoordinates coordinates = new FacilityCoordinates();
        coordinates.setLat(40.5);
        coordinates.setLng(-74.5);
        FacilityTypeAssignment type = new FacilityTypeAssignment();
        type.setFacilityTypeId(1);
        type.setIsDefault(true);
        FacilityCreateRequest request = new FacilityCreateRequest();
        request.setFacilityAlias("North hub");
        request.setAddress("100 Example St");
        request.setCoordinates(coordinates);
        request.setFacilityTypes(Collections.singletonList(type));
        request.setStatus(1);
        return request;
    }

    @Test
    public void getFacilitiesSendsOnlyTheGivenFilters() throws Exception {
        FacilityManager manager = manager();
        http.respondToAll(200, "{\"data\":[" + FACILITY + "],\"current_page\":2,\"total\":11,\"per_page\":10}");

        FacilitiesPaginateResource page = manager.getFacilities(2, 10, 1, 3, "hub");
        manager.getFacilities(null, null, null, null, null);

        http.request(0).assertEndpoint("GET", HOST, FACILITIES_EP).assertApiKeyInQuery()
                .assertQuery("page", "2")
                .assertQuery("per_page", "10")
                .assertQuery("status", "1")
                .assertQuery("facility_type_id", "3")
                .assertQuery("search_query", "hub");
        http.request(1).assertNoQuery("page").assertNoQuery("search_query");
        assertEquals("North hub", page.getData().get(0).getFacilityAlias());
        assertEquals(Integer.valueOf(11), page.getTotal());
    }

    @Test
    public void createFacilityPostsTheFacility() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, FACILITY);

        FacilityResource created = manager.createFacility(createRequest());

        http.request().assertEndpoint("POST", HOST, FACILITIES_EP)
                .assertJson("facility_alias", "North hub")
                .assertJson("address", "100 Example St")
                .assertJson("coordinates.lat", 40.5)
                .assertJson("facility_types[0].facility_type_id", 1)
                .assertJson("facility_types[0].is_default", true)
                .assertJson("status", 1);
        assertEquals("FAC-1", created.getFacilityId());
        assertEquals(-74.5, created.getCoordinates().getLng(), 0.0);
    }

    @Test
    public void getFacilityTypesListsTheTypes() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, json("{'data':[{'facility_type_id':1,'facility_type_alias':'Hub'}]}"));

        FacilityTypeCollectionResource types = manager.getFacilityTypes();

        http.request().assertEndpoint("GET", HOST, FACILITIES_EP + "/types");
        assertEquals("Hub", types.getData().get(0).getFacilityTypeAlias());
    }

    @Ignore(REGISTRY_ID)
    @Test
    public void getFacilityPutsTheIdInThePath() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, FACILITY);

        FacilityResource facility = manager.getFacility("FAC-1");

        http.request().assertEndpoint("GET", HOST, FACILITIES_EP + "/FAC-1");
        assertEquals("Hub", facility.getFacilityTypes().get(0).getFacilityTypeAlias());
    }

    @Ignore(REGISTRY_ID)
    @Test
    public void updateFacilityPutsTheFacility() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, FACILITY);
        FacilityUpdateRequest request = new FacilityUpdateRequest();
        request.setFacilityAlias("South hub");

        manager.updateFacility("FAC-1", request);

        http.request().assertEndpoint("PUT", HOST, FACILITIES_EP + "/FAC-1").assertJson("facility_alias", "South hub");
    }

    @Ignore(REGISTRY_ID)
    @Test
    public void deleteFacilityReturnsTheDeletedFacilities() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, "[" + FACILITY + "]");

        FacilityResource[] deleted = manager.deleteFacility("FAC-1");

        http.request().assertEndpoint("DELETE", HOST, FACILITIES_EP + "/FAC-1").assertNoBody();
        assertEquals("FAC-1", deleted[0].getFacilityId());
    }

    @Ignore("registry: /modules/api/v5.0/facilities/types/{} is not in endpoints.json (the probe found it alive)")
    @Test
    public void getFacilityTypePutsTheIdInThePath() throws Exception {
        FacilityManager manager = manager();
        http.respond(200, json("{'facility_type_id':1,'facility_type_alias':'Hub'}"));

        FacilityTypeResource type = manager.getFacilityType(1);

        http.request().assertEndpoint("GET", HOST, FACILITIES_EP + "/types/1");
        assertEquals(Integer.valueOf(1), type.getFacilityTypeId());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        FacilityManager manager = http.install(new FacilityManager(MockHttp.API_KEY, true));
        http.respond(200, json("{'data':[]}"));

        manager.getFacilityTypes();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final FacilityManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.createFacility(createRequest()));
    }
}
