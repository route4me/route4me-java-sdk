package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** How the time windows of bundled stops are combined, in {@link Bundling}. */
@Data
public class TimeWindowRules {


    /**
     * How the time window of a bundle is built from its stops. 0 = min-max (default), 1 = max-min ({@code
     * resolve_strategy}).
     */
    @SerializedName("resolve_strategy")
    @QueryParameter("resolve_strategy")
    private Integer resolveStrategy;
    /** If true, only stops with overlapping time windows are bundled ({@code bundle_only_overlapping}). */
    @SerializedName("bundle_only_overlapping")
    @QueryParameter("bundle_only_overlapping")
    private Boolean bundleOnlyOverlapping;
    /**
     * Whether stops whose service time is longer than their time window can be bundled ({@code
     * bundle_service_time_greater_than_tw}).
     */
    @SerializedName("bundle_service_time_greater_than_tw")
    @QueryParameter("bundle_service_time_greater_than_tw")
    private Boolean bundleServiceTimeGreaterThanTW;


}
