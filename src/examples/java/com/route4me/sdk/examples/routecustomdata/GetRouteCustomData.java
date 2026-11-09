package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.Map;

/**
 * Reads the custom data of a route with {@link RouteCustomDataManager#getRouteCustomData}. Set {@code ROUTE_ID} to
 * one of your routes.
 */
public class GetRouteCustomData {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example reads its custom data.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            Map<String, String> customData = manager.getRouteCustomData(routeId);
            if (customData != null) {
                for (Map.Entry<String, String> entry : customData.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
