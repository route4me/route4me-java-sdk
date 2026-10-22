package com.route4me.sdk.exception;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.route4me.sdk.utils.Sanitizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A request to the Route4Me API failed: the server answered with an error status, the
 * response could not be parsed, or the request could not be sent at all.
 *
 * <p>When the server answered, the exception carries the details in machine-readable form:
 * <ul>
 *   <li>{@link #getStatusCode()}: the HTTP status, for example to retry on {@code 502} but
 *       not on {@code 404};</li>
 *   <li>{@link #getErrors()}: the V4 {@code {"errors": [...]}} list, followed by a
 *       singular {@code "error"} string, which the V5 API sends for example when the
 *       account lacks a module ({@code 403 {"status": false, "error": "..."}});</li>
 *   <li>{@link #getCode()} and {@link #getMessages()}: the V5
 *       {@code {"code": 422, "messages": {...}}} fields;</li>
 *   <li>{@link #getResponseBody()}: the body, cut to {@link #MAX_BODY_LENGTH}
 *       characters;</li>
 *   <li>{@link #getMethod()} and {@link #getUrl()}: the request.</li>
 * </ul>
 *
 * <p>None of it contains the API key: the SDK removes it from the URL and the body before
 * it creates the exception. The message shows the status, the method, the URL without its
 * query string and the first error. It never contains the raw body or the request
 * parameters, so it is safe to log.
 */
public class APIException extends Exception {

    private static final long serialVersionUID = 1L;

    /** The maximum length of {@link #getResponseBody()}. */
    public static final int MAX_BODY_LENGTH = 2000;

    /** The maximum length of the first error shown in the message. */
    private static final int MAX_ERROR_LENGTH = 300;

    private final int statusCode;
    private final String responseBody;
    // Concrete collection types, so that the exception stays serializable.
    private final ArrayList<String> errors;
    private final Integer code;
    private final LinkedHashMap<String, List<String>> messages;
    private final String method;
    private final String url;

    /**
     * Creates an exception with a message only, for errors the SDK detects itself.
     *
     * @param message the message
     */
    public APIException(String message) {
        this(message, null);
    }

    /**
     * Creates an exception with a message and a cause, for errors the SDK detects itself.
     *
     * @param message the message
     * @param e       the cause, or {@code null}
     */
    public APIException(String message, Throwable e) {
        this(message, e, null, null, 0, null);
    }

    /**
     * Wraps another exception, keeping it as the cause and its message as this message.
     *
     * @param e the cause
     */
    public APIException(Exception e) {
        this(e == null ? null : e.getMessage(), e);
    }

    /** Creates an exception with all its details; the body is cut and parsed here. */
    private APIException(String message, Throwable cause, String method, String url, int statusCode,
                         String responseBody) {
        super(message, cause);
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;
        this.responseBody = Sanitizer.truncate(responseBody, MAX_BODY_LENGTH);
        this.errors = new ArrayList<>();
        this.messages = new LinkedHashMap<>();
        this.code = parse(responseBody, this.errors, this.messages);
    }

    /**
     * Creates the exception for a response with an error status.
     *
     * <p>The caller must have removed the API key from {@code url} and {@code responseBody}.
     *
     * @param method       the HTTP method, such as {@code GET}
     * @param url          the request URL, credentials redacted
     * @param statusCode   the HTTP status code
     * @param responseBody the response body, or {@code null} when there was none
     * @return the exception
     */
    public static APIException forErrorStatus(String method, String url, int statusCode, String responseBody) {
        String firstError = firstError(responseBody);
        String message = String.format("Invalid status code %d for %s %s", statusCode, method, withoutQuery(url))
                + (firstError == null ? "" : ": " + firstError);
        return new APIException(message, null, method, url, statusCode, responseBody);
    }

    /**
     * Creates the exception for a successful response whose body could not be parsed.
     *
     * <p>The caller must have removed the API key from {@code url} and {@code responseBody}.
     *
     * @param method       the HTTP method, such as {@code GET}
     * @param url          the request URL, credentials redacted
     * @param statusCode   the HTTP status code
     * @param responseBody the response body
     * @param expectedType the name of the type the body should have matched
     * @param cause        the parse error
     * @return the exception
     */
    public static APIException forUnparsableResponse(String method, String url, int statusCode, String responseBody,
                                                     String expectedType, Throwable cause) {
        String message = String.format("Unexpected server response for %s %s (status %d): the body is not valid JSON for %s",
                method, withoutQuery(url), statusCode, expectedType);
        return new APIException(message, cause, method, url, statusCode, responseBody);
    }

    /**
     * Creates the exception for a request that failed before a response arrived, such as
     * a timeout or a refused connection.
     *
     * @param method the HTTP method, such as {@code GET}
     * @param url    the request URL, credentials redacted
     * @param cause  the I/O error
     * @return the exception
     */
    public static APIException forIOException(String method, String url, IOException cause) {
        String reason = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
        String message = String.format("%s %s failed: %s", method, withoutQuery(url),
                Sanitizer.forLog(reason, MAX_ERROR_LENGTH));
        return new APIException(message, cause, method, url, 0, null);
    }

    /**
     * @return the HTTP status code of the response, or {@code 0} when there was no response
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return the response body, cut to {@link #MAX_BODY_LENGTH} characters, or
     * {@code null} when there was no response
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * @return the {@code errors} of a V4 error response ({@code {"errors": [...]}}), in
     * order, followed by the {@code error} of the body when it is a string (a boolean
     * {@code error} flag is ignored); empty when the body has neither
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * @return the {@code code} of a V5 error response, or {@code null} when the body has none
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Returns the {@code messages} of a V5 error response, by field name. When the server
     * sends a plain list or string instead of a map, its values are under the key
     * {@code ""}.
     *
     * @return the messages; empty when the body has none
     */
    public Map<String, List<String>> getMessages() {
        return Collections.unmodifiableMap(messages);
    }

    /**
     * @return the HTTP method of the request, or {@code null} when it is unknown
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the request URL with credentials redacted, or {@code null} when it is unknown
     */
    public String getUrl() {
        return url;
    }

    /** Returns the URL without its query string and fragment. */
    private static String withoutQuery(String url) {
        if (url == null) {
            return null;
        }
        int end = url.length();
        int query = url.indexOf('?');
        if (query >= 0) {
            end = query;
        }
        int fragment = url.indexOf('#');
        if (fragment >= 0 && fragment < end) {
            end = fragment;
        }
        return url.substring(0, end);
    }

    /**
     * Returns the first error of a body, cleaned for a one-line message: the first of the
     * {@code errors} and the {@code error} string, else the first V5 message, else a
     * top-level {@code message} string.
     */
    private static String firstError(String body) {
        List<String> errors = new ArrayList<>();
        Map<String, List<String>> messages = new LinkedHashMap<>();
        parse(body, errors, messages);
        String first = null;
        if (!errors.isEmpty()) {
            first = errors.get(0);
        } else {
            for (List<String> values : messages.values()) {
                if (!values.isEmpty()) {
                    first = values.get(0);
                    break;
                }
            }
        }
        if (first == null) {
            JsonObject object = parseObject(body);
            if (object != null) {
                first = stringMember(object, "message");
            }
        }
        return first == null ? null : Sanitizer.forLog(first, MAX_ERROR_LENGTH);
    }

    /**
     * Reads the V4 {@code errors}, the singular {@code error} string and the V5
     * {@code messages} of a JSON body.
     *
     * @return the V5 {@code code}, or {@code null}
     */
    private static Integer parse(String body, List<String> errors, Map<String, List<String>> messages) {
        JsonObject object = parseObject(body);
        if (object == null) {
            return null;
        }
        if (object.has("errors")) {
            addTexts(object.get("errors"), errors);
        }
        // The V5 API answers {"status": false, "exit_code": 1, "error": "..."} for example
        // when the account lacks a module. Some answers carry "error": true as a flag only.
        String error = stringMember(object, "error");
        if (error != null) {
            errors.add(error);
        }
        if (object.has("messages")) {
            JsonElement value = object.get("messages");
            if (value.isJsonObject()) {
                for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    List<String> texts = new ArrayList<>();
                    addTexts(entry.getValue(), texts);
                    messages.put(entry.getKey(), Collections.unmodifiableList(texts));
                }
            } else if (!value.isJsonNull()) {
                List<String> texts = new ArrayList<>();
                addTexts(value, texts);
                messages.put("", Collections.unmodifiableList(texts));
            }
        }
        JsonElement code = object.get("code");
        if (code != null && code.isJsonPrimitive() && code.getAsJsonPrimitive().isNumber()) {
            try {
                return code.getAsInt();
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }

    /** Parses a body as a JSON object, or returns {@code null} when it is not one. */
    private static JsonObject parseObject(String body) {
        if (body == null || body.isEmpty()) {
            return null;
        }
        try {
            JsonElement element = JsonParser.parseString(body);
            return element.isJsonObject() ? element.getAsJsonObject() : null;
        } catch (JsonParseException | IllegalStateException ex) {
            return null;
        }
    }

    /** Returns a string member of an object, or {@code null} when it is absent or not a string. */
    private static String stringMember(JsonObject object, String name) {
        JsonElement value = object.get(name);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()
                ? value.getAsString() : null;
    }

    /** Adds the texts of a JSON value: a string, a list, or the values of an object. */
    private static void addTexts(JsonElement value, List<String> texts) {
        if (value == null || value.isJsonNull()) {
            return;
        }
        if (value.isJsonPrimitive()) {
            texts.add(value.getAsString());
        } else if (value.isJsonArray()) {
            JsonArray array = value.getAsJsonArray();
            for (JsonElement element : array) {
                addTexts(element, texts);
            }
        } else if (value.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                addTexts(entry.getValue(), texts);
            }
        }
    }
}
