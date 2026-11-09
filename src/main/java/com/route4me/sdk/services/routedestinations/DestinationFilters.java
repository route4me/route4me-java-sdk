package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The filters of a {@link GetDestinationsRequest}; filters left {@code null} are not sent. */
@Data
public class DestinationFilters {
    /** Free-text search across destination fields ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /** Filter by route ID (32-character hex string) ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;

    /** Filter by vehicle ID (32-character hex string) ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;

    /** Filter by driver/member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** Filter by route creation date range [from, to] in "YYYY-MM-DD" format ({@code route_created_date}). */
    @SerializedName("route_created_date")
    private List<String> routeCreatedDate;

    /**
     * Filter by stop type. Valid values: PICKUP, DELIVERY, BREAK, MEETUP, SERVICE, VISIT, DRIVEBY ({@code
     * address_stop_type}).
     */
    @SerializedName("address_stop_type")
    private List<String> addressStopType;

    /** Filter by stop status IDs ({@code stop_status_id}). */
    @SerializedName("stop_status_id")
    private List<String> stopStatusId;

    /** View mode selector (UI-specific) ({@code view_mode}). */
    @SerializedName("view_mode")
    private String viewMode;

    /** Filter by customer purchase order number ({@code customer_po}). */
    @SerializedName("customer_po")
    private String customerPo;

    /** Filter by optimization problem ID (32-character hex string) ({@code optimization_problem_id}). */
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;

    /** Filter by customer ID (32-character hex string) ({@code customer_id}). */
    @SerializedName("customer_id")
    private String customerId;

    /** Local timezone identifier used for interpreting date-based filters ({@code local_timezone}). */
    @SerializedName("local_timezone")
    private String localTimezone;
}
