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

/**
 *
 * @author route4me
 */
@Data
public class RouteData {

    @QueryParameter("route_id")
    @SerializedName("route_id")
    private String routeID;

    @QueryParameter("route_name")
    @SerializedName("route_name")
    private String routeName;
    @QueryParameter("created_timestamp")
    @SerializedName("created_timestamp")
    private Number createdTimestamp;
    @QueryParameter("created_date")
    @SerializedName("created_date")
    private String createdDate;
    @QueryParameter("schedule_date")
    @SerializedName("schedule_date")
    private String scheduleDate;
    @QueryParameter("approved_to_execute")
    @SerializedName("approved_to_execute")
    private Boolean approvedToExecute;
    @QueryParameter("approved_to_execute_date")
    @SerializedName("approved_to_execute_date")
    private String approvedToExecuteDate;
    @QueryParameter("destination_count")
    @SerializedName("destination_count")
    private Integer destinationCount;
    @QueryParameter("visited_count")
    @SerializedName("visited_count")
    private Integer visitedCount;
    @QueryParameter("notes_count")
    @SerializedName("notes_count")
    private Integer notesCount;
    @QueryParameter("vehicle_id")
    @SerializedName("vehicle_id")
    private String vehicleID;
    @QueryParameter("member_id")
    @SerializedName("member_id")
    private Integer memberID;
    @QueryParameter("trip_distance")
    @SerializedName("trip_distance")
    private Double tripDistance;
    @QueryParameter("actual_travel_distance")
    @SerializedName("actual_travel_distance")
    private Double actualTravelDistance;
    @QueryParameter("actual_travel_time")
    @SerializedName("actual_travel_time")
    private String actualTravelTime;
    @QueryParameter("route_duration")
    @SerializedName("route_duration")
    private String routeDuration;
    @QueryParameter("route_progress")
    @SerializedName("route_progress")
    private Integer routeProgress;
    @QueryParameter("route_status")
    @SerializedName("route_status")
    private String routeStatus;
    @QueryParameter("is_roundtrip")
    @SerializedName("is_roundtrip")
    private Boolean isRoundtrip;
    @QueryParameter("route_tx_source")
    @SerializedName("route_tx_source")
    private String routeTXSource;
    @QueryParameter("smart_optimization_id")
    @SerializedName("smart_optimization_id")
    private String smartOptimizationID;
    @QueryParameter("distance_unit")
    @SerializedName("distance_unit")
    private String distanceUnit;
    @QueryParameter("depot_address_id")
    @SerializedName("depot_address_id")
    private Number depotAddressID;
    @QueryParameter("is_master")
    @SerializedName("is_master")
    private Boolean isMaster;
    @QueryParameter("is_last_locked")
    @SerializedName("is_last_locked")
    private Boolean isLastLocked;
    @QueryParameter("optimization_problem_id")
    @SerializedName("optimization_problem_id")
    private String optimizationProblemID;
    @QueryParameter("algorithm_type")
    @SerializedName("algorithm_type")
    private Integer algorithmType;
    @QueryParameter("organization_id")
    @SerializedName("organization_id")
    private Integer organization_id;
    @QueryParameter("master_route_id")
    @SerializedName("master_route_id")
    private String masterRouteID;
    @QueryParameter("route_end_time")
    @SerializedName("route_end_time")
    private String routeEndTime;
    @QueryParameter("original_route_id")
    @SerializedName("original_route_id")
    private String originalRouteID;
    @QueryParameter("root_member_id")
    @SerializedName("root_member_id")
    private Integer rootMemberID;

}
