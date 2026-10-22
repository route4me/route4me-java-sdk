package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.AutomaticTerritoriesJobResult;
import com.route4me.sdk.services.territories.TerritoriesManagerV5;

/**
 * Prints the clusters of a finished automatic territories job with
 * {@link TerritoriesManagerV5#getAutomaticTerritoriesJobResult}. Set {@code JOB_ID} to the ID of the job.
 */
public class GetAutomaticTerritoriesJobResult {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        try {
            AutomaticTerritoriesJobResult result = manager.getAutomaticTerritoriesJobResult(System.getenv("JOB_ID"));
            if (result.getClusters() != null) {
                for (AutomaticTerritoriesJobResult.Cluster cluster : result.getClusters()) {
                    System.out.println("Cluster " + cluster.getClusterId() + ": " + cluster.getAddressesCount()
                            + " addresses");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
