package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/** Prints one facility with {@link FacilityManager#getFacility}. Set {@code FACILITY_ID} to one of your facilities. */
public class GetFacility {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityResource facility = manager.getFacility(System.getenv("FACILITY_ID"));
            System.out.println(facility.getFacilityAlias() + ": " + facility.getAddress());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
