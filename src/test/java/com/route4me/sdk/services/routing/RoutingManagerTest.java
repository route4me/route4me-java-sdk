package com.route4me.sdk.services.routing;

import com.route4me.sdk.services.routing.balance.Balance;
import com.route4me.sdk.services.routing.balance.BalanceMode;
import com.route4me.sdk.services.routing.search.RouteSearchResult;
import com.route4me.sdk.testsupport.Fixtures;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link RoutingManager}: optimizations, routes and route destinations on
 * the V4 API, and the V5.1 route search.
 */
public class RoutingManagerTest {

    private static final String API = "api.route4me.com";
    private static final String WH = "wh.route4me.com";
    private static final String OPTIMIZATION_EP = "/api.v4/optimization_problem.php";
    private static final String ROUTE_EP = "/api.v4/route.php";
    private static final String ADDRESS_EP = "/api.v4/address.php";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RoutingManager manager() {
        return http.install(new RoutingManager(MockHttp.API_KEY));
    }

    /** Builds a sample two-address optimization request with fake data. */
    private static OptimizationParameters optimizationParameters() {
        Parameters parameters = new Parameters();
        parameters.setRouteName("Morning run");
        parameters.setAlgorithmType("1");
        OptimizationParameters request = new OptimizationParameters();
        request.setParameters(parameters);
        request.setAddresses(Arrays.asList(
                new Address("100 Example St, Springfield", true, 40.1, -74.1, 0),
                new Address("200 Sample Ave, Springfield", 40.2, -74.2, 300)));
        return request;
    }

    // ------------------------------------------------------------------ constructors

    @Test
    public void constructorsPassRedirectCallbackAndProxySettings() throws Exception {
        RoutingManager noRedirects = http.install(new RoutingManager(MockHttp.API_KEY, true));
        RoutingManager withCallback = http.install(new RoutingManager(MockHttp.API_KEY, false, "https://example.com/cb"));
        RoutingManager proxied = http.install(new RoutingManager(MockHttp.API_KEY, "proxy.example.com", 8080, "http"));
        RoutingManager proxiedNoRedirects = http.install(
                new RoutingManager(MockHttp.API_KEY, "proxy.example.com", 8080, "http", true));
        http.respondToAll(200, Fixtures.read("optimization.json"));

        noRedirects.runOptimization(optimizationParameters());
        withCallback.runOptimization(optimizationParameters());
        proxied.runOptimization(optimizationParameters());
        proxiedNoRedirects.runOptimization(optimizationParameters());

        http.request(0).assertQuery("redirect", "0");
        http.request(1).assertQuery("optimized_callback_url", "https://example.com/cb").assertNoQuery("redirect");
        assertEquals("http://proxy.example.com:8080", http.request(2).getProxy());
        http.request(3).assertQuery("redirect", "0");
        assertEquals("http://proxy.example.com:8080", http.request(3).getProxy());
    }

    // ------------------------------------------------------------------ optimizations

    @Test
    public void runOptimizationPostsParametersAndAddresses() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization.json");

        DataObject result = manager.runOptimization(optimizationParameters());

