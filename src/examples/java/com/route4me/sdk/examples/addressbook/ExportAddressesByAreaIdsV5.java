package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import com.route4me.sdk.services.addressbook.AddressExportByAreaIdsRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Exports the contacts inside a territory to a CSV file, in a background job, with
 * {@link AddressBookManagerV5#exportAddressesByAreaIds}, then follows the job with
 * {@link AddressBookManagerV5#getExportStatus} and downloads the file with
 * {@link AddressBookManagerV5#getExportResult}.
 * Set {@code TERRITORY_ID} to a territory of the account.
 */
public class ExportAddressesByAreaIdsV5 {
    private static final List<String> DONE = Arrays.asList("done", "completed", "finished", "ready", "success");
    private static final List<String> FAILED = Arrays.asList("failed", "error");

    public static void main(String[] args) {
        String territoryId = System.getenv("TERRITORY_ID");
        if (territoryId == null || territoryId.isEmpty()) {
            System.err.println("Set TERRITORY_ID to a territory of the account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressExportByAreaIdsRequest request = new AddressExportByAreaIdsRequest();
            request.setTerritoryIds(Collections.singletonList(territoryId));
            request.setFilename("territory-contacts");
            request.setExtension("csv");
            AsyncJob job = manager.exportAddressesByAreaIds(request);
            if (job.getJobId() == null) {
                System.out.println("The API answered " + job.getStatusCode() + " without an export ID");
                return;
            }
            System.out.println("Export " + job.getJobId() + " queued; status URL " + job.getLocation());
            if (waitForExport(manager, job.getJobId())) {
                byte[] file = manager.getExportResult(job.getJobId());
                System.out.println("Downloaded " + file.length + " bytes");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the status of an export every 2 seconds, for up to a minute, until it reports done. The API does not
     * document its status values: this example takes {@code done}, {@code completed}, {@code finished}, {@code ready}
     * and {@code success} as done, and {@code failed} and {@code error} as failed, and prints every status it reads. An
     * error from {@link AddressBookManagerV5#getExportStatus} is not retried: it ends the example.
     *
     * @param manager the manager that reads the status
     * @param jobId the export ID
     * @return {@code true} when the export reported done, so its file can be downloaded
     * @throws APIException if the status cannot be read
     * @throws InterruptedException if the wait is interrupted
     */
    private static boolean waitForExport(AddressBookManagerV5 manager, String jobId)
            throws APIException, InterruptedException {
        for (int attempt = 1; attempt <= 30; attempt++) {
            String status = manager.getExportStatus(jobId);
            System.out.println("Status: " + status);
            String state = status.trim().toLowerCase(Locale.ROOT);
            if (DONE.contains(state)) {
                return true;
            }
            if (FAILED.contains(state)) {
                System.out.println("The export failed");
                return false;
            }
            Thread.sleep(2000);
        }
        System.out.println("The export did not report done after 30 status checks; download it later with"
                + " GetExportResultV5 and EXPORT_JOB_ID=" + jobId);
        return false;
    }
}
