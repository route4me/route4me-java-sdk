package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookAreasFilter;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BoundingBox;
import com.route4me.sdk.services.addressbook.JobStatusResponse;
import com.route4me.sdk.services.addressbook.UpdateContactsByAreasRequest;

/**
 * Moves the contacts inside a rectangle to another group, in a background job, with
 * {@link AddressBookManagerV5#updateAddressBookContactByAreas}, then reads the job status with
 * {@link AddressBookManagerV5#getJobStatus}.
 */
public class UpdateContactsByAreasV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BoundingBox box = new BoundingBox();
            box.setTop(40.80);
            box.setLeft(-74.05);
            box.setBottom(40.70);
            box.setRight(-73.90);
            AddressBookAreasFilter filter = new AddressBookAreasFilter();
            filter.setBoundingBox(box);
            AddressBookContactV5 data = new AddressBookContactV5();
            data.setAddressGroup("Midtown");
            UpdateContactsByAreasRequest request = new UpdateContactsByAreasRequest();
            request.setFilter(filter);
            request.setData(data);
            AsyncJob job = manager.updateAddressBookContactByAreas(request);
            System.out.println("The update was queued as job " + job.getJobId());
            if (job.getJobId() != null) {
                JobStatusResponse status = manager.getJobStatus(job.getJobId());
                System.out.println("Job status: " + status.getStatus());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
