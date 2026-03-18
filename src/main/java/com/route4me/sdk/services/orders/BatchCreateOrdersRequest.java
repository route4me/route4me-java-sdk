package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class BatchCreateOrdersRequest {
    @SerializedName("data")
    private List<OrderV5> data;
}
