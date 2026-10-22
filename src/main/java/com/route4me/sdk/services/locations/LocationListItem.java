package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationListItem {
    @SerializedName("address_id")
    private Integer addressId;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    @SerializedName("address_1")
    private String address1;

    @SerializedName("address_alias")
    private String addressAlias;
}
