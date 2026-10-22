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
 * Predicts how long an optimization will take, through the V4 API
 * ({@code https://api.route4me.com/api.v4/predict-optimization-time.php}).
 */
public class PredictOptimizationTimeManager extends Manager {


    /** The path of the prediction, on {@code api.route4me.com}. */
    public static final String OPTIMIZATION_PREDICTION_URL = "/api.v4/predict-optimization-time.php";


    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public PredictOptimizationTimeManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Predicts how long an optimization will take: {@code POST
     * https://api.route4me.com/api.v4/predict-optimization-time.php}, with the
     * optimization as the JSON body.
     *
     * @param parameters the optimization, as for {@code RoutingManager.runOptimization}
     * @return the predicted time of each step
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public OptimizationTimePrediction runOptimizationPrediction(OptimizationParameters parameters) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(OPTIMIZATION_PREDICTION_URL);
        return this.makeJSONRequest(RequestMethod.POST, builder, parameters, OptimizationTimePrediction.class);
    }


}
