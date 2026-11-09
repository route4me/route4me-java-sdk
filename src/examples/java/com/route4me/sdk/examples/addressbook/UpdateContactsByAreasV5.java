package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.examples.utils.ExampleInput;
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
 * {@link AddressBookManagerV5#getJobStatus}. It changes real data: set {@code CONTACTS_BOUNDING_BOX} to
 * {@code top,left,bottom,right}, in degrees, of an area of a test account, and {@code ADDRESS_GROUP} to the group to
 * give every contact inside it. Without them, or with a malformed rectangle, the example stops. A rectangle whose left
 * edge is east of its right edge, such as {@code 20,170,10,-170}, crosses the 180th meridian; the API does not document
 * how it treats one, so try it on a test account first.
 */
public class UpdateContactsByAreasV5 {
    public static void main(String[] args) {
        String text = System.getenv("CONTACTS_BOUNDING_BOX");
        String group = System.getenv("ADDRESS_GROUP");
        if (text == null || text.trim().isEmpty() || group == null || group.isEmpty()) {
            System.err.println("Set CONTACTS_BOUNDING_BOX to top,left,bottom,right in degrees of an area of a test"
                    + " account, such as 40.80,-74.05,40.70,-73.90, and ADDRESS_GROUP to a group name; this example"
                    + " moves every contact inside the area to that group.");
            return;
        }
        BoundingBox box = ExampleInput.toBoundingBox("CONTACTS_BOUNDING_BOX", text);
        if (box == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookAreasFilter filter = new AddressBookAreasFilter();
            filter.setBoundingBox(box);
            AddressBookContactV5 data = new AddressBookContactV5();
            data.setAddressGroup(group);
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
