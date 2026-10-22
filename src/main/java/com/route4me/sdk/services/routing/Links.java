package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The URLs of an optimization and of its view. */
@Data
public class Links {

    /** The URL of the optimization ({@code optimization_problem}). */
    @SerializedName("optimization_problem")
    private String optimizationProblem;
    /** API URL Route GET call for the current route ({@code route}). */
    @SerializedName("route")
    private String route;
    /** A Link to the GET operation for the optimization problem ({@code view}). */
    @SerializedName("view")
    private String view;
}
