/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.predict.optimization.time;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.OptimizationParameters;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author Route4Me
 */
public class PredictOptimizationTimeManager extends Manager {
    
    
    public static final String OPTIMIZATION_PREDICTION_URL = "/api.v4/predict-optimization-time.php";

    
    public PredictOptimizationTimeManager(String apiKey) {
        super(apiKey);
    }
    
    public OptimizationTimePrediction runOptimizationPrediction(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_PREDICTION_URL);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, OptimizationTimePrediction.class);
    }    
    
    
}
