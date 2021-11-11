/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.orders;

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
    private String display;
    @QueryParameter("scheduled_for_YYMMDD")
    private List<String> scheduled_for_YYMMDD;
    @QueryParameter("terms")
    private Map<String, Object> terms;
    
    
    

    
    
    
}
