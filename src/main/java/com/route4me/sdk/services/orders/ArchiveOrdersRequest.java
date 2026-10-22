package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class ArchiveOrdersRequest {
    @SerializedName("order_ids")
    private List<String> orderIds;
}
