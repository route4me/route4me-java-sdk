package com.route4me.sdk.examples.routes;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.enums.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class GetRoute {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        Map<String, String> params = new HashMap<>();
        RouteManager routeManager = route4me.getRouteManager();
        DataObject route = routeManager.getRoute("624A65A3B122F779107C5908633EAEAD");
        System.out.println("Optimization Problem ID:" + route.getOptimization_problem_id());
        System.out.println("State:" + Constants.OptimizationState.get(route.getState().intValue()));
        
    }    
    
    
}
