package com.route4me.sdk.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Reads a JSON boolean that the Route4Me API sends as {@code true}/{@code false}, as a number
 * ({@code 1}/{@code 0}) or as a string ({@code "1"}, {@code "0"}, {@code "true"},
 * {@code "false"}), and writes it as a JSON boolean.
 *
 * <p>The API is written in PHP, and many of its boolean fields arrive as {@code 0} or
 * {@code 1}, for example {@code is_predefined} of a vehicle profile. Gson's own adapter
 * rejects those, so the whole reply failed to parse. {@link #register(GsonBuilder)} installs
 * this adapter for both {@code Boolean} and {@code boolean}.
 */
public final class LenientBooleanAdapter extends TypeAdapter<Boolean> {

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
     * @return the value; {@code null} for a JSON {@code null}
     * @throws IOException if the reader fails
     * @throws JsonSyntaxException if the value is not a boolean, a number or one of the strings
     *     {@code "1"}, {@code "0"}, {@code "true"} or {@code "false"}
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
                return parse(in.nextString(), in);
            default:
                throw new JsonSyntaxException("Expected a boolean but was " + token + " at " + in.getPath());
        }
    }

    private static Boolean parse(String text, JsonReader in) {
        String value = text.trim().toLowerCase(Locale.ROOT);
        if ("1".equals(value) || "true".equals(value)) {
            return Boolean.TRUE;
        }
        if ("0".equals(value) || "false".equals(value)) {
            return Boolean.FALSE;
        }
        if (value.isEmpty()) {
            return null;
        }
        throw new JsonSyntaxException("Expected a boolean but was the string \"" + Sanitizer.forLog(text)
                + "\" at " + in.getPath());
    }
}
