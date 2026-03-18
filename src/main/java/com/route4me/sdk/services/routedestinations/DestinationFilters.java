package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class DestinationFilters {
    @SerializedName("search_query")
    private String searchQuery;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("route_created_date")
    private List<String> routeCreatedDate;

    @SerializedName("address_stop_type")
    private List<String> addressStopType;

    @SerializedName("stop_status_id")
    private List<String> stopStatusId;

    @SerializedName("view_mode")
    private String viewMode;

    @SerializedName("customer_po")
    private String customerPo;

    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("local_timezone")
    private String localTimezone;
}
