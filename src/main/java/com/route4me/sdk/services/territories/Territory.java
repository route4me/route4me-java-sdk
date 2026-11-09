package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/** A territory or an avoidance zone: an area, by its shape, with its name and color. */
@Data
public class Territory {

    /** The ID of the territory ({@code territory_id}). */
    @SerializedName("territory_id")
    @QueryParameter("territory_id")
    private String territoryId;

    /** Territory name ({@code territory_name}). */
    @SerializedName("territory_name")
    private String territoryName;

    /** Territory color ({@code territory_color}). */
    @SerializedName("territory_color")
    private String territoryColor;

    /** Member ID ({@code member_id}). */
    @SerializedName("member_id")
    private String memberID;

    /** An array of the address IDs covered by territory ({@code addresses}). */
    @SerializedName("addresses")
    private List<Integer> addresses;

    /** An array of the order IDs covered by territory ({@code orders}). */
    @SerializedName("orders")
    private List<Integer> orders;

    /** The number of addresses in the territory, from the V5 API when asked for ({@code addresses_count}). */
    @SerializedName("addresses_count")
    private Integer addressesCount;

    /** The number of orders in the territory, from the V5 API when asked for ({@code orders_count}). */
    @SerializedName("orders_count")
    private Integer ordersCount;

    /** The shape of the territory ({@code territory}). */
    @SerializedName("territory")
    private TerritoryData territory;
}
