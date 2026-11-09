package com.route4me.sdk.services.routedestinations;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** One destination of {@link RouteDestinationsManager#getDestinationsList(GetDestinationsRequest)}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteDestinationListResource extends AbstractRouteDestinationResource {
}
