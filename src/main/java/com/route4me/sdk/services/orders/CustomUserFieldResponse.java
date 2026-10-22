package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomUserFieldResponse {
    @SerializedName("data")
    private CustomUserField data;
}
