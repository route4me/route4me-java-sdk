package com.route4me.sdk.services.configurationsettings;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/** One configuration setting of a member, as the V5 API returns it. */
@Data
public class MemberConfigurationSetting {
    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The name of the key, up to 64 characters ({@code config_key}). */
    @SerializedName("config_key")
    private String configKey;

    /**
     * The value of the key. A setting can hold a secret, such as the token of an integration, so
     * {@code toString()} leaves it out ({@code config_value}).
     */
    @ToString.Exclude
    @SerializedName("config_value")
    private String configValue;

    /** Whether the value is hidden in the web app ({@code is_hidden_value}). */
    @SerializedName("is_hidden_value")
    private Boolean isHiddenValue;

    /** Whether the member may show a hidden value ({@code can_unhide_value}). */
    @SerializedName("can_unhide_value")
    private Boolean canUnhideValue;
}
