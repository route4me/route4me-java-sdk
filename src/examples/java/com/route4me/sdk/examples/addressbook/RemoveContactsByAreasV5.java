package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookAreasFilter;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BoundingBox;
import com.route4me.sdk.services.addressbook.JobStatusResponse;

/**
 * Deletes the contacts inside a rectangle, in a background job, with
 * {@link AddressBookManagerV5#removeAddressBookContactsByAreas}, then reads the job status with
 * {@link AddressBookManagerV5#getJobStatus}. It deletes real data: set {@code CONTACTS_BOUNDING_BOX} to
 * {@code top,left,bottom,right}, in degrees, of an area of a test account, and optionally {@code CONTACTS_QUERY} to a
 * text that narrows the contacts to delete. Without the rectangle, the example stops.
 */
public class RemoveContactsByAreasV5 {
    public static void main(String[] args) {
        BoundingBox box = boundingBox(System.getenv("CONTACTS_BOUNDING_BOX"));
        if (box == null) {
            System.err.println("Set CONTACTS_BOUNDING_BOX to top,left,bottom,right of an area of a test account;"
                    + " this example deletes every contact inside it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookAreasFilter filter = new AddressBookAreasFilter();
            filter.setBoundingBox(box);
            String query = System.getenv("CONTACTS_QUERY");
            if (query != null && !query.isEmpty()) {
                filter.setQuery(query);
            }
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
