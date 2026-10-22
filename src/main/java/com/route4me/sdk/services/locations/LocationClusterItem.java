package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A location that is not in a cluster. */
@Data
public class LocationClusterItem {
    /** Unique identifier for the address ({@code address_id}). */
    @SerializedName("address_id")
    private Integer addressId;

    /** Latitude coordinate ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** Longitude coordinate ({@code lng}). */
    @SerializedName("lng")
    private Double lng;

    /** Primary address line ({@code address_1}). */
    @SerializedName("address_1")
    private String address1;

    /** Custom alias or label for the address ({@code address_alias}). */
    @SerializedName("address_alias")
    private String addressAlias;
}
