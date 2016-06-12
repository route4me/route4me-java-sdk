package com.route4me.sdk.model;

import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.Address;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Routes {

    private List<Address> addresses;
    @SerializedName("parameters")
    private Parameters parameters;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("member_email")
    private String memberEmail;
    private List<Direction> directions;
    @SerializedName("driver_alias")
    private String driverAlias;
    @SerializedName("vehicle_alias")
    private String vehicleAlias;
    @SerializedName("deleted")
    private Boolean deleted;
    private List<String> routes_ids;
    private List<String> tracking_history;
}
