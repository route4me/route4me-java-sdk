
package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The shape of a {@link Territory}. */
@Data
public class TerritoryData {

    /** Territory type (circle, rectangle, polygon) ({@code type}). */
    @SerializedName("type")
    private String type;
    /** Territory shape data ({@code data}). */
    @SerializedName("data")
    private List<String> data;
}
