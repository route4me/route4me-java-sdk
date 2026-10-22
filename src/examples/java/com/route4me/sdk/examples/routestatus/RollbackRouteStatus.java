package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/** Rolls the status of a route back to the previous one with {@link RouteStatusManager#rollbackRouteStatus}. */
public class RollbackRouteStatus {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
            RouteStatusResponse result = manager.rollbackRouteStatus(routeId);
            System.out.println("Rolled back to status: " + result.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
