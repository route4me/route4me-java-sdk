package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link AddressBookManagerV5#getJobStatus(String)}. */
@Data
public class JobStatusResponse {
    /**
     * The status of the job: {@code started}, {@code processing} or {@code exception_occurred} while it runs, then
     * {@code processed} or {@code failed}. When the job is over the API redirects to its result, which this SDK
     * follows unless redirects are disabled; the {@code status} of the result, {@code true} or {@code false}, is then
     * returned here as text ({@code status}).
     */
    @SerializedName("status")
    private String status;
}
