package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityTypeResource;

/** Prints facility type 1 with {@link FacilityManager#getFacilityType}. */
public class GetFacilityType {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityTypeResource type = manager.getFacilityType(1);
            System.out.println(type.getFacilityTypeId() + ": " + type.getFacilityTypeAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
