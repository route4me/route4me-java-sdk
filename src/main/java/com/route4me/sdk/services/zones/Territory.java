package com.route4me.sdk.services.zones;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Territory {

    @SerializedName("territory_name")
    private String territoryName;
    @SerializedName("territory_color")
    private String territoryColor;
    @SerializedName("territory")
    private TerritoryData territory;
    @SerializedName("territory_id")
    private String territoryId;

}
