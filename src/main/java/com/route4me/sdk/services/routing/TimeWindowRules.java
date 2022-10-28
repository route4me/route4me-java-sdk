package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

@Data
public class TimeWindowRules {


    @SerializedName("resolve_strategy")
    @QueryParameter("resolve_strategy")
    private Integer resolveStrategy;
    @SerializedName("bundle_only_overlapping")
    @QueryParameter("bundle_only_overlapping")
    private Boolean bundleOnlyOverlapping;
    @SerializedName("bundle_service_time_greater_than_tw")
    @QueryParameter("bundle_service_time_greater_than_tw")
    private Boolean bundleServiceTimeGreaterThanTW;


}
