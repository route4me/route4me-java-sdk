package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Break {
    @SerializedName("mode")
    private Integer mode;

    @SerializedName("mode_params")
    private List<Integer> modeParams;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("repeats_number")
    private Integer repeatsNumber;
}
