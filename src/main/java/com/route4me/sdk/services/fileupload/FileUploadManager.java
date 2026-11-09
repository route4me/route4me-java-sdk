package com.route4me.sdk.services.fileupload;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
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
 *
 * <p>The file is sent from memory: the request body is built as one array, so an upload holds about twice the size
 * of the file while it runs.
 */
public class FileUploadManager extends Manager {

    private static final String UPLOAD_EP = "/actions/upload/upload.php";
    private static final String PREVIEW_EP = "/actions/upload/csv-xls-preview.php";
    private static final String GEOCODE_EP = "/actions/upload/csv-xls-geocode.php";
    /** The name of the multipart part that holds the file. */
    private static final String FILE_PART = "strFilename";
    private static final String CRLF = "\r\n";
    /** The largest request body, in bytes: the largest array Java VMs can allocate. */
    private static final int MAX_BODY_SIZE = Integer.MAX_VALUE - 8;
    /** The message of a file that does not fit in one request body. */
    private static final String TOO_LARGE = "The file is too large to upload in one request";

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
     * @throws IllegalArgumentException if the file name or the content is {@code null}, or the file is too large for
     *                                  one request body (about 2 GB)
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
     * <p>The call waits for the answer with this manager's read timeout ({@link #getSocketTimeout()}, 30 seconds by
     * default). If the server geocodes the rows before it answers, a large file can take longer: raise the timeout
     * with {@link #setSocketTimeout(int)} first. A call that times out may still be geocoding on the server, and
     * sending it again geocodes the file again.
     *
     * @param uploadId the ID of the upload, from {@link #uploadFile} ({@code strUploadID})
     * @param fileName the name of the file
     * @param content the content of the file
     * @return the answer
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the upload ID is {@code null} or empty, the file name or the content is
     *                                  {@code null}, or the file is too large for one request body (about 2 GB)
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
     * @return the body, with its content type and boundary, in one array of the exact size
     * @throws IllegalArgumentException if the file name or the content is {@code null}, or the body would not fit in
     *                                  one array
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
        byte[] headBytes = head.getBytes(StandardCharsets.UTF_8);
        byte[] tailBytes = tail.getBytes(StandardCharsets.UTF_8);
        // One copy of the file, not three: the body is written straight into an array of its size.
        byte[] body = new byte[bodySize(headBytes.length, content.length, tailBytes.length)];
        System.arraycopy(headBytes, 0, body, 0, headBytes.length);
        System.arraycopy(content, 0, body, headBytes.length, content.length);
        System.arraycopy(tailBytes, 0, body, headBytes.length + content.length, tailBytes.length);
        ByteArrayEntity entity = new ByteArrayEntity(body);
        entity.setContentType("multipart/form-data; boundary=" + boundary);
        return entity;
    }

    /**
     * Adds the sizes of the parts of a multipart body.
     *
     * @param head    the size of the part header, in bytes
     * @param content the size of the file, in bytes
     * @param tail    the size of the closing boundary, in bytes
     * @return the size of the body
     * @throws IllegalArgumentException if the sum is larger than {@code Integer.MAX_VALUE - 8}, the largest array
     *                                  Java VMs can allocate; a larger one would fail with {@link OutOfMemoryError}
     */
    static int bodySize(int head, int content, int tail) {
        int size;
        try {
            size = Math.addExact(Math.addExact(head, content), tail);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException(TOO_LARGE, ex);
        }
        if (size > MAX_BODY_SIZE) {
            throw new IllegalArgumentException(TOO_LARGE);
        }
        return size;
    }
}
