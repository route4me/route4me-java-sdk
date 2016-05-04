package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetRoutes {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        for (Routes route: routes){
            System.out.println("Route ID" + route.getRoute_id());
            System.out.println("Optimization Problem ID: " + route.getOptimization_problem_id());
            System.out.println();
        }

        
    }    
    
    
}
