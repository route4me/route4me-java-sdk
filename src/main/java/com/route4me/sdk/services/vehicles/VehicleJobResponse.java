package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer about an asynchronous vehicle job, from {@link VehiclesManagerV5#getVehicleJobStatus(String)} and
 * {@link VehiclesManagerV5#getVehicleJobResult(String)}. A bulk method of {@link VehiclesManagerV5} returns an
 * {@link com.route4me.sdk.AsyncJob}; when its reply has a body, {@code getBodyAs(VehicleJobResponse.class)} reads it.
 */
@Data
public class VehicleJobResponse {
    /**
     * The state of the job ({@code status}): {@code processing} while it runs, then {@code processed},
     * {@code failed} or {@code canceled}; the result endpoint answers {@code true}, read here as the text
     * {@code "true"}.
     */
    @SerializedName("status")
    private String status;
    /** The job ID, when the API puts it in the body ({@code job_id}). */
    @SerializedName("job_id")
    private String jobId;
    /**
     * What the finished job wrote ({@code result}); its shape depends on the method that started the job. JSON
     * objects come back as {@code Map}s, arrays as {@code List}s and numbers as {@code Double}s.
     */
    @SerializedName("result")
    private Object result;
}
