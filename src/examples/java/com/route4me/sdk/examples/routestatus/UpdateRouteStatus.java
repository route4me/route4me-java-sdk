package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusRequest;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/**
 * Marks a route as started with {@link RouteStatusManager#updateRouteStatus}. Set {@code ROUTE_ID} to a route of a
 * test account: the example changes its status.
 */
public class UpdateRouteStatus {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example changes its status.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            RouteStatusRequest request = new RouteStatusRequest();
            request.setStatus("started");
            request.setLatitude(40.7128);
            request.setLongitude(-74.0060);
            request.setEventTimestamp(System.currentTimeMillis() / 1000);
            RouteStatusResponse result = manager.updateRouteStatus(routeId, request);
            System.out.println("Updated status: " + result.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
