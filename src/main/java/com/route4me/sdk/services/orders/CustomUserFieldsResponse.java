package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#getCustomUserFields()}. */
@Data
public class CustomUserFieldsResponse {
    /** Data ({@code data}). */
    @SerializedName("data")
    private List<CustomUserField> data;
}
