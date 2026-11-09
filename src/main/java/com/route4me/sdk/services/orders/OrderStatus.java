/*
 * The MIT License
 *
 * Copyright 2021 Route4Me.
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
package com.route4me.sdk.services.orders;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** The status codes of an order ({@code order_status_id}); {@link #getValue()} gives the code. */
public enum OrderStatus {

    /** New ({@code 0}). */
    NEW(0),
    /** Inbound scan ({@code 1}). */
    INBOUND_SCAN(1),
    /** Sorted by territory ({@code 2}). */
    SORTED_BY_TERRITORY(2),
    /** Loaded ({@code 3}). */
    LOADED(3),
    /** Missing ({@code 4}). */
    MISSING(4),
    /** Damaged ({@code 5}). */
    DAMAGED(5),
    /** Manually loaded ({@code 6}). */
    MANUALLY_LOADED(6),
    /** Routed ({@code 7}). */
    ROUTED(7),
    /** Unrouted ({@code 8}). */
    UNROUTED(8),
    /** Sorted by route ({@code 9}). */
    SORTED_BY_ROUTE(9),
    /** Route started ({@code 10}). */
    ROUTE_STARTED(10),
    /** Failed ({@code 11}). */
    FAILED(11),
    /** Skipped ({@code 12}). */
    SKIPPED(12),
    /** Done ({@code 13}). */
    DONE(13),
    /** Cancelled ({@code 14}). */
    CANCELLED(14),
    /** Scheduled ({@code 15}). */
    SCHEDULED(15);

    private final int value;
    private static final Map<Integer, OrderStatus> LOOKUP
            = new HashMap<>();

    static {
        for (Iterator<OrderStatus> it = EnumSet.allOf(OrderStatus.class).iterator(); it.hasNext();) {
            OrderStatus s = it.next();
            LOOKUP.put(s.getValue(), s);
        }
    }

    OrderStatus(int value) {
        this.value = value;
    }

    /**
     * Returns the status code.
     *
     * @return the code, such as {@code 0} for {@link #NEW}
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the status of a code.
     *
     * @param code the status code
     * @return the status, or {@code null} for an unknown code
     */
    public static OrderStatus get(int code) {
        return LOOKUP.get(code);
    }

}
