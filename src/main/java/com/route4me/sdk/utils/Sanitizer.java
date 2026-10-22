package com.route4me.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
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
     * Query parameter names whose values are credentials, in lower case. See
     * {@link #isSensitiveParameter(String)}.
     */
    private static final Set<String> SENSITIVE_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "api_key", "apikey", "api-key", "api_token", "access_token", "token", "connection_token",
            "session_guid", "member_session_id", "password", "member_password", "strpassword",
            "strpassword_1", "strpassword_2", "secret", "client_secret")));

    /**
     * Name endings that mark a credential, such as {@code organization_api_key} (a V5 query
     * parameter) or {@code device_token}, in lower case.
     */
    private static final String[] SENSITIVE_SUFFIXES = {"api_key", "apikey", "_token", "_secret", "password"};

    /** The start of a query parameter value: {@code ?name=} or {@code &name=}. */
    private static final Pattern QUERY_PARAMETER = Pattern.compile("[?&]([^=&#?\\s]+)=");

    /** Line breaks and other control characters, which could forge extra log lines. */
    private static final Pattern CONTROL_CHARACTERS = Pattern.compile("[\\p{Cntrl}\\u0085\\u2028\\u2029]");

    /** Not instantiable: static helpers only. */
    private Sanitizer() {
    }

    /**
     * Hides the values of credential query parameters in a URL, or in a text that contains
     * URLs: every parameter for which {@link #isSensitiveParameter(String)} is true, such as
     * {@code api_key}, {@code api_token}, {@code password} or {@code organization_api_key}.
     *
     * <p>A value runs to the next {@code &} or {@code #}. A parameter inside the value of
     * another one, such as a URL passed as {@code ?next=https://...?api_key=...}, is found
     * too.
     *
     * @param url the URL, or {@code null}
     * @return the URL with each credential value replaced by {@link #REDACTED}, or
     * {@code null} when {@code url} is {@code null}
     */
    public static String redactUrl(String url) {
        if (url == null) {
            return null;
        }
        Matcher parameter = QUERY_PARAMETER.matcher(url);
        StringBuilder result = null;
        int copied = 0;
        int from = 0;
        while (parameter.find(from)) {
            if (isSensitiveParameter(parameter.group(1))) {
                int end = valueEnd(url, parameter.end());
                if (result == null) {
                    result = new StringBuilder(url.length());
                }
                result.append(url, copied, parameter.end()).append(REDACTED);
                copied = end;
                from = end;
            } else {
                // Keep searching inside the value, which may hold a URL with its own query.
                from = parameter.end();
            }
        }
        return result == null ? url : result.append(url, copied, url.length()).toString();
    }

    /**
     * Tells whether a query or form parameter holds a credential.
     *
     * <p>The name is percent-decoded and compared in lower case. It is a credential when
     * it, or one of its bracketed keys ({@code filters[device_token]},
     * {@code organization_api_key[]}), is one of {@code api_key}, {@code apikey},
     * {@code api-key}, {@code api_token}, {@code access_token}, {@code token},
     * {@code connection_token}, {@code session_guid}, {@code member_session_id},
     * {@code password}, {@code member_password}, {@code strpassword},
     * {@code strpassword_1}, {@code strpassword_2}, {@code secret} and
     * {@code client_secret}, or ends with {@code api_key}, {@code apikey}, {@code _token},
     * {@code _secret} or {@code password}.
     *
     * @param name the parameter name, as it appears in a query string; {@code null} is not
     *             a credential
     * @return {@code true} when the value of the parameter must be hidden
     */
    public static boolean isSensitiveParameter(String name) {
        if (name == null) {
            return false;
        }
        String decoded;
        try {
            decoded = URLDecoder.decode(name, "UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException ex) {
            // A malformed escape: judge the name as written.
            decoded = name;
        }
        for (String part : decoded.toLowerCase(Locale.ROOT).split("[\\[\\]]")) {
            if (part.isEmpty()) {
                continue;
            }
            if (SENSITIVE_NAMES.contains(part)) {
                return true;
            }
            for (String suffix : SENSITIVE_SUFFIXES) {
                if (part.endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns where a query value that starts at {@code start} ends: the next {@code &} or {@code #}. */
    private static int valueEnd(String url, int start) {
        for (int i = start; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == '&' || c == '#') {
                return i;
            }
        }
        return url.length();
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
