package com.route4me.sdk;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.testsupport.MockHttp;
import com.route4me.sdk.utils.Sanitizer;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsyncJob}: where the job ID comes from, and how the body is read and hidden.
 */
public class AsyncJobTest {

    private static final String STATUS_URL =
            "https://wh.route4me.com/modules/api/v5.0/vehicles/job-tracker/status/C0FFEE0000000000000000000000000A";

    /** A reply body. */
    static final class Reply {
        @SerializedName("status")
        private Boolean status;
        @SerializedName("job_id")
        private String jobId;
    }

    private static Header[] headers(String... namesAndValues) {
        Header[] headers = new Header[namesAndValues.length / 2];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new BasicHeader(namesAndValues[2 * i], namesAndValues[2 * i + 1]);
        }
        return headers;
    }

    @Test
    public void jobIdComesFromTheJobIdHeader() {
        AsyncJob job = AsyncJob.fromReply(202, headers("X-Job-Id", "JOB-1", "Location", STATUS_URL), "");

        assertEquals(202, job.getStatusCode());
        assertEquals("JOB-1", job.getJobId());
        assertEquals(STATUS_URL, job.getLocation());
    }

    @Test
    public void headerNamesAreReadInAnyCase() {
        AsyncJob lower = AsyncJob.fromReply(202, headers("x-job-id", "JOB-1", "location", STATUS_URL), null);
        AsyncJob mixed = AsyncJob.fromReply(202, headers("X-job-id", "JOB-2", "LOCATION", STATUS_URL), null);

        assertEquals("JOB-1", lower.getJobId());
        assertEquals(STATUS_URL, lower.getLocation());
        assertEquals("JOB-2", mixed.getJobId());
        assertEquals(STATUS_URL, mixed.getLocation());
    }

    @Test
    public void jobIdComesFromTheLocationWithoutAJobIdHeader() {
        AsyncJob absolute = AsyncJob.fromReply(202, headers("Location", STATUS_URL + "?page=1"), "");
        AsyncJob relative = AsyncJob.fromReply(202,
                headers("Location", "/modules/api/v5.0/address-book/job-tracker/result/JOB-3/"), "");
        AsyncJob blankHeader = AsyncJob.fromReply(202, headers("X-Job-Id", " ", "Location", STATUS_URL), "");
        AsyncJob encoded = new AsyncJob(202, null, "/api/v5.0/automatic-territories/job-tracker/status/A%2FB+C", "");

        assertEquals("C0FFEE0000000000000000000000000A", absolute.getJobId());
        assertEquals("JOB-3", relative.getJobId());
        assertEquals("C0FFEE0000000000000000000000000A", blankHeader.getJobId());
        assertEquals("A/B+C", encoded.getJobId());
    }

    @Test
    public void jobIdIsNullWhenTheReplyNamesNoJob() {
        AsyncJob none = AsyncJob.fromReply(200, null, json("{'status':true}"));
        AsyncJob otherUrl = AsyncJob.fromReply(202,
                headers("Location", "https://wh.route4me.com/modules/api/v5.0/team-files/status/F1"), "");
        AsyncJob noSegment = new AsyncJob(202, null, STATUS_URL.substring(0, STATUS_URL.lastIndexOf('/') + 1), "");
        AsyncJob trackerInQuery = new AsyncJob(202, null, "https://example.com/files?next=/job-tracker/status/J1", "");

        assertNull(none.getJobId());
        assertNull(none.getLocation());
        assertNull(otherUrl.getJobId());
        assertEquals("https://wh.route4me.com/modules/api/v5.0/team-files/status/F1", otherUrl.getLocation());
        assertNull(noSegment.getJobId());
        assertNull(trackerInQuery.getJobId());
    }

    @Test
    public void locationHidesCredentialParameters() {
        AsyncJob job = new AsyncJob(202, null, STATUS_URL + "?api_key=" + MockHttp.API_KEY, "");

        assertEquals(STATUS_URL + "?api_key=" + Sanitizer.REDACTED, job.getLocation());
        assertEquals("C0FFEE0000000000000000000000000A", job.getJobId());
    }

    @Test
    public void bodyIsEmptyTextWhenTheReplyHasNone() {
        AsyncJob job = new AsyncJob(202, "JOB-1", null, null);

        assertEquals("", job.getBody());
        assertNull(job.getBodyAs(Reply.class));
        assertNull(new AsyncJob(202, "JOB-1", null, " \n").getBodyAs(Reply.class));
    }

    @Test
    public void bodyIsParsedAsJsonWithLenientBooleans() {
        AsyncJob job = new AsyncJob(202, null, null, json("{'status':1,'job_id':'JOB-4'}"));

        Reply reply = job.getBodyAs(Reply.class);

        assertTrue(reply.status);
        assertEquals("JOB-4", reply.jobId);
        assertNull("the body is not a source of the job ID", job.getJobId());
    }

    @Test
    public void badJsonThrowsJsonParseException() {
        AsyncJob notJson = new AsyncJob(202, null, null, "<html>Accepted</html>");
        AsyncJob wrongShape = new AsyncJob(202, null, null, "[1,2]");

        for (AsyncJob job : new AsyncJob[] {notJson, wrongShape}) {
            try {
                job.getBodyAs(Reply.class);
                fail("expected a JsonParseException for " + job.getBody());
            } catch (JsonParseException expected) {
                // the documented failure
            }
        }
    }

    @Test
    public void toStringShowsTheJobButNotTheBody() {
        AsyncJob job = AsyncJob.fromReply(202, headers("X-Job-Id", "JOB-1", "Location", STATUS_URL),
                json("{'customer_email':'alex@example.com'}"));

        String text = job.toString();

        assertEquals("AsyncJob{statusCode=202, jobId=JOB-1, location=" + STATUS_URL + "}", text);
        assertFalse(text, text.contains("alex@example.com"));
    }
}
