package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/** The body of {@link LocationManager#exportLocations(LocationExportRequest)}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationExportRequest extends LocationCombinedRequest {
    /** Array of column field names to include in export ({@code columns}). */
    @SerializedName("columns")
    private List<String> columns;

    /** Specific address IDs to export (if specified, filters are ignored) ({@code address_ids}). */
    @SerializedName("address_ids")
    private List<Integer> addressIds;

    /** Address IDs to exclude from export ({@code excluded_ids}). */
    @SerializedName("excluded_ids")
    private List<Integer> excludedIds;

    /** Output filename (without extension) ({@code filename}). */
    @SerializedName("filename")
    private String filename;

    /** Export format: "csv", "xls", or "xlsx" ({@code format}). */
    @SerializedName("format")
    private String format;

    /** Timezone for date/time formatting (e.g., "America/New_York") ({@code tz}). */
    @SerializedName("tz")
    private String timezone;
}
