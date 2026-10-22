package com.route4me.sdk.services.routedestinations;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void getDestinationPutsTheIdInThePath() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respond(200, DESTINATION);

        RouteDestinationResource destination = manager.getDestination("5002");

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/5002").assertApiKeyAsBearer().assertNoBody();
        assertEquals("Example Bakery", destination.getDestinationName());
        assertEquals("CUST-1", destination.getCustomerId());
        assertEquals("Hub", destination.getFacilities().get(0).getFacilityTypes().get(0).getFacilityTypeAlias());
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
    public void pathIdsWithASlashStayInOneSegment() throws Exception {
        RouteDestinationsManager manager = manager();
        http.respondToAll(200, "{\"items\":[]}");

        manager.getDestinationsByOrder("../secret");

        assertEquals(DESTINATIONS_EP + "/order/..%2Fsecret", http.request().getUri().getRawPath());
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
