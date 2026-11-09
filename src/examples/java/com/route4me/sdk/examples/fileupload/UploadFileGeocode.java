package com.route4me.sdk.examples.fileupload;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.fileupload.FileUploadManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Geocodes the addresses of an uploaded file with {@link FileUploadManager#uploadFileGeocode}. Pass the upload ID,
 * from {@link UploadFile}, and the path of the file as the arguments. The read timeout is raised to 5 minutes, since
 * the answer may come only once the rows are geocoded.
 */
public class UploadFileGeocode {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: UploadFileGeocode <upload ID from UploadFile> <path of the same file>");
            return;
        }
        Path file = Paths.get(args[1]);
        String apiKey = System.getenv("R4M_API_KEY");
        FileUploadManager manager = new FileUploadManager(apiKey);
        manager.setSocketTimeout(5 * 60 * 1000);
        byte[] content = Files.readAllBytes(file);
        try {
            System.out.println(manager.uploadFileGeocode(args[0], file.getFileName().toString(), content));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
