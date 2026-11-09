package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A webinar registration, the body of {@link UsersManager#registerForWebinar(WebinarRegistration)}. */
@Data
public class WebinarRegistration {
    /** The email address of the person who registers ({@code email_address}). */
    @SerializedName("email_address")
    private String emailAddress;

    /** The first name ({@code first_name}). */
    @SerializedName("first_name")
    private String firstName;

    /** The last name ({@code last_name}). */
    @SerializedName("last_name")
    private String lastName;

    /** The phone number ({@code phone_number}). */
    @SerializedName("phone_number")
    private String phoneNumber;

    /** The company name ({@code company_name}). */
    @SerializedName("company_name")
    private String companyName;

    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The date of the webinar, such as {@code 2026-11-05 10:00:00} ({@code webinar_date}). */
    @SerializedName("webinar_date")
    private String webinarDate;
}
