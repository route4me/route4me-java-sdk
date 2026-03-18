package com.route4me.sdk.services.accountprofile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AccountProfile {
    @SerializedName("email")
    private String email;

    @SerializedName("member_id")
    private Long memberId;

    @SerializedName("api_key")
    private String apiKey;

    @SerializedName("root_member_id")
    private Long rootMemberId;

    @SerializedName("preferred_units")
    private String preferredUnits;
}
