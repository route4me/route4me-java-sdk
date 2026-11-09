package com.route4me.sdk.examples.locations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.locations.LocationCombinedRequest;
import com.route4me.sdk.services.locations.LocationListItem;
import com.route4me.sdk.services.locations.LocationListResource;
import com.route4me.sdk.services.locations.LocationManager;

/** Lists the address book locations with {@link LocationManager#getLocationsList}. */
public class GetLocationsList {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        LocationManager manager = new LocationManager(apiKey);
        try {
            LocationCombinedRequest request = new LocationCombinedRequest();
            request.setPage(1);
            request.setPerPage(10);
            LocationListResource result = manager.getLocationsList(request);
            if (result.getItems() != null) {
                for (LocationListItem item : result.getItems()) {
                    System.out.println("Location: " + item.getAddressAlias() + " (" + item.getLat() + ", "
                            + item.getLng() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
