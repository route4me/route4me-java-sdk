package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * One rule of an {@link AddressBookGroupFilter}, such as "{@code address_1} contains {@code Main}". The field names
 * come from {@link AddressBookManagerV5#getCustomFields()}; a custom data field is named {@code custom_data.<name>}.
 */
@Data
public class AddressBookGroupRule {
    /** The ID of the rule, usually the field name ({@code id}). */
    @SerializedName("id")
    private String id;

    /** The contact field to test ({@code field}). */
    @SerializedName("field")
    private String field;

    /** The type of the field's values, such as {@code string} ({@code type}). */
    @SerializedName("type")
    private String type;

    /** The kind of input that edits the value, such as {@code text} ({@code input}). */
    @SerializedName("input")
    private String input;

    /** The test, such as {@code equal}, {@code contains} or {@code begins_with} ({@code operator}). */
    @SerializedName("operator")
    private String operator;

    /** The value to test against: a text, a number or a list ({@code value}). */
    @SerializedName("value")
    private Object value;
}
