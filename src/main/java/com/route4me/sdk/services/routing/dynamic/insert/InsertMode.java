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
package com.route4me.sdk.services.routing.dynamic.insert;

/** Where a new destination may go in a route; {@code toString()} and {@link #getValue()} give the API value. */
public enum InsertMode {
    /** Wherever it fits best ({@code optimal_anywhere}). */
    OPTIMAL_ANYWHERE("optimal_anywhere"),
    /** Wherever it fits best after the last visited stop ({@code optimal_after_last_visited}). */
    OPTIMAL_AFTER_LAST_VISITED("optimal_after_last_visited"),
    /** At the end of the route ({@code end_of_route}). */
    END_OF_ROUTE("end_of_route"),
    /** At the beginning of the route ({@code beginning_of_route}). */
    BEGINNING_OF_ROUTE("beginning_of_route");

    private final String value;

    InsertMode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Returns the API value of the mode.
     *
     * @return the value, such as {@code end_of_route}
     */
    public String getValue() {
        return value;
    }

}
