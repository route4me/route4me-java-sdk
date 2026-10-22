package com.route4me.sdk.utils;

import org.junit.Test;

import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;

public class DateTimeUtilsTest {

    @Test
    public void testUTCDateStaysSameDay() {
        String result = DateTimeUtils.localDateTimeToUTCParser("2023-06-15T10:30:00:000+00:00");
        assertEquals("2023-06-15", result);
    }

    @Test
    public void testPositiveOffsetConvertedToUTC() {
        String result = DateTimeUtils.localDateTimeToUTCParser("2023-06-15T14:30:00:000+05:00");
        assertEquals("2023-06-15", result);
    }

    @Test
    public void testNegativeOffsetConvertedToUTC() {
        String result = DateTimeUtils.localDateTimeToUTCParser("2023-06-15T10:30:00:000-04:00");
        assertEquals("2023-06-15", result);
    }

    @Test
    public void testOffsetCrossingMidnightChangesDate() {
        // 03:00 at +05:00 on the 16th is 22:00 UTC on the 15th.
        String result = DateTimeUtils.localDateTimeToUTCParser("2023-06-16T03:00:00:000+05:00");
        assertEquals("2023-06-15", result);
    }

    @Test
    public void testNegativeOffsetCrossingMidnightForward() {
        String result = DateTimeUtils.localDateTimeToUTCParser("2023-06-15T22:00:00:000-05:00");
        assertEquals("2023-06-16", result);
    }

    @Test(expected = DateTimeParseException.class)
    public void testInvalidFormatThrowsException() {
        DateTimeUtils.localDateTimeToUTCParser("not-a-date");
    }

    @Test(expected = DateTimeParseException.class)
    public void testWrongFormatThrowsException() {
        DateTimeUtils.localDateTimeToUTCParser("2023-06-15 10:30:00");
    }
}
