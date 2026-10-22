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

/** How the service time of a bundle is computed, in {@link Bundling}. */
@Data
public class ServiceTimeRules {

    /** Mode of a first item of the bundled addresses ({@code first_item_mode}). */
    @SerializedName("first_item_mode")
    @QueryParameter("first_item_mode")
    private Integer firstItemMode;
    /**
     * The custom service time of the first stop, in seconds, for first item mode 2 ({@code first_item_mode_params}).
     */
    @SerializedName("first_item_mode_params")
    @QueryParameter("first_item_mode_params")
    private List<Integer> firstItemModeParams;
    /** Mode of the non-first items of the bundled addresses ({@code additional_items_mode}). */
    @SerializedName("additional_items_mode")
    @QueryParameter("additional_items_mode")
    private Integer additionalItemsMode;
    /**
     * The custom service time of the other stops, in seconds, for additional items mode 2 ({@code
     * additional_items_mode_params}).
     */
    @SerializedName("additional_items_mode_params")
    @QueryParameter("additional_items_mode_params")
    private List<Integer> additionalItemsModeParams;

}
