package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookClustering;
import com.route4me.sdk.services.addressbook.AddressBookClusteringRequest;
import com.route4me.sdk.services.addressbook.AddressBookClusteringResponse;
import com.route4me.sdk.services.addressbook.AddressBookContactsFilter;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.BoundingBox;

/**
 * Groups the contacts inside a rectangle into small map clusters with
 * {@link AddressBookManagerV5#getAddressBookContactsClustering(AddressBookClusteringRequest)}.
 */
public class GetContactsClusteringByBodyV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            BoundingBox box = new BoundingBox();
            box.setTop(40.80);
            box.setLeft(-74.05);
            box.setBottom(40.70);
            box.setRight(-73.90);
            AddressBookContactsFilter filter = new AddressBookContactsFilter();
            filter.setBoundingBox(box);
            AddressBookClustering clustering = new AddressBookClustering();
            clustering.setPrecision(7);
            AddressBookClusteringRequest request = new AddressBookClusteringRequest();
            request.setClustering(clustering);
            request.setFilter(filter);
            AddressBookClusteringResponse response = manager.getAddressBookContactsClustering(request);
            System.out.println("Contacts: " + response.getTotal());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
