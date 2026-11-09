package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/**
 * Deletes a facility with {@link FacilityManager#deleteFacility}. Set {@code FACILITY_ID} to a facility of a test
 * account: the example deletes it.
 */
public class DeleteFacility {
    public static void main(String[] args) {
        String facilityId = System.getenv("FACILITY_ID");
        if (facilityId == null || facilityId.isEmpty()) {
            System.err.println("Set FACILITY_ID to the ID of a facility of a test account; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityResource[] result = manager.deleteFacility(facilityId);
            System.out.println("Deleted facility. Result count: " + (result != null ? result.length : 0));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
