package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class User {

    @SerializedName("member_id")
    private String memberId;
    @SerializedName("member_first_name")
    private String memberFirstName;
    @SerializedName("member_last_name")
    private String memberLastName;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("account_type_id")
    private String accountTypeId;
    @SerializedName("member_type")
    private String memberType;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("readonly_user")
    private String readonlyUser;
    @SerializedName("show_superuser_addresses")
    private String showSuperuserAddresses;
    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("hashed_member_id")
    private String hashedMemberId;

}
