/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing.predict.optimization.time;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author Route4Me
 */

/** The predicted time of one step of an optimization. */
@Data
public class TimePredictionModel {

    /** The step, such as {@code matrix}, {@code optimization} or {@code direction} ({@code model}). */
    @SerializedName("model")
    private String model;
    /** The predicted time of the step ({@code value}). */
    @SerializedName("value")
    private Long value;
    /** Time unit (e.g. 'seconds', 'minutes') ({@code unit}). */
    @SerializedName("unit")
    private String unit;
}
