/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */
@Data
public class TerritoryAllocated {
    @SerializedName("address")
    private String address;
    @SerializedName("original")
    private String orginal;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;    
    @SerializedName("confidence")
    private String confidence;
    @SerializedName("type")
    private String type;
    @SerializedName("territories")
    public List<Territory> territories;

}
