package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.JobStatusResponse;
import com.route4me.sdk.services.addressbook.StatusResponse;

/**
 * Deletes contacts with {@link AddressBookManagerV5#deleteContacts}. The API deletes them at once, or queues a
 * background job whose status {@link AddressBookManagerV5#getJobStatus} reads. Arguments: the contact IDs
 * (a placeholder ID without them).
 */
public class DeleteContactsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            long[] contactIds = {123456789L};
            if (args.length > 0) {
                contactIds = new long[args.length];
                for (int i = 0; i < args.length; i++) {
                    contactIds[i] = Long.parseLong(args[i]);
                }
            }
            AsyncJob job = manager.deleteContacts(contactIds);
            if (job.getJobId() != null) {
                JobStatusResponse status = manager.getJobStatus(job.getJobId());
                System.out.println("The deletion runs as job " + job.getJobId() + ": " + status.getStatus());
            } else {
                StatusResponse result = job.getBodyAs(StatusResponse.class);
                System.out.println("Deleted: " + (result != null && Boolean.TRUE.equals(result.getStatus())));
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
