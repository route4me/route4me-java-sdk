package com.route4me.sdk.services.fileupload;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;

/**
 * Uploads CSV and XLS(X) files of addresses, previews them and geocodes them, through the V4 API
 * ({@code https://api.route4me.com/actions/upload/...}). The file goes as the {@code strFilename} part of a
 * {@code multipart/form-data} body, as the Python, C and cURL examples send it.
 *
 * <p>No OpenAPI spec and no C# method cover these endpoints; the requests follow the Python SDK's
 * {@code FileUploading}. Every method returns the answer as text.
 */
public class FileUploadManager extends Manager {

    private static final String UPLOAD_EP = "/actions/upload/upload.php";
    private static final String PREVIEW_EP = "/actions/upload/csv-xls-preview.php";
    private static final String GEOCODE_EP = "/actions/upload/csv-xls-geocode.php";
    /** The name of the multipart part that holds the file. */
    private static final String FILE_PART = "strFilename";
    private static final String CRLF = "\r\n";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public FileUploadManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Uploads a file of addresses: {@code POST https://api.route4me.com/actions/upload/upload.php?format=...}, with
     * the file as the {@code strFilename} part of a multipart body.
     *
     * @param fileName the name of the file, such as {@code addresses.csv}
     * @param content the content of the file
     * @param format the format of the answer, such as {@code json} ({@code format}); {@code null} leaves it out
     * @return the answer, which identifies the upload for {@link #getFilePreview} and {@link #uploadFileGeocode}
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the file name or the content is {@code null}
     */
    public String uploadFile(String fileName, byte[] content, String format) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(UPLOAD_EP);
        if (format != null) {
            builder.setParameter("format", format);
        }
        return this.makeRequest(RequestMethod.POST, builder, multipart(fileName, content), String.class);
    }

    /**
     * Previews the content of an uploaded file: {@code GET
     * https://api.route4me.com/actions/upload/csv-xls-preview.php?strUploadID=...&format=...}.
     *
     * @param uploadId the ID of the upload, from {@link #uploadFile} ({@code strUploadID})
     * @param format the format of the answer, such as {@code json} ({@code format}); {@code null} leaves it out
     * @return the preview
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the upload ID is {@code null} or empty
     */
    public String getFilePreview(String uploadId, String format) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(PREVIEW_EP);
        builder.setParameter("strUploadID", requireUploadId(uploadId));
        if (format != null) {
            builder.setParameter("format", format);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String.class);
    }

    /**
     * Geocodes the addresses of an uploaded file: {@code POST
     * https://api.route4me.com/actions/upload/csv-xls-geocode.php?strUploadID=...}, with the file as the
     * {@code strFilename} part of a multipart body.
     *
     * @param uploadId the ID of the upload, from {@link #uploadFile} ({@code strUploadID})
     * @param fileName the name of the file
     * @param content the content of the file
     * @return the answer
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the upload ID is {@code null} or empty, or the file name or the content is
     *                                  {@code null}
     */
    public String uploadFileGeocode(String uploadId, String fileName, byte[] content) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GEOCODE_EP);
        builder.setParameter("strUploadID", requireUploadId(uploadId));
        return this.makeRequest(RequestMethod.POST, builder, multipart(fileName, content), String.class);
    }

    /** Returns the upload ID, or throws when it is {@code null} or empty. */
    private static String requireUploadId(String uploadId) {
        if (uploadId == null || uploadId.isEmpty()) {
            throw new IllegalArgumentException("The upload ID is required");
        }
        return uploadId;
    }

    /**
     * Builds a {@code multipart/form-data} body with one part, {@code strFilename}, that holds the file.
     *
     * @param fileName the file name; quotes, backslashes and line breaks become {@code _}, so it cannot end the
     *                 header
     * @param content the content of the file
     * @return the body, with its content type and boundary
     */
    private static HttpEntity multipart(String fileName, byte[] content) {
        if (fileName == null || content == null) {
            throw new IllegalArgumentException("The file name and the content are required");
        }
        String boundary = "Route4MeSDK" + UUID.randomUUID().toString().replace("-", "");
        String safeName = fileName.replaceAll("[\"\\\\\r\n]", "_");
        String head = "--" + boundary + CRLF
                + "Content-Disposition: form-data; name=\"" + FILE_PART + "\"; filename=\"" + safeName + "\"" + CRLF
                + "Content-Type: application/octet-stream" + CRLF + CRLF;
        String tail = CRLF + "--" + boundary + "--" + CRLF;
        ByteArrayOutputStream body = new ByteArrayOutputStream(head.length() + content.length + tail.length());
        byte[] headBytes = head.getBytes(StandardCharsets.UTF_8);
        byte[] tailBytes = tail.getBytes(StandardCharsets.UTF_8);
        body.write(headBytes, 0, headBytes.length);
        body.write(content, 0, content.length);
        body.write(tailBytes, 0, tailBytes.length);
        ByteArrayEntity entity = new ByteArrayEntity(body.toByteArray());
        entity.setContentType("multipart/form-data; boundary=" + boundary);
        return entity;
    }
}
