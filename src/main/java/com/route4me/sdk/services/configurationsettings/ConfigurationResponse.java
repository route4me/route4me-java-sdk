package com.route4me.sdk.services.configurationsettings;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer of the V4 methods of {@link ConfigurationSettingsManager} that create, change or delete a key, as the
 * C# SDK ({@code MemberConfigurationResponse}) reads it.
 */
@Data
public class ConfigurationResponse {
    /** {@code OK} when the request succeeded ({@code result}). */
    @SerializedName("result")
    private String result;

    /** The number of keys created, changed or deleted ({@code affected}). */
    @SerializedName("affected")
    private Integer affected;
}
