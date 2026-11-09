package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Replaces the custom data of a route with {@link RouteCustomDataManager#updateRouteCustomData}. Set {@code ROUTE_ID}
 * to a route of a test account: the example replaces its custom data.
 */
public class UpdateRouteCustomData {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example replaces its custom"
                    + " data.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            Map<String, String> customData = new HashMap<String, String>();
            customData.put("key1", "value1");
            customData.put("key2", "value2");
            Map<String, String> result = manager.updateRouteCustomData(routeId, customData);
            System.out.println("Updated custom data: " + result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
