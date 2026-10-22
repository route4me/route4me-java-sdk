package com.route4me.sdk.examples.locations;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.locations.LocationExportRequest;
import com.route4me.sdk.services.locations.LocationFilters;
import com.route4me.sdk.services.locations.LocationManager;

import java.util.Arrays;

/**
 * Exports the address book locations that match a search text to a CSV file, in a background job, with
 * {@link LocationManager#exportLocations}, and prints the job ID and the link to the export status.
 */
public class ExportLocations {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        LocationManager manager = new LocationManager(apiKey);
        try {
            LocationFilters filters = new LocationFilters();
            filters.setSearchQuery("Bakery");
            LocationExportRequest request = new LocationExportRequest();
            request.setFilters(filters);
            request.setColumns(Arrays.asList("address_1", "address_alias"));
            request.setFilename("locations.csv");
            request.setFormat("csv");
            request.setTimezone("UTC");
            AsyncJob job = manager.exportLocations(request);
            System.out.println("The export was queued as job " + job.getJobId() + "; its status: " + job.getLocation());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
