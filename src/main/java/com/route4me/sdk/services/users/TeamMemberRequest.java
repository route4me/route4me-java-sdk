package com.route4me.sdk.services.users;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;
import java.util.Map;
/**
 * A team member to create or update, the body of {@link TeamManagementManagerV5#createTeamMember(TeamMemberRequest)}
 * and {@link TeamManagementManagerV5#updateTeamMember(long, TeamMemberRequest)}, and an item of
 * {@link TeamManagementManagerV5#bulkCreateTeamMembers(TeamMemberRequest[], String)}.
 */
@Data
public class TeamMemberRequest {
    /** The email address of the member ({@code member_email}). */
    @SerializedName("member_email") private String memberEmail;
    /** The first name of the member ({@code member_first_name}). */
    @SerializedName("member_first_name") private String memberFirstName;
    /** The last name of the member ({@code member_last_name}). */
    @SerializedName("member_last_name") private String memberLastName;
    /** The phone number of the member ({@code member_phone}). */
    @SerializedName("member_phone") private String memberPhone;
    /** User zipcode ({@code member_zipcode}). */
    @SerializedName("member_zipcode") private String memberZipcode;
    /** The company of the member ({@code member_company}). */
    @SerializedName("member_company") private String memberCompany;
    /** The time zone of the member, such as {@code America/New_York} ({@code timezone}). */
    @SerializedName("timezone") private String timezone;
    /** The preferred distance unit of the member, {@code mi} or {@code km} ({@code preferred_units}). */
    @SerializedName("preferred_units") private String preferredUnits;
    /**
     * The password of the member. It is sent as {@code new_password}, the field the V5 spec and the C# SDK use; the
     * API does not read {@code member_password}. {@code toString()} leaves it out.
     */
    @ToString.Exclude @SerializedName("new_password") private String memberPassword;
    /**
     * Member type. Available values: PRIMARY_ACCOUNT, SUB_ACCOUNT_ADMIN, SUB_ACCOUNT_REGIONAL_MANAGER,
     * SUB_ACCOUNT_DISPATCHER, SUB_ACCOUNT_PLANNER, SUB_ACCOUNT_DRIVER ({@code member_type}).
     */
    @SerializedName("member_type") private String memberType;
    /**
     * The member ID of the account owner of the new member; required to create a member
     * ({@code OWNER_MEMBER_ID}).
     */
    @SerializedName("OWNER_MEMBER_ID") private Long ownerMemberId;
    /**
     * The email address of the account owner; in a bulk insert it may replace {@code OWNER_MEMBER_ID}
     * ({@code owner_email}).
     */
    @SerializedName("owner_email") private String ownerEmail;
    /**
     * The user's custom data ({@code custom_data}). A driver's skills go in it as {@code driver_skills}, separated by
     * commas, as the C# SDK ({@code AddSkillsToDriver}) sends them.
     */
    @SerializedName("custom_data") private Map<String, String> customData;
}
