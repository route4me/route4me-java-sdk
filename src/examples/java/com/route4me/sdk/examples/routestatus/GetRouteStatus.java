package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/**
 * Gets the status of a route with {@link RouteStatusManager#getRouteStatus}. Set {@code ROUTE_ID} to one of your
 * routes.
 */
public class GetRouteStatus {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example reads its status.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            RouteStatusResponse status = manager.getRouteStatus(routeId);
            System.out.println("Route: " + status.getRouteId());
            System.out.println("Status: " + status.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
