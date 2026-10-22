package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class OrderFilters {
    @SerializedName("search_query")
    private String searchQuery;

    @SerializedName("order_uuid")
    private List<String> orderUuid;

    @SerializedName("tracking_number")
    private List<String> trackingNumber;

    @SerializedName("day_scheduled_for_YYYYMMDD")
    private List<String> dayScheduledFor;
}
