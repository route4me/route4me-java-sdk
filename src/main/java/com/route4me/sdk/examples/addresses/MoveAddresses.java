package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

public class MoveAddresses {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> data = manager.getOptimizations(10, 5);
            DataObject obj = data.get(0);
            List<Route> routes = manager.getRoutes(new RoutesRequest().setLimit(10));
            DataObject newObj = manager.moveAddresses(obj, routes.get(2).getId());
            System.out.println(newObj);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
