package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
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
    @QueryParameter("territory_id")
    private String territoryId;

}
