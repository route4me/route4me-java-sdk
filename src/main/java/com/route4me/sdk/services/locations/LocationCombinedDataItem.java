package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationCombinedDataItem {
    @SerializedName("address_id")
    private Integer addressId;

    @SerializedName("address_1")
    private String address1;

    @SerializedName("address_alias")
    private String addressAlias;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    @SerializedName("external_connection_name")
    private String externalConnectionName;

    @SerializedName("external_connection_token")
    private String externalConnectionToken;
}
