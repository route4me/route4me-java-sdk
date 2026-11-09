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
package com.route4me.sdk.services.routing.search;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/** The body of a route search on the V5.1 route API. */
@Data
public class RouteSearch {

    /** The filters of the search ({@code filters}). */
    @QueryParameter("filters")
    @SerializedName("filters")
    private RouteSearchFilter filters;
    /**
     * The sort order, as [field, direction] pairs, such as {@code [["route_created_unix", "desc"]]} ({@code order_by}).
     */
    @QueryParameter("order_by")
    @SerializedName("order_by")
    private List<List<String>> orderBy;
    /** The time zone of the dates of the search, such as {@code America/New_York} ({@code timezone}). */
    @QueryParameter("timezone")
    @SerializedName("timezone")
    private String timeZone;
    /** Page number (1-based). Defaults to 1 ({@code page}). */
    @QueryParameter("page")
    @SerializedName("page")
    private Integer page;
    /** Number of items per page (1–500) ({@code per_page}). */
    @QueryParameter("per_page")
    @SerializedName("per_page")
    private Integer perPage;

}
