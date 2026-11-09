package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A contact field that address book groups can filter on, in a {@link CustomFieldsResponse}. */
@Data
public class CustomField {
    /** The name to show ({@code label}). */
    @SerializedName("label")
    private String label;

    /** The field, as the group rules name it ({@code value}). */
    @SerializedName("value")
    private String value;

    /** {@code static} for a built-in field, {@code dynamic} for a custom data field ({@code kind}). */
    @SerializedName("kind")
    private String kind;

    /** The type of the values ({@code type}). */
    @SerializedName("type")
    private String type;

    /** The rule operators the field does not support ({@code operators_exclude}). */
    @SerializedName("operators_exclude")
    private List<String> operatorsExclude;
}
