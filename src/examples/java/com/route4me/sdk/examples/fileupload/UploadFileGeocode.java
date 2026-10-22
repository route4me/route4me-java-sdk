package com.route4me.sdk.examples.fileupload;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.fileupload.FileUploadManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Geocodes the addresses of an uploaded file with {@link FileUploadManager#uploadFileGeocode}. Pass the upload ID,
 * from {@link UploadFile}, and the path of the file as the arguments.
 */
public class UploadFileGeocode {
    public static void main(String[] args) throws IOException {
        String apiKey = System.getenv("R4M_API_KEY");
        FileUploadManager manager = new FileUploadManager(apiKey);
        byte[] content = Files.readAllBytes(Paths.get(args[1]));
        try {
            System.out.println(manager.uploadFileGeocode(args[0], Paths.get(args[1]).getFileName().toString(),
                    content));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
