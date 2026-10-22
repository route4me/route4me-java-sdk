package com.route4me.sdk.services.routing;

import com.google.gson.JsonElement;
import com.route4me.sdk.services.routing.search.RouteSearchResult;
import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link RoutingManagerV5}: the V5 route lists, the route data table and the route export.
 */
public class RoutingManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String ROUTES_EP = "/modules/api/v5.0/routes";
    private static final String EXPORT_EP = "/modules/api/v5.0/route-export";
    private static final String ROUTE_ID = "C0FFEE00000000000000000000000001";
    private static final String PAGE = json("{'data':[{'route_id':'" + ROUTE_ID + "','route_name':'Morning run',"
            + "'destination_count':12,'member_id':1001,'trip_distance':42.5,'actual_travel_time':3600,"
            + "'route_status':'planned'}],'links':{'next':'page-2'},'meta':{'current_page':1,'per_page':20,"
            + "'total':1}}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RoutingManagerV5 manager() {
        return http.install(new RoutingManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample route list request with fake data. */
    private static RouteListRequest listRequest() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("schedule_date", Arrays.asList("2026-10-01", "2026-10-31"));
        filters.put("route_id", Collections.singletonList(ROUTE_ID));
        return new RouteListRequest()
                .setQuery("Morning")
                .setFilters(filters)
                .setOrderBy(Collections.singletonList(Arrays.asList("route_created_unix", "desc")))
                .setPage(1)
                .setPerPage(20)
                .setTimezone("America/New_York")
                .setGroupBy("vehicle")
                .setFields(Arrays.asList("route_id", "route_name"));
    }

    /** Asserts the JSON body of {@link #listRequest()}. */
    private static void assertListBody(CapturedRequest request) {
        request.assertApiKeyAsBearer()
                .assertContentType("application/json")
                .assertJson("query", "Morning")
                .assertJson("filters.schedule_date[0]", "2026-10-01")
                .assertJson("filters.schedule_date[1]", "2026-10-31")
                .assertJson("filters.route_id[0]", ROUTE_ID)
                .assertJson("order_by[0][0]", "route_created_unix")
                .assertJson("order_by[0][1]", "desc")
                .assertJson("page", 1)
                .assertJson("per_page", 20)
                .assertJson("timezone", "America/New_York")
                .assertJson("group_by", "vehicle")
                .assertJson("fields[1]", "route_name")
                .assertJsonLacks("search_query", "exclude_ids", "exist_fields");
    }

    @Test
    public void getRoutesPostsTheRequestAndParsesThePage() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respondToAll(200, PAGE);

        RouteSearchResult page = manager.getRoutes(listRequest());
        manager.getRoutes(null);

        CapturedRequest request = http.request(0).assertEndpoint("POST", HOST, ROUTES_EP);
        assertListBody(request);
        assertTrue(http.request(1).assertEndpoint("POST", HOST, ROUTES_EP).jsonKeys().isEmpty());
        assertEquals(ROUTE_ID, page.getData().get(0).getRouteID());
        assertEquals(Integer.valueOf(12), page.getData().get(0).getDestinationCount());
        assertEquals("3600", page.getData().get(0).getActualTravelTime());
        assertEquals("page-2", page.getLinks().getNext());
        assertEquals(Integer.valueOf(1), page.getMeta().getTotal());
    }

    @Test
    public void getRoutesListPostsToTheListEndpoint() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respondToAll(200, PAGE);

        RouteSearchResult page = manager.getRoutesList(listRequest());
        manager.getRoutesList(null);

        assertListBody(http.request(0).assertEndpoint("POST", HOST, ROUTES_EP + "/list"));
        assertTrue(http.request(1).assertEndpoint("POST", HOST, ROUTES_EP + "/list").jsonKeys().isEmpty());
        assertEquals("Morning run", page.getData().get(0).getRouteName());
    }

    @Test
    public void getRouteDatatableWithElasticSearchPostsAndReturnsTheJson() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respond(200, PAGE).respond(200, json("[{'route_id':'" + ROUTE_ID + "'}]"));

        JsonElement table = manager.getRouteDatatableWithElasticSearch(listRequest());
        JsonElement array = manager.getRouteDatatableWithElasticSearch(null);

        assertListBody(http.request(0).assertEndpoint("POST", HOST, ROUTES_EP + "/datatable"));
        assertTrue(http.request(1).assertEndpoint("POST", HOST, ROUTES_EP + "/datatable").jsonKeys().isEmpty());
        assertEquals(1, table.getAsJsonObject().getAsJsonArray("data").size());
        assertEquals(ROUTE_ID, array.getAsJsonArray().get(0).getAsJsonObject().get("route_id").getAsString());
    }

    @Test
    public void getRouteDataTableConfigReadsTheCapabilities() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respond(200, json("{'api_capabilities':{'sortable_fields':['route_name','route_created_unix'],"
                + "'sortable_fields_combinations':[['route_name','member_id']],'multi_sorting_enabled':true,"
                + "'filterable_fields':['schedule_date'],'search':true},'api_preferences':"
                + "{'force_server_side_search':true,'partial_load':false,'simple_pagination':true}}"));

        RouteDataTableConfigResponse config = manager.getRouteDataTableConfig();

        http.request().assertEndpoint("GET", HOST, ROUTES_EP + "/datatable/config").assertApiKeyAsBearer()
                .assertNoBody();
        assertEquals("route_created_unix", config.getApiCapabilities().getSortableFields().get(1));
        assertEquals("member_id", config.getApiCapabilities().getSortableFieldsCombinations().get(0).get(1));
        assertTrue(config.getApiCapabilities().getMultiSortingEnabled());
        assertEquals("schedule_date", config.getApiCapabilities().getFilterableFields().get(0));
        assertTrue(config.getApiCapabilities().getSearch());
        assertTrue(config.getApiPreferences().getForceServerSideSearch());
        assertFalse(config.getApiPreferences().getPartialLoad());
        assertTrue(config.getApiPreferences().getSimplePagination());
    }

    @Test
    public void exportRoutePostsTheOptionsAndReturnsTheText() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respond(200, "route_name,address\nMorning run,100 Example St\n", "text/csv")
                .respond(200, "[]");
        RouteExportRequest request = new RouteExportRequest()
                .setFormat("csv")
                .setWithAddresses(true)
                .setWithNotes(false)
                .setAllCustomFields(true)
                .setColumns("route_name,address")
                .setTz("America/New_York");

        String csv = manager.exportRoute(ROUTE_ID, request);
        manager.exportRoute(ROUTE_ID, null);

        http.request(0).assertEndpoint("POST", HOST, EXPORT_EP + "/" + ROUTE_ID + "/export").assertApiKeyAsBearer()
                .assertContentType("application/json")
                .assertJson("format", "csv")
                .assertJson("with_addresses", true)
                .assertJson("with_notes", false)
                .assertJson("all_custom_fields", true)
                .assertJson("columns", "route_name,address")
                .assertJson("tz", "America/New_York")
                .assertJsonLacks("download", "route_id", "filters");
        assertTrue(http.request(1).jsonKeys().isEmpty());
        assertEquals("route_name,address\nMorning run,100 Example St\n", csv);
    }

    @Test
    public void exportRouteKeepsTheRouteIdInOneSegment() throws Exception {
        RoutingManagerV5 manager = manager();
        http.respond(200, "");

        manager.exportRoute("../secret", null);

        assertEquals(EXPORT_EP + "/..%2Fsecret/export", http.request().getUri().getRawPath());
    }

    @Test
    public void exportRouteRejectsAnEmptyIdBeforeSending() {
        final RoutingManagerV5 manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.exportRoute("", null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.exportRoute(null, null));
        http.assertNoRequests();
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        RoutingManagerV5 manager = http.install(new RoutingManagerV5(MockHttp.API_KEY, true));
        http.respond(200, PAGE);

        manager.getRoutes(null);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final RoutingManagerV5 manager = manager();
        http.respond(422, json("{'errors':['The page must be at least 1.']}"));

        http.expectApiException(() -> manager.getRoutesList(new RouteListRequest().setPage(0)));
    }
}
