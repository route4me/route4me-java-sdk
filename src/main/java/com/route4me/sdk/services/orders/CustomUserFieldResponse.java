package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the single custom-field methods of {@link OrderManagerV5}. */
@Data
public class CustomUserFieldResponse {
    /** The custom user field ({@code data}). */
    @SerializedName("data")
    private CustomUserField data;
}
