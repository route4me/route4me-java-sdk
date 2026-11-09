package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * A route destination as {@link RouteDestinationsManager#getDestination(String)} returns it, with its customer and
 * facilities.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteDestinationResource extends AbstractRouteDestinationResource {
    /** Customer identifier (32-character hex UUID) associated with this stop ({@code customer_id}). */
    @SerializedName("customer_id")
    private String customerId;

    /** The facilities linked to the destination ({@code facilities}). */
    @SerializedName("facilities")
    private List<FacilityDataResource> facilities;
}
