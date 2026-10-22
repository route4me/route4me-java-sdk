package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The filters of a {@link LocationCombinedRequest}; filters left {@code null} are not sent. */
@Data
public class LocationFilters {
    /** Text to search for in the locations ({@code search_query}). */
    @SerializedName("search_query")
    private String searchQuery;

    /** The view the list is shown in ({@code view_mode}). */
    @SerializedName("view_mode")
    private String viewMode;

    /** Only locations with these stop types ({@code address_stop_type}). */
    @SerializedName("address_stop_type")
    private List<String> addressStopType;

    /** Only these address book contact IDs ({@code address_id}). */
    @SerializedName("address_id")
    private List<Integer> addressId;

    /** Only locations of these customer IDs ({@code customer_id}). */
    @SerializedName("customer_id")
    private List<String> customerId;

    /** Only depots ({@code true}), only other locations ({@code false}), or both ({@code is_depot}). */
    @SerializedName("is_depot")
    private List<Boolean> isDepot;

    /** Only locations in these territory IDs ({@code territories}). */
    @SerializedName("territories")
    private List<String> territories;

    /** Only locations inside this rectangle ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private LocationBoundingBox boundingBox;

    /** Only locations linked to these facility IDs ({@code facility_ids}). */
    @SerializedName("facility_ids")
    private List<String> facilityIds;

    /** Only locations of these location type IDs ({@code location_type_id}). */
    @SerializedName("location_type_id")
    private List<String> locationTypeId;
}
