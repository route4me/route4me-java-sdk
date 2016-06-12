package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.model.Links;
import com.route4me.sdk.model.Optimizations;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.services.tracking.TrackingHistory;
import lombok.Data;

import java.util.List;

@Data
public class DataObject {

    private List<Address> addresses;
    @SerializedName("tracking_history")
    private List<TrackingHistory> trackingHistory;
    private Parameters parameters;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    private Number state;
    @SerializedName("sent_to_background")
    private Boolean sentToBackground;
    private Links links;
    private List<Routes> routes;
    private List<Optimizations> optimizations;
    @SerializedName("driver_alias")
    private String driverAlias;
}
