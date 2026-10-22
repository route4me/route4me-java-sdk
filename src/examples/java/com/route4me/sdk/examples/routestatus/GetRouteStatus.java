package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/** Gets the status of a route with {@link RouteStatusManager#getRouteStatus}. */
public class GetRouteStatus {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
            RouteStatusResponse status = manager.getRouteStatus(routeId);
            System.out.println("Route: " + status.getRouteId());
            System.out.println("Status: " + status.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
