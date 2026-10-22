package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 * One bundle of a route or of a route destination: the stops that the bundling options
 * merged into one, with their combined load. The API fills it in; it is read-only.
 *
 * <p>Matches {@code BundledItemResponse} of the C# SDK.
 */
@Data
public class BundledItem {

    /** The combined cube (volume) of the bundled stops. */
    @SerializedName("cube")
    private Double cube;
    /** The combined revenue of the bundled stops. */
    @SerializedName("revenue")
    private Double revenue;
    /** The combined number of pieces of the bundled stops. */
    @SerializedName("pieces")
    private Integer pieces;
    /** The combined weight of the bundled stops. */
    @SerializedName("weight")
    private Double weight;
    /** The combined cost of the bundled stops. */
    @SerializedName("cost")
    private Double cost;
    /** The service time of the bundle, in seconds. */
    @SerializedName("service_time")
    private Integer serviceTime;
    /** The start of the bundle's time window, in seconds. */
    @SerializedName("time_window_start")
    private Integer timeWindowStart;
    /** The end of the bundle's time window, in seconds. */
    @SerializedName("time_window_end")
    private Integer timeWindowEnd;
    /** The custom data of the bundle, as the API sends it. */
    @SerializedName("custom_data")
    private Object customData;
    /** The IDs of the bundled addresses. */
    @SerializedName("addresses_id")
    private List<Long> addressesId;
}
