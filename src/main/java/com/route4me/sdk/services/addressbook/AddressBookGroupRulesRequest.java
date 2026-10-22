package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBookManagerV5#filterContactsByGroupRules(AddressBookGroupRulesRequest, String)}: group
 * rules, and the range, fields and order of the contacts that match them. {@code rules} is required; fields left
 * {@code null} are not sent.
 */
@Data
public class AddressBookGroupRulesRequest {
    /** The rules the contacts must match; required ({@code rules}). */
    @SerializedName("rules")
    private AddressBookGroupFilter rules;

    /** Only the contacts inside this rectangle; all four edges are then required ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private BoundingBox boundingBox;

    /** The fields to return for each contact ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /**
     * The sort order, as {@code [field, direction]} pairs; the fields are those of
     * the {@code order_by} of {@link AddressBookContactsBodyRequest} ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** The number of contacts to return, up to 10000, the default ({@code limit}). */
    @SerializedName("limit")
    private Integer limit;

    /** The number of contacts to skip ({@code offset}). */
    @SerializedName("offset")
    private Integer offset;
}
