package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.AutomaticTerritoriesJobStatus;
import com.route4me.sdk.services.territories.TerritoriesManagerV5;

/**
 * Prints the status of an automatic territories job with {@link TerritoriesManagerV5#getAutomaticTerritoriesJobStatus}.
 * Set {@code JOB_ID} to the ID of the job.
 */
public class GetAutomaticTerritoriesJobStatus {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        try {
            AutomaticTerritoriesJobStatus status = manager.getAutomaticTerritoriesJobStatus(System.getenv("JOB_ID"));
            System.out.println("Status: " + status.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
