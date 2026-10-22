package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A rectangle on the map, by its edges. */
@Data
public class LocationBoundingBox {
    /** Top (northern) latitude boundary ({@code top}). */
    @SerializedName("top")
    private Double top;

    /** Left (western) longitude boundary ({@code left}). */
    @SerializedName("left")
    private Double left;

    /** Bottom (southern) latitude boundary ({@code bottom}). */
    @SerializedName("bottom")
    private Double bottom;

    /** Right (eastern) longitude boundary ({@code right}). */
    @SerializedName("right")
    private Double right;
}
