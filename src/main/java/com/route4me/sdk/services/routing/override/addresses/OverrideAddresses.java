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
package com.route4me.sdk.services.routing.override.addresses;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** Values that replace those of every address of an optimization ({@code override_addresses}). */
@Data
public class OverrideAddresses {

    /** The service time to give every address of the route, in seconds ({@code time}). */
    @QueryParameter("time")
    @SerializedName("time")
    private Integer time;
    /**
     * The type of stop that this is (DELIVERY,PICKUP,BREAK,MEETUP,SERVICE,VISIT,DRIVEBY,REFUEL) ({@code
     * address_stop_type}).
     */
    @QueryParameter("address_stop_type")
    @SerializedName("address_stop_type")
    private String addressStopType;
    /** The address group ({@code group}). */
    @QueryParameter("group")
    @SerializedName("group")
    private String group;

}
