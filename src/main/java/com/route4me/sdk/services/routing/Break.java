package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

@Data
public class Break {
    @SerializedName("mode")
    @QueryParameter("mode")
    private Integer mode;

    @SerializedName("mode_params")
    @QueryParameter("mode_params")
    private int[] modeParams;

    @SerializedName("duration")
    @QueryParameter("duration")
    private Integer duration;

    @SerializedName("repeats_number")
    @QueryParameter("repeats_number")
    private Integer repeatsNumber;
}
