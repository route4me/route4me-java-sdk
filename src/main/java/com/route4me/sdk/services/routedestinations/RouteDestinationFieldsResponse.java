package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteDestinationsManager#getDestinationFields()}. */
@Data
public class RouteDestinationFieldsResponse {
    /** Array of field definitions available for filtering and sorting ({@code fields}). */
    @SerializedName("fields")
    private List<RouteDestinationFieldItem> fields;
}
