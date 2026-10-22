package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportByAreaIdsRequest;

import java.util.Collections;

/**
 * Exports the contacts inside a territory to a CSV file, in a background job, with
 * {@link AddressBookManagerV5#exportAddressesByAreaIds}.
 */
public class ExportAddressesByAreaIdsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressExportByAreaIdsRequest request = new AddressExportByAreaIdsRequest();
            request.setTerritoryIds(Collections.singletonList("TERRITORY_ID_HERE"));
            request.setFilename("territory-contacts");
            request.setExtension("csv");
            manager.exportAddressesByAreaIds(request);
            System.out.println("The export was queued");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
