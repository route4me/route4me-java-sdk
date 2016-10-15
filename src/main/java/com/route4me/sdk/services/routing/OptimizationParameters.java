package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OptimizationParameters {
    @QueryParameter("optimization_problem_id")
    private String problemId;
    @QueryParameter("reoptimize")
    private Boolean reoptimize;
    @QueryParameter("show_directions")
    private Boolean showDirections;
    @SerializedName("parameters")
    private Parameters parameters;
    @SerializedName("addresses")
    private List<Address> addresses;
}
