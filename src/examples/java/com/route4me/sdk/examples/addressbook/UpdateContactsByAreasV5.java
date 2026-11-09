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
 * {@link AddressBookManagerV5#getJobStatus}. It changes real data: set {@code CONTACTS_BOUNDING_BOX} to
 * {@code top,left,bottom,right}, in degrees, of an area of a test account, and {@code ADDRESS_GROUP} to the group to
 * give every contact inside it. Without them, the example stops.
 */
public class UpdateContactsByAreasV5 {
    public static void main(String[] args) {
        BoundingBox box = boundingBox(System.getenv("CONTACTS_BOUNDING_BOX"));
        String group = System.getenv("ADDRESS_GROUP");
        if (box == null || group == null || group.isEmpty()) {
            System.err.println("Set CONTACTS_BOUNDING_BOX to top,left,bottom,right of an area of a test account and"
                    + " ADDRESS_GROUP to a group name; this example moves every contact inside the area to that"
                    + " group.");
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

    /**
     * Reads a rectangle written as {@code top,left,bottom,right}.
     *
     * @param text the four edges in degrees, separated by commas
     * @return the rectangle, or {@code null} when the text is missing or is not four numbers
     */
    private static BoundingBox boundingBox(String text) {
        if (text == null) {
            return null;
        }
        String[] edges = text.split(",");
        if (edges.length != 4) {
            return null;
        }
        try {
            BoundingBox box = new BoundingBox();
            box.setTop(Double.valueOf(edges[0].trim()));
            box.setLeft(Double.valueOf(edges[1].trim()));
            box.setBottom(Double.valueOf(edges[2].trim()));
            box.setRight(Double.valueOf(edges[3].trim()));
            return box;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
