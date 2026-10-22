package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/** One location of a {@link LocationCombinedData}. */
@Data
public class LocationCombinedDataItem {
    /** Unique identifier for the address ({@code address_id}). */
    @SerializedName("address_id")
    private Integer addressId;

    /** Primary address line ({@code address_1}). */
    @SerializedName("address_1")
    private String address1;

    /** Custom alias or label for the address ({@code address_alias}). */
    @SerializedName("address_alias")
    private String addressAlias;

    /** Latitude coordinate ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** Longitude coordinate ({@code lng}). */
    @SerializedName("lng")
    private Double lng;

    /** Name of external connection (e.g., third-party integration) ({@code external_connection_name}). */
    @SerializedName("external_connection_name")
    private String externalConnectionName;

    /** Token for external connection authentication ({@code external_connection_token}). */
    @ToString.Exclude
    @SerializedName("external_connection_token")
    private String externalConnectionToken;
}
