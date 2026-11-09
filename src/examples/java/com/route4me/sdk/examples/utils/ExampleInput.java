package com.route4me.sdk.examples.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks the targets that the examples read from their arguments or environment variables. A malformed value prints
 * what is wrong and gives {@code null}, so the example returns on {@code null} without a second message and stops
 * before it sends a request. It sends no request itself.
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
     * Splits a comma-separated list and trims each item, so {@code " A, B "} gives {@code [A, B]}. An empty item, as
     * in {@code "A,,B"} or {@code "A,B,"}, is a malformed list: a typo there could leave out a target.
     *
     * @param name the variable that holds the list, named in the message
     * @param value the list, or {@code null}
     * @return the items (an empty list for {@code null} or a blank value), or {@code null} after printing that
     *     {@code name} has an empty item
     */
    public static List<String> toList(String name, String value) {
        List<String> items = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            return items;
        }
        // The limit -1 keeps trailing empty items, so "A,B," is caught too.
        for (String item : value.split(",", -1)) {
            if (item.trim().isEmpty()) {
                System.err.println(name + " has an empty item: " + value);
                return null;
            }
            items.add(item.trim());
        }
        return items;
    }

    /**
     * Reads a comma-separated list of numbers, such as stop or contact IDs, as {@link #toList(String, String)} splits
     * it.
     *
     * @param name the variable that holds the value, named in the message
     * @param value the list, or {@code null}
     * @return the numbers (an empty list for {@code null} or a blank value), or {@code null} after printing that
     *     {@code name} has an empty item or must be comma-separated numbers
     */
    public static List<Long> toLongs(String name, String value) {
        List<String> items = toList(name, value);
        if (items == null) {
            return null;
        }
        List<Long> numbers = new ArrayList<>();
        try {
            for (String item : items) {
                numbers.add(Long.valueOf(item));
            }
        } catch (NumberFormatException e) {
            System.err.println(name + " must be comma-separated numbers: " + value);
            return null;
        }
        return numbers;
    }

    /**
     * Reads a comma-separated list of numbers that the API takes as {@code int}s, such as member IDs, as
     * {@link #toList(String, String)} splits it.
     *
     * @param name the variable that holds the value, named in the message
     * @param value the list, or {@code null}
     * @return the numbers (an empty list for {@code null} or a blank value), or {@code null} after printing that
     *     {@code name} has an empty item or must be comma-separated numbers up to {@link Integer#MAX_VALUE}
     */
    public static List<Integer> toIntegers(String name, String value) {
        List<String> items = toList(name, value);
        if (items == null) {
            return null;
        }
        List<Integer> numbers = new ArrayList<>();
        try {
            for (String item : items) {
                numbers.add(Integer.valueOf(item));
            }
        } catch (NumberFormatException e) {
            System.err.println(name + " must be comma-separated numbers up to " + Integer.MAX_VALUE + ": " + value);
            return null;
        }
        return numbers;
    }
}
