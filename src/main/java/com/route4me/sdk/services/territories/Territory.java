package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

@Data
public class Territory {

    @SerializedName("territory_id")
    @QueryParameter("territory_id")
    private String territoryId;

    @SerializedName("territory_name")
    private String territoryName;

    @SerializedName("territory_color")
    private String territoryColor;

    @SerializedName("member_id")
    private String memberID;

    @SerializedName("addresses")
    private List<Integer> addresses;

    @SerializedName("orders")
    private List<Integer> orders;
    
    @SerializedName("territory")
    private TerritoryData territory;
}
