package com.route4me.sdk.services.configurationsettings;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link ConfigurationSettingsManager#getConfigurationData(String)}. */
@Data
public class ConfigurationDataResponse {
    /** {@code OK} when the request succeeded ({@code result}). */
    @SerializedName("result")
    private String result;

    /** The keys ({@code data}). */
    @SerializedName("data")
    private List<ConfigurationData> data;

    /** The number of keys returned ({@code affected}). */
    @SerializedName("affected")
    private Integer affected;
}
