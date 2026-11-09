package com.route4me.sdk.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Reads a JSON boolean that the Route4Me API sends as {@code true}/{@code false}, as a number
 * ({@code 1}/{@code 0}) or as a string, and writes it as a JSON boolean.
 *
 * <p>The API is written in PHP, and many of its boolean fields arrive as {@code 0} or
 * {@code 1}, for example {@code is_predefined} of a vehicle profile. Gson's own adapter
 * rejects numbers, so the whole reply failed to parse. {@link #register(GsonBuilder)} installs
 * this adapter for both {@code Boolean} and {@code boolean}.
 *
 * <p>A number is {@code true} unless it is 0. A string, trimmed and in any case, is
 * {@code true} when it is a plain decimal number other than 0 (digits with an optional sign
 * and fraction, such as {@code "1"}, {@code "2"} or {@code "-0.5"}, as the number would be) or
 * one of the words PHP reads as true ({@code "true"}, {@code "yes"}, {@code "on"}), and
 * {@code false} otherwise ({@code "0"}, {@code "false"}, {@code "no"}, {@code "off"},
 * {@code ""} or any other text, including other number notations such as {@code "1e3"} or
 * {@code "1."}). So a string never makes a reply fail, and
 * every string that Gson's own adapter read as {@code true} in SDK 1.16.1 (with
 * {@code Boolean.parseBoolean}) is still {@code true}.
 */
public final class LenientBooleanAdapter extends TypeAdapter<Boolean> {

    /** A decimal number written as text, such as {@code 1}, {@code -2} or {@code 0.0}. */
    private static final Pattern DECIMAL = Pattern.compile("[+-]?[0-9]+(\\.[0-9]+)?");

    /** The words, besides numbers, that PHP's {@code FILTER_VALIDATE_BOOLEAN} reads as true. */
    private static final Set<String> TRUE_WORDS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList("true", "yes", "on")));

    private LenientBooleanAdapter() {
    }

    /**
     * Registers the adapter for {@code Boolean} and {@code boolean} on a Gson builder.
     *
     * @param builder the builder to change
     * @return the same builder
     */
    public static GsonBuilder register(GsonBuilder builder) {
        LenientBooleanAdapter adapter = new LenientBooleanAdapter();
        return builder.registerTypeAdapter(Boolean.class, adapter).registerTypeAdapter(boolean.class, adapter);
    }

    /**
     * Writes the value as a JSON boolean, or as {@code null}.
     *
     * @param out the JSON writer
     * @param value the value; {@code null} writes {@code null}
     * @throws IOException if the writer fails
     */
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.booleanValue());
        }
    }

    /**
     * Reads a JSON boolean, number or string as a boolean.
     *
     * @param in the JSON reader
     * @return the value; {@code null} for a JSON {@code null}; for a string, {@code true} for a
     *     number other than 0 and for {@code "true"}, {@code "yes"} and {@code "on"} (any case),
     *     and {@code false} for any other text
     * @throws IOException if the reader fails
     * @throws JsonSyntaxException if the value is an object or an array
     */
    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case NULL:
                in.nextNull();
                return null;
            case BOOLEAN:
                return in.nextBoolean();
            case NUMBER:
                return in.nextDouble() != 0;
            case STRING:
                return parse(in.nextString());
            default:
                throw new JsonSyntaxException("Expected a boolean but was " + token + " at " + in.getPath());
        }
    }

    /**
     * Reads a string as this adapter reads a JSON string: {@code true} for a plain decimal number
     * other than 0 and for {@code "true"}, {@code "yes"} and {@code "on"} (trimmed, any case),
     * and {@code false} for any other text. Other adapters of lenient flags use it.
     *
     * @param text the string
     * @return the boolean the string stands for
     * @throws NullPointerException if {@code text} is {@code null}
     */
    public static boolean readString(String text) {
        return parse(text);
    }

    /** Reads a string: a number as a number, a word that PHP reads as true as {@code true}, anything else as false. */
    private static Boolean parse(String text) {
        String value = text.trim().toLowerCase(Locale.ROOT);
        if (DECIMAL.matcher(value).matches()) {
            return new BigDecimal(value).signum() != 0;
        }
        return TRUE_WORDS.contains(value);
    }
}
