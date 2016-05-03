package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Direction;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.Step;
import com.route4me.sdk.model.enums.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class GetRouteManifest {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        Routes route = routes.get(0);
        route = routeManager.getRouteManifest(route.getRoute_id());
        System.out.println("Route ID:" + route.getRoute_id());
        for (Direction direction : route.getDirections()){
            System.out.println("Direction Name: " + direction.getLocation().getName());
            for (Step step: direction.getSteps()){
                System.out.println("Direction: " + step.getDirection());
                System.out.println("Distance: " + step.getDistance());
                System.out.println("Compass Direction: " + step.getCompass_direction());
            }
        }
    }    
    
    
}
