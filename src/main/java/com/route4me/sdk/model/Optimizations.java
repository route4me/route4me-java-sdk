package com.route4me.sdk.model;

import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.Address;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Optimizations {
    private Parameters parameters;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    private Number state;
    @SerializedName("sent_to_background")
    private Boolean sentToBackground;
    private Links links;
    private List<Routes> routes;
    private List<Address> addresses;
}
