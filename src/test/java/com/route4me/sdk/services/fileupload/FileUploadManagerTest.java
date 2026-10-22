package com.route4me.sdk.services.fileupload;

import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link FileUploadManager}: file upload, preview and geocoding on {@code /actions/upload/}.
 */
public class FileUploadManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String UPLOAD_ID = "C0FFEE0000000000000000000000000A";
    private static final byte[] CSV = "address,lat,lng\r\n\"100 Example St\",40.1,-74.1\r\n"
            .getBytes(StandardCharsets.UTF_8);

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private FileUploadManager manager() {
        return http.install(new FileUploadManager(MockHttp.API_KEY));
    }

    /** Asserts that a request carries the CSV file as the only multipart part, and returns its boundary. */
    private static String assertFilePart(CapturedRequest request, String fileName) {
        request.assertContentType("multipart/form-data");
        String contentType = request.effectiveContentType();
        String boundary = contentType.substring(contentType.indexOf("boundary=") + "boundary=".length());
        String expected = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"strFilename\"; filename=\"" + fileName + "\"\r\n"
                + "Content-Type: application/octet-stream\r\n\r\n"
                + new String(CSV, StandardCharsets.UTF_8) + "\r\n--" + boundary + "--\r\n";
        assertEquals(expected, request.getBody());
        return boundary;
    }

    @Test
    public void uploadFileSendsTheFileAsAMultipartPart() throws Exception {
        FileUploadManager manager = manager();
        http.respond(200, json("{'status':true,'strUploadID':'" + UPLOAD_ID + "'}")).respond(200, "{}");

        String answer = manager.uploadFile("addresses.csv", CSV, "json");
        manager.uploadFile("addresses.csv", CSV, null);

        http.request(0).assertEndpoint("POST", HOST, "/actions/upload/upload.php").assertApiKeyInQuery()
                .assertQuery("format", "json");
        String first = assertFilePart(http.request(0), "addresses.csv");
        String second = assertFilePart(http.request(1).assertNoQuery("format"), "addresses.csv");
        assertTrue("each body gets its own boundary", !first.equals(second));
        assertTrue(answer.contains(UPLOAD_ID));
    }

    @Test
    public void fileNamesCannotBreakThePartHeader() throws Exception {
        FileUploadManager manager = manager();
        http.respond(200, "{}");

        manager.uploadFile("a\"b\r\nc\\d.csv", CSV, null);

        assertFilePart(http.request(), "a_b__c_d.csv");
    }

    @Test
    public void getFilePreviewSendsTheUploadId() throws Exception {
        FileUploadManager manager = manager();
        http.respondToAll(200, json("[['address','lat','lng'],['100 Example St','40.1','-74.1']]"));

        String preview = manager.getFilePreview(UPLOAD_ID, "json");
        manager.getFilePreview(UPLOAD_ID, null);

        http.request(0).assertEndpoint("GET", HOST, "/actions/upload/csv-xls-preview.php").assertApiKeyInQuery()
                .assertQuery("strUploadID", UPLOAD_ID)
                .assertQuery("format", "json")
                .assertNoBody();
        http.request(1).assertNoQuery("format");
        assertTrue(preview.contains("100 Example St"));
    }

    @Test
    public void uploadFileGeocodeSendsTheUploadIdAndTheFile() throws Exception {
        FileUploadManager manager = manager();
        http.respond(200, json("{'status':true}"));

        String answer = manager.uploadFileGeocode(UPLOAD_ID, "addresses.csv", CSV);

        http.request().assertEndpoint("POST", HOST, "/actions/upload/csv-xls-geocode.php").assertApiKeyInQuery()
                .assertQuery("strUploadID", UPLOAD_ID);
        assertFilePart(http.request(), "addresses.csv");
        assertEquals(json("{'status':true}"), answer);
    }

    @Test
    public void missingArgumentsAreRejectedBeforeSending() {
        final FileUploadManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.uploadFile(null, CSV, "json"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.uploadFile("a.csv", null, "json"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getFilePreview("", "json"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getFilePreview(null, "json"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.uploadFileGeocode(null, "a.csv", CSV));
        http.expectThrows(IllegalArgumentException.class, () -> manager.uploadFileGeocode(UPLOAD_ID, "a.csv", null));
        http.assertNoRequests();
    }

    @Test
    public void errorBecomesApiException() {
        final FileUploadManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getFilePreview(UPLOAD_ID, "json"));
    }
}
