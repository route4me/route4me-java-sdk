package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportByAreasFilter;
import com.route4me.sdk.services.addressbook.BoundingBox;

/**
 * Exports the contacts inside a rectangle to a CSV file, in a background job, with
 * {@link AddressBookManagerV5#exportAddressesByAreas}.
 */
public class ExportAddressesByAreasV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BoundingBox box = new BoundingBox();
            box.setTop(40.80);
            box.setLeft(-74.05);
            box.setBottom(40.70);
            box.setRight(-73.90);
            AddressExportByAreasFilter filter = new AddressExportByAreasFilter();
            filter.setBoundingBox(box);
            filter.setFilename("midtown-contacts");
            filter.setExtension("csv");
            AsyncJob job = manager.exportAddressesByAreas(filter);
            System.out.println("The export was queued as job " + job.getJobId() + "; its status: " + job.getLocation());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
