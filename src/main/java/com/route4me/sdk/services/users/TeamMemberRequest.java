package com.route4me.sdk.services.users;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;
@Data
public class TeamMemberRequest {
    @SerializedName("member_email") private String memberEmail;
    @SerializedName("member_first_name") private String memberFirstName;
    @SerializedName("member_last_name") private String memberLastName;
    @SerializedName("member_phone") private String memberPhone;
    @SerializedName("member_zipcode") private String memberZipcode;
    @SerializedName("member_password") private String memberPassword;
    @SerializedName("member_type") private String memberType;
    @SerializedName("custom_data") private Map<String, String> customData;
}
