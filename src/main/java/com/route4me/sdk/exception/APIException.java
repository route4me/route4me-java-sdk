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
import java.util.Objects;

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
 * <p>The body is parsed once, in full, but the exception keeps at most 50 errors and 50
 * messages from it, each cut to 300 characters.
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

    /** The maximum length of the first error shown in the message, and of each error and message kept. */
    private static final int MAX_ERROR_LENGTH = 300;

    /** The most errors, and the most messages, that an exception keeps from a body. */
    private static final int MAX_TEXTS = 50;

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
        this(message, e, null, null, 0, null, Details.parse(null));
    }

    /**
     * Wraps another exception, keeping it as the cause and its message as this message.
     *
     * @param e the cause
     */
    public APIException(Exception e) {
        this(e == null ? null : e.getMessage(), e);
    }

    /** Creates an exception with all its details; the body is cut here, and was parsed into {@code details}. */
    private APIException(String message, Throwable cause, String method, String url, int statusCode,
                         String responseBody, Details details) {
        super(message, cause);
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;
        this.responseBody = Sanitizer.truncate(responseBody, MAX_BODY_LENGTH);
        this.errors = details.errors;
        this.messages = details.messages;
        this.code = details.code;
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
        Details details = Details.parse(responseBody);
        String firstError = details.firstError();
        String message = String.format("Invalid status code %d for %s %s", statusCode, method, withoutQuery(url))
                + (firstError == null ? "" : ": " + firstError);
        return new APIException(message, null, method, url, statusCode, responseBody, details);
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
        String message = String.format(
                "Unexpected server response for %s %s (status %d): the body is not valid JSON for %s",
                method, withoutQuery(url), statusCode, expectedType);
        return new APIException(message, cause, method, url, statusCode, responseBody, Details.parse(responseBody));
    }

    /**
     * Creates the exception for a request that failed before a response arrived, such as
     * a timeout or a refused connection.
     *
     * <p>When the message of the I/O error, or of one of its causes, holds a credential query
     * parameter (Apache HttpClient quotes the whole target of a refused or circular redirect,
     * for example), the exception keeps a redacted copy of the chain as its cause instead
     * (see {@link Sanitizer#redactCause(IOException, String)}).
     *
     * @param method the HTTP method, such as {@code GET}
     * @param url    the request URL, credentials redacted
     * @param cause  the I/O error
     * @return the exception
     * @throws NullPointerException if {@code cause} is {@code null}
     */
    public static APIException forIOException(String method, String url, IOException cause) {
        Objects.requireNonNull(cause, "cause");
        IOException safe = Sanitizer.redactCause(cause, null);
        String reason = safe.getMessage() != null ? safe.getMessage() : cause.getClass().getSimpleName();
        String message = String.format("%s %s failed: %s", method, withoutQuery(url),
                Sanitizer.forLog(reason, MAX_ERROR_LENGTH));
        return new APIException(message, safe, method, url, 0, null, Details.parse(null));
    }

    /**
     * Returns the HTTP status of the response.
     *
     * @return the HTTP status code of the response, or {@code 0} when there was no response
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the response body, cut.
     *
     * @return the response body, cut to {@link #MAX_BODY_LENGTH} characters, or {@code null} when there was no
     *     response
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Returns the errors of the response: at most 50, each cut to 300 characters.
     *
     * @return the {@code errors} of a V4 error response ({@code {"errors": [...]}}), in order, followed by the
     *     {@code error} of the body when it is a string (a boolean {@code error} flag is ignored); empty when the
     *     body has neither
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Returns the code of a V5 error response.
     *
     * @return the {@code code} of a V5 error response, or {@code null} when the body has none
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Returns the {@code messages} of a V5 error response, by field name. When the server
     * sends a plain list or string instead of a map, its values are under the key
     * {@code ""}. At most 50 messages are kept in all, and each message and field name is cut
     * to 300 characters.
     *
     * @return the messages; empty when the body has none
     */
    public Map<String, List<String>> getMessages() {
        return Collections.unmodifiableMap(messages);
    }

    /**
     * Returns the HTTP method of the request.
     *
     * @return the HTTP method, such as {@code GET}, or {@code null} when it is unknown
     */
    public String getMethod() {
        return method;
    }

    /**
     * Returns the URL of the request, without credentials.
     *
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

    /** Cuts a text that the exception keeps to {@link #MAX_ERROR_LENGTH} characters. */
    private static String cut(String text) {
        return Sanitizer.truncate(text, MAX_ERROR_LENGTH);
    }

    /**
     * Adds the texts of a JSON value (a string, a list, or the values of an object), each cut,
     * until {@code texts} holds {@code limit} of them.
     */
    private static void addTexts(JsonElement value, List<String> texts, int limit) {
        if (value == null || value.isJsonNull() || texts.size() >= limit) {
            return;
        }
        if (value.isJsonPrimitive()) {
            texts.add(cut(value.getAsString()));
        } else if (value.isJsonArray()) {
            JsonArray array = value.getAsJsonArray();
            for (JsonElement element : array) {
                addTexts(element, texts, limit);
            }
        } else if (value.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                addTexts(entry.getValue(), texts, limit);
            }
        }
    }

    /**
     * The details of a body, parsed once: the V4 {@code errors} and singular {@code error}
     * string, the V5 {@code code} and {@code messages}, and a top-level {@code message} string.
     */
    private static final class Details {
        /** The errors, at most {@link #MAX_TEXTS}. */
        private final ArrayList<String> errors = new ArrayList<>();
        /** The messages by field name: at most {@link #MAX_TEXTS} in all, under at most as many names. */
        private final LinkedHashMap<String, List<String>> messages = new LinkedHashMap<>();
        /** The V5 {@code code}, or {@code null}. */
        private Integer code;
        /** The top-level {@code message} string, or {@code null}; used for the exception message only. */
        private String message;

        /**
         * Parses a body.
         *
         * @param body the body, or {@code null}
         * @return its details; empty when the body is not a JSON object
         */
        static Details parse(String body) {
            Details details = new Details();
            JsonObject object = parseObject(body);
            if (object == null) {
                return details;
            }
            if (object.has("errors")) {
                addTexts(object.get("errors"), details.errors, MAX_TEXTS);
            }
            // The V5 API answers {"status": false, "exit_code": 1, "error": "..."} for example
            // when the account lacks a module. Some answers carry "error": true as a flag only.
            String error = stringMember(object, "error");
            if (error != null && details.errors.size() < MAX_TEXTS) {
                details.errors.add(cut(error));
            }
            if (object.has("messages")) {
                details.readMessages(object.get("messages"));
            }
            JsonElement code = object.get("code");
            if (code != null && code.isJsonPrimitive() && code.getAsJsonPrimitive().isNumber()) {
                try {
                    details.code = code.getAsInt();
                } catch (NumberFormatException ex) {
                    details.code = null;
                }
            }
            String message = stringMember(object, "message");
            details.message = message == null ? null : cut(message);
            return details;
        }

        /** Reads the V5 {@code messages}: a map of field names to texts, or a plain list or string. */
        private void readMessages(JsonElement value) {
            if (value.isJsonObject()) {
                int left = MAX_TEXTS;
                for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    if (left <= 0 || this.messages.size() >= MAX_TEXTS) {
                        break;
                    }
                    List<String> texts = new ArrayList<>();
                    addTexts(entry.getValue(), texts, left);
                    left -= texts.size();
                    this.messages.put(cut(entry.getKey()), Collections.unmodifiableList(texts));
                }
            } else if (!value.isJsonNull()) {
                List<String> texts = new ArrayList<>();
                addTexts(value, texts, MAX_TEXTS);
                this.messages.put("", Collections.unmodifiableList(texts));
            }
        }

        /**
         * Returns the first error, cleaned for a one-line message: the first of the
         * {@code errors} and the {@code error} string, else the first V5 message, else the
         * top-level {@code message} string.
         *
         * @return the error, or {@code null} when the body has none
         */
        String firstError() {
            String first = null;
            if (!this.errors.isEmpty()) {
                first = this.errors.get(0);
            } else {
                for (List<String> values : this.messages.values()) {
                    if (!values.isEmpty()) {
                        first = values.get(0);
                        break;
                    }
                }
            }
            if (first == null) {
                first = this.message;
            }
            return first == null ? null : Sanitizer.forLog(first, MAX_ERROR_LENGTH);
        }
    }
}
