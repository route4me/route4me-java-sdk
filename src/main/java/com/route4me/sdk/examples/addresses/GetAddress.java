package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.examples.routes.*;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.AddressesManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetAddress {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        Number routeDestinationId;
        String routeID = route.getRoute_id();
        DataObject responseObject = routeManager.getRoute(route.getOptimization_problem_id());
        routeDestinationId = responseObject.getAddresses().get(0).getRoute_destination_id();
        AddressesManager addressManager = route4me.getAddressesManager();
        Address address = addressManager.getAddress(routeID, routeDestinationId);
        System.out.println("Address: " + address.getAddress());
        System.out.println("Latitude: " + address.getLat());
        System.out.println("Longitude: " + address.getLng());

        
    }    
    
    
}
