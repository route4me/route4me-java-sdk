package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteDestinationsManager#editDestinationColumns(EditDestinationColumnsRequest)}. */
@Data
public class EditDestinationColumnsResponse {
    /** Updated column order as saved by the API ({@code order}). */
    @SerializedName("order")
    private List<String> order;
}
