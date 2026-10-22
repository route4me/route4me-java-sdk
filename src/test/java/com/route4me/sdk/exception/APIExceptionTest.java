package com.route4me.sdk.exception;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link APIException}: the details it parses from error responses and the
 * message it builds.
 */
public class APIExceptionTest {

    private static final String URL = "https://api.route4me.com/api.v4/route.php?route_id=R1&api_key=REDACTED";

    @Test
    public void legacyConstructorsStillWork() {
        IllegalStateException cause = new IllegalStateException("boom");

        APIException plain = new APIException("plain");
        APIException withCause = new APIException("with cause", cause);
        APIException wrapped = new APIException(cause);

        assertEquals("plain", plain.getMessage());
        assertNull(plain.getCause());
        assertSame(cause, withCause.getCause());
        assertEquals("boom", wrapped.getMessage());
        assertSame("the wrapped exception is kept as the cause", cause, wrapped.getCause());
        assertEquals(0, plain.getStatusCode());
        assertNull(plain.getResponseBody());
        assertNull(plain.getMethod());
        assertNull(plain.getUrl());
        assertNull(plain.getCode());
        assertTrue(plain.getErrors().isEmpty());
        assertTrue(plain.getMessages().isEmpty());
    }

    @Test
    public void errorStatusMessageHasStatusMethodPathAndFirstError() {
        APIException error = APIException.forErrorStatus("DELETE", URL, 404, "{\"errors\":[\"Route not found\"]}");

        assertEquals("Invalid status code 404 for DELETE https://api.route4me.com/api.v4/route.php: Route not found",
                error.getMessage());
        assertEquals(URL, error.getUrl());
        assertEquals("DELETE", error.getMethod());
        assertEquals(Collections.singletonList("Route not found"), error.getErrors());
    }

    @Test
    public void errorStatusWithoutBodyHasAShortMessage() {
        APIException error = APIException.forErrorStatus("GET", URL, 500, null);

        assertEquals("Invalid status code 500 for GET https://api.route4me.com/api.v4/route.php", error.getMessage());
        assertNull(error.getResponseBody());
    }

    @Test
    public void errorsMayBeAStringOrAnObject() {
        APIException single = APIException.forErrorStatus("GET", URL, 400, "{\"errors\":\"Only one\"}");
        APIException keyed = APIException.forErrorStatus("GET", URL, 400,
                "{\"errors\":{\"route_id\":[\"Missing\"],\"limit\":\"Too big\"}}");

        assertEquals(Collections.singletonList("Only one"), single.getErrors());
        assertEquals(Arrays.asList("Missing", "Too big"), keyed.getErrors());
    }

    @Test
    public void v5MessagesMayBeAListOrAString() {
        APIException list = APIException.forErrorStatus("GET", URL, 400, "{\"code\":400,\"messages\":[\"A\",\"B\"]}");
        APIException text = APIException.forErrorStatus("GET", URL, 400, "{\"messages\":\"Only one\"}");

        assertEquals(Arrays.asList("A", "B"), list.getMessages().get(""));
        assertEquals(Integer.valueOf(400), list.getCode());
        assertTrue(list.getMessage().endsWith(": A"));
        assertEquals(Collections.singletonList("Only one"), text.getMessages().get(""));
        assertNull(text.getCode());
    }

    @Test
    public void topLevelMessageOrErrorIsUsedWhenThereIsNothingElse() {
        APIException message = APIException.forErrorStatus("GET", URL, 401, "{\"message\":\"Unauthenticated.\"}");
        APIException error = APIException.forErrorStatus("GET", URL, 403, "{\"error\":\"Forbidden\"}");

        assertTrue(message.getMessage(), message.getMessage().endsWith(": Unauthenticated."));
        assertTrue(error.getMessage(), error.getMessage().endsWith(": Forbidden"));
    }

    @Test
    public void v5FeatureGateErrorIsReadFromTheSingularErrorKey() {
        String body = "{\"status\":false,\"exit_code\":1,"
                + "\"error\":\"Access denied: the required feature is not enabled on the used Route4Me account\"}";

        APIException error = APIException.forErrorStatus("GET", URL, 403, body);

        assertEquals(Collections.singletonList(
                "Access denied: the required feature is not enabled on the used Route4Me account"), error.getErrors());
        assertEquals("Invalid status code 403 for GET https://api.route4me.com/api.v4/route.php: "
                        + "Access denied: the required feature is not enabled on the used Route4Me account",
                error.getMessage());
        assertEquals(403, error.getStatusCode());
        assertTrue(error.getMessages().isEmpty());
    }

    @Test
    public void singularErrorIsAddedAfterTheErrorsList() {
        APIException error = APIException.forErrorStatus("GET", URL, 400,
                "{\"errors\":[\"First\"],\"error\":\"Second\"}");

        assertEquals(Arrays.asList("First", "Second"), error.getErrors());
        assertTrue(error.getMessage(), error.getMessage().endsWith(": First"));
    }

