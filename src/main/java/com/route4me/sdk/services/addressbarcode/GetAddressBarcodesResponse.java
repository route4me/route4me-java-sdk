package com.route4me.sdk.services.addressbarcode;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class GetAddressBarcodesResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("next_page_cursor")
    private String nextPageCursor;

    @SerializedName("data")
    private List<BarcodeDataResponse> data;
}
