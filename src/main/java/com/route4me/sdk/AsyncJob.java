package com.route4me.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.route4me.sdk.utils.LenientBooleanAdapter;
import com.route4me.sdk.utils.LenientMapAdapterFactory;
import com.route4me.sdk.utils.Sanitizer;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.http.Header;

/**
 * The reply to a request that starts a background job on the API. The API accepts such a request, usually with
 * {@code 202 Accepted}, and runs the job later; the job ID comes in the {@code X-Job-Id} response header and the URL
 * of the job status in the {@code Location} header.
 *
 * <p>Pass {@link #getJobId()} to the status or result method of the manager that started the job:
 * <pre>{@code
 * AsyncJob job = vehicles.activateVehicles(vehicleIds);
 * if (job.getJobId() != null) {
 *     VehicleJobResponse status = vehicles.getVehicleJobStatus(job.getJobId());
 * }
 * }</pre>
 *
 * <p>Instances are immutable and safe to share between threads. {@link #toString()} leaves the body out, since it
 * may hold account data.
 */
public final class AsyncJob {

    /** The response header with the job ID; header names are compared without regard to case. */
    private static final String JOB_ID_HEADER = "X-Job-Id";
    /** The response header with the URL of the job status. */
    private static final String LOCATION_HEADER = "Location";
    /** The path parts after which the job ID ends a job tracker URL. */
    private static final String[] JOB_TRACKER_PATHS = {"/job-tracker/status/", "/job-tracker/result/"};
    /** The JSON mapper of {@link #getBodyAs(Class)}; it reads booleans sent as {@code 1}/{@code 0}. */
    private static final Gson GSON =
            LenientMapAdapterFactory.register(LenientBooleanAdapter.register(new GsonBuilder())).create();

    private final int statusCode;
    private final String jobId;
    private final String location;
    private final String body;

    /**
     * Creates the job from the parts of a reply.
     *
     * @param statusCode the HTTP status code of the reply
     * @param jobIdHeader the value of the {@code X-Job-Id} header, or {@code null} when the reply has none; the job
     *     ID is then read from {@code location}
     * @param location the value of the {@code Location} header, or {@code null} when the reply has none; credential
     *     query parameters in it are hidden (see {@link Sanitizer#redactUrl(String)})
     * @param body the body of the reply; {@code null} is read as an empty body
     */
    public AsyncJob(int statusCode, String jobIdHeader, String location, String body) {
        this.statusCode = statusCode;
        this.location = Sanitizer.redactUrl(location);
        this.jobId = jobIdOf(jobIdHeader, this.location);
        this.body = body == null ? "" : body;
    }

    /**
     * Creates the job from a reply: its status code, its {@code X-Job-Id} and {@code Location} headers (the first
     * of each, in any case) and its body.
     *
     * @param statusCode the HTTP status code
     * @param headers the response headers; {@code null} for none
     * @param body the body, or {@code null} for none
     * @return the job
     */
    static AsyncJob fromReply(int statusCode, Header[] headers, String body) {
        return new AsyncJob(statusCode, firstHeader(headers, JOB_ID_HEADER), firstHeader(headers, LOCATION_HEADER),
                body);
    }

    /**
     * Returns the HTTP status code of the reply: {@code 202} when the API queued the job; some endpoints answer
     * {@code 200} when they did the work at once.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the job ID: the {@code X-Job-Id} header (in any case, such as {@code x-job-id}) or, when the reply has
     * none, the last path segment of the {@code Location} URL when that URL is a job tracker URL (its path contains
     * {@code /job-tracker/status/} or {@code /job-tracker/result/}), percent-decoded.
     *
     * @return the job ID, or {@code null} when the reply names no job, for example when the API did the work at once
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Returns the {@code Location} header of the reply: the URL, or the path, of the job status (for an export, of
     * the file status). Credential query parameters in it are hidden.
     *
     * @return the URL, or {@code null} when the reply has no {@code Location} header
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the body of the reply as text.
     *
     * @return the body; {@code ""} when the reply has none, which is usual for {@code 202 Accepted}
     */
    public String getBody() {
        return body;
    }

    /**
     * Parses the body of the reply as JSON. Booleans sent as {@code 1}/{@code 0} are read as well (see
     * {@link LenientBooleanAdapter}), and an empty JSON array as an empty map (see {@link LenientMapAdapterFactory}).
     *
     * @param type the class to read the body into, such as a response class of the manager that started the job
     * @param <T> the type of the result
     * @return the parsed body, or {@code null} when the body is empty or holds only blanks
     * @throws JsonParseException if the body is not valid JSON for {@code type}
     */
    public <T> T getBodyAs(Class<T> type) {
        if (body.trim().isEmpty()) {
            return null;
        }
        try {
            return GSON.fromJson(body, type);
        } catch (IllegalStateException ex) {
            throw new JsonParseException("The body is not valid JSON for " + type.getSimpleName(), ex);
        }
    }

    /**
     * Describes the job by its status code, job ID and location. The body is left out, since it may hold account
     * data.
     *
     * @return the description
     */
    @Override
    public String toString() {
        return "AsyncJob{statusCode=" + statusCode + ", jobId=" + jobId + ", location=" + location + "}";
    }

    /** Returns the value of the first header named {@code name}, in any case, or {@code null}. */
    private static String firstHeader(Header[] headers, String name) {
        if (headers == null) {
            return null;
        }
        for (Header header : headers) {
            if (header != null && name.equalsIgnoreCase(header.getName())) {
                return header.getValue();
            }
        }
        return null;
    }

    /**
     * Returns the job ID: the header when it is not blank, else the segment after a job tracker path in the
     * location, else {@code null}.
     */
    private static String jobIdOf(String header, String location) {
        if (header != null && !header.trim().isEmpty()) {
            return header.trim();
        }
        if (location == null) {
            return null;
        }
        String path = location;
        for (char end : new char[] {'?', '#'}) {
            int at = path.indexOf(end);
            if (at >= 0) {
                path = path.substring(0, at);
            }
        }
        for (String marker : JOB_TRACKER_PATHS) {
            int at = path.indexOf(marker);
            if (at < 0) {
                continue;
            }
            String rest = path.substring(at + marker.length());
            while (rest.endsWith("/")) {
                rest = rest.substring(0, rest.length() - 1);
            }
            String segment = rest.substring(rest.lastIndexOf('/') + 1);
            return segment.isEmpty() ? null : decode(segment);
        }
        return null;
    }

    /** Percent-decodes one path segment; a {@code +} stays a {@code +}, and a malformed escape keeps the segment. */
    private static String decode(String segment) {
        try {
            return URLDecoder.decode(segment.replace("+", "%2B"), "UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException ex) {
            return segment;
        }
    }
}
