/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class ServiceTimeRules {

    @SerializedName("first_item_mode")
    @QueryParameter("first_item_mode")
    private Integer firstItemMode;
    @SerializedName("first_item_mode_params")
    @QueryParameter("first_item_mode_params")
    private List<Integer> firstItemModeParams;
    @SerializedName("additional_items_mode")
    @QueryParameter("additional_items_mode")
    private Integer additionalItemsMode;
    @SerializedName("additional_items_mode_params")
    @QueryParameter("additional_items_mode_params")
    private List<Integer> additionalItemsModeParams;

    
}
