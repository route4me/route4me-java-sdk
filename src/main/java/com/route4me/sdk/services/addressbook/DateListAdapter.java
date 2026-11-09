package com.route4me.sdk.services.addressbook;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the {@code schedule_blacklist} of a contact, a list of {@code YYYY-MM-DD} dates, in every shape the address
 * book spec allows, and writes it as a JSON array of strings.
 *
 * <p>The spec types the field as an array of strings, but adds that it "also can be a date string", and its
 * {@code ResponseAddress} nests that item in another array. With Gson's own adapter, one contact that came back with
 * {@code "schedule_blacklist": "2026-12-25"} failed the parse of the whole list. This adapter reads a string as a list
 * of one date, an array as its dates, and an array of arrays as all their dates in order. {@code null} items and empty
 * strings are left out, so {@code ""} reads as an empty list.
 */
final class DateListAdapter extends TypeAdapter<List<String>> {

    /**
     * Writes the dates as a JSON array of strings, or {@code null}.
     *
     * @param out the JSON writer
     * @param value the dates; {@code null} writes {@code null}
     * @throws IOException if the writer fails
     */
    @Override
    public void write(JsonWriter out, List<String> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (String date : value) {
            out.value(date);
        }
        out.endArray();
    }

    /**
     * Reads a date string, an array of dates or an array of such arrays as a list of dates.
     *
     * @param in the JSON reader
     * @return the dates; {@code null} for a JSON {@code null}
     * @throws IOException if the reader fails
     * @throws JsonSyntaxException if the value or an item is a boolean or an object
     */
    @Override
    public List<String> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        List<String> dates = new ArrayList<>();
        readInto(in, dates);
        return dates;
    }

    /** Adds the dates of the next value to a list, going into nested arrays. */
    private static void readInto(JsonReader in, List<String> dates) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case NULL:
                in.nextNull();
                return;
            case STRING:
            case NUMBER:
                String date = in.nextString();
                if (!date.isEmpty()) {
                    dates.add(date);
                }
                return;
            case BEGIN_ARRAY:
                in.beginArray();
                while (in.hasNext()) {
                    readInto(in, dates);
                }
                in.endArray();
                return;
            default:
                throw new JsonSyntaxException("Expected a date string or an array of dates for schedule_blacklist"
                        + " but was " + token + " at " + in.getPath());
        }
    }
}
