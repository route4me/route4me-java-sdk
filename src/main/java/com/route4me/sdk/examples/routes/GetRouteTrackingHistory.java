package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Direction;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.Step;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetRouteTrackingHistory {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        route = routeManager.getRouteTracking(route.getRoute_id());
        System.out.println("Route ID:" + route.getRoute_id());
        System.out.println("Tracking History");
        for (String track: route.getTracking_history()){
            System.out.println(track);      
        }
    }    
    
    
}
