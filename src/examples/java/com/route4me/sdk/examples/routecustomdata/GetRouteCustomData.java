package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.Map;

/** Reads the custom data of a route with {@link RouteCustomDataManager#getRouteCustomData}. */
public class GetRouteCustomData {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
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
