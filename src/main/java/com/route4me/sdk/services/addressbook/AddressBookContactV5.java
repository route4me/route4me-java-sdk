package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;
@Data
public class AddressBookContactV5 {
    @SerializedName("address_id") private Long addressId;
    @SerializedName("member_id") private Long memberId;
    @SerializedName("address_1") private String address1;
    @SerializedName("address_2") private String address2;
    @SerializedName("address_alias") private String addressAlias;
    @SerializedName("address_group") private String addressGroup;
    @SerializedName("first_name") private String firstName;
    @SerializedName("last_name") private String lastName;
    @SerializedName("address_email") private String addressEmail;
    @SerializedName("address_phone_number") private String addressPhoneNumber;
    @SerializedName("cached_lat") private Double cachedLat;
    @SerializedName("cached_lng") private Double cachedLng;
    @SerializedName("curbside_lat") private Double curbsideLat;
    @SerializedName("curbside_lng") private Double curbsideLng;
    @SerializedName("address_city") private String addressCity;
    @SerializedName("address_state_id") private String addressStateId;
    @SerializedName("address_country_id") private String addressCountryId;
    @SerializedName("address_zip") private String addressZip;
    @SerializedName("local_time_window_start") private Long localTimeWindowStart;
    @SerializedName("local_time_window_end") private Long localTimeWindowEnd;
    @SerializedName("local_time_window_start_2") private Long localTimeWindowStart2;
    @SerializedName("local_time_window_end_2") private Long localTimeWindowEnd2;
    @SerializedName("address_custom_data") private Map<String, String> addressCustomData;
    @SerializedName("created_timestamp") private Long createdTimestamp;
    @SerializedName("territory_name") private String territoryName;
}
