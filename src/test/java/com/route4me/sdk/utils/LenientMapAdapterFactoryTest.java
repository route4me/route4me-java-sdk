package com.route4me.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LenientMapAdapterFactory}: PHP sends an empty map as {@code []}, for
 * example {@code "EXT_FIELD_custom_data": []} on an order without custom data.
 */
public class LenientMapAdapterFactoryTest {

    private static final Gson GSON = LenientMapAdapterFactory.register(new GsonBuilder()).create();

    /** A model with map fields of several types. */
    static final class Model {
        @SerializedName("custom_data")
        private Map<String, Object> customData;
        @SerializedName("labels")
        private Map<String, String> labels;
        @SerializedName("counts")
        private HashMap<String, Integer> counts;
        @SerializedName("lists")
        private Map<String, List<String>> lists;
    }

    @Test
    public void anEmptyArrayIsAnEmptyMapOfTheFieldType() {
        Model model = GSON.fromJson("{\"custom_data\":[],\"labels\":[ ],\"counts\":[],\"lists\":[]}", Model.class);

        assertTrue(model.customData.isEmpty());
        assertTrue(model.labels.isEmpty());
        assertTrue(model.counts.isEmpty());
        assertEquals(HashMap.class, model.counts.getClass());
        assertTrue(model.lists.isEmpty());
    }

    @Test
    public void objectsAreReadAsGsonReadsThem() {
        Model empty = GSON.fromJson("{\"custom_data\":{}}", Model.class);
        Model full = GSON.fromJson("{\"custom_data\":{\"gate\":\"A1\",\"floor\":2},\"labels\":{\"a\":\"b\"},"
                + "\"lists\":{\"x\":[\"1\",\"2\"]}}", Model.class);

        assertTrue(empty.customData.isEmpty());
        assertEquals("A1", full.customData.get("gate"));
        assertEquals(2.0, full.customData.get("floor"));
        assertEquals("b", full.labels.get("a"));
        assertEquals("2", full.lists.get("x").get(1));
        assertEquals(new Gson().fromJson("{\"custom_data\":{\"gate\":\"A1\",\"floor\":2}}", Model.class).customData,
                full.customData);
    }

    @Test
    public void nullStaysNull() {
        Model model = GSON.fromJson("{\"custom_data\":null}", Model.class);

        assertNull(model.customData);
        assertNull(model.labels);
    }

    @Test
    public void aNonEmptyArrayStillFails() {
        for (String json : new String[] {"{\"custom_data\":[1,2]}", "{\"labels\":[[\"k\",\"v\"]]}"}) {
            try {
                GSON.fromJson(json, Model.class);
                fail("expected " + json + " to fail");
            } catch (JsonSyntaxException expected) {
                assertTrue(expected.getMessage(), expected.getMessage().contains("non-empty array"));
            }
        }
    }

    @Test
    public void writingIsUnchanged() {
        Model model = new Model();
        model.labels = new HashMap<>();
        model.labels.put("a", "b");

        assertEquals("{\"labels\":{\"a\":\"b\"}}", GSON.toJson(model));
    }

    @Test
    public void otherTypesAreLeftToGson() {
        assertEquals("[1,2]", GSON.toJson(GSON.fromJson("[1,2]", int[].class)));
        assertNull(LenientMapAdapterFactory.register(new GsonBuilder()).create()
                .fromJson("null", Model.class));
    }
}
