package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RouteDestinationResource extends AbstractRouteDestinationResource {
    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("facilities")
    private List<FacilityDataResource> facilities;
}
