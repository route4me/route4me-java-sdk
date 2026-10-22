package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
import java.util.Map;

/** One event of an order's history, from {@link OrderManagerV5#getOrderHistory}. */
@Data
public class OrderHistoryEntry {
    /** When the event happened, as a Unix timestamp ({@code created_timestamp}). */
    @SerializedName("created_timestamp")
    private Long createdTimestamp;

    /** The order ID ({@code order_id}). */
    @SerializedName("order_id")
    private String orderId;

    /** The type of the event ({@code event_type}). */
    @SerializedName("event_type")
    private Integer eventType;

    /** The member of the order ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The account owner ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** The member who caused the event ({@code initiator_member_id}). */
    @SerializedName("initiator_member_id")
    private Long initiatorMemberId;

    /** The fields the event changed ({@code order_diffs}). */
    @SerializedName("order_diffs")
    private List<OrderDiff> orderDiffs;

    /** The order after the event ({@code order_model}). */
    @SerializedName("order_model")
    private OrderV5 orderModel;

    /** Data related to the event ({@code related_data}). */
    @SerializedName("related_data")
    private List<Map<String, Object>> relatedData;
}
