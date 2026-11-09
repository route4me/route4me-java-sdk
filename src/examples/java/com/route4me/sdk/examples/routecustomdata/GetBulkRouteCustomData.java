package com.route4me.sdk.examples.routecustomdata;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataCollection;
import com.route4me.sdk.services.routecustomdata.RouteCustomDataManager;

import java.util.Arrays;
import java.util.List;

/**
 * Lists the custom data keys of the account with {@link RouteCustomDataManager#getCustomDataKeys}, then reads the
 * custom data of several routes at once with {@link RouteCustomDataManager#getBulkRouteCustomData}. Set
 * {@code ROUTE_IDS} to the comma-separated IDs of some of your routes.
 */
public class GetBulkRouteCustomData {
    public static void main(String[] args) {
        List<String> routeIds = ExampleInput.toList("ROUTE_IDS", System.getenv("ROUTE_IDS"));
        if (routeIds == null) {
            return;
        }
        if (routeIds.isEmpty()) {
            System.err.println("Set ROUTE_IDS to the comma-separated IDs of some of your routes; this example reads"
                    + " their custom data.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteCustomDataManager manager = new RouteCustomDataManager(apiKey);
        try {
            String[] keys = manager.getCustomDataKeys();
            System.out.println("Keys: " + Arrays.toString(keys));
            RouteCustomDataCollection data = manager.getBulkRouteCustomData(routeIds.toArray(new String[0]));
            System.out.println("Custom data by route: " + data.getData());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
