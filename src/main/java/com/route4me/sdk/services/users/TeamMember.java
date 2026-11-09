package com.route4me.sdk.services.users;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;
/** A team member, as the V5 team API returns it. */
@Data
public class TeamMember {
    /** An unique ID of the member ({@code member_id}). */
    @SerializedName("member_id") private Long memberId;
    /** An email of the member ({@code member_email}). */
    @SerializedName("member_email") private String memberEmail;
    /**
     * Member type. Available values: PRIMARY_ACCOUNT, SUB_ACCOUNT_ADMIN, SUB_ACCOUNT_REGIONAL_MANAGER,
     * SUB_ACCOUNT_DISPATCHER, SUB_ACCOUNT_PLANNER, SUB_ACCOUNT_DRIVER ({@code member_type}).
     */
    @SerializedName("member_type") private String memberType;
    /** A first name of the member ({@code member_first_name}). */
    @SerializedName("member_first_name") private String memberFirstName;
    /** A last name of the member ({@code member_last_name}). */
    @SerializedName("member_last_name") private String memberLastName;
    /** User's phone number ({@code member_phone}). */
    @SerializedName("member_phone") private String memberPhone;
    /** User zipcode ({@code member_zipcode}). */
    @SerializedName("member_zipcode") private String memberZipcode;
    /** The user's custom data ({@code custom_data}). */
    @SerializedName("custom_data") private Map<String, String> customData;
}
