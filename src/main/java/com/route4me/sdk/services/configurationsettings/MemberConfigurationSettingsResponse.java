package com.route4me.sdk.services.configurationsettings;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link ConfigurationSettingsManagerV5#getMemberConfigurationSettings(String, String, String)}. */
@Data
public class MemberConfigurationSettingsResponse {
    /** The status of the request ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The settings ({@code data}). */
    @SerializedName("data")
    private List<MemberConfigurationSetting> data;
}
