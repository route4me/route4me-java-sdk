package com.route4me.sdk.examples.fileupload;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.fileupload.FileUploadManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Uploads a CSV file of addresses with {@link FileUploadManager#uploadFile} and prints the answer, which identifies
 * the upload. Pass the path of the file as the first argument.
 */
public class UploadFile {
    public static void main(String[] args) throws IOException {
        String apiKey = System.getenv("R4M_API_KEY");
        FileUploadManager manager = new FileUploadManager(apiKey);
        byte[] content = Files.readAllBytes(Paths.get(args[0]));
        try {
            System.out.println(manager.uploadFile(Paths.get(args[0]).getFileName().toString(), content, "json"));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
