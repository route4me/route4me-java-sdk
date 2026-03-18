package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DeleteLocationTypeResponse {
    @SerializedName("status")
    private Boolean status;
}
