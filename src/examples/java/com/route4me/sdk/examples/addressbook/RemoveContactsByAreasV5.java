package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookAreasFilter;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.JobStatusResponse;

/**
 * Deletes the contacts that match a search text, in a background job, with
 * {@link AddressBookManagerV5#removeAddressBookContactsByAreas}, then reads the job status with
 * {@link AddressBookManagerV5#getJobStatus}.
 */
public class RemoveContactsByAreasV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookAreasFilter filter = new AddressBookAreasFilter();
            filter.setQuery("TEST CONTACT");
            AsyncJob job = manager.removeAddressBookContactsByAreas(filter);
            System.out.println("The deletion was queued as job " + job.getJobId());
            if (job.getJobId() != null) {
                JobStatusResponse status = manager.getJobStatus(job.getJobId());
                System.out.println("Job status: " + status.getStatus());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
