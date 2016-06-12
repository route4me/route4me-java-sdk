package com.route4me.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DeleteResponse {
    @SerializedName("status")
    private boolean deleted;
}
