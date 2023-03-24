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

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author route4me
 */
@Data
public class DynamicInsert {

    @SerializedName("route_info_server")
    @QueryParameter("route_info_server")
    private String routeInfoServer;
    @SerializedName("scheduled_for")
    @QueryParameter("scheduled_for")
    private String scheduledFor;
    @SerializedName("insert_mode")
    @QueryParameter("insert_mode")
    private String insertMode;
    @SerializedName("lat")
    @QueryParameter("lat")
    private Double latitude;
    @SerializedName("lng")
    @QueryParameter("lng")
    private Double longitude;
    @SerializedName("lookup_results_limit")
    @QueryParameter("lookup_results_limit")
    private Integer lookupResultsLimit;
    @SerializedName("recommend_by")
    @QueryParameter("recommend_by")
    private String recommendBy;
    @SerializedName("max_increase_percent_allowed")
    @QueryParameter("max_increase_percent_allowed")
    private Integer maxIncreasePercentAllowed;
    @SerializedName("route_ids")
    @QueryParameter("route_ids")
    private List<String> routeIDs;

}
