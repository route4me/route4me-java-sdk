/*
 * The MIT License
 *
 * Copyright 2022 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** Date conversions for the route API. */
public class DateTimeUtils {

    /** Parses a local date and time with its offset, such as {@code 2026-10-22T08:00:00:000+02:00}. */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "uuuu-MM-dd'T'HH:mm:ss:SSSXXXXX");
    /** Formats a date as {@code YYYY-MM-DD}. */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    /**
     * Converts a local date and time to the UTC date the route API takes.
     *
     * @param dateString a date and time in {@link #DATE_TIME_FORMATTER} format, such as
     *     {@code 2026-10-22T08:00:00:000+02:00}
     * @return the UTC date of that instant, as {@code YYYY-MM-DD}
     * @throws java.time.format.DateTimeParseException if the text is not in that format
     */
    public static String localDateTimeToUTCParser(String dateString) {
        OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(dateString, DATE_TIME_FORMATTER);
        OffsetDateTime odtInstanceAtUTC = odtInstanceAtOffset.withOffsetSameInstant(ZoneOffset.UTC);
        String dateStringInUTC = odtInstanceAtUTC.format(DATE_FORMATTER);
        return dateStringInUTC;

    }

}
