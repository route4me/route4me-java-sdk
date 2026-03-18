package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CreateMasterRouteResponse {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("data")
    private RouteScheduleResponseData data;
}
