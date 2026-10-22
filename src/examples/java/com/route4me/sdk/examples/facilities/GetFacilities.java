package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilitiesPaginateResource;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/** Lists facilities with {@link FacilityManager#getFacilities}. */
public class GetFacilities {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilitiesPaginateResource result = manager.getFacilities(1, 10, null, null, null);
            if (result.getData() != null) {
                for (FacilityResource facility : result.getData()) {
                    System.out.println("Facility: " + facility.getFacilityAlias() + " (ID: " + facility.getFacilityId()
                            + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
