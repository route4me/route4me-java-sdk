package com.route4me.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LenientBooleanAdapter}: the API sends many booleans as {@code 0} or
 * {@code 1}, for example {@code is_predefined} of a vehicle profile.
 */
public class LenientBooleanAdapterTest {

    private static final Gson GSON = LenientBooleanAdapter.register(new GsonBuilder()).create();

    /** A model with a boxed and a primitive boolean. */
    static final class Flags {
        @SerializedName("is_predefined")
        private Boolean predefined;
        @SerializedName("is_default")
        private boolean isDefault;
    }

    @Test
    public void numbersAreReadAsBooleans() {
        Flags flags = GSON.fromJson("{\"is_predefined\":1,\"is_default\":0}", Flags.class);

        assertTrue(flags.predefined);
        assertFalse(flags.isDefault);
    }

    @Test
    public void booleansAndBooleanStringsAreRead() {
        assertTrue(GSON.fromJson("{\"is_predefined\":true}", Flags.class).predefined);
        assertTrue(GSON.fromJson("{\"is_predefined\":\"1\"}", Flags.class).predefined);
        assertFalse(GSON.fromJson("{\"is_predefined\":\"false\"}", Flags.class).predefined);
        assertTrue(GSON.fromJson("{\"is_default\":\"TRUE\"}", Flags.class).isDefault);
    }

    @Test
    public void nullLeavesTheFieldUnset() {
        assertNull(GSON.fromJson("{\"is_predefined\":null}", Flags.class).predefined);
        assertFalse(GSON.fromJson("{\"is_default\":null}", Flags.class).isDefault);
    }

    /**
     * SDK 1.16.1 read a string with {@code Boolean.parseBoolean}: only {@code "true"} was true. A
     * string the API sends for false still reads as false, and so does any text that names
     * no boolean, so that a string never fails the parse.
     */
    @Test
    public void anEmptyStringFalseWordsZeroAndOtherTextAreFalse() {
        for (String text : Arrays.asList("", " ", "0", "00", "0.0", "-0", "false", "FALSE", "no", "off", "t", "f",
                "null", "maybe", "NaN", "Infinity", "1e3", "0x1", "1.", "yes please")) {
            String json = "{\"is_predefined\":\"" + text + "\",\"is_default\":\"" + text + "\"}";
            Flags flags = GSON.fromJson(json, Flags.class);
            assertEquals(text, Boolean.FALSE, flags.predefined);
            assertFalse(text, flags.isDefault);
        }
    }

    /**
     * A numeric string reads as the number would ({@code "2"} like {@code 2}), and PHP's own
     * words for true ({@code FILTER_VALIDATE_BOOLEAN}) read as true, so that the API cannot send
     * a true that the SDK reads as false. Every string that 1.16.1 read as true still is.
     */
    @Test
    public void nonZeroNumericStringsAndPhpTrueWordsAreTrue() {
        for (String text : Arrays.asList("1", " True ", "TRUE", "2", "-1", "+3", "0.5", "10", "yes", "YES", "on",
                "On")) {
            String json = "{\"is_predefined\":\"" + text + "\",\"is_default\":\"" + text + "\"}";
            Flags flags = GSON.fromJson(json, Flags.class);
            assertEquals(text, Boolean.TRUE, flags.predefined);
            assertTrue(text, flags.isDefault);
        }
    }

    @Test
    public void numbersOtherThanZeroAreTrue() {
        assertTrue(GSON.fromJson("{\"is_predefined\":2}", Flags.class).predefined);
        assertTrue(GSON.fromJson("{\"is_predefined\":-1}", Flags.class).predefined);
        assertFalse(GSON.fromJson("{\"is_predefined\":0.0}", Flags.class).predefined);
    }

    @Test(expected = JsonSyntaxException.class)
    public void objectsAreRejected() {
        GSON.fromJson("{\"is_predefined\":{}}", Flags.class);
    }

    @Test
    public void booleansAreWrittenAsJsonBooleans() {
        Flags flags = new Flags();
        flags.predefined = Boolean.TRUE;

        assertEquals("{\"is_predefined\":true,\"is_default\":false}", GSON.toJson(flags));
    }
}
