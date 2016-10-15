package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Links {

    @SerializedName("optimization_problem")
    private String optimizationProblem;
    @SerializedName("route")
    private String route;
    @SerializedName("view")
    private String view;
}
