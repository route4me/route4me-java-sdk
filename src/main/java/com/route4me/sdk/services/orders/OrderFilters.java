package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The filters of a V5 order search or batch update; filters left {@code null} are not sent. */
@Data
public class OrderFilters {
    /** Text to search for in the orders ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /** Parent order ID as a 32-character hex string. Ignored when the order is not found ({@code order_uuid}). */
    @SerializedName("order_uuid")
    private List<String> orderUuid;

    /**
     * System-wide unique code, which permits end-users (recipients) to track the status of their order ({@code
     * tracking_number}).
     */
    @SerializedName("tracking_number")
    private List<String> trackingNumber;

    /** Only orders scheduled for these dates, as {@code YYYY-MM-DD} ({@code day_scheduled_for_YYYYMMDD}). */
    @SerializedName("day_scheduled_for_YYYYMMDD")
    private List<String> dayScheduledFor;
}
