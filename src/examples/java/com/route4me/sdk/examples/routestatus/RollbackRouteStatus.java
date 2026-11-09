package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/**
 * Rolls the status of a route back to the previous one with {@link RouteStatusManager#rollbackRouteStatus}. Set
 * {@code ROUTE_ID} to a route of a test account: the example changes its status.
 */
public class RollbackRouteStatus {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example changes its status.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            RouteStatusResponse result = manager.rollbackRouteStatus(routeId);
            System.out.println("Rolled back to status: " + result.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
