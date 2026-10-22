package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.HashMap;
import java.util.Map;

public class UpdateRouteCustomData {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            String routeId = "ROUTE_ID_HERE";
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
