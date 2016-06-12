package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class UpdateRouteParameters {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        System.out.println("Route Name: " + route.getParameters().getRoute_name());
        Parameters parameters = new Parameters();
        parameters.setRoute_name("Updating Route Name");
        parameters.setMember_id("1");
        DataObject data = new DataObject();
        data.setParameters(parameters);
        System.out.println("Updating Route ID:" + route.getRoute_id());
        route = routeManager.updateRouteParameters(data, route.getRoute_id());
        System.out.println("Route ID: " + route.getRoute_id());
        System.out.println("Member Email: " + route.getMember_email());
        System.out.println("Member ID: " + route.getMember_id());
        System.out.println("Route Name: " + route.getParameters().getRoute_name());
        
    }    
    
    
}
