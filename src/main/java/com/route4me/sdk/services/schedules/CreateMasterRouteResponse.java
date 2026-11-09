package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link SchedulesManager#createMasterRoute(CreateMasterRouteRequest)}. */
@Data
public class CreateMasterRouteResponse {
    /** Whether the master route was created ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** The route schedule of the master route ({@code data}). */
    @SerializedName("data")
    private RouteScheduleResponseData data;
}
