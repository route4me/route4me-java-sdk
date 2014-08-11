package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Base;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Optimization;
import com.route4me.sdk.model.Optimizations;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Route;
import com.route4me.sdk.model.enums.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class GetOptimizationProblemsID {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        Response response = route4me.getOptimizationsIDFromAPI();        
        DataObject responseObject = Base.GSONDeserializer.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = Base.GSONSerializer.toJson(responseObject);
        System.out.println(jsonResponse);
        
    }    
    
    
}
