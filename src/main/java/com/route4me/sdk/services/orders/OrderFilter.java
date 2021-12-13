/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Route4Me
 */

@Data
class OrderFilter {
    @QueryParameter("display")
    @SerializedName("display")
    private String display;
    @SerializedName("scheduled_for_YYMMDD")
    @QueryParameter("scheduled_for_YYMMDD")
    private List<String> scheduled_for_YYMMDD;
    @SerializedName("terms")
    @QueryParameter("terms")
    private Map<String, Object> terms;
    @SerializedName("statuses")
    @QueryParameter("statuses")
    private List<Integer> statuses;
    @SerializedName("tracking_numbers")
    @QueryParameter("tracking_numbers")
    private List<String> trackingNumbers;   
}
