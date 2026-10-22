package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class BatchUpdateOrdersRequest {
    @SerializedName("data")
    private List<OrderV5> data;
}
