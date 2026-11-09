package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityCoordinates;
import com.route4me.sdk.services.facilities.FacilityCreateRequest;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;
import com.route4me.sdk.services.facilities.FacilityTypeAssignment;
import java.util.Collections;

/**
 * Creates a facility with {@link FacilityManager#createFacility}; the alias, address, coordinates and types are
 * required. Set {@code FACILITY_TYPE_ID} to one of the type IDs that {@link GetFacilityTypes} lists.
 */
public class CreateFacility {
    public static void main(String[] args) {
        String typeId = System.getenv("FACILITY_TYPE_ID");
        if (typeId == null || typeId.isEmpty()) {
            System.err.println("Set FACILITY_TYPE_ID to a facility type ID that GetFacilityTypes lists; this example"
                    + " creates a facility of that type.");
            return;
        }
        Integer facilityTypeId = ExampleInput.toInteger("FACILITY_TYPE_ID", typeId);
        if (facilityTypeId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityCreateRequest request = new FacilityCreateRequest();
            request.setFacilityAlias("Test Facility");
            request.setAddress("123 Main St, New York, NY");
            FacilityCoordinates coords = new FacilityCoordinates();
            coords.setLat(40.7128);
            coords.setLng(-74.0060);
            request.setCoordinates(coords);
            FacilityTypeAssignment type = new FacilityTypeAssignment();
            type.setFacilityTypeId(facilityTypeId);
            type.setIsDefault(true);
            request.setFacilityTypes(Collections.singletonList(type));
            request.setStatus(1);
            FacilityResource result = manager.createFacility(request);
            System.out.println("Created facility: " + result.getFacilityAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
