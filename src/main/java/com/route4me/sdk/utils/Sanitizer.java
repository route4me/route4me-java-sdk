package com.route4me.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Makes text safe to put in logs and exception messages: it hides credentials, removes
 * line breaks and limits the length.
 *
 * <p>The SDK uses these helpers on every response body, URL and user-supplied value that
 * reaches a log line or an {@link com.route4me.sdk.exception.APIException}. They never
 * throw.
 */
public final class Sanitizer {

    /** The text that replaces a hidden credential. */
    public static final String REDACTED = "REDACTED";

    /** The default length limit of {@link #forLog(Object)}. */
    public static final int DEFAULT_LOG_LENGTH = 500;

    /**
     * Query parameters whose values are credentials. Matched case-insensitively, also with
     * a {@code []} suffix.
     */
    private static final Pattern SENSITIVE_QUERY_PARAMETER = Pattern.compile(
            "([?&](?:api_key|apikey|api-key|api_token|access_token|token|connection_token|session_guid"
                    + "|member_session_id|password|member_password|strpassword(?:_[12])?|secret|client_secret)"
                    + "(?:\\[\\]|%5B%5D)?=)[^&#]*",
            Pattern.CASE_INSENSITIVE);

    /** Line breaks and other control characters, which could forge extra log lines. */
    private static final Pattern CONTROL_CHARACTERS = Pattern.compile("[\\p{Cntrl}\\u0085\\u2028\\u2029]");

    /** Not instantiable: static helpers only. */
    private Sanitizer() {
    }

    /**
     * Hides the values of credential query parameters ({@code api_key}, {@code api_token},
     * {@code password}, ...) in a URL.
     *
     * @param url the URL, or {@code null}
     * @return the URL with each credential value replaced by {@link #REDACTED}, or
     * {@code null} when {@code url} is {@code null}
     */
    public static String redactUrl(String url) {
        if (url == null) {
            return null;
        }
        return SENSITIVE_QUERY_PARAMETER.matcher(url).replaceAll("$1" + Matcher.quoteReplacement(REDACTED));
    }

    /**
     * Replaces every occurrence of a secret, as typed and URL-encoded, with
     * {@link #REDACTED}.
     *
     * @param text   the text, or {@code null}
     * @param secret the value to hide, such as the API key; {@code null} or empty hides nothing
     * @return the text without the secret, or {@code null} when {@code text} is {@code null}
     */
    public static String redact(String text, String secret) {
        if (text == null || secret == null || secret.isEmpty()) {
            return text;
        }
        String result = text.replace(secret, REDACTED);
        try {
            String encoded = URLEncoder.encode(secret, "UTF-8");
            if (!encoded.equals(secret)) {
                result = result.replace(encoded, REDACTED);
            }
        } catch (UnsupportedEncodingException ex) {
            // UTF-8 is always supported.
        }
        return result;
    }

    /**
     * Shortens a text to at most {@code maxLength} characters, marking the cut with
     * {@code "..."}.
     *
     * @param text      the text, or {@code null}
     * @param maxLength the maximum length of the result; at least 3
     * @return the text, shortened when it was longer, or {@code null} when it is {@code null}
     */
    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        int keep = Math.max(0, maxLength - 3);
        return text.substring(0, keep) + "...";
    }

    /**
     * Makes a value safe for one log line: line breaks and other control characters become
     * spaces, credential query parameters are hidden, and the text is cut to
     * {@link #DEFAULT_LOG_LENGTH} characters.
     *
     * @param value the value to log; {@code null} gives {@code "null"}
     * @return the safe text
     */
    public static String forLog(Object value) {
        return forLog(value, DEFAULT_LOG_LENGTH);
    }

    /**
     * Makes a value safe for one log line, like {@link #forLog(Object)}, with a custom
     * length limit.
     *
     * @param value     the value to log; {@code null} gives {@code "null"}
     * @param maxLength the maximum length of the result; at least 3
     * @return the safe text
     */
    public static String forLog(Object value, int maxLength) {
        String text = String.valueOf(value);
        return truncate(redactUrl(CONTROL_CHARACTERS.matcher(text).replaceAll(" ")), maxLength);
    }
}
