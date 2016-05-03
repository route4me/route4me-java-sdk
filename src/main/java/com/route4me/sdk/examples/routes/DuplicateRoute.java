package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import java.util.List;

/**
 *
 * @author juan
 */
public class DuplicateRoute {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager routeManager = route4me.getRouteManager();
        List<Routes> routes = routeManager.getRoutes(10, 5);
        String routeID = routes.get(0).getRoute_id();
        Response response = routeManager.duplicateRoute(routeID);
        System.out.println(response.getResponseBody());
        
    }    
    
    
}
