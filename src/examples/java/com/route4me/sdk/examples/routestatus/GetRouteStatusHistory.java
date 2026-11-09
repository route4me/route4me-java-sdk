package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusHistoryData;
import com.route4me.sdk.services.routestatus.RouteStatusHistoryResponse;
import com.route4me.sdk.services.routestatus.RouteStatusManager;

/**
 * Lists the status changes of a route with {@link RouteStatusManager#getRouteStatusHistory}. Set {@code ROUTE_ID} to
 * one of your routes.
 */
public class GetRouteStatusHistory {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example lists its status changes.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            RouteStatusHistoryResponse history = manager.getRouteStatusHistory(routeId, "asc", null, null);
            if (history.getData() != null) {
                for (RouteStatusHistoryData entry : history.getData()) {
                    System.out.println("Status: " + entry.getStatus() + " at " + entry.getTimestamp());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
