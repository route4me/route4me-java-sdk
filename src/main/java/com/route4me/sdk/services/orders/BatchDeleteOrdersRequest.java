package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class BatchDeleteOrdersRequest {
    @SerializedName("order_uuids")
    private List<String> orderUuids;
}
