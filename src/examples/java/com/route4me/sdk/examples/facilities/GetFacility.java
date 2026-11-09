package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/** Prints one facility with {@link FacilityManager#getFacility}. Set {@code FACILITY_ID} to one of your facilities. */
public class GetFacility {
    public static void main(String[] args) {
        String facilityId = System.getenv("FACILITY_ID");
        if (facilityId == null || facilityId.isEmpty()) {
            System.err.println("Set FACILITY_ID to the ID of one of your facilities; this example prints it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityResource facility = manager.getFacility(facilityId);
            System.out.println(facility.getFacilityAlias() + ": " + facility.getAddress());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