    @Test
    public void booleanErrorFlagIsIgnored() {
        APIException flagOnly = APIException.forErrorStatus("GET", URL, 400, "{\"error\":true}");
        APIException flagAndMessage = APIException.forErrorStatus("GET", URL, 400,
                "{\"error\":true,\"message\":\"Route not found\"}");
        APIException flagAndMessages = APIException.forErrorStatus("GET", URL, 422,
                "{\"error\":false,\"messages\":{\"route_id\":[\"Required\"]}}");

        assertTrue(flagOnly.getErrors().isEmpty());
        assertEquals("Invalid status code 400 for GET https://api.route4me.com/api.v4/route.php", flagOnly.getMessage());
        assertTrue(flagAndMessage.getErrors().isEmpty());
        assertTrue(flagAndMessage.getMessage(), flagAndMessage.getMessage().endsWith(": Route not found"));
        assertTrue(flagAndMessages.getErrors().isEmpty());
        assertTrue(flagAndMessages.getMessage(), flagAndMessages.getMessage().endsWith(": Required"));
    }

    @Test
    public void nonJsonOrNonObjectBodiesHaveNoParsedDetails() {
        for (String body : Arrays.asList("<html>oops</html>", "[1,2]", "\"text\"", "", "{\"code\":\"x\"}")) {
            APIException error = APIException.forErrorStatus("GET", URL, 500, body);

            assertTrue(body, error.getErrors().isEmpty());
            assertTrue(body, error.getMessages().isEmpty());
            assertNull(body, error.getCode());
            assertEquals(body, "Invalid status code 500 for GET https://api.route4me.com/api.v4/route.php",
                    error.getMessage());
        }
    }

    @Test
    public void firstErrorIsCutAndKeptOnOneLine() {
        StringBuilder longError = new StringBuilder("line one\r\nline two ");
        for (int i = 0; i < 100; i++) {
            longError.append("0123456789");
        }
        APIException error = APIException.forErrorStatus("GET", URL, 400,
                "{\"errors\":[\"" + longError.toString().replace("\r", "\\r").replace("\n", "\\n") + "\"]}");

        String message = error.getMessage();
        assertTrue(message, message.contains("line one  line two"));
        assertTrue(message, message.endsWith("..."));
        assertTrue(message, message.length() < 400);
    }

    @Test
    public void unparsableResponseKeepsTheCauseAndTheExpectedType() {
        IllegalStateException cause = new IllegalStateException("Expected BEGIN_ARRAY");

        APIException error = APIException.forUnparsableResponse("GET", URL, 200, "{}", "Route[]", cause);

        assertEquals("Unexpected server response for GET https://api.route4me.com/api.v4/route.php (status 200): "
                + "the body is not valid JSON for Route[]", error.getMessage());
        assertSame(cause, error.getCause());
        assertEquals("{}", error.getResponseBody());
    }

    @Test
    public void ioFailureHasNoStatusAndUsesTheCauseNameWhenThereIsNoMessage() {
        IOException cause = new IOException();

        APIException error = APIException.forIOException("POST", URL, cause);

        assertEquals("POST https://api.route4me.com/api.v4/route.php failed: IOException", error.getMessage());
        assertEquals(0, error.getStatusCode());
        assertSame(cause, error.getCause());
    }

    @Test
    public void urlWithoutQueryOrWithFragmentIsShownWithoutIt() {
        assertEquals("Invalid status code 500 for GET https://wh.route4me.com/modules/api/v5.0/vehicles",
                APIException.forErrorStatus("GET", "https://wh.route4me.com/modules/api/v5.0/vehicles", 500, null)
                        .getMessage());
        assertEquals("Invalid status code 500 for GET https://api.route4me.com/x",
                APIException.forErrorStatus("GET", "https://api.route4me.com/x#part", 500, null).getMessage());
        assertEquals("Invalid status code 500 for GET null",
                APIException.forErrorStatus("GET", null, 500, null).getMessage());
    }

    @Test
    public void detailsAreReadOnly() {
        APIException error = APIException.forErrorStatus("GET", URL, 400,
                "{\"errors\":[\"x\"],\"messages\":{\"a\":[\"b\"]}}");

        assertUnsupported(() -> error.getErrors().add("y"));
        assertUnsupported(() -> error.getMessages().put("c", Collections.<String>emptyList()));
        assertUnsupported(() -> error.getMessages().get("a").add("c"));
    }

    @Test
    public void survivesSerialization() throws Exception {
        APIException error = APIException.forErrorStatus("GET", URL, 422,
                "{\"code\":422,\"errors\":[\"x\"],\"messages\":{\"a\":[\"b\"]}}");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(error);
        }
        APIException copy;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            copy = (APIException) in.readObject();
        }

        assertEquals(error.getMessage(), copy.getMessage());
        assertEquals(422, copy.getStatusCode());
        assertEquals(Integer.valueOf(422), copy.getCode());
        assertEquals(Collections.singletonList("x"), copy.getErrors());
        assertEquals(Collections.singletonList("b"), copy.getMessages().get("a"));
    }

    /** Fails unless {@code change} throws UnsupportedOperationException. */
    private static void assertUnsupported(Runnable change) {
        try {
            change.run();
        } catch (UnsupportedOperationException expected) {
            return;
        }
        throw new AssertionError("expected an UnsupportedOperationException");
    }
}
