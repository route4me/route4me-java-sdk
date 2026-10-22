package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

/** Re-sequences all the stops of a route by distance with {@link RoutingManager#resequenceReoptimizeRoute}. */
public class ResequenceAllAddresses {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            StatusResponse result = manager.resequenceReoptimizeRoute("ROUTE_ID_HERE", false,
                    Constants.Optimize.DISTANCE.toString());
            System.out.println("Re-sequenced: " + result.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
