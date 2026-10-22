/*
 * The MIT License
 *
 * Copyright 2022 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.services.routing.dynamic.insert;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/**
 *
 * @author route4me
 */

/** A route where a new destination fits, with what it costs the route. */
@Data
public class MatchedRoute {

    /** Route name ({@code route_name}). */
    @SerializedName("route_name")
    @QueryParameter("route_name")
    private String routeName;

    /** Route ID ({@code route_id}). */
    @SerializedName("route_id")
    @QueryParameter("route_id")
    private String routeID;

    /** The stop number at which to insert the new destination ({@code recommended_insertion_stop_number}). */
    @SerializedName("recommended_insertion_stop_number")
    @QueryParameter("recommended_insertion_stop_number")
    private Long recommendedInsertionStopNumber;

    /** The distance of the route without it ({@code old_distance}). */
    @SerializedName("old_distance")
    @QueryParameter("old_distance")
    private Double oldDistance;

    /** The distance of the route with the new destination ({@code new_distance}). */
    @SerializedName("new_distance")
    @QueryParameter("new_distance")
    private Double newDistance;

    /** The duration of the route without it ({@code old_time}). */
    @SerializedName("old_time")
    @QueryParameter("old_time")
    private Double oldTime;

    /** The duration of the route with the new destination ({@code new_time}). */
    @SerializedName("new_time")
    @QueryParameter("new_time")
    private Double newTime;

    /** How much longer, in percent, the route is with the new destination ({@code percentage_distance_increase}). */
    @SerializedName("percentage_distance_increase")
    @QueryParameter("percentage_distance_increase")
    private Double percentageDistanceIncrease;

    /** How much longer, in percent, the route takes with the new destination ({@code percentage_time_increase}). */
    @SerializedName("percentage_time_increase")
    @QueryParameter("percentage_time_increase")
    private Double percentageTimeIncrease;

    /** Whether the new destination breaks a time window of the route ({@code time_violation}). */
    @SerializedName("time_violation")
    @QueryParameter("time_violation")
    private Double timeViolation;

}
