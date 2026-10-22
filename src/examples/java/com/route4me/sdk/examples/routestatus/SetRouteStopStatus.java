package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.StatusResponse;

/** Marks two stops of a route as completed with {@link RouteStatusManager#setRouteStopStatus}. */
public class SetRouteStopStatus {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            StatusResponse response = manager.setRouteStopStatus(new long[] {123456789L, 123456790L}, "Completed");
            System.out.println("Status set: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
