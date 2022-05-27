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

@Data
public class RouteSearchMeta {

    @QueryParameter("current_page")
    @SerializedName("current_page")
    private Integer currentPage;
    @QueryParameter("from")
    @SerializedName("from")
    private Integer from;
    @QueryParameter("last_page")
    @SerializedName("last_page")
    private Integer lastPage;
    @QueryParameter("path")
    @SerializedName("path")
    private String path;
    @QueryParameter("per_page")
    @SerializedName("per_page")
    private Integer perPage;
    @QueryParameter("to")
    @SerializedName("to")
    private Integer to;
    @QueryParameter("total")
    @SerializedName("total")
    private Integer total;


    
}
