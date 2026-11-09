package com.route4me.sdk.examples.facilities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.facilities.FacilityManager;
import com.route4me.sdk.services.facilities.FacilityTypeCollectionResource;
import com.route4me.sdk.services.facilities.FacilityTypeResource;

/** Lists the facility types with {@link FacilityManager#getFacilityTypes}. */
public class GetFacilityTypes {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FacilityManager manager = new FacilityManager(apiKey);
        try {
            FacilityTypeCollectionResource types = manager.getFacilityTypes();
            if (types.getData() != null) {
                for (FacilityTypeResource type : types.getData()) {
                    System.out.println(type.getFacilityTypeId() + ": " + type.getFacilityTypeAlias());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
