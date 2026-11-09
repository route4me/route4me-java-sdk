package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityCoordinates;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;
import com.route4me.sdk.services.facilities.FacilityTypeAssignment;
import com.route4me.sdk.services.facilities.FacilityUpdateRequest;
import java.util.Collections;

/**
 * Updates a facility with {@link FacilityManager#updateFacility}; the alias, address, coordinates and types are
 * required. Set {@code FACILITY_ID} to a facility of a test account and {@code FACILITY_TYPE_ID} to one of the type IDs
 * that {@link GetFacilityTypes} lists: the example changes the facility.
 */
public class UpdateFacility {
    public static void main(String[] args) {
        String facilityId = System.getenv("FACILITY_ID");
        String typeId = System.getenv("FACILITY_TYPE_ID");
        if (facilityId == null || facilityId.isEmpty() || typeId == null || typeId.isEmpty()) {
            System.err.println("Set FACILITY_ID to the ID of a facility of a test account and FACILITY_TYPE_ID to a"
                    + " facility type ID that GetFacilityTypes lists; this example changes the facility.");
            return;
        }
        Integer facilityTypeId = ExampleInput.toInteger("FACILITY_TYPE_ID", typeId);
        if (facilityTypeId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        FacilityCoordinates coordinates = new FacilityCoordinates();
        coordinates.setLat(40.7128);
        coordinates.setLng(-74.0060);
        FacilityTypeAssignment type = new FacilityTypeAssignment();
        type.setFacilityTypeId(facilityTypeId);
        type.setIsDefault(true);
        FacilityUpdateRequest request = new FacilityUpdateRequest();
        request.setFacilityAlias("Main warehouse");
        request.setAddress("123 Main St, New York, NY");
        request.setCoordinates(coordinates);
        request.setFacilityTypes(Collections.singletonList(type));
        try {
            FacilityResource updated = manager.updateFacility(facilityId, request);
            System.out.println("Updated facility: " + updated.getFacilityAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
