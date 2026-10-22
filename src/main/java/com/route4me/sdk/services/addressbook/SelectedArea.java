package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * An area drawn on the map, for the area filters of {@link AddressBookManagerV5}: a circle, a polygon or a
 * rectangle. The spec leaves the shape of {@code value} open; the C# SDK sends a circle as {@code center} and
 * {@code distance}, a polygon as {@code points} ({@code [[lat, lng], ...]}) and a rectangle as {@code top_left}
 * and {@code bottom_right} ({@code [lat, lng]}).
 */
@Data
public class SelectedArea {
    /** The kind of area: {@code circle}, {@code polygon} or {@code rect} ({@code type}). */
    @SerializedName("type")
    private String type;

    /** The geometry of the area, as JSON names and values ({@code value}). */
    @SerializedName("value")
    private Map<String, Object> value;
}
