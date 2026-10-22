package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookCluster;
import com.route4me.sdk.services.addressbook.AddressBookClusteringQuery;
import com.route4me.sdk.services.addressbook.AddressBookClusteringResponse;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

/**
 * Groups the unrouted contacts into map clusters with
 * {@link AddressBookManagerV5#getAddressBookContactsClustering(AddressBookClusteringQuery)}.
 */
public class GetContactsClusteringV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookClusteringQuery query = new AddressBookClusteringQuery();
            query.setDisplay("unrouted");
            AddressBookClusteringResponse response = manager.getAddressBookContactsClustering(query);
            if (response.getClusters() != null) {
                for (AddressBookCluster cluster : response.getClusters()) {
                    System.out.println("Cluster " + cluster.getGeohash() + ": " + cluster.getAddressCount());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
