package com.route4me.sdk.examples.fileupload;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.fileupload.FileUploadManager;

/**
 * Prints the preview of an uploaded file with {@link FileUploadManager#getFilePreview}. Pass the upload ID, from
 * {@link UploadFile}, as the first argument.
 */
public class GetFilePreview {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        FileUploadManager manager = new FileUploadManager(apiKey);
        try {
            System.out.println(manager.getFilePreview(args[0], "json"));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
