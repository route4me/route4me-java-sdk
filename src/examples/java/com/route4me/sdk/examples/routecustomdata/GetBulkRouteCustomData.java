package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataCollection;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.Arrays;

/**
 * Lists the custom data keys of the account with {@link RouteCustomDataManager#getCustomDataKeys}, then reads the
 * custom data of two routes at once with {@link RouteCustomDataManager#getBulkRouteCustomData}.
 */
public class GetBulkRouteCustomData {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            String[] keys = manager.getCustomDataKeys();
            System.out.println("Keys: " + Arrays.toString(keys));
            RouteCustomDataCollection data = manager.getBulkRouteCustomData(
                    new String[] {"ROUTE_ID_1_HERE", "ROUTE_ID_2_HERE"});
            System.out.println("Custom data by route: " + data.getData());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
