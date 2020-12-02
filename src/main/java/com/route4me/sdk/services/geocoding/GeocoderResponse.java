/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

@Data
public class GeocoderResponse {

    @SerializedName("optimization_problem_id")
    @QueryParameter("optimization_problem_id")
    private String optimizationProblemID;
    @SerializedName("temporary_addresses_storage_id")
    @QueryParameter("temporary_addresses_storage_id")
    private String temporaryAddressesStorageID;
    @SerializedName("address_count")
    @QueryParameter("address_count")
    private Integer addressCount;
    @SerializedName("status")
    @QueryParameter("status")
    private Boolean status;   
}
