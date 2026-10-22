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
import lombok.Data;

/**
 *
 * @author route4me
 */

/** The position of a page of a {@link RouteSearchResult}. */
@Data
public class RouteSearchMeta {

    /** Current page number ({@code current_page}). */
    @QueryParameter("current_page")
    @SerializedName("current_page")
    private Integer currentPage;
    /** The position of the first route of this page ({@code from}). */
    @QueryParameter("from")
    @SerializedName("from")
    private Integer from;
    /** The number of the last page ({@code last_page}). */
    @QueryParameter("last_page")
    @SerializedName("last_page")
    private Integer lastPage;
    /** The URL of the search, without the page parameters ({@code path}). */
    @QueryParameter("path")
    @SerializedName("path")
    private String path;
    /** The number of routes per page ({@code per_page}). */
    @QueryParameter("per_page")
    @SerializedName("per_page")
    private Integer perPage;
    /** The position of the last route of this page ({@code to}). */
    @QueryParameter("to")
    @SerializedName("to")
    private Integer to;
    /** The number of routes in all the pages ({@code total}). */
    @QueryParameter("total")
    @SerializedName("total")
    private Integer total;



}
