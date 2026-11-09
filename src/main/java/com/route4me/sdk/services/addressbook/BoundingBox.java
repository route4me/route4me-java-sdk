package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A rectangle on the map, by its edges in degrees, for the area filters of {@link AddressBookManagerV5}. */
@Data
public class BoundingBox {
    /** The latitude of the top edge ({@code top}). */
    @SerializedName("top")
    private Double top;

    /** The longitude of the left edge ({@code left}). */
    @SerializedName("left")
    private Double left;

    /** The latitude of the bottom edge ({@code bottom}). */
    @SerializedName("bottom")
    private Double bottom;

    /** The longitude of the right edge ({@code right}). */
    @SerializedName("right")
    private Double right;
}
