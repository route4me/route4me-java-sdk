package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The filters of an {@link AddressBookContactsBodyRequest}; filters left {@code null} are not sent. */
@Data
public class AddressBookFilters {
    /** Text to search for in the contacts ({@code search_query}). */
    @SerializedName("search_query") private String searchQuery;
    /** Unique ID of an address book contact ({@code address_id}). */
    @SerializedName("address_id") private List<Long> addressId;
    /**
     * Specifies which address book contacts to display. Available values are: 'all', 'routed', 'unrouted' ({@code
     * display}).
     */
    @SerializedName("display") private List<String> display;
}
