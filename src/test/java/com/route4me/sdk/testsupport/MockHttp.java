package com.route4me.sdk.testsupport;

import com.route4me.sdk.Manager;
import com.route4me.sdk.ManagerTestAccess;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * JUnit rule that replaces the HTTP client of the managers under test with a Mockito mock,
 * records every request they send, and answers with canned responses. Nothing goes to
 * the network.
 *
 * <p>Typical use:
 * <pre>{@code
 * @Rule
 * public final MockHttp http = new MockHttp();
 *
 * @Test
 * public void getsVehicle() throws Exception {
 *     VehiclesManagerV5 manager = http.install(new VehiclesManagerV5(MockHttp.API_KEY));
 *     http.respond(200, MockHttp.json("{'vehicle_id':'V1'}"));
 *
 *     VehicleV5 vehicle = manager.getVehicleById("V1");
 *
 *     http.request().assertEndpoint("GET", "wh.route4me.com", "/modules/api/v5.0/vehicles/V1");
 *     assertEquals("V1", vehicle.getVehicleId());
 * }
 * }</pre>
 *
 * <p>Every captured request goes through the {@link RequestGuard}: a request to an unknown
 * host or endpoint, over plain http, or with the API key outside its place (the
 * {@code Authorization: Bearer} header on the V5 API, the {@code api_key} query parameter
 * elsewhere) fails the test. The guard also fails the test when the API key appears in an
 * exception that escapes the test (for a failed assertion: in the exception it wraps), in
 * one returned by {@link #expectThrows}, or in a {@code java.util.logging} record written
 * during the test.
 */
public final class MockHttp implements TestRule {

    /** Placeholder API key for unit tests. It is not a real key. */
    public static final String API_KEY = "11111111111111111111111111111111";

    /**
     * A piece of test code that may throw, for {@link #expectThrows} and
     * {@link #expectApiException}.
     */
    @FunctionalInterface
    public interface Call {
        /**
         * Runs the code.
         *
         * @throws Exception whatever the code throws
         */
        void run() throws Exception;
    }

    private final RequestGuard guard = new RequestGuard(EndpointRegistry.load(), API_KEY);
    private final CloseableHttpClient client = Mockito.mock(CloseableHttpClient.class);
    private final Queue<Object> steps = new ConcurrentLinkedQueue<>();
    private final List<CapturedRequest> requests = new CopyOnWriteArrayList<>();
    private final List<String> violations = new CopyOnWriteArrayList<>();
    private final AtomicInteger closeCount = new AtomicInteger();
    private volatile CannedResponse defaultResponse;
    private volatile IOException closeFailure;
    private volatile long responseDelayMillis;
    private volatile String testName;

    /** Creates the rule and its mocked client. */
    public MockHttp() {
        try {
            Mockito.when(client.execute(Mockito.any(HttpUriRequest.class)))
                    .thenAnswer(invocation -> execute(invocation.getArgument(0)));
            Mockito.doAnswer(invocation -> {
                closeCount.incrementAndGet();
                IOException failure = closeFailure;
                if (failure != null) {
                    throw failure;
                }
                return null;
            }).when(client).close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot stub the mocked HTTP client", e);
        }
    }

    /**
     * Turns JSON written with single quotes into real JSON, to keep inline bodies readable:
     * {@code json("{'id':'V1'}")} returns {@code {"id":"V1"}}.
     *
     * @param singleQuoted JSON text that uses {@code '} instead of {@code "}
     * @return the JSON text
     */
    public static String json(String singleQuoted) {
        return singleQuoted.replace('\'', '"');
    }

    // ------------------------------------------------------------------ setup

    /**
     * Makes a manager send its requests to the mocked client.
     *
     * @param manager the manager under test
     * @param <M>     the manager type
     * @return the same manager
     */
    public <M extends Manager> M install(M manager) {
        ManagerTestAccess.useHttpClient(manager, client);
        return manager;
    }

    /**
     * Queues a JSON response for the next request.
     *
     * @param status the HTTP status code
     * @param body   the response body
     * @return this rule
     */
    public MockHttp respond(int status, String body) {
        return respond(status, body, "application/json");
    }

    /**
     * Queues a response with a given content type for the next request.
     *
     * @param status      the HTTP status code
     * @param body        the response body
     * @param contentType the media type, such as {@code text/plain}
     * @return this rule
     */
    public MockHttp respond(int status, String body, String contentType) {
        steps.add(new CannedResponse(status, body, contentType));
        return this;
    }

    /**
     * Queues a JSON response read from {@code src/test/resources/fixtures/}.
     *
     * @param status  the HTTP status code
     * @param fixture the fixture file name
     * @return this rule
     */
    public MockHttp respondWithFixture(int status, String fixture) {
        return respond(status, Fixtures.read(fixture));
    }

    /**
     * Queues a response whose body is raw bytes, with a content type sent exactly as given.
     * Use it to check how the SDK decodes a body, for example one without a declared
     * charset.
     *
     * @param status      the HTTP status code
     * @param body        the response body
     * @param contentType the {@code Content-Type} value, such as {@code text/plain}
     * @return this rule
     */
    public MockHttp respondWithBytes(int status, byte[] body, String contentType) {
        steps.add(new CannedResponse(status, body.clone(), contentType));
        return this;
    }

    /**
     * Queues a response without an entity, like a real {@code 204 No Content}.
     *
     * @param status the HTTP status code
     * @return this rule
     */
    public MockHttp respondWithoutBody(int status) {
        steps.add(new CannedResponse(status, (String) null, null));
        return this;
    }

    /**
     * Queues a JSON response with response headers for the next request, such as the
     * {@code X-Job-Id} and {@code Location} headers of a {@code 202 Accepted} job.
     *
     * @param status  the HTTP status code
     * @param body    the response body, or {@code null} for a response without an entity
     * @param headers the header names and values, in pairs:
     *                {@code "X-Job-Id", "JOB-1", "Location", "https://..."}
     * @return this rule
     * @throws IllegalArgumentException if {@code headers} does not hold name and value pairs
     */
    public MockHttp respondWithHeaders(int status, String body, String... headers) {
        if (headers.length % 2 != 0) {
            throw new IllegalArgumentException("Give the headers as name and value pairs");
        }
        steps.add(new CannedResponse(status, body, body == null ? null : "application/json", headers.clone()));
        return this;
    }

    /**
     * Sets the JSON response used whenever no queued response is left, for code that sends
     * many requests.
     *
     * @param status the HTTP status code
     * @param body   the response body
     * @return this rule
     */
    public MockHttp respondToAll(int status, String body) {
        defaultResponse = new CannedResponse(status, body, "application/json");
        return this;
    }

    /**
     * Makes the next request fail with an I/O error, as if the network were down.
     *
     * @param failure the exception {@code HttpClient.execute} throws
     * @return this rule
     */
    public MockHttp failNextRequest(IOException failure) {
        steps.add(failure);
        return this;
    }

    /**
     * Makes every response arrive after a delay, to simulate a slow server.
     *
     * @param millis how long {@code HttpClient.execute} waits before answering
     * @return this rule
     */
    public MockHttp delayResponses(long millis) {
        responseDelayMillis = millis;
        return this;
    }

    /**
     * Makes {@code close()} on the mocked client throw, for every request.
     *
     * @param failure the exception {@code close()} throws
     * @return this rule
     */
    public MockHttp failOnClose(IOException failure) {
        closeFailure = failure;
        return this;
    }

    // ------------------------------------------------------------------ captured requests

    /** @return every request captured so far, in order */
    public List<CapturedRequest> requests() {
        return Collections.unmodifiableList(new ArrayList<>(requests));
    }

    /** @return the number of requests captured so far */
    public int requestCount() {
        return requests.size();
    }

    /**
     * Returns the only request of the test, failing when there were zero or several.
     *
     * @return the request
     */
    public CapturedRequest request() {
        assertEquals("Number of requests sent: " + requests, 1, requests.size());
        return requests.get(0);
    }

    /**
     * @param index the position of the request, starting at 0
     * @return the request
     */
    public CapturedRequest request(int index) {
        if (index >= requests.size()) {
            throw new AssertionError("Only " + requests.size() + " request(s) were sent: " + requests);
        }
        return requests.get(index);
    }

    /** @return the most recent request */
    public CapturedRequest lastRequest() {
        if (requests.isEmpty()) {
            throw new AssertionError("No request was sent");
        }
        return requests.get(requests.size() - 1);
    }

    /** Asserts that nothing was sent. */
    public void assertNoRequests() {
        assertEquals("Requests sent: " + requests, 0, requests.size());
    }

    /** @return how many times the SDK closed the mocked client */
    public int closeCount() {
        return closeCount.get();
    }

    // ------------------------------------------------------------------ exceptions

    /**
     * Runs {@code call} and returns the {@link APIException} it throws. The exception is
     * checked by the guard, so its message must not contain the API key.
     *
     * @param call the code that should throw
     * @return the exception
     */
    public APIException expectApiException(Call call) {
        return expectThrows(APIException.class, call);
    }

    /**
     * Runs {@code call} and returns the exception of the given type it throws. The
     * exception is checked by the guard, so its message must not contain the API key.
     * Guard failures and other assertion errors are passed through unchanged.
     *
     * @param type the expected exception type
     * @param call the code that should throw
     * @param <T>  the expected exception type
     * @return the exception
     */
    public <T extends Throwable> T expectThrows(Class<T> type, Call call) {
        try {
            call.run();
        } catch (Throwable thrown) {
            if (type.isInstance(thrown)) {
                List<String> leaks = guard.keyLeaks(thrown);
                if (!leaks.isEmpty()) {
                    violations.addAll(leaks);
                    throw new AssertionError(guardMessage(leaks), thrown);
                }
                return type.cast(thrown);
            }
            if (thrown instanceof AssertionError) {
                throw (AssertionError) thrown;
            }
            throw new AssertionError("Expected " + type.getName() + " but got " + thrown, thrown);
        }
        throw new AssertionError("Expected " + type.getName() + " but nothing was thrown");
    }

    // ------------------------------------------------------------------ rule

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                testName = description.getTestClass().getSimpleName() + "." + description.getMethodName();
                LogCapture logs = LogCapture.start();
                Throwable failure = null;
                try {
                    base.evaluate();
                } catch (Throwable thrown) {
                    failure = thrown;
                } finally {
                    logs.stop();
                }
                if (failure instanceof AssumptionViolatedException) {
                    throw failure;
                }
                List<String> problems = new ArrayList<>(violations);
                // A failed assertion is the test's own message; only what it wraps comes from the SDK.
                problems.addAll(guard.keyLeaks(failure instanceof AssertionError ? failure.getCause() : failure));
                for (LogRecord record : logs.records()) {
                    if (guard.containsKey(record.getMessage())) {
                        problems.add("the API key appears in a log message from " + record.getLoggerName());
                    }
                    problems.addAll(guard.keyLeaks(record.getThrown()));
                }
                if (!problems.isEmpty()) {
                    AssertionError error = new AssertionError(guardMessage(problems));
                    if (failure != null) {
                        error.addSuppressed(failure);
                    }
                    throw error;
                }
                if (failure != null) {
                    throw failure;
                }
            }
        };
    }

    /** Captures, checks and answers one request; the mocked {@code execute} calls this. */
    private CloseableHttpResponse execute(HttpUriRequest request) throws IOException {
        CapturedRequest captured = CapturedRequest.from(request);
        requests.add(captured);
        List<String> problems = guard.check(captured, testName);
        if (!problems.isEmpty()) {
            violations.addAll(problems);
            throw new AssertionError(guardMessage(problems));
        }
        Object step = steps.poll();
        if (step == null) {
            step = defaultResponse;
        }
        if (step == null) {
            throw new AssertionError("No canned response is queued for " + captured + "; call respond(...) first");
        }
        if (responseDelayMillis > 0) {
            try {
                Thread.sleep(responseDelayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while delaying the response", e);
            }
        }
        if (step instanceof IOException) {
            throw (IOException) step;
        }
        return ((CannedResponse) step).toHttpResponse();
    }

    /** Formats guard problems as one assertion message. */
    private static String guardMessage(List<String> problems) {
        StringBuilder message = new StringBuilder("Request guard failed:");
        for (String problem : problems) {
            message.append(System.lineSeparator()).append("  - ").append(problem);
        }
        return message.toString();
    }

    /** A status code, optional body and headers; each request gets a fresh response object. */
    private static final class CannedResponse {
        private static final String[] NO_HEADERS = {};

        private final int status;
        private final String body;
        private final byte[] bytes;
        private final String contentType;
        /** Header names and values, in pairs. */
        private final String[] headers;

        CannedResponse(int status, String body, String contentType) {
            this(status, body, contentType, NO_HEADERS);
        }

        CannedResponse(int status, String body, String contentType, String[] headers) {
            this.status = status;
            this.body = body;
            this.bytes = null;
            this.contentType = contentType;
            this.headers = headers;
        }

        CannedResponse(int status, byte[] bytes, String contentType) {
            this.status = status;
            this.body = null;
            this.bytes = bytes;
            this.contentType = contentType;
            this.headers = NO_HEADERS;
        }

        CloseableHttpResponse toHttpResponse() {
            String reason = EnglishReasonPhraseCatalog.INSTANCE.getReason(status, Locale.ENGLISH);
            ClosableResponse response = new ClosableResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, status, reason));
            for (int i = 0; i < headers.length; i += 2) {
                response.addHeader(headers[i], headers[i + 1]);
            }
            if (body != null) {
                response.setEntity(new StringEntity(body, ContentType.create(contentType, StandardCharsets.UTF_8)));
            } else if (bytes != null) {
                ByteArrayEntity entity = new ByteArrayEntity(bytes);
                entity.setContentType(contentType);
                response.setEntity(entity);
            }
            return response;
        }
    }

    /** A plain {@link BasicHttpResponse} that can be closed, as the SDK expects. */
    private static final class ClosableResponse extends BasicHttpResponse implements CloseableHttpResponse {
        ClosableResponse(StatusLine statusLine) {
            super(statusLine);
        }

        @Override
        public void close() {
            // nothing to release
        }
    }

    /** Collects {@code java.util.logging} records written while a test runs. */
    private static final class LogCapture extends Handler {
        private final List<LogRecord> records = new CopyOnWriteArrayList<>();

        /** Starts capturing records on the root logger. */
        static LogCapture start() {
            LogCapture capture = new LogCapture();
            Logger.getLogger("").addHandler(capture);
            return capture;
        }

        void stop() {
            Logger.getLogger("").removeHandler(this);
        }

        List<LogRecord> records() {
            return records;
        }

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
            // records are kept in memory
        }

        @Override
        public void close() {
            // nothing to release
        }
    }
}
