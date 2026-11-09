package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/**
 * The {@code search} of a V5 order search ({@link OrderManagerV5#searchOrders}) or of a batch update by filters
 * ({@link OrderManagerV5#batchUpdateFilter}); fields left {@code null} are not sent.
 */
@Data
public class OrderSearch {
    /** Text to search for; when it is set, {@code matches} and {@code terms} are ignored ({@code query}). */
    @SerializedName("query")
    private String query;

    /**
     * Fields to match loosely, such as {@code first_name}, {@code email}, {@code address_1} or {@code custom_data}
     * ({@code matches}).
     */
    @SerializedName("matches")
    private Map<String, Object> matches;

    /** Fields to match exactly, with the same keys as {@code matches} ({@code terms}). */
    @SerializedName("terms")
    private Map<String, Object> terms;
}
