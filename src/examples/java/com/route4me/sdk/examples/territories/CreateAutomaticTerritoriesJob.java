package com.route4me.sdk.examples.territories;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.AutomaticTerritoriesAddress;
import com.route4me.sdk.services.territories.AutomaticTerritoriesJobRequest;
import com.route4me.sdk.services.territories.AutomaticTerritoriesJobStatus;
import com.route4me.sdk.services.territories.TerritoriesManagerV5;
import java.util.Arrays;

/**
 * Starts a job that clusters four addresses into territories with
 * {@link TerritoriesManagerV5#createAutomaticTerritoriesJob}, then reads its status with
 * {@link TerritoriesManagerV5#getAutomaticTerritoriesJobStatus}. Once the job is over,
 * {@link TerritoriesManagerV5#getAutomaticTerritoriesJobResult} reads the clusters.
 */
public class CreateAutomaticTerritoriesJob {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        AutomaticTerritoriesJobRequest request = new AutomaticTerritoriesJobRequest();
        request.setAddresses(Arrays.asList(
                new AutomaticTerritoriesAddress("1", 38.5017547607, -77.5400848389),
                new AutomaticTerritoriesAddress("2", 38.4633636475, -77.3740234375),
                new AutomaticTerritoriesAddress("3", 38.3437728882, -77.358215332),
                new AutomaticTerritoriesAddress("4", 38.3560218811, -77.4427642822)));
        request.setMode(0);
        try {
            AsyncJob job = manager.createAutomaticTerritoriesJob(request);
            System.out.println("Job accepted: " + job.getJobId());
            if (job.getJobId() != null) {
                AutomaticTerritoriesJobStatus status = manager.getAutomaticTerritoriesJobStatus(job.getJobId());
                System.out.println("Job status: " + status.getStatus());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
