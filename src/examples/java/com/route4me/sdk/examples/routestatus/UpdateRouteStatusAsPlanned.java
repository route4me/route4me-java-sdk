package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.UpdateRouteStatusAsPlannedResponse;

/**
 * Marks routes as planned with {@link RouteStatusManager#updateRouteStatusAsPlanned}. Set {@code ROUTE_IDS} to the
 * comma-separated IDs of routes of a test account: the example changes their status.
 */
public class UpdateRouteStatusAsPlanned {
    public static void main(String[] args) {
        String routeIds = System.getenv("ROUTE_IDS");
        if (routeIds == null || routeIds.isEmpty()) {
            System.err.println("Set ROUTE_IDS to the comma-separated IDs of routes of a test account; this example"
                    + " changes their status.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            UpdateRouteStatusAsPlannedResponse response = manager.updateRouteStatusAsPlanned(
                    routeIds.trim().split("\\s*,\\s*"));
            System.out.println(response.getData());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
