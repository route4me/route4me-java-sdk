package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationTerritoryResource {
    @SerializedName("territory_id")
    private String territoryId;

    @SerializedName("territory_name")
    private String territoryName;

    @SerializedName("territory_color")
    private String territoryColor;

    @SerializedName("member_id")
    private Integer memberId;

    @SerializedName("locations_count")
    private Integer locationsCount;
}
