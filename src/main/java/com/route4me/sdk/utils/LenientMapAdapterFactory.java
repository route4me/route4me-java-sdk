package com.route4me.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Reads a JSON object into a {@code Map} field as Gson does, and reads an empty JSON array
 * ({@code []}) as an empty map.
 *
 * <p>The Route4Me API is written in PHP, which encodes an empty associative array as
 * {@code []}: for example {@code "EXT_FIELD_custom_data": []} on an order without custom data,
 * next to {@code {"gate": "A1"}} on another one. This factory reads such a value as an empty
 * map of the field's type, keeps {@code null} as {@code null}, and hands everything else to
 * Gson's own map adapter. A non-empty array is not a map and fails with a
 * {@link JsonSyntaxException}, rather than being read as Gson's list of key and value pairs.
 * Writing is unchanged.
 *
 * <p>{@link #register(GsonBuilder)} installs it; every manager's JSON mapper has it, next to
 * {@link LenientBooleanAdapter}.
 */
public final class LenientMapAdapterFactory implements TypeAdapterFactory {

    private LenientMapAdapterFactory() {
    }

    /**
     * Registers the factory on a Gson builder, for every {@code Map} type.
     *
     * @param builder the builder to change
     * @return the same builder
     */
    public static GsonBuilder register(GsonBuilder builder) {
        return builder.registerTypeAdapterFactory(new LenientMapAdapterFactory());
    }

    /**
     * Returns the lenient adapter for a {@code Map} type, or {@code null} for any other type.
     *
     * @param gson the Gson instance that asks
     * @param type the type to read or write
     * @param <T>  the type to read or write
     * @return the adapter, or {@code null} when {@code type} is not a map
     */
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Map.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonToken token = in.peek();
                if (token == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                if (token == JsonToken.BEGIN_ARRAY) {
                    in.beginArray();
                    if (in.hasNext()) {
                        throw new JsonSyntaxException("Expected a JSON object or an empty array for a map, but was"
                                + " a non-empty array at " + in.getPath());
                    }
                    in.endArray();
                    // Gson's own adapter builds the empty map, of the field's type.
                    return delegate.fromJsonTree(new JsonObject());
                }
                return delegate.read(in);
            }
        };
    }
}
