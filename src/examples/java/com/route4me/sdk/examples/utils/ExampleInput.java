package com.route4me.sdk.examples.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks the targets that the examples read from their arguments or environment variables. A malformed value prints
 * what is wrong and gives {@code null}, so the example stops before it sends a request. It sends no request itself.
 */
public final class ExampleInput {

    private ExampleInput() {
    }

    /**
     * Reads a number, such as an order, stop or member ID.
     *
     * @param name the variable that holds the value, named in the message
     * @param value the value, surrounding spaces allowed, or {@code null}
     * @return the number, or {@code null} after printing that {@code name} must be a number
     */
    public static Long toLong(String name, String value) {
        if (value != null) {
            try {
                return Long.valueOf(value.trim());
            } catch (NumberFormatException e) {
                // Not a number: say so below.
            }
        }
        System.err.println(name + " must be a number: " + value);
        return null;
    }

    /**
     * Reads a number that the API takes as an {@code int}, such as a facility type ID.
     *
     * @param name the variable that holds the value, named in the message
     * @param value the value, surrounding spaces allowed, or {@code null}
     * @return the number, or {@code null} after printing that {@code name} must be a number up to
     *     {@link Integer#MAX_VALUE}
     */
    public static Integer toInteger(String name, String value) {
        if (value != null) {
            try {
                return Integer.valueOf(value.trim());
            } catch (NumberFormatException e) {
                // Not a number, or too large: say so below.
            }
        }
        System.err.println(name + " must be a number up to " + Integer.MAX_VALUE + ": " + value);
        return null;
    }

    /**
     * Splits a comma-separated list, trims each item and drops the empty ones, so {@code " A, ,B,"} gives
     * {@code [A, B]}.
     *
     * @param value the list, or {@code null}
     * @return the items; an empty list for {@code null} or a value with no items
     */
    public static List<String> toList(String value) {
        List<String> items = new ArrayList<>();
        if (value != null) {
            for (String item : value.split(",")) {
                if (!item.trim().isEmpty()) {
                    items.add(item.trim());
                }
            }
        }
        return items;
    }

    /**
     * Reads a comma-separated list of numbers, such as stop or contact IDs, as {@link #toList(String)} splits it.
     *
     * @param name the variable that holds the value, named in the message
     * @param value the list, or {@code null}
     * @return the numbers (an empty list for {@code null} or a value with no items), or {@code null} after printing
     *     that {@code name} must be comma-separated numbers
     */
    public static List<Long> toLongs(String name, String value) {
        List<Long> numbers = new ArrayList<>();
        try {
            for (String item : toList(value)) {
                numbers.add(Long.valueOf(item));
            }
        } catch (NumberFormatException e) {
            System.err.println(name + " must be comma-separated numbers: " + value);
            return null;
        }
        return numbers;
    }
}