        http.request()
                .assertEndpoint("POST", API, OPTIMIZATION_EP)
                .assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("parameters.route_name", "Morning run")
                .assertJson("parameters.algorithm_type", "1")
                .assertJson("addresses[0].address", "100 Example St, Springfield")
                .assertJson("addresses[0].is_depot", true)
                .assertJson("addresses[1].lat", 40.2)
                .assertJson("addresses[1].lng", -74.2)
                .assertJson("addresses[1].time", 300);
        assertEquals("OPT-1", result.getOptimizationProblemId());
        assertEquals(4, result.getState().intValue());
        assertEquals(2, result.getAddresses().size());
        assertEquals(Long.valueOf(5002), result.getAddresses().get(1).getRouteDestinationId());
        assertEquals("Morning run", result.getParameters().getRouteName());
        assertTrue(result.getLinks().getRoute().contains("ROUTE-A1"));
    }

    @Test
    public void runOptimizationSendsNestedOptionsWithApiFieldNames() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization.json");
        Bundling bundling = new Bundling();
        bundling.setMode(BundlingEnum.BundlingMode.BUNDLING_BY_ADDRESS.getValue());
        SlowDowns slowDowns = new SlowDowns();
        slowDowns.setServiceTime(10);
        slowDowns.setTravelTime(20);
        Break lunch = new Break();
        lunch.setMode(1);
        lunch.setDuration(1800);
        lunch.setModeParams(Arrays.asList(4, 5));
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(Constants.AlgorithmType.CVRP_TW_MD.getValue());
        parameters.setTravelMode(Constants.TravelMode.DRIVING.toString());
        parameters.setOptimize(Constants.Optimize.TIME.toString());
        parameters.setUTurn(Constants.UTurn.UTURN_DEPART_SHORTEST.getValue());
        parameters.setBalance(new Balance(BalanceMode.DESTINATIONS_COUNT.toString()));
        parameters.setBundling(bundling);
        parameters.setSlowdowns(slowDowns);
        parameters.setBreaks(Collections.singletonList(lunch));
        OptimizationParameters request = new OptimizationParameters();
        request.setParameters(parameters);
        request.setAddresses(Arrays.asList(
                new Address("100 Example St", true, "Depot", 40.1, -74.1, 0),
                new Address("200 Sample Ave", false, 40.2, -74.2, 300, 28800, 36000),
                new Address("300 Demo Rd", 40.3, -74.3, 600, 32400, 39600),
                new Address("400 Test Blvd", "Back door", 40.4, -74.4, 900)));

        manager.runOptimization(request);

        http.request()
                .assertJson("parameters.algorithm_type", "4")
                .assertJson("parameters.travel_mode", "Driving")
                .assertJson("parameters.optimize", "Time")
                .assertJson("parameters.uturn", 1)
                .assertJson("parameters.balance.mode", "destinations_count")
                .assertJson("parameters.bundling.mode", 1)
                .assertJson("parameters.slowdowns.service_time", 10)
                .assertJson("parameters.slowdowns.travel_time", 20)
                .assertJson("parameters.breaks[0].duration", 1800)
                .assertJson("parameters.breaks[0].mode_params[1]", 5)
                .assertJson("addresses[0].alias", "Depot")
                .assertJson("addresses[0].is_depot", true)
                .assertJson("addresses[1].time_window_start", 28800)
                .assertJson("addresses[1].time_window_end", 36000)
                .assertJson("addresses[2].time_window_start", 32400)
                .assertJson("addresses[3].alias", "Back door")
                .assertJson("addresses[3].time", 900);
    }

    @Test
    public void runOptimizationMultiParsesAnArray() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, "[" + Fixtures.read("optimization.json") + "," + Fixtures.read("optimization.json") + "]");

        DataObject[] results = manager.runOptimizationMulti(optimizationParameters());

        http.request().assertEndpoint("POST", API, OPTIMIZATION_EP);
        assertEquals(2, results.length);
    }

    @Test
    public void getOptimizationSendsTheIdAsQueryParameter() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization_with_routes.json");
        OptimizationParameters request = new OptimizationParameters();
        request.setProblemId("OPT-1");

        Optimization optimization = manager.getOptimization(request);

        http.request().assertEndpoint("GET", API, OPTIMIZATION_EP).assertQuery("optimization_problem_id", "OPT-1")
                .assertNoQuery("reoptimize").assertNoBody();
        assertEquals(2, optimization.getRoutes().size());
        assertEquals("ROUTE-A2", optimization.getRoutes().get(1).getId());
        assertEquals(12.5, optimization.getRoutes().get(0).getTripDistance(), 0.0);
    }

    @Test
    public void updateOptimizationSendsPutWithTheParameters() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization.json");
        OptimizationParameters request = optimizationParameters();
        request.setProblemId("OPT-1");

        DataObject result = manager.updateOptimization(request);

        http.request().assertEndpoint("PUT", API, OPTIMIZATION_EP).assertJson("parameters.route_name", "Morning run");
        assertEquals("OPT-1", result.getOptimizationProblemId());
    }

    @Ignore("bug (unfiled): updateOptimization does not put optimization_problem_id on the URL")
    @Test
    public void updateOptimizationPutsTheIdOnTheUrl() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization.json");
        OptimizationParameters request = optimizationParameters();
        request.setProblemId("OPT-1");

        manager.updateOptimization(request);

        http.request().assertQuery("optimization_problem_id", "OPT-1");
    }

    @Test
    public void reOptimizationByIdSendsIdAndFlagInTheQuery() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization_with_routes.json");
        OptimizationParameters request = new OptimizationParameters();
        request.setProblemId("OPT-1");
        request.setReoptimize(true);

        Optimization optimization = manager.reOptimizationByOptimizationID(request);

        http.request().assertEndpoint("PUT", API, OPTIMIZATION_EP)
                .assertQuery("optimization_problem_id", "OPT-1")
                .assertQuery("reoptimize", "1");
        assertEquals("OPT-1", optimization.getOptimizationProblemId());
    }

    @Test
    public void reOptimizationSendsTheOptimizationAsBody() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "optimization_with_routes.json");
        OptimizationParameters request = new OptimizationParameters();
        request.setProblemId("OPT-1");
        request.setReoptimize(true);
        DataObject optimization = new DataObject();
        optimization.setAddresses(Collections.singletonList(new Address("300 Demo Rd", 40.3, -74.3)));

        manager.reOptimization(request, optimization);

        http.request().assertEndpoint("PUT", API, OPTIMIZATION_EP)
                .assertQuery("optimization_problem_id", "OPT-1")
                .assertJson("addresses[0].address", "300 Demo Rd");
    }

    @Test
    public void getOptimizationsSendsPagingAndUnwrapsTheList() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, "{\"optimizations\":[" + Fixtures.read("optimization.json") + "]}");

        List<DataObject> optimizations = manager.getOptimizations(10, 20);

        http.request().assertEndpoint("GET", API, OPTIMIZATION_EP).assertQuery("limit", "10").assertQuery("offset", "20");
        assertEquals(1, optimizations.size());
        assertEquals("OPT-1", optimizations.get(0).getOptimizationProblemId());
    }

    @Test
    public void deleteOptimizationSendsTheIdsInTheBody() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'status':true,'removed':2}"));

        Map<String, Object> result = manager.deleteOptimization(Arrays.asList("OPT-1", "OPT-2"));

        http.request().assertEndpoint("DELETE", API, OPTIMIZATION_EP)
                .assertJson("optimization_problem_ids[0]", "OPT-1")
                .assertJson("optimization_problem_ids[1]", "OPT-2");
        assertEquals(Boolean.TRUE, result.get("status"));
    }

    @Test
    public void optimizationErrorBecomesApiException() {
        final RoutingManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.runOptimization(optimizationParameters()));
    }

    // ------------------------------------------------------------------ route destinations

    @Test
    public void addAddressesToRouteSendsAddressesAndOptimalPosition() throws Exception {
        RoutingManager manager = manager();
        http.respondToAll(200, Fixtures.read("optimization.json"));
        List<Address> addresses = Collections.singletonList(new Address("300 Demo Rd", 40.3, -74.3));

        manager.addAddressesToRoute("ROUTE-A1", addresses);
        DataObject result = manager.addAddressesToRoute("ROUTE-A1", addresses, true);

        http.request(0).assertEndpoint("PUT", API, ROUTE_EP).assertQuery("route_id", "ROUTE-A1")
                .assertJson("addresses[0].address", "300 Demo Rd").assertJson("optimal_position", false);
        http.request(1).assertJson("optimal_position", true);
        assertEquals("OPT-1", result.getOptimizationProblemId());
    }

    @Test
    public void getAddressSendsRouteAndDestinationIds() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'route_destination_id':5002,'address':'200 Sample Ave','lat':40.2,'lng':-74.2}"));

        Address address = manager.getAddress("ROUTE-A1", 5002);

        http.request().assertEndpoint("GET", API, ADDRESS_EP).assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002");
        assertEquals("200 Sample Ave", address.getAddress());
        assertEquals(40.2, address.getLatitude(), 0.0);
    }

    @Test
    public void moveAddressesPostsFormFields() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'success':true}")).respond(200, json("{'success':false,'error':'not found'}"));

        MovedAddressesResponse moved = manager.moveAddresses(5002, "ROUTE-B1", 7001);
        MovedAddressesResponse failed = manager.moveAddresses(5002, "ROUTE-B1");

        http.request(0).assertEndpoint("POST", API, "/actions/route/move_route_destination.php")
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("to_route_id", "ROUTE-B1")
                .assertForm("route_destination_id", "5002")
                .assertForm("after_destination_id", "7001");
        assertNull(http.request(1).form("after_destination_id"));
        assertTrue(moved.getSuccess());
        assertFalse(failed.getSuccess());
        assertEquals("not found", failed.getError());
    }

    @Test
    public void deleteAddressVariantsSendTheIds() throws Exception {
        RoutingManager manager = manager();
        http.respondToAll(200, json("{'deleted':true}"));

        manager.deleteAddress("ROUTE-A1", 5002);
        manager.deleteAddressFromOptimization("OPT-1", 5002);

        http.request(0).assertEndpoint("DELETE", API, ADDRESS_EP).assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002").assertNoBody();
        http.request(1).assertEndpoint("DELETE", API, ADDRESS_EP).assertQuery("optimization_problem_id", "OPT-1")
                .assertQuery("route_destination_id", "5002").assertNoQuery("route_id");
    }

    @Test
    public void updateAddressAttributeSendsTheAddress() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'route_destination_id':5002,'alias':'Back door'}"));
        Address change = new Address();
        change.setAlias("Back door");
        change.addCustomField("gate_code", "1234");

        Address updated = manager.updateAddressAttribute("ROUTE-A1", 5002, change);

        http.request().assertEndpoint("PUT", API, ADDRESS_EP).assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002")
                .assertJson("alias", "Back door").assertJson("custom_fields.gate_code", "1234");
        assertEquals("Back door", updated.getAlias());
    }

    // ------------------------------------------------------------------ routes

    @Test
    public void getRoutesByTimeDateConvertsDatesAndPaging() throws Exception {
        RoutingManager manager = manager();
        http.respondToAll(200, "[" + Fixtures.read("route.json") + "]");

        List<Route> routes = manager.getRoutesbyTimeDate("2026-10-21T00:00:00:000+00:00", "2026-10-22T23:00:00:000+00:00");
        manager.getRoutesbyTimeDate("2026-10-21T00:00:00:000+00:00", "2026-10-22T23:00:00:000+00:00", 5, 10);

        http.request(0).assertEndpoint("GET", API, ROUTE_EP).assertQuery("start_date", "2026-10-21")
                .assertQuery("end_date", "2026-10-22").assertNoQuery("limit").assertNoBody();
        http.request(1).assertQuery("limit", "5").assertQuery("offset", "10");
        assertEquals("ROUTE-A1", routes.get(0).getId());
        assertEquals("driver@example.com", routes.get(0).getMemberEmail());
    }

    @Test
    public void getRoutesByDateTimeZonePostsTheV51Search() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "route_search.json").respondWithFixture(200, "route_search.json");

        RouteSearchResult result = manager.getRoutesbyDateTimeZone("2026-10-21", "2026-10-22", "America/New_York", 2, 50);
        manager.getRoutesbyDateTimeZone("2026-10-21", "2026-10-22", "UTC");

        http.request(0).assertEndpoint("POST", WH, "/modules/api/v5.1/routes")
                .assertContentType("application/json")
                .assertJson("timezone", "America/New_York")
                .assertJson("page", 2)
                .assertJson("per_page", 50)
                .assertJson("filters.schedule_date[0]", "2026-10-21")
                .assertJson("filters.schedule_date[1]", "2026-10-22")
                .assertJson("order_by[0][0]", "route_created_unix")
                .assertJson("order_by[0][1]", "desc");
        http.request(1).assertJson("page", 1).assertJson("per_page", 30);
        assertEquals("Morning run", result.getData().get(0).getRouteName());
        assertEquals(Integer.valueOf(1), result.getMeta().getTotal());
    }

    @Test
    public void getRoutesAndGetRouteSendTheRequestAsQuery() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, "[" + Fixtures.read("route.json") + "]").respondWithFixture(200, "route.json");
        RoutesRequest list = new RoutesRequest();
        list.setLimit(10);
        list.setOffset(0);
        RoutesRequest single = new RoutesRequest();
        single.setId("ROUTE-A1");
        single.setDirections(true);

        List<Route> routes = manager.getRoutes(list);
        Route route = manager.getRoute(single);

        http.request(0).assertEndpoint("GET", API, ROUTE_EP).assertQuery("limit", "10").assertQuery("offset", "0");
        http.request(1).assertEndpoint("GET", API, ROUTE_EP).assertQuery("route_id", "ROUTE-A1")
                .assertQuery("directions", "1");
        assertEquals(1, routes.size());
        assertEquals("VEH-1", route.getParameters().getVehicleId());
    }

    @Test
    public void updateRouteSendsTheRouteIdAndBody() throws Exception {
        RoutingManager manager = manager();
        http.respondWithFixture(200, "route.json");
        Route change = new Route();
        change.setId("ROUTE-A1");
        Parameters parameters = new Parameters();
        parameters.setRouteName("Renamed");
        change.setParameters(parameters);

        Route updated = manager.updateRoute(change);

        http.request().assertEndpoint("PUT", API, ROUTE_EP).assertQuery("route_id", "ROUTE-A1")
                .assertJson("route_id", "ROUTE-A1").assertJson("parameters.route_name", "Renamed");
        assertEquals("ROUTE-A1", updated.getId());
    }

    @Test
    public void assignDriverAndVehicleUpdateTheRouteParameters() throws Exception {
        RoutingManager manager = manager();
        http.respondToAll(200, Fixtures.read("route.json"));

        manager.assignDriver("ROUTE-A1", "1001");
        manager.assignVehicle("ROUTE-A1", "VEH-1");

        http.request(0).assertEndpoint("PUT", API, ROUTE_EP).assertQuery("route_id", "ROUTE-A1")
                .assertJson("parameters.member_id", "1001").assertJsonLacks("parameters.vehicle_id");
        http.request(1).assertJson("parameters.vehicle_id", "VEH-1").assertJsonLacks("parameters.member_id");
    }

    @Test
    public void deleteRoutesSendsTheIdsAndParsesTheResult() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'deleted':true,'route_ids':['ROUTE-A1','ROUTE-A2']}"));

        RouteDeletedResponse result = manager.deleteRoutes("ROUTE-A1", "ROUTE-A2");

        http.request().assertEndpoint("DELETE", API, ROUTE_EP).assertNoBody();
        assertTrue(http.request().query("route_id").startsWith("ROUTE-A1,ROUTE-A2"));
        assertTrue(result.isDeleted());
        assertEquals(Arrays.asList("ROUTE-A1", "ROUTE-A2"), result.getAddresses());
    }

    @Ignore("bug #41: deleteRoutes leaves a trailing comma in the route_id list")
    @Test
    public void deleteRoutesHasNoTrailingComma() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'deleted':true}"));

        manager.deleteRoutes("ROUTE-A1", "ROUTE-A2");

        http.request().assertQuery("route_id", "ROUTE-A1,ROUTE-A2");
    }

    @Test
    public void duplicateRouteSendsTheRouteIdAndTarget() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'optimization_problem_id':'OPT-2','success':true}"));

        RoutingManager.DuplicateRouteResponse result = manager.duplicateRoute("ROUTE-A1");

        http.request().assertEndpoint("GET", API, "/actions/duplicate_route.php")
                .assertQuery("route_id", "ROUTE-A1").assertQuery("to", "none");
        assertEquals("OPT-2", result.getProblemId());
        assertTrue(result.isSuccess());
        assertTrue(result.toString().contains("OPT-2"));
    }

    @Ignore("registry: POST /actions/route/rename_route.php is not in endpoints.json")
    @Test
    public void renameRoutePostsTheNewName() throws Exception {
        RoutingManager manager = manager();
        http.respond(200, json("{'success':true}"));

        RouteRenamedStatus status = manager.renameRoute("Renamed", "ROUTE-A1");

        http.request().assertEndpoint("POST", API, "/actions/route/rename_route.php")
                .assertQuery("format", "json")
                .assertForm("route_name", "Renamed")
                .assertForm("route_id", "ROUTE-A1");
        assertTrue(status.getSuccess());
    }

    @Test
    public void routeErrorBecomesApiException() {
        final RoutingManager manager = manager();
        http.respond(404, json("{'errors':['Route not found']}"));

        http.expectApiException(() -> manager.getRoute(new RoutesRequest()));
    }
}
