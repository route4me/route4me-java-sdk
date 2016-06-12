package com.route4me.sdk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Links {

    @SerializedName("optimization_problem")
    private String optimizationProblem;
    private String route;
    private String view;
}
