package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportByAreaIdsRequest;

import java.util.Collections;

/**
 * Exports the contacts inside a territory to a CSV file, in a background job, with
 * {@link AddressBookManagerV5#exportAddressesByAreaIds}, then follows the job with
 * {@link AddressBookManagerV5#getExportStatus} and downloads the file with
 * {@link AddressBookManagerV5#getExportResult}.
 * Set {@code TERRITORY_ID} to a territory of the account.
 */
public class ExportAddressesByAreaIdsV5 {
    public static void main(String[] args) {
        String territoryId = System.getenv("TERRITORY_ID");
        if (territoryId == null || territoryId.isEmpty()) {
            System.err.println("Set TERRITORY_ID to a territory of the account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressExportByAreaIdsRequest request = new AddressExportByAreaIdsRequest();
            request.setTerritoryIds(Collections.singletonList(territoryId));
            request.setFilename("territory-contacts");
            request.setExtension("csv");
            AsyncJob job = manager.exportAddressesByAreaIds(request);
            if (job.getJobId() == null) {
                System.out.println("The API answered " + job.getStatusCode() + " without an export ID");
                return;
            }
            System.out.println("Export " + job.getJobId() + " queued; status URL " + job.getLocation());
            // Poll the status, then download the file once the export is ready (getExportResult fails until then).
            for (int attempt = 1; attempt <= 30; attempt++) {
                System.out.println("Status: " + manager.getExportStatus(job.getJobId()));
                try {
                    String file = manager.getExportResult(job.getJobId());
                    System.out.println("Downloaded " + file.length() + " characters");
                    return;
                } catch (APIException notReady) {
                    Thread.sleep(2000);
                }
            }
            System.out.println("The export was not ready after 30 attempts");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
