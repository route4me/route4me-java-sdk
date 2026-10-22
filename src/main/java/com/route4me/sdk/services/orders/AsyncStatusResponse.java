package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AsyncStatusResponse {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("async")
    private Boolean async;
}
