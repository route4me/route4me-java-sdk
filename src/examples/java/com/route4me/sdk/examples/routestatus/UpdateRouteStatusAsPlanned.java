package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.UpdateRouteStatusAsPlannedResponse;

/** Marks two routes as planned with {@link RouteStatusManager#updateRouteStatusAsPlanned}. */
public class UpdateRouteStatusAsPlanned {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            UpdateRouteStatusAsPlannedResponse response = manager.updateRouteStatusAsPlanned(
                    new String[] {"ROUTE_ID_1_HERE", "ROUTE_ID_2_HERE"});
            System.out.println(response.getData());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
