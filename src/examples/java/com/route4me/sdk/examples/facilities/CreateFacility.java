package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityCoordinates;
import com.route4me.sdk.services.facilities.FacilityCreateRequest;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityResource;

/** Creates a facility with {@link FacilityManager#createFacility}. */
public class CreateFacility {
    public static void main(String[] args) {
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
            request.setStatus(1);
            FacilityResource result = manager.createFacility(request);
            System.out.println("Created facility: " + result.getFacilityAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
