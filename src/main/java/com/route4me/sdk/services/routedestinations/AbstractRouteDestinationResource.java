package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** The fields that every route destination of the V5 destination API has, with its planned and actual times. */
@Data
public abstract class AbstractRouteDestinationResource {
    /** ID of the route destination (stop) ({@code route_destination_id}). */
    @SerializedName("route_destination_id")
    private Long routeDestinationId;

    /** Human-readable name of the destination ({@code destination_name}). */
    @SerializedName("destination_name")
    private String destinationName;

    /** Planned service time formatted as "HHh:MMm" ({@code service_time}). */
    @SerializedName("service_time")
    private String serviceTime;

    /** Actual service time formatted as "HHh:MMm" ({@code actual_service_time}). */
    @SerializedName("actual_service_time")
    private String actualServiceTime;

    /** Reported on-site time formatted as "HHh:MMm" ({@code reported_time_on_site}). */
    @SerializedName("reported_time_on_site")
    private String reportedTimeOnSite;

    /** Geofence-detected service time formatted as "HHh:MMm" ({@code geofence_detected_service_time}). */
    @SerializedName("geofence_detected_service_time")
    private String geofenceDetectedServiceTime;

    /** Time arrived on time, formatted as "HHh:MMm" ({@code arrival_on_time}). */
    @SerializedName("arrival_on_time")
    private String arrivalOnTime;

    /** Time departed on time, formatted as "HHh:MMm" ({@code departure_on_time}). */
    @SerializedName("departure_on_time")
    private String departureOnTime;

    /** Running service time formatted as "HHh:MMm" ({@code running_service_time}). */
    @SerializedName("running_service_time")
    private String runningServiceTime;

    /** Running travel time formatted as "HHh:MMm" ({@code running_travel_time}). */
    @SerializedName("running_travel_time")
    private String runningTravelTime;

    /** Running wait time formatted as "HHh:MMm" ({@code running_wait_time}). */
    @SerializedName("running_wait_time")
    private String runningWaitTime;

    /** Running break time formatted as "HHh:MMm" ({@code running_break_time}). */
    @SerializedName("running_break_time")
    private String runningBreakTime;

    /**
     * Projected wait time before time-window opens, formatted as "HHh:MMm:SSs" ({@code
     * projected_wait_time_before_tw_open}).
     */
    @SerializedName("projected_wait_time_before_tw_open")
    private String projectedWaitTimeBeforeTwOpen;

    /**
     * Estimated wait time before time-window opens, formatted as "HHh:MMm:SSs" ({@code
     * estimated_wait_time_before_tw_open}).
     */
    @SerializedName("estimated_wait_time_before_tw_open")
    private String estimatedWaitTimeBeforeTwOpen;

    /** Arrival deviation in seconds, formatted as "HHh:MMm:SSs" ({@code arrival_deviation_seconds}). */
    @SerializedName("arrival_deviation_seconds")
    private String arrivalDeviationSeconds;

    /** Departure deviation in seconds, formatted as "HHh:MMm:SSs" ({@code departure_deviation_seconds}). */
    @SerializedName("departure_deviation_seconds")
    private String departureDeviationSeconds;

    /** "Yes" if the stop is a depot; otherwise "No" ({@code is_depot}). */
    @SerializedName("is_depot")
    private String isDepot;

    /** "Yes" if any time windows were violated; otherwise "No" ({@code time_windows_violated}). */
    @SerializedName("time_windows_violated")
    private String timeWindowsViolated;

    /** "Yes" if the stop has been visited; otherwise "No" ({@code is_visited}). */
    @SerializedName("is_visited")
    private String isVisited;

    /** "Yes" if the stop has been departed; otherwise "No" ({@code is_departed}). */
    @SerializedName("is_departed")
    private String isDeparted;

    /** "Yes" if the stop is a pickup; otherwise "No" ({@code pickup}). */
    @SerializedName("pickup")
    private String pickup;

    /** "Yes" if the stop is a dropoff; otherwise "No" ({@code dropoff}). */
    @SerializedName("dropoff")
    private String dropoff;

    /** "Yes" if this is a joint stop; otherwise "No" ({@code joint}). */
    @SerializedName("joint")
    private String joint;

    /** "Yes" if this is a bundle stop; otherwise "No" ({@code bundle}). */
    @SerializedName("bundle")
    private String bundle;

    /** Comma-separated list of required driver skills ({@code skills}). */
    @SerializedName("skills")
    private String skills;

    /** Running distance to this stop, formatted with unit (e.g., "12.34 mi") ({@code running_distance}). */
    @SerializedName("running_distance")
    private String runningDistance;

    /** Stop status label (e.g., "Completed"). Null if not yet assigned ({@code stop_status_id}). */
    @SerializedName("stop_status_id")
    private String stopStatusId;

    /** Workflow identifier (hexadecimal UUID) associated with this stop ({@code workflow_id}). */
    @SerializedName("workflow_id")
    private Integer workflowId;

    /** Stop type (e.g., "Dropoff", "Pickup", "Service") ({@code address_stop_type}). */
    @SerializedName("address_stop_type")
    private String addressStopType;

    /** Customer billing type code ({@code customer_billing_type}). */
    @SerializedName("customer_billing_type")
    private Integer customerBillingType;

    /** Human-readable label for the customer billing type ({@code customer_billing_type_label}). */
    @SerializedName("customer_billing_type_label")
    private String customerBillingTypeLabel;

    /** Location sitemap URL for this destination ({@code location_sitemap}). */
    @SerializedName("location_sitemap")
    private String locationSitemap;

    /** Service type identifier (32-character hex UUID). Null if not assigned ({@code service_type_id}). */
    @SerializedName("service_type_id")
    private String serviceTypeId;

    /** Human-readable label for the service type. Null if not assigned ({@code service_type_label}). */
    @SerializedName("service_type_label")
    private String serviceTypeLabel;

    /** The custom fields of the destination, as key-value pairs ({@code custom_fields}). */
    @SerializedName("custom_fields")
    private Map<String, String> customFields;

    /** Notes attached to this destination ({@code notes}). */
    @SerializedName("notes")
    private List<RouteDestinationNote> notes;
}
