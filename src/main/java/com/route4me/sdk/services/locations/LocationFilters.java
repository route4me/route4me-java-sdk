package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationFilters {
    @SerializedName("search_query")
    private String searchQuery;

    @SerializedName("view_mode")
    private String viewMode;

    @SerializedName("address_stop_type")
    private List<String> addressStopType;

    @SerializedName("address_id")
    private List<Integer> addressId;

    @SerializedName("customer_id")
    private List<String> customerId;

    @SerializedName("is_depot")
    private List<Boolean> isDepot;

    @SerializedName("territories")
    private List<String> territories;

    @SerializedName("bounding_box")
    private LocationBoundingBox boundingBox;

    @SerializedName("facility_ids")
    private List<String> facilityIds;

    @SerializedName("location_type_id")
    private List<String> locationTypeId;
}
