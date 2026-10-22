package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/** Deletes a facility with {@link FacilityManager#deleteFacility}. */
public class DeleteFacility {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            String facilityId = "FACILITY_ID_HERE";
            FacilityResource[] result = manager.deleteFacility(facilityId);
            System.out.println("Deleted facility. Result count: " + (result != null ? result.length : 0));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
