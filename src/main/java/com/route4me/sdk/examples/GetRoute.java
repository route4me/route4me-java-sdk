package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Base;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Route;
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
        params.put("optimization_problem_id", "624A65A3B122F779107C5908633EAEAD");
        Route route = route4me.getRoute();
        route.setParams(params);
        Response response = route4me.getRouteFromAPI();
        DataObject responseObject = Base.GSONDeserializer.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = Base.GSONSerializer.toJson(responseObject);
        System.out.println(jsonResponse);
        System.out.println("Response Code:" + response.getResponseCode());        
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        System.out.println("State:" + Constants.OptimizationState.get(responseObject.getState().intValue()));
        
    }    
    
    
}
