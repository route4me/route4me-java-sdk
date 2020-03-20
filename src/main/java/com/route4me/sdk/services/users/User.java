package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
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
    @SerializedName("member_password")
    private String memberPassword;
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
    @SerializedName("OWNER_MEMBER_ID")
    private String ownerMemberID;
    @SerializedName("preferred_units")
    private String preferredUnits;
    @SerializedName("preferred_language")
    private String preferredLanguage;
    @SerializedName("HIDE_ROUTED_ADDRESSES")
    private String hideRoutedAddresses;
    @SerializedName("HIDE_VISITED_ADDRESSES")
    private String hideVisitedAddresses;
    @SerializedName("HIDE_NONFUTURE_ROUTES")
    private String hideNonFutureRoutes;
    @SerializedName("SHOW_ALL_DRIVERS")
    private String showAllDrivers;
    @SerializedName("SHOW_ALL_VEHICLES")
    private String showAllVehicles;
    @SerializedName("READONLY_USER")
    private String readOnlyUser;
    @SerializedName("member_phone")
    private String memberPhone;
    @SerializedName("member_zipcode")
    private String memberZipcode;
    @SerializedName("timezone")
    private String timeZone;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @SerializedName("user_reg_state_id")
    private String userRegStateID;
    @SerializedName("user_reg_country_id")
    private String userRegCountryID;
    @SerializedName("member_picture")
    private String memberPicture;
    @SerializedName("level")
    int level;
    @SerializedName("custom_data")
    private Map<String, Object> customData;
    
    
    
}
