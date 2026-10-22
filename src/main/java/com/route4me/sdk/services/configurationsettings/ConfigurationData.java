package com.route4me.sdk.services.configurationsettings;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/** One configuration key of the member, as the V4 API returns it. */
@Data
public class ConfigurationData {
    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The name of the key ({@code config_key}). */
    @SerializedName("config_key")
    private String configKey;

    /**
     * The value of the key. A setting can hold a secret, such as the token of an integration, so
     * {@code toString()} leaves it out ({@code config_value}).
     */
    @ToString.Exclude
    @SerializedName("config_value")
    private String configValue;
}
