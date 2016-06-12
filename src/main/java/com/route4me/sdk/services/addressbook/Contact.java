package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Contact {

    @SerializedName("address_id")
    private Number addressId;
    @SerializedName("address_1")
    private String address1;
    @SerializedName("address_2")
    private String address2;
    @SerializedName("address_alias")
    private String addressAlias;
    @SerializedName("address_group")
    private String addressGroup;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("address_email")
    private String addressEmail;
    @SerializedName("address_phone_number")
    private String addressPhoneNumber;
    @SerializedName("cached_lat")
    private Number cachedLat;
    @SerializedName("cached_lng")
    private Number cachedLng;
    @SerializedName("address_city")
    private String addressCity;
    @SerializedName("address_state_id")
    private String addressStateId;
    @SerializedName("address_country_id")
    private String addressCountryId;
    @SerializedName("address_zip")
    private String addressZip;
}
