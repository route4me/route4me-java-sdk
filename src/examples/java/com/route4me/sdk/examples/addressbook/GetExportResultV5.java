package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Downloads the file of a finished address book export with {@link AddressBookManagerV5#getExportResult} and prints
 * it. Set {@code EXPORT_JOB_ID} to the ID of an export, the {@code getJobId()} of the job that an export method
 * started.
 */
public class GetExportResultV5 {
    public static void main(String[] args) {
        String jobId = System.getenv("EXPORT_JOB_ID");
        if (jobId == null || jobId.isEmpty()) {
            System.err.println("Set EXPORT_JOB_ID to the ID of an address book export.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            System.out.println(manager.getExportResult(jobId));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
