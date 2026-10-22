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
    private static final String DESTINATION = json("{'route_destination_id':5002,'destination_name':'Example Bakery',"
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
                .respond(200, json("{'categories':[]}"))
                .respond(200, json("{'order':['eta','name']}"));
        EditDestinationColumnsRequest edit = new EditDestinationColumnsRequest();
        edit.setColumnsConfiguratorKey("route-view");
        edit.setOrder(Arrays.asList("eta", "name"));

        RouteDestinationColumnsResource columns = manager.getDestinationColumns("route-view");
        manager.getDestinationColumns(null);
        EditDestinationColumnsResponse edited = manager.editDestinationColumns(edit);

        http.request(0).assertEndpoint("GET", HOST, DESTINATIONS_EP + "/columns")
                .assertQuery("columns_configurator_key", "route-view");
        http.request(1).assertNoQuery("columns_configurator_key");
        http.request(2).assertEndpoint("PUT", HOST, DESTINATIONS_EP + "/columns")
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

        RouteDestinationsListResponse list = manager.getDestinationsList(request);

        http.request().assertEndpoint("POST", HOST, DESTINATIONS_EP + "/list")
                .assertJson("filters.route_id", "ROUTE-A1")
                .assertJson("filters.stop_status_id[0]", "1")
                .assertJson("page", 1)
                .assertJson("per_page", 20)
                .assertJson("order_by[0][0]", "sequence_no")
                .assertJson("fields[1]", "destination_name");
        assertEquals(Integer.valueOf(5002), list.getItems().get(0).getRouteDestinationId());
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
        http.respond(200, json("{'destination_id':5002,'route_id':'ROUTE-A1','label_code':'LBL-1','sequence_no':3}"));

        SortingFilteringResource sorting = manager.getDestinationByLabelCode("LBL-1");

        http.request().assertEndpoint("GET", HOST, DESTINATIONS_EP + "/sorting").assertQuery("label_code", "LBL-1");
        assertEquals(Integer.valueOf(3), sorting.getSequenceNo());
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
        first.setLat(40.5f);
        first.setLng(-74.5f);
        DestinationSequenceRequest request = new DestinationSequenceRequest();
        request.setCoordinates(Arrays.asList(first, new SequenceCoordinate()));

        DestinationSequenceResponse response = manager.getDestinationSequence(request);

        http.request().assertEndpoint("POST", HOST, DESTINATIONS_EP + "/sequence")
                .assertJson("coordinates[0].lat", 40.5)
                .assertJson("coordinates[0].lng", -74.5);
        assertArrayEquals(new int[] {1, 0}, response.getOptimizedSequence());
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
