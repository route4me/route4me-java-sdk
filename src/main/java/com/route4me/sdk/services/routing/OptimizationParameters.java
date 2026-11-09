package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * An optimization to run or change: its route parameters and addresses, sent as the JSON body, and its ID and flags,
 * sent as query parameters where the method says so.
 */
@Data
@Accessors(chain = true)
public class OptimizationParameters {
    /** Optimization problem ID ({@code optimization_problem_id}). */
    @QueryParameter("optimization_problem_id")
    @SerializedName("optimization_problem_id")
    private String problemId;
    /** If true, the optimization will be reoptimized ({@code reoptimize}). */
    @QueryParameter("reoptimize")
    @SerializedName("reoptimize")
    private Boolean reoptimize;
    /** If true, the directions are shown in the optimization ({@code show_directions}). */
    @QueryParameter("show_directions")
    @SerializedName("show_directions")
    private Boolean showDirections;
    /** The route parameters of the optimization ({@code parameters}). */
    @SerializedName("parameters")
    private Parameters parameters;
    /** The addresses (stops) of the optimization ({@code addresses}). */
    @SerializedName("addresses")
    private List<Address> addresses;
}
