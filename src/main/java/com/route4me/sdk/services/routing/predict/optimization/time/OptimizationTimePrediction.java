/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.predict.optimization.time;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Route4Me
 */
@Data
public class OptimizationTimePrediction {

    @SerializedName("time-prediction")
    private List<TimePredictionModel> timePrediction;

}
