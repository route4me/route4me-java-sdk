package com.route4me.sdk.services.routedestinations;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link RouteDestinationsManager}: route destinations, their list columns
 * and sequencing on the V5 API.
 */
public class RouteDestinationsManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String DESTINATIONS_EP = "/modules/api/v5.0/route-destinations";
    /** A stop ID above Integer.MAX_VALUE: the API's IDs are 64-bit. */
    private static final long LARGE_ID = 3000000002L;
    private static final String DESTINATION = json("{'route_destination_id':3000000002,"
            + "'destination_name':'Example Bakery','notes':[{'lat':40.1234567,'lng':-74.7654321}],"
            + "'service_time':'300','is_depot':'0','customer_id':'CUST-1','facilities':[{'facility_id':7,"
            + "'facility_alias':'North hub','facility_types':[{'facility_type_id':1,'facility_type_alias':'Hub'}]}]}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RouteDestinationsManager manager() {
        return http.install(new RouteDestinationsManager(MockHttp.API_KEY));
    }

    /** Returns a list request with the filters the API requires. */
    private static GetDestinationsRequest listRequest() {
        DestinationFilters filters = new DestinationFilters();
        filters.setRouteId("ROUTE-A1");
        GetDestinationsRequest request = new GetDestinationsRequest();
        request.setFilters(filters);
        return request;
    }

    @Test
    public void getDestinationPutsTheIdInThePath() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, DESTINATION);

        RouteDestinationResource destination = manager.getDestination("5002");

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/5002").assertApiKeyAsBearer().assertNoBody();
        assertEquals("Example Bakery", destination.getDestinationName());
        assertEquals("CUST-1", destination.getCustomerId());
        assertEquals("Hub", destination.getFacilities().get(0).getFacilityTypes().get(0).getFacilityTypeAlias());
        assertEquals("7", destination.getFacilities().get(0).getFacilityId());
    }

    @Test
    public void facilitiesAreReadInTheShapesOfTheSpecAndOfTheFacilitiesApi() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'route_destination_id':5002,'facilities':["
                + "{'facility_id':101,'status_code':'A','coordinates':'49.8397, 24.0297'},"
                + "{'facility_id':'C0FFEE0000000000000000000000000A','status_code':1,"
                + "'coordinates':{'lat':40.1,'lng':-74.1}},"
                + "{'facility_id':102,'coordinates':'unknown'},"
                + "{'facility_id':103,'coordinates':[]},"
                + "{'facility_id':104,'coordinates':null}]}"));

        RouteDestinationResource destination = manager.getDestination("5002");

        FacilityDataResource spec = destination.getFacilities().get(0);
        assertEquals("101", spec.getFacilityId());
        assertEquals("A", spec.getStatusCode());
        assertEquals(49.8397, spec.getCoordinates().getLat(), 0.0);
        assertEquals(24.0297, spec.getCoordinates().getLng(), 0.0);
        FacilityDataResource facilitiesApi = destination.getFacilities().get(1);
        assertEquals("C0FFEE0000000000000000000000000A", facilitiesApi.getFacilityId());
        assertEquals("1", facilitiesApi.getStatusCode());
        assertEquals(40.1, facilitiesApi.getCoordinates().getLat(), 0.0);
        assertEquals(-74.1, facilitiesApi.getCoordinates().getLng(), 0.0);
        assertNull("text that is not lat,lng", destination.getFacilities().get(2).getCoordinates());
        assertNull("PHP sends an empty map as []", destination.getFacilities().get(3).getCoordinates());
        assertNull(destination.getFacilities().get(4).getCoordinates());
    }

    /** No source gives the order of a coordinates array, so it is not guessed: GeoJSON's is [lng, lat]. */
    @Test
    public void facilityCoordinatesInAnArrayAreReadAsNull() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'route_destination_id':5002,'facilities':["
                + "{'facility_id':101,'coordinates':[49.8397,24.0297]},"
                + "{'facility_id':102,'coordinates':[24.0297,49.8397]}]}"));

        RouteDestinationResource destination = manager.getDestination("5002");

        assertEquals(5002L, destination.getRouteDestinationId().longValue());
        assertEquals("102", destination.getFacilities().get(1).getFacilityId());
        assertNull("[lat, lng] is not guessed", destination.getFacilities().get(0).getCoordinates());
        assertNull("[lng, lat] is not guessed", destination.getFacilities().get(1).getCoordinates());
    }

    @Test
    public void facilityCoordinatesOfAnotherShapeAreReadAsNull() throws Exception {
        RouteDestinationsManager manager = manager();
        String[] shapes = {"false", "true", "0", "49.8", "[49.8]", "[49.8,24.0,1]", "['49.8','24.0']", "[[1,2],[3,4]]",
            "{'lat':'north','lng':24.0}", "{'lat':{},'lng':24.0}"};
        StringBuilder facilities = new StringBuilder();
        for (String shape : shapes) {
            facilities.append(facilities.length() == 0 ? "" : ",").append("{'facility_id':101,'coordinates':")
                    .append(shape).append('}');
        }
        http.respond(200, json("{'route_destination_id':5002,'facilities':[" + facilities + "]}"));

        RouteDestinationResource destination = manager.getDestination("5002");

        assertEquals("one odd facility does not fail the reply", 5002L,
                destination.getRouteDestinationId().longValue());
        for (int i = 0; i < shapes.length; i++) {
            assertEquals(shapes[i], "101", destination.getFacilities().get(i).getFacilityId());
            assertNull(shapes[i], destination.getFacilities().get(i).getCoordinates());
        }
    }

    @Test
    public void workflowIdIsReadAsTextFromAStringOrANumber() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'items':[{'route_destination_id':5002,"
                + "'workflow_id':'C0FFEE0000000000000000000000000B'},"
                + "{'route_destination_id':5003,'workflow_id':789}]}"));

        RouteDestinationsListResponse list = manager.getDestinationsList(listRequest());

        assertEquals("C0FFEE0000000000000000000000000B", list.getItems().get(0).getWorkflowId());
        assertEquals("789", list.getItems().get(1).getWorkflowId());
    }

    @Test
    public void requiredSkillsAreReadUnderEitherName() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'items':[{'route_destination_id':5002,'required_skills':'Forklift, Driver'},"
                + "{'route_destination_id':5003,'skills':'Hazmat'}]}"));

        RouteDestinationsByOrderResponse response = manager.getDestinationsByOrder("ORDER-UUID-1");

        assertEquals("Forklift, Driver", response.getItems().get(0).getSkills());
        assertEquals("Hazmat", response.getItems().get(1).getSkills());
    }

    @Test
    public void getAndEditDestinationColumns() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'categories':[{'category':'Main','columns':[{'id':'name','title':'Name'}]}],"
                        + "'order':['name'],'default_order':['name','eta']}"))
                .respond(200, json("{'order':['eta','name']}"));
        EditDestinationColumnsRequest edit = new EditDestinationColumnsRequest();
        edit.setColumnsConfiguratorKey("route-view");
        edit.setOrder(Arrays.asList("eta", "name"));

        RouteDestinationColumnsResource columns = manager.getDestinationColumns("route-view");
        EditDestinationColumnsResponse edited = manager.editDestinationColumns(edit);

        http.request(0).assertEndpoint("GET", HOST, DESTINATIONS_EP + "/columns")
                .assertQuery("columns_configurator_key", "route-view").assertNoBody();
        http.request(1).assertEndpoint("PUT", HOST, DESTINATIONS_EP + "/columns")
                .assertJson("columns_configurator_key", "route-view")
                .assertJson("order[0]", "eta");
        assertEquals("Name", columns.getCategories().get(0).getColumns().get(0).getTitle());
        assertEquals(Arrays.asList("name", "eta"), columns.getDefaultOrder());
        assertEquals(Arrays.asList("eta", "name"), edited.getOrder());
    }

    @Test
    public void getDestinationsListPostsTheFilters() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, "{\"items\":[" + DESTINATION + "]}");
        DestinationFilters filters = new DestinationFilters();
        filters.setRouteId("ROUTE-A1");
        filters.setStopStatusId(Collections.singletonList("1"));
        GetDestinationsRequest request = new GetDestinationsRequest();
        request.setFilters(filters);
        request.setPage(1);
        request.setPerPage(20);
        request.setOrderBy(Collections.singletonList(new String[] {"sequence_no", "asc"}));
        request.setFields(Arrays.asList("route_destination_id", "destination_name"));
        request.setNotFilters(Collections.<String, Object>singletonMap("address_stop_type",
                Collections.singletonList("BREAK")));
        request.setRoute(Collections.<String, Object>singletonMap("query", "Morning"));

        RouteDestinationsListResponse list = manager.getDestinationsList(request);

        http.request().assertEndpoint("POST", HOST, DESTINATIONS_EP + "/list")
                .assertJson("filters.route_id", "ROUTE-A1")
                .assertJson("filters.stop_status_id[0]", "1")
                .assertJson("page", 1)
                .assertJson("per_page", 20)
                .assertJson("order_by[0][0]", "sequence_no")
                .assertJson("fields[1]", "destination_name")
                .assertJson("not_filters.address_stop_type[0]", "BREAK")
                .assertJson("route.query", "Morning");
        assertEquals(Long.valueOf(LARGE_ID), list.getItems().get(0).getRouteDestinationId());
        assertEquals(40.1234567, list.getItems().get(0).getNotes().get(0).getLatitude(), 0.0);
        assertEquals(-74.7654321, list.getItems().get(0).getNotes().get(0).getLongitude(), 0.0);
    }

    @Test
    public void getDestinationsListReadsThePaging() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, "{\"items\":[" + DESTINATION + "],\"current_page_index\":2,\"total_items_count\":41}");

        RouteDestinationsListResponse list = manager.getDestinationsList(listRequest());

        assertEquals(Integer.valueOf(2), list.getCurrentPageIndex());
        assertEquals(Integer.valueOf(41), list.getTotalItemsCount());
        assertEquals(1, list.getItems().size());
    }

    @Test
    public void getDestinationFieldsListsTheFields() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200,
                json("{'fields':[{'label':'Name','value':'destination_name','kind':'text','type':'string'}]}"));

        RouteDestinationFieldsResponse fields = manager.getDestinationFields();

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/list/fields");
        assertEquals("destination_name", fields.getFields().get(0).getValue());
    }

    @Test
    public void getDestinationByLabelCodeSendsTheCode() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'destination_id':3000000002,'route_id':'ROUTE-A1','label_code':'LBL-1',"
                + "'sequence_no':3}"));

        SortingFilteringResource sorting = manager.getDestinationByLabelCode("LBL-1");

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/sorting").assertQuery("label_code", "LBL-1");
        assertEquals(Integer.valueOf(3), sorting.getSequenceNo());
        assertEquals(Long.valueOf(LARGE_ID), sorting.getDestinationId());
    }

    @Test
    public void sortingResourceReadsA64BitMemberIdAndScheduledFor() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'destination_id':5002,'member_id':3000000003,'scheduled_for':3000000004}"));

        SortingFilteringResource sorting = manager.getDestinationByLabelCode("LBL-1");

        assertEquals(Long.valueOf(3000000003L), sorting.getMemberId());
        assertEquals(Long.valueOf(3000000004L), sorting.getScheduledFor());
    }

    @Test
    public void getDestinationsByOrderPutsTheUuidInThePath() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, "{\"items\":[" + DESTINATION + "]}");

        RouteDestinationsByOrderResponse response = manager.getDestinationsByOrder("ORDER-UUID-1");

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/order/ORDER-UUID-1");
        assertEquals("Example Bakery", response.getItems().get(0).getDestinationName());
    }

    @Test
    public void getDestinationSequencePostsTheCoordinates() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, json("{'optimized_sequence':[1,0]}"));
        SequenceCoordinate first = new SequenceCoordinate();
        first.setLat(40.1234567);
        first.setLng(-74.7654321);
        DestinationSequenceRequest request = new DestinationSequenceRequest();
        request.setCoordinates(Arrays.asList(first, new SequenceCoordinate()));

        DestinationSequenceResponse response = manager.getDestinationSequence(request);

        http.request().assertEndpoint("POST", HOST, DESTINATIONS_EP + "/sequence")
                .assertJson("coordinates[0].lat", 40.1234567)
                .assertJson("coordinates[0].lng", -74.7654321);
        assertArrayEquals(new int[] {1, 0}, response.getOptimizedSequence());
    }

    @Test
    public void requiredValuesAreCheckedBeforeSending() {
        final RouteDestinationsManager manager = manager();
        final EditDestinationColumnsRequest noKey = new EditDestinationColumnsRequest();
        noKey.setOrder(Collections.singletonList("eta"));
        final EditDestinationColumnsRequest noOrder = new EditDestinationColumnsRequest();
        noOrder.setColumnsConfiguratorKey("route-view");

        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationColumns(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationColumns(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationByLabelCode(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationByLabelCode(""));
        http.expectThrows(IllegalArgumentException.class, () -> manager.editDestinationColumns(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.editDestinationColumns(noKey));
        http.expectThrows(IllegalArgumentException.class, () -> manager.editDestinationColumns(noOrder));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationsList(null));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getDestinationsList(new GetDestinationsRequest()));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationSequence(null));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.getDestinationSequence(new DestinationSequenceRequest()));
        http.assertNoRequests();
    }

    @Test
    public void emptyPathIdsAreRejectedBeforeSending() {
        final RouteDestinationsManager manager = manager();

        for (final String id : Arrays.asList(null, "")) {
            http.expectThrows(IllegalArgumentException.class, () -> manager.getDestination(id));
            http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationsByOrder(id));
        }
        http.assertNoRequests();
    }

    @Test
    public void pathIdsWithASlashAreRefused() throws Exception {
        final RouteDestinationsManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationsByOrder("../secret"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getDestinationsByOrder("..\\secret"));
        http.assertNoRequests();

        http.respondToAll(200, "{\"items\":[]}");
        manager.getDestinationsByOrder("a b");

        assertEquals(DESTINATIONS_EP + "/order/a%20b", http.request().getUri().getRawPath());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        RouteDestinationsManager manager = http.install(new RouteDestinationsManager(MockHttp.API_KEY, true));
        http.respond(200, DESTINATION);

        manager.getDestination("5002");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final RouteDestinationsManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getDestination("5002"));
    }
}
