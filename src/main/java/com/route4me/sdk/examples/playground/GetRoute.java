package com.route4me.sdk.examples.playground;

import com.route4me.sdk.examples.routes.*;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class GetRoute {

    public static void main(String[] args) {
        String apiKey = "75FD49F7292A61857E1C5989A65FBEB3";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            Route r = routeManager.getRoute(new RoutesRequest().setNotes(Boolean.TRUE).setDirections(Boolean.TRUE).setId("95F40848399ABF623E807EAE15805DF7"));
            System.out.println(r);
            for (Address address : r.getAddresses()) {
                    System.out.println(address);
            }
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }


}
