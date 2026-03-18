package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public abstract class AbstractRouteDestinationResource {
    @SerializedName("route_destination_id")
    private Integer routeDestinationId;

    @SerializedName("destination_name")
    private String destinationName;

    @SerializedName("service_time")
    private String serviceTime;

    @SerializedName("actual_service_time")
    private String actualServiceTime;

    @SerializedName("reported_time_on_site")
    private String reportedTimeOnSite;

    @SerializedName("geofence_detected_service_time")
    private String geofenceDetectedServiceTime;

    @SerializedName("arrival_on_time")
    private String arrivalOnTime;

    @SerializedName("departure_on_time")
    private String departureOnTime;

    @SerializedName("running_service_time")
    private String runningServiceTime;

    @SerializedName("running_travel_time")
    private String runningTravelTime;

    @SerializedName("running_wait_time")
    private String runningWaitTime;

    @SerializedName("running_break_time")
    private String runningBreakTime;

    @SerializedName("projected_wait_time_before_tw_open")
    private String projectedWaitTimeBeforeTwOpen;

    @SerializedName("estimated_wait_time_before_tw_open")
    private String estimatedWaitTimeBeforeTwOpen;

    @SerializedName("arrival_deviation_seconds")
    private String arrivalDeviationSeconds;

    @SerializedName("departure_deviation_seconds")
    private String departureDeviationSeconds;

    @SerializedName("is_depot")
    private String isDepot;

    @SerializedName("time_windows_violated")
    private String timeWindowsViolated;

    @SerializedName("is_visited")
    private String isVisited;

    @SerializedName("is_departed")
    private String isDeparted;

    @SerializedName("pickup")
    private String pickup;

    @SerializedName("dropoff")
    private String dropoff;

    @SerializedName("joint")
    private String joint;

    @SerializedName("bundle")
    private String bundle;

    @SerializedName("skills")
    private String skills;

    @SerializedName("running_distance")
    private String runningDistance;

    @SerializedName("stop_status_id")
    private String stopStatusId;

    @SerializedName("workflow_id")
    private Integer workflowId;

    @SerializedName("address_stop_type")
    private String addressStopType;

    @SerializedName("customer_billing_type")
    private Integer customerBillingType;

    @SerializedName("customer_billing_type_label")
    private String customerBillingTypeLabel;

    @SerializedName("location_sitemap")
    private String locationSitemap;

    @SerializedName("service_type_id")
    private String serviceTypeId;

    @SerializedName("service_type_label")
    private String serviceTypeLabel;

    @SerializedName("custom_fields")
    private Map<String, String> customFields;

    @SerializedName("notes")
    private List<RouteDestinationNote> notes;
}
