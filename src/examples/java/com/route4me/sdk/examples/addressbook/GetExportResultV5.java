package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Downloads the file of a finished address book export with {@link AddressBookManagerV5#getExportResult}. Set
 * {@code EXPORT_JOB_ID} to the ID of an export, the {@code getJobId()} of the job that an export method started.
 * Set {@code EXPORT_FILE} to a path to save the file there, as it came (do this for an {@code xls} or {@code xlsx}
 * export); without it, the example prints the file as UTF-8 text, which suits a CSV export.
 */
public class GetExportResultV5 {
    public static void main(String[] args) {
        String jobId = System.getenv("EXPORT_JOB_ID");
        if (jobId == null || jobId.isEmpty()) {
            System.err.println("Set EXPORT_JOB_ID to the ID of an address book export.");
            return;
        }
        String path = System.getenv("EXPORT_FILE");
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            byte[] file = manager.getExportResult(jobId);
            if (path == null || path.isEmpty()) {
                System.out.println(new String(file, StandardCharsets.UTF_8));
            } else {
                Files.write(Paths.get(path), file);
                System.out.println("Saved " + file.length + " bytes to " + path);
            }
        } catch (APIException | IOException e) {
            e.printStackTrace();
        }
    }
}
