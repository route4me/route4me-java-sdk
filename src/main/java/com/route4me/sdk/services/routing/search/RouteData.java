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
package com.route4me.sdk.services.routing.search;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** A route found by {@code RoutingManager.getRoutesbyDateTimeZone}. */
@Data
public class RouteData {

    /** The route ID ({@code route_id}). */
    @QueryParameter("route_id")
    @SerializedName("route_id")
    private String routeID;

    /** The name of the route ({@code route_name}). */
    @QueryParameter("route_name")
    @SerializedName("route_name")
    private String routeName;
    /** When the route was created in Unix EPOCH format ({@code created_timestamp}). */
    @QueryParameter("created_timestamp")
    @SerializedName("created_timestamp")
    private Number createdTimestamp;
    /** When the route was created in human readable format ({@code created_date}). */
    @QueryParameter("created_date")
    @SerializedName("created_date")
    private String createdDate;
    /** The date the route is scheduled for ({@code schedule_date}). */
    @QueryParameter("schedule_date")
    @SerializedName("schedule_date")
    private String scheduleDate;
    /** If true, the route is approved to be executed ({@code approved_to_execute}). */
    @QueryParameter("approved_to_execute")
    @SerializedName("approved_to_execute")
    private Boolean approvedToExecute;
    /** The date the route was approved to be executed ({@code approved_to_execute_date}). */
    @QueryParameter("approved_to_execute_date")
    @SerializedName("approved_to_execute_date")
    private String approvedToExecuteDate;
    /** Destination count ({@code destination_count}). */
    @QueryParameter("destination_count")
    @SerializedName("destination_count")
    private Integer destinationCount;
    /** How many route addresses are visited ({@code visited_count}). */
    @QueryParameter("visited_count")
    @SerializedName("visited_count")
    private Integer visitedCount;
    /** Notes count in the route ({@code notes_count}). */
    @QueryParameter("notes_count")
    @SerializedName("notes_count")
    private Integer notesCount;
    /** The vehicle ID ({@code vehicle_id}). */
    @QueryParameter("vehicle_id")
    @SerializedName("vehicle_id")
    private String vehicleID;
    /** The member ID ({@code member_id}). */
    @QueryParameter("member_id")
    @SerializedName("member_id")
    private Integer memberID;
    /** Total route's trip distance (the unit is given by distance_unit) ({@code trip_distance}). */
    @QueryParameter("trip_distance")
    @SerializedName("trip_distance")
    private Double tripDistance;
    /** Actual travel distance ({@code actual_travel_distance}). */
    @QueryParameter("actual_travel_distance")
    @SerializedName("actual_travel_distance")
    private Double actualTravelDistance;
    /** Actual travel time (time span) ({@code actual_travel_time}). */
    @QueryParameter("actual_travel_time")
    @SerializedName("actual_travel_time")
    private String actualTravelTime;
    /** Total route duration in human readable format (e.g. 'HH:mm:ss') ({@code route_duration}). */
    @QueryParameter("route_duration")
    @SerializedName("route_duration")
    private String routeDuration;
    /** Route progress ({@code route_progress}). */
    @QueryParameter("route_progress")
    @SerializedName("route_progress")
    private Double routeProgress;
    /** Route status ({@code route_status}). */
    @QueryParameter("route_status")
    @SerializedName("route_status")
    private String routeStatus;
    /** If true, the tour type is round trip ({@code is_roundtrip}). */
    @QueryParameter("is_roundtrip")
    @SerializedName("is_roundtrip")
    private Boolean isRoundtrip;
    /** Route TX source ({@code route_tx_source}). */
    @QueryParameter("route_tx_source")
    @SerializedName("route_tx_source")
    private String routeTXSource;
    /** The ID of the smart optimization of the route, if any ({@code smart_optimization_id}). */
    @QueryParameter("smart_optimization_id")
    @SerializedName("smart_optimization_id")
    private String smartOptimizationID;
    /** The distance measurement unit for the route ({@code distance_unit}). */
    @QueryParameter("distance_unit")
    @SerializedName("distance_unit")
    private String distanceUnit;
    /** Depot address ID ({@code depot_address_id}). */
    @QueryParameter("depot_address_id")
    @SerializedName("depot_address_id")
    private Number depotAddressID;
    /** If true, the route is master ({@code is_master}). */
    @QueryParameter("is_master")
    @SerializedName("is_master")
    private Boolean isMaster;
    /** If true, the route trip is finished at last destination ({@code is_last_locked}). */
    @QueryParameter("is_last_locked")
    @SerializedName("is_last_locked")
    private Boolean isLastLocked;
    /** The ID of the optimization of the route ({@code optimization_problem_id}). */
    @QueryParameter("optimization_problem_id")
    @SerializedName("optimization_problem_id")
    private String optimizationProblemID;
    /** The optimization algorithm of the route; see {@code Constants.AlgorithmType} ({@code algorithm_type}). */
    @QueryParameter("algorithm_type")
    @SerializedName("algorithm_type")
    private Integer algorithmType;
    /** The ID of the organization of the route ({@code organization_id}). */
    @QueryParameter("organization_id")
    @SerializedName("organization_id")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private Integer organization_id;
    /** The ID of the master route this route was created from, if any ({@code master_route_id}). */
    @QueryParameter("master_route_id")
    @SerializedName("master_route_id")
    private String masterRouteID;
    /** When the route finished (or canceled) (day time in seconds) ({@code route_end_time}). */
    @QueryParameter("route_end_time")
    @SerializedName("route_end_time")
    private String routeEndTime;
    /** Original route ID ({@code original_route_id}). */
    @QueryParameter("original_route_id")
    @SerializedName("original_route_id")
    private String originalRouteID;
    /** Route member ID ({@code root_member_id}). */
    @QueryParameter("root_member_id")
    @SerializedName("root_member_id")
    private Integer rootMemberID;

}
