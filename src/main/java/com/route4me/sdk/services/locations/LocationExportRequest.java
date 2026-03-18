package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationExportRequest extends LocationCombinedRequest {
    @SerializedName("columns")
    private List<String> columns;

    @SerializedName("address_ids")
    private List<Integer> addressIds;

    @SerializedName("excluded_ids")
    private List<Integer> excludedIds;

    @SerializedName("filename")
    private String filename;

    @SerializedName("format")
    private String format;

    @SerializedName("tz")
    private String timezone;
}
