package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Exports some contacts to a spreadsheet, in a background job, with {@link AddressBookManagerV5#exportAddresses}, then
 * follows the job with {@link AddressBookManagerV5#getExportStatus} and downloads the file with
 * {@link AddressBookManagerV5#getExportResult}. Set {@code CONTACT_IDS} to the comma-separated IDs of some of your
 * contacts.
 */
public class ExportAddressesV5 {
    public static void main(String[] args) {
        String ids = System.getenv("CONTACT_IDS");
        if (ids == null || ids.trim().isEmpty()) {
            System.err.println("Set CONTACT_IDS to the comma-separated IDs of some of your contacts; this example"
                    + " exports them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressExportRequest request = new AddressExportRequest();
            List<Long> contactIds = new ArrayList<>();
            for (String id : ids.trim().split("\\s*,\\s*")) {
                contactIds.add(Long.valueOf(id));
            }
            request.setIds(contactIds);
            request.setFilename("contacts");
            request.setExtension("xlsx");
            request.setTimezone("America/New_York");
            AsyncJob job = manager.exportAddresses(request);
            if (job.getJobId() == null) {
                System.out.println("The API answered " + job.getStatusCode() + " without an export ID");
                return;
            }
            System.out.println("Export " + job.getJobId() + " queued; status URL " + job.getLocation());
            // Poll the status, then download the file once the export is ready (getExportResult fails until then).
            for (int attempt = 1; attempt <= 30; attempt++) {
                System.out.println("Status: " + manager.getExportStatus(job.getJobId()));
                try {
                    byte[] file = manager.getExportResult(job.getJobId());
                    System.out.println("Downloaded " + file.length + " bytes");
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
