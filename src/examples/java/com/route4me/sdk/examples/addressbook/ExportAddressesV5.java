package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportRequest;

import java.util.Arrays;

/** Exports some contacts to a spreadsheet, in a background job, with {@link AddressBookManagerV5#exportAddresses}. */
public class ExportAddressesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressExportRequest request = new AddressExportRequest();
            request.setIds(Arrays.asList(12345L, 12346L));
            request.setFilename("contacts");
            request.setExtension("xlsx");
            request.setTimezone("America/New_York");
            manager.exportAddresses(request);
            System.out.println("The export was queued");
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
