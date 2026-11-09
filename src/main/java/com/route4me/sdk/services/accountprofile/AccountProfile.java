package com.route4me.sdk.services.accountprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/**
 * The profile of the account that owns the API key, as
 * {@link AccountProfileManager#getAccountProfile()} returns it.
 */
@Data
public class AccountProfile {
    /** The email address of the account ({@code email}). */
    @SerializedName("email")
    private String email;

    /** The member ID of the account ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /**
     * The API key of the account. The API omits it when the account has the {@code USERS_PROFILE_HIDE_API_KEY} feature;
     * it is a credential, so do not log it, and {@code toString()} leaves it out ({@code api_key}).
     */
    @ToString.Exclude
    @SerializedName("api_key")
    private String apiKey;

    /** The member ID of the account owner (the root member) ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** The preferred distance unit, such as {@code MI} ({@code preferred_units}). */
    @SerializedName("preferred_units")
    private String preferredUnits;
}
