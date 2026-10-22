package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;
/** A contact, as the V5 address book API takes and returns it. */
@Data
public class AddressBookContactV5 {
    /** Unique ID of the contact ({@code address_id}). */
    @SerializedName("address_id") private Long addressId;
    /** Unique ID of the member ({@code member_id}). */
    @SerializedName("member_id") private Long memberId;
    /** The geographic address of the contact ({@code address_1}). */
    @SerializedName("address_1") private String address1;
    /** Second geographic address of the contact ({@code address_2}). */
    @SerializedName("address_2") private String address2;
    /** The contact's alias ({@code address_alias}). */
    @SerializedName("address_alias") private String addressAlias;
    /** A group the contact belongs ({@code address_group}). */
    @SerializedName("address_group") private String addressGroup;
    /** The first name of the contact person ({@code first_name}). */
    @SerializedName("first_name") private String firstName;
    /** The last name of the contact person ({@code last_name}). */
    @SerializedName("last_name") private String lastName;
    /** The contact's email ({@code address_email}). */
    @SerializedName("address_email") private String addressEmail;
    /** The contact's phone number ({@code address_phone_number}). */
    @SerializedName("address_phone_number") private String addressPhoneNumber;
    /** A latitude of the contact's cached position ({@code cached_lat}). */
    @SerializedName("cached_lat") private Double cachedLat;
    /** A longitude of the contact's cached position ({@code cached_lng}). */
    @SerializedName("cached_lng") private Double cachedLng;
    /** A latitude of the contact's curbside ({@code curbside_lat}). */
    @SerializedName("curbside_lat") private Double curbsideLat;
    /** A longitude of the contact's curbside ({@code curbside_lng}). */
    @SerializedName("curbside_lng") private Double curbsideLng;
    /** A city the contact belongs ({@code address_city}). */
    @SerializedName("address_city") private String addressCity;
    /** The ID of the state the contact belongs ({@code address_state_id}). */
    @SerializedName("address_state_id") private String addressStateId;
    /** The ID of the country the contact belongs ({@code address_country_id}). */
    @SerializedName("address_country_id") private String addressCountryId;
    /** The ZIP code the address is located in ({@code address_zip}). */
    @SerializedName("address_zip") private String addressZip;
    /** Start of the contact's local time window ({@code local_time_window_start}). */
    @SerializedName("local_time_window_start") private Long localTimeWindowStart;
    /** End of the contact's local time window ({@code local_time_window_end}). */
    @SerializedName("local_time_window_end") private Long localTimeWindowEnd;
    /** Start of the contact's second local time window ({@code local_time_window_start_2}). */
    @SerializedName("local_time_window_start_2") private Long localTimeWindowStart2;
    /** End of the contact's second local time window ({@code local_time_window_end_2}). */
    @SerializedName("local_time_window_end_2") private Long localTimeWindowEnd2;
    /** An array of the contact's custom field-value pairs ({@code address_custom_data}). */
    @SerializedName("address_custom_data") private Map<String, String> addressCustomData;
    /** Time when the contact was created ({@code created_timestamp}). */
    @SerializedName("created_timestamp") private Long createdTimestamp;
    /** A territory shape name the contact belongs ({@code territory_name}). */
    @SerializedName("territory_name") private String territoryName;
}
