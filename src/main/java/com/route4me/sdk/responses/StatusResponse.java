package com.route4me.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class StatusResponse {
    @SerializedName("status")
    private Boolean status;
}
