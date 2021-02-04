package com.route4me.sdk.services.geocoding;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@Data
public class GeocoderAddressNotFound {
    
    @SerializedName("status")
    String status;
    
    @SerializedName("resutls")
    Integer results;
   
    
}
