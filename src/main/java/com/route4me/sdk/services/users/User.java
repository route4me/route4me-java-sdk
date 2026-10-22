package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

/** A user of the account, as the V4 user API takes and returns it. */
@Data
public class User {

    /** Unique ID of the member inside the Route4Me system ({@code member_id}). */
    @SerializedName("member_id")
    private String memberId;
    /** First name of the user ({@code member_first_name}). */
    @SerializedName("member_first_name")
    private String memberFirstName;
    /** Last name of the user ({@code member_last_name}). */
    @SerializedName("member_last_name")
    private String memberLastName;
    /** User email ({@code member_email}). */
    @SerializedName("member_email")
    private String memberEmail;
    /** The password of the user, sent when the user is created; do not log it ({@code member_password}). */
    @ToString.Exclude
    @SerializedName("member_password")
    private String memberPassword;
    /** Unique ID of the account type ({@code account_type_id}). */
    @SerializedName("account_type_id")
    private String accountTypeId;
    /** Member type of the user account ({@code member_type}). */
    @SerializedName("member_type")
    private String memberType;
    /** Phone number of the user ({@code phone_number}). */
    @SerializedName("phone_number")
    private String phoneNumber;
    /** If true, the user has readonly access right ({@code readonly_user}). */
    @SerializedName("readonly_user")
    private String readonlyUser;
    /** If true, the superuser's addresses are visible to the user ({@code show_superuser_addresses}). */
    @SerializedName("show_superuser_addresses")
    private String showSuperuserAddresses;
    /** The API key of the user; a credential, so do not log it ({@code api_key}). */
    @ToString.Exclude
    @SerializedName("api_key")
    private String apiKey;
    /** The hashed member ID of the user ({@code hashed_member_id}). */
    @SerializedName("hashed_member_id")
    private String hashedMemberId;
    /** The user's account owner ID ({@code OWNER_MEMBER_ID}). */
    @SerializedName("OWNER_MEMBER_ID")
    private String ownerMemberID;
    /** The preferred distance unit: {@code mi} or {@code km} ({@code preferred_units}). */
    @SerializedName("preferred_units")
    private String preferredUnits;
    /** Preferred language (en, fr) ({@code preferred_language}). */
    @SerializedName("preferred_language")
    private String preferredLanguage;
    /** If true, the routed addresses will be hidden ({@code HIDE_ROUTED_ADDRESSES}). */
    @SerializedName("HIDE_ROUTED_ADDRESSES")
    private String hideRoutedAddresses;
    /** If true, the visited addresses will be hidden ({@code HIDE_VISITED_ADDRESSES}). */
    @SerializedName("HIDE_VISITED_ADDRESSES")
    private String hideVisitedAddresses;
    /** If true, the nonfuture routes will be hidden ({@code HIDE_NONFUTURE_ROUTES}). */
    @SerializedName("HIDE_NONFUTURE_ROUTES")
    private String hideNonFutureRoutes;
    /** If true, all drivers are visible to the user ({@code SHOW_ALL_DRIVERS}). */
    @SerializedName("SHOW_ALL_DRIVERS")
    private String showAllDrivers;
    /** If true, all vehicles are visible to the user ({@code SHOW_ALL_VEHICLES}). */
    @SerializedName("SHOW_ALL_VEHICLES")
    private String showAllVehicles;
    /** If true, the user has read-only access type ({@code READONLY_USER}). */
    @SerializedName("READONLY_USER")
    private String readOnlyUser;
    /** User's phone number ({@code member_phone}). */
    @SerializedName("member_phone")
    private String memberPhone;
    /** The ZIP code of the user ({@code member_zipcode}). */
    @SerializedName("member_zipcode")
    private String memberZipcode;
    /** Member's location timezone ({@code timezone}). */
    @SerializedName("timezone")
    private String timeZone;
    /** Birthdate of the user ({@code date_of_birth}). */
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    /** Registration state ID of a user ({@code user_reg_state_id}). */
    @SerializedName("user_reg_state_id")
    private String userRegStateID;
    /** Registration country ID of a user ({@code user_reg_country_id}). */
    @SerializedName("user_reg_country_id")
    private String userRegCountryID;
    /** URL to a member picture ({@code member_picture}). */
    @SerializedName("member_picture")
    private String memberPicture;
    /** Subordination level. 0 is the highest level ({@code level}). */
    @SerializedName("level")
    private int level;
    /** The custom data of the user, as key-value pairs ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, Object> customData;



}
