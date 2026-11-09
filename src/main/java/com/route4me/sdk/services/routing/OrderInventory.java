package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * One inventory line of the order behind a route destination, as in
 * {@code Address.getOrderInventory()}.
 *
 * <p>Matches {@code OrderInventory} of the C# SDK.
 */
@Data
public class OrderInventory {

    /** The inventory item ID. */
    @SerializedName("inventory_id")
    private Long inventoryId;
    /** The ID of the order the line belongs to. */
    @SerializedName("order_id")
    private Long orderId;
    /** The item name. */
    @SerializedName("name")
    private String name;
    /** The quantity of the item. */
    @SerializedName("quantity")
    private Integer quantity;
    /** The total weight of the line. */
    @SerializedName("total_weight")
    private Double totalWeight;
    /** The total volume of the line. */
    @SerializedName("total_volume")
    private Double totalVolume;
    /** The total cost of the line. */
    @SerializedName("total_cost")
    private Double totalCost;
    /** The total price of the line. */
    @SerializedName("total_price")
    private Double totalPrice;
    /** When the line was created. */
    @SerializedName("created_at")
    private String createdAt;
    /** When the line was last updated. */
    @SerializedName("updated_at")
    private String updatedAt;
}
