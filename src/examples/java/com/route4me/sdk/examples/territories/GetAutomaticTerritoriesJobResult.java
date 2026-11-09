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
        String jobId = System.getenv("JOB_ID");
        if (jobId == null || jobId.isEmpty()) {
            System.err.println("Set JOB_ID to the ID of an automatic territories job (see"
                    + " CreateAutomaticTerritoriesJob); this example prints its clusters.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        try {
            AutomaticTerritoriesJobResult result = manager.getAutomaticTerritoriesJobResult(jobId);
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
