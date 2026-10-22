package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of the contact list methods of {@link AddressBookManagerV5}. */
@Data
public class AddressBookContactsBodyRequest {
    /** Routes filters ({@code filters}). */
    @SerializedName("filters") private AddressBookFilters filters;
    /** You can sort the results using the specified fields ({@code order_by}). */
    @SerializedName("order_by") private List<String[]> orderBy;
    /** Pagination page number ({@code page}). */
    @SerializedName("page") private Integer page;
    /** Items per page ({@code per_page}). */
    @SerializedName("per_page") private Integer perPage;
    /**
     * Response fields to return. Supports one level of nesting with a wildcard, e.g. "route_data.is_depot" or
     * "route_data.*". Empty/omitted returns all fields; unknown fields are ignored ({@code fields}).
     */
    @SerializedName("fields") private List<String> fields;
}
