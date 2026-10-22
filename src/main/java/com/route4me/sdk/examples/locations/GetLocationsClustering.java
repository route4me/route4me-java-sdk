package com.route4me.sdk.examples.locations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.locations.ClusteringParameters;
import com.route4me.sdk.services.locations.LocationCluster;
import com.route4me.sdk.services.locations.LocationClusteringRequest;
import com.route4me.sdk.services.locations.LocationClusteringResource;
import com.route4me.sdk.services.locations.LocationManager;

public class GetLocationsClustering {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        LocationManager manager = new LocationManager(apiKey);
        try {
            LocationClusteringRequest request = new LocationClusteringRequest();
            request.setWithClusters(true);
            ClusteringParameters clustering = new ClusteringParameters();
            clustering.setZoom(10);
            request.setClustering(clustering);
            LocationClusteringResource result = manager.getLocationsClustering(request);
            if (result.getClusters() != null) {
                for (LocationCluster cluster : result.getClusters()) {
                    System.out.println("Cluster at: " + cluster.getCluster().getLat() + ", " + cluster.getCluster().getLng()
                        + " (" + cluster.getCluster().getLocationsCount() + " locations)");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
