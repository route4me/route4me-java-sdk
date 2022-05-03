package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.tracking.TrackingHistory;
import lombok.Data;

import java.util.List;

@Data
public class DataObject {

    @SerializedName("addresses")
    private List<Address> addresses;
    @SerializedName("tracking_history")
    private List<TrackingHistory> trackingHistory;
    @SerializedName("parameters")
    private Parameters parameters;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("state")
    private Number state;
    @SerializedName("sent_to_background")
    private Boolean sentToBackground;
    @SerializedName("links")
    private Links links;
    @SerializedName("driver_alias")
    private String driverAlias;
    @SerializedName("optimal_position")
    private Boolean optimalPosition;

}
