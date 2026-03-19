package com.route4me.sdk.integration;

import com.route4me.sdk.services.routing.*;
import com.route4me.sdk.exception.APIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class RoutingManagerIntegrationTest extends BaseIntegrationTest {
    private RoutingManager manager;
    private List<String> createdOptimizationIds = new ArrayList<>();
    private List<String> createdRouteIds = new ArrayList<>();

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new RoutingManager(API_KEY);
    }

    @After
    public void tearDown() {
        for (String routeId : createdRouteIds) {
            try { manager.deleteRoutes(routeId); } catch (Exception e) { }
        }
        for (String optId : createdOptimizationIds) {
            try { manager.deleteOptimization(Arrays.asList(optId)); } catch (Exception e) { }
        }
    }

    private OptimizationParameters buildOptimizationParams() {
        OptimizationParameters params = new OptimizationParameters();
        Parameters routeParams = new Parameters();
        routeParams.setRouteName("SDK Test Route " + System.currentTimeMillis());
        routeParams.setAlgorithmType("1");
        routeParams.setDistanceUnit("mi");
        routeParams.setDeviceType("web");
        routeParams.setTravelMode("Driving");
        params.setParameters(routeParams);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("754 5th Ave New York, NY 10019", true, "Depot", 40.7636197, -73.9744060, 0));
        addresses.add(new Address("717 5th Ave New York, NY 10022", false, "Stop 1", 40.7669692, -73.9693864, 300));
        addresses.add(new Address("888 Madison Ave New York, NY 10014", false, "Stop 2", 40.7715154, -73.9669241, 300));
        params.setAddresses(addresses);

        return params;
    }

    private Optimization runAndTrack() throws APIException {
        DataObject result = manager.runOptimization(buildOptimizationParams());
        assertNotNull(result);
        assertNotNull(result.getOptimizationProblemId());
        createdOptimizationIds.add(result.getOptimizationProblemId());

        OptimizationParameters getParams = new OptimizationParameters();
        getParams.setProblemId(result.getOptimizationProblemId());
        Optimization opt = manager.getOptimization(getParams);
        if (opt.getRoutes() != null) {
            for (Route r : opt.getRoutes()) {
                createdRouteIds.add(r.getId());
            }
        }
        return opt;
    }

    @Test
    public void testRunOptimization() throws APIException {
        DataObject result = manager.runOptimization(buildOptimizationParams());
        assertNotNull(result);
        assertNotNull(result.getOptimizationProblemId());
        createdOptimizationIds.add(result.getOptimizationProblemId());
    }

    @Test
    public void testGetOptimization() throws APIException {
        DataObject created = manager.runOptimization(buildOptimizationParams());
        createdOptimizationIds.add(created.getOptimizationProblemId());

        OptimizationParameters getParams = new OptimizationParameters();
        getParams.setProblemId(created.getOptimizationProblemId());
        Optimization fetched = manager.getOptimization(getParams);
        assertNotNull(fetched);
        if (fetched.getRoutes() != null) {
            for (Route r : fetched.getRoutes()) {
                createdRouteIds.add(r.getId());
            }
        }
    }

    @Test
    public void testGetOptimizations() throws APIException {
        List<DataObject> optimizations = manager.getOptimizations(5, 0);
        assertNotNull(optimizations);
    }

    @Test
    public void testGetRoutes() throws APIException {
        RoutesRequest request = new RoutesRequest();
        request.setLimit(5);
        request.setOffset(0);
        List<Route> routes = manager.getRoutes(request);
        assertNotNull(routes);
    }

    @Test
    public void testGetRoute() throws APIException {
        Optimization opt = runAndTrack();
        assertNotNull(opt.getRoutes());
        assertFalse(opt.getRoutes().isEmpty());
        String routeId = opt.getRoutes().get(0).getId();

        RoutesRequest request = new RoutesRequest();
        request.setId(routeId);
        Route route = manager.getRoute(request);
        assertNotNull(route);
        assertEquals(routeId, route.getId());
    }

    @Test
    public void testUpdateRoute() throws APIException {
        Optimization opt = runAndTrack();
        assertNotNull(opt.getRoutes());
        String routeId = opt.getRoutes().get(0).getId();

        Route updateRoute = new Route();
        updateRoute.setId(routeId);
        Parameters params = new Parameters();
        params.setRouteName("SDK Updated Route");
        updateRoute.setParameters(params);
        Route updated = manager.updateRoute(updateRoute);
        assertNotNull(updated);
    }

    @Test
    public void testDeleteRoutes() throws APIException {
        Optimization opt = runAndTrack();
        assertNotNull(opt.getRoutes());
        String routeId = opt.getRoutes().get(0).getId();
        createdRouteIds.remove(routeId);

        RouteDeletedResponse deleted = manager.deleteRoutes(routeId);
        assertNotNull(deleted);
    }
}
