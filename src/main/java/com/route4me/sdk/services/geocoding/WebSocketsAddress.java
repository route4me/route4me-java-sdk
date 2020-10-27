/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.routing.Address;
import lombok.Data;

@Data
public class WebSocketsAddress {
    
    @SerializedName("address")
    private Address address;

    
}
