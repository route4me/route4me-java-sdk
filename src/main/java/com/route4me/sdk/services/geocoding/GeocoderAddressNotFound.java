package com.route4me.sdk.services.geocoding;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** The answer of the geocoder for an address it cannot find. */
@Data
public class GeocoderAddressNotFound {

    /** The status of the answer ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The number of results. The API spells the key {@code resutls}; {@code results} is accepted too. */
    @SerializedName(value = "resutls", alternate = {"results"})
    private Integer results;


}
