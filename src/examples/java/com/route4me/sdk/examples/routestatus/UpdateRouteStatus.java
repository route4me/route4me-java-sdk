package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.RouteStatusRequest;
import com.route4me.sdk.services.routestatus.RouteStatusResponse;

/** Changes the status of a route with {@link RouteStatusManager#updateRouteStatus}. */
public class UpdateRouteStatus {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
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
