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

/**
 *
 * @author Route4Me
 */
public enum OrderStatus {

    NEW(0),
    INBOUND_SCAN(1),
    SORTED_BY_TERRITORY(2),
    LOADED(3),
    MISSING(4),
    DAMAGED(5),
    MANUALLY_LOADED(6),
    ROUTED(7),
    UNROUTED(8),
    SORTED_BY_ROUTE(9),
    ROUTE_STARTED(10),
    FAILED(11),
    SKIPPED(12),
    DONE(13),
    CANCELLED(14),
    SCHEDULED(15);

    private int value;
    private static final Map<Integer, OrderStatus> lookup
            = new HashMap<>();

    static {
        for (Iterator<OrderStatus> it = EnumSet.allOf(OrderStatus.class).iterator(); it.hasNext();) {
            OrderStatus s = it.next();
            lookup.put(s.getValue(), s);
        }
    }

    private OrderStatus(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    public static OrderStatus get(int code) {
        return lookup.get(code);
    }

}
