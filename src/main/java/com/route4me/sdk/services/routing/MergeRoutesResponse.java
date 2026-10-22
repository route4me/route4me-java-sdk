package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link RoutingManager#mergeRoutes(java.util.List, String, Boolean, Double, Double)}. */
@Data
public class MergeRoutesResponse {
    /** Whether the routes were merged ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** The ID of the optimization that holds the merged route ({@code optimization_problem_id}). */
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
}
