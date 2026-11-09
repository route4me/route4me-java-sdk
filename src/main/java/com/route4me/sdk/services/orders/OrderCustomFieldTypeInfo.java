package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** The labels and allowed values of an {@link OrderCustomUserField}. */
@Data
public class OrderCustomFieldTypeInfo {

    /** The short label of a {@code checkbox} field ({@code short_label}). */
    @SerializedName("short_label")
    private String shortLabel;
    /** The short caption of a {@code dropdown} field ({@code short_caption}). */
    @SerializedName("short_caption")
    private String shortCaption;
    /** The allowed values of a {@code dropdown} field ({@code allowed_values}). */
    @SerializedName("allowed_values")
    private List<String> allowedValues;
}
