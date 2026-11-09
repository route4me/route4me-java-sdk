package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The contacts that {@link AddressBookManagerV5#updateAddressBookContactByAreas} and
 * {@link AddressBookManagerV5#removeAddressBookContactsByAreas} work on: those inside drawn areas or a rectangle,
 * optionally narrowed by a text. Filters left {@code null} are not sent. Both methods refuse a filter with neither a
 * drawn area nor a rectangle with its four edges, since an empty filter could match every contact.
 */
@Data
public class AddressBookAreasFilter {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Only the contacts inside this rectangle ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private BoundingBox boundingBox;

    /** Only the contacts inside these areas ({@code selected_areas}). */
    @SerializedName("selected_areas")
    private List<SelectedArea> selectedAreas;
}
