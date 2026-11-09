package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityTypeResource;

/**
 * Prints one facility type with {@link FacilityManager#getFacilityType}. Set {@code FACILITY_TYPE_ID} to one of the
 * type IDs that {@link GetFacilityTypes} lists.
 */
public class GetFacilityType {
    public static void main(String[] args) {
        String typeId = System.getenv("FACILITY_TYPE_ID");
        if (typeId == null || typeId.isEmpty()) {
            System.err.println("Set FACILITY_TYPE_ID to a facility type ID that GetFacilityTypes lists; this example"
                    + " prints that type.");
            return;
        }
        Integer facilityTypeId = ExampleInput.toInteger("FACILITY_TYPE_ID", typeId);
        if (facilityTypeId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityTypeResource type = manager.getFacilityType(facilityTypeId);
            System.out.println(type.getFacilityTypeId() + ": " + type.getFacilityTypeAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
