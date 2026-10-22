package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusHistoryData;
import com.route4me.sdk.services.routestatus.RouteStatusHistoryResponse;
import com.route4me.sdk.services.routestatus.RouteStatusManager;

/** Lists the status changes of a route with {@link RouteStatusManager#getRouteStatusHistory}. */
public class GetRouteStatusHistory {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
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
